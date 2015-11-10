package com.surong.leadload.api;

import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils2.Constans;

public class CommonServiceBean {

	public static void nearPeer(Context context, int page, double longitude,
			double latitude, final RequestCallBack<String> cb) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getToken(context));
		params.addQueryStringParameter("longitude", longitude + "");
		params.addQueryStringParameter("latitude", latitude + "");
		params.addQueryStringParameter("pageNo", page + "");
		MyHttpUtils.getHttpJsonString(context, HttpRequest.HttpMethod.GET,
				Constans.nearPeer, params, cb);
	}

	public static void sameIndustry(Context context ,String searchStr, 
			String type, String cityId, int id,
			final RequestCallBack<String> cb) {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getToken(context));
		if (!TextUtils.isEmpty(searchStr))
			params.addQueryStringParameter("searchStr", searchStr);
//		if (!TextUtils.isEmpty(pageNo))
//			params.addQueryStringParameter("pageNo", pageNo);
		if (!TextUtils.isEmpty(cityId))
			params.addQueryStringParameter("cityId", cityId);
		if (!TextUtils.isEmpty(type))
			params.addQueryStringParameter("typeId", type);
//		if (id > 0)
//			params.addQueryStringParameter("cityId", cityId + "");
		MyHttpUtils.getHttpJsonString(context, HttpRequest.HttpMethod.GET,
				Constans.sameIndustry, params, cb);

	}
	
	public static void friendList(Context context, final RequestCallBack<String> cb) {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getToken(context));
		MyHttpUtils.getHttpJsonString(context, HttpRequest.HttpMethod.POST,
				Constans.friendList, params, cb);

	}
	public static void possibleKnow(Context context, final RequestCallBack<String> cb) {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getToken(context));
		MyHttpUtils.getHttpJsonString(context, HttpRequest.HttpMethod.POST,
				Constans.possibleKnow, params, cb);

	}
	
	public static void friendRecommend(Context context, RequestCallBack<String> cb){
//		Gson gson = new Gson();
//		String arrJson = gson.toJson(phoneList);
//		String phoneString = "{\"addressBook\":" + arrJson + "}";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", SharedPreferencesHelp.getString(context, "token"));
//		params.addQueryStringParameter("addressBook", phoneString);
		MyHttpUtils.getHttpJsonString(context, HttpRequest.HttpMethod.POST,
				Constans.friendRecommendUrl, params, cb);
	}
}
