package com.surong.leadload.api.data;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CityArray implements Serializable{
	private	String id;
	private String name;
	private boolean isSelect;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static List<CityArray> parse(String data){
		Type type = new TypeToken<List<CityArray>>() {
		}.getType();
		Gson gson = new Gson();
		List<CityArray> city = gson.fromJson(data, type);
		return city;
	}
	
}
