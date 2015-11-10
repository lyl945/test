package com.surong.leadloan.activity.order;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.httpserver.MyHttpUtils;
import com.easemob.chatuidemo.utils.CustomProgressDialog;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.entity.Order;
import com.surong.leadloan.ui.AutoLineFeedLayout;
import com.surong.leadloan.ui.MyButton;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.TimeUtils;

public class ProgressQueryActivity extends CommonActivity implements
		OnClickListener {

	View view;
	private Context context;
	private TextView btn_search;
	private LinearLayout myLinearLayout;
	private TextView timeStart, timeEnd;
	private AlertDialog dialog;
	private Calendar mCalendar;
	private String token;
	private MyHttpUtils myHttpUtils;
	private TextView successOrder, overDueOrder, commisionBalance, totalCount;
	private String islast;
	private EditText editText1;
	private List<Order> orderlist = new ArrayList<Order>();
	private TextView time[];
	private boolean flag[];
	String defaultStartDate, defaultEndDate; // 格式化前一天
	private TextView textView1, textView2;
	private boolean statue1, statue2;
	private String orderType;
	private AutoLineFeedLayout orderTypeLayout;
	Map<String, String> orders = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.progress_query_activity, null);
		addContentView(view);
		changeTitle("交单管理");
		initView();
		init();
		getLoanInfo();
		addOrderTypeView();
	}

	private void init() {

	}

	private void initView() {
		token = SharedPreferencesHelp.getString(context, "token");
		myHttpUtils.instance();
		successOrder = (TextView) view.findViewById(R.id.success);
		overDueOrder = (TextView) view.findViewById(R.id.overdue);
		totalCount = (TextView) view.findViewById(R.id.total_count);
		time = new TextView[3];
		flag = new boolean[3];
		time[0] = (TextView) view.findViewById(R.id.time1);
		time[1] = (TextView) view.findViewById(R.id.time2);
		time[2] = (TextView) view.findViewById(R.id.time3);

		editText1 = (EditText) view.findViewById(R.id.editText1);
		commisionBalance = (TextView) view.findViewById(R.id.wallet11);
		timeStart = (TextView) view.findViewById(R.id.start_time);
		timeEnd = (TextView) view.findViewById(R.id.end_time);
		btn_search = (TextView) view.findViewById(R.id.search);
		myLinearLayout = (LinearLayout) view.findViewById(R.id.myLinearLayout);
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		for (int i = 0; i < time.length; i++) {
			time[i].setOnClickListener(this);
		}
		timeStart.setOnClickListener(this);
		timeEnd.setOnClickListener(this);
		// btn_search.setOnClickListener(this);
		myLinearLayout.setOnClickListener(this);
		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);
	}

	private void getLoanInfo() {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		CustomProgressDialog.startProgressDialog(context);
		myHttpUtils.getHttpJsonString(params, Constans.loanAssistantUrl,
				handler, context, 0, Constans.thod_Get_0);

	}

	private void getLoanList() {

		String abcString = editText1.getText().toString();
		defaultStartDate = "";
		CustomProgressDialog.startProgressDialog(context);

		Date dNow = new Date(); // 当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(dNow);// 把当前时间赋给日历
		RequestParams params = new RequestParams();
		for (int i = 0; i < time.length; i++) {
			if (flag[i]) {
				if (i == 0) {
					// calendar.add(Calendar.MONTH, -1);
					calendar.add(Calendar.DAY_OF_MONTH, -7); // 设置为前7天
					dBefore = calendar.getTime(); // 得到前7天的时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
					defaultStartDate = sdf.format(dBefore); // 格式化前7天
					defaultEndDate = sdf.format(dNow); // 格式化当前时间
				}
				if (i == 1) {
					calendar.add(Calendar.MONTH, -1);
					dBefore = calendar.getTime(); // 得到前7天的时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
					defaultStartDate = sdf.format(dBefore); // 格式化前7天
					defaultEndDate = sdf.format(dNow); // 格式化当前时间
				}
				if (i == 2) {
					calendar.add(Calendar.MONTH, -3);
					dBefore = calendar.getTime(); // 得到前7天的时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
					defaultStartDate = sdf.format(dBefore); // 格式化前7天
					defaultEndDate = sdf.format(dNow); // 格式化当前时间
				}
			}
		}
		if (!defaultStartDate.trim().equals("")) {
			params.addQueryStringParameter("startDate", defaultStartDate);
			params.addQueryStringParameter("endDate", defaultEndDate);
		}
		if (!TextUtils.isEmpty(orderType)) {
			params.addQueryStringParameter("status", orderType);
		}
		params.addQueryStringParameter("token", token);
		if (!TextUtils.isEmpty(abcString)) {
			params.addQueryStringParameter("searchStr", abcString);
		}
		myHttpUtils.getHttpJsonString(params, Constans.loanAssistantListUrl,
				handler, context, 1, Constans.thod_Get_0);

	}

	private void addOrderTypeView() {
		orderTypeLayout = (AutoLineFeedLayout) view
				.findViewById(R.id.order_type);

		orderTypeLayout.removeAllViews();
		// 从字典中获取订单数据
		Map<String, String> orderMap = MyApplication.dictionaryMap2
				.get("loan_assi_order_status");
		Iterator<Entry<String, String>> iterator = orderMap.entrySet()
				.iterator();
		// 添加订单button集合
		while (iterator.hasNext()) {
			MyButton button = (MyButton) View
					.inflate(context, R.layout.c, null);
			button.setTextColor(Color.argb(255, 51, 51, 51));
			final Entry<String, String> entry = iterator.next();
			button.setId(Integer.valueOf(entry.getValue()));
			button.setText(entry.getKey());
			button.setTag("false");
			if (orders.get(entry.getValue()) != null) {
				button.setBackgroundResource(R.drawable.shape_red_bg);
				button.setTextColor(Color.rgb(38, 71, 103));
			}
			// button.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Button button = (Button) v;
			//
			// if ("true".equals(v.getTag())) {
			// orderType = "";
			// v.setTag("false");
			// v.setBackgroundDrawable(getResources().getDrawable(
			// R.drawable.shape_bg));
			// button.setTextColor(Color.argb(255, 51, 51, 51));
			//
			// } else {
			// orderType = entry.getValue();
			// v.setTag("true");
			// v.setBackgroundDrawable(getResources().getDrawable(
			// R.drawable.shape_red_bg));
			// button.setTextColor(Color.WHITE);
			// }
			// }
			// });

			orderTypeLayout.addView(button);
		}
		for (int i = 0; i < orderMap.size(); i++) {
			final int num = orderMap.size();
			final int k = i;
			orderTypeLayout.getChildAt(k).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							for (int j = 0; j < num; j++) {
								Button button = (Button) orderTypeLayout
										.getChildAt(j);
								orderTypeLayout.getChildAt(j)
										.setBackgroundDrawable(
												getResources().getDrawable(
														R.drawable.shape_bg));
								button.setTextColor(Color.argb(255, 51, 51, 51));
								if (j != k) {
									button.setTag("false");
								}
							}

							Button button = (Button) orderTypeLayout
									.getChildAt(k);
							if (button.getTag().equals("false")) {
								button.setTextColor(Color.rgb(38, 71, 103));
								orderTypeLayout
										.getChildAt(k)
										.setBackgroundDrawable(
												getResources()
														.getDrawable(
																R.drawable.shape_red_bg));
								orderType = "0" + button.getId();
								button.setTag("true");

							} else if (button.getTag().equals("true")) {

								orderTypeLayout.getChildAt(0)
										.setBackgroundDrawable(
												getResources().getDrawable(
														R.drawable.shape_bg));
								button.setTextColor(Color.argb(255, 51, 51, 51));
								button.setTag("false");
								orderType = "";
							}

						}
					});
		}
	}

	public void clearAll(MyButton button[], int id) {
		for (int i = 0; i < button.length; i++) {
			if (button[i].getId() != id) {
				button[i].setCheck(false);
				button[i].setTag(null);
				button[i].setTextColor(getResources().getColor(
						R.color.register_text_black));
				button[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.shape_bg));
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				CustomProgressDialog.stopProgressDialog();
				JSONObject object = (JSONObject) msg.obj;
				try {
					String successCount = object.getString("successCount");
					String expiredCount = object.getString("expiredCount");
					String commission = object.getString("commission");
					String total = object.getString("totalCount");

					successOrder.setText(successCount);
					overDueOrder.setText(expiredCount);
					commisionBalance.setText(commission);
					totalCount.setText(total);

				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			case 1:
				CustomProgressDialog.stopProgressDialog();
				JSONObject object1 = (JSONObject) msg.obj;
				try {
					String orderlistString = object1.getString("orderList");
					List<Order> list = Analyze
							.analyzeCRMOrders(orderlistString);
					orderlist.clear();
					orderlist.addAll(list);
					islast = object1.getString("isLast");

				} catch (JSONException e) {
					e.printStackTrace();
				}
				Intent isearch = new Intent(ProgressQueryActivity.this,
						CommissionManageActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("orderList", (Serializable) orderlist);
				bundle.putString("isLast", islast);
				isearch.putExtras(bundle);
				startActivity(isearch);
				break;

			default:
				break;
			}

		};
	};

	private void showDateDialog(final int type) {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.date_dialog);
		final DatePicker datePicker = (DatePicker) dialog.getWindow()
				.findViewById(R.id.datePicker1);
		dialog.getWindow().findViewById(R.id.comfirm)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mCalendar.set(Calendar.YEAR, datePicker.getYear());
						mCalendar.set(Calendar.MONTH, datePicker.getMonth());
						mCalendar.set(Calendar.DAY_OF_MONTH,
								datePicker.getDayOfMonth());
						String date = TimeUtils.transformToTime1(mCalendar
								.getTimeInMillis());
						// Intent i1 = new Intent(context,
						// ProgressQueryActivity.class);
						// i1.putExtra("date", date);
						// startActivity(i1);
						dialog.dismiss();

						switch (type) {
						case 0:
							timeStart.setText(date);
							break;
						case 1:
							timeEnd.setText(date);
						default:
							break;
						}

					}
				});
		dialog.getWindow().findViewById(R.id.cancel)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myLinearLayout:
			getLoanList();
			break;
		case R.id.time1:
			flag[0] = !flag[0];
			if (flag[0]) {
				setColor(0);
			} else {
				time[0].setTextColor(getResources().getColor(
						R.color.mshop_text_black));
				time[0].setBackgroundResource(R.drawable.shape_bg);
			}
			break;
		case R.id.time2:
			flag[1] = !flag[1];
			if (flag[1]) {
				setColor(1);
			} else {
				time[1].setTextColor(getResources().getColor(
						R.color.mshop_text_black));
				time[1].setBackgroundResource(R.drawable.shape_bg);
			}
			break;
		case R.id.time3:
			flag[2] = !flag[2];
			if (flag[2]) {
				setColor(2);
			} else {
				time[2].setTextColor(getResources().getColor(
						R.color.mshop_text_black));
				time[2].setBackgroundResource(R.drawable.shape_bg);
			}
			break;
		case R.id.textView1:
			statue1 = !statue1;
			if (statue1) {
				statue2 = false;
				orderType = "01";
				textView1.setTextColor(getResources().getColor(R.color.white));
				textView1.setBackgroundColor(getResources().getColor(
						R.color.title_red));
				textView2.setTextColor(getResources().getColor(
						R.color.mshop_text_black));
				textView2.setBackgroundResource(R.drawable.shape_bg);
			} else {
				orderType = "";
				textView1.setTextColor(getResources().getColor(
						R.color.mshop_text_black));
				textView1.setBackgroundResource(R.drawable.shape_bg);
			}

			break;
		case R.id.textView2:
			statue2 = !statue2;
			if (statue2) {
				statue1 = false;
				orderType = "02";
				textView2.setTextColor(getResources().getColor(R.color.white));
				textView2.setBackgroundColor(getResources().getColor(
						R.color.title_red));
				textView1.setTextColor(getResources().getColor(
						R.color.mshop_text_black));
				textView1.setBackgroundResource(R.drawable.shape_bg);
			} else {
				orderType = "";
				textView2.setTextColor(getResources().getColor(
						R.color.mshop_text_black));
				textView2.setBackgroundResource(R.drawable.shape_bg);
			}

			break;
		case R.id.start_time:
			showDateDialog(0);
			break;

		case R.id.end_time:
			showDateDialog(1);
			break;
		default:
			break;
		}

	}

	private void setColor(int i) {
		for (int j = 0; j < time.length; j++) {
			if (j == i) {
				time[j].setTextColor(Color.rgb(38, 71, 103));
				time[j].setBackgroundResource(R.drawable.shape_red_bg);

			} else {
				flag[j] = false;
				time[j].setTextColor(getResources().getColor(
						R.color.mshop_text_black));
				time[j].setBackgroundResource(R.drawable.shape_bg);
			}

		}
	}

}
