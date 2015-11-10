package com.surong.leadloan.entity;

import java.io.Serializable;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String mobile;
	private String head;
	private boolean checked;
	private String  HeadImgPath;
	private String id;

	

	

	public String sortKey;
	private String institue;
	
	public String getHeadImgPath() {
		return HeadImgPath;
	}

	public void setHeadImgPath(String headImgPath) {
		HeadImgPath = headImgPath;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstitue() {
		return institue;
	}

	public void setInstitue(String institue) {
		this.institue = institue;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public void toggleCheck() {
		checked = !checked;

	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setHead(String h) {
		this.head = h;
	}

	public String getHead() {
		return this.head;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
