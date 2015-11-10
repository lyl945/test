package com.surong.leadloan.activity;

import android.os.Bundle;

import com.pj.core.viewholders.NavigationViewHolder;
import com.surong.leadload.holders.AddFriendViewHolder;

public class AddFriendActivity extends LDCoreActivity {
	
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		NavigationViewHolder navigationViewHolder = new NavigationViewHolder(new AddFriendViewHolder(this));
		setContentView(navigationViewHolder);
	}
}
