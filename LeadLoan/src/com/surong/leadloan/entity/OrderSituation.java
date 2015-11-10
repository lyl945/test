package com.surong.leadloan.entity;

import java.io.Serializable;

import android.R.string;

public class OrderSituation implements Serializable {

	String followNote;
	String followTime;
	String status;

	public String getFollowNote() {
		return followNote;
	}

	public void setFollowNote(String followNote) {
		this.followNote = followNote;
	}

	public String getFollowTime() {
		return followTime;
	}

	public void setFollowTime(String followTime) {
		this.followTime = followTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
