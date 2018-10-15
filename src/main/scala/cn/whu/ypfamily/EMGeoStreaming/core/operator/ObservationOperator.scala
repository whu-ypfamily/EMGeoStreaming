package cn.whu.ypfamily.EMGeoStreaming.core.operator

import cn.whu.ypfamily.EMGeoStreaming.core.Observation

class ObservationOperator[T](_f: Observation[T] => Observation[T]) extends Operator {
  val f: Observation[T] => Observation[T] = _f
}
