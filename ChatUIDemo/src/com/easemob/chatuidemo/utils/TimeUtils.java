package com.easemob.chatuidemo.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;

public class TimeUtils {
	public static String transformToTime1(long time) {
		Date date = new Date(time);
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static String transformToTime1(Date date) {
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static String transformToTime2(long time) {
		Date date = new Date(time);
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static String transformToTime2(Date date) {
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static String transformToTime3(long time) {
		Date date = new Date(time);
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MM.dd");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static String transformToTime4(long time) {
		Date date = new Date(time);
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy年");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static String transformToTime5(long time) {
		Date date = new Date(time);
		SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static String transformToTime6(long time) {
		Date date = new Date(time);
		SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static String transformToTime7(long time) {
		Date date = new Date(time);
		SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String timeString = mDateFormat.format(date);
		return timeString;
	}

	public static Calendar transformToCalender(String time) {
		Calendar calendar = Calendar.getInstance();
		try {
			SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = mDateFormat.parse(time);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}

	public static int getAge(long birthday) {
		int age = 0;
		Date date = new Date(birthday);
		try {
			age = getAge(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return age;
	}

	// java中计算年龄
	/** 计算年龄 */
	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}

		return age;
	}

	/**
	 * 
	 * @Title: transformToLong
	 * @author huangxiang
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param time HH:mm:ss
	 * @param @return 参数
	 * @return long 返回类型
	 * @throws
	 */
	public static long transformToSecond(String time) {
		if (TextUtils.isEmpty(time)) {
			return 0;
		}
		String[] times = time.split(":");
		long second = 0;
		for (int i = times.length - 1; i >= 0; i--) {
			if (i == times.length - 1) {
				int s = Integer.parseInt(times[i]);
				if (s > 0) {
					second += s;
				}
			} else if (i == times.length - 2) {
				int m = Integer.parseInt(times[i]);
				if (m > 0) {
					second += m * 60;
				}
			} else if (i == times.length - 3) {
				int h;
				h = Integer.parseInt(times[i]);
				if (h > 0) {
					second += h * 60 * 60;
				}
			}
		}
		int first = Integer.parseInt(times[0]);
		if (first < 0) {
			second = 0;
		}
		return second;
	}

	/**
	 * 
	 * @Title: tranformToString
	 * @author huangxiang
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param second
	 * @param @return 参数
	 * @return String 返回类型 xxx天xx小时xx分
	 * @throws
	 */
	public static String tranformToString(long second) {
		int m = 0;
		int h = 0;
		int d = 0;
		if (second >= 60) {
			m = (int) ((second / 60) % 60);
		}

		if (second >= 60 * 60) {
			h = (int) ((second / (60 * 60)) % 24);
		}

		if (second >= 60 * 60 * 24) {
			d = (int) (second / (60 * 60 * 24));
		}
		if (d > 0) {
			return (d + "天" + h + "小时" + m + "分");
		} else if (h > 0) {
			return (h + "小时" + m + "分");
		} else {
			return (m + "分");
		}
	}

	/**
	 * 
	 * @Title: tranformToString2
	 * @author huangxiang
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param second
	 * @param @return 参数
	 * @return String 返回类型 xxx:xx:xx (时：分：秒)
	 * @throws
	 */
	public static String tranformToString2(long second,
			DecimalFormat decimalFormat) {
		int s = 0;
		int m = 0;
		int h = 0;
		s = (int) (second % 60);

		if (second >= 60) {
			m = (int) ((second / 60) % 60);
		}

		if (second >= 60 * 60) {
			h = (int) (second / (60 * 60));
		}
		return (decimalFormat.format(h) + ":" + decimalFormat.format(m) + ":" + decimalFormat
				.format(s));
	}

}
