package cn.whu.ypfamily.sensorhubclient.entities;

import java.util.Arrays;

public class Sensor {
	private String sensorName;
	private String sensorId;
	private ObservationOffering obsOffering;
	private String longName;
	private String shortName;
	private String lat;
	private String lng;
	private String alt;
	private String[] outputs;
	
	public String getSensorName() {
		return sensorName;
	}
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	public String getSensorId() {
		return sensorId;
	}
	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public ObservationOffering getObsOffering() {
		return obsOffering;
	}
	public void setObsOffering(ObservationOffering obsOffering) {
		this.obsOffering = obsOffering;
	}
	public String[] getOutputs() {
		return outputs;
	}
	public void setOutputs(String[] outputs) {
		this.outputs = outputs;
	}
	
	@Override
	public String toString() {
		return "Sensor [sensorName=" + sensorName + ", sensorId=" + sensorId + ", obsOffering=" + obsOffering
				+ ", longName=" + longName + ", shortName=" + shortName + ", lat=" + lat + ", lng=" + lng + ", alt="
				+ alt + ", outputDef=" + Arrays.toString(outputs) + "]";
	}
}
