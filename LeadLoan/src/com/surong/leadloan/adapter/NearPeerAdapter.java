package com.surong.leadloan.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.easemob.chatuidemo.res.EASEConstants;
import com.pj.core.NotificationCenter;
import com.pj.core.NotificationCenter.NotificationListener;
import com.pj.core.http.HttpImage;
import com.pj.core.utilities.DimensionUtility;
import com.surong.leadload.api.data.AddFriendEntry;
import com.surong.leadload.api.data.Member;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.contact_group.NearPeerActivity;
import com.surong.leadloan.activity.personal.MShopActivity;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.LocationUtil;

public class NearPeerAdapter extends BaseAdapter implements
		NotificationListener {
	private LayoutInflater inflater;
	private List<Member> mMembers = new ArrayList<Member>();
	private Context mContext;
	private Set<String> mAdded = new HashSet<String>();

	class Holder {
		TextView nick, trends, distance;
		Button operation;
		ImageView head;
	}

	public NearPeerAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		mContext = context;

		NotificationCenter.getDefaultCenter().removeNotificationListener(this,
				null, EASEConstants.N_FRIEND_APPLY);
		NotificationCenter.getDefaultCenter().addNotificationListener(this,
				(Object) null, EASEConstants.N_FRIEND_APPLY);
	}

	public NearPeerAdapter addMember(List<Member> m) {
		mMembers.addAll(m);
		return this;
	}

	@Override
	public int getCount() {
		return mMembers.size();
	}

	@Override
	public Object getItem(int position) {

		return mMembers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clear() {
		mMembers.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final Member member = mMembers.get(position);
		final Holder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.buddy_listview_child_item, null);
			holder = new Holder();
			holder.nick = (TextView) view
					.findViewById(R.id.buddy_listview_child_nick);
			holder.head = (ImageView) view
					.findViewById(R.id.buddy_listview_child_avatar);
			holder.trends = (TextView) view
					.findViewById(R.id.buddy_listview_child_trends);
			holder.distance = (TextView) view.findViewById(R.id.distance);
			holder.operation = (Button) view.findViewById(R.id.operation_name);
			holder.operation.setBackgroundResource(R.drawable.addfriend_bj);
			holder.operation.setText(mContext.getString(R.string.add_friend));
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		HttpImage.getInstance().setImage(member.getHeadImgPath(), holder.head,
				R.drawable.default_avatar, DimensionUtility.dp2px(20));

		holder.nick.setText(member.getDisplayName());
		holder.trends.setText(member.getShortName());
		holder.operation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AddFriendEntry addFriendEntry = new AddFriendEntry();
				addFriendEntry.member = member;
				addFriendEntry.type = AddFriendEntry.TYPE_ID;
				addFriendEntry.extraObject = member;

				Intent intent = new Intent(EASEConstants.ACTION_ADD_FRIEND);
				intent.putExtra(EASEConstants.KEY_ADD_FRIEND_ENTRY,
						addFriendEntry);

				mContext.startActivity(intent);

				return;
			}

		});

		if (!TextUtils.isEmpty(member.getLatitude())
				&& !TextUtils.isEmpty(member.getLongitude())) {
			holder.distance.setVisibility(View.VISIBLE);
			BDLocation loca = LocationUtil.getInstance(mContext).getLocation();
			double dis = Constans.distanceBetween(NearPeerActivity.latitudess,
					NearPeerActivity.longitudess,
					Double.parseDouble(member.getLatitude()),
					Double.parseDouble(member.getLongitude()));
			holder.distance.setText(distanceString(dis));
		} else {
			holder.distance.setVisibility(View.INVISIBLE);
		}

		if (mAdded.contains(member.getId())) {
			holder.operation.setText(mContext
					.getString(R.string.waiting_accepted));
			holder.operation.setEnabled(false);
		} else {
			holder.operation.setText(mContext.getString(R.string.add_friend));
			holder.operation.setEnabled(true);
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i1 = new Intent(mContext, MShopActivity.class);
				i1.putExtra("memberId", member.getId());
				i1.putExtra("mobile", member.getMobile());
				mContext.startActivity(i1);
			}
		});
		return view;
	}

	@Override
	public void onReceivedNotification(Object arg0, int arg1, Object arg2) {
		if (arg1 == EASEConstants.N_FRIEND_APPLY) {
			if (arg2 != null) {
				AddFriendEntry entry = (AddFriendEntry) arg2;
				if (entry.success) {// 成功添加
					mAdded.add(entry.member.getId());
					notifyDataSetChanged();
				}
			}
		}
	}

	private String distanceString(double dis) {
		if (dis < 500) {
			return String.format("%.0f 米", (float) dis);
		} else {

			dis *= 0.001;

			String dw = "";

			// String dig = Double.toString(Math.round(dis));
			// int len = dig.length();
			//
			// if (len > 3) {
			// dis *= 0.001;
			// dw = "万";
			// } else if (len > 2) {
			// dw = "千";
			// dis *= 0.01;
			// }

			return String.format("%.2f%s 公里", dis, dw);
		}
	}

}
