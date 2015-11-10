package com.surong.leadloan.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class SelfRecommendationActivity extends CommonActivity implements
		OnClickListener {

	View view;
	private Context context;
	private TextView quick_recommendation, classification_recommendation;
	private Button btn_infirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.self_recommendation, null);
		addContentView(view);
		changeTitle("自主推荐");
		// myHttpUtils.myInstance();
		// token = SharedPreferencesHelp.getString(context, "token");
		initView();

	}

	private void initView() {

		// btn_infirm = (Button) view.findViewById(R.id.btn_infirm);
		// quick_recommendation = (TextView)
		// findViewById(R.id.quick_recommendation);
		// classification_recommendation = (TextView)
		// findViewById(R.id.classification_recommendation);
		// quick_recommendation.setOnClickListener(this);
		// classification_recommendation.setOnClickListener(this);
		// btn_infirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_infirm:// 快速推荐
			startActivity(new Intent(this, QuickRecommendationActivity2.class));
			break;
		// case R.id.classification_recommendation:// 分类推荐
		//
		// break;
		default:
			break;
		}

	}

}
