package com.surong.leadloan.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.pj.core.managers.LogManager;
import com.pj.core.utilities.StringUtility;
import com.surong.leadload.api.data.Friend;
import com.surong.leadloan.R;
import com.surong.leadloan.ui.CircleImageView;
import com.surong.leadloan.utils.Constans;

public class BuddyAdapter extends BaseExpandableListAdapter implements
		OnClickListener,OnScrollListener {
	private String[] group;
	private List<List<Friend>> buddy = new ArrayList<List<Friend>>();
	private List<List<Friend>> search = new ArrayList<List<Friend>>();
	private Context context;
	LayoutInflater inflater;
	private boolean isSearch = false;
	private String tel;
	private AlertDialog verificationDialog;
	private TextView t_title, t_message;
	private Button b_queding, b_quxiao;
	private DisplayImageOptions options;

	public BuddyAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		
	}

	public void setGroup(String[] group) {
		this.group = group;
	}

	public void setData(List<List<Friend>> d) {
		this.buddy = d;
	}

	public Object getChild(int groupPosition, int childPosition) {
		if (!isSearch) {
			return buddy.get(groupPosition).get(childPosition);
		} else {
			return search.get(groupPosition).get(childPosition);

		}
	}

	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition * 10 + childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean arg2, View convertView, ViewGroup arg4) {
		ViewHolder holder = null;
		options = new DisplayImageOptions.Builder()
				// .showImageOnLoading(R.drawable.default_avator)
				.showImageForEmptyUri(R.drawable.default_avatar)
				.showImageOnFail(R.drawable.default_avatar).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.displayer(new SimpleBitmapDisplayer()).build();
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.buddy_listview_child_item,
					null);

			holder.nickTextView = (TextView) convertView
					.findViewById(R.id.buddy_listview_child_nick);

			holder.operation = (Button) convertView
					.findViewById(R.id.operation_name);

			holder.com = (TextView) convertView
					.findViewById(R.id.buddy_listview_child_trends);

			holder.imageView = (CircleImageView) convertView
					.findViewById(R.id.buddy_listview_child_avatar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ViewHolder holders = holder;
		Friend friend = null;
		if (isSearch) {
			friend = search.get(groupPosition).get(childPosition);
		} else {
			friend = buddy.get(groupPosition).get(childPosition);

		}
		final Friend friends = friend;
		holder.operation.setTag(R.id.operation_name, friend);
		holder.operation.setOnClickListener(this);

		holder.nickTextView.setText(StringUtility.select(true,
				friend.getRemarkName(), friend.getDisplayName(), "--"));
		holder.com.setText(friend.getInstituShortName());
		
					ImageLoader.getInstance().displayImage(friends.getHeadImgPath(),
							holders.imageView, options, null);
		// HttpImage.getInstance().setImage(friend.getHeadImgPath(),
		// holder.imageView, R.drawable.default_avatar,
		// DimensionUtility.dp2px(40) * 0.5f)

		return convertView;
	}

	static class ViewHolder {
		TextView com, nickTextView;
		CircleImageView imageView;
		Button operation;
	}

	public void search(String text) {

		if (StringUtility.isEmpty(StringUtility.trim(text))) {
			reset();
			return;
		}

		search.clear();
		isSearch = true;
		for (List<Friend> g : buddy) {
			List<Friend> sg = new ArrayList<Friend>();
			for (Friend f : g) {
				String remarkName = f.getRemarkName();
				String displayName = f.getDisplayName();

				if (TextUtils.isEmpty(remarkName)) {
					remarkName = "";
				}

				if (TextUtils.isEmpty(displayName)) {
					displayName = "";
				}

				if (remarkName.contains(text) || displayName.contains(text)) {
					sg.add(f);
				}
			}
			search.add(sg);
		}
		notifyDataSetChanged();
	}

	public void reset() {
		isSearch = false;
		search.clear();
		notifyDataSetChanged();
	}

	public int getChildrenCount(int groupPosition) {
		if (!isSearch) {
			return buddy.size() == 0 ? 0 : buddy.get(groupPosition).size();
		} else {

			return search.size() == 0 ? 0 : search.get(groupPosition).size();
		}
	}

	public Object getGroup(int groupPosition) {
		return group == null ? null : group[groupPosition];
	}

	public int getGroupCount() {
		return group == null ? 0 : group.length;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup arg3) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.buddy_listview_group_item,
					arg3, false);
		}

		TextView groupNameTextView = (TextView) convertView
				.findViewById(R.id.buddy_listview_group_name);
		groupNameTextView.setText(getGroup(groupPosition).toString());
		ImageView image = (ImageView) convertView
				.findViewById(R.id.buddy_listview_image);
		image.setImageResource(isExpanded ? R.drawable.down
				: R.drawable.put_away);

		return convertView;
	}

	public boolean hasStableIds() {
		return false;
	}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
//			isLyl[groupPosition][childPosition] = true;
//			notifyDataSetChanged();
			return true;
			}

	@Override
	public void onClick(View v) {

		final Object object = v.getTag(R.id.operation_name);
		if (object instanceof Friend) {
			Friend friend = (Friend) object;
			tel = friend.getMobile();
			verificationDialog = new AlertDialog.Builder(context).create();
			verificationDialog.show();
			verificationDialog.getWindow()
					.setContentView(R.layout.cust_diaolog);
			t_title = (TextView) verificationDialog.getWindow().findViewById(
					R.id.title);
			t_message = (TextView) verificationDialog.getWindow().findViewById(
					R.id.text_message);
			b_queding = (Button) verificationDialog.getWindow().findViewById(
					R.id.btn_quedingWf);
			b_quxiao = (Button) verificationDialog.getWindow().findViewById(
					R.id.btn_quXiaoWf);
			t_title.setVisibility(View.GONE);
			t_message.setText(tel);
			b_queding.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (!StringUtility.isEmpty(tel)) {
						tel = tel.replaceAll("\\s+|\\-", "");
						LogManager.i(getClass().getSimpleName(), "打电话:%s", tel);

						Intent intent = new Intent(Intent.ACTION_DIAL, Uri
								.parse("tel:" + tel));
						try {
							context.startActivity(intent);
						} catch (Exception e) {
							Toast.makeText(context, "拨号失败", Toast.LENGTH_LONG)
									.show();

						}
					}
					verificationDialog.dismiss();
				}
			});
			b_quxiao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					verificationDialog.dismiss();
				}
			});
			// new AlertDialog.Builder(context)
			// .setMessage(tel)
			// .setPositiveButton("取消",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// }
			// })
			// .setNegativeButton("呼叫",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			//
			// if (!StringUtility.isEmpty(tel)) {
			// tel = tel.replaceAll("\\s+|\\-", "");
			// LogManager.i(
			// getClass().getSimpleName(),
			// "打电话:%s", tel);
			//
			// Intent intent = new Intent(
			// Intent.ACTION_DIAL, Uri
			// .parse("tel:" + tel));
			// try {
			// context.startActivity(intent);
			// } catch (Exception e) {
			// Toast.makeText(context, "拨号失败",
			// Toast.LENGTH_LONG).show();
			//
			// }
			// }
			// }
			// }).show();

		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Constans.Toast(context, "ff"+arg1);
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
