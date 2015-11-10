package com.surong.leadload.api.data;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class InstituResult {
	private List<Institu> instituteArray;
	private int typeId;
	private String typeName;
	public List<Institu> getInstituteArray() {
		return instituteArray;
	}
	public void setInstituteArray(List<Institu> instituteArray) {
		this.instituteArray = instituteArray;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public static List<InstituResult> parse(String json){
		Type type = new TypeToken<List<InstituResult>>() {
		}.getType();
		Gson gson = new Gson();
		List<InstituResult> members = gson.fromJson(json, type);
		return members;
	}
}
