package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class SecuritieExchange extends CommonActivity {
	private View view;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.securitie_exchange, null);
		addContentView(view);
		changeTitle("点劵兑换");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		default:
			break;
		}
	}

}
