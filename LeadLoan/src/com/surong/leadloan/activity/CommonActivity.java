package com.surong.leadloan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.activity.BaseActivity;
import com.lidroid.xutils.DbUtils;
import com.surong.leadloan.R;
import com.surong.leadloan.utils.Utils;

public class CommonActivity extends BaseActivity implements OnClickListener {
	public TextView center_title, text_right;
	public LinearLayout body;
	protected RelativeLayout relative_back;
	private RelativeLayout right;
	public static DbUtils db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_title);

		center_title = (TextView) findViewById(R.id.center_title);
		right = (RelativeLayout) findViewById(R.id.right);
		text_right = (TextView) findViewById(R.id.text_right);
		text_right.setOnClickListener(this);
		right.setOnClickListener(this);
		relative_back = (RelativeLayout) findViewById(R.id.relative_back);
		relative_back.setOnClickListener(this);
		body = (LinearLayout) findViewById(R.id.body);

		relative_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		db = DbUtils.create(this);
		db.configAllowTransaction(true);
		Utils.initData(getApplicationContext());
	}

	public void addContentView(View view) {
		body.addView(view);
	}

	public void changeTitle(String str) {
		center_title.setText(str);
	}

	public void setRight(String str) {
		text_right.setText(str);
		text_right.setTextColor(Color.WHITE);
	}

	public void setRightColor(int color) {
		text_right.setTextColor(color);
	}

	@Override
	public void onClick(View v) {

	}

}
