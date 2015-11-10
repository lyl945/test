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

public class CrmCustomer extends Fragment{
	private View view;
	private Map<String, String> map;
	private LinearLayout contentLayout;
	private TextView title;
	public CrmCustomer(Map<String, String> customerIdentifierMap) {
		map= customerIdentifierMap;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.crm_customer2, container, false);
		contentLayout = (LinearLayout) view.findViewById(R.id.content);
//		title = (TextView) view.findViewById(R.id.title);
		initData();
		return view;
	}
	private void initData() {
		String titleString;
		titleString=map.get("qualification");
//		title.setText(titleString);
		map.remove("qualification");
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
		
		map.put("qualification",titleString);
		
	}
}
