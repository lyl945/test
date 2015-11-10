package com.surong.leadloan.entity;
import java.util.ArrayList;
import java.util.List;

/*通讯录JavaBean*/
public class Category {

	private String mCategoryName;
	
	private List<Contact> mCategoryItem = new ArrayList<Contact>();

	public Category(String mCategroyName) {
		mCategoryName = mCategroyName;
	}
	
	public String getmCategoryName() {
		return mCategoryName;
	}

	public void addItem(Contact mobile) {
		mCategoryItem.add(mobile);
	}
	public void removeItem(int position){
		mCategoryItem.remove(position);
	}
	public Contact getItem(int pPosition){
		return mCategoryItem.get(pPosition - 1);
	}
	
	/**
	 *  ��ȡItem����
	 * 
	 * @param pPosition
	 * @return
	 */
	public String getItemName(int pPosition) {
		// Category���ڵ�һλ
		if (pPosition == 0) {
			return mCategoryName;
		} else {
			return mCategoryItem.get(pPosition - 1).getName();
		}
	}
	
	public String getItemPhone(int pPosition) {
		// Category���ڵ�һλ
		if (pPosition == 0) {
			return mCategoryName;
		} else {
			return mCategoryItem.get(pPosition - 1).getMobile();
		}
	}
	
	/**
	 * ��ǰ���Item����CategoryҲ��Ҫռ��һ��Item
	 * @return 
	 */
	public int getItemCount() {
		return mCategoryItem.size() + 1;
	}
	
}