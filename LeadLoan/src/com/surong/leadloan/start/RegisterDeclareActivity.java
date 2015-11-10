package com.surong.leadloan.start;

import android.os.Bundle;
import android.view.View;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class RegisterDeclareActivity extends CommonActivity{

	private View view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.declare, null);
		addContentView(view);
		changeTitle("速融100服务条款");
	}
}
