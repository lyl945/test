package com.surong.leadloan.activity.order;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chatuidemo.httpserver.MyHttpUtils;
import com.easemob.chatuidemo.utils.CustomProgressDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.entity.Order;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.ViewUtils;

public class CommissionManageActivity extends Activity implements
		OnClickListener, OnRefreshListener2<ListView> {

	Context context;
	private PullToRefreshListView mRefreshListView;
	private ListView mListView;
	private MyAdapter myAdapter;
	private MyHttpUtils myHttpUtils;
	private String token;
	private List<Order> orderlist = new ArrayList<Order>();
	private String isLast;
	private TextView back, title;
	// 当前页数
	protected int pageIndex = 1;
	private String pageNo;
	static JSONObject order;// 基本信息的order

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.query_progress);
		initView();
	}

	private void initView() {
		token = SharedPreferencesHelp.getString(context, "token");
		myHttpUtils.instance();
		Bundle bundle = getIntent().getExtras();
		orderlist = (List<Order>) bundle.getSerializable("orderList");
		isLast = bundle.getString("isLast");
		mRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_listview);
		mListView = mRefreshListView.getRefreshableView();
		ViewUtils.setEmptyView(context, "该条件下暂无内容", mListView);
		mRefreshListView
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
		mRefreshListView.setIsColor(1);
		// // 设置字体颜色为黑色
		mRefreshListView.setTextColor(getResources().getColor(
				R.color.black));
		myAdapter = new MyAdapter(context);
		mListView.setAdapter(myAdapter);
		mRefreshListView.setOnRefreshListener(this);
		pageNo = pageIndex + "";
		back = (TextView) findViewById(R.id.top_textView_left);
		title = (TextView) findViewById(R.id.top_textview_title);
		title.setText("进度查询");
		back.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back, 0, 0, 0);
		back.setOnClickListener(this);

	}

	private void getLoanList() {
		CustomProgressDialog.startProgressDialog(context);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("pageNo", pageNo);
		myHttpUtils.getHttpJsonString(params, Constans.loanAssistantListUrl,
				handler, context, 0, Constans.thod_Get_0);

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			mRefreshListView.onRefreshComplete();

			switch (msg.what) {
			case 0:
				JSONObject object = (JSONObject) msg.obj;
				try {
					String orderlistString = object.getString("orderList");
					List<Order> list = Analyze
							.analyzeCRMOrders(orderlistString);
					orderlist.clear();
					orderlist.addAll(list);
					isLast = object.getString("isLast");
					myAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			case 1:
				JSONObject object1 = (JSONObject) msg.obj;
				try {
					order = object1.getJSONObject("order");
					Intent i1 = new Intent(context,
							CommissionDetailActivity.class);
					startActivity(i1);

				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case 5:

				break;

			default:
				break;
			}
		};
	};

	public static JSONObject getOrder() {
		return order;
	}

	class MyAdapter extends BaseAdapter {

		private Context context;

		public MyAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return orderlist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return orderlist.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			ViewHoler holder = null;
			final Order data = orderlist.get(position);
			if (convertView == null) {
				holder = new ViewHoler();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.query_progress_item, null);
				holder.custName = (TextView) convertView
						.findViewById(R.id.cust_name);
				holder.time = (TextView) convertView.findViewById(R.id.time);
				holder.loanTerm = (TextView) convertView
						.findViewById(R.id.loan_term);
				holder.loanAmount = (TextView) convertView
						.findViewById(R.id.loan_amount);
				holder.loanProgress = (TextView) convertView
						.findViewById(R.id.loan_progress);
				convertView.setTag(holder);
			} else {
				holder = (ViewHoler) convertView.getTag();
			}
			holder.custName.setText(data.getCustName());
			holder.time.setText(data.getRecoDate());
			holder.loanTerm.setText(data.getApplyPeriod());
			holder.loanAmount.setText(data.getApplyAmount());
			holder.loanProgress.setText(data.getStatus());
			

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (TextUtils.isEmpty(orderlist.get(position).getProdId())) {
						CustomProgressDialog.startProgressDialog(context);
						RequestParams params = new RequestParams();
						params.addQueryStringParameter("token", token);
						params.addQueryStringParameter("orderId", data.getId());
						myHttpUtils.getHttpJsonString(params,
								Constans.loanAssistantDetailUrl, handler, context,
								1, Constans.thod_Get_0);
					}
					else {
						String Id = orderlist.get(position).getId();
						Intent intent = new Intent(context,
								CommissionDetailWebActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("Id", Id);
						intent.putExtras(bundle);
						startActivity(new Intent(intent));
					}
				}
			});

			return convertView;
		}

	}

	class ViewHoler {
		TextView custName, time, loanTerm, loanAmount, loanProgress;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

		if (!isLast.equals("0")) {
			Constans.Toast(context, "没有更多数据了");
			handler.sendEmptyMessage(5);
			return;
		} else {
			pageIndex++;
			getLoanList();
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
