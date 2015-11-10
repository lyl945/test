package com.surong.leadloan.activity;

import android.os.Bundle;
import android.view.View;

import com.surong.leadloan.R;

public class AddressBookExplain extends CommonActivity {
	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.address_explain, null);
		addContentView(view);
		changeTitle("功能说明");

	}
}
