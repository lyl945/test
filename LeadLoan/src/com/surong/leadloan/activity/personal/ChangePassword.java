package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;

public class ChangePassword extends CommonActivity {

	private View view;
	private EditText edi_eagin_pwd, edi_newPwd, edi_oldPwd;
	private String newPassword, oldPassword;
	private Button btn_sure;
	private Context context;
	private RequestParams params;
	private MyHttpUtils myHttpUtils;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.change_password, null);
		addContentView(view);
		context = this;
		changeTitle("修改密码");
		initView();
	}

	private void initView() {

		myHttpUtils = MyHttpUtils.myInstance();
		edi_oldPwd = (EditText) view.findViewById(R.id.edi_oldPwd);
		edi_newPwd = (EditText) view.findViewById(R.id.edi_newPwd);
		edi_eagin_pwd = (EditText) view.findViewById(R.id.edi_eagin_pwd);
		btn_sure = (Button) view.findViewById(R.id.btn_next);
		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isPwdSame()) {
					return;
				}
				CustomProgressDialog.startProgressDialog(context);
				params = new RequestParams();
				params.addQueryStringParameter("token",
						SharedPreferencesHelp.getString(context, "token"));
				params.addQueryStringParameter("oldPassword", edi_oldPwd
						.getText().toString());
				params.addQueryStringParameter("newPassword", newPassword);
				myHttpUtils.getHttpJsonString(params, Constans.changeUrl,
						handler, context, 0, Constans.thod_Get_0);
			}
		});
	}

	// 判断两次密码输入是否一致
	public boolean isPwdSame() {
		oldPassword = edi_oldPwd.getText().toString().trim();
		newPassword = edi_newPwd.getText().toString().trim();
		String agePwd = edi_eagin_pwd.getText().toString().trim();
		if (!newPassword.equals(agePwd)) {
			Constans.Toast(this, "两次输入的密码不一致");
			return false;
		}
		if ("".equals(newPassword)) {
			Constans.Toast(this, "密码不能为空");
			return false;
		} else {
			if (!Constans.isPwdNo(newPassword)) {
				Constans.Toast(this, "请输入八位以上的数字+字母组合");
				return false;
			}
		}
		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			switch (msg.what) {
			case 0:
				Constans.Toast(ChangePassword.this, "修改成功");
				ChangePassword.this.finish();
				Intent i = getPackageManager().getLaunchIntentForPackage(
						getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				break;

			default:
				break;
			}
		};
	};
}
