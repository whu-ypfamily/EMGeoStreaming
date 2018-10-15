package cn.whu.ypfamily.EMGeoStreaming.core

import cn.whu.ypfamily.EMGeoStreaming.core.operator.{FilterOperator, Operator}

class ObservationFilter[T](_filterOperator: FilterOperator[T]) {
  val filterOperator: FilterOperator[T] = _filterOperator
}
