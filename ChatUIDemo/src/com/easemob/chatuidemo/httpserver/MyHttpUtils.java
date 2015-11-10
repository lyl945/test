package com.easemob.chatuidemo.httpserver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.easemob.chatuidemo.utils.Constans;
import com.easemob.chatuidemo.utils.CustomProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MyHttpUtils {
	// 实例化MyHttpUtils
	private static MyHttpUtils myHttpUtils;

	public static MyHttpUtils myInstance() {
		if (null == myHttpUtils) {
			myHttpUtils = new MyHttpUtils();
		}
		return myHttpUtils;
	}

	// 实例化 httpUtils
	private static HttpUtils httpUtils;

	public static HttpUtils instance() {
		if (null == httpUtils) {
			httpUtils = new HttpUtils();
			httpUtils.configCurrentHttpCacheExpiry(0);
			httpUtils.configSoTimeout(30 * 1000);
			httpUtils.configTimeout(30 * 1000);
		}
		return httpUtils;
	}

	/*
	 * @author 胡田
	 * 
	 * @params 传入参数
	 * 
	 * @path 接口
	 * 
	 * @flag 标识同一个handler处理的不同请求
	 * 
	 * @handler 处理请求
	 * 
	 * @thod标识是post还是get方法
	 */
	public static void getHttpJsonString(final Context context,
			HttpRequest.HttpMethod method, String path, RequestParams params,
			final RequestCallBack<String> cb) {
		HttpUtils http = instance();

		http.send(method, path, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (cb == null) {
					return;
				}
				Log.i("zhangyuan", "success " + arg0.result);
				try {
					JSONObject ob = new JSONObject(arg0.result);
					int code = ob.getInt("code");
					if (0 == code && cb != null) {
						cb.onSuccess(arg0);
						return;
					} else {
						String msg = ob.getString("msg");
						showError(context, code, msg);
						cb.onFailure(new HttpException(code), "");
					}
				} catch (JSONException e) {
					showError(context, -1000, "");
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.i("MyTest", "httpGetLogin==error===>" + arg1);
				showError(context, -100, "");
				if (cb != null) {
					cb.onFailure(arg0, arg1);
				}
			}
		});
	}

	public static void getHttpJsonString(RequestParams params, String path,
			final Handler handler, final Context context, final int flag,
			int thod) {
		HttpUtils http = instance();

		HttpMethod method = null;
		if (thod == 0) {//
			method = HttpRequest.HttpMethod.GET;
		} else {
			method = HttpRequest.HttpMethod.POST;
		}
		http.send(method, path, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					JSONObject ob = new JSONObject(arg0.result);
					int code = ob.getInt("code");
					if (0 == code) {
						handler.sendMessage(handler.obtainMessage(flag, ob));
						return;
					}
					String msg = ob.getString("msg");
					showError(context, code, msg);
				} catch (JSONException e) {
					showError(context, -1000, "");
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.i("MyTest", "httpGetLogin==error===>" + arg1);
				showError(context, -100, "");
			}
		});
	}

	public static void showError(Context context, int code, String msg) {
		CustomProgressDialog.stopProgressDialog();

		if (msg == null || msg.equals("")) {
			Constans.Toast(context, "网络超时");
		} else {
			if (code == -1000) {
				Constans.Toast(context, "后台返回数据解析报错");
			} else if (code == -40004) {
				Constans.Toast(context, "账户无效");
			} else {
				Constans.Toast(context, msg);
			}
		}

		// switch (code) {
		// case -1:
		// Constans.Toast(context, "系统繁忙");
		// break;
		// case -40004:
		// Constans.Toast(context, "账户无效");
		// break;
		// case 40000:
		// Constans.Toast(context, "发送失败");
		// break;
		// case 40001:
		// Constans.Toast(context, "会话超时");
		// break;
		// case 40002:
		// Constans.Toast(context, "参数错误");
		// break;
		// case 40003:
		// Constans.Toast(context, "手机号已被注册");
		// break;
		// case 40004:
		// Constans.Toast(context, "账号或密码错误");
		// break;
		// case 40007:
		// Constans.Toast(context, "验证码错误");
		// break;
		// case 40010:
		// Constans.Toast(context, "验证码失效");
		// break;
		// case 40011:
		// Constans.Toast(context, "1分钟内不能重新请求发送短信");
		// break;
		// case 40012:
		// Constans.Toast(context, "密码错误");
		// break;
		// case 40060:
		// Constans.Toast(context, "用户不存在");
		// break;
		// case 40065:
		// Constans.Toast(context, "您还未认证通过");
		// break;
		// case 40110:
		// Constans.Toast(context, "您的点券账户余额不足");
		// break;
		// case 40130:
		// Constans.Toast(context, "此客户已有其他信贷经理交单跟进中");
		// break;
		// case -8:
		// Constans.Toast(context, "置顶失败");
		// break;
		// case -1000:
		// Constans.Toast(context, "后台返回数据解析报错");
		// break;
		// case -100:
		// Constans.Toast(context, "网络超时");
		// break;
		// case 40008:
		// Constans.Toast(context, "登录超时");
		//
		// Intent i = context.getPackageManager().getLaunchIntentForPackage(
		// context.getPackageName());
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// context.startActivity(i);
		//
		// break;
		//
		// default:
		// Constans.Toast(context, "网络超时");
		// break;
		// }
	}

}
