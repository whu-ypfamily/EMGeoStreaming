package org.apache.spark.streaming.dstream

import cn.whu.ypfamily.EMGeoStreaming.core.{EventModel, Observation, ObservationFilter, ObservationRDD}
import org.apache.spark.streaming.{Duration, Time}

class ObservationStream[T](parent: DStream[Observation[T]])
  extends DStream[Observation[T]](parent.ssc) {

  override def dependencies: List[DStream[_]] = List(parent)

  override def slideDuration: Duration = parent.slideDuration

  override def compute(validTime: Time): Option[ObservationRDD[T]] = {
    Some(new ObservationRDD[T](parent.getOrCompute(validTime).get))
  }

  /**
    * Filter computing of O-Stream
    * @param observationFilter O-Filter
    * @return
    */
  def filterObservation(observationFilter: ObservationFilter[T]): ObservationStream[T] = {
    new ObservationStream[T](
      this.transform(rdd =>
        rdd.asInstanceOf[ObservationRDD[T]].filterObservation(observationFilter)
      )
    )
  }

  /**
    * Event computing of O-Stream
    * @param eventModel event model
    * @return
    */
  def processEvent(eventModel: EventModel): ObservationStream[T] = {
    new ObservationStream[T](
      this.transform(rdd =>
        rdd.asInstanceOf[ObservationRDD[T]].processEvent(eventModel)
      )
    )
  }

}
