package com.surong.leadloan.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.utils.Utils;

public class BaseFragment extends Fragment {
	public Activity mActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.initData(getActivity());
	}
	
	public void setmActivity(Activity mActivity) {
		this.mActivity = mActivity;
	 
	}

//	/**
//	 * 初始化组件
//	 */
//	private void initView() {
//		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
//		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
//		mTabs = new Button[3];
//		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
//		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
//		mTabs[2] = (Button) findViewById(R.id.btn_setting);
//		// 把第一个tab设为选中状态
//		mTabs[0].setSelected(true);
//
//	}

//	/**
//	 * button点击事件
//	 * 
//	 * @param view
//	 */
//	public void onTabClicked(View view) {
//		int id = view.getId();
//        if (id == R.id.btn_conversation) {
//            index = 0;
//        } else if (id == R.id.btn_address_list) {
//            index = 1;
//        } else if (id == R.id.btn_setting) {
//            index = 2;
//        }
//		if (currentTabIndex != index) {
//			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//			trx.hide(fragments[currentTabIndex]);
//			if (!fragments[index].isAdded()) {
//				trx.add(R.id.fragment_container, fragments[index]);
//			}
//			trx.show(fragments[index]).commit();
//		}
//		mTabs[currentTabIndex].setSelected(false);
//		// 把当前tab设为选中状态
//		mTabs[index].setSelected(true);
//		currentTabIndex = index;
//	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		

	}
 
	@Override
	public void onResume() {
		super.onResume();
//		DbUtils dbUtils = DbUtils.create(getActivity());
		super.onResume();
		try {
			if(CommonActivity.db.findFirst(Personal.class)==null)
				getActivity().finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
