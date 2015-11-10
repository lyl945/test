package com.surong.leadloan.activity.personal;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.pj.core.managers.LogManager;
import com.pj.core.utilities.StringUtility;
import com.surong.leadload.api.data.DownloadeManager;
import com.surong.leadload.api.data.Friend;
import com.surong.leadload.api.data.VersionNews;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;

public class AboutReceiveLoanActivity extends CommonActivity {
	private View view;
	private Context context;
	protected MyHttpUtils myHttpUtils;
	private TextView version, versionName, download;
	private LinearLayout linear_about1, linear_about2, linear_about3;

	private String uploadUri;
	private TextView newVersion;
	private String message = "";
	private LinearLayout call;
	private String name;
	private String versionNames;
	private int versionCode;
	private RelativeLayout fuction_introductionLayout;
	private String introduction;
	private AlertDialog verificationDialog;
	private TextView t_title, t_message;
	private Button b_queding, b_quxiao;
	private String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.about_us3, null);
		addContentView(view);
		changeTitle("关于领贷");

		try {
			PackageManager pm = getPackageManager();
			PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			versionNames = pinfo.versionName;
			versionCode = pinfo.versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		init();

	}

	private void init() {

		newVersion = (TextView) findViewById(R.id.newVersion);
		call = (LinearLayout) findViewById(R.id.customer_service_phone_textview);
		fuction_introductionLayout = (RelativeLayout) findViewById(R.id.relative_about2);
		fuction_introductionLayout.setOnClickListener(this);
		call.setOnClickListener(this);
		newVersion.setOnClickListener(this);
		// download = (TextView)view.findViewById(R.id.download);
		version = (TextView) view.findViewById(R.id.versions);

		// download = (TextView)view.findViewById(R.id.download);
		version = (TextView) view.findViewById(R.id.versions);
		CustomProgressDialog.startProgressDialog(context);
		versionName = (TextView) view.findViewById(R.id.versionName);
		RequestParams params = new RequestParams();
		myHttpUtils = MyHttpUtils.myInstance();
		myHttpUtils.getHttpJsonString(params, Constans.versionUrl, handler,
				context, 1, Constans.thod_Get_0);
		CustomProgressDialog.startProgressDialog(context);
	}

	private static boolean compare(String newVersion, String oldVersion) {
		if (null == newVersion || null == oldVersion) {
			return false;
		}

		Boolean result = null;

		String[] newVersionArr = newVersion.split("\\.");
		String[] oldVersionArr = oldVersion.split("\\.");

		int index = 0;
		int newVersionArrSize = newVersionArr.length;
		int oldVersionArrSize = oldVersionArr.length;
		while (index < newVersionArrSize && index < oldVersionArrSize) {
			if (Integer.parseInt(newVersionArr[index]) > Integer
					.parseInt(oldVersionArr[index])) {
				result = true;
				break;
			} else {
				if (Integer.parseInt(newVersionArr[index]) < Integer
						.parseInt(oldVersionArr[index])) {
					result = false;
					break;
				}
			}
			index++;
		}
		if (null == result) {
			if (newVersionArrSize > oldVersionArrSize) {
				result = true;
			} else {
				result = false;
			}
		}

		return result;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// xx.setText(msg+"");
			// Toast.makeText(AboutReceiveLoanActivity.this, msg+"",
			// Toast.LENGTH_SHORT).show();

			// 关闭隐藏下拉
			try {
				JSONObject object = (JSONObject) msg.obj;
				String versions = object.getString("versions");
				List<VersionNews> versionNews = Analyze
						.analyzeVersion(versions);
				// versionName.setText(versionNews.get(0).getLog());
				// version.setText(versionNews.get(0).getName() + versionNames);
				version.setText(versionNames + "版本");
				introduction = versionNews.get(0).getIntroduction();
				Boolean isNewVersion = compare(versionNews.get(0).getNumber(),
						versionNames);
				if (!isNewVersion) {
					newVersion.setText("目前已经是最新版本");
				} else {
					newVersion.setText("有新版本点击更新");
				}
				uploadUri = versionNews.get(0).getDownloadLink();
				name = versionNews.get(0).getName();
				CustomProgressDialog.stopProgressDialog();
				// Toast.makeText(AboutReceiveLoanActivity.this, versions+"",
				// Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// List<TimeTable> timeTable1 = Analyze.analyzeCRMO(record);
		};
	};

	@Override
	public void onClick(View v) {

		super.onClick(v);
		switch (v.getId()) {
		case R.id.newVersion:
//			if (newVersion.getText().toString().trim().equals("有新版本点击更新")) {
//
//				Intent ii = new Intent(AboutReceiveLoanActivity.this,
//						NewFunctionIntroduction.class);
//				ii.putExtra("name", name);
//				ii.putExtra("introduction", introduction);
//				ii.putExtra("uploadUri", uploadUri);
//				startActivity(ii);
//			}
			//
			// if (newVersion.getText().toString().trim().equals("有新版本点击更新")) {
			 DownloadeManager dm = new DownloadeManager(
			 AboutReceiveLoanActivity.this, name, uploadUri);
			 dm.showDownloadDialog();
			// }
			//
			break;
		case R.id.relative_about2:
			Intent intent = new Intent(AboutReceiveLoanActivity.this,
					OldFunctionIntroduction.class);
			intent.putExtra("introduction", introduction);
			startActivity(intent);
			break;
		case R.id.customer_service_phone_textview:
			tel = "400-0077-100";
			verificationDialog = new AlertDialog.Builder(context).create();
			verificationDialog.show();
			verificationDialog.getWindow()
					.setContentView(R.layout.cust_diaolog);
			t_title = (TextView) verificationDialog.getWindow().findViewById(
					R.id.title);
			t_message = (TextView) verificationDialog.getWindow().findViewById(
					R.id.text_message);
			b_queding = (Button) verificationDialog.getWindow().findViewById(
					R.id.btn_quedingWf);
			b_quxiao = (Button) verificationDialog.getWindow().findViewById(
					R.id.btn_quXiaoWf);
			t_title.setVisibility(View.GONE);
			t_message.setText(tel);
			b_queding.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (!StringUtility.isEmpty(tel)) {
						tel = tel.replaceAll("\\s+|\\-", "");
						LogManager.i(getClass().getSimpleName(), "打电话:%s", tel);

						Intent intent = new Intent(Intent.ACTION_DIAL, Uri
								.parse("tel:" + tel));
						try {
							context.startActivity(intent);
						} catch (Exception e) {
							Toast.makeText(context, "拨号失败", Toast.LENGTH_LONG)
									.show();

						}
					}
					verificationDialog.dismiss();
				}
			});
			b_quxiao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					verificationDialog.dismiss();
				}
			});
			break;
		default:
			break;
		}

	}
}
