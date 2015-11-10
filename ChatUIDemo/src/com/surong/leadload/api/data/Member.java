package com.surong.leadload.api.data;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * ��������Ϣ
 * 
 * @author Administrator
 * 
 */
public class Member  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String typeName;
	
	private String typeId;
	private String stdFlag;
	private String authStatus;
	private String shortName;
	private String realName;
	private String cityId;
	private String cityName;
	private String displayName;
	private String headImgPath;
	private String id;
	private String instituId;
	private String instituName;
	private String latitude;
	private String longitude;
	private String memberLevel;
	private String mobile;
	private String publisherId;
	private String provinceName;
	private String serviceTime;
	private int friendCount;
	private String isColleague;
	public String getIsColleague() {
        return isColleague;
    }
    public void setIsColleague(String isColleague) {
        this.isColleague = isColleague;
    }
    public int getFriendCount() {
        return friendCount;
    }
    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }
    public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getStdFlag() {
		return stdFlag;
	}
	public void setStdFlag(String stdFlag) {
		this.stdFlag = stdFlag;
	}
	public String getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getHeadImgPath() {
		return headImgPath;
	}
	public void setHeadImgPath(String headImgPath) {
		this.headImgPath = headImgPath;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static List<Member> parse(String data){
		Type type = new TypeToken<List<Member>>() {
		}.getType();
		Gson gson = new Gson();
		List<Member> members = gson.fromJson(data, type);
		return members;
	}
}
