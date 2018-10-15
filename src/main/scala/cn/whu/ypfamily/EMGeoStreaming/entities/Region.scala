package cn.whu.ypfamily.EMGeoStreaming.entities

import cn.whu.ypfamily.EMGeoStreaming.core.basic.GFI_Feature

class Region(_id: String) extends GFI_Feature(_id) with Serializable {
  var name: String = ""
  var area: Double = 0.0
  var landtype: String = ""
  var cn: Int = 0
  var outletnum: Int = 0
  var shapewkt = ""
}
