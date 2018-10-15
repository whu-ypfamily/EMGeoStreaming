package cn.whu.ypfamily.EMGeoStreaming.core

import cn.whu.ypfamily.EMGeoStreaming.core.basic.TM_Object
import cn.whu.ypfamily.EMGeoStreaming.core.operator.{ObservationOperator, ObservationRDDOperator}
import org.apache.spark.{Partition, TaskContext}
import org.apache.spark.rdd.RDD

class ObservationRDD[T](val rddPrev: RDD[Observation[T]])
  extends RDD[Observation[T]](rddPrev) {

  var fromTime: TM_Object = _
  var untilTime: TM_Object = _

  override def compute(split: Partition, context: TaskContext): Iterator[Observation[T]] = {
    rddPrev.iterator(split, context)
  }

  override protected def getPartitions: Array[Partition] = rddPrev.partitions

  def countObservations(): Long = {
    this.count()
  }

  /**
    * Filter computing of O-RDD
    * @param observationFilter O-Filter
    * @return
    */
  def filterObservation(observationFilter: ObservationFilter[T]): ObservationRDD[T] = {
    new ObservationRDD[T](
      this.filter(observation => observationFilter.filterOperator.f(observation))
    )
  }

  /**
    * Event computing of O-RDD
    * @param eventModel event model
    * @return
    */
  def processEvent(eventModel: EventModel): ObservationRDD[T] = {
    var rdd: ObservationRDD[T] = this
    eventModel.eventOperators.foreach(operator => {
      if (operator.isInstanceOf[ObservationRDDOperator[T]]) {
        rdd = operator.asInstanceOf[ObservationRDDOperator[T]].f(rdd)
      } else if (operator.isInstanceOf[ObservationOperator[T]]) {
        rdd = new ObservationRDD[T](rdd.map(operator.asInstanceOf[ObservationOperator[T]].f))
      }
    })
    rdd
  }
}
