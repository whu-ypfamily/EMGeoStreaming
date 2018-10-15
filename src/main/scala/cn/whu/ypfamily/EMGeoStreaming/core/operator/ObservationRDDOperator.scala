package cn.whu.ypfamily.EMGeoStreaming.core.operator

import cn.whu.ypfamily.EMGeoStreaming.core.ObservationRDD

class ObservationRDDOperator[T](_f: ObservationRDD[T] => ObservationRDD[T]) extends Operator {
  val f: ObservationRDD[T] => ObservationRDD[T] = _f
}
