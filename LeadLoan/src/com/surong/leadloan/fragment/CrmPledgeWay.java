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


public class CrmPledgeWay extends Fragment{
	private View view;
	private Map<String, String> map;
	private LinearLayout contentLayout;
	public CrmPledgeWay(Map<String, String> assuranceMap) {
		map = assuranceMap;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.crm_pledge_way2, container, false);
		contentLayout = (LinearLayout) view.findViewById(R.id.content);
		initData();
		return view;
	}
	
	private void initData() {
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		if(map.size()==0){
			TextView textView = new TextView(getActivity());
			textView.setText("无");
			textView.setPadding(20, 0, 0, 0);
			//textView.setGravity(Gravity.CENTER);
			textView.setTextSize(18);
			textView.setTextColor(getResources().getColor(R.color.black));
			
			//View view  = View.inflate(getActivity(), R.layout.item_white, null);
			//TextView name = (TextView) view.findViewById(R.id.name);
			//name.setText("无");
			contentLayout.addView(textView);
		}
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
}
