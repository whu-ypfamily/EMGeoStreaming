package cn.whu.ypfamily.EMGeoStreaming.entities

import cn.whu.ypfamily.EMGeoStreaming.core.basic.OM_Process

class Sensor(_id: String) extends OM_Process(_id) with Serializable {
  var sensorId: String = ""
  var sensorName: String = ""
  var longName: String = ""
  var shortName: String = ""
  var lat: Double = 0.0
  var lng: Double = 0.0
  var alt: Double = 0.0
}
