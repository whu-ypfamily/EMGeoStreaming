package cn.whu.ypfamily.EMGeoStreaming.action

import java.sql.{DriverManager, ResultSet}

import cn.whu.ypfamily.EMGeoStreaming.core.operator.{ObservationRDDOperator, Operator, OutputOperator}
import cn.whu.ypfamily.EMGeoStreaming.core.{EventModel, Observation, ObservationRDD}
import cn.whu.ypfamily.EMGeoStreaming.entities.{Region, WeatherData}
import com.google.gson.Gson
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.ObservationStream

object WaterloggingGeoStreaming {

  def main(args: Array[String]): Unit = {

    // get input parameters
    if (args.length < 6) {
      println("Usage " +
        "<master URL of Spark> " +
        "<URL of Kafka> " +
        "<URL of postgresql database> " +
        "<Account of postgresql database> " +
        "<Password of postgresql database> ")
      return
    }
    val masterUrl = args(0).toString
    val kafkaUrl = args(1).toString
    val dbUrl = args(2).toString
    val dbAcc = args(3).toString
    val dbPsw = args(4).toString

    /** ****************** Get basic data from the database ********************/
    // Connect database
    Class.forName("org.postgresql.Driver")
    val connInDriver = DriverManager.getConnection(dbUrl, dbAcc, dbPsw)
    val stateInDriver = connInDriver.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
    // Get metadata of the DEM
    val rsDemInfo = stateInDriver.executeQuery(
      "SELECT (ST_SummaryStats(rast)).min, " +
        "(ST_SummaryStats(rast)).max, " +
        "(ST_MetaData(rast)).scalex, " +
        "(ST_MetaData(rast)).scaley " +
        "FROM public.dem"
    )
    rsDemInfo.next()
    val minElev = rsDemInfo.getDouble("min")
    val maxElev = rsDemInfo.getDouble("max")
    val scaleX = rsDemInfo.getDouble("scalex")
    val scaleY = rsDemInfo.getDouble("scaley")
    connInDriver.close()

    /** ****************** Init Spark Streaming application ********************/
    val sparkConf = new SparkConf().setAppName("WaterloggingGeoStreaming")
    val ssc = new StreamingContext(sparkConf, Seconds(60)) // batch interval is 60 seconds
    // get O-Stream from kafka
    val kafkaStream = {
      val kafkaParams = Map[String, Object](
        "bootstrap.servers" -> kafkaUrl,
        "key.deserializer" -> classOf[StringDeserializer],
        "value.deserializer" -> classOf[StringDeserializer],
        "group.id" -> "Waterlogging-Streaming",
        "auto.offset.reset" -> "latest",
        "enable.auto.commit" -> (false: java.lang.Boolean)
      )
      val topics = Array("waterlogging")
      KafkaUtils.createDirectStream[String, String](
        ssc,
        PreferConsistent,
        Subscribe[String, String](topics, kafkaParams)
      )
    }
    val observationStream = new ObservationStream[WeatherData](
      kafkaStream.map(record => {
        val gson = new Gson()
        gson.fromJson(record.value(), classOf[Observation[WeatherData]])
      })
    )

    /** ****************** compute DStream ********************/
    // calculate average precipitation of each region
    val operators = new Array[Operator](2)
    operators(0) = new ObservationRDDOperator[WeatherData](this.calcAveragePrecipitation)
    operators(1) = new OutputOperator[WeatherData](rdd => {
      rdd.foreachPartition { partition =>
        // Connect database
        val connInPartition = DriverManager.getConnection(dbUrl, dbAcc, dbPsw)
        val stateInPartition = connInPartition.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)
        // Update waterlogging information
        partition.foreach { observation =>
          // Get previous waterlogging information
          val rsWatrloggingPrev = stateInPartition.executeQuery(
            "SELECT * FROM waterlogging WHERE areaid = '" + observation.featureOfInterest.id + "' order by timestamp desc limit 1")
          rsWatrloggingPrev.next
          val nRowInRs = rsWatrloggingPrev.getRow
          val region = observation.featureOfInterest.asInstanceOf[Region]
          var sumDip: Double = region.outletnum * 15.0 / 1000 * 60
          var sumPcp: Double = observation.result.asInstanceOf[WeatherData].precipitation
          if (nRowInRs == 1) {
            sumDip = sumDip + rsWatrloggingPrev.getDouble("displacement") // Sum displacement
            sumPcp = sumPcp + rsWatrloggingPrev.getDouble("precipitation") // Sum precipitation
          }
          // Calculate water volume
          val runoff = scscnFunc(sumPcp, region.cn)
          var volWater: Double = runoff / 1000 * region.area - sumDip
          if (volWater < 0) {
            volWater = 0.0
          }
          // get DEM data
          val rsDemOfArea = stateInPartition.executeQuery(
            "SELECT ST_DumpValues(rast, 1, false) As dem " +
              "FROM demclip WHERE rid=" + region.id
          )
          rsDemOfArea.next()
          val arrDem = rsDemOfArea.getArray("dem").getArray.asInstanceOf[Array[Array[java.lang.Double]]]
          // calculate water elevation
          val elevWater = calcWaterElevationFunc(volWater, arrDem, minElev, maxElev, Math.abs(scaleX * scaleY))
          // insert information to the database
          stateInPartition.addBatch(
            "INSERT INTO waterlogging(" +
              "areaid, volume, elevation, runoff, displacement, precipitation, timestamp)" +
              "VALUES (" +
              region.id + ", " +
              volWater + ", " +
              elevWater + ", " +
              runoff + ", " +
              sumDip + ", " +
              sumPcp + ", " +
              "current_timestamp(0))"
          )
        }
        stateInPartition.executeBatch()
        connInPartition.close()
      }
    })
    observationStream.processEvent(new EventModel(operators))
    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * Sum 2 obervation
    *
    * @param observation1 o1
    * @param observation2 o2
    * @return
    */
  private def sumObservation(observation1: Observation[WeatherData],
                             observation2: Observation[WeatherData]): Observation[WeatherData] = {
    observation1.result.precipitation = observation1.result.precipitation + observation2.result.precipitation
    observation1
  }

  /**
    * Divide observation
    *
    * @param observation observation
    * @param num         number
    * @return
    */
  private def divideObservation(observation: Observation[WeatherData], num: Int): Observation[WeatherData] = {
    observation.result.precipitation = observation.result.precipitation / num
    observation
  }

  /**
    * Calculate average precipitation of each region
    *
    * @param observationRDD O-RDD
    * @return
    */
  private def calcAveragePrecipitation(observationRDD: ObservationRDD[WeatherData]): ObservationRDD[WeatherData] = {
    new ObservationRDD[WeatherData](
      observationRDD.map(observation => (observation.featureOfInterest.id, observation))
        .groupByKey()
        .map(record => {
          divideObservation(record._2.reduce(sumObservation(_, _)), record._2.count(record => true))
        })
    )
  }

  /**
    * SCS-CN model
    *
    * @param precipitation precipitation
    * @param cn            CN value
    * @return
    */
  private def scscnFunc(precipitation: Double, cn: Int): Double = {
    val P = precipitation
    val S = 25400 / cn - 254
    val I = 0.2 * S
    var runoff = 0.0
    if (P > I) {
      runoff = (P - I) * (P - I) / (P + S - I)
    }
    runoff
  }

  /**
    * Calculate water elevation with DEM data
    *
    * @param volume   waterlogging volume
    * @param dem      DEM data
    * @param elevMin  DEM min elevation
    * @param elevMax  DEM max elevation
    * @param areaUnit area of each DEM unit
    * @return
    */
  private def calcWaterElevationFunc(volume: Double,
                                     dem: Array[Array[java.lang.Double]],
                                     elevMin: Double,
                                     elevMax: Double,
                                     areaUnit: Double): Double = {
    var eLow = elevMin
    var eHigh = elevMax
    var eMid = (eLow + eHigh) / 2.0
    while (eHigh - eLow > 0.0001) {
      val vLow = volume - calcWaterVolumeFunc(eLow, dem, areaUnit)
      val vMid = volume - calcWaterVolumeFunc(eMid, dem, areaUnit)
      if (vLow * vMid <= 0.0) {
        eHigh = eMid
      } else {
        eLow = eMid
      }
      eMid = (eLow + eHigh) / 2.0
    }
    eMid
  }

  /**
    * Calculate watelogging volume with DEM data and water elevation
    *
    * @param elevW    water elevation
    * @param dem      DEM data
    * @param areaUnit area of each DEM unit
    * @return
    */
  private def calcWaterVolumeFunc(elevW: Double,
                                  dem: Array[Array[java.lang.Double]],
                                  areaUnit: Double): Double = {
    var V = 0.0
    dem.foreach { elevArr =>
      elevArr.foreach { elev =>
        if (elev > -9999) {
          val diff = elevW - elev
          if (diff > 0) {
            V += diff * areaUnit
          }
        }
      }
    }
    V
  }

}
