package com.surong.leadloan.entity;

import java.io.Serializable;

public class Promotion implements Serializable {

	private String basePrice;// 底价
	private String currentPrice;// 当前价格
	private String id;// 产品ID
	private String minAddPrice;// 最小加价单位
	private String orderCount;// 订单数
	private String prodName;// 产品名
	private String promMethod;// 推广方式
	private String promStatus;// 推广状态

	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMinAddPrice() {
		return minAddPrice;
	}

	public void setMinAddPrice(String minAddPrice) {
		this.minAddPrice = minAddPrice;
	}

	public String getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getPromMethod() {
		return promMethod;
	}

	public void setPromMethod(String promMethod) {
		this.promMethod = promMethod;
	}

	public String getPromStatus() {
		return promStatus;
	}

	public void setPromStatus(String promStatus) {
		this.promStatus = promStatus;
	}

}
