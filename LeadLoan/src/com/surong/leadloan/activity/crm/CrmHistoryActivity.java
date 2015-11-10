package com.surong.leadloan.activity.crm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.adapter.CrmApdapter;
import com.surong.leadloan.entity.Order;
import com.surong.leadloan.entity.OrderDetail;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;

public class CrmHistoryActivity extends Activity implements
		OnRefreshListener2<ListView>, OnItemClickListener, OnClickListener {

	private View view;
	// 自定义ListView
	protected PullToRefreshListView mPullToRefreshListView;
	// 接口请求对象
	protected MyHttpUtils myHttpUtils;
	// 身份令牌
	protected String token;
	// 当前页数
	protected int pageIndex = 1;
	// 判断是下拉还是上啦
	protected int type = 0;
	// 数据库操作对象
	protected DbUtils db;
	private Context context;
	// ListView
	protected ListView mlistView;
	// 客户管理
	private List<Order> mlist;
	// 客户管理适配器
	private CrmApdapter adapter;
	private OrderDetail orderDetail;

	private String custMobile;
	private TextView textLeft, textTitle;
	String json;
	String isLast;
	private String from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crm_history);
		initView();
		initData();
		initAction();
	}

	private void initView() {
		context = this;
		// 得到令牌
		token = SharedPreferencesHelp.getString(context, "token");
		// 初始化请求接口对象
		myHttpUtils = MyHttpUtils.myInstance();
		// 初始化ListView
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_fun_page);
		// 设置下拉和上拉模式
		mPullToRefreshListView
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
		// 得到显示ListView
		mlistView = mPullToRefreshListView.getRefreshableView();

		// 设置字体颜色为黑色
		mPullToRefreshListView.setIsColor(1);
		// 设置字体颜色为黑色
		mPullToRefreshListView.setTextColor(getResources().getColor(
				R.color.black));

		textTitle = (TextView) findViewById(R.id.top_textview_title);
		textLeft = (TextView) findViewById(R.id.top_textView_left);
		textLeft.setVisibility(View.VISIBLE);
		textTitle.setText("历史订单");
		textLeft.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back, 0, 0,
				0);
		textLeft.setOnClickListener(this);
	}

	private void initData() {
		// 得到缓存里的数据
		json = SharedPreferencesHelp.getString(context, "orderlist");
		isLast = SharedPreferencesHelp.getString(context, "isLast");
		if (null != json && !"".equals(json)) {
			mlist = Analyze.analyzeCRMOrders(json);
		} else {
			mlist = new ArrayList<Order>();
		}
		// 初始化适配器
		adapter = new CrmApdapter(this, mlist);

		// 添加适配器
		mlistView.setAdapter(adapter);
		mlistView.setOnItemClickListener(this);
		mPullToRefreshListView.setOnRefreshListener(this);

	}

	private void initAction() {

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(context));
		mPullToRefreshListView.onRefreshComplete();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(context));
		if (isLast.equals("1")) {
			Constans.Toast(context, "没有更多数据了");
			handler.sendEmptyMessage(5);
			return;
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("mobile", custMobile);
		myHttpUtils.getHttpJsonString(params, Constans.findOrderHistoryUrl,
				handler, context, 1, Constans.thod_Get_0);
		CustomProgressDialog.startProgressDialog(context);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String orderId = String.valueOf(mlist.get(position - 1).getId());
		from = "";// 订单来源需根据两个字段确定
		if (null == mlist.get(position - 1).getOrderId()) {
			from = "推广订单";
		} else {
			if ("01".equals(mlist.get(position - 1).getServiceOrderCode())) {
				from = "100领单";
			} else {
				from = "100转单";
			}
		}
		String fromString = "";
		if ("".equals(mlist.get(position - 1).getOrderId())
				|| null == mlist.get(position - 1).getOrderId()) {
			fromString = "1";
		} else {
			fromString = "2";
		}
		Intent intent = new Intent(context, SpreadOrderDetailActivity.class);
		Bundle bun = new Bundle();
		bun.putString("orderSource", fromString);
		bun.putString("orderId", orderId);
		bun.putString("from", from);
		bun.putString("isHistory", "isHistory");
		intent.putExtras(bun);

		startActivity(intent);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			// 关闭隐藏下拉
			mPullToRefreshListView.onRefreshComplete();
			switch (msg.what) {

			// 上拉加载数据
			case 1:
				try {
					JSONObject object = (JSONObject) msg.obj;
					Log.e("123", "objet=" + object);
					String itemArray = object.getString("orderList");
					List<Order> list = Analyze.analyzeCRMOrders(itemArray);
					mlist.addAll(list);
					adapter.notifyDataSetChanged();
					pageIndex++;
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("Mytest", e.toString() + "");
				}
				break;
			case 10:
				// 成功获取详细信息数据
				JSONObject jsonObject = (JSONObject) msg.obj;
				// try {
				// String js = "";
				// /*
				// * try { js = jsonObject.getString("order"); } catch
				// * (JSONException e) { e.printStackTrace(); }
				// */
				// // 解析JSon数据保存于对象
				// orderDetail = Analyze.analyzeCRMOrdersDetail(js);
				// // 跳转到详细页面以及传详细信息对象过去
				// Intent intent = new Intent(context,
				// SpreadOrderDetailActivity.class);
				// Bundle bun = new Bundle();
				// bun.putString("orderDetail", jsonObject.toString());
				// bun.putString("isHistory", "isHistory");
				// bun.putString("from", from);
				// if (jsonObject.getString("isStatus").equals("1")) {
				// bun.putString("isStatusReason",
				// jsonObject.getString("isStatusReason"));
				// }
				// if (!jsonObject.isNull("canBackCash")// 退点券
				// && jsonObject.getString("canBackCash").equals("1")) {
				// bun.putString("canBackCash",
				// jsonObject.getString("canBackCash"));
				// }
				// if (!jsonObject.isNull("canApplyPoint")// 申报奖励
				// && jsonObject.getString("canApplyPoint")
				// .equals("1")) {
				// bun.putString("canApplyPoint",
				// jsonObject.getString("canApplyPoint"));
				// }
				// intent.putExtras(bun);
				//
				// context.startActivity(intent);
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.top_textView_left:
			finish();
			break;

		default:
			break;
		}
	}
}
