package com.surong.leadloan.entity;

/**��JavaBean*/
public class Institu {

	private int id;
	private String instituteId;
	private String instituteName;
	private String shortName;
	private String typeId;
	private String typeName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Institu(String instituteId, String instituteName, String typeId,
			String typeName) {
		super();
		this.instituteId = instituteId;
		this.instituteName = instituteName;
		this.typeId = typeId;
		this.typeName = typeName;
	}

	public Institu() {
		// TODO Auto-generated constructor stub
	}

}
