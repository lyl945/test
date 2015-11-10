package com.surong.leadloan.entity;

import java.io.Serializable;

/*
 * ����ʵ����
 * @author �޽�
 */

public class City implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;// ��ݴ洢id���
	private String cityId;// ����id
	private String cityName;// �������
	private String provinceId;// ʡ��id
	private String ProvinceName;// ʡ�����

	public String getProvinceName() {
		return ProvinceName;
	}

	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public City(String cityId, String cityName, String provinceId) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.provinceId = provinceId;
	}

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

}
