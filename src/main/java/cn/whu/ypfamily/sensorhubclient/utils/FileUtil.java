package cn.whu.ypfamily.sensorhubclient.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	
	public static byte[] read(File bFile) throws IOException {
		BufferedInputStream bf = new BufferedInputStream(
				new FileInputStream(bFile));
		try {
			byte[] data = new byte[bf.available()];
			bf.read(data);
			return data;
		} finally {
			bf.close();
		}
	}
	
	public static byte[] read(String bFile) throws IOException {
		return read(new File(bFile).getAbsoluteFile());
	}
	
	public static void write(File bFile, byte[] bt) throws IOException {
		BufferedOutputStream bf = new BufferedOutputStream(
				new FileOutputStream(bFile));
		try {
			bf.write(bt);
		} finally {
			bf.close();
		}
	}
	
	public static void write(String bFile, byte[] bt) throws IOException {
		write(new File(bFile).getAbsoluteFile(), bt);
	}
}
