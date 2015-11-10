package com.surong.leadloan.utils;

import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.applib.model.EaseUser;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.res.EASEConstants;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.pj.core.datamodel.DataWrapper;
import com.surong.leadload.database.EASEDatabaseUserInfo;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.start.LoginActivity;
import com.surong.leadloan.start.MainActivity;

public class Utils {
	// 获取当前时间
	public static String getCurrenTime(Context context) {
		String label = DateUtils.formatDateTime(context,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		return label;
	}

	public static String getStringWithKey(String key, JSONObject obj) {
		if (!obj.isNull(key)) {
			try {
				return obj.getString(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Personal JsonGetPersonal(JSONObject object) throws JSONException{
		Personal per = new Personal();
		String member = object.getString("member");
		JSONObject ob = new JSONObject(member);
		String id = ob.getString("id");
		per.setPersonalId(ob.getString("id"));
		if (!ob.isNull("amount")) {
			per.setAmount(ob.getString("amount").toString());

		}
		if (!ob.isNull("authStatus")) {
			per.setAuthStatus(ob.getString("authStatus").toString());

		}
		if (!ob.isNull("cityId")) 
			per.setCityId(ob.getString("cityId"));
		if (!ob.isNull("cityName")) 
			per.setCityName(ob.getString("cityName"));
		if (!ob.isNull("createDate")) 
		per.setCreateDate(ob.getString("createDate"));
		if (!ob.isNull("displayName")) 
			per.setDisplayName(ob.getString("displayName"));
		if (!ob.isNull("qq")) {
			per.setQq(ob.getString("qq").toString());

		}
		if (!ob.isNull("webChat")) {
			per.setWebChat(ob.getString("webChat").toString());

		}
		if (!ob.isNull("email")) {
			per.setEmail(ob.getString("email").toString());

		}

		if (!ob.isNull("headImgPath")) {
			per.setHeadImgPath(ob.getString("headImgPath")
					.toString());
		}
		if (!ob.isNull("instituId"))
			per.setInstituId(ob.getString("instituId"));
		if (!ob.isNull("instituName"))
			per.setInstituName(ob.getString("instituName"));
		if (!ob.isNull("instituTermOfOffice")) {
			per.setInstituTermOfOffice(ob.getString(
					"instituTermOfOffice").toString());
		}

		if (!ob.isNull("memberLevel")) {
			per.setMemberLevel(ob.getString("memberLevel")
					.toString());
		}
		per.setMobile(ob.getString("mobile"));
		if (!ob.isNull("personDuty")) {
			per.setPersonDuty(ob.getString("personDuty").toString());
		}
		if (!ob.isNull("point")) {
			per.setPoint(ob.getString("point").toString());
		}
		if (!ob.isNull("provnId")) {
			per.setProvnId(ob.getString("provnId"));
		}

		if (!ob.isNull("provnName")) {
			per.setProvnName(ob.getString("provnName"));
		}

		if (!ob.isNull("realName")) {
			per.setRealName(ob.getString("realName"));
		}

		if (!ob.isNull("srvClsPoint")) {
			per.setSrvClsPoint(ob.getString("srvClsPoint"));
		}
		if (!ob.isNull("stdFlag")) {
			per.setStdFlag(ob.getString("stdFlag"));
		}

		if (!ob.isNull("stdFlagInstitu")) {
			per.setStdFlagInstitu(ob.getString("stdFlagInstitu"));
		}

		if (!ob.isNull("workingTime")) {
			per.setWorkingTime(ob.getString("workingTime")
					.toString());
		}
		if (!ob.isNull("isHuanxinFlag")) {
			per.setIsHuanxinFlag(ob.getInt("isHuanxinFlag") == 1 ? true
					: false);
		}
		per.setHuanxinAccount(ob.getString("huanxinAccount"));
		per.setHuanxinPassword(ob.getString("huanxinPassword"));
		if (!ob.isNull("hobby")) {
			per.setHobby(ob.getString("hobby").toString());
		}
		if (!ob.isNull("tag")) {
			per.setTag(ob.getString("tag").toString());
		}
		return per;
	} 
	
	public static void saveOrUpdateEaseUser(JSONObject jObjet,Context context){
		// 存到当前会话
		EaseUser user = new EaseUser(jObjet);
		LDApplication.getInstance().addSessionData(
				EASEConstants.CURRENT_USER, user);
		// 存到数据库
		EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(
				context);
		DataWrapper userWrapper = new DataWrapper();
		userWrapper.setObject(EASEDatabaseUserInfo.UserID,
				user.getUserId());
		userWrapper.setObject(EASEDatabaseUserInfo.UserDisplayName,
				user.getDisplayName());
		userWrapper.setObject(EASEDatabaseUserInfo.UserImg,
				user.getHeadImgPath());
		userWrapper.setObject(EASEDatabaseUserInfo.UserIsMyFriend,
				EASEDatabaseUserInfo.NO);
		userWrapper.setObject(
				EASEDatabaseUserInfo.UserOrganizationName,
				user.getInstituName());
		userWrapper.setObject(EASEDatabaseUserInfo.UserRealName,
				user.getRealName());
		databaseUserInfo.insertOrUpdateUserTmpInfo(userWrapper);
	}
	
	
	public static void saveOrUpdatePersonal(Personal personal, final DbUtils db) {
		try {
			db.dropTable(Personal.class);
			db.createTableIfNotExist(Personal.class);
			db.save(personal);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收到认证通过后 重新获取账号信息
	 * 
	 * @param context
	 */
	public static void reLogin(final Context context) {
		
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		if (sharedPre != null) {
			String mobile = sharedPre.getString("username", "");
			String password = sharedPre.getString("password", "");

			RequestParams params = new RequestParams();
			params.addQueryStringParameter("mobile", mobile);
			params.addQueryStringParameter("password", password);
			String path = Constans.loginUrl;
			MyHttpUtils myHttpUtils = MyHttpUtils.myInstance();

			Handler handler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case 0:
						JSONObject object = (JSONObject) msg.obj;
//						DbUtils db = DbUtils.create(context);
						try {
							String token = object.getString("token");
							SharedPreferencesHelp.SavaString(context, "token",
									token);
							LDApplication.getInstance().addSessionData(
									EASEConstants.TOKEN, token);
							Personal personal = JsonGetPersonal(object);
							saveOrUpdatePersonal(personal, CommonActivity.db);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						break;
					}
				};
			};
			myHttpUtils.getHttpJsonString(params, path, handler, context, 0,
					Constans.thod_Get_0);
		}
	}

	public static boolean initData = false;

	public static void initData(final Context context) {
		if (!initData) {
			final int flag_city = 1;// 省市
			final int flag_institute = 2;// 机构
			final int flag_map = 3;// 字典

			boolean progressShow;
			final Handler handler = new Handler() {
				public void handleMessage(final android.os.Message msg) {
//					final DbUtils db = DbUtils.create(context);

					final JSONObject ob = (JSONObject) msg.obj;
					switch (msg.what) {
					// 获取省市成功
					case flag_city:
						new Thread(){
							@Override
							public void run() {
								// CustomProgressDialog.stopProgressDialog();
								// Constans.Toast(LoginActivity.this, "获取省市成功");
								long currentTime = System.currentTimeMillis();
								SharedPreferencesHelp.SavaLong(context, "currnetTime",
										currentTime);
								SpinnerCityAnalyze.analyzeCityJson(ob, CommonActivity.db);

								super.run();
							}
						}.start();
						
						break;
					// 获取机构接口成功
					case flag_institute:

						final JSONObject obInstitute = (JSONObject) msg.obj;
						new Thread(){
							@Override
							public void run() {
								SpinnerCityAnalyze
										.analyzeInstituteJson(obInstitute, CommonActivity.db);
								super.run();
							}
						}.start();
						break;
					// 获取字典接口成功
					case flag_map:
						final JSONObject obDictionary = (JSONObject) msg.obj;
						Properties prop = new Properties();
						prop.put("prop1", obDictionary.toString());
						FileUtils.saveConfig(context, "/sdcard/config.dat",
								prop);
						new Thread(){
							@Override
							public void run() {
								initData = true;
								
								MyApplication.dictionaryMap = SpinnerCityAnalyze
										.analyzeDictionaryJson(obDictionary);
								MyApplication.dictionaryMap2 = SpinnerCityAnalyze
										.analyzeDictionaryJson2(obDictionary);
								super.run();
							}
						}.start();
						
						break;

					default:
						break;
					}
				};
			};
			new Thread() {
				@Override
				public void run() {
					long currnetTime;// 上次获取数字字典信息的时间；
					long time;// 当前时间
					MyHttpUtils myHttpUtils;
					myHttpUtils = new MyHttpUtils().myInstance();
					currnetTime = SharedPreferencesHelp.GetLong(context,
							"currnetTime");
					time = System.currentTimeMillis();
					RequestParams params = new RequestParams();
					// 获取字典
					if (time - currnetTime > 0 || currnetTime == 0) {
						/*
						 * if (currnetTime == 0) {
						 * CustomProgressDialog.startProgressDialog(context); }
						 */
						// CustomProgressDialog.startProgressDialog(this);
						myHttpUtils
								.getHttpJsonString(params, Constans.mapUrl,
										handler, context, flag_map,
										Constans.thod_Get_0);
					}
					// 获取省市接口
					if (time - currnetTime > 0 || currnetTime == 0) {
						/*
						 * if (currnetTime == 0) {
						 * CustomProgressDialog.startProgressDialog(this); }
						 */
						// CustomProgressDialog.startProgressDialog(this);

						myHttpUtils.getHttpJsonString(params, Constans.cityUrl,
								handler, context, flag_city,
								Constans.thod_Get_0);
						// http.httpGetSpinner(handler, this);
					}

					// 获取机构
					if (time - currnetTime > 31536000 || currnetTime == 0) {// 31536000=365天
						/*
						 * if (currnetTime == 0) {
						 * CustomProgressDialog.startProgressDialog(context); }
						 */
						// CustomProgressDialog.startProgressDialog(this);
						myHttpUtils.getHttpJsonString(params,
								Constans.instituteUrl, handler, context,
								flag_institute, Constans.thod_Get_0);
					}
				};
			}.start();

		}
	}
}
