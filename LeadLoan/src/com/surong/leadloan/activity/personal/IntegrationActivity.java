package com.surong.leadloan.activity.personal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadload.api.data.TimeTable;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;

public class IntegrationActivity extends CommonActivity implements
		OnClickListener, OnRefreshListener2<ListView> {

	View view;
	Context context;
	private PullToRefreshListView mRefreshListView;
	private ListView mListView;
	private TAdapter myAdapter;
	private ArrayList<TimeTable> listData;
	private List<TimeTable> list = new ArrayList<TimeTable>();

	private AlertDialog dialog;
	private Calendar mCalendar;
	private String companyString, jobString, timeStartString, timeEndString,
			workDescribeString;
	protected MyHttpUtils myHttpUtils;
	private int islast;
	private TimeTable dianJuan;
	private int mYear, mMonth, mDay;
	private int premYear, premMonth, premDay;
	private String defaultStartDate, defaultEndDate;
	private String token;
	private TextView back, title;
	private Button timeStart, timeEnd;
	private static final int DATE_DIALOG_ID = 1;
	private static final int DATE_DIALOG_ID2 = 3;
	private static final int SHOW_DATAPICK = 0;
	private static final int SHOW_DATAPICK2 = 2;
	private int pageNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.integration2);
		back = (TextView) findViewById(R.id.top_textView_left);
		title = (TextView) findViewById(R.id.top_textview_title);
		title.setText("积分明细账户");
		back.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back, 0, 0, 0);
		back.setOnClickListener(this);

		mRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_listview);
		mListView = mRefreshListView.getRefreshableView();
		// ViewUtils.setEmptyView(context, "该条件下暂无内容", mListView);
		mRefreshListView
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
		mRefreshListView.setIsColor(1);
		// // 设置字体颜色为黑色
		mRefreshListView.setTextColor(getResources().getColor(R.color.black));
		mRefreshListView.setOnRefreshListener(this);

		Date dNow = new Date(); // 当前时间
		Date dBefore = new Date();
		Calendar calendar = Calendar.getInstance(); // 得到日历
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(dNow);
		calendar.add(Calendar.MONTH, -1); // 设置为前7天
		premYear = calendar.get(Calendar.YEAR);
		premMonth = calendar.get(Calendar.MONTH);
		premDay = calendar.get(Calendar.DAY_OF_MONTH);
		dBefore = calendar.getTime(); // 得到前7天的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		defaultStartDate = sdf.format(dBefore); // 格式化前7天
		defaultEndDate = sdf.format(dNow);
		listData = new ArrayList<TimeTable>();
		init();
		initView();
		updateDisplay();

	}

	private void updateDisplay() {
		// 格式化当前时间
		myHttpUtils = MyHttpUtils.myInstance();
		token = SharedPreferencesHelp.getString(context, "token");
		CustomProgressDialog.startProgressDialog(context);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("fromDate", defaultStartDate);
		params.addQueryStringParameter("toDate", defaultEndDate);
		myHttpUtils.getHttpJsonString(params, Constans.integrationUrl, handler,
				context, 1, Constans.thod_Get_0);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// xx.setText(msg+"");
			// Toast.makeText(IntegrationActivity.this, msg.obj+"",
			// Toast.LENGTH_SHORT).show();
			CustomProgressDialog.stopProgressDialog();
			// 关闭隐藏下拉
			switch (msg.what) {
			// 下拉刷新数据
			case 1:
				try {
					pageNo = 1;
					JSONObject object = (JSONObject) msg.obj;
					islast = object.getInt("isLast");
					String record = object.getString("recordList");
					List<TimeTable> timeTable1 = Analyze.analyzeCRMO(record);
					listData.clear();
					if (timeTable1.size() == 0) {
						Constans.Toast(context, "没有数据,请更改时间");
					}
					listData.addAll(timeTable1);
					if (listData.size() == 0) {
						TimeTable table = new TimeTable("", "", "", "");
						listData.add(table);
					}
					myAdapter = new TAdapter(context, listData);
					mListView.setAdapter(myAdapter);
					myAdapter.notifyDataSetChanged();
					/*
					 * scrollview嵌套listview 手动设置ListView高度
					 */
					// String beizhu = object.getString("recordList");
					// Toast.makeText(SecuritiesAccountActivity.this,
					// jifen+"heh", Toast.LENGTH_SHORT).show();
					// Toast.makeText(SecuritiesAccountActivity.this, time,
					// Toast.LENGTH_SHORT).show();
					// List<Order> list = Analyze.analyzeCRMOrders(itemArray);
					// 把集合添加到适配器集合里
					// mlist.addAll(list);
					// 把数据放入数据库
					// SharedPreferencesHelp.SavaString(context, "orderList",
					// itemArray);
					// 刷新适配器
					// adapter.notifyDataSetChanged();
					// pageIndex = 2;
				} catch (Exception e) {
					Log.v("error", "aaaaaaaaa" + e.getLocalizedMessage());
					Log.v("error", "aaaaaaaaa" + e.getCause());
					Log.v("error", "aaaaaaaaa" + e.getClass());
					Log.v("error", "aaaaaaaaa" + e.getStackTrace());
				}
				break;
			case 5:
				CustomProgressDialog.stopProgressDialog();
				// 关闭隐藏下拉
				mRefreshListView.onRefreshComplete();
				break;
			case 3:
				try {
					CustomProgressDialog.stopProgressDialog();
					// 关闭隐藏下拉
					mRefreshListView.onRefreshComplete();
					JSONObject object = (JSONObject) msg.obj;
					islast = object.getInt("isLast");
					String record = object.getString("recordList");
					List<TimeTable> timeTable1 = Analyze.analyzeCRMO(record);
					// listData.clear();
					listData.addAll(timeTable1);
					if (listData.size() == 0) {
						TimeTable table = new TimeTable("", "", "", "");
						listData.add(table);
					}
					myAdapter = new TAdapter(context, listData);
					mListView.setAdapter(myAdapter);
					myAdapter.notifyDataSetChanged();
				} catch (Exception e) {
				}
				break;
			}
		};
	};

	private void init() {

	}

	private void initView() {
		// myHttpUtils = MyHttpUtils.myInstance();
		// TimeTable timeTable = new TimeTable("123", "2014", "1", "123");
		// TimeTable timeTable2 = new TimeTable("123", "2014", "1", "123");
		// list.add(timeTable);
		// list.add(timeTable2);
		// myAdapter = new TAdapter(this,list);
		// mListView.setAdapter(myAdapter);

	}

	class TAdapter extends BaseAdapter {
		Context context;
		List<TimeTable> timeTable;

		public TAdapter(Context context, List<TimeTable> timeTable) {
			super();
			this.timeTable = timeTable;
			this.context = context;
		}

		@Override
		public int getCount() {
			return timeTable.size();
		}

		@Override
		public Object getItem(int position) {

			return timeTable.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public int getViewTypeCount() {
			return timeTable.size() + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			TimeTable data = timeTable.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.integration2_search, null);
				holder.tableName = (TextView) convertView
						.findViewById(R.id.tableName);
				holder.time = (TextView) convertView.findViewById(R.id.time1);
				holder.find = (TextView) convertView.findViewById(R.id.find);
				holder.jifen = (TextView) convertView.findViewById(R.id.jifen1);
				holder.beizhu = (TextView) convertView
						.findViewById(R.id.beizhu1);
				timeStart = (Button) convertView.findViewById(R.id.time_start);
				timeEnd = (Button) convertView.findViewById(R.id.time_end);
				holder.topLayout = (LinearLayout) convertView
						.findViewById(R.id.top_layout);
				holder.layout_list10 = (LinearLayout) convertView
						.findViewById(R.id.layout_list10);
				holder.list_top_Layout = (LinearLayout) convertView
						.findViewById(R.id.list_top_Layout);
				holder.line1 = (View) convertView.findViewById(R.id.line1);
				holder.line2 = (View) convertView.findViewById(R.id.line2);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.topLayout.setVisibility(View.VISIBLE);
			} else {
				holder.topLayout.setVisibility(View.GONE);

			}
			if (TextUtils.isEmpty(data.getOperateTime())) {
				holder.layout_list10.setVisibility(View.GONE);
				if (timeTable.size() == 1) {
					holder.layout_list10.setVisibility(View.GONE);
					holder.list_top_Layout.setVisibility(View.GONE);
				} else {
					holder.layout_list10.setVisibility(View.VISIBLE);
					holder.list_top_Layout.setVisibility(View.VISIBLE);
				}
			}
			holder.tableName.setText("积分");
			holder.time.setText(data.getOperateTime());
			holder.jifen.setText(data.getOperateType() + data.getAmount());
			holder.beizhu.setText(data.getreasonDesc());
			if (data.getOperateType().trim().equals("+")) {
				holder.jifen.setTextColor(Color.argb(255, 255, 192, 0));
			}
			if (position % 2 == 0) {
				// holder.layout_list10.setBackgroundColor(Color.argb(255, 192,
				// 224, 255));
				holder.time.setBackgroundColor(Color.argb(255, 192, 224, 255));
				holder.jifen.setBackgroundColor(Color.argb(255, 192, 224, 255));
				holder.beizhu
						.setBackgroundColor(Color.argb(255, 192, 224, 255));
				holder.line1.setBackgroundColor(Color.argb(255, 255, 255, 255));
				holder.line2.setBackgroundColor(Color.argb(255, 255, 255, 255));
			} else {
				// holder.layout_list10.setBackgroundColor(Color.argb(255, 255,
				// 255, 255));
				holder.time.setBackgroundColor(Color.rgb(255, 255, 255));
				holder.jifen.setBackgroundColor(Color.argb(255, 255, 255, 255));
				holder.beizhu
						.setBackgroundColor(Color.argb(255, 255, 255, 255));
				holder.line1.setBackgroundColor(Color.argb(255, 192, 224, 255));
				holder.line2.setBackgroundColor(Color.argb(255, 192, 224, 255));
			}
			timeStart.setText(defaultStartDate);
			timeEnd.setText(defaultEndDate);
			// final Button timeStart1 = holder.timeStart;
			// final Button timeEnd1 = holder.timeEnd;
			timeStart.setOnClickListener(new DateButtonOnClickListener());
			timeEnd.setOnClickListener(new DateButtonOnClickListener2());
			holder.find.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					CustomProgressDialog.startProgressDialog(context);
					RequestParams params = new RequestParams();
					params.addQueryStringParameter("token", token);
					params.addQueryStringParameter("fromDate", defaultStartDate);
					params.addQueryStringParameter("toDate", defaultEndDate);
					myHttpUtils.getHttpJsonString(params,
							Constans.integrationUrl, handler, context, 1,
							Constans.thod_Get_0);
				}
			});

			return convertView;
		}
	}

	class ViewHolder {
		TextView time, jifen, beizhu, find, tableName;
		LinearLayout topLayout, layout_list10, list_top_Layout;
		// Button timeStart, timeEnd;
		View line1, line2;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

		int dayOfMonth) {

			premYear = year;

			premMonth = monthOfYear;

			premDay = dayOfMonth;

			update();

		}

		private void update() {

			defaultStartDate = premYear
					+ "-"
					+ ((premMonth + 1) < 10 ? "0" + (premMonth + 1)
							: (premMonth + 1)) + "-"
					+ ((premDay < 10) ? "0" + (premDay) : premDay);
			myAdapter = new TAdapter(context, listData);
			mListView.setAdapter(myAdapter);
			myAdapter.notifyDataSetChanged();
			// timeStart.setText(defaultStartDate);
			// Constans.Toast(context, defaultStartDate);
			// myAdapter.notifyDataSetChanged();
		}
	};
	private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

		int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDis();
		}

		private void updateDis() {
			defaultEndDate = mYear + "-"
					+ ((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
					+ "-" + ((mDay < 10) ? "0" + (mDay) : mDay);
			myAdapter = new TAdapter(context, listData);
			mListView.setAdapter(myAdapter);
			myAdapter.notifyDataSetChanged();
			// timeEnd.setText(defaultEndDate);
			// myAdapter.notifyDataSetChanged();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, premYear,
					premMonth, premDay);
		case DATE_DIALOG_ID2:
			return new DatePickerDialog(this, mDateSetListener2, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
	}

	class DateButtonOnClickListener implements

	android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			Message msg = new Message();
			// if (timeStart.equals((Button) v)) {

			msg.what = IntegrationActivity.SHOW_DATAPICK;
			// }
			// if (timeEnd.equals((Button) v)) {
			// msg.what = Text123456Activity.SHOW_DATAPICK2;
			// }
			IntegrationActivity.this.saleHandler.sendMessage(msg);
		}
	}

	class DateButtonOnClickListener2 implements

	android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			Message msg = new Message();
			// if (timeStart.equals((Button) v)) {
			//
			// msg.what = Text123456Activity.SHOW_DATAPICK;
			// }
			// if (timeEnd.equals((Button) v)) {
			msg.what = IntegrationActivity.SHOW_DATAPICK2;
			// }
			IntegrationActivity.this.saleHandler.sendMessage(msg);
		}
	}

	Handler saleHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 时间选择1的按钮时间
			case IntegrationActivity.SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			// 时间选择2的按钮时间
			case IntegrationActivity.SHOW_DATAPICK2:
				showDialog(DATE_DIALOG_ID2);
				break;
			}
		}
	};

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		CustomProgressDialog.startProgressDialog(context);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(context));
		if (1 == islast) {
			Constans.Toast(context, "没有更多数据了");
			handler.sendEmptyMessage(5);
			return;
		} else {
			pageNo++;
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("token", token);
			params.addQueryStringParameter("fromDate", defaultStartDate);
			params.addQueryStringParameter("toDate", defaultEndDate);
			params.addQueryStringParameter("pageNo", pageNo + "");
			myHttpUtils.getHttpJsonString(params, Constans.integrationUrl,
					handler, context, 3, Constans.thod_Get_0);
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.top_textView_left:
			finish();
			break;
		default:
			break;
		}

	}

}
