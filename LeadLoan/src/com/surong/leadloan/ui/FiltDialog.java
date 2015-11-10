package com.surong.leadloan.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.entity.Order;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.SharedPreferencesHelp;

public class FiltDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private RadioButton stateRadioButton[];
	private StateOnClickListener StateOnClickListener;
	private RadioButton fromRadioButton[];
	private FromOnClickListener fromOnClickListener;
	private RadioButton starRadioButton[];
	private StarOnClickListener starOnClickListener;
	private MyHttpUtils myHttpUtils;
	public String state = "", from, star;
	private Map<String, String> map;
	public String customerSource;

	private Button btn_choose;

	public FiltDialog(Context context) {
		super(context);

	}

	public FiltDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.filt_dialog);
		// 去掉标题
		btn_choose = (Button) findViewById(R.id.btn_choose);
		btn_choose.setOnClickListener(this);
		map = MyApplication.dictionaryMap2.get("MAP_ORDER_STATUS");
		state = new String();
		init();
	}

	private void init() {
		myHttpUtils = MyHttpUtils.myInstance();

		StateOnClickListener = new StateOnClickListener();
		fromOnClickListener = new FromOnClickListener();
		starOnClickListener = new StarOnClickListener();
		// 按状态搜索
		stateRadioButton = new RadioButton[10];
		stateRadioButton[0] = (RadioButton) findViewById(R.id.state_1);
		stateRadioButton[1] = (RadioButton) findViewById(R.id.state_2);
		stateRadioButton[2] = (RadioButton) findViewById(R.id.state_3);
		stateRadioButton[3] = (RadioButton) findViewById(R.id.state_4);
		stateRadioButton[4] = (RadioButton) findViewById(R.id.state_5);
		stateRadioButton[5] = (RadioButton) findViewById(R.id.state_6);
		stateRadioButton[6] = (RadioButton) findViewById(R.id.state_7);
		stateRadioButton[7] = (RadioButton) findViewById(R.id.state_8);
		stateRadioButton[8] = (RadioButton) findViewById(R.id.state_9);
		stateRadioButton[9] = (RadioButton) findViewById(R.id.state_10);
		for (int i = 0; i < stateRadioButton.length; i++) {
			stateRadioButton[i].setOnClickListener(StateOnClickListener);
		}
		/*
		 * Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		 * if (map.size() == stateRadioButton.length) {//如果控件跟数据字典匹配，则进行动态设置 for
		 * (int i = 0; i < map.size(); i++) {
		 * 
		 * Map.Entry<String, String> entry = it.next();
		 * stateRadioButton[i].setText(entry.getKey()); } }
		 */
		fromRadioButton = new RadioButton[5];
		fromRadioButton[0] = (RadioButton) findViewById(R.id.from_1);
		fromRadioButton[1] = (RadioButton) findViewById(R.id.from_2);
		fromRadioButton[2] = (RadioButton) findViewById(R.id.from_3);
		fromRadioButton[3] = (RadioButton) findViewById(R.id.from_4);
		fromRadioButton[4] = (RadioButton) findViewById(R.id.from_5);
		for (int i = 0; i < fromRadioButton.length; i++) {
			fromRadioButton[i].setOnClickListener(fromOnClickListener);
		}

		starRadioButton = new RadioButton[4];
		starRadioButton[0] = (RadioButton) findViewById(R.id.star_1);
		starRadioButton[1] = (RadioButton) findViewById(R.id.star_2);
		starRadioButton[2] = (RadioButton) findViewById(R.id.star_3);
		starRadioButton[3] = (RadioButton) findViewById(R.id.star_4);
		for (int i = 0; i < starRadioButton.length; i++) {
			starRadioButton[i].setOnClickListener(starOnClickListener);
		}

	}

	public void clearAll(RadioButton radioButton[], int id) {

		for (int i = 0; i < radioButton.length; i++) {
			if (radioButton[i].getId() != id) {
				radioButton[i].setChecked(false);
				radioButton[i].setTag(null);
			}
		}
	}

	class StateOnClickListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View arg0) {
			CompoundButton buttonView = (CompoundButton) arg0;
			if (buttonView.getTag() == null) {
				clearAll(stateRadioButton, buttonView.getId());
				buttonView.setChecked(true);
				buttonView.setTag(true);
				String string = ((RadioButton) buttonView).getText().toString();
				if (string.trim().equals("公司审批通过/成功放款")) {
					state = "99";
				} else {
					state = map.get(string);
				}

			} else {
				buttonView.setChecked(false);
				buttonView.setTag(null);
				state = "";
			}

		}
	}

	class FromOnClickListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View arg0) {
			CompoundButton buttonView = (CompoundButton) arg0;
			if (buttonView.getTag() == null) {
				clearAll(fromRadioButton, buttonView.getId());
				buttonView.setChecked(true);
				buttonView.setTag(true);
			} else {
				buttonView.setChecked(false);
				buttonView.setTag(null);
			}
		}
	}

	class StarOnClickListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View arg0) {
			CompoundButton buttonView = (CompoundButton) arg0;
			if (buttonView.getTag() == null) {
				clearAll(starRadioButton, buttonView.getId());
				buttonView.setChecked(true);
				buttonView.setTag(true);
			} else {
				buttonView.setChecked(false);
				buttonView.setTag(null);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_choose:

			RequestParams params = new RequestParams();
			String tokenString = SharedPreferencesHelp.getString(context,
					"token");

			params.addQueryStringParameter("token", tokenString);
			params.addQueryStringParameter("orderStatus", state);

			for (int i = 0; i < fromRadioButton.length; i++) {
				if (fromRadioButton[i].isChecked()) {
					customerSource = "" + i;
					break;
				} else {
					customerSource = "";
				}
			}

			params.addQueryStringParameter("customerSource", customerSource);
			CustomProgressDialog.startProgressDialog(context);
			myHttpUtils.getHttpJsonString(params, Constans.findNewOrderUrl,
					handler, context, 0, Constans.thod_Get_0);
			break;

		default:
			break;
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// 得到数据
				CustomProgressDialog.stopProgressDialog();
				JSONObject object = (JSONObject) msg.obj;
				System.out.println(object.toString());
				// 把json保存到对象里
				String itemArray;
				List<Order> list = new ArrayList<Order>();
				try {
					itemArray = object.getString("orderList");
					list = Analyze.analyzeCRMOrders(itemArray);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// 把集合添加到适配器集合里
				if (list == null || list.size() == 0) {
					Constans.Toast(context, "没有符合条件的结果，请重新筛选");
				} else {
					Intent mIntent = new Intent(Constans.CRM_ACTION_NAME);
//					mIntent.putExtra("orderList", (Serializable) list);
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderList", (Serializable) list);
					mIntent.putExtras(bundle);
					context.sendBroadcast(mIntent);
					dismiss();
				}

				break;

			default:
				break;
			}
		};
	};

}
