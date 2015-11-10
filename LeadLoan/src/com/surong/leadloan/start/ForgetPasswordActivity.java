package com.surong.leadloan.start;

import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;

public class ForgetPasswordActivity extends CommonActivity {
	private View view;
	private EditText edit_phone, edit_code;
	private Button get_code, btn_check;
	private String mobile, code, newPwd, agePwd;
	private Context context;
	private MyHttpUtils myHttpUtils;
	private EditText edi_pwd, edi_again_pwd;

	// 定时器
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.forget_password, null);
		addContentView(view);
		context = this;
		changeTitle("重置密码");
		timer = new Timer();
		myHttpUtils = MyHttpUtils.myInstance();
		edit_phone = (EditText) view.findViewById(R.id.edit_phone);
		edit_code = (EditText) view.findViewById(R.id.edit_code);
		get_code = (Button) view.findViewById(R.id.get_code);
		get_code.setOnClickListener(this);
		btn_check = (Button) view.findViewById(R.id.btn_revisePwd);
		btn_check.setOnClickListener(this);
		edi_pwd = (EditText) findViewById(R.id.edi_newPwd);
		edi_again_pwd = (EditText) findViewById(R.id.edi_eagin_pwd);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.get_code:
			mobile = edit_phone.getText().toString();
			getCode();
			break;
		case R.id.btn_revisePwd:
			mobile = edit_phone.getText().toString().trim();
			code = edit_code.getText().toString().trim();
			code = edit_code.getText().toString().trim();
			code = edit_code.getText().toString().trim();
			newPwd = edi_pwd.getText().toString();
			boolean bol = checkAll();
			/*
			 * if (bol) { CustomProgressDialog.startProgressDialog(this);
			 * http.httpFindPwd(mobile, handler, this); }
			 */
			if (bol) {
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("mobile", mobile);
				params.addQueryStringParameter("newPsd", newPwd);
				params.addQueryStringParameter("checkCode", code);
				CustomProgressDialog.startProgressDialog(context);
				myHttpUtils.getHttpJsonString(params, Constans.resetUrl,
						mHandler, context, 2, Constans.thod_Get_0);
			}
			break;

		default:
			break;
		}
	}

	/* 判断信息时间是否过来120秒* */
	private int index = 120;

	/*
	 * 定时器
	 */
	// class MytimerTask extends TimerTask {
	// @Override
	// public void run() {
	// index--;
	// handler.sendMessage(handler.obtainMessage(10, index));
	// }
	// }

	public boolean checkAll() {
		if (!checkPhone() || !isPwdSame()) {
			return false;
		}
		if ("".equals(code) || code.length() != 4) {
			Constans.Toast(this, "验证码输入有误");
			return false;
		}
		return true;
	}

	// 判断两次密码输入是否一致
	public boolean isPwdSame() {
		newPwd = edi_pwd.getText().toString().trim();
		agePwd = edi_again_pwd.getText().toString().trim();
		if (!newPwd.equals(agePwd)) {
			Constans.Toast(this, "两次输入的密码不一致");
			return false;
		}
		if ("".equals(newPwd)) {
			Constans.Toast(this, "密码不能为空");
			return false;
		} else {
			if (!Constans.isPwdNo(newPwd)) {
				Constans.Toast(this, "请输入八位以上的数字+字母组合");
				return false;
			}
		}
		return true;
	}

	private void getCode() {

		if (!checkPhone()) {
			return;
		}
		// if (timer != null) {
		// task = new MytimerTask();
		// timer.schedule(task, 1000, 1000);
		// }
		mHandler.removeMessages(1);
		mHandler.sendEmptyMessageDelayed(1, 1000);

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		CustomProgressDialog.startProgressDialog(context);

		myHttpUtils.getHttpJsonString(params, Constans.forgetPasswordUrl,
				mHandler, context, 0, Constans.thod_Get_0);

	}

	/*
	 * 验证手机号码是否为空
	 */
	public boolean checkPhone() {
		if ("".equals(mobile)) {
			Constans.Toast(this, "手机号码不能为空");
			return false;
		} else {
			if (!Constans.isMobileNO(mobile)) {
				get_code.setEnabled(true);
				Constans.Toast(this, "手机号码格式有误");
				return false;
			}
		}
		return true;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 0:
				JSONObject object = (JSONObject) msg.obj;
				try {
					int code = object.getInt("code");
					if (code == 0) {
						CustomProgressDialog.stopProgressDialog();
						Constans.Toast(ForgetPasswordActivity.this, "验证码已发送");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				index--;
				get_code.setEnabled(false);
				get_code.setBackgroundResource(R.drawable.bj_code);
				get_code.setText(index + "秒");
				if (index == 0) {
					get_code.setText("重新获取");
					get_code.setEnabled(true);
				} else if (index == -1) {
					get_code.setText("获取验证码");
					get_code.setEnabled(true);
					get_code.setBackgroundResource(R.drawable.bj_codea);
				} else {
					mHandler.sendEmptyMessageDelayed(1, 1000);
				}
				break;
			case 2:
				JSONObject object1 = (JSONObject) msg.obj;
				try {
					int code = object1.getInt("code");
					if (code == 0) {
						CustomProgressDialog.stopProgressDialog();
						Intent i = context.getPackageManager()
								.getLaunchIntentForPackage(
										context.getPackageName());
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(i);
						Constans.Toast(context, "修改成功");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}

	};
}
