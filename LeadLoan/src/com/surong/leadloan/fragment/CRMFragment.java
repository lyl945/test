package com.surong.leadloan.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.crm.ContactActivity;
import com.surong.leadloan.activity.crm.SpreadOrderDetailActivity;
import com.surong.leadloan.activity.order.PromotionManagerActivity;
import com.surong.leadloan.activity.personal.ApplyCertification;
import com.surong.leadloan.adapter.CrmApdapter;
import com.surong.leadloan.entity.Order;
import com.surong.leadloan.entity.OrderDetail;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.ui.FiltDialog;
import com.surong.leadloan.ui.SideBar;
import com.surong.leadloan.ui.SideBar.OnTouchingLetterChangedListener;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;
import com.surong.leadloan.utils.ViewUtils;

public class CRMFragment extends BaseFragment implements
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
	// 0代表下一页还有数据， 1代表下一页没有数据
	private int k = 1;
	private String pageNo;
	protected int isData = 0;
	// 数据库操作对象
	// protected DbUtils db;
	// ListView
	protected ListView mlistView;
	// 客户管理
	private List<Order> mlist = new ArrayList<Order>();
	private List<Order> mSortList = new ArrayList<Order>();
	// 客户管理适配器
	private CrmApdapter adapter;
	private OrderDetail orderDetail;
	private LinearLayout tipLayout, listLayout;
	private TextView tip1, tip2, tip3;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private OrderComparator orderComparator;

	private SideBar sideBar;
	private TextView back, title, right;
	private FiltDialog filtDialog;
	private RadioButton radiobutton;
	private String from;
	Set<String> idSet = new HashSet<String>();
	// 获取SharedPreferences对象
	SharedPreferences sharedPre;
	// 获取Editor对象
	Editor editor;
	String status = "";
	String isProm = "";

	private Personal personal;

	@Override
	public void setmActivity(Activity mActivity) {
		super.setmActivity(mActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.crm, container, false);

		initView();
		initData();
		registerBoradcastReceiver();
		return view;
	}

	private void initView() {
		pageNo = k + "";
		// 得到令牌
		token = SharedPreferencesHelp.getString(mActivity, "token");
		// 初始化请求接口对象
		myHttpUtils = MyHttpUtils.myInstance();
		// 初始化ListView
		mPullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.list_fun_page_main);
		// 设置下拉和上拉模式
		mPullToRefreshListView
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
		// 得到显示ListView
		mlistView = mPullToRefreshListView.getRefreshableView();
		ViewUtils.setEmptyView(mActivity, "暂无内容", mlistView);
		// 设置字体颜色为黑色
		mPullToRefreshListView.setIsColor(1);
		// 设置字体颜色为黑色
		mPullToRefreshListView.setTextColor(getResources().getColor(
				R.color.black));
		orderComparator = new OrderComparator();

		sharedPre = mActivity.getSharedPreferences("config",
				mActivity.MODE_PRIVATE);
		editor = sharedPre.edit();

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		filtDialog = new FiltDialog(mActivity, R.style.filt_dialog);

		back = (TextView) view.findViewById(R.id.top_textView_left);
		title = (TextView) view.findViewById(R.id.top_textview_title);
		right = (TextView) view.findViewById(R.id.top_textView_right);
		right.setText("添加");
		title.setText("客户管理");
		back.setText("筛选");
		right.setOnClickListener(this);
		back.setOnClickListener(this);
		back.setCompoundDrawablesWithIntrinsicBounds(R.drawable.filled_filter,
				0, 0, 0);

		tipLayout = (LinearLayout) view.findViewById(R.id.crm_tip_layout);
		listLayout = (LinearLayout) view.findViewById(R.id.crm_list_layout);
		tip1 = (TextView) view.findViewById(R.id.tip1);
		tip2 = (TextView) view.findViewById(R.id.tip2);
		tip3 = (TextView) view.findViewById(R.id.tip3);

	}

	@Override
	public void onStart() {
		super.onStart();
		initData();
	}

	private void initData() {

		CustomProgressDialog.startProgressDialog(mActivity);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		myHttpUtils.getHttpJsonString(params, Constans.findNewOrderUrl,
				handler, mActivity, 0, Constans.thod_Get_0);
		// 得到缓存里的数据

	}

	private void setData() {
		mSortList.clear();
		// 对list进行排序
		mSortList = filledData(mlist);
		// 根据a-z进行排序源数据
		Collections.sort(mSortList, orderComparator);
		// 初始化适配器
		adapter = new CrmApdapter(mActivity, mSortList);
		// 添加适配器
		mlistView.setAdapter(adapter);
		mlistView.setOnItemClickListener(this);
		mPullToRefreshListView.setOnRefreshListener(this);

		if (mlist != null && mlist.size() > 0) {
			listLayout.setVisibility(View.VISIBLE);
			tipLayout.setVisibility(View.GONE);
		} else {
			listLayout.setVisibility(View.GONE);
			tipLayout.setVisibility(View.VISIBLE);
		}

		if (status.equals("00")||status.equals("05")) {
			tip1.setText("亲，还未认证哦!");
			tip2.setText("更多精彩功能等着你，快来认证吧！");
			tip3.setText("去认证");
			tip3.setVisibility(View.VISIBLE);
			tip3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(mActivity,
							ApplyCertification.class));
				}
			});
		}
		if (status.equals("03") || status.equals("04")) {
			tip1.setText(" 亲，还在认证中哦！");
			tip2.setText(" 更多精彩功能等着你，请耐心等待吧！");
			tip3.setVisibility(View.GONE);
		}

		if (status.equals("02")) {
			tip1.setText("亲，您暂时还没有订单!");
			if (isProm.equals("0")) {
				tip2.setText("点击下方按钮开始推广，立即获取一手客户");
			} else {
				tip2.setText("修改推广出价展示在更好位置，可获得更多订单哦");
			}
			tip3.setText("去推广");
			tip3.setVisibility(View.VISIBLE);
			tip3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(mActivity,
							PromotionManagerActivity.class));
				}
			});
		}

		// 设置右侧触摸监听
//		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
//
//			@Override
//			public void onTouchingLetterChanged(String s) {
//				// 该字母首次出现的位置
//				int position = adapter.getPositionForSection(s.charAt(0));
//				if (position != -1) {
//					mlistView.setSelection(position);
//				}
//			}
//		});
	}

	// 按拼音排序
	class OrderComparator implements Comparator<Order> {

		public int compare(Order o1, Order o2) {
			if (o2.getSortLetters().equals("#")) {
				return -1;
			} else if (o1.getSortLetters().equals("#")) {
				return 1;
			} else {
				return o1.getSortLetters().compareTo(o2.getSortLetters());
			}
		}
	}

	@SuppressLint("NewApi")
	public void getListId() {
		SharedPreferences sharedPre = mActivity.getSharedPreferences("config",
				mActivity.MODE_PRIVATE);
		if (sharedPre != null) {
			idSet = sharedPre.getStringSet("read", idSet);
		}
	}

	@SuppressLint("NewApi")
	public void saveListId() {

		editor.putStringSet("read", idSet);
		// 提交
		editor.commit();
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<Order> filledData(List<Order> data) {
		List<Order> mSortList = new ArrayList<Order>();
		getListId();
		for (int i = 0; i < data.size(); i++) {
			Order order = new Order();
			order = data.get(i);
			String pinyin = "";
			if (!idSet.contains(data.get(i).getId())) {
				// 汉字转换成拼音
				pinyin = getPingYin("*" + data.get(i).getCustName());
			} else {
				pinyin = getPingYin(data.get(i).getCustName());
			}
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				order.setSortLetters(sortString.toUpperCase());
			} else if (!idSet.contains(data.get(i).getId())) {
				order.setSortLetters("*");
			} else {
				order.setSortLetters("#");
			}

			mSortList.add(order);
		}
		return mSortList;

	}

	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		if (TextUtils.isEmpty(inputString)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
					if (temp == null || TextUtils.isEmpty(temp[0])) {
						continue;
					}
					output += temp[0].replaceFirst(temp[0].substring(0, 1),
							temp[0].substring(0, 1).toUpperCase());
				} else
					output += java.lang.Character.toString(input[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(mActivity));

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(mActivity));
		Constans.Toast(mActivity, "没有更多数据了");
		handler.sendEmptyMessage(5);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String orderId = String.valueOf(mSortList.get(position - 1).getId());
		from = "";// 订单来源需根据两个字段确定
		if (null == mSortList.get(position - 1).getOrderId()) {
			from = "推广订单";
		} else {
			if ("01".equals(mSortList.get(position - 1).getServiceOrderCode())) {
				from = "100领单";
			} else {
				from = "100转单";
			}
		}
		String fromString = "";
		if ("".equals(mSortList.get(position - 1).getOrderId())
				|| null == mSortList.get(position - 1).getOrderId()) {
			fromString = "1";
		} else {
			fromString = "2";
		}

		Intent intent = new Intent(mActivity, SpreadOrderDetailActivity.class);
		Bundle bun = new Bundle();
		bun.putString("orderSource", fromString);
		bun.putString("orderId", orderId);
		bun.putString("from", from);

		intent.putExtras(bun);

		mActivity.startActivity(intent);

		idSet.add(orderId);
		saveListId();

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			// 关闭隐藏下拉
			mPullToRefreshListView.onRefreshComplete();
			switch (msg.what) {
			// 下拉刷新数据
			case 0:
				try {
					// 清空list集合
					mlist.clear();
					// 得到数据
					JSONObject object = (JSONObject) msg.obj;
					// 把json保存到对象里
					if (!object.isNull("authStatus")) {
						status = object.getString("authStatus");
					}
					if (!object.isNull("isProming")) {
						isProm = object.getString("isProming");
					}

					String itemArray = object.getString("orderList");
					List<Order> list = Analyze.analyzeCRMOrders(itemArray);
					Log.e("123", "list=" + list);
					// 把集合添加到适配器集合里
					mlist.addAll(list);

					// // 对list进行排序
					// mSortList = filledData(mlist);
					// // 根据a-z进行排序源数据
					// Collections.sort(mSortList, orderComparator);
					// // 初始化适配器
					// adapter = new CrmApdapter(mActivity, mSortList);
					// mlistView.setAdapter(adapter);
					// // 把数据放入数据库
					// SharedPreferencesHelp.SavaString(mActivity,
					// "CRMorderList",
					// itemArray);
					// // 刷新适配器
					// adapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
				setData();
				break;
			// 上拉加载数据
			case 1:
				try {
					JSONObject object = (JSONObject) msg.obj;
					// 得到是否有下一页判断
					isData = object.getInt("isLast");
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
				CustomProgressDialog.stopProgressDialog();
				// 成功获取详细信息数据
				JSONObject jsonObject = (JSONObject) msg.obj;
				// try {

//				String js = "";

				// 解析JSon数据保存于对象
//				orderDetail = Analyze.analyzeCRMOrdersDetail(js);
				// 跳转到详细页面以及传详细信息对象过去
				// Intent intent = new Intent(mActivity,
				// SpreadOrderDetailActivity.class);
				// Bundle bun = new Bundle();
				// bun.putString("orderDetail", jsonObject.toString());
				// bun.putString("from", from);
				//
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
				//
				// intent.putExtras(bun);
				//
				// mActivity.startActivity(intent);
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }

				break;
			}
		};
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(Constans.CRM_ACTION_NAME);
		// 注册广播
		mActivity.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constans.CRM_ACTION_NAME)) {
				mlist.clear();
				Bundle bundle = intent.getExtras();
				// List<Order> list1 = (List<Order>) mActivity.getIntent()
				// .getSerializableExtra("orderList");
				List<Order> list = (List<Order>) bundle
						.getSerializable("orderList");
				mlist.addAll(list);
				// 对list进行排序
				mSortList = filledData(mlist);
				// 根据a-z进行排序源数据
				Collections.sort(mSortList, orderComparator);
				// 初始化适配器
				adapter = new CrmApdapter(mActivity, mSortList);
				mlistView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				// context.unregisterReceiver(mBroadcastReceiver);
			}
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_textView_left:

			Window dialogWindow = filtDialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
			lp.x = 0; // 新位置X坐标
			lp.y = 200; // 新位置Y坐标
			// 147标识title的高度像素值
			lp.height = dialogWindow.getWindowManager().getDefaultDisplay()
					.getHeight() - 147;
			dialogWindow.setAttributes(lp);
			// 点击外部区域时，dialog消失

			filtDialog.show();

			break;
		case R.id.top_textView_right:
			startActivity(new Intent(mActivity, ContactActivity.class));
			break;

		default:
			break;
		}

	}

}
