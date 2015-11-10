package com.surong.leadload.database;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.pj.core.datamodel.DataWrapper;
import com.pj.core.utilities.StringUtility;

import android.content.ContentValues;
import android.content.Context;

public class EASEDatabaseApply extends EASEDatabase {
	
	public static final int ApplyStatePending  = 0;     // 等待处理
	public static final int ApplyStateAgree    = 1<<1;  // 已添加
	public static final int ApplyStateRefuse   = 1<<2;  // 已拒绝
	public static final int ApplyStateIgnore   = 1<<3;  // 已忽略
	
	public static final int ApplyStyleFriend    = 0;  // 已添加
	public static final int ApplyFriend    = 3;  // 已添加
	public static final int ApplyStyleGroupInvitation   = 1;  // 已拒绝
	public static final int ApplyStyleJoinGroup   = 2;  // 已忽略
	
	
	// 申请加好友表字段
	public static final String ApplyID =                  "_id";
	public static final String ApplyApplicantNickname =   "applicant_nickname";  //申请人昵称
	public static final String ApplyApplicantID =         "applicant_id";        //申请人ID（环信用户名）
	public static final String ApplyGroupId =             "group_id";            //群组ID
	public static final String ApplyGroupSubject =        "group_subject";       //群组名称
	public static final String ApplyApplicantImage =      "applicant_img";       //申请人头像/群组头像
	public static final String ApplyReason =              "reason";              //申请原因
	public static final String ApplyReceiverNickname =    "receiver_nickname";   //接收人昵称
	public static final String ApplyReceiverID =          "receiver_id";         //接收人ID(环信用户名)
	public static final String ApplyType =                "type";                //申请类型，申请好友/申请加入群dd,参考环信申请类型
	public static final String ApplyReceiverImage =       "receiver_img";        //接收人头像/群组头像
	public static final String ApplyState =               "state";               //处理状态，已添加/已拒绝/忽略，默认是等待处理
	public static final String ApplyTime =                "time";               //处理状态，已添加/已拒绝/忽略，默认是等待处理
	
	public static final String sql(){
		String applyTable = "create table if not exists %s("
				+ "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s text default ' ', "
				+ "%s integer, "
				+ "%s text default ' ', "
				+ "%s integer default 0,"
				+ "%s long default 0)";
	    
	    
	    String sql = String.format(applyTable, 
	    		T_APPLY,
	    		ApplyID,
	    		ApplyApplicantNickname,
	    		ApplyApplicantID, 
	    		ApplyGroupId, 
	    		ApplyGroupSubject, 
	    		ApplyApplicantImage , 
	    		ApplyReason , 
	    		ApplyReceiverNickname, 
	    		ApplyReceiverID, 
	    		ApplyType, 
	    		ApplyReceiverImage , 
	    		ApplyState,
	    		ApplyTime
	    		);
		return sql;
	}

	public EASEDatabaseApply(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public String[] getColumns() {
		// TODO Auto-generated method stub
		return new String[]{
				ApplyID,
				ApplyApplicantNickname,
				ApplyApplicantID,
				ApplyGroupId,
				ApplyGroupSubject,
				ApplyGroupSubject,
				ApplyApplicantImage,
				ApplyReason,
				ApplyReceiverNickname,
				ApplyReceiverID,
				ApplyType,
				ApplyReceiverImage,
				ApplyState,
				ApplyTime
				};
	}

	@Override
	public String getIDColumn() {
		// TODO Auto-generated method stub
		return ApplyID;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return T_APPLY;
	}

	@SuppressWarnings("unchecked")
	public List<DataWrapper> listApplicant(String receiverId,int state){
		if (receiverId == null) {
	        return Collections.EMPTY_LIST;
	    }
	    
		LinkedList<DataWrapper> list = new LinkedList<DataWrapper>();
	    
	    int states[] =new int[] {ApplyStateAgree,ApplyStateIgnore,ApplyStatePending,ApplyStateRefuse};
	    
	    String sql = String.format("%s=? and %s=?",ApplyReceiverID,ApplyState);
	    
	    for (int i = 0; i<states.length; i++) {
	        int single = states[i];
	        if ((single & state) == single) {
	        	List<DataWrapper> tmp = query(sql, new String[]{receiverId,single+""});
	            if (tmp != null) {
	                list.addAll(tmp);
	            }
	        }
	    }
	    
	    return list;
	}


	public void insertOrUpdateApplicant(DataWrapper applicant){
		if (applicant == null) {
	        return;
	    }
	    
	    if (StringUtility.isEmpty(applicant.getString(ApplyApplicantID)) || StringUtility.isEmpty(applicant.getString(ApplyReceiverID))) {
	        
	        return;
	    }
	    
	    
	    String sql = String.format("%s=? and %s=?", ApplyReceiverID,ApplyApplicantID);
	    DataWrapper apply = querySingle(sql, new String[]{applicant.getString(ApplyReceiverID), applicant.getString(ApplyApplicantID)});
	    
	    if (apply != null) {        
	        
	        ContentValues values = copyFrom(applicant);
	        values.remove(ApplyID);
	        values.remove(ApplyApplicantID);
	        values.remove(ApplyReceiverID);
	        
	        update(values, sql, new String[]{applicant.getString(ApplyReceiverID), applicant.getString(ApplyApplicantID)});
	    } else {
	    	if (!applicant.containsKey(ApplyType)) {
				applicant.setObject(ApplyType, ApplyStyleFriend+"");
			}
	    	if (!applicant.containsKey(ApplyState)) {
				applicant.setObject(ApplyState, ApplyStyleFriend+"");
			}
	    	if (!applicant.containsKey(ApplyState)) {
	    	    applicant.setObject(ApplyState, ApplyFriend+"");
	    	}
	    	insert(applicant);
	    }
	}

	public void removeApply(String receiver,String applicantId){
		if (receiver==null || applicantId==null) {
	        return;
	    }
	    delete(String.format("%s=? and %s=?", ApplyReceiverID,ApplyApplicantID), new String[]{receiver,applicantId});
	}
}
