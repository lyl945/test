package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class AccountRecharge extends CommonActivity {

	private View view;
	private Context context;
	/*
	 * 判断状态，DATE_DIALOG_ID和SHOW_DATAPICK是时间1控件的状态，
	 * 
	 * DATE_DIALOG_ID2和SHOW_DATAPICK2是时间2控件的状态
	 */
	private static final int DATE_DIALOG_ID = 1;
	private static final int DATE_DIALOG_ID2 = 3;
	private static final int SHOW_DATAPICK = 0;
	private static final int SHOW_DATAPICK2 = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.account_recharge, null);
		addContentView(view);
		changeTitle("账户充值");
	}
}
