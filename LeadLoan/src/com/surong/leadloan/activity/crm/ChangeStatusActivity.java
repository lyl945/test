package com.surong.leadloan.activity.crm;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.SharedPreferencesHelp;

/*
 * 修改订单状态
 */
public class ChangeStatusActivity extends CommonActivity {

	private View view;
	private ListView listView;
	private ArrayAdapter<String> arrayAdapter;
	private Context context;
	private List<String> list;
	private TextView[] textViews;
	private CheckBox[] checkBoxs_03, checkBoxs_05, check_04, check_05,
			check_06, check_07, check_08, check_09, check_10, check_11,
			check_12, check_13, check_14, check_15, check_16, check_17,
			check_18, check_19, check_20, check_21, check_22, check_23,
			check_24, check_25, check_26, check_27, check_28, check_29;
	private Map<String, String> map;
	private Map<String, String> reasonMap;
	private Map<String, String> pledgeMap;

	private RadioGroup radioGroup_02;
	private LinearLayout LinearLayout_03, LinearLayout_05, linearLayout_per_06,
			linearLayout_firm_06, linearLayout_firm_08, linearLayout_per_08,
			linearlayout_state_99, radioGroup_11;

	private String reason, token, stateString, main, orderId;
	private String reasons;

	private MyHttpUtils myHttpUtils;

	private Intent dataIntent;

	private RadioButton[] radioButtons_02, radioButtons_11;

	private Button btn_sure;

	private int changeFlag;// 标识修改的订单状态
	private String description;
	private EditText editText_03, editText_05, editText_per_06,
			editText_firm_06, editText_per_08, editText_firm_08, editText_11;
	private EditText main_con, lendAmount, lendPeriod, ratePerMonth, lendDate,
			apply_date;

	private String lendAmountStr, lendPeriodStr, ratePerMonthStr, lendDateStr,
			pledgeTypeStr, repaymntTypeStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		changeTitle("修改订单状态");
		view = View.inflate(this, R.layout.change_state, null);
		getData();
		addContentView(view);
		btn_sure = (Button) view.findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		textViews = new TextView[10];
		textViews[0] = (TextView) view.findViewById(R.id.text_1);
		textViews[1] = (TextView) view.findViewById(R.id.text_2);
		textViews[2] = (TextView) view.findViewById(R.id.text_3);
		textViews[3] = (TextView) view.findViewById(R.id.text_4);
		textViews[4] = (TextView) view.findViewById(R.id.text_5);
		textViews[5] = (TextView) view.findViewById(R.id.text_6);
		textViews[6] = (TextView) view.findViewById(R.id.text_7);
		textViews[7] = (TextView) view.findViewById(R.id.text_8);
		textViews[8] = (TextView) view.findViewById(R.id.text_9);
		textViews[9] = (TextView) view.findViewById(R.id.text_10);
		list = MyApplication.dictionaryMap.get("MAP_ORDER_STATUS");
		for (int i = 0; i < list.size(); i++) {
			textViews[i].setText(list.get(i));
			textViews[i].setOnClickListener(this);
		}

		map = MyApplication.dictionaryMap2.get("MAP_ORDER_STATUS");
		pledgeMap = MyApplication.dictionaryMap2.get("MAP_MORTGAGE_GUARA");
		// String order_status_02_val = map.get("");
		// reasonMap = MyApplication.dictionaryMap2.get(order_status_02_val);
		// 无效电话
		radioGroup_02 = (RadioGroup) view.findViewById(R.id.status_02);
		radioButtons_02 = new RadioButton[4];
		radioButtons_02[0] = (RadioButton) view.findViewById(R.id.status_02_01);
		radioButtons_02[1] = (RadioButton) view.findViewById(R.id.status_02_02);
		radioButtons_02[2] = (RadioButton) view.findViewById(R.id.status_02_03);
		radioButtons_02[3] = (RadioButton) view.findViewById(R.id.status_02_04);

		// 用户考虑中
		LinearLayout_03 = (LinearLayout) view.findViewById(R.id.status_03);
		checkBoxs_03 = new CheckBox[6];
		checkBoxs_03[0] = (CheckBox) view.findViewById(R.id.status_03_01);
		checkBoxs_03[1] = (CheckBox) view.findViewById(R.id.status_03_02);
		checkBoxs_03[2] = (CheckBox) view.findViewById(R.id.status_03_03);
		checkBoxs_03[3] = (CheckBox) view.findViewById(R.id.status_03_04);
		checkBoxs_03[4] = (CheckBox) view.findViewById(R.id.status_03_05);
		checkBoxs_03[5] = (CheckBox) view.findViewById(R.id.status_03_06);

		editText_03 = (EditText) view.findViewById(R.id.editText_03);

		// 用户放弃
		LinearLayout_05 = (LinearLayout) view.findViewById(R.id.status_05);
		checkBoxs_05 = new CheckBox[8];
		checkBoxs_05[0] = (CheckBox) view.findViewById(R.id.status_05_01);
		checkBoxs_05[1] = (CheckBox) view.findViewById(R.id.status_05_02);
		checkBoxs_05[2] = (CheckBox) view.findViewById(R.id.status_05_03);
		checkBoxs_05[3] = (CheckBox) view.findViewById(R.id.status_05_04);
		checkBoxs_05[4] = (CheckBox) view.findViewById(R.id.status_05_05);
		checkBoxs_05[5] = (CheckBox) view.findViewById(R.id.status_05_06);
		checkBoxs_05[6] = (CheckBox) view.findViewById(R.id.status_05_07);
		checkBoxs_05[7] = (CheckBox) view.findViewById(R.id.status_05_08);
		editText_05 = (EditText) view.findViewById(R.id.editText_05);

		// 初审不符合条件
		// 个人
		linearLayout_per_06 = (LinearLayout) view
				.findViewById(R.id.status_per_06);
		check_04 = new CheckBox[3];
		check_04[0] = (CheckBox) view.findViewById(R.id.check_04_01);
		check_04[1] = (CheckBox) view.findViewById(R.id.check_04_02);
		check_04[2] = (CheckBox) view.findViewById(R.id.check_04_03);
		check_05 = new CheckBox[4];
		check_05[0] = (CheckBox) view.findViewById(R.id.check_05_01);
		check_05[1] = (CheckBox) view.findViewById(R.id.check_05_02);
		check_05[2] = (CheckBox) view.findViewById(R.id.check_05_03);
		check_05[3] = (CheckBox) view.findViewById(R.id.check_05_04);
		check_06 = new CheckBox[5];
		check_06[0] = (CheckBox) view.findViewById(R.id.check_06_01);
		check_06[1] = (CheckBox) view.findViewById(R.id.check_06_02);
		check_06[2] = (CheckBox) view.findViewById(R.id.check_06_03);
		check_06[3] = (CheckBox) view.findViewById(R.id.check_06_04);
		check_06[4] = (CheckBox) view.findViewById(R.id.check_06_05);
		check_07 = new CheckBox[1];
		check_07[0] = (CheckBox) view.findViewById(R.id.check_07_01);
		check_08 = new CheckBox[1];
		check_08[0] = (CheckBox) view.findViewById(R.id.check_08_01);
		editText_per_06 = (EditText) view.findViewById(R.id.editText_per_06);

		// 公司
		linearLayout_firm_06 = (LinearLayout) view
				.findViewById(R.id.status_firm_06);
		check_09 = new CheckBox[4];
		check_09[0] = (CheckBox) view.findViewById(R.id.check_09_01);
		check_09[1] = (CheckBox) view.findViewById(R.id.check_09_02);
		check_09[2] = (CheckBox) view.findViewById(R.id.check_09_03);
		check_09[3] = (CheckBox) view.findViewById(R.id.check_09_04);
		check_10 = new CheckBox[2];
		check_10[0] = (CheckBox) view.findViewById(R.id.check_10_01);
		check_10[1] = (CheckBox) view.findViewById(R.id.check_10_02);
		check_11 = new CheckBox[3];
		check_11[0] = (CheckBox) view.findViewById(R.id.check_11_01);
		check_11[1] = (CheckBox) view.findViewById(R.id.check_11_02);
		check_11[2] = (CheckBox) view.findViewById(R.id.check_11_03);
		check_12 = new CheckBox[2];
		check_12[0] = (CheckBox) view.findViewById(R.id.check_12_01);
		check_12[1] = (CheckBox) view.findViewById(R.id.check_12_02);
		check_13 = new CheckBox[1];
		check_13[0] = (CheckBox) view.findViewById(R.id.check_13_01);
		check_14 = new CheckBox[2];
		check_14[0] = (CheckBox) view.findViewById(R.id.check_14_01);
		check_15 = new CheckBox[1];
		check_15[0] = (CheckBox) view.findViewById(R.id.check_15_01);
		editText_firm_06 = (EditText) view.findViewById(R.id.editText_firm_06);

		// 公司审批被拒
		// 个人
		linearLayout_per_08 = (LinearLayout) view
				.findViewById(R.id.status_per_08);
		check_16 = new CheckBox[3];
		check_16[0] = (CheckBox) view.findViewById(R.id.check_16_01);
		check_16[1] = (CheckBox) view.findViewById(R.id.check_16_02);
		check_16[2] = (CheckBox) view.findViewById(R.id.check_16_03);
		check_17 = new CheckBox[4];
		check_17[0] = (CheckBox) view.findViewById(R.id.check_17_01);
		check_17[1] = (CheckBox) view.findViewById(R.id.check_17_02);
		check_17[2] = (CheckBox) view.findViewById(R.id.check_17_03);
		check_17[3] = (CheckBox) view.findViewById(R.id.check_17_04);
		check_18 = new CheckBox[5];
		check_18[0] = (CheckBox) view.findViewById(R.id.check_18_01);
		check_18[1] = (CheckBox) view.findViewById(R.id.check_18_02);
		check_18[2] = (CheckBox) view.findViewById(R.id.check_18_03);
		check_18[3] = (CheckBox) view.findViewById(R.id.check_18_04);
		check_18[4] = (CheckBox) view.findViewById(R.id.check_18_05);
		check_19 = new CheckBox[1];
		check_19[0] = (CheckBox) view.findViewById(R.id.check_19_01);
		check_20 = new CheckBox[1];
		check_20[0] = (CheckBox) view.findViewById(R.id.check_20_01);
		editText_per_08 = (EditText) view.findViewById(R.id.editText_per_08);

		// 公司
		linearLayout_firm_08 = (LinearLayout) view
				.findViewById(R.id.status_firm_08);
		check_21 = new CheckBox[4];
		check_21[0] = (CheckBox) view.findViewById(R.id.check_21_01);
		check_21[1] = (CheckBox) view.findViewById(R.id.check_21_02);
		check_21[2] = (CheckBox) view.findViewById(R.id.check_21_03);
		check_21[3] = (CheckBox) view.findViewById(R.id.check_21_04);
		check_22 = new CheckBox[2];
		check_22[0] = (CheckBox) view.findViewById(R.id.check_22_01);
		check_22[1] = (CheckBox) view.findViewById(R.id.check_22_02);
		check_23 = new CheckBox[3];
		check_23[0] = (CheckBox) view.findViewById(R.id.check_23_01);
		check_23[1] = (CheckBox) view.findViewById(R.id.check_23_02);
		check_23[2] = (CheckBox) view.findViewById(R.id.check_23_03);
		check_24 = new CheckBox[2];
		check_24[0] = (CheckBox) view.findViewById(R.id.check_24_01);
		check_24[1] = (CheckBox) view.findViewById(R.id.check_24_02);
		check_25 = new CheckBox[1];
		check_25[0] = (CheckBox) view.findViewById(R.id.check_25_01);
		check_26 = new CheckBox[1];
		check_26[0] = (CheckBox) view.findViewById(R.id.check_26_01);
		check_27 = new CheckBox[1];
		check_27[0] = (CheckBox) view.findViewById(R.id.check_27_01);
		editText_firm_08 = (EditText) view.findViewById(R.id.editText_firm_08);

		// 放款成功
		linearlayout_state_99 = (LinearLayout) view
				.findViewById(R.id.linearlayout_state_99);
		main_con = (EditText) view.findViewById(R.id.main_con);
		lendAmount = (EditText) view.findViewById(R.id.lendAmount);
		lendPeriod = (EditText) view.findViewById(R.id.lendPeriod);
		ratePerMonth = (EditText) view.findViewById(R.id.ratePerMonth);
		lendDate = (EditText) view.findViewById(R.id.lendDate);
		apply_date = (EditText) view.findViewById(R.id.apply_date);

		check_28 = new CheckBox[12];
		check_28[0] = (CheckBox) view.findViewById(R.id.check_28_01);
		check_28[1] = (CheckBox) view.findViewById(R.id.check_28_02);
		check_28[2] = (CheckBox) view.findViewById(R.id.check_28_03);
		check_28[3] = (CheckBox) view.findViewById(R.id.check_28_04);
		check_28[4] = (CheckBox) view.findViewById(R.id.check_28_05);
		check_28[5] = (CheckBox) view.findViewById(R.id.check_28_06);
		check_28[6] = (CheckBox) view.findViewById(R.id.check_28_07);
		check_28[7] = (CheckBox) view.findViewById(R.id.check_28_08);
		check_28[8] = (CheckBox) view.findViewById(R.id.check_28_09);
		check_28[9] = (CheckBox) view.findViewById(R.id.check_28_10);
		check_28[10] = (CheckBox) view.findViewById(R.id.check_28_11);
		check_28[11] = (CheckBox) view.findViewById(R.id.check_28_12);

		check_29 = new CheckBox[4];
		check_29[0] = (CheckBox) view.findViewById(R.id.check_29_01);
		check_29[1] = (CheckBox) view.findViewById(R.id.check_29_02);
		check_29[2] = (CheckBox) view.findViewById(R.id.check_29_03);
		check_29[3] = (CheckBox) view.findViewById(R.id.check_29_04);

		radioGroup_11 = (LinearLayout) view.findViewById(R.id.status_11);
		radioButtons_11 = new RadioButton[6];
		radioButtons_11[0] = (RadioButton) view.findViewById(R.id.status_11_01);
		radioButtons_11[1] = (RadioButton) view.findViewById(R.id.status_11_02);
		radioButtons_11[2] = (RadioButton) view.findViewById(R.id.status_11_03);
		radioButtons_11[3] = (RadioButton) view.findViewById(R.id.status_11_04);
		radioButtons_11[4] = (RadioButton) view.findViewById(R.id.status_11_05);
		radioButtons_11[5] = (RadioButton) view.findViewById(R.id.status_11_06);

		editText_11 = (EditText) view.findViewById(R.id.editText_11);

	}

	private void getData() {
		myHttpUtils = MyHttpUtils.myInstance();
		token = SharedPreferencesHelp.getString(context, "token");
		dataIntent = new Intent();
		main = getIntent().getStringExtra("main");
		orderId = getIntent().getStringExtra("id");

	}

	private void normalHttp(String reason, int i) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("reason", reason);
		params.addQueryStringParameter("id", orderId);
		if (null != description && !"".equals(description)) {
			params.addQueryStringParameter("description", description);
		}
		String string = map.get(textViews[i - 1].getText().toString());
		params.addQueryStringParameter("status", string);
		myHttpUtils.getHttpJsonString(params, Constans.editOrderUrl, handler,
				context, i - 1, Constans.thod_Get_0);

	}

	private void noReasonHttp(int i) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("id", orderId);
		params.addQueryStringParameter("status",
				map.get(textViews[i - 1].getText().toString()));
		myHttpUtils.getHttpJsonString(params, Constans.editOrderUrl, handler,
				context, i - 1, Constans.thod_Get_0);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Constans.Toast(context, "修改订单状态成功");
			switch (msg.what) {
			case 0:
				stateString = textViews[0].getText().toString();
				break;
			case 1:
				stateString = textViews[1].getText().toString();
				break;
			case 2:
				// { when=-29s288ms what=2
				// obj={"order":{"户籍":{"value":"本地","term":"户籍","termId":"CP9003"},"两年内信用情况":{"value":"信用记录良好","term":"两年内信用情况","termId":"CP9001"},"房产所在地":{"value":"本地","term":"房产所在地","termId":"CP9037"},"status":{"value":"用户考虑中","term":"订单状态","termId":"03"},"reason":{"value":"考虑自筹","term":"原因","termId":"0002-05"},"经营注册地":
				stateString = textViews[2].getText().toString();
				break;
			case 3:
				stateString = textViews[3].getText().toString();
				break;
			case 4:
				stateString = textViews[4].getText().toString();
				break;
			case 5:
				stateString = textViews[5].getText().toString();
				break;
			case 6:
				stateString = textViews[6].getText().toString();
				break;
			case 7:
				stateString = textViews[7].getText().toString();
				break;
			case 8:
				stateString = textViews[8].getText().toString();
				break;
			case 9:
				stateString = textViews[9].getText().toString();
				break;

			default:

				break;
			}
			dataIntent.putExtra("reasons", reasons);
			dataIntent.putExtra("status", stateString);
			setResult(1001, dataIntent);
			finish();
		};
	};

	@Override
	public void onClick(View v) {

		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_sure:

			checkAll();
			break;
		case R.id.text_1:
			changeFlag = 1;
			closeOther();
			textViews[0].setTextColor(getResources()
					.getColor(R.color.title_red));

			// noReasonHttp(0);

			break;
		case R.id.text_2:// 无效电话
			changeFlag = 2;
			closeOther();
			textViews[1].setTextColor(getResources()
					.getColor(R.color.title_red));
			if (radioGroup_02.getVisibility() == 8) {
				radioGroup_02.setVisibility(View.VISIBLE);
				// 设置其他的为不可见和红色文字
			} else {
				if (radioGroup_02.getVisibility() == 0) {
					radioGroup_02.setVisibility(View.GONE);
					// 清楚掉选中状态
					for (int i = 0; i < radioButtons_02.length; i++) {
						radioButtons_02[i].setChecked(false);
					}
				}
			}

			break;

		case R.id.text_3:// 用户考虑中
			changeFlag = 3;
			closeOther();
			textViews[2].setTextColor(getResources()
					.getColor(R.color.title_red));
			if (LinearLayout_03.getVisibility() == 8) {
				LinearLayout_03.setVisibility(View.VISIBLE);
			} else {
				if (LinearLayout_03.getVisibility() == 0) {
					LinearLayout_03.setVisibility(View.GONE);
				}
			}
			break;

		case R.id.text_4:// 正常跟进
			closeOther();

			textViews[3].setTextColor(getResources()
					.getColor(R.color.title_red));
			changeFlag = 4;

			break;

		case R.id.text_5:// 用户放弃
			changeFlag = 5;
			closeOther();
			textViews[4].setTextColor(getResources()
					.getColor(R.color.title_red));
			if (LinearLayout_05.getVisibility() == 8) {
				LinearLayout_05.setVisibility(View.VISIBLE);
			} else {
				if (LinearLayout_05.getVisibility() == 0) {
					LinearLayout_05.setVisibility(View.GONE);
				}
			}
			break;

		case R.id.text_6:
			changeFlag = 6;// 初审不符合条件
			closeOther();
			textViews[5].setTextColor(getResources()
					.getColor(R.color.title_red));
			if ("公司".equals(main)) {// 01代表公司 if
				if (linearLayout_firm_06.getVisibility() == 8) {
					linearLayout_firm_06.setVisibility(View.VISIBLE);
				} else {
					if (linearLayout_firm_06.getVisibility() == 0) {
						linearLayout_firm_06.setVisibility(View.GONE);
					}
				}
			} else {
				if (linearLayout_per_06.getVisibility() == 8) {
					linearLayout_per_06.setVisibility(View.VISIBLE);
				} else {
					if (linearLayout_per_06.getVisibility() == 0) {
						linearLayout_per_06.setVisibility(View.GONE);
					}
				}
			}

			break;

		case R.id.text_7:// 公司审批中
			closeOther();

			textViews[6].setTextColor(getResources()
					.getColor(R.color.title_red));
			changeFlag = 7;

			break;

		case R.id.text_8:// 放款成功99
			changeFlag = 8;
			closeOther();
			textViews[7].setTextColor(getResources()
					.getColor(R.color.title_red));
			if (linearlayout_state_99.getVisibility() == 8) {
				linearlayout_state_99.setVisibility(View.VISIBLE);
			} else {
				if (linearlayout_state_99.getVisibility() == 0) {
					linearlayout_state_99.setVisibility(View.GONE);
				}
			}
			break;

		case R.id.text_9:// 放款失败11
			closeOther();

			textViews[8].setTextColor(getResources()
					.getColor(R.color.title_red));
			changeFlag = 9;

			if (radioGroup_11.getVisibility() == 8) {
				radioGroup_11.setVisibility(View.VISIBLE);
			} else {
				if (radioGroup_11.getVisibility() == 0) {
					radioGroup_11.setVisibility(View.GONE);
				}
			}

			break;

		case R.id.text_10:// 公司审批被拒 08
			changeFlag = 10;
			closeOther();
			textViews[9].setTextColor(getResources()
					.getColor(R.color.title_red));
			if ("公司".equals(main)) {// 01代表公司 if
				if (linearLayout_firm_08.getVisibility() == 8) {
					linearLayout_firm_08.setVisibility(View.VISIBLE);
				} else {
					if (linearLayout_firm_08.getVisibility() == 0) {
						linearLayout_firm_08.setVisibility(View.GONE);
					}
				}
			} else {
				if (linearLayout_per_08.getVisibility() == 8) {
					linearLayout_per_08.setVisibility(View.VISIBLE);
				} else {
					if (linearLayout_per_08.getVisibility() == 0) {
						linearLayout_per_08.setVisibility(View.GONE);
					}
				}
			}

			break;

		default:
			break;
		}
	}

	private void closeOther() {
		for (int i = 0; i < textViews.length; i++) {
			textViews[i].setTextColor(getResources().getColor(
					R.color.black_state));
		}
		radioGroup_02.setVisibility(View.GONE);
		LinearLayout_03.setVisibility(View.GONE);
		LinearLayout_05.setVisibility(View.GONE);
		linearLayout_per_06.setVisibility(View.GONE);
		linearLayout_firm_06.setVisibility(View.GONE);
		linearLayout_firm_08.setVisibility(View.GONE);
		linearLayout_per_08.setVisibility(View.GONE);
		linearlayout_state_99.setVisibility(View.GONE);
		radioGroup_11.setVisibility(View.GONE);
	}

	private void checkAll() {
		switch (changeFlag) {
		case 1:
			noReasonHttp(1);
			break;
		case 2:
			for (int i = 0; i < radioButtons_02.length; i++) {
				if (radioButtons_02[i].isChecked()) {
					reasons = radioButtons_02[i].getText().toString();
					reason = "0001-0" + (i + 1);
				}
			}
			if (null == reason || "".equals(reason)) {
				Constans.Toast(context, "请勾选原因");
			} else {
				normalHttp(reason, 2);
			}
			break;
		case 3:
			reasons = "";
			for (int i = 0; i < checkBoxs_03.length - 2; i++) {

				if (checkBoxs_03[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = checkBoxs_03[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ checkBoxs_03[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0002-0" + (i + 1);
					} else {
						reason += ",0002-0" + (i + 1);
					}
				}
			}
			if (checkBoxs_03[5].isChecked()) {
				if (null == editText_03.getText().toString()
						|| "".equals(editText_03.getText().toString())) {
					Constans.Toast(context, "请填写原因");
					return;
				} else {
					if (reasons == null || "".equals(reasons)) {
						reasons = editText_03.getText().toString();

					} else {
						reasons = reasons + ";"
								+ editText_03.getText().toString();
					}

					description = editText_03.getText().toString();
				}
				if (null == reason || "".equals(reason)) {
					reason += "0002-99";
				} else {
					reason += ",0002-99";
				}
			}

			if (null == reason || "".equals(reason)) {
				Constans.Toast(context, "请勾选原因");
			} else {
				normalHttp(reason, 3);
			}
			break;
		case 4:
			noReasonHttp(4);
			break;

		case 5:
			reasons = "";
			for (int i = 0; i < checkBoxs_05.length - 1; i++) {

				if (checkBoxs_05[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = checkBoxs_05[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ checkBoxs_05[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0003-0" + (i + 1);
					} else {
						reason += ",0003-0" + (i + 1);
					}

				}
			}
			if (checkBoxs_05[7].isChecked()) {
				if (null == editText_05.getText().toString()
						|| "".equals(editText_05.getText().toString())) {
					Constans.Toast(context, "请填写原因");
					return;
				} else {
					if (reasons == null || "".equals(reasons)) {
						reasons = editText_05.getText().toString();
					} else {
						reasons = reasons + ";"
								+ editText_05.getText().toString();
					}

					description = editText_05.getText().toString();
				}
				if (null == reason || "".equals(reason)) {
					reason += "0002-99";
				} else {
					reason += ",0002-99";
				}
			}

			if (null == reason || "".equals(reason)) {
				Constans.Toast(context, "请勾选原因");
			} else {
				normalHttp(reason, 5);
			}
			break;

		case 6:
			// 初审不符合条件
			reasons = "";
			reason = "";

			if (check_08[0].isChecked()) {

				if (null == editText_per_06.getText().toString()
						|| "".equals(editText_per_06.getText().toString())) {
					Constans.Toast(context, "请填写原因");
					return;
				} else {
					reasons = editText_per_06.getText().toString();
					description = editText_per_06.getText().toString();
				}

				if (null == reason || "".equals(reason)) {
					reason += "0015-99";
				} else {
					reason += ",0015-99";
					// 0008-99
				}
			}

			for (int i = 0; i < check_04.length - 1; i++) {

				if (check_04[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = check_04[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ check_04[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0004-0" + (i + 1);
					} else {
						reason += ",0004-0" + (i + 1);
					}
				}
			}
			for (int i = 0; i < check_05.length - 1; i++) {

				if (check_05[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = check_05[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ check_05[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0003-0" + (i + 1);
					} else {
						reason += ",0003-0" + (i + 1);
					}
				}
			}
			for (int i = 0; i < check_06.length - 1; i++) {

				if (check_06[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = check_06[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ check_06[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0003-0" + (i + 1);
					} else {
						reason += ",0003-0" + (i + 1);
					}
				}
			}
			for (int i = 0; i < check_07.length - 1; i++) {

				if (check_07[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = check_07[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ check_07[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0003-0" + (i + 1);
					} else {
						reason += ",0003-0" + (i + 1);
					}
				}
			}

			if (null == reason || "".equals(reason)) {
				Constans.Toast(context, "请勾选原因");
			} else {
				normalHttp(reason, 6);
			}
			break;
		case 7:
			noReasonHttp(7);
			break;
		case 8:// 放款成功
			if (null == lendAmount.getText() || "".equals(lendAmount.getText())) {
				Constans.Toast(context, "请填写贷款金额");
				return;
			} else {
				lendAmountStr = lendAmount.getText().toString();
			}

			if (null == lendPeriod.getText().toString()
					|| "".equals(lendPeriod.getText().toString())) {
				Constans.Toast(context, "请填写贷款期限");
				return;
			} else {
				lendPeriodStr = lendPeriod.getText().toString();
			}
			if (null == ratePerMonth.getText()
					|| "".equals(ratePerMonth.getText())) {
				Constans.Toast(context, "请填写月均总成本");
				return;
			} else {
				ratePerMonthStr = ratePerMonth.getText().toString();
			}

			if (null == lendDate.getText() || "".equals(lendDate.getText())) {
				Constans.Toast(context, "请填放款时间");
				return;
			} else {
				lendDateStr = lendDate.getText().toString();
			}
			pledgeTypeStr = "";
			for (int i = 0; i < check_28.length; i++) {
				if (check_28[i].isChecked()) {
					if ("".equals(pledgeTypeStr)) {
						pledgeTypeStr = pledgeMap.get(check_28[i].getText()
								.toString());
					} else {
						pledgeTypeStr = ","
								+ pledgeMap.get(check_28[i].getText()
										.toString());
					}
				}
			}

			repaymntTypeStr = "";
			for (int j = 0; j < check_29.length; j++) {
				if (check_29[j].isChecked()) {
					if ("".equals(repaymntTypeStr)) {
						repaymntTypeStr = "0" + (j + 1);
					} else {
						repaymntTypeStr = ",0" + (j + 1);
					}

				}
			}

			if ("".equals(pledgeTypeStr)) {
				Constans.Toast(context, "请勾选抵押方式");
				return;
			}

			if ("".equals(repaymntTypeStr)) {
				Constans.Toast(context, "请勾还款方式");
				return;
			}

			Http_99();
			break;
		case 9:// 放款失败
			reasons = "";
			for (int i = 0; i < radioButtons_11.length - 1; i++) {

				if (radioButtons_11[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = radioButtons_11[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ radioButtons_11[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0028-0" + (i + 1);
					} else {
						reason += ",0028-0" + (i + 1);
					}

				}
			}
			if (radioButtons_11[5].isChecked()) {
				if (null == editText_11.getText().toString()
						|| "".equals(editText_11.getText().toString())) {
					Constans.Toast(context, "请填写原因");
					return;
				} else {
					if (reasons == null || "".equals(reasons)) {
						reasons = editText_11.getText().toString();
					} else {
						reasons = reasons + ";"
								+ editText_11.getText().toString();
					}

					description = editText_11.getText().toString();
				}
				if (null == reason || "".equals(reason)) {
					reason += "0028-99";
				} else {
					reason += ",0028-99";
				}
			}

			if (null == reason || "".equals(reason)) {
				Constans.Toast(context, "请勾选原因");
			} else {
				normalHttp(reason, 9);
			}
			break;

		case 10:// 公司审批被拒
			reasons = "";
			reason = "";

			if (check_20[0].isChecked()) {

				if (null == editText_per_08.getText().toString()
						|| "".equals(editText_per_08.getText().toString())) {
					Constans.Toast(context, "请填写原因");
					return;
				} else {
					reasons = editText_per_08.getText().toString();
					description = editText_per_08.getText().toString();
				}

				if (null == reason || "".equals(reason)) {
					reason += "0020-99";
				} else {
					reason += ",0020-99";
				}
			}

			for (int i = 0; i < check_16.length - 1; i++) {

				if (check_16[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = check_16[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ check_16[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0016-0" + (i + 1);
					} else {
						reason += ",0016-0" + (i + 1);
					}
				}
			}
			for (int i = 0; i < check_17.length - 1; i++) {

				if (check_17[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = check_17[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ check_17[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0017-0" + (i + 1);
					} else {
						reason += ",0017-0" + (i + 1);
					}
				}
			}
			for (int i = 0; i < check_18.length - 1; i++) {

				if (check_18[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = check_18[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ check_18[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0018-0" + (i + 1);
					} else {
						reason += ",0018-0" + (i + 1);
					}
				}
			}
			for (int i = 0; i < check_19.length - 1; i++) {

				if (check_19[i].isChecked()) {
					if (null == reasons || "".equals(reasons)) {
						reasons = check_19[i].getText().toString();
					} else {
						reasons = reasons + ";"
								+ check_19[i].getText().toString();
					}
					if (null == reason || "".equals(reason)) {
						reason = "0019-0" + (i + 1);
					} else {
						reason += ",0019-0" + (i + 1);
					}
				}
			}

			if (null == reason || "".equals(reason)) {
				Constans.Toast(context, "请勾选原因");
			} else {
				normalHttp(reason, 10);
			}
			break;

		default:
			break;
		}

	}

	private void Http_99() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("id", orderId);
		params.addQueryStringParameter("lendAmount", lendAmountStr);
		params.addQueryStringParameter("lendPeriod", lendPeriodStr);
		params.addQueryStringParameter("ratePerMonth", ratePerMonthStr);
		params.addQueryStringParameter("lendDate", lendDateStr);
		params.addQueryStringParameter("pledgeType", pledgeTypeStr);
		params.addQueryStringParameter("repaymntType", repaymntTypeStr);
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("status", "99");
		myHttpUtils.getHttpJsonString(params, Constans.editOrderUrl, handler,
				context, 7, Constans.thod_Post_1);

	}

	private String pingjie(CheckBox[] checkBoxs, String num) {
		String reasonString = "";
		for (int i = 0; i < checkBoxs.length; i++) {
			if (checkBoxs[i].isChecked()) {
				if (null == reason || "".equals(reason)) {
					reasonString = num + (i + 1);
				} else {
					reasonString = "," + num + (i + 1);
				}

			}

		}

		return reasonString;
	}

}
