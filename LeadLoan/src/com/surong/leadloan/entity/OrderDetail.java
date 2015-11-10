package com.surong.leadloan.entity;

import java.io.Serializable;

public class OrderDetail implements Serializable{

	String applyAmount;//申请金额1
	String applyPeriod;//贷款期限1
	String custMobile;//手机号1
	String custName;//客户名称1
	String custType;//客户类型1
	String prodType;//贷款类型1
	String orderId;//订单id
	String serviceOrderCode;//订单来源 01 未匹配订单 （100领单）02 信贷经理转单（100转单）1
	String status;//订单状态1
	String getOrderId;//没有此字段表示订单来源于平台(推广订单)，有此字段表示转单（分为100转单、100领单）1
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGetOrderId() {
		return getOrderId;
	}
	public void setGetOrderId(String getOrderId) {
		this.getOrderId = getOrderId;
	}
	
	
}



