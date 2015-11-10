package com.surong.leadloan.fragment;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.entity.OrderDetail;

public class CrmBaseainformation extends Fragment {
	private View view;
	private OrderDetail orderDetail;
	private Map<String, String> map;
	private LinearLayout contentLayout;
	private TextView text_main, text_money, text_date, text_loan_way;

	public CrmBaseainformation(Map<String, String> map) {
		this.map = map;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater
				.inflate(R.layout.crm_base_information2, container, false);
		contentLayout = (LinearLayout) view.findViewById(R.id.content);
		initData();
		return view;
	}

	private void initData() {
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		for(int i=0;i<map.size();i++){
			Map.Entry  mapentry = (Map.Entry) iterator.next();
			View itemView;
			if(i%2==0){
				itemView = View.inflate(getActivity(), R.layout.item_blue, null);
			}else {
				itemView = View.inflate(getActivity(), R.layout.item_white, null);
			}
			
			TextView name = (TextView) itemView.findViewById(R.id.name);
			TextView context = (TextView) itemView.findViewById(R.id.context);
			name.setText(mapentry.getKey().toString());
			context.setText(mapentry.getValue().toString());
			contentLayout.addView(itemView);
		}
		
	}
	
	
	/*private void initData() {
		text_main = (TextView) view.findViewById(R.id.text_main);
		text_money = (TextView) view.findViewById(R.id.text_money);
		text_date = (TextView) view.findViewById(R.id.text_date);
		text_loan_way = (TextView) view.findViewById(R.id.text_loan_way);
		if(isNull(orderDetail.getCustType())){
			text_main.setText("无");
		}else{
			if (orderDetail.getCustType().equals("01")) {
				text_main.setText("公司");
			} else {
				text_main.setText("个人");
			}
		}
			
		
		if(isNull(orderDetail.getApplyAmount())){
			text_money.setText("无");
		}else {
			text_money.setText(orderDetail.getApplyAmount()+"万元");
		}
       if(isNull(orderDetail.getApplyPeriod())){
    	   text_date.setText("无");
       }else {
    	   text_date.setText(orderDetail.getApplyPeriod()+"个月");
	}
       
       if(null==orderDetail.getProdType()||"".equals(orderDetail.getProdType()))
       {
    	   text_loan_way.setText("无"); 
       }else {
    	   text_loan_way.setText(orderDetail.getProdType());
	}
      
	}
*/
	private boolean isNull(String applyAmount) {
		if(null==applyAmount||"".equals(applyAmount)){
			return true;
		}
		return false;
	}
}
