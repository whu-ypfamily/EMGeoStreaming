package cn.whu.ypfamily.EMGeoStreaming.core

import cn.whu.ypfamily.EMGeoStreaming.core.basic.{GFI_Feature, OM_Process, TM_Object}

class Observation[T] (_result: T, _phenomenonTime: TM_Object,
                      _featureOfInterest: GFI_Feature, _procedure: OM_Process) extends Serializable {
  val result: T = _result
  val phenomenonTime: TM_Object = _phenomenonTime
  val featureOfInterest: GFI_Feature = _featureOfInterest
  val procedure: OM_Process = _procedure
}
