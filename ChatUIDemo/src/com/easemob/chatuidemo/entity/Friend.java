package com.easemob.chatuidemo.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Friend {

	private String displayName;//������ʾ��
	private String friendId;//����id
	private String headImgPath;
	private String instituShortName;
	private int labelCode;
	
	private String memberLevel;//��ע��
	private String mobile;//����
	private String stdFlag;
	
	private String remarkName;
	
	private String isFriend;
	
	public String getIsFriend() {
		return isFriend;
	}
	
	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}
	
	public String getRemarkName() {
		return remarkName;
	}
	
	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}
	

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String chineseName) {
		this.displayName = chineseName;
	}

	public static Friend parse(JSONObject jsonObject){
		try {
			Friend friend = new Friend();
			friend.setDisplayName(jsonObject.getString("displayName"));
			friend.setFriendId(jsonObject.getString("friendId"));
			return friend;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public String getHeadImgPath() {
		return headImgPath;
	}

	public void setHeadImgPath(String headImgPath) {
		this.headImgPath = headImgPath;
	}

	public String getInstituShortName() {
		return instituShortName;
	}

	public void setInstituShortName(String instituShortName) {
		this.instituShortName = instituShortName;
	}

	public int getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(int labelCode) {
		this.labelCode = labelCode;
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

	public String getStdFlag() {
		return stdFlag;
	}

	public void setStdFlag(String stdFlag) {
		this.stdFlag = stdFlag;
	}

}
