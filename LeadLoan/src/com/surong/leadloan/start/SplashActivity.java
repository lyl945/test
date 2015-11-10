package com.surong.leadloan.start;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.easemob.chatuidemo.activity.BaseActivity;

/**
 * 判断是不是第一次进入软件，如果第一次进入，跳到 引导页，否则就跳转到登陆页面
 * 
 * 
 */
public class SplashActivity extends BaseActivity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟1秒
	private static final long SPLASH_DELAY_MILLIS = 1000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	// /**
	// * Handler:跳转到不同界面
	// */
	// private Handler mHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case GO_HOME:
	// goHome();
	// break;
	// case GO_GUIDE:
	// goGuide();
	// break;
	// }
	// super.handleMessage(msg);
	// }
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		Intent intent = null;
		if (!isFirstIn) {
			intent = new Intent(SplashActivity.this, LoginActivity.class);
		} else {
			// mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
			intent = new Intent(SplashActivity.this, GuideActivity.class);
		}
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	

}