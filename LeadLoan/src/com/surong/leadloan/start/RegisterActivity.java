package com.surong.leadloan.start;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.res.EASEConstants;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;

public class RegisterActivity extends CommonActivity implements
		OnClickListener, TextWatcher {

	private View view;
	private EditText ediText_phone, ediText_pwd, ediText_aginPwd,
			ediText_identifyingCode;
	// 短信验证码，语言验证码，注册按钮
	private Button btn_identifyingCode, btn_soundCode, btn_register;
	private TextView text_hetong;// 服务条款
	private CheckBox check_hetong;// 服务条款，复选框
	private String mobile, password, code, agePwd;
	private int method;// 设置获取验证码方式，0-短信，1-语音
	// 定时器
	private Timer timer;
	private MytimerTask task;

	private Context context;
	private final int flag_message_code = 1;// 短信验证码成功时，返回字段标识；
	private final int flag_register_success = 2;// 注册成功
	// http请求类
	private MyHttpUtils myHttpUtils;

	// private DbUtils db ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.register, null);
		context = this;
		addContentView(view);
		changeTitle("注册");
		findView();
		initData();
		initAction();
	}

	private void findView() {
		ediText_phone = (EditText) view.findViewById(R.id.ediText_phone);
		ediText_pwd = (EditText) view.findViewById(R.id.ediText_pwd);
		ediText_aginPwd = (EditText) view.findViewById(R.id.ediText_aginPwd);
		ediText_identifyingCode = (EditText) view
				.findViewById(R.id.ediText_identifyingCode);
		btn_identifyingCode = (Button) view
				.findViewById(R.id.btn_identifyingCode);
		btn_soundCode = (Button) view.findViewById(R.id.btn_soundCode);
		text_hetong = (TextView) view.findViewById(R.id.text_hetong);

		text_hetong.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		btn_register = (Button) view.findViewById(R.id.btn_register);
		check_hetong = (CheckBox) view.findViewById(R.id.check_hetong);
	}

	private void initData() {
		task = new MytimerTask();
		timer = new Timer();
		myHttpUtils = MyHttpUtils.myInstance();
	}

	private void initAction() {
		btn_identifyingCode.setOnClickListener(this);
		btn_soundCode.setOnClickListener(this);
		text_hetong.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		ediText_phone.addTextChangedListener(this);

	}

	/* 判断信息时间是否过来120秒* */
	private int index = 120;

	/*
	 * 定时器
	 */
	class MytimerTask extends TimerTask {
		@Override
		public void run() {
			index--;
			handler.sendMessage(handler.obtainMessage(10, index));
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			switch (msg.what) {
			case flag_message_code:
				Constans.Toast(RegisterActivity.this, "验证码已发送");
				if (timer != null) {
					task = new MytimerTask();
					timer.schedule(task, 1000, 1000);
				}
				if (method == 0) {
					btn_identifyingCode.setEnabled(false);
				} else {
					btn_soundCode.setEnabled(false);
				}
				break;
			// 定时器返回
			case 10:
				int k = (Integer) msg.obj;
				// String sTime = "重新获取  " + k + "s";
				String sTime = k + "s";
				if (method == 0) {
					btn_identifyingCode.setText(sTime);
				} else {
					btn_soundCode.setText(sTime);
				}

				if (index <= 0) {
					index = 120;
					if (method == 0) {
						btn_identifyingCode.setEnabled(true);
						btn_identifyingCode.setText("重新获取");
					} else {
						btn_soundCode.setEnabled(true);
						btn_soundCode.setText("语音");
					}

					task.cancel();
				}
				break;
			case flag_register_success:
				JSONObject s = (JSONObject) msg.obj;
				try {
					String tokenString = s.getString("token");
					SharedPreferencesHelp.SavaString(context, "token",
							tokenString);
					// 保存member到数据库
					// 登陆环信
					String psd = "", account = "";
					try {
						String token = s.getString("token");
						LDApplication.getInstance().addSessionData(
								EASEConstants.TOKEN, token);
						SharedPreferencesHelp.SavaString(context, "token",
								token);
						Personal personal = Utils.JsonGetPersonal(s);
						Utils.saveOrUpdatePersonal(personal, db);
						Utils.saveOrUpdateEaseUser(s.getJSONObject("member"),
								context);

						psd = personal.getHuanxinPassword();
						account = personal.getHuanxinAccount();

					} catch (JSONException e) {
						e.printStackTrace();
					}
					final String currentUsername = account;
					final String currentPassword = psd;
					// 调用sdk登陆方法登陆聊天服务器
					EMChatManager.getInstance().login(currentUsername,
							currentPassword, new EMCallBack() {

								@Override
								public void onSuccess() {
									// 登陆成功，保存用户名密码
									((LDApplication) LDApplication
											.getInstance())
											.setUserName(currentUsername);
									((LDApplication) LDApplication
											.getInstance())
											.setPassword(currentPassword);
								}

								@Override
								public void onProgress(int progress,
										String status) {
								}

								@Override
								public void onError(final int code,
										final String message) {
									// //
									// loginFailure2Umeng(start,code,message);
									//
									// runOnUiThread(new Runnable() {
									// public void run() {
									// pd.dismiss();
									// Toast.makeText(
									// getApplicationContext(),
									// getString(R.string.Login_failed)
									// + message,
									// Toast.LENGTH_SHORT).show();
									// }
									// });
								}
							});

				} catch (JSONException e) {
					e.printStackTrace();
				}

				Intent intent1 = new Intent(RegisterActivity.this,
						RegisterCompleteOneActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("mobile", mobile);
				bundle.putString("password", password);
				intent1.putExtras(bundle);
				startActivity(intent1);
				Constans.Toast(RegisterActivity.this, "注册成功");
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.text_hetong:
			startActivity(new Intent(RegisterActivity.this,
					RegisterDeclareActivity.class));
			break;
		case R.id.btn_identifyingCode:
			method = 0;
			getCode();
			break;
		case R.id.btn_soundCode:
			method = 1;
			getCode();
		case R.id.btn_register:
			getData();
			boolean bol = checkAll();
			if (bol) {
				RequestParams params2 = new RequestParams();
				CustomProgressDialog.startProgressDialog(this);
				params2.addQueryStringParameter("mobile", mobile);
				params2.addQueryStringParameter("password", password);
				params2.addQueryStringParameter("checkCode", code);
				myHttpUtils.getHttpJsonString(params2, Constans.registerUrl,
						handler, context, flag_register_success,
						Constans.thod_Get_0);
			}
			break;
		case R.id.relative_back:
			finish();

		default:
			break;
		}

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
				btn_identifyingCode.setEnabled(true);
				btn_soundCode.setEnabled(true);
				Constans.Toast(this, "手机号码格式有误");
				return false;
			}
		}
		return true;
	}

	public void getData() {
		mobile = ediText_phone.getText().toString().trim();
		password = ediText_pwd.getText().toString().trim();
		agePwd = ediText_aginPwd.getText().toString().trim();
		code = ediText_identifyingCode.getText().toString().trim();
	}

	/*
	 * 验证所有填空
	 */
	public boolean checkAll() {
		if (!checkPhone()) {
			return false;
		}
		if ("".equals(password)) {
			Constans.Toast(this, "密码不能为空");
			return false;
		} else {
			if (!Constans.isPwdNo(password)) {
				Constans.Toast(this, "密码格式有误");
				return false;
			}
		}
		if (!password.equals(agePwd)) {
			Constans.Toast(this, "两次密码输入不一致");
			return false;
		}

		if ("".equals(code) || code.length() != 4) {
			Constans.Toast(this, "验证码输入有误");
			return false;
		}
		if (!check_hetong.isChecked()) {
			Constans.Toast(this, "请阅读服务条款并勾选");
			return false;
		}
		return true;
	}

	public void getCode() {
		mobile = ediText_phone.getText().toString().trim();
		if (!checkPhone()) {
			return;
		}

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		params.addQueryStringParameter("method", method + "");
		String path = Constans.codeUrl;
		CustomProgressDialog.startProgressDialog(context);

		myHttpUtils.getHttpJsonString(params, path, handler, context,
				flag_message_code, Constans.thod_Get_0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		btn_register.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.next_button1));

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
