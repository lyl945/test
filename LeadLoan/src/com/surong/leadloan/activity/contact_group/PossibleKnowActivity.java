package com.surong.leadloan.activity.contact_group;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.surong.leadload.api.BaiduMapTool;
import com.surong.leadload.api.CommonServiceBean;
import com.surong.leadload.api.data.Member;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.adapter.NearPeerAdapter;
import com.surong.leadloan.adapter.PossibleKnowAdapter;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.LocationUtil;

public class PossibleKnowActivity extends CommonActivity {
	private ListView mResult;
	private View view;

	private PossibleKnowAdapter mAdapter;
	private int mPage = 1;
	private boolean mIslast;
	private boolean mIsloading;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(this, R.layout.possible_know, null);
		addContentView(view);
		changeTitle("可能认识的人");
		mResult = (ListView) view.findViewById(R.id.cg_near_list);
		mAdapter = new PossibleKnowAdapter(this);
		mResult.setAdapter(mAdapter);
		getNearFriend();
		LocationUtil.getInstance(this).request();
		// mLocationClient.
		// mMyLocationListener = new MyLocationListener();
		// LocationUtil.getInstance(this).setLocationListener(mMyLocationListener);
		// LocationUtil.getInstance(this).request();

		mResult.setCacheColorHint(Color.TRANSPARENT);
		mResult.setDividerHeight(0);

	}

	private void getNearFriend() {
		CustomProgressDialog.startProgressDialog(context);
		CommonServiceBean.possibleKnow(PossibleKnowActivity.this,
				new RequestCallBack<String>() {

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
							mAdapter.addMember(member);
							// ListAdapter listAdapter = mResult.getAdapter();
							// if (listAdapter == null) {
							// // pre-condition
							// return;
							// }
							// int totalHeight = 0;
							// for (int i = 0; i < listAdapter.getCount(); i++)
							// {
							// View listItem = listAdapter.getView(i, null,
							// mResult);
							// listItem.measure(0, 0);
							// totalHeight += listItem.getMeasuredHeight();
							// }
							// ViewGroup.LayoutParams params = mResult
							// .getLayoutParams();
							// params.height = totalHeight
							// + (mResult.getDividerHeight() * (listAdapter
							// .getCount() - 1));
							// mResult.setLayoutParams(params);
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
