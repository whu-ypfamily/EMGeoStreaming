package cn.whu.ypfamily.EMGeoStreaming.core.operator

import cn.whu.ypfamily.EMGeoStreaming.core.ObservationRDD

class OutputOperator[T](_f: ObservationRDD[T] => Unit) extends Operator {
  val f: ObservationRDD[T] => Unit = _f
}
