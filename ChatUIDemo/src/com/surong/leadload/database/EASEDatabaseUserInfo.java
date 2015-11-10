package com.surong.leadload.database;

import com.pj.core.datamodel.DataWrapper;
import com.pj.core.utilities.StringUtility;

import android.content.ContentValues;
import android.content.Context;

public class EASEDatabaseUserInfo extends EASEDatabase {
	// 用户头像URL表字段
	public static final String UserID =                  "user_id";      //用户ID（环信用户名）
	public static final String UserImg =                 "user_image";   // 用户头像地址
	public static final String UserDisplayName =         "user_display_name";
	public static final String UserRealName =            "user_real_name";
	public static final String UserOrganizationName =    "user_organization_name";
	public static final String UserIsMyFriend =          "user_is_my_friend";
	
	
	public static final String YES =          "Y";
	public static final String NO  =          "N";

	public EASEDatabaseUserInfo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static final String sql(){
		String userInfoTable = "create table if not exists %s("
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ')";
	    String sql = String.format(userInfoTable, 
	    		T_USER_INFO,
	    		UserID,
	    		UserImg,
	    		UserDisplayName,
	    		UserOrganizationName,
	    		UserRealName,
	    		UserIsMyFriend);
		return sql;
	}

	@Override
	public String[] getColumns() {
		// TODO Auto-generated method stub
		return new String[]{
				UserID,
	    		UserImg,
	    		UserDisplayName,
	    		UserOrganizationName,
	    		UserRealName,
	    		UserIsMyFriend
	    		};
	}

	@Override
	public String getIDColumn() {
		// TODO Auto-generated method stub
		return UserID;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return T_USER_INFO;
	}

	
	public void insertOrUpdateUserTmpInfo(DataWrapper userInf){
	    if (userInf == null || !userInf.containsKey(UserID)) {
	        return;
	    }
	    
	    DataWrapper chk = querySingle(UserID+"=?", new String[]{userInf.getString(UserID)});
	    
	    if (chk != null) {
	    	String userId = (String)userInf.remove(UserID);
	    	update(copyFrom(userInf), UserID+"=?", new String[]{userId});
	    }else{
	        insert(userInf);
	    }
	}

	public String getImageURLByUserID(String userId){
	    DataWrapper userInf = getUserInfoByUserID(userId);
	    return (userInf!=null?userInf.getString(UserImg): null);
	}


	public DataWrapper getUserInfoByUserID(String userId){
	    if (StringUtility.isEmpty(userId)) {
	        return null;
	    }
	    return querySingle(UserID+"=?", new String[]{userId});
	}

	public boolean isMyFriend(String userId){
		DataWrapper userInf = getUserInfoByUserID(userId);
	    if (userInf != null) {
	        return "Y".equalsIgnoreCase(userInf.getString(UserIsMyFriend));
	    }
	    return false;
	}

	public void setIsMyFriend(boolean isMyFriend,String userId){
	    if (userId!=null) {
	    	ContentValues values = new ContentValues(1);
	    	values.put(UserIsMyFriend, isMyFriend?"Y":"N");
	    	update(values, UserID+"=?", new String[]{userId});
	    }
	}

	public void clearAllUserTempInfo(){
		delete(null, null);
	}

	public void removeUserByUserID(String userId){
		delete(UserID+"=?", new String[]{userId});
	}
}
