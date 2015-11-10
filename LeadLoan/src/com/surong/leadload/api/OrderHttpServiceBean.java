package com.surong.leadload.api;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils2.Constans;

public class OrderHttpServiceBean {

	
	
	public static void addFriend(String id,final Context context, RequestCallBack<String> cb){
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",SharedPreferencesHelp.getToken(context));
		params.addQueryStringParameter("friendId",id);
		MyHttpUtils.getHttpJsonString(context, HttpRequest.HttpMethod.POST, Constans.addById, params,cb);
		
		
	}

	
	/*
	 * ��ȡ������Ѻ��ʽ���ת�ӿ�
	 */
	public void httpGetPersonalConsumption(final int type, String token,
			String loadUse, String pageNo, String pageSize,
			final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("loadUse", loadUse);
		params.addQueryStringParameter("pageNo", pageNo);
		params.addQueryStringParameter("pageSize", pageSize);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET,
				Constans.orderPersonalConsumptionUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(type,
										ob));
								return;
							}
							handler.sendMessage(handler.obtainMessage(100));
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						handler.sendMessage(handler.obtainMessage(100));
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ��ȡ���ô��
	 */
	public void httpGetCreadits(final int type, String token, String pageNo,
			String pageSize, final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("pageNo", pageNo);
		params.addQueryStringParameter("pageSize", pageSize);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.orderCreditUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(type,
										ob));
								return;
							}
							handler.sendMessage(handler.obtainMessage(100));
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						handler.sendMessage(handler.obtainMessage(100));
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ��¥����
	 */
	public void httpGetPurchase(final int type, String token, String pageNo,
			String pageSize, final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("pageNo", pageNo);
		params.addQueryStringParameter("pageSize", pageSize);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.orderPurchaseUrl,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(type,
										ob));
								return;
							}
							handler.sendMessage(handler.obtainMessage(100));
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						handler.sendMessage(handler.obtainMessage(100));
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ��ȡ��˾���
	 */
	public void httpGetCompanyLoan(final int type, String token, String pageNo,
			String pageSize, final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("pageNo", pageNo);
		params.addQueryStringParameter("pageSize", pageSize);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.orderCompanyLoadUrl,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(type,
										ob));
								return;
							}
							handler.sendMessage(handler.obtainMessage(100));
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						handler.sendMessage(handler.obtainMessage(100));
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ������Ѻ
	 */
	public void httpGetVehiclePledge(final int type, String token,
			String pageNo, String pageSize, final Handler handler,
			final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("pageNo", pageNo);
		params.addQueryStringParameter("pageSize", pageSize);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.orderVehiclePledgeUrl,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(type,
										ob));
								return;
							}
							handler.sendMessage(handler.obtainMessage(100));
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						handler.sendMessage(handler.obtainMessage(100));
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ��Ŀ����
	 */
	public void httpGetOrderPraise(String token, String itemId,
			final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("itemId", itemId);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.orderPraiseUrl, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							JSONObject ob = new JSONObject(responseInfo.result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(10,
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
						//HandlerUtil.showError(context, 100);
					}
				});
	}

	/*
	 * ��Ŀ����
	 */
	public void httpGetOrderNegative(String token, String itemId,
			final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("itemId", itemId);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.orderNegativeUrl,
				params, new RequestCallBack<String>() {
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
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						//HandlerUtil.showError(context, 100);
					}
				});
	}


	
	/*
	 * ��ȡ��Ŀ����ӿ�
	 */
	public void httpGetProDetail(String token, String itemId,
			final Handler handler, final Context context) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("itemId", itemId);
		MyHttpUtils.getHttpJsonString(context,HttpRequest.HttpMethod.GET, Constans.orderProDetailUrl,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							String result = responseInfo.result;
							JSONObject ob = new JSONObject(result);
							int code = ob.getInt("code");
							if (0 == code) {
								handler.sendMessage(handler.obtainMessage(10,
										result));
								return;
							}
							handler.sendMessage(handler.obtainMessage(100));
							//HandlerUtil.showError(context, code);
						} catch (Exception e) {
							e.printStackTrace();
							//HandlerUtil.showError(context, -1000);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						handler.sendMessage(handler.obtainMessage(100));
						////HandlerUtil.showError(context, 100);
					}
				});
	}

	

}
