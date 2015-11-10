package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class MyEvaluate extends CommonActivity {
	private View view;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.apply_certification, null);
		addContentView(view);
		changeTitle("我的评价");

	}
}
