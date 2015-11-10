package com.surong.leadloan.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.content.Context;

public class FileUtils {

	public FileUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �����ֵ���Ϣ
	 * @param context
	 * @param file :�ļ�·��
	 * @param properties :������Ϣproperties
	 * */
	public static void saveConfig(Context context, String file,
			Properties properties) {
		try {
			FileOutputStream s = new FileOutputStream(file, false);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�ֵ���Ϣ
	 * @param context
	 * @param file :�ļ�·��
	 * */
	public static Properties readConfig(Context context,String file){
		Properties properties=null;
		try {
			File file2=new File(file);
			if(file2.exists()){
				properties=new Properties();
				FileInputStream fileInputStream=new FileInputStream(file);
				properties.load(fileInputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

}
