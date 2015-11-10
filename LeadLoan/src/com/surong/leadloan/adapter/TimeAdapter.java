package com.surong.leadloan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surong.leadload.api.data.TimeTable;
import com.surong.leadloan.R;
import com.surong.leadloan.R.color;

public class TimeAdapter extends BaseAdapter {
	private Context context;
	private List<TimeTable> timetable;
	color color = new color();

	public TimeAdapter(Context context, List<TimeTable> timetable2) {
		this.context = context;
		this.timetable = timetable2;
	}

	@Override
	public int getCount() {
		return timetable.size();
	}

	@Override
	public Object getItem(int arg0) {
		return timetable.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup parent) {
		// 缓存机制
		if (convertView == null) {
			// LayoutInflater用于加载一个 外部的xml布局文件，可以从这个布局中查找视图控件
			convertView = LayoutInflater.from(context).inflate(R.layout.time,
					null);
		}
		TextView time1 = (TextView) convertView.findViewById(R.id.time1);
		TextView jifen1 = (TextView) convertView.findViewById(R.id.jifen1);
		TextView beizhu1 = (TextView) convertView.findViewById(R.id.beizhu1);
		LinearLayout layout_list10 = (LinearLayout) convertView
				.findViewById(R.id.layout_list10);
		View line1 = convertView.findViewById(R.id.line1);
		View line2 = convertView.findViewById(R.id.line2);
		time1.setText(timetable.get(arg0).getOperateTime());
		if (timetable.get(arg0).getOperateType().trim().equals("+")) {
			jifen1.setTextColor(Color.argb(255, 255, 192, 0));
		}
		jifen1.setText(timetable.get(arg0).getOperateType()
				+ timetable.get(arg0).getAmount());
		beizhu1.setText(timetable.get(arg0).getreasonDesc());
		if (arg0 % 2 == 0) {
			layout_list10.setBackgroundColor(Color.argb(255, 192, 224, 255));
			time1.setBackgroundColor(Color.argb(255, 192, 224, 255));
			jifen1.setBackgroundColor(Color.argb(255, 192, 224, 255));
			beizhu1.setBackgroundColor(Color.argb(255, 192, 224, 255));
			line1.setBackgroundColor(Color.argb(255, 255, 255, 255));
			line2.setBackgroundColor(Color.argb(255, 255, 255, 255));
		}
		return convertView;
	}
}
