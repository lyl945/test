package com.surong.leadloan.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class QuickRecommendationActivity2 extends CommonActivity implements
		OnClickListener {

	View view;
	private Context context;
	private String custType = "02", custName, custMobile, applyAmount,
			applyPeriod, memo, cityName;
	private TextView mycustType, mycustName, mycustMobile, myapplyAmount,
			myapplyPeriod, mymemo, city;
	private Button sure;
	private String isCustKnow;
	private CheckBox mycheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.quick_recommendation2, null);

		addContentView(view);
		changeTitle("快速推荐");
		// myHttpUtils.myInstance();
		// token = SharedPreferencesHelp.getString(context, "token");
		initView();
		init();

	}

	private void init() {
		custType = getIntent().getExtras().getString("custType");
		custName = getIntent().getExtras().getString("custName");
		custMobile = getIntent().getExtras().getString("custMobile");
		applyAmount = getIntent().getExtras().getString("applyAmount");
		applyPeriod = getIntent().getExtras().getString("applyPeriod");
		isCustKnow = getIntent().getExtras().getString("isCustKnow");
		memo = getIntent().getExtras().getString("memo");
		cityName = getIntent().getExtras().getString("cityName");
		isCustKnow = getIntent().getExtras().getString("isCustKnow");
		if (isCustKnow.trim().equals("是")) {
			mycheckBox.setChecked(true);
		} else {
			mycheckBox.setChecked(false);
		}
		if (custType.trim().equals("02")) {
			mycustType.setText("个人");
		} else {
			mycustType.setText("企业");
		}
		mycustName.setText(custName);
		mycustMobile.setText(custMobile);
		myapplyAmount.setText(applyAmount + "万元");
		myapplyPeriod.setText(applyPeriod + "个月");
		mymemo.setText(memo);
		city.setText(cityName);

	}

	private void initView() {

		mycheckBox = (CheckBox) view.findViewById(R.id.mycheckBox);
		sure = (Button) view.findViewById(R.id.sure);
		sure.setOnClickListener(this);
		mycustType = (TextView) view.findViewById(R.id.custType);
		mycustName = (TextView) view.findViewById(R.id.custName);
		mycustMobile = (TextView) view.findViewById(R.id.custMobile);
		myapplyAmount = (TextView) view.findViewById(R.id.applyAmount);
		myapplyPeriod = (TextView) view.findViewById(R.id.applyPeriod);
		mymemo = (TextView) view.findViewById(R.id.memo);
		city = (TextView) view.findViewById(R.id.city);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.sure:
			sendBroadcast(new Intent("不是吧"));
			startActivity(new Intent(this, ProgressQueryActivity.class));
			finish();
			break;

		default:
			break;
		}
	}
}
