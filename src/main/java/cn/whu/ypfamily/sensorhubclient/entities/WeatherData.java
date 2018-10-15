package cn.whu.ypfamily.sensorhubclient.entities;

public class WeatherData {
	private String sensorId;
	private String precipitation;
	
	public String getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}
	public String getSensorId() {
		return sensorId;
	}
	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}
	
	@Override
	public String toString() {
		return "WeatherData [sensorId=" + sensorId + ", precipitation=" + precipitation + "]";
	}
}
