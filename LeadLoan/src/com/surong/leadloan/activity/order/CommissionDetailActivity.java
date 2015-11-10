package com.surong.leadloan.activity.order;

import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.entity.CommissionDetail;
import com.surong.leadloan.entity.OrderSituation;
import com.surong.leadloan.utils.Analyze;

public class CommissionDetailActivity extends CommonActivity {
	View view;
	Context context;
	private LinearLayout baseInfoLayout, orderLayout;
	private JSONObject order;
	Iterator<String> iterator;
	JSONObject generalInfo;
	String generalInfos;

	JSONObject followInfo;
	String followInfos;
	private LinearLayout orderSitLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.commission_detail_activity, null);
		addContentView(view);
		changeTitle("订单详情");
		order = CommissionManageActivity.order;
		initInfoView();
	}

	private void initInfoView() {
		baseInfoLayout = (LinearLayout) view.findViewById(R.id.base_info);
		orderLayout = (LinearLayout) view.findViewById(R.id.order_situation);
		orderSitLayout = (LinearLayout) view
				.findViewById(R.id.order_sit_layout);
		baseInfoLayout.removeAllViews();
		orderLayout.removeAllViews();
		TextView name, content;
		TextView time, status, note;
		LinearLayout layout;
		boolean check = true;
		// 基本信息
		try {

			generalInfos = order.getString("generalInfoArr");
			List<CommissionDetail> list = Analyze
					.analyzeCommissionDetail(generalInfos);

			for (int i = 0; i < list.size(); i++) {
				CommissionDetail data = list.get(i);
				View v = View.inflate(context, R.layout.about_us_item, null);
				layout = (LinearLayout) v.findViewById(R.id.layout);
				if (check == true) {
					layout.setBackgroundColor(Color.argb(255, 247, 251, 255));
				} else {
					layout.setBackgroundColor(Color.argb(255, 255, 255, 255));
				}
				name = (TextView) v.findViewById(R.id.item_key);
				content = (TextView) v.findViewById(R.id.item_value);
				name.setText(data.getName());
				content.setText(data.getValue());
				baseInfoLayout.addView(v);
				check = !check;
			}

			// while (iterator.hasNext()) {
			// View v = View.inflate(context, R.layout.about_us_item, null);
			// layout = (LinearLayout) v.findViewById(R.id.layout);
			// if (check == true) {
			// layout.setBackgroundColor(Color.argb(255, 247, 251, 255));
			// } else {
			// layout.setBackgroundColor(Color.argb(255, 255, 255, 255));
			// }
			// name = (TextView) v.findViewById(R.id.item_key);
			// content = (TextView) v.findViewById(R.id.item_value);
			// String key = iterator.next();
			// if (key != null) {
			// name.setText(key);
			// content.setText(generalInfo.getString(key));
			// }
			// baseInfoLayout.addView(v);
			// check = !check;
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// 跟单情况
		try {
			if (!order.isNull("followInfoArr")) {
				orderSitLayout.setVisibility(View.VISIBLE);
				followInfos = order.getString("followInfoArr");

				List<OrderSituation> list1 = Analyze
						.analyzeOrderSituation(followInfos);
				for (int i = 0; i < list1.size(); i++) {
					OrderSituation data = list1.get(i);
					View v = View.inflate(context,
							R.layout.order_situation_item, null);
					layout = (LinearLayout) v.findViewById(R.id.layout1);
					if (check == true) {
						layout.setBackgroundColor(Color
								.argb(255, 247, 251, 255));
					} else {
						layout.setBackgroundColor(Color
								.argb(255, 255, 255, 255));
					}
					time = (TextView) v.findViewById(R.id.time);
					status = (TextView) v.findViewById(R.id.status);
					note = (TextView) v.findViewById(R.id.note);
					time.setText(data.getFollowTime());
					status.setText(data.getStatus());
					note.setText(data.getFollowNote());
					orderLayout.addView(v);
					check = !check;
				}
			} else {
				orderSitLayout.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
