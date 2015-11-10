package com.surong.leadloan.adapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.entity.Order;
import com.surong.leadloan.utils.MyApplication;

@SuppressLint("WrongViewCast")
public class CrmApdapter extends BaseAdapter implements SectionIndexer {

	private Context context;
	private List<Order> mList;
	private Map<String, String> map;
	private Map<String, String> newMap;
	Set<String> idSet = new HashSet<String>();

	@SuppressLint("NewApi")
	public CrmApdapter(Context context, List<Order> mlist) {
		this.context = context;
		mList = mlist;
		map = new HashMap<String, String>();
		newMap = new HashMap<String, String>();
		map = MyApplication.dictionaryMap2.get("MAP_ORDER_STATUS");
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			newMap.put(entry.getValue(), entry.getKey());

		}
		SharedPreferences sharedPre = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		if (sharedPre != null) {
			idSet = sharedPre.getStringSet("read", idSet);
		}

	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Order entity = mList.get(position);
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.crm_item, null);
			viewHolder.newImage = (ImageView) convertView
					.findViewById(R.id.new_tip_image);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.from = (TextView) convertView.findViewById(R.id.from);
			viewHolder.date = (TextView) convertView.findViewById(R.id.date);
			viewHolder.money = (TextView) convertView.findViewById(R.id.money);
			viewHolder.mainPart = (TextView) convertView
					.findViewById(R.id.main_part);
			viewHolder.way = (TextView) convertView.findViewById(R.id.way);
			viewHolder.dateLimit = (TextView) convertView
					.findViewById(R.id.date_limit);
			viewHolder.state = (TextView) convertView.findViewById(R.id.state);
			viewHolder.button1 = (ImageView) convertView
					.findViewById(R.id.button1);
			viewHolder.button2 = (ImageView) convertView
					.findViewById(R.id.button2);
			viewHolder.button3 = (ImageView) convertView
					.findViewById(R.id.button3);
			viewHolder.button4 = (ImageView) convertView
					.findViewById(R.id.button4);
			viewHolder.button5 = (ImageView) convertView
					.findViewById(R.id.button5);
			viewHolder.make_call = (ImageView) convertView
					.findViewById(R.id.make_call);
			viewHolder.linear_make_call = (LinearLayout) convertView
					.findViewById(R.id.linear_make_call);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.linear_make_call.setTag(entity.getCustMobile());
		viewHolder.linear_make_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("tel:" + v.getTag());
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				context.startActivity(intent);

			}
		});

		if (!idSet.contains(entity.getId())) {
			viewHolder.name.setTextColor(Color.rgb(227, 57, 68));
			viewHolder.newImage.setVisibility(View.VISIBLE);
		} else {
			viewHolder.name.setTextColor(Color.rgb(51, 51, 51));
			viewHolder.newImage.setVisibility(View.GONE);
		}
		// 设置数据
		viewHolder.name.setText(entity.getCustName());
		String from = "";// 订单来源需根据两个字段确定
		if (null == entity.getOrderId()) {
			from = "推广订单";
		} else {
			if ("01".equals(entity.getServiceOrderCode())) {
				from = "100领单";
			} else {
				from = "100转单";
			}
		}
		viewHolder.from.setText(from);
		viewHolder.date.setText(entity.getApplyTime());
		// 设置贷款金额
		if (null == entity.getApplyAmount()
				|| "".equals(entity.getApplyAmount())) {
			viewHolder.money.setText("无");
		} else {
			viewHolder.money.setText(entity.getApplyAmount() + "万元");
		}
		// 设置贷款时间
		if (null == entity.getApplyPeriod()
				|| "".equals(entity.getApplyPeriod())) {
			viewHolder.dateLimit.setText("无");
		} else {
			viewHolder.dateLimit.setText(entity.getApplyPeriod() + "个月");
		}
		// 设置贷款主体
		if (entity.getCustType() == null) {
			viewHolder.mainPart.setText("无");
		} else if (entity.getCustType().equals("02")) {
			viewHolder.mainPart.setText("个人");
		} else {
			viewHolder.mainPart.setText("企业");
		}

		if (null == entity.getState() || "".equals(entity.getState())) {
			viewHolder.state.setText("无");
		} else {

			viewHolder.state.setText(newMap.get(entity.getState()));
		}

		if (null == entity.getProdName() || "".equals(entity.getProdName())) {
			viewHolder.way.setText("无");
		} else {
			viewHolder.way.setText(entity.getProdName());
		}
		return convertView;
	}

	class ViewHolder {
		TextView name, from, date, money, dateLimit, state, mainPart, way;
		ImageView button1, button2, button3, button4, button5, make_call;
		LinearLayout linear_make_call;
		ImageView newImage;

	}

	/**
	 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		return mList.get(position).getSortLetters().charAt(0);
	}

	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

}
