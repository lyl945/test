package com.surong.leadloan.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.surong.leadloan.R;

public class ResultAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private String mText;

	public ResultAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public ResultAdapter setText(String text) {
		mText = text;
		return this;
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.buddy_listview_child_item, null);
		TextView textView = (TextView) view.findViewById(R.id.operation_name);
		if (!TextUtils.isEmpty(mText)) {
			textView.setText(mText);
		}
		return view;
	}

}
