package com.surong.leadloan.activity.contact_group;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.surong.leadload.api.CommonServiceBean;
import com.surong.leadload.api.HttpServiceBean;
import com.surong.leadload.api.data.InstituResult;
import com.surong.leadload.api.data.Member;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.adapter.NearPeerAdapter;
import com.surong.leadloan.ui.MyButton;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.Utils;

// 同行查找
public class PeerActivity extends CommonActivity implements
		OnRefreshListener2<ListView>, OnItemClickListener {
	// 机构类型数组
	private String[] m = null;

	private String[] typeid = null;
	// 推荐同行列表
	private ListView mResult;
	// 搜索同行列表
	private ListView mlistView;
	private View view;
	// 搜索按钮
	private Button mSearch;
	private int count;
	// LinearLayout触发Spinner
	private LinearLayout spin;
	// 用来实现同行查找结果页 增加搜索标签 可删除
	private LinearLayout LinearLayout1, LinearLayout2, LinearLayout3,
			myLinearLayout, LinearLayout4;
	// FrameLayout布局的切换
	private FrameLayout mFragment;
	private TextView mCity;
	// 搜索字段
	private EditText mAbbr, mFind;
	private Spinner mType;
	// 0代表下一页还有数据， 1代表下一页没有数据
	protected int isData = 0;
	private String cityId = "440300";
	private String CityId;
	// 搜索字段的值
	private TextView TextView1, TextView2, TextView3, TextView4;
	// 搜索结果的适配器
	private NearPeerAdapter mAdapter;
	private NearPeerAdapter mAdapter2;

	private boolean mIsloading;
	private String cityname = "深圳市";
	// 下拉刷新，上拉加载数据
	protected PullToRefreshListView mPullToRefreshListView;
	// Spinner选中的值
	String typeId = "";
	String type = "";
	private ArrayAdapter<String> adapter;
	private Context context;
	// 重写父类的点击方法
	private RelativeLayout back;
	// 判断返回键的标示
	private boolean flag;
	private int InstiueNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(this, R.layout.cg_peer, null);
		addContentView(view);
		changeTitle("同行");
		back();
		init();
		/*
		 * 获取机构类型
		 */
		Map<String, String> hobbyMap = MyApplication.dictionaryMap2
				.get("MAP_INSTITU_TYPE");
		Iterator<Entry<String, String>> iterator = hobbyMap.entrySet()
		.iterator();
		m = new String[hobbyMap.size() + 1];
		typeid = new String[hobbyMap.size() + 1];
		m[0] = "全部";
		typeid[0] = "";
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			InstiueNum++;
			m[InstiueNum] = entry.getKey();
			typeid[InstiueNum] = entry.getValue();
		}
		adapter = new ArrayAdapter<String>(PeerActivity.this,
				R.layout.spinner_header, m);
		mType.setAdapter(adapter);
		mType.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();

//		CustomProgressDialog.startProgressDialog(context);
//		HttpServiceBean.httpGetInstitu(this, new RequestCallBack<String>() {
//
//			@Override
//			public void onSuccess(ResponseInfo<String> data) {
//				CustomProgressDialog.stopProgressDialog();
//				try {
//					List<InstituResult> results = InstituResult
//							.parse(new JSONObject(data.result)
//									.getString("typeArray"));
//					List<InstituResult> result = LoginActivity.result;
//					m = new String[result.size() + 1];
//					typeid = new String[result.size() + 1];
//
//					m[0] = "全部";
//					for (int i = 0; i < result.size(); i++) {
//						m[i + 1] = result.get(i).getTypeName();
//						if (result.get(i).getTypeId() > 9) {
//							typeid[i + 1] = "" + result.get(i).getTypeId();
//						} else {
//							typeid[i + 1] = "0" + result.get(i).getTypeId();
//						}
//
//					}
//					// adapter = new TextAdapter(PeerActivity.this, 0);
//					// adapter.setText(m);
//					// 设置下拉列表的风格
//					// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					adapter = new ArrayAdapter<String>(PeerActivity.this,
//							R.layout.spinner_header, m);
//					// adapter.setDropDownViewResource(R.layout.spinner_item);
//					// 将adapter 添加到spinner中
//					mType.setAdapter(adapter);
//					// 添加事件Spinner事件监听
//					// mType.setOnItemSelectedListener(new
//					// SpinnerSelectedListener());
//					// 设置默认值
//					mType.setVisibility(View.VISIBLE);
//					adapter.notifyDataSetChanged();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				List<InstituResult> result = LoginActivity.result;
//				m = new String[result.size() + 1];
//				typeid = new String[result.size() + 1];
//				m[0] = "全部";
//				for (int i = 0; i < result.size(); i++) {
//					m[i + 1] = result.get(i).getTypeName();
//					if (result.get(i).getTypeId() > 9) {
//						typeid[i + 1] = "" + result.get(i).getTypeId();
//					} else {
//						typeid[i + 1] = "0" + result.get(i).getTypeId();
//					}
//				}
//				adapter = new ArrayAdapter<String>(PeerActivity.this,
//						R.layout.spinner_header, m);
//				mType.setAdapter(adapter);
//				mType.setVisibility(View.VISIBLE);
//				adapter.notifyDataSetChanged();
//			}
//		});
	}

	private void back() {
		back = this.relative_back;
	}

	private void init() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!flag) {
					finish();

				} else {
					mFragment.setVisibility(View.GONE);
					mAdapter.clear();
				}
				flag = !flag;
			}
		});
		mPullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.list_fun_page_main);
		// 设置下拉和上拉模式
		mPullToRefreshListView
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_START);
		// 得到显示ListView
		mlistView = mPullToRefreshListView.getRefreshableView();
		// 设置字体颜色为黑色
		mPullToRefreshListView.setIsColor(1);
		// // 设置字体颜色为黑色
		mPullToRefreshListView.setTextColor(getResources().getColor(
				R.color.black));
		mSearch = (Button) view.findViewById(R.id.peer_search);

		mCity = (TextView) view.findViewById(R.id.city);
		mType = (Spinner) view.findViewById(R.id.institution_type);
		mAbbr = (EditText) view.findViewById(R.id.institution_abbr);
		mFind = (EditText) view.findViewById(R.id.mFind);
		LinearLayout1 = (LinearLayout) view.findViewById(R.id.LinearLayout1);
		myLinearLayout = (LinearLayout) view.findViewById(R.id.myLinearLayout);
		LinearLayout2 = (LinearLayout) view.findViewById(R.id.LinearLayout2);
		LinearLayout3 = (LinearLayout) view.findViewById(R.id.LinearLayout3);
		LinearLayout4 = (LinearLayout) view.findViewById(R.id.LinearLayout4);
		TextView1 = (TextView) view.findViewById(R.id.TextView1);
		TextView2 = (TextView) view.findViewById(R.id.TextView2);
		TextView3 = (TextView) view.findViewById(R.id.TextView3);
		TextView4 = (TextView) view.findViewById(R.id.TextView4);

		mResult = (ListView) view.findViewById(R.id.peer_result);

		mAdapter = new NearPeerAdapter(this);
		mAdapter2 = new NearPeerAdapter(this);
		mFragment = (FrameLayout) view.findViewById(R.id.peer_result_layout);
		spin = (LinearLayout) findViewById(R.id.spin);
		spin.setOnClickListener(this);
		mCity.setOnClickListener(this);
		LinearLayout1.setOnClickListener(this);
		LinearLayout2.setOnClickListener(this);
		LinearLayout3.setOnClickListener(this);
		LinearLayout4.setOnClickListener(this);
		mSearch.setOnClickListener(this);
		mResult.setAdapter(mAdapter2);
		CustomProgressDialog.startProgressDialog(context);
		CommonServiceBean.sameIndustry(getApplicationContext(), null, null,
				null, -1, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> paramResponseInfo) {
						CustomProgressDialog.stopProgressDialog();
						List<Member> member = null;
						List<Member> members = null;
						try {
							JSONObject json = new JSONObject(
									paramResponseInfo.result);
							// provinceId":"440000","instituName":"深圳市亚联财小额信贷有限公司","cityId":"440300","cityName":"深圳市","provinceName":"广东省","instituId":"f1e27e0e441ee18001441fa027760014","typeName":"小贷公司","id":"f1eadfb3478bdc2f01479f8a36b90048","realName":"邓江","shortName":"亚联财","headImgPath":"http:\/\/f.surong100.com\/upload\/personal\/HeadImgPathf1eadfb3478bdc2f01479f8a36b90048\/headImg.jpg","displayName":"邓经理","typeId":"02","mobile":"18588276027","memberLevel":"01"},

							member = Member.parse(json.getString("memberArray"));
							// Toast.makeText(PeerActivity.this,
							// member.size()+"", Toast.LENGTH_SHORT).show();
							//
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (member != null && member.size() > 0) {
							mAdapter2.addMember(member);
							mAdapter2.notifyDataSetChanged();
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
	}

	@Override
	public void onBackPressed() {
		flag = !flag;
		if (mFragment.isShown()) {
			mFragment.setVisibility(View.GONE);
			mAdapter.clear();
			return;
		}
		super.onBackPressed();
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
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.city:
			Intent i = new Intent(PeerActivity.this, CitySelectActivity.class);
			i.putExtra("cityname", mCity.getText().toString());
			// startActivity(new
			// Intent(PeerActivity.this,CitySelectActivity.class));
			startActivityForResult(i, 1);
			// int version = Integer.valueOf(android.os.Build.VERSION.SDK);
			// if (version >= 5) {
			// overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			// }
			// int version = Integer.valueOf(android.os.Build.VERSION.SDK);
			// if (version >= 5) {
			// overridePendingTransition(R.anim.zoomin, R.anim.zoomout); //
			// overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
			// overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			// }
			break;

		case R.id.relative_back:
			finish();
			break;
		case R.id.LinearLayout1:

			/*
			 * 隐藏搜索字段的布局，搜索字段无要求，其它搜索条件不变 ，继续搜索
			 */
			TextView1.setText("");
			LinearLayout1.setVisibility(View.GONE);
			search(TextView1.getText().toString(), CityId, TextView4.getText()
					.toString(), typeId);
			break;
		case R.id.LinearLayout2:
			CityId = "";
			LinearLayout2.setVisibility(View.GONE);
			search(TextView1.getText().toString(), CityId, TextView4.getText()
					.toString(), typeId);
			break;
		case R.id.LinearLayout3:
			LinearLayout3.setVisibility(View.GONE);
			search(TextView1.getText().toString(), CityId, TextView4.getText()
					.toString(), typeId);
			break;
		case R.id.LinearLayout4:
			TextView4.setText("");
			typeId = "";
			LinearLayout4.setVisibility(View.GONE);
			search(TextView1.getText().toString(), CityId, TextView4.getText()
					.toString(), typeId);
			break;
		case R.id.spin:
			// spinner触发
			mType.performClick();
			break;
		case R.id.peer_search:
			flag = !flag;
			typeId = typeid[mType.getSelectedItemPosition()];
			type = m[mType.getSelectedItemPosition()];
			CityId = cityId;
			search( mFind.getText().toString(), CityId, type, typeId);
			break;
		default:
			break;
		}
	}

	private void search(String mFind_text, String mCity_text,
			String mAbbr_text, String id) {

		if (m != null) {
			if (!TextUtils.isEmpty(mFind_text)) {
				LinearLayout1.setVisibility(view.VISIBLE);
				TextView1.setText(mFind_text);
			}else {
				TextView1.setText("");
				LinearLayout1.setVisibility(view.GONE);
			}
			if (!TextUtils.isEmpty(mCity_text)) {
				LinearLayout2.setVisibility(view.VISIBLE);
				TextView2.setText(cityname);
			}
			if (!TextUtils.isEmpty(mAbbr_text)) {
				if (mAbbr_text.trim().equals("全部")) {
					LinearLayout4.setVisibility(view.GONE);
				} else {
					LinearLayout4.setVisibility(view.VISIBLE);
				}
				TextView4.setText(type);
			}
			if (TextUtils.isEmpty(mFind_text) && TextUtils.isEmpty(id)
					&& TextUtils.isEmpty(mCity_text)) {
				myLinearLayout.setVisibility(view.GONE);
			} else {
				myLinearLayout.setVisibility(view.VISIBLE);
			}

		}
		mFragment.setVisibility(View.VISIBLE);
		CustomProgressDialog.startProgressDialog(context);
		mIsloading = true;
		/*
		 * 根据搜索字段mFind_text，城市id ，mCity_text，机构类型mAbbr_text搜索
		 */
		CommonServiceBean.sameIndustry(getApplicationContext(), mFind_text, id,
				mCity_text, -1, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> paramResponseInfo) {
						CustomProgressDialog.stopProgressDialog();
						mPullToRefreshListView.onRefreshComplete();
						mAdapter.clear();
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
		mPullToRefreshListView.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 下拉刷新
		CustomProgressDialog.startProgressDialog(context);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(context));
		search( TextView1.getText().toString(), CityId, TextView4.getText()
				.toString(), typeId);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// 上拉加载数据
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrenTime(context));
		// refreshView.getLoadingLayoutProxy().setRefreshingLabel("");
		// refreshView.getLoadingLayoutProxy().setPullLabel("上拉加载");
		// 设置释放标签
		// refreshView.getLoadingLayoutProxy().setReleaseLabel("");
		// 设置上一次刷新的提示标签
		// refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
		CustomProgressDialog.startProgressDialog(context);
		if (1 == isData) {
			Constans.Toast(context, "没有更多数据了");
			handler.sendEmptyMessage(5);
			return;
		}
		handler.sendEmptyMessage(5);
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
			}
		};
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	protected void onActivityResult(int resultCode, int re, Intent data) {

		// if (data == null) {
		//
		// return;
		cityname = data.getStringExtra("cityname");
		cityId = data.getStringExtra("cityId");
		mCity.setText(cityname);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // getMenuInflater().inflate(R.menu.activity_main, menu);
	// return true;
	// }

}