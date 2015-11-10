package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class AboutActivity extends CommonActivity {
	private View view;
	private Context context;
	private LinearLayout relative_about1, relative_about2, relative_about3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.about_us2, null);
		addContentView(view);
		changeTitle("关于我们");
		init();
	}

	private void init() {
		relative_about1 = (LinearLayout) findViewById(R.id.relative_about1);
		relative_about2 = (LinearLayout) findViewById(R.id.relative_about2);
		relative_about3 = (LinearLayout) findViewById(R.id.relative_about3);
		relative_about1.setOnClickListener(this);
		relative_about2.setOnClickListener(this);
		relative_about3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.relative_about1:
			startActivity(new Intent(AboutActivity.this,
					AboutReceiveLoanActivity.class));
			break;
		case R.id.relative_about2:
			// startActivity(new Intent(AboutActivity.this,TextActivity.class));
			break;
		case R.id.relative_about3:
			startActivity(new Intent(AboutActivity.this,
					AboutReceiveLoanActivity.class));

		default:
			break;
		}
	}
}
