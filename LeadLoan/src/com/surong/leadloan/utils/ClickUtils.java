package com.surong.leadloan.utils;


/**
 * 防止暴力点击
 * 
 * @author hedongsheng
 * 
 */
public class ClickUtils {
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		// Log.e("ClickUtils", "time = " + time + " lastClickTime = "
		// + lastClickTime + " intervel = " + (time - lastClickTime));
		if (time < lastClickTime) {// 防止调手机时间到过去导至点击失效
			lastClickTime = 0;
		}
		if (time - lastClickTime < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
