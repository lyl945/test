package com.surong.leadloan.activity.order;

import android.R.integer;
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

public class UploadDataActivity extends CommonActivity implements
		OnClickListener {

	View view;
	private Context context;
	private TextView quick_recommendation, classification_recommendation;
	private LinearLayout[] layouts;
	private CheckBox[] mBoxs;
	boolean[] isCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.upload_data, null);
		addContentView(view);
		changeTitle("上传基础资料");
		setRight("跳过");

		// myHttpUtils.myInstance();
		// token = SharedPreferencesHelp.getString(context, "token");
		initView();

	}

	private void initView() {
		layouts = new LinearLayout[9];
		isCheck = new boolean[9];
		layouts[0] = (LinearLayout) view
				.findViewById(R.id.marriage_linearlayout);
		layouts[1] = (LinearLayout) view
				.findViewById(R.id.family_book_linearlayout);
		layouts[2] = (LinearLayout) view
				.findViewById(R.id.income_certificate_linearlayout);
		layouts[3] = (LinearLayout) view
				.findViewById(R.id.address_proof_linearLayout);
		layouts[4] = (LinearLayout) view
				.findViewById(R.id.assets_certification_linearlayout);
		layouts[5] = (LinearLayout) view
				.findViewById(R.id.sin_card_linearlayout);
		layouts[6] = (LinearLayout) view
				.findViewById(R.id.insurance_policy_linearlayout);
		layouts[7] = (LinearLayout) view
				.findViewById(R.id.personal_credit_report_linearlayout);
		layouts[8] = (LinearLayout) view.findViewById(R.id.others_linearlayout);

		mBoxs = new CheckBox[9];
		mBoxs[0] = (CheckBox) view.findViewById(R.id.marriage_checkbox);
		mBoxs[1] = (CheckBox) view.findViewById(R.id.family_book_checkbox);
		mBoxs[2] = (CheckBox) view
				.findViewById(R.id.income_certificate_checkbox);
		mBoxs[3] = (CheckBox) view.findViewById(R.id.address_proof_checkbox);
		mBoxs[4] = (CheckBox) view
				.findViewById(R.id.assets_certification_checkbox);
		mBoxs[5] = (CheckBox) view.findViewById(R.id.sin_card_checkbox);
		mBoxs[6] = (CheckBox) view.findViewById(R.id.insurance_policy_checkbox);
		mBoxs[7] = (CheckBox) view
				.findViewById(R.id.personal_credit_report_checkbox);
		mBoxs[8] = (CheckBox) view.findViewById(R.id.others_checkbox);

		for (int i = 0; i < mBoxs.length; i++) {
			mBoxs[i].setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right:
			startActivity(new Intent(this, QuickRecommendationActivity2.class));
			finish();
			break;
		case R.id.marriage_checkbox:
			isCheck[0] = !isCheck[0];
			change(0);
			break;
		case R.id.family_book_checkbox:
			isCheck[1] = !isCheck[1];
			change(1);
			break;
		case R.id.income_certificate_checkbox:
			isCheck[2] = !isCheck[2];
			change(2);
			break;
		case R.id.address_proof_checkbox:
			isCheck[3] = !isCheck[3];
			change(3);
			break;
		case R.id.assets_certification_checkbox:
			isCheck[4] = !isCheck[4];
			change(4);
			break;
		case R.id.sin_card_checkbox:
			isCheck[5] = !isCheck[5];
			change(5);
			break;
		case R.id.insurance_policy_checkbox:
			isCheck[6] = !isCheck[6];
			change(6);
			break;
		case R.id.personal_credit_report_checkbox:
			isCheck[7] = !isCheck[7];
			change(7);
			break;
		case R.id.others_checkbox:
			isCheck[8] = !isCheck[8];
			change(8);
			break;

		default:
			break;
		}

	}

	private void change(int i) {
		if (isCheck[i]) {
			layouts[i].setVisibility(view.VISIBLE);
		}else {
			layouts[i].setVisibility(view.GONE);
		}
	}

}
