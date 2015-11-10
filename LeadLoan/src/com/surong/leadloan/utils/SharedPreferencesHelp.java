package com.surong.leadloan.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelp {

	/*
	 * 保存long
	 */
	public static void SavaLong(Context context, String key, long value) {
		try {
			SharedPreferences params = context.getSharedPreferences(
					"linkGroup", 0);
			params.edit().putLong(key, value).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 保存long
	 */
	public static void SavaString(Context context, String key, String value) {
		try {
			SharedPreferences params = context.getSharedPreferences(
					"linkGroup", 0);
			params.edit().putString(key, value).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/*
	 * 根据手机号码保存String
	 */
	public static void SavaFindString(Context context, String key, String value) {
		try {
			String link = SharedPreferencesHelp.getString(context, "phone");
			SharedPreferences params = context.getSharedPreferences(link, 0);
			params.edit().putString(key, value).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 根据手机号码获取String
	 */
	public static String getFindString(Context context, String key) {
		String object = "";
		String link = SharedPreferencesHelp.getString(context, "phone");
		try {
			SharedPreferences params = context.getSharedPreferences(link, 0);
			object = params.getString(key, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	/*
	 * 获取long
	 */
	public static String getString(Context context, String key) {
		String object = "";
		try {
			SharedPreferences params = context.getSharedPreferences(
					"linkGroup", 0);
			object = params.getString(key, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	public static String getToken(Context context) {
		String object = "";
		try {
			SharedPreferences params = context.getSharedPreferences(
					"linkGroup", 0);
			object = params.getString("token", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public static String getHead(Context context) {
	    String object = "";
	    try {
	        SharedPreferences params = context.getSharedPreferences(
	                "linkGroup", 0);
	        object = params.getString("head", null);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return object;
	}

	/*
	 * 获取long
	 */
	public static long GetLong(Context context, String key) {
		long object = 0;
		try {
			SharedPreferences params = context.getSharedPreferences(
					"linkGroup", 0);
			object = params.getLong(key, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public static boolean isFirst(Context context) {
	    boolean object = false;
	    try {
	        SharedPreferences params = context.getSharedPreferences(
	                "linkGroup", 0);
	        object = params.getBoolean("first", false);
	        if(object == false){
	            params.edit().putBoolean("first", true).commit();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return object;
	}
}

