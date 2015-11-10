package com.surong.leadloan.activity.contact_group;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.surong.leadload.api.CommonServiceBean;
import com.surong.leadload.api.data.Member;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.adapter.NearPeerAdapter;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.Utils;

// 同行查找

	
	//搜索同行列表

public class FriendsActivity extends CommonActivity implements
		OnRefreshListener2<ListView>, OnItemClickListener {

	// 搜索同行列表

	private ListView mlistView;
	private int count;

	// 0代表下一页还有数据， 1代表下一页没有数据
	protected int isData = 0;

	//搜索字段的值

	private NearPeerAdapter mAdapter;
	private boolean mIsloading;

	//下拉刷新，上拉加载数据
	private  PullToRefreshListView mPullToRefreshListView;

	private Context context;

	private View view;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(this, R.layout.friendlist, null);
		addContentView(view);
		changeTitle("同行");
		init();
		/*
		 * 获取机构类型
		 */
	}

	private void init() {

		mlistView = (ListView) view
				.findViewById(R.id.list_fun_page_main);
//		mPullToRefreshListView = (PullToRefreshListView) view
//				.findViewById(R.id.list_fun_page_main);
//		// 设置下拉和上拉模式
//		mPullToRefreshListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
//		// 得到显示ListView
//		mlistView = mPullToRefreshListView.getRefreshableView();
//		// 设置字体颜色为黑色
//		 mPullToRefreshListView.setIsColor(1);
//		// // 设置字体颜色为黑色
//		 mPullToRefreshListView.setTextColor(getResources().getColor(
//		 R.color.black));


		mAdapter = new NearPeerAdapter(this);

	
		CommonServiceBean.sameIndustry(getApplicationContext() ,null, null, null,
				-1, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> paramResponseInfo) {
						CustomProgressDialog.stopProgressDialog();
						List<Member> member = null;
						try {
							JSONObject json = new JSONObject(
									paramResponseInfo.result);
							isData = json.getInt("isLast");
							member = Member.parse(json.getString("memberArray"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (member != null && member.size() > 0) {



							mAdapter.addMember(member);
							mAdapter.notifyDataSetChanged();
							mlistView.setAdapter(mAdapter);
							count = member.size();
						} else {
							Toast.makeText(getApplicationContext(), "没有查找结果",
									Toast.LENGTH_SHORT).show();
						}
						mIsloading = false;
					}

					@Override
					public void onFailure(HttpException paramHttpException,
							String paramString) {
						mIsloading = false;
					}
				});

//		mPullToRefreshListView.setOnRefreshListener(this);

		// mPullToRefreshListView.setOnRefreshListener(this);

	}


	class TextAdapter extends BaseAdapter {

		public TextAdapter(Context context, int resource) {
			super();
		}

		public void setText(String[] t) {
			texts = t;
		}

		private String[] texts = null;

		@Override
		public int getCount() {
			return texts == null ? 0 : texts.length;
		}

		@Override
		public String getItem(int arg0) {
			return texts[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			TextView text = null;
			RadioButton radioButton = null;
			if (arg0 == 0) {
				text = new TextView(context);
				convertView = View.inflate(context, R.layout.spinner_header,
						null);
				text = (TextView) convertView.findViewById(R.id.name);
				text.setText(texts[arg0]);
			} else {
				text = new TextView(context);
				radioButton = new RadioButton(context);
				convertView = View
						.inflate(context, R.layout.spinner_item, null);
				text = (TextView) convertView.findViewById(R.id.name);
				radioButton = (RadioButton) convertView
						.findViewById(R.id.radio);
				radioButton
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// mType.dispatchWindowVisibilityChanged(View.GONE);
							}
						});
				text.setText(texts[arg0]);
			}
			return convertView;
		}
	}


	@Override

	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 下拉刷新
		CustomProgressDialog.startProgressDialog(context);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(context));

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 上拉加载数据
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(context));
		if (1 == isData) {
			Constans.Toast(context, "没有更多数据了");
			handler.sendEmptyMessage(5);
			return;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			// 关闭隐藏下拉

//			mPullToRefreshListView.onRefreshComplete();


			switch (msg.what) {
			// 下拉刷新数据
			case 0:
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// 上拉加载数据
			case 1:
				try {

				} catch (Exception e) {
					e.printStackTrace();
					Log.i("Mytest", e.toString() + "");
				}
				break;
			case 10:
				CustomProgressDialog.stopProgressDialog();
				// 成功获取详细信息数据
				/*
				 * try { js = jsonObject.getString("order"); } catch
				 * (JSONException e) { e.printStackTrace(); }
				 */
				// 解析JSon数据保存于对象
				// 跳转到详细页面以及传详细信息对象过去
				// startActivity(new
				// Intent(getActivity(),SpreadOrderDetailActivity.class));
				break;
			};
		};
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}
}