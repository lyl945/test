package com.surong.leadloan.entity;

/** 手机通讯录匹配返回结果Javabean */
public class MobileSoftware {

	// 手机号名
	private String phoneName;
	// 手机号码
	private String phoneNum;
	// 等待用户另答
	private int state;

	private String sortLetters; // 显示数据拼音的首字母

	private boolean checked;

	public boolean isChecked() {
		return checked;
	}

	public void toggleCheck() {
		checked = !checked;

	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

}
