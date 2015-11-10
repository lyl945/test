package com.surong.leadload.database;

import android.content.Context;

import com.pj.core.database.CoreDBService;

public abstract class EASEDatabase extends CoreDBService {
	
	public static final String T_USER_INFO  = "t_user_info";
	public static final String T_APPLY 		= "t_apply";

	public EASEDatabase(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getAllTableNames() {
		// TODO Auto-generated method stub
		return new String[]{T_APPLY, T_USER_INFO};
	}

	@Override
	public String[] getCreateSQLs() {
		// TODO Auto-generated method stub
		return new String[]{ EASEDatabaseApply.sql(), EASEDatabaseUserInfo.sql() };
	}
}
