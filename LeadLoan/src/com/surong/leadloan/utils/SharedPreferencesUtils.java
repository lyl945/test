package com.surong.leadloan.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;
	// sharedpreference文件名
	public static String PREFERENCE_DATA_NAME = "template_prefs";

	public static String PREFERENCE_IS_FIRST = "is_first";// 是否是第一次开启APP
	public static String PREFERENCE_LOGIN_ACCOUNT = "account";
	public static String PREFERENCE_LOGIN_PASSWORD = "password";
	public static String PREFERENCE_IS_LOGIN = "is_login";
	public static String PREFERENCE_LOCK_PATTERN_KEY = "lock_pattern_key";
	

	/**
	 * 构造函数，实例化sharedPreferences, sharedpreferences.Editor
	 * 
	 * @param context
	 * @param preferenceName
	 *            要操作xml的文件名，e.g. PREFERENCE_DATA_NAME
	 */
	public SharedPreferencesUtils(Context context, String preferenceDataName) {
		mPreferences = context.getSharedPreferences(preferenceDataName,
				Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	public SharedPreferencesUtils(Context context) {
		mPreferences = context.getSharedPreferences(PREFERENCE_DATA_NAME,
				Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	/**
	 * 获取 preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param defValue
	 *            默认值
	 * @return
	 */
	public String getPreferenceValue(String preferenceKey, String defValue) {
		return mPreferences.getString(preferenceKey, defValue);
	}

	/**
	 * 获取 preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param defValue
	 *            默认值
	 * @return
	 */
	public int getPreferenceValue(String preferenceKey, int defValue) {
		return mPreferences.getInt(preferenceKey, defValue);
	}

	/**
	 * 获取 preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param defValue
	 *            默认值
	 * @return
	 */
	public float getPreferenceValue(String preferenceKey, float defValue) {
		return mPreferences.getFloat(preferenceKey, defValue);
	}

	/**
	 * 获取 preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param defValue
	 *            默认值
	 * @return
	 */
	public long getPreferenceValue(String preferenceKey, long defValue) {
		return mPreferences.getLong(preferenceKey, defValue);
	}

	/**
	 * 获取 preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param defValue
	 *            默认值
	 * @return
	 */
	public boolean getPreferenceValue(String preferenceKey, boolean defValue) {
		return mPreferences.getBoolean(preferenceKey, defValue);
	}

	/**
	 * 设置preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param value
	 *            默认值值
	 * @return
	 */
	public boolean setPreferenceValue(String preferenceKey, String value) {
		return mEditor.putString(preferenceKey, value).commit();
	}

	/**
	 * 设置preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param value
	 *            默认值值
	 * @return
	 */
	public boolean setPreferenceValue(String preferenceKey, int value) {
		return mEditor.putInt(preferenceKey, value).commit();
	}

	/**
	 * 设置preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param value
	 *            默认值值
	 * @return
	 */
	public boolean setPreferenceValue(String preferenceKey, boolean value) {
		return mEditor.putBoolean(preferenceKey, value).commit();
	}

	/**
	 * 设置preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param value
	 *            默认值值
	 * @return
	 */
	public boolean setPreferenceValue(String preferenceKey, float value) {
		return mEditor.putFloat(preferenceKey, value).commit();
	}

	/**
	 * 设置preferenceKey的值
	 * 
	 * @param preferenceKey
	 * @param value
	 *            默认值值
	 * @return
	 */
	public boolean setPreferenceValue(String preferenceKey, long value) {
		return mEditor.putLong(preferenceKey, value).commit();
	}
}
