package com.surong.leadloan.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lidroid.xutils.DbUtils;
import com.surong.leadloan.entity.City;
import com.surong.leadloan.entity.Institu;
import com.surong.leadloan.entity.Province;

public class SpinnerCityAnalyze {

	/**
	 * 解析省市json
	 */
	public static void analyzeCityJson(final JSONObject ob, final DbUtils db) {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		try {
			db.dropTable(Province.class);
			db.dropTable(City.class);
			String province = ob.getString("provinceArray");
			JSONArray provinceArray = new JSONArray(province);
			for (int i = 0; i < provinceArray.length(); i++) {
				JSONObject provinceOb = provinceArray.getJSONObject(i);
				String provinceId = provinceOb.getString("id");
				String provinceName = provinceOb.getString("name");
				String city = provinceOb.getString("cityArray");
				JSONArray cityArray = new JSONArray(city);
				for (int j = 0; j < cityArray.length(); j++) {
					JSONObject cityOb = cityArray.getJSONObject(j);
					String cityId = cityOb.getString("id");
					String cityName = cityOb.getString("name");
					City cy = new City(cityId, cityName, provinceId);
					db.save(cy);
				}
				Province item = new Province(provinceId, provinceName);
				db.save(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
		// }).start();
	}

	/*
	 * 解析机构json
	 */
	public static void analyzeInstituteJson(final JSONObject ob,
			final DbUtils db) {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		try {
			db.dropTable(Institu.class);
			String province = ob.getString("typeArray");
			JSONArray institutArray = new JSONArray(province);
			for (int i = 0; i < institutArray.length(); i++) {
				JSONObject intituType = institutArray.getJSONObject(i);
				String typeId = intituType.getString("typeId");
				String typeName = intituType.getString("typeName");
				String institute = intituType.getString("instituteArray");
				JSONArray instiuNameArray = new JSONArray(institute);
				for (int j = 0; j < instiuNameArray.length(); j++) {
					JSONObject institutOb = instiuNameArray.getJSONObject(j);
					String instituteId = institutOb.getString("instituteId");
					String instituteName = institutOb
							.getString("instituteName");
					Institu institu = new Institu(instituteId, instituteName,
							typeId, typeName);
					db.save(institu);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
		// }).start();
	}

	/**
	 * 解析数据字典
	 */
	public static Map<String, List<String>> analyzeDictionaryJson(JSONObject ob) {
		Map<String, List<String>> dictionaryMap = new HashMap<String, List<String>>();
		try {
			String institute = ob.getString("instituteArray");
			JSONArray instituteArray = new JSONArray(institute);
			for (int i = 0; i < instituteArray.length(); i++) {
				JSONObject object = instituteArray.getJSONObject(i);
				String type = object.getString("type");
				String Array = object.getString("typeArray");
				JSONArray typeArray = new JSONArray(Array);
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < typeArray.length(); j++) {
					JSONObject mapObject = typeArray.getJSONObject(j);
					String label = mapObject.getString("label");
					list.add(label);
				}
				dictionaryMap.put(type, list);
			}
		} catch (Exception e) {
		}
		return dictionaryMap;
	}

	/**
	 * 解析数据字典
	 */
	public static Map<String, Map<String, String>> analyzeDictionaryJson2(
			JSONObject ob) {
		Map<String, Map<String, String>> dictionaryMap = new HashMap<String, Map<String, String>>();
		try {
			String institute = ob.getString("instituteArray");
			JSONArray instituteArray = new JSONArray(institute);
			for (int i = 0; i < instituteArray.length(); i++) {
				JSONObject object = instituteArray.getJSONObject(i);
				String type = object.getString("type");
				String Array = object.getString("typeArray");
				JSONArray typeArray = new JSONArray(Array);
				// List<String> list = new ArrayList<String>();
				Map<String, String> map = new LinkedHashMap<String, String>();
				for (int j = 0; j < typeArray.length(); j++) {
					JSONObject mapObject = typeArray.getJSONObject(j);
					String label = mapObject.getString("label");// 文字
					String val = mapObject.getString("val");// 数字
					map.put(label, val);
				}
				dictionaryMap.put(type, map);
			}
		} catch (Exception e) {
		}
		return dictionaryMap;
	}
}
