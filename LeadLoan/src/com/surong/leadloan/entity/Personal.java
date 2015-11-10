package com.surong.leadloan.entity;

import java.io.Serializable;

/**
 * 个人信息javabean
 */
public class Personal implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String personalId;// 个人id
	private String mobile;// 手机号
	private String displayName;// 显示名
	private String realName;// 真是姓名
	private String typeId;// 机构类型id
	private String typeName;// 机构类型名称
	private String instituId;// 机构id
	private String instituName;// 机构名称
	private String cityId;// 城市id
	private String cityName;// 城市名称
	private String provnId;// 省份id
	private String provnName;// 身份名称
	private String createDate;// 入职时间
	private String point;// 剩余积分
	private String amount;// 剩余点卷
	private String email;// 邮箱地址ַ
	private String headImgPath;// 头像地址ַ
	private String authStatus;// 认证状态（00未认证，01认证未通过，02认证通过，03认证中,04申请认证中）
	private String stdFlag;// 是否为标准信贷经理（S标准信贷经理，A信贷顾问）
	private String stdFlagInstitu;// 是否是标准信贷机构（S标准，A非标准）
	private String srvClsPoint;// 钻石等级
	private String memberLevel;// 等级
	private String personDuty;// 职位
	private String workingTime;// 工作时间
	private String instituTermOfOffice;// 在职工作时间
	private Boolean isHuanxinFlag;
	private String huanxinAccount;
	private String huanxinPassword;
	private String hobby;// 爱好
	private String gender;// 性别0-男，1-女
	private String idNo;// 身份
	private String idExpiredDate;// 省份证到期日
	private String idFrontImg;// 身份证正面照
	private String qq;
	private String webChat;// 微信
	private String tag;// 职业标签

	private String officeAddr;// 办公地址

	private String badgeImg;// 工牌或同和照

	private String bizCardImg;// 公司名片

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWebChat() {
		return webChat;
	}

	public void setWebChat(String webChat) {
		this.webChat = webChat;
	}

	public String getBadgeImg() {
		return badgeImg;
	}

	public void setBadgeImg(String badgeImg) {
		this.badgeImg = badgeImg;
	}

	public String getBizCardImg() {
		return bizCardImg;
	}

	public void setBizCardImg(String bizCardImg) {
		this.bizCardImg = bizCardImg;
	}

	public String getOfficeAddr() {
		return officeAddr;
	}

	public void setOfficeAddr(String officeAddr) {
		this.officeAddr = officeAddr;
	}

	public String getIdFrontImg() {
		return idFrontImg;
	}

	public void setIdFrontImg(String idFrontImg) {
		this.idFrontImg = idFrontImg;
	}

	public String getIdExpiredDate() {
		return idExpiredDate;
	}

	public void setIdExpiredDate(String idExpiredDate) {
		this.idExpiredDate = idExpiredDate;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getPersonalId() {
		return personalId;
	}

	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getInstituId() {
		return instituId;
	}

	public void setInstituId(String instituId) {
		this.instituId = instituId;
	}

	public String getInstituName() {
		return instituName;
	}

	public void setInstituName(String instituName) {
		this.instituName = instituName;
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

	public String getProvnId() {
		return provnId;
	}

	public void setProvnId(String provnId) {
		this.provnId = provnId;
	}

	public String getProvnName() {
		return provnName;
	}

	public void setProvnName(String provnName) {
		this.provnName = provnName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHeadImgPath() {
		return headImgPath;
	}

	public void setHeadImgPath(String headImgPath) {
		this.headImgPath = headImgPath;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getStdFlag() {
		return stdFlag;
	}

	public void setStdFlag(String stdFlag) {
		this.stdFlag = stdFlag;
	}

	public String getStdFlagInstitu() {
		return stdFlagInstitu;
	}

	public void setStdFlagInstitu(String stdFlagInstitu) {
		this.stdFlagInstitu = stdFlagInstitu;
	}

	public String getSrvClsPoint() {
		return srvClsPoint;
	}

	public void setSrvClsPoint(String srvClsPoint) {
		this.srvClsPoint = srvClsPoint;
	}

	public String getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}

	public String getPersonDuty() {
		return personDuty;
	}

	public void setPersonDuty(String personDuty) {
		this.personDuty = personDuty;
	}

	public String getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(String workingTime) {
		this.workingTime = workingTime;
	}

	public String getInstituTermOfOffice() {
		return instituTermOfOffice;
	}

	public void setInstituTermOfOffice(String instituTermOfOffice) {
		this.instituTermOfOffice = instituTermOfOffice;
	}

	public Boolean getIsHuanxinFlag() {
		return isHuanxinFlag;
	}

	public void setIsHuanxinFlag(Boolean isHuanxinFlag) {
		this.isHuanxinFlag = isHuanxinFlag;
	}

	public String getHuanxinAccount() {
		return huanxinAccount;
	}

	public void setHuanxinAccount(String huanxinAccount) {
		this.huanxinAccount = huanxinAccount;
	}

	public String getHuanxinPassword() {
		return huanxinPassword;
	}

	public void setHuanxinPassword(String huanxinPassword) {
		this.huanxinPassword = huanxinPassword;
	}

}
