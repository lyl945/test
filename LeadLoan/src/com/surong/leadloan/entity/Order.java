package com.surong.leadloan.entity;

import java.io.Serializable;

public class Order implements Serializable {
	String prodName;// 产品名称
	String acceptMgrId;// 信贷经理id
	String applyAmount;// 申请金额
	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	String applyPeriod;// 贷款期限
	String applyTime;// 申请时间
	String assessLevel;// 客户评价
	String custMobile;// 手机号
	String custName;// 客户名称
	String custType;// 客户类型
	String id;// 订单id
	String prodType;// 贷款类型
	String orderId;// 没有此字段表示订单来源于平台(推广订单)，有此字段表示转单（分为100转单、100领单）
	String serviceOrderCode;// 订单来源 01 未匹配订单 （100领单）02 信贷经理转单（100转单）
	String getStatus;// 订单来源
	String status;// 订单状态
	String recoDate;// 推荐日期
	private String sortLetters; // 显示数据拼音的首字母
	private String prodId;

	public String getRecoDate() {
		return recoDate;
	}

	public void setRecoDate(String recoDate) {
		this.recoDate = recoDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getState() {
		return status;
	}

	public void setState(String state) {
		this.status = state;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getAcceptMgrId() {
		return acceptMgrId;
	}

	public void setAcceptMgrId(String acceptMgrId) {
		this.acceptMgrId = acceptMgrId;
	}

	public String getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getApplyPeriod() {
		return applyPeriod;
	}

	public void setApplyPeriod(String applyPeriod) {
		this.applyPeriod = applyPeriod;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getAssessLevel() {
		return assessLevel;
	}

	public void setAssessLevel(String assessLevel) {
		this.assessLevel = assessLevel;
	}

	public String getCustMobile() {
		return custMobile;
	}

	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getServiceOrderCode() {
		return serviceOrderCode;
	}

	public void setServiceOrderCode(String serviceOrderCode) {
		this.serviceOrderCode = serviceOrderCode;
	}

	public String getGetStatus() {
		return getStatus;
	}

	public void setGetStatus(String getStatus) {
		this.getStatus = getStatus;
	}

}
