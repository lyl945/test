package com.surong.leadloan.entity;

import java.io.Serializable;

/**
 * ʡ��
 * 
 * @author �޽�
 */
public class Province implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String proCode;
	private String ProName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getProName() {
		return ProName;
	}

	public void setProName(String proName) {
		ProName = proName;
	}

	public Province(String proCode, String proName) {
		super();
		this.proCode = proCode;
		ProName = proName;
	}

	public Province() {
		super();
		// TODO Auto-generated constructor stub
	}

}
