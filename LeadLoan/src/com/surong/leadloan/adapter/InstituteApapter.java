package com.surong.leadloan.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.entity.Institu;

public class InstituteApapter extends BaseAdapter {
	private Context context;
	private List<Institu> list;

	public InstituteApapter(Context context, List<Institu> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.city_spinner_item,
					null);
			viewHolder.text_City = (TextView) convertView
					.findViewById(R.id.text_city);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text_City.setText(list.get(position).getInstituteName());
		return convertView;
	}

	class ViewHolder {
		TextView text_City;
	}
}
