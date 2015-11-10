package com.surong.leadloan.activity.contact_group;

import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

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
import com.surong.leadloan.utils.DistanceComparator;
import com.surong.leadloan.utils.LocationUtil;

public class NearPeerActivity extends CommonActivity {
	private ListView mResult;
	private View view;

	// private MyLocationListener mMyLocationListener;
	private NearPeerAdapter mAdapter;
	private int mPage = 1;
	private boolean mIslast;
	private boolean mIsloading;
	public static double latitudess;
	public static double longitudess;
	private Context context;

	// public class MyLocationListener implements BDLocationListener {
	//
	// @Override
	// public void onReceiveLocation(BDLocation location) {
	// // Receive Location
	// StringBuffer sb = new StringBuffer(256);
	// sb.append("time : ");
	// sb.append(location.getTime());
	// sb.append("\nerror code : ");
	// sb.append(location.getLocType());
	// sb.append("\nlatitude : ");
	// sb.append(location.getLatitude());
	// sb.append("\nlontitude : ");
	// sb.append(location.getLongitude());
	// sb.append("\nradius : ");
	// sb.append(location.getRadius());
	// mIsloading = true;
	// CommonServiceBean.nearPeer(NearPeerActivity.this, mPage,
	// longitudess, latitudess,
	// new RequestCallBack<String>() {
	//
	// @Override
	// public void onSuccess(
	// ResponseInfo<String> paramResponseInfo) {
	//
	// List<Member> member = null;
	// try {
	// JSONObject json = new JSONObject(
	// paramResponseInfo.result);
	// mIslast = json.getInt("isLast") == 1;
	// member = Member.parse(json
	// .getString("memberArray"));
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// if (member != null && member.size() > 0) {
	// mAdapter.addMember(member);
	// mAdapter.notifyDataSetChanged();
	// }
	// mIsloading = false;
	// }
	//
	// @Override
	// public void onFailure(HttpException paramHttpException,
	// String paramString) {
	// mIsloading = false;
	// }
	// });
	// }
	//
	// @Override
	// public void onReceivePoi(BDLocation arg0) {
	//
	// }
	//
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(this, R.layout.cg_near_peer, null);
		addContentView(view);
		changeTitle("附近的同行");
		latitudess = getIntent().getExtras().getDouble("latitude");
		longitudess = getIntent().getExtras().getDouble("longitude");
		 if (latitudess == 4.9e-324) {
         	Toast.makeText(context, "通过网络对您的手机定位被拒绝,您可以到安全中心->权限管理打开", Toast.LENGTH_SHORT).show();
			}else {
				mResult = (ListView) view.findViewById(R.id.cg_near_list);
				mAdapter = new NearPeerAdapter(this);
				mResult.setAdapter(mAdapter);
				getNearFriend();
				LocationUtil.getInstance(this).request();
				// mLocationClient.
				// mMyLocationListener = new MyLocationListener();
				// LocationUtil.getInstance(this).setLocationListener(mMyLocationListener);
				// LocationUtil.getInstance(this).request();
				mResult.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) {

					}

					@Override
					public void onScroll(AbsListView list, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						if (firstVisibleItem + visibleItemCount == totalItemCount
								&& totalItemCount > 0 && !mIslast && mPage < 3
								&& !mIsloading) {
							mIsloading = true;
							CustomProgressDialog.startProgressDialog(context);
							CommonServiceBean.nearPeer(NearPeerActivity.this, ++mPage,
									longitudess, latitudess,
									new RequestCallBack<String>() {

										@Override
										public void onSuccess(
												ResponseInfo<String> paramResponseInfo) {
											CustomProgressDialog.stopProgressDialog();

											List<Member> member = null;
											List<Member> members = null;
											try {
												JSONObject json = new JSONObject(
														paramResponseInfo.result);
												mIslast = json.getInt("isLast") == 1;
												member = Member.parse(json
														.getString("memberArray"));
											} catch (JSONException e) {
												e.printStackTrace();
											}

											if (member != null && member.size() > 0) {
												for (int i = 0; i < 5; i++) {
													members.add(member.get(i));
												}
												mAdapter.addMember(members);
												mAdapter.notifyDataSetChanged();
											}
											mIsloading = false;
										}

										@Override
										public void onFailure(
												HttpException paramHttpException,
												String paramString) {
											CustomProgressDialog.stopProgressDialog();
											mIsloading = false;
										}
									});
						}
					}
				});

				mResult.setCacheColorHint(Color.TRANSPARENT);
				mResult.setDividerHeight(0);
			}
		

	}

	private void getNearFriend() {
		CustomProgressDialog.startProgressDialog(context);
		CommonServiceBean.nearPeer(NearPeerActivity.this, mPage, longitudess,
				latitudess, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> paramResponseInfo) {
						CustomProgressDialog.stopProgressDialog();
						List<Member> member = null;
						try {
							JSONObject json = new JSONObject(
									paramResponseInfo.result);
							mIslast = json.getInt("isLast") == 1;
							member = Member.parse(json.getString("memberArray"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (member != null && member.size() > 0) {
							Collections.sort(member, new DistanceComparator(
									member, latitudess, longitudess));
							mAdapter.addMember(member);
							mAdapter.notifyDataSetChanged();
						}
						mIsloading = false;
					}

					@Override
					public void onFailure(HttpException paramHttpException,
							String paramString) {
						CustomProgressDialog.stopProgressDialog();
						mIsloading = false;
					}
				});
	}

	@Override
	protected void onDestroy() {
		// LocationUtil.getInstance(getApplicationContext()).removeLocationListener(mMyLocationListener);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.relative_back:
			finish();
			break;

		default:
			break;
		}
	}
}
