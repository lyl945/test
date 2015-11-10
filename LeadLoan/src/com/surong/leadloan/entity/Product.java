package com.surong.leadloan.entity;

public class Product {
	String prodName;
	String cityName;// 产品适用城市
	String costExplain;// 总成本说明
	String custType;// 可贷主体
	String guaranteeType;// 接受的担保方式
	String id;// 产品ID
	String loanAmount;// 贷款额度
	String loanPeriod;// 贷款期限

	

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCostExplain() {
		return costExplain;
	}

	public void setCostExplain(String costExplain) {
		this.costExplain = costExplain;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getGuaranteeType() {
		return guaranteeType;
	}

	public void setGuaranteeType(String guaranteeType) {
		this.guaranteeType = guaranteeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getLoanPeriod() {
		return loanPeriod;
	}

	public void setLoanPeriod(String loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

}
