package com.surong.leadload.api.data;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FriendList {

	private int labelCode;
	private String labelName;
	private List<Friend> friendList;
	public int getLabelCode() {
		return labelCode;
	}
	public void setLabelCode(int labelCode) {
		this.labelCode = labelCode;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public List<Friend> getFriendList() {
		return friendList;
	}
	public void setFriendList(List<Friend> friendList) {
		this.friendList = friendList;
	}
	
	public static List<FriendList> parse(String data){
		Type type = new TypeToken<List<FriendList>>() {
		}.getType();
		Gson gson = new Gson();
		List<FriendList> members = gson.fromJson(data, type);
		return members;
	}
}
