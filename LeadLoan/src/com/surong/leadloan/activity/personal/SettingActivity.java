package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.easemob.chat.EMChatManager;
import com.lidroid.xutils.exception.DbException;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.start.LoginActivity;
import com.surong.leadloan.ui.ShowDialog;

public class SettingActivity extends CommonActivity {

	private View view;
	private Context context;
	private LinearLayout relative_setting, relative_change_password,
			relative_exit;
	private ShowDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.setting, null);
		addContentView(view);
		changeTitle("设置");

		init();

	}

	private void init() {
		dialog = new ShowDialog(context, handler);
		relative_setting = (LinearLayout) findViewById(R.id.relative_setting);
		relative_change_password = (LinearLayout) findViewById(R.id.relative_change_password);
		relative_exit = (LinearLayout) findViewById(R.id.relative_exit);
		relative_setting.setOnClickListener(this);
		relative_change_password.setOnClickListener(this);
		relative_exit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.relative_back:
			finish();
			break;
		case R.id.relative_setting:
			startActivity(new Intent(SettingActivity.this,
					PrivacySettingActivity.class));
			break;
		case R.id.relative_change_password:
			startActivity(new Intent(SettingActivity.this, ChangePassword.class));
			break;
		case R.id.relative_exit:
			toExit();
			break;
		default:
			break;
		}
	}

	// 显示退出dialog
	public void toExit() {
		View viewExit = View
				.inflate(context, R.layout.exit_system_dialog, null);

		dialog.createDialog(viewExit, new int[] { R.id.text_Exit,
				R.id.text_fosi }, ShowDialog.TYPE_BOTTOM);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 退出登录
			case R.id.text_Exit:
				Intent intent = new Intent();
				intent.setClass(SettingActivity.this, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				// 退出环信
				EMChatManager.getInstance().logout();// 此方法为同步方法
				dialog.dismiss();
				finish();
				try {
					db.dropTable(Personal.class);
				} catch (DbException e) {
					e.printStackTrace();
				}

				break;
			// 隐藏dialog
			case R.id.text_fosi:
				dialog.dismiss();
				break;
			}
		};
	};

}
