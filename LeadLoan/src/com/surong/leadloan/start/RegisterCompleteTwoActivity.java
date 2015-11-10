package com.surong.leadloan.start;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.easemob.chatuidemo.res.EASEConstants;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.AddressBookGet;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.ui.AutoLineFeedLayout;
import com.surong.leadloan.ui.MyButton;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;

public class RegisterCompleteTwoActivity extends CommonActivity implements
		TextWatcher {

	private View view;
	private Context context;
	private Button btn_next;
	// 了解渠道
	private RadioButton[] btnWay;
	private LinearLayout way_lin;//
	private EditText wayEdi;
	// 兴趣爱好
	private Button[] btnInterest;
	private LinearLayout Interest_lin;//
	private EditText InterestEdi;
	// 提供服务
	private Button[] serviceButtons;
	private EditText edi_service;

	private MyOnClickListener myOnClickListener;
	private boolean isClick;

	private MyHttpUtils myHttpUtils;

	private String knowUsWay = "", hobby = "", expectService = "";
	private final int flag_register_two = 0;
	Map<String, String> knowUsWayMap = new HashMap<String, String>();
	Map<String, String> hobbyMap = new HashMap<String, String>();
	Map<String, String> expectServiceMap = new HashMap<String, String>();
	private AutoLineFeedLayout knowUsAutoLayout, hobbyLayout,
			expectServiceLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.register_two, null);
		addContentView(view);
		changeTitle("完善资料");
		setRight("跳过");
		context = this;
		findView();
		initData();
		initAction();
		initMybutton();
	}

	private void findView() {
		btn_next = (Button) view.findViewById(R.id.btn_next);
		// 了解我们的渠道
		btnWay = new RadioButton[5];
		/*
		 * for(int i= 0 ;i<btnWay.length;i++){ btnWay[i] = new MyButton(context,
		 * i); }
		 */
		btnWay[0] = (RadioButton) view.findViewById(R.id.way_0);
		btnWay[1] = (RadioButton) view.findViewById(R.id.way_1);
		btnWay[2] = (RadioButton) view.findViewById(R.id.way_2);
		btnWay[3] = (RadioButton) view.findViewById(R.id.way_3);
		btnWay[4] = (RadioButton) view.findViewById(R.id.way_4);
		way_lin = (LinearLayout) view.findViewById(R.id.way_Lin);
		wayEdi = (EditText) view.findViewById(R.id.way_edit);
		// 兴趣爱好
		btnInterest = new Button[12];
		/*
		 * for(int i= 0 ;i<btnInterest.length;i++){ btnInterest[i] = new
		 * MyButton(context, i); }
		 */
		btnInterest[0] = (Button) view.findViewById(R.id.interest_0);
		btnInterest[1] = (Button) view.findViewById(R.id.interest_1);
		btnInterest[2] = (Button) view.findViewById(R.id.interest_2);
		btnInterest[3] = (Button) view.findViewById(R.id.interest_3);
		btnInterest[4] = (Button) view.findViewById(R.id.interest_4);
		btnInterest[5] = (Button) view.findViewById(R.id.interest_5);
		btnInterest[6] = (Button) view.findViewById(R.id.interest_6);
		btnInterest[7] = (Button) view.findViewById(R.id.interest_7);
		btnInterest[8] = (Button) view.findViewById(R.id.interest_8);
		btnInterest[9] = (Button) view.findViewById(R.id.interest_9);
		btnInterest[10] = (Button) view.findViewById(R.id.interest_10);
		btnInterest[11] = (Button) view.findViewById(R.id.interest_11);

		InterestEdi = (EditText) view.findViewById(R.id.interest_edit);

		serviceButtons = new Button[7];
		serviceButtons[0] = (Button) view.findViewById(R.id.service_1);
		serviceButtons[1] = (Button) view.findViewById(R.id.service_2);
		serviceButtons[2] = (Button) view.findViewById(R.id.service_3);
		serviceButtons[3] = (Button) view.findViewById(R.id.service_4);
		serviceButtons[4] = (Button) view.findViewById(R.id.service_5);
		serviceButtons[5] = (Button) view.findViewById(R.id.service_6);
		serviceButtons[6] = (Button) view.findViewById(R.id.service_7);
		edi_service = (EditText) view.findViewById(R.id.edi_service);
	}

	private void initData() {
		myOnClickListener = new MyOnClickListener();
		myHttpUtils = MyHttpUtils.myInstance();
	}

	private void initAction() {
		btn_next.setOnClickListener(this);
		for (int i = 0; i < btnWay.length; i++) {
			btnWay[i].setOnClickListener(new WayListener());
			btnWay[i].addTextChangedListener(this);

		}
		for (int i = 0; i < btnInterest.length; i++) {
			btnInterest[i].setOnClickListener(myOnClickListener);

		}
		for (int i = 0; i < serviceButtons.length; i++) {
			serviceButtons[i].setOnClickListener(myOnClickListener);
		}
		wayEdi.addTextChangedListener(this);
		InterestEdi.addTextChangedListener(this);
		edi_service.addTextChangedListener(this);

	}

	private class WayListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			CompoundButton button = (CompoundButton) v;
			if (button.getTag() == null) {
				clearAll(btnWay, button.getId());
				button.setChecked(true);
				button.setTag(true);
				button.setTextColor(Color.rgb(38, 71, 103));
				button.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.shape_red_bg));
			} else {
				button.setChecked(false);
				button.setTag(null);
				button.setTextColor(getResources().getColor(
						R.color.register_text_black));
				button.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.shape_bg));
			}
		}

	}

	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			btn_next.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.next_button1));

			if (v.getTag() == null || v.getTag().equals(false)) {
				v.setTag(true);
				((Button) v).setTextColor(Color.rgb(38, 71, 103));
				v.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.shape_red_bg));
			} else {
				v.setTag(false);
				((Button) v).setTextColor(getResources().getColor(
						R.color.register_text_black));
				v.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.shape_bg));
			}

		}

	}

	public void clearAll(RadioButton button[], int id) {
		for (int i = 0; i < button.length; i++) {
			if (button[i].getId() != id) {
				button[i].setChecked(false);
				button[i].setTag(null);
				button[i].setTextColor(getResources().getColor(
						R.color.register_text_black));
				button[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.shape_bg));
			}
		}
	}

	private void initMybutton() {
		// 了解途径
		knowUsAutoLayout = (AutoLineFeedLayout) findViewById(R.id.know_us_way);
		knowUsAutoLayout.removeAllViews();
		Map<String, String> knowUsWayMap = MyApplication.dictionaryMap2
				.get("know_us_way");
		Iterator<Entry<String, String>> iterator1 = knowUsWayMap.entrySet()
				.iterator();
		while (iterator1.hasNext()) {
			MyButton button1 = (MyButton) View.inflate(context, R.layout.c,
					null);
			button1.setTextColor(Color.argb(255, 51, 51, 51));
			Entry<String, String> entry = iterator1.next();
			button1.setId(Integer.valueOf(entry.getValue()));
			button1.setText(entry.getKey());
			button1.setTag("false");
			if (knowUsWayMap.get(entry.getValue()) != null) {
				button1.setBackgroundResource(R.drawable.shape_red_bg);
				button1.setTextColor(Color.rgb(38, 71, 103));
				button1.setTag("true");
			}
			// button1.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// Button button = (Button) v;
			// if ("true".equals(v.getTag())) {
			// v.setTag("false");
			// v.setBackgroundDrawable(getResources().getDrawable(
			// R.drawable.shape_bg));
			// button.setTextColor(Color.argb(255, 51, 51, 51));
			//
			// } else {
			// v.setTag("true");
			// v.setBackgroundDrawable(getResources().getDrawable(
			// R.drawable.shape_red_bg));
			// button.setTextColor(Color.rgb(38, 71, 103));
			// }
			// }
			// });
			knowUsAutoLayout.addView(button1);
		}
		for (int i = 0; i < knowUsWayMap.size(); i++) {
			final int num = knowUsWayMap.size();
			final int k = i;
			knowUsAutoLayout.getChildAt(k).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							for (int j = 0; j < num; j++) {
								Button button = (Button) knowUsAutoLayout
										.getChildAt(j);
								knowUsAutoLayout.getChildAt(j)
										.setBackgroundDrawable(
												getResources().getDrawable(
														R.drawable.shape_bg));
								button.setTextColor(Color.argb(255, 51, 51, 51));
								if (j != k) {
									button.setTag("false");
								}
							}

							Button button = (Button) knowUsAutoLayout
									.getChildAt(k);
							if (button.getTag().equals("false")) {
								button.setTextColor(Color.rgb(38, 71, 103));
								knowUsAutoLayout
										.getChildAt(k)
										.setBackgroundDrawable(
												getResources()
														.getDrawable(
																R.drawable.shape_red_bg));
								button.setTag("true");

							} else if (button.getTag().equals("true")) {

								knowUsAutoLayout.getChildAt(0)
										.setBackgroundDrawable(
												getResources().getDrawable(
														R.drawable.shape_bg));
								button.setTextColor(Color.argb(255, 51, 51, 51));
								button.setTag("false");
							}
						}
					});
		}

		// 兴趣爱好

		hobbyLayout = (AutoLineFeedLayout) findViewById(R.id.hobby);
		hobbyLayout.removeAllViews();
		Map<String, String> hobbysMap = MyApplication.dictionaryMap2
				.get("hobby");
		Iterator<Entry<String, String>> iteratorHobby = hobbysMap.entrySet()
				.iterator();
		// 添加兴趣爱好button集合
		while (iteratorHobby.hasNext()) {
			MyButton button = (MyButton) View
					.inflate(context, R.layout.c, null);
			button.setTextColor(Color.argb(255, 51, 51, 51));
			Entry<String, String> entry = iteratorHobby.next();
			button.setId(Integer.valueOf(entry.getValue()));
			button.setText(entry.getKey());
			if (hobbyMap.get(entry.getValue()) != null) {
				button.setBackgroundResource(R.drawable.shape_red_bg);
				button.setTextColor(Color.rgb(38, 71, 103));
				button.setTag(true);
			}

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Button button = (Button) v;
					if ("true".equals(v.getTag())) {
						v.setTag("false");
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.shape_bg));
						button.setTextColor(Color.argb(255, 51, 51, 51));

					} else {
						v.setTag("true");
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.shape_red_bg));
						button.setTextColor(Color.rgb(38, 71, 103));
					}
				}
			});
			hobbyLayout.addView(button);
		}

		// 更多服务
		expectServiceLayout = (AutoLineFeedLayout) findViewById(R.id.expect_service);
		expectServiceLayout.removeAllViews();
		Map<String, String> expectServicesMap = MyApplication.dictionaryMap2
				.get("expect_service");
		Iterator<Entry<String, String>> iteratorService = expectServicesMap
				.entrySet().iterator();
		// 添加更多服务button集合
		while (iteratorService.hasNext()) {
			MyButton button = (MyButton) View
					.inflate(context, R.layout.c, null);
			Entry<String, String> entry = iteratorService.next();
			button.setId(Integer.valueOf(entry.getValue()));
			button.setText(entry.getKey());
			if (expectServiceMap.get(entry.getValue()) != null) {
				button.setBackgroundResource(R.drawable.shape_red_bg);
				button.setTextColor(Color.WHITE);
				button.setTag("true");
			}

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Button button = (Button) v;
					if ("true".equals(v.getTag())) {
						v.setTag("false");
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.shape_bg));
						button.setTextColor(Color.argb(255, 51, 51, 51));

					} else {
						v.setTag("true");
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.shape_red_bg));
						button.setTextColor(Color.rgb(38, 71, 103));
					}
				}
			});
			expectServiceLayout.addView(button);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:

			RequestParams params = new RequestParams();

			if (getAll()) {
				String token = SharedPreferencesHelp
						.getString(context, "token");
				CustomProgressDialog.startProgressDialog(this);
				params.addQueryStringParameter("token", token);
				params.addQueryStringParameter("knowUsWay", knowUsWay);
				params.addQueryStringParameter("hobby", hobby);
				params.addQueryStringParameter("expectService", expectService);
				myHttpUtils.getHttpJsonString(params,
						Constans.registerTwo,
						// token=9779641542ed411ca26ccbe61b6846cc, knowUsWay=,
						// hobby=4;9-哦而了, expectService=
						handler, context, flag_register_two,
						Constans.thod_Get_0);
			}

			break;
		case R.id.right:
			startActivity(new Intent(RegisterCompleteTwoActivity.this,
					AddressBookGet.class));
			finish();
			break;
		default:
			break;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case flag_register_two:
				JSONObject object = (JSONObject) msg.obj;
				CustomProgressDialog.stopProgressDialog();
				try {
					String token = object.getString("token");
					LDApplication.getInstance().addSessionData(
							EASEConstants.TOKEN, token);
					SharedPreferencesHelp.SavaString(context, "token", token);
					Personal personal = Utils.JsonGetPersonal(object);
					Utils.saveOrUpdatePersonal(personal, db);
					Utils.saveOrUpdateEaseUser(object.getJSONObject("member"),
							context);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Constans.Toast(context, "完善资料成功");
				startActivity(new Intent(RegisterCompleteTwoActivity.this,
						AddressBookGet.class));
				break;

			default:
				break;
			}
		};
	};

	private boolean getAll() {
		hobby = "";
		for (int i = 0; i < hobbyLayout.getChildCount(); i++) {
			MyButton button = (MyButton) hobbyLayout.getChildAt(i);
			if ("true".equals(button.getTag())) {
				hobby += button.getId() + ";";
			}
		}
		if (hobby.length() > 1) {
			hobby = hobby.substring(0, hobby.length() - 1);
		}
		String hobbyString = InterestEdi.getText().toString().trim();
		if (!"".equals(hobbyString)) {
			hobby += "-" + hobbyString;
		}
		// 获得选中的职业标签
		knowUsWay = "";
		for (int i = 0; i < knowUsAutoLayout.getChildCount(); i++) {
			MyButton button1 = (MyButton) knowUsAutoLayout.getChildAt(i);
			if ("true".equals(button1.getTag())) {
				knowUsWay += button1.getId() + ";";
			}
		}
		if (knowUsWay.length() > 1) {
			knowUsWay = knowUsWay.substring(0, knowUsWay.length() - 1);
		}
		String waysString = wayEdi.getText().toString().trim();
		if (!"".equals(waysString)) {
			knowUsWay += "-" + waysString;
		}
		expectService = "";
		for (int i = 0; i < expectServiceLayout.getChildCount(); i++) {
			MyButton button1 = (MyButton) expectServiceLayout.getChildAt(i);
			if ("true".equals(button1.getTag())) {
				expectService += button1.getId() + ";";
			}
		}
		if (expectService.length() > 1) {
			expectService = expectService.substring(0,
					expectService.length() - 1);
		}
		String expectServiceString = edi_service.getText().toString().trim();
		if (!"".equals(expectServiceString)) {
			expectService += "-" + expectServiceString;
		}
		return true;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		btn_next.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.next_button1));

	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
