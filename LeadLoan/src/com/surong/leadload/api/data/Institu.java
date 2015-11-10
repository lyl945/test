package com.surong.leadload.api.data;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**��JavaBean*/
public class Institu {

	private String instituteId;
	private String instituteName;


	public String getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(String instituteId) {
		this.instituteId = instituteId;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}


	public Institu(String instituteId, String instituteName) {
		super();
		this.instituteId = instituteId;
		this.instituteName = instituteName;
	}

	public Institu() {
		// TODO Auto-generated constructor stub
	}

	public static List<Institu> parse(String data){
		Type type = new TypeToken<List<Institu>>() {
		}.getType();
		Gson gson = new Gson();
		List<Institu> members = gson.fromJson(data, type);
		return members;
	}

}
