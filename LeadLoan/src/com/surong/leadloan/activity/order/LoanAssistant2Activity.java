package com.surong.leadloan.activity.order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class LoanAssistant2Activity extends CommonActivity implements
		OnClickListener {
	View view;
	private Context context;
	private LinearLayout quickRecom, classificationRecom;
	private ImageView ImageView1;
	 private static final String SHAREDPREFERENCES = "config";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = LayoutInflater.from(context).inflate(R.layout.loan_assistant2,
				null);
		addContentView(view);
		changeTitle("帮助");
		  SharedPreferences preferences = getSharedPreferences(
				  SHAREDPREFERENCES, MODE_PRIVATE);
	        Editor editor = preferences.edit();
	        // 存入数据
	        editor.putBoolean("first_is", false);
	        // 提交修改
	        editor.commit();
		// myHttpUtils.myInstance();
		// token = SharedPreferencesHelp.getString(context, "token");
		initView();
	}

	private void initView() {
		ImageView1 = (ImageView) view.findViewById(R.id.ImageView1);
		ImageView1.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ImageView1:// 快速推荐
			startActivity(new Intent(this, QuickRecommendationActivity.class));
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout); // 
				// overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
				// overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			finish();
			break;
		default:
			break;
		}
	}
}
