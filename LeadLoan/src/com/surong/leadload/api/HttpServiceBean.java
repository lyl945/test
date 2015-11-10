package com.surong.leadload.api;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils2.Constans;

public class HttpServiceBean {

	private static HttpServiceBean httpServiceBean;

	public static HttpServiceBean instance() {
		if (null == httpServiceBean) {
			httpServiceBean = new HttpServiceBean();
		}
		return httpServiceBean;
	}

	/*
	 * ��¼�ӿ�
	 */
	public void httpGetLogin(String mobile, String password,
			final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		params.addQueryStringParameter("password", password);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.loginUrl, params,
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
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							//HandlerUtil.showError(context, -1000);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetLogin==error===>" + msg);
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ע��ӿ�
	 */
	public void httpGetRegister(String mobile, String password,
			String realName, String displayName, String provnId,
			String provnName, String cityId, String cityName, String instituId,
			String instituName, String checkCode, final Handler handler,
			final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		params.addQueryStringParameter("password", password);
		params.addQueryStringParameter("realName", realName);
		params.addQueryStringParameter("displayName", displayName);
		params.addQueryStringParameter("provnId", provnId);
		params.addQueryStringParameter("provnName", provnName);
		params.addQueryStringParameter("cityId", cityId);
		params.addQueryStringParameter("cityName", cityName);
		params.addQueryStringParameter("instituId", instituId);
		params.addQueryStringParameter("instituName", instituName);
		params.addQueryStringParameter("checkCode", checkCode);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.registerUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(code));
								return;
							}
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							//HandlerUtil.showError(context, -1000);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetRegister==error===>" + msg);
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	public void httpGetSpinner(final Handler handler, final Context context) {
		
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.spinnerUrl,null,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(20,
										ob));
								return;
							}
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							//HandlerUtil.showError(context, -1000);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetSpinner==error===>" + msg);
						// HandlerUtil.showError(context, 100);
					}
				});
	}

	public static void httpGetInstitu(final Context context,
			final RequestCallBack<String> cb) {

		MyHttpUtils.getHttpJsonString(context, HttpRequest.HttpMethod.GET,
				Constans.institutionUrl, null, cb);
	}

	/*
	 * ��ȡ�ֵ�ӿ�
	 */
	public void httpGetdictionary(final Handler handler, final Context context) {
		
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.dictionaryUrl,null,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(40,
										ob));
								return;
							}
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetInstitu==error===>" + msg);
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ��ȡע����֤��
	 */
	public void httpGetRegisterCode(String mobile, final Handler handler,
			final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.codeUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(1));
								return;
							}
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetInstitu==error===>" + msg);
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ��ȡ�һ�������֤��
	 */
	public void httpFindPwdCode(String mobile, final Handler handler,
			final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.findcodeUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(1));
								return;
							}
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetInstitu==error===>" + msg);
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * �һ�����ӿ�
	 */
	public void httpFindPwd(String mobile, final Handler handler,
			final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.findcodeUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(code));
								return;
							}
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							//HandlerUtil.showError(context, -1000);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetInstitu==error===>" + msg);
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/**��������*/
	public void httpSetPwd(String mobile, String newPsd, String checkCode,
			final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		params.addQueryStringParameter("newPsd", newPsd);
		params.addQueryStringParameter("checkCode", checkCode);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.set_pwd, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(code));
								return;
							}
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							//HandlerUtil.showError(context, -1000);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetInstitu==error===>" + msg);
						//HandlerUtil.showError(context, 100);
					}
				});
	}
	/*
	 * �ٶ��������ϴ�����
	 */
	
	public void httpBaiduSetup(String clientType ,String token, String baiduUserId, String channelId,
			final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("clientType", "1");
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("baiduUserId", baiduUserId);
		params.addQueryStringParameter("channelId", channelId);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.baiduSetup, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(0));
								return;
							}
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							//HandlerUtil.showError(context, -1000);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("MyTest", "httpGetInstitu==error===>" + msg);
						//HandlerUtil.showError(context, 100);
					}
				});
	}
	

}
