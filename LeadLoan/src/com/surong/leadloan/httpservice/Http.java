package com.surong.leadloan.httpservice;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.surong.leadloan.utils.Constans;

public class Http {
	/*
	 * ��¼�ӿ�
	 */
	public void httpGetLogin(String mobile, String password,
			final Handler handler, final Context context) {
		HttpUtils http = MyHttpUtils.instance();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		params.addQueryStringParameter("password", password);
		http.send(HttpRequest.HttpMethod.GET, Constans.loginUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								String token = ob.getString("token");
								handler.sendMessage(handler.obtainMessage(code,
										token));
								return;
							}
							String msg = ob.getString("msg");
							MyHttpUtils.showError(context, code, msg);
						} catch (Exception e) {
							MyHttpUtils.showError(context, -1000, "");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						MyHttpUtils.showError(context, -100, "");

					}

				});
	}
}
