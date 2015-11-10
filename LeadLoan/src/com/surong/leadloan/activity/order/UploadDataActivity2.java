package com.surong.leadloan.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class UploadDataActivity2 extends CommonActivity implements
		OnClickListener {

	View view;
	private Context context;
	private LinearLayout uploaddateLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.upload_data2, null);
		addContentView(view);
		changeTitle("上传基础资料2");
		initView();
		loadData();

	}

	private void loadData() {
		// TODO Auto-generated method stub
		
		
	}

	private void initView() {
		uploaddateLayout =(LinearLayout) view.findViewById(R.id.uploaddate_linearlayout) ;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.right:
//			startActivity(new Intent(this, QuickRecommendationActivity2.class));
//			finish();
//			break;
		default:
			break;
		}

	}

}
