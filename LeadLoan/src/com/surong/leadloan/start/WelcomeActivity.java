package com.surong.leadloan.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.surong.leadloan.R;

/**
 * 引导页末页，便于各种监听事件的点击切换
 * 
 * 
 */
public class WelcomeActivity extends Activity implements OnClickListener {

	private Button login, register;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome4);
		init();
	}

	private void init() {
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.login:
			startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
			finish();
			break;
		case R.id.register:
			startActivity(new Intent(WelcomeActivity.this,
					RegisterActivity.class));
			finish();
			break;
		default:
			break;
		}
	}
}