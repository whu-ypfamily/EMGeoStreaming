package cn.whu.ypfamily.sensorhubclient.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	public static String getCurrentTime() {
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		return format.format(date);
	}
}
