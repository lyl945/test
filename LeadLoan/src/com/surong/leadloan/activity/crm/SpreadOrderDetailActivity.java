package com.surong.leadloan.activity.crm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.entity.Order;
import com.surong.leadloan.entity.OrderDetail;
import com.surong.leadloan.fragment.CrmBaseainformation;
import com.surong.leadloan.fragment.CrmCustomer;
import com.surong.leadloan.fragment.CrmKeepUp;
import com.surong.leadloan.fragment.CrmPledgeWay;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;

/*
 * 推广订单详情
 */
public class SpreadOrderDetailActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {

	private View view;
	private Context context;
	private TextView textView[];// fragment切换按钮
	private RelativeLayout relativeLayout_back;
	private CrmBaseainformation crmBaseainformation;// 基本信息
	private CrmCustomer crmCustomer;// 客户资质
	private CrmPledgeWay crmPledgeWay;// 担保措施
	private CrmKeepUp crmKeepUp;// 跟进记录
	private ViewPager vPager_Sc;
	private View viewLine[];
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private LinearLayout contentLayout, lin_call, lin_message,
			returnPointLayout, declareRewardLayout;
	private JSONObject orderDetailJson;
	private OrderDetail orderDetail;

	private TextView custType;// 客户类别
	private TextView custName;// 客户姓名
	private TextView custMobile;// 客户手机
	private TextView status;// 订单状态
	private TextView from;// 来源
	private TextView change;// 更改
	private TextView reason;// 原因
	private Map<String, String> customerInforMap;
	private Map<String, String> baseMap;
	private Map<String, String> customerIdentifierMap;
	private Map<String, String> assuranceMap;
	private String orderId, isStatus, isStatusReason, canBackCash,
			canApplyPoint;
	private LinearLayout history;
	private String froms;
	private LinearLayout mylinearLayout;
	private View myview;
	private TextView size;
	private String token;
	private MyHttpUtils myHttpUtils;
	String phone;
	private int sizeAcout;
	String fromString;
	String jsonStrings;
	Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.spread_order_detail);
		bundle = getIntent().getExtras();
		myHttpUtils = MyHttpUtils.myInstance();
		token = SharedPreferencesHelp.getString(context, "token");
		findView();
		initAction();

	}

	private void findView() {
		textView = new TextView[4];
		textView[0] = (TextView) findViewById(R.id.txt_1);
		textView[1] = (TextView) findViewById(R.id.txt_2);
		textView[2] = (TextView) findViewById(R.id.txt_3);
		textView[3] = (TextView) findViewById(R.id.txt_4);
		vPager_Sc = (ViewPager) findViewById(R.id.vPager_Sc);
		viewLine = new View[4];
		viewLine[0] = findViewById(R.id.view_1);
		viewLine[1] = findViewById(R.id.view_2);
		viewLine[2] = findViewById(R.id.view_3);
		viewLine[3] = findViewById(R.id.view_4);
		mylinearLayout = (LinearLayout) findViewById(R.id.mylinearLayout);
		myview = (View) findViewById(R.id.myview);
		contentLayout = (LinearLayout) findViewById(R.id.content);
		relativeLayout_back = (RelativeLayout) findViewById(R.id.relative_back);
		history = (LinearLayout) findViewById(R.id.LinearLayout_history);
		history.setOnClickListener(this);
		custType = (TextView) findViewById(R.id.custType);
		custName = (TextView) findViewById(R.id.custName);
		custMobile = (TextView) findViewById(R.id.custMobile);
		status = (TextView) findViewById(R.id.status);
		from = (TextView) findViewById(R.id.from);
		lin_call = (LinearLayout) findViewById(R.id.lin_call);
		returnPointLayout = (LinearLayout) findViewById(R.id.return_point_layout);
		declareRewardLayout = (LinearLayout) findViewById(R.id.declare_reward_layout);
		returnPointLayout.setOnClickListener(this);
		declareRewardLayout.setOnClickListener(this);
		lin_message = (LinearLayout) findViewById(R.id.lin_message);
		change = (TextView) findViewById(R.id.change);
		reason = (TextView) findViewById(R.id.reason);
		size = (TextView) findViewById(R.id.size);
	}

	private void initAction() {
		for (int i = 0; i < textView.length; i++) {
			textView[i].setOnClickListener(this);
		}
		vPager_Sc.setOnPageChangeListener(this);
		relativeLayout_back.setOnClickListener(this);
		lin_call.setOnClickListener(this);
		lin_message.setOnClickListener(this);
		status.setOnClickListener(this);
		change.setOnClickListener(this);
	}

	private void initData() {

		customerInforMap = new HashMap<String, String>();
		baseMap = new HashMap<String, String>();
		customerIdentifierMap = new HashMap<String, String>();
		assuranceMap = new HashMap<String, String>();
		froms = bundle.getString("from");
		String jsonString = jsonStrings;
		if (bundle.getString("isHistory") != null) {
			history.setVisibility(View.GONE);
		} else {
			history.setVisibility(View.VISIBLE);

		}

		try {
			orderDetailJson = new JSONObject(jsonString);
			isStatus = orderDetailJson.getString("isStatus");
			JSONArray customerInfor = orderDetailJson.getJSONArray("客户信息");
			JSONArray baseJsonArray = orderDetailJson.getJSONArray("基本信息");
			JSONArray customerIdentifierJsonArray = orderDetailJson
					.getJSONArray("客户资质");
			JSONArray assuranceJsonArray = orderDetailJson.getJSONArray("担保措施");
			customerInforMap = getJsonArray(customerInfor);
			baseMap = getJsonArray(baseJsonArray);
			customerIdentifierMap = getJsonArray(customerIdentifierJsonArray);
			assuranceMap = getJsonArray(assuranceJsonArray);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		setData();
		crmBaseainformation = new CrmBaseainformation(baseMap);
		crmCustomer = new CrmCustomer(customerIdentifierMap);
		crmPledgeWay = new CrmPledgeWay(assuranceMap);
		crmKeepUp = new CrmKeepUp();

		List<Fragment> list = new ArrayList<Fragment>();
		list.add(crmBaseainformation);
		list.add(crmCustomer);
		list.add(crmPledgeWay);
		list.add(crmKeepUp);

		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content, crmBaseainformation);
		fragmentTransaction.commit();

	}

	private void getInfo() {

		fromString = bundle.getString("orderSource");
		orderId = bundle.getString("orderId");
		CustomProgressDialog.startProgressDialog(context);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("orderId", orderId);
		params.addQueryStringParameter("orderSource", fromString);
		myHttpUtils.getHttpJsonString(params, Constans.findOrderDetailsUrl,
				handler, context, 10, Constans.thod_Get_0);
	}

	private void setData() {
		/*
		 * if("01".equals(customerInforMap.get("客户姓名"))){
		 * custType.setText("公司"); }else { custType.setText("个人"); }
		 */
		custType.setText(customerInforMap.get("客户类型"));
		custName.setText(customerInforMap.get("客户姓名"));
		custMobile.setText(customerInforMap.get("客户手机"));
		status.setText(customerInforMap.get("订单状态"));
		if (customerInforMap.get("订单状态").trim().equals("新订单")
				|| customerInforMap.get("订单状态").trim().equals("公司审批通过/成功放款")
				|| customerInforMap.get("订单状态").trim().equals("公司审批中")
				|| customerInforMap.get("订单状态").trim().equals("正常跟进")) {
			mylinearLayout.setVisibility(view.GONE);
			myview.setVisibility(view.GONE);

		}
		phone = customerInforMap.get("客户手机");
		getHistoryList();
		reason.setText(customerInforMap.get("原因"));
		// 订单状态
		/*
		 * Map<String, String> statusMap= new HashMap<String, String>();
		 * statusMap = (Map<String, String>)
		 * MyApplication.getInstance().dictionaryMap.get("MAP_ORDER_STATUS");
		 * status.setText(statusMap.get(orderDetail.getStatus()));
		 */

		// 订单来源
		/*
		 * String fromString="";
		 * if(null==customerInforMap.get("订单状态")||"".equals
		 * (customerInforMap.get("订单状态"))){ fromString="推广订单"; }else {
		 * if("01".equals(orderDetail.getServiceOrderCode())){
		 * fromString="100领单"; }else { fromString="100转单"; } }
		 * from.setText(fromString);
		 */
		from.setText(froms);

	}

	@Override
	protected void onStart() {
		super.onStart();

		getInfo();
	}

	private void getOrderPointStatus() {
		CustomProgressDialog.startProgressDialog(context);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("orderId", orderId);
		CustomProgressDialog.startProgressDialog(context);
		myHttpUtils.getHttpJsonString(params, Constans.applyAuthUrl, handler,
				context, 1, Constans.thod_Get_0);
	}

	private void getOrderRewardStatus() {
		CustomProgressDialog.startProgressDialog(context);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("orderId", orderId);
		CustomProgressDialog.startProgressDialog(context);
		myHttpUtils.getHttpJsonString(params, Constans.applyAuthUrl, handler,
				context, 2, Constans.thod_Get_0);
	}

	// 获取个人历史列表
	private void getHistoryList() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		params.addQueryStringParameter("mobile", phone);
		CustomProgressDialog.startProgressDialog(context);
		myHttpUtils.getHttpJsonString(params, Constans.findOrderHistoryUrl,
				handler, context, 0, Constans.thod_Get_0);
	}

	@Override
	public void onClick(View v) {
		fragmentTransaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.change:
			if ("0".equals(isStatus)) {
				Intent intent2 = new Intent();
				intent2.setClass(SpreadOrderDetailActivity.this,
						ChangeStatusActivity.class);
				intent2.putExtra("main", customerInforMap.get("客户类型"));
				intent2.putExtra("id", orderId);
				startActivityForResult(intent2, 1001);
			} else {
				Constans.Toast(context, isStatusReason);
			}

			break;
		case R.id.status:
			if ("0".equals(isStatus)) {
				Intent intent = new Intent();
				intent.setClass(SpreadOrderDetailActivity.this,
						ChangeStatusActivity.class);
				intent.putExtra("main", customerInforMap.get("客户类型"));
				intent.putExtra("id", orderId);
				startActivityForResult(intent, 1001);
			} else {
				Constans.Toast(context, isStatusReason);
			}
			break;
		case R.id.lin_call:
			Uri uri = Uri.parse("tel:" + customerInforMap.get("客户手机"));
			Intent intent3 = new Intent(Intent.ACTION_DIAL, uri);
			context.startActivity(intent3);
			break;
		case R.id.lin_message:
			Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
			sendIntent.setData(Uri.parse("smsto:" + phone));
			String string = context.getResources().getString(R.string.message);

			// sendIntent.putExtra("sms_body", string);
			sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(sendIntent);
			break;
		case R.id.relative_back:
			finish();
			break;
		case R.id.txt_1:
			textView[1].setTextColor(Color.argb(255, 80, 80, 80));
			textView[0].setTextColor(Color.argb(255, 255, 0, 0));
			SetColor(0);
			// vPager_Sc.setCurrentItem(0);
			fragmentTransaction.replace(R.id.content, crmBaseainformation);
			fragmentTransaction.commit();
			break;
		case R.id.txt_2:
			textView[0].setTextColor(Color.argb(255, 80, 80, 80));
			textView[1].setTextColor(Color.argb(255, 255, 0, 0));
			// textView[1].setTextColor(color.argb(255, 192, 224, 255));
			SetColor(1);
			fragmentTransaction.replace(R.id.content, crmCustomer);
			fragmentTransaction.commit();
			break;
		case R.id.txt_3:
			SetColor(2);
			fragmentTransaction.replace(R.id.content, crmPledgeWay);
			fragmentTransaction.commit();
			break;
		case R.id.txt_4:
			/*
			 * SetColor(3); fragmentTransaction.replace(R.id.content,
			 * crmKeepUp); fragmentTransaction.commit();
			 */
			break;
		case R.id.LinearLayout_history:

			Intent iHistory = new Intent(this, CrmHistoryActivity.class);
			startActivity(iHistory);

			break;

		case R.id.return_point_layout:
			getOrderPointStatus();

			break;

		case R.id.declare_reward_layout:
			getOrderRewardStatus();

			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			textView[0].performClick();
			break;
		case 1:
			textView[1].performClick();
			break;
		case 2:
			textView[2].performClick();
			break;
		case 3:
			textView[3].performClick();
			break;

		default:
			break;
		}

	}

	private void SetColor(int j) {
		for (int i = 0; i < viewLine.length; i++) {
			if (i == j) {
				viewLine[i].setBackgroundColor(getResources().getColor(
						R.color.title_red));
			} else {
				viewLine[i].setBackgroundColor(getResources().getColor(
						R.color.white));
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent arg2) {
		super.onActivityResult(requestCode, arg1, arg2);
		if (requestCode == 1001 && arg2 != null) {
			if (null != arg2.getExtras().getString("status")) {
				status.setText(arg2.getExtras().getString("status"));
				reason.setText(arg2.getExtras().getString("reasons"));
			}
		}

	}

	private Map<String, String> getJsonArray(JSONArray jsonArray) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				String term = jsonObject.getString("term");
				String value = jsonObject.getString("value");
				map.put(term, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			switch (msg.what) {
			case 0:
				try {
					JSONObject object = (JSONObject) msg.obj;
					String itemArray = object.getString("orderList");
					String isLast = object.getString("isLast");
					List<Order> list = Analyze.analyzeCRMOrders(itemArray);
					sizeAcout = list.size();
					size.setText(sizeAcout + "");
					// 把数据放入数据库
					SharedPreferencesHelp.SavaString(context, "orderlist",
							itemArray);
					SharedPreferencesHelp.SavaString(context, "isLast", isLast);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			case 1:
				try {
					JSONObject object1 = (JSONObject) msg.obj;
					int code = object1.getInt("code");
					if (code == 0) {
						Constans.Toast(context, "退点券申请成功");
						returnPointLayout.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case 2:
				try {
					JSONObject object2 = (JSONObject) msg.obj;
					int code = object2.getInt("code");
					if (code == 0) {
						Constans.Toast(context, "申报奖励申请成功");
						declareRewardLayout.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			case 10:
				JSONObject object3 = (JSONObject) msg.obj;
				try {
					jsonStrings = object3.toString();
					if (!object3.isNull("isStatusReason")) {
						isStatusReason = object3.getString("isStatusReason");
					}
					if (!object3.isNull("canBackCash")// 退点券
							&& object3.getString("canBackCash").equals("1")) {
						returnPointLayout.setVisibility(View.VISIBLE);
					} else {
						returnPointLayout.setVisibility(View.GONE);
					}
					if (!object3.isNull("canApplyPoint")// 申报奖励
							&& object3.getString("canApplyPoint").equals("1")) {
						declareRewardLayout.setVisibility(View.VISIBLE);
					} else {
						declareRewardLayout.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				initData();
				break;

			default:
				break;
			}
		};
	};
}
