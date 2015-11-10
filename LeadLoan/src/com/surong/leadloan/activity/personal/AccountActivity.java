package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.ui.ShowDialog;

public class AccountActivity extends CommonActivity {
	private View view;
	private Context context;
	private LinearLayout relative_securitie, relative_recharge,
			relative_integral, relative_exchange, relative_description;
	private ShowDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		view = View.inflate(context, R.layout.account_management, null);
		addContentView(view);
		changeTitle("账户管理");
		init();
	}

	private void init() {
		relative_securitie = (LinearLayout) findViewById(R.id.relative_securitie);
		relative_recharge = (LinearLayout) findViewById(R.id.relative_recharge);
		relative_integral = (LinearLayout) findViewById(R.id.relative_integral);
		relative_exchange = (LinearLayout) findViewById(R.id.relative_exchange);
		relative_description = (LinearLayout) findViewById(R.id.relative_description);

		relative_securitie.setOnClickListener(this);
		relative_recharge.setOnClickListener(this);
		relative_integral.setOnClickListener(this);
		relative_exchange.setOnClickListener(this);
		relative_description.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.relative_securitie:
			startActivity(new Intent(AccountActivity.this,
					SecuritiesActivity.class));
			break;
		case R.id.relative_integral:
			startActivity(new Intent(AccountActivity.this,
					IntegrationActivity.class));
			break;
		case R.id.relative_recharge:
			// startActivity(new Intent(AccountActivity.this,
			// UploadActivity.class));
			break;

		case R.id.relative_exchange:
			startActivity(new Intent(AccountActivity.this,
					SecuritieExchange.class));
			break;
		case R.id.relative_description:
			startActivity(new Intent(AccountActivity.this,
					IntegralIntroduction.class));
			break;
		default:
			break;
		}
	}

}
