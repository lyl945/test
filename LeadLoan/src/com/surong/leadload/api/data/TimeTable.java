package com.surong.leadload.api.data;

import java.io.Serializable;

public class TimeTable implements Serializable{
	private String operateType;
	private String operateTime;
	private String amount;
	private String reasonDesc;
	public TimeTable(String operateType, String operateTime, String amount,
			String reasonDesc) {
		super();
		this.operateType = operateType;
		this.operateTime = operateTime;
		this.amount = amount;
		this.reasonDesc = reasonDesc;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getreasonDesc() {
		return reasonDesc;
	}
	public void setreasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	

}
