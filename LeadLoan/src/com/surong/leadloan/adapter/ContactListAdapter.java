package com.surong.leadloan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.surong.leadloan.R;

public class ContactListAdapter extends BaseExpandableListAdapter {
	private String[] group;
	private String[][] buddy;
	private Context context;
	LayoutInflater inflater;

	public ContactListAdapter(Context context, String[] group, String[][] buddy) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.group = group;
		this.buddy = buddy;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return buddy[groupPosition][childPosition];
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean arg2, View convertView, ViewGroup arg4) {
		convertView = inflater
				.inflate(R.layout.buddy_listview_child_item, null);
		TextView nickTextView = (TextView) convertView
				.findViewById(R.id.buddy_listview_child_nick);
		nickTextView.setText(getChild(groupPosition, childPosition).toString());
		TextView textView = (TextView) convertView
				.findViewById(R.id.operation_name);
		switch (groupPosition) {
		case 0:
			textView.setText(context.getString(R.string.add_friend));
			break;
		case 1:
			textView.setText(context.getString(R.string.send_invite));
			break;
		case 2:
			textView.setText(context.getString(R.string.added));
			break;
		default:
			break;
		}
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return buddy[groupPosition].length;
	}

	public Object getGroup(int groupPosition) {
		return group[groupPosition];
	}

	public int getGroupCount() {
		return group.length;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup arg3) {
		convertView = inflater
				.inflate(R.layout.buddy_listview_group_item, null);
		TextView groupNameTextView = (TextView) convertView
				.findViewById(R.id.buddy_listview_group_name);
		groupNameTextView.setText(getGroup(groupPosition).toString());
		ImageView image = (ImageView) convertView
				.findViewById(R.id.buddy_listview_image);
		// image.setImageResource(R.drawable.group_unfold_arrow);
		// ��չ������ͼƬ
		if (!isExpanded) {
			// image.setImageResource(R.drawable.group_fold_arrow);
		}
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	// ��ѡ���Ƿ����ѡ��
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
