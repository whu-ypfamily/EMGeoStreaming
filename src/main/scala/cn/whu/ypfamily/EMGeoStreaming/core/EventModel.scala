package cn.whu.ypfamily.EMGeoStreaming.core

import cn.whu.ypfamily.EMGeoStreaming.core.operator.Operator

class EventModel(_eventOperators: Array[Operator]) {
  val eventOperators: Array[Operator] = _eventOperators
}
