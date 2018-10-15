package cn.whu.ypfamily.EMGeoStreaming.entities

import cn.whu.ypfamily.EMGeoStreaming.core.basic.TM_Object

class WeatherData extends Serializable {
  var time: TM_Object = _
  var precipitation: Double = 0.0
}
