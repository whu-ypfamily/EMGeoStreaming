package cn.whu.ypfamily.EMGeoStreaming.core.operator

import cn.whu.ypfamily.EMGeoStreaming.core.Observation

class FilterOperator[T](_f: Observation[T] => Boolean) extends Operator {
  val f: Observation[T] => Boolean = _f
}
