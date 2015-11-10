package com.easemob.applib.model;

import org.json.JSONObject;

import com.pj.core.utilities.ConvertUtility;

public class EaseUser {
	private String mobile;
	private String instituName;
	private String headImgPath;
	private String memberLevel;
	private String displayName;
	private String realName;
	
	private String userId;
	private String typeId; //	机构类型id;
	private String typeName; //	机构类型名称;

	private String instituId;//	机构id
	private String cityId;//	城市id
	private String cityName;// 城市名称
	private String provnId;//	省id
	private String provnName;// 省名称
	private String createDate;//创建时间
	private int point;//	剩余积分
	private int amount;//	剩余点券
	private String email;//	邮箱地址
	private int authStatus;//认证状态（00未认证，01认证未通过，02认证通过，03认证中,04申请认证中）
	private String stdFlag;//是否为标准信贷经理（S标准信贷经理，A信贷顾问）
	private String stdFlagInstitu;//是否是标准信贷机构（S标准，A非标准）
	private String srvClsPoint;   //信用等级
	private String personDuty;	//个人职务
	private String workingTime;	//从业时间
	private String instituTermOfOffice;//本机构任职时间
	private boolean    easeMobUser;//		是否注册了环信（0 未注册 1已注册）
	private String easeMobAccount;// 环信账号
	private String easeMobPassword;//环信密码
	private boolean hasUploadContacts;//是否已上传通讯录
	
	
	public EaseUser(JSONObject userData){
		readDataFrom(userData);
	}
	public void readDataFrom(JSONObject userData) {
		this.userId = getString(userData,"id");
        this.mobile = getString(userData,"mobile");
        this.displayName = getString(userData,"displayName"); 
        this.realName = getString(userData,"realName"); 
        this.typeId = getString(userData,"typeId"); 
        this.typeName = getString(userData,"typeName"); 
        this.instituId = getString(userData,"instituId"); 
        this.instituName = getString(userData,"instituName"); 
        this.cityId = getString(userData,"cityId"); 
        this.cityName = getString(userData,"cityName"); 
        this.provnId = getString(userData,"provnId"); 
        this.provnName = getString(userData,"provnName"); 
        this.createDate = getString(userData,"createDate"); 
        this.point = ConvertUtility.parseInt(getString(userData,"point")) ;
        this.amount = ConvertUtility.parseInt(getString(userData,"amount"));
        this.email = getString(userData,"email"); 
        this.headImgPath = getString(userData,"headImgPath"); 
        this.authStatus = ConvertUtility.parseInt(getString(userData,"authStatus"));
        this.stdFlag = getString(userData,"stdFlag"); 
        this.stdFlagInstitu = getString(userData,"stdFlagInstitu"); 
        this.srvClsPoint = getString(userData,"srvClsPoint"); 
        this.memberLevel = getString(userData,"memberLevel"); 
        this.personDuty = getString(userData,"personDuty"); 
        this.workingTime = getString(userData,"workingTime"); 
        this.instituTermOfOffice = getString(userData,"instituTermOfOffice"); 
        this.easeMobUser = ConvertUtility.parseInt(getString(userData,"isHuanxinFlag"))!=0;
        this.easeMobAccount = getString(userData,"huanxinAccount");
        this.easeMobPassword = getString(userData,"huanxinPassword");
        this.hasUploadContacts = ConvertUtility.parseInt(getString(userData,"hasPhoneBook"))!=0;
	}
	
	private String getString(JSONObject object,String key){
		try {
			return object.getString(key);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getInstituName() {
		return instituName;
	}
	public void setInstituName(String instituName) {
		this.instituName = instituName;
	}
	public String getHeadImgPath() {
		return headImgPath;
	}
	public void setHeadImgPath(String headImgPath) {
		this.headImgPath = headImgPath;
	}
	public String getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(int authStatus) {
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
	public boolean isEaseMobUser() {
		return easeMobUser;
	}
	public void setEaseMobUser(boolean easeMobUser) {
		this.easeMobUser = easeMobUser;
	}
	public String getEaseMobAccount() {
		return easeMobAccount;
	}
	public void setEaseMobAccount(String easeMobAccount) {
		this.easeMobAccount = easeMobAccount;
	}
	public String getEaseMobPassword() {
		return easeMobPassword;
	}
	public void setEaseMobPassword(String easeMobPassword) {
		this.easeMobPassword = easeMobPassword;
	}
	public boolean isHasUploadContacts() {
		return hasUploadContacts;
	}
	public void setHasUploadContacts(boolean hasUploadContacts) {
		this.hasUploadContacts = hasUploadContacts;
	}
}
