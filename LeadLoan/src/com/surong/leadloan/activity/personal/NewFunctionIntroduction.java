package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surong.leadload.api.data.DownloadeManager;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;

public class NewFunctionIntroduction extends CommonActivity {
	private View view;
	private Context context;
	protected MyHttpUtils myHttpUtils;
	private TextView version, versionName, download;
	private LinearLayout linear_about1, linear_about2, linear_about3;

	private String uploadUri;
	private TextView newVersion;
	private String message = "";
	private String name;
	private String versionNames;
	private int versionCode;
	private TextView my_textview;
	private TextView fuction_introduction;
	private String introduction;
	private TextView version_update_textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.new_fuctionintroduction, null);
		addContentView(view);
		changeTitle("版本更新");
		introduction = getIntent().getStringExtra("introduction");
		name = getIntent().getStringExtra("name");
		uploadUri = getIntent().getStringExtra("uploadUri");
		// my_textview = (TextView)view.findViewById(R.id.my_textview);
		fuction_introduction = (TextView) view
				.findViewById(R.id.fuction_introduction);
		version_update_textview = (TextView) view
				.findViewById(R.id.version_update_textview);
		fuction_introduction.setText("　　" + introduction.trim());
		version_update_textview.setOnClickListener(this);
		// my_textview.setBackgroundColor(Color.argb(255, 47, 117, 211));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.version_update_textview:
			DownloadeManager dm = new DownloadeManager(
					NewFunctionIntroduction.this, name, uploadUri);
			dm.showDownloadDialog();
			break;

		default:
			break;
		}
	}

}
