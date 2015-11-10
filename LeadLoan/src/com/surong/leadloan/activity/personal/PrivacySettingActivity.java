package com.surong.leadloan.activity.personal;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.ui.SwitchButton;
import com.surong.leadloan.ui.SwitchButton.OnSwitchListener;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;

public class PrivacySettingActivity extends CommonActivity {

	private View view;
	private SwitchButton switch_order_information, switch_general_information,
			switch_friends_information, switch_company_information;
	private MySwitchLinstener mySwitchLinstener;
	private MyHttpUtils myHttpUtils;
	int allowAddFriend, displayOrder, openGeneralInfo, openSeniorInfo, flag;

	private String memberId, temp;
	private String token;
	private Context context;
	private final int flag_get_setting = 0;
	private final int flag_change_setting = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.privacy_setting, null);
		context = this;
		body.addView(view);
		changeTitle("设置");
		myHttpUtils = MyHttpUtils.myInstance();
		initView();
		initData();
	}

	private void initData() {
		token = SharedPreferencesHelp.getString(this, "token");
		System.out.println("ppppppppppppp" + token);
		CustomProgressDialog.startProgressDialog(this);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		myHttpUtils.getHttpJsonString(params, Constans.setUpUrl, handler,
				context, flag_get_setting, Constans.thod_Get_0);
	}

	public void initView() {
		mySwitchLinstener = new MySwitchLinstener();
		switch_order_information = (SwitchButton) findViewById(R.id.switch_order_information);
		switch_order_information.setImageResource(
				R.drawable.personal_setting_btn_h,
				R.drawable.personal_setting_btn_n, R.drawable.btn_pressed);
		switch_order_information.setSwitchState(false);
		switch_order_information.setOnSwitchListener(mySwitchLinstener);

		switch_general_information = (SwitchButton) findViewById(R.id.switch_general_information);
		switch_general_information.setImageResource(
				R.drawable.personal_setting_btn_h,
				R.drawable.personal_setting_btn_n, R.drawable.btn_pressed);
		switch_general_information.setSwitchState(false);
		switch_general_information.setOnSwitchListener(mySwitchLinstener);

		switch_friends_information = (SwitchButton) findViewById(R.id.switch_friends_information);
		switch_friends_information.setImageResource(
				R.drawable.personal_setting_btn_h,
				R.drawable.personal_setting_btn_n, R.drawable.btn_pressed);
		switch_friends_information.setSwitchState(false);
		switch_friends_information.setOnSwitchListener(mySwitchLinstener);

		switch_company_information = (SwitchButton) findViewById(R.id.switch_company_information);
		switch_company_information.setImageResource(
				R.drawable.personal_setting_btn_h,
				R.drawable.personal_setting_btn_n, R.drawable.btn_pressed);
		switch_company_information.setSwitchState(false);
		switch_company_information.setOnSwitchListener(mySwitchLinstener);
	}

	class MySwitchLinstener implements OnSwitchListener {
		@Override
		public void onSwitched(View view, boolean isSwitchOn) {
			switch (view.getId()) {
			case R.id.switch_order_information:
				temp = "displayOrder";
				// Constans.Toast(PrivacySettingActivity.this, "1");
				break;
			case R.id.switch_general_information:
				temp = "openGeneralInfo";
				// Constans.Toast(PrivacySettingActivity.this, "2");
				break;
			case R.id.switch_friends_information:
				temp = "openSeniorInfo";
				// Constans.Toast(PrivacySettingActivity.this, "3");
				break;
			case R.id.switch_company_information:
				temp = "allowAddFriend";
				// Constans.Toast(PrivacySettingActivity.this, "4");
				break;
			}
			if (isSwitchOn) {
				flag = 1;
			} else {
				flag = 0;
			}

			/*
			 * http.ChangeSetUp(temp, flag, token, handler,
			 * PrivacySettingActivity.this);
			 */

			RequestParams params = new RequestParams();
			params.addQueryStringParameter("token", token);
			params.addQueryStringParameter(temp, flag + "");
			myHttpUtils.getHttpJsonString(params, Constans.changeSetUpUrl,
					handler, context, flag_change_setting, Constans.thod_Get_0);

		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case flag_change_setting:
				Constans.Toast(PrivacySettingActivity.this, "修改设置成功");
				break;
			case -1:
				Constans.Toast(PrivacySettingActivity.this, "系统繁忙");
				break;
			case 40002:
				Constans.Toast(PrivacySettingActivity.this, "参数错误");
				break;
			case flag_get_setting:
				CustomProgressDialog.stopProgressDialog();
				// Constans.Toast(PrivacySettingActivity.this, "设置成功");

				try {
					JSONObject ob = (JSONObject) msg.obj;
					System.out.println("ddddddddddddddd" + ob);
					// String displaySetUp = ob.getString("displaySetUp");
					// JSONArray displaySetUpArray = new
					// JSONArray(displaySetUp);
					JSONObject display = ob.getJSONObject("displaySetUp");
					//
					displayOrder = display.getInt("displayOrder");
					switch_order_information.setSwitchState(Boolean.valueOf(""
							+ displayOrder));

					switch_order_information.setChange(displayOrder);

					openGeneralInfo = display.getInt("openGeneralInfo");

					switch_general_information.setSwitchState(Boolean
							.valueOf("" + openGeneralInfo));

					switch_general_information.setChange(openGeneralInfo);

					openSeniorInfo = display.getInt("openSeniorInfo");

					switch_friends_information.setSwitchState(Boolean
							.valueOf("" + openSeniorInfo));

					switch_friends_information.setChange(openSeniorInfo);

					allowAddFriend = display.getInt("allowAddFriend");

					switch_company_information.setSwitchState(Boolean
							.valueOf("" + allowAddFriend));

					switch_company_information.setChange(allowAddFriend);

					memberId = display.getString("memberId");

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}

		};
	};
}
