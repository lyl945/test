package com.surong.leadloan.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;

public class SetPwdActivity extends CommonActivity implements OnClickListener {

	private Button btn;
	private String mobile;
	private String code, newPsd;
	private EditText edi_pwd, edi_again_pwd;
	private Context context;
	private RelativeLayout rela_back;
	private View view;
	private MyHttpUtils myHttpUtils;
	private RequestParams params;
	Bundle bun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = View.inflate(this, R.layout.set_pwd, null);
		super.onCreate(savedInstanceState);
		addContentView(view);
		changeTitle("重置密码");
		context = this;
		myHttpUtils.myInstance();
		bun = this.getIntent().getExtras();

		edi_pwd = (EditText) findViewById(R.id.edi_newPwd);
		edi_again_pwd = (EditText) findViewById(R.id.edi_eagin_pwd);

		btn = (Button) findViewById(R.id.btn_revisePwd);
		btn.setOnClickListener(this);
	}

	private void resetPassword() {
		mobile = bun.getString("mobile");
		code = bun.getString("code");
		newPsd = edi_pwd.getText().toString();
		params = new RequestParams();
		params.addQueryStringParameter("mobile", mobile);
		params.addQueryStringParameter("newPsd", newPsd);
		params.addQueryStringParameter("checkCode", code);
		if (!isPwdSame()) {
			return;
		} else {
			CustomProgressDialog.startProgressDialog(context);
			myHttpUtils.getHttpJsonString(params, Constans.resetUrl, handler,
					context, 0, Constans.thod_Get_0);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_revisePwd:
			resetPassword();
			break;

		default:
			break;
		}

	}
	

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			Intent i = context.getPackageManager().getLaunchIntentForPackage(
					context.getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);
			Constans.Toast(context, "修改成功");

		};
	};

	// 判断两次密码输入是否一致
	public boolean isPwdSame() {
		String newPassword = edi_pwd.getText().toString().trim();
		String agePwd = edi_again_pwd.getText().toString().trim();
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
}
