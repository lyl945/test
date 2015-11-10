package com.surong.leadloan.activity.order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.SharedPreferencesHelp;

public class QuickRecommendationActivity extends CommonActivity implements
		OnClickListener {

	View view;
	private Context context;
	private Button btn_infirm;
	// private PopupWindow mPopupWindow;
	private CheckBox mBox1, mBox2;
	// private TextView loan_subject;
	private EditText custom_name, phone_num, loan_money, loan_date_limit,
			EditText1;
	private TextView city;

	private boolean flag;
	private TextView person_select, company_select;
	protected MyHttpUtils myHttpUtils;
	protected String token;
	// custType 客户类型。01-公司，02-个人
	// * custName 客户姓名
	// * custMobile 客户手机号
	// * applyAmount 贷款金额
	// * applyPeriod 贷款期限
	// isCustKnow 客户知道我为其推荐贷款。Y-是，N-否
	// memo 备注
	private String custType = "02", custName, custMobile, applyAmount,
			applyPeriod, memo;
	private String isCustKnow;
	private int width;
	private TextView txt_1, txt_2;
	private boolean statue1, statue2;
	private String cityName = "深圳市";
	private String cityId = "440300";
	private Map<String, String> maps;
	private PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.quick_recommendation, null);
		addContentView(view);
		changeTitle("快速推荐");
		setRight("帮助");
		
		// myHttpUtils.myInstance();
		// token = SharedPreferencesHelp.getString(context, "token");

		initView();
		init();
		// 获取group的宽度
		ViewTreeObserver vto = city.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				width = city.getMeasuredWidth();
			}
		});

	}

	private void init() {
		token = SharedPreferencesHelp.getString(context, "token");
		myHttpUtils = MyHttpUtils.myInstance();
	}

	private void initView() {

		txt_1 = (TextView) view.findViewById(R.id.txt_1);
		txt_2 = (TextView) view.findViewById(R.id.txt_2);
		btn_infirm = (Button) view.findViewById(R.id.btn_infirm);
		custom_name = (EditText) view.findViewById(R.id.custom_name);
		phone_num = (EditText) view.findViewById(R.id.phone_num);
		loan_money = (EditText) view.findViewById(R.id.loan_money);
		loan_date_limit = (EditText) view.findViewById(R.id.loan_date_limit);
		EditText1 = (EditText) view.findViewById(R.id.EditText1);
		city = (TextView) view.findViewById(R.id.city);
		// loan_subject = (TextView) view.findViewById(R.id.loan_subject);
		city.setOnClickListener(this);
		btn_infirm.setOnClickListener(this);
		// loan_subject.setOnClickListener(this);
		mBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
		mBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
		mBox1.setOnClickListener(this);
		setPricePoint(loan_money);
		mBox2.setOnClickListener(this);
		txt_1.setOnClickListener(this);
		txt_2.setOnClickListener(this);
	}

	private void initMenu(int width) {
		// View menu = getLayoutInflater().inflate(R.layout.quick_select, null,
		// false);
		// mPopupWindow = new PopupWindow(menu,
		// android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		// android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		// mPopupWindow.setWidth(width);
		// mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// person_select = (TextView) menu.findViewById(R.id.person_select);
		// company_select = (TextView) menu.findViewById(R.id.company_select);
		person_select.setOnClickListener(this);
		company_select.setOnClickListener(this);
		// mPopupWindow.setFocusable(true);
		// mPopupWindow.setOutsideTouchable(true);
		// mPopupWindow.update();
	}

	public static void setPricePoint(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				if (!s.toString().trim().contains(".")) {
					if (TextUtils.isEmpty(s.toString().trim())) {

					} else {
						if (Long.valueOf(s.toString()) >= 100000.00) {
							s = s.toString().subSequence(0, s.length() - 1);
							editText.setText(s);
							editText.setSelection(s.length());
						}
					}

				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					editText.setText(s);
					editText.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_infirm:// 快速推荐
			if (checkAll()) {
				CustomProgressDialog.startProgressDialog(context);
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("token", token);
				params.addQueryStringParameter("custType", custType);
				params.addQueryStringParameter("cityId", cityId);
				params.addQueryStringParameter("custName", custName);
				params.addQueryStringParameter("custMobile", custMobile);
				params.addQueryStringParameter("applyAmount", applyAmount);
				params.addQueryStringParameter("applyPeriod", applyPeriod);
				params.addQueryStringParameter("memo", memo);
				if (flag) {
					isCustKnow = "是";
				} else {
					isCustKnow = "否";
				}
				params.addQueryStringParameter("isCustKnow", isCustKnow);
				myHttpUtils.getHttpJsonString(params, Constans.saveOrder,
						handler, context, 1, Constans.thod_Get_0);
			}

			break;
		// case R.id.loan_subject:
		// if (!mPopupWindow.isShowing()) {
		// mPopupWindow.showAsDropDown(loan_subject);
		// } else {
		// mPopupWindow.dismiss();
		// }
		// break;
		case R.id.checkBox2:
			flag = !flag;
			break;
		// case R.id.person_select:
		// loan_subject.setText("个人");
		// custType = "02";
		// mPopupWindow.dismiss();
		// break;
		// case R.id.company_select:
		// loan_subject.setText("企业");
		// custType = "01";
		// mPopupWindow.dismiss();
		// break;
		case R.id.txt_1:
			custType = "02";
			txt_1.setTextColor(getResources().getColor(R.color.white));
			txt_1.setBackgroundResource(R.drawable.red_left_shaper);
			txt_2.setTextColor(getResources()
					.getColor(R.color.mshop_text_black));
			txt_2.setBackgroundResource(R.drawable.shape_right);

			break;
		case R.id.txt_2:
			custType = "01";
			txt_2.setTextColor(getResources().getColor(R.color.white));
			txt_2.setBackgroundResource(R.drawable.red_right_shaper);
			txt_1.setTextColor(getResources()
					.getColor(R.color.mshop_text_black));
			txt_1.setBackgroundResource(R.drawable.shape_left);

			break;
		case R.id.city:
			showCity(width);
			if (!mPopupWindow.isShowing()) {
				mPopupWindow.showAsDropDown(city);
			} else {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.text_right:
			startActivity(new Intent(QuickRecommendationActivity.this,LoanAssistant2Activity.class));
			break;
		default:
			break;
		}

	}

	private void showCity(int width) {
		maps = new HashMap<String, String>();

		View menu = getLayoutInflater().inflate(R.layout.quick_select, null,
				false);
		mPopupWindow = new PopupWindow(menu,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setWidth(width);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		LinearLayout cityView = (LinearLayout) menu
				.findViewById(R.id.city_select);
		cityView.removeAllViews();
		maps = MyApplication.dictionaryMap2.get("loan_assi_city");
		Iterator<Map.Entry<String, String>> its = maps.entrySet().iterator();
		while (its.hasNext()) {
			final Map.Entry<String, String> entry = its.next();
			View button = (View) View.inflate(context, R.layout.cc, null);
			TextView content = (TextView) button.findViewById(R.id.button_text);
			View line = (View) button.findViewById(R.id.lines);
			// 截取市

			content.setText(entry.getKey()
					.substring(3, entry.getKey().length()));
			cityView.addView(button);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					cityName = entry.getKey().substring(3,
							entry.getKey().length());
					city.setText(entry.getKey().substring(3,
							entry.getKey().length()));
					cityId = entry.getValue();
					mPopupWindow.dismiss();
				}
			});
		}
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.update();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			switch (msg.what) {
			case 1:
				if (mBox1.isChecked()) {
					startActivity(new Intent(QuickRecommendationActivity.this,
							UploadDataActivity.class));
				} else {
					Intent intent = new Intent(
							QuickRecommendationActivity.this,
							QuickRecommendationActivity2.class);
					intent.putExtra("cityName", cityName);
					intent.putExtra("custType", custType);
					intent.putExtra("custName", custName);
					intent.putExtra("custMobile", custMobile);
					intent.putExtra("applyAmount", applyAmount);
					intent.putExtra("applyPeriod", applyPeriod);
					intent.putExtra("memo", memo);
					intent.putExtra("isCustKnow", isCustKnow);
					startActivity(intent);
					// finish();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 105:
			if (!TextUtils.isEmpty(data.getStringExtra("cityname"))) {
				cityName = data.getStringExtra("cityname");
				city.setText(cityName);
				cityId = data.getStringExtra("cityId");
			}
			break;
		default:
			break;
		}

	};

	private boolean checkAll() {
		if (TextUtils.isEmpty(custom_name.getText().toString())) {
			Constans.Toast(context, "请填写客户名称!");
			return false;
		} else {
			custName = custom_name.getText().toString();
		}
		if (TextUtils.isEmpty(phone_num.getText().toString())) {
			Constans.Toast(context, "请填写手机号码!");
			return false;
		} else if (!Constans.isMobileNO(phone_num.getText().toString())) {
			Constans.Toast(this, "手机号码格式有误");
			return false;
		} else {
			custMobile = phone_num.getText().toString();
		}
		if (TextUtils.isEmpty(loan_money.getText().toString())) {
			Constans.Toast(context, "请填写货贷金额!");

			return false;
		} else {
			applyAmount = loan_money.getText().toString();
		}
		if (TextUtils.isEmpty(loan_date_limit.getText().toString())) {
			Constans.Toast(context, "请填写货贷期限!");

			return false;
		} else {
			applyPeriod = loan_date_limit.getText().toString();
		}
		// if (TextUtils.isEmpty(EditText1.getText().toString())) {
		// Constans.Toast(context, "备注不能空!");
		// return false;
		// } else {
		// memo = EditText1.getText().toString();
		// }
		memo = EditText1.getText().toString();
		return true;
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("不是吧");
		registerReceiver(broadcastReceiver, intentFilter);
	}
}
