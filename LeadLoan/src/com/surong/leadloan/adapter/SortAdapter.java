package com.surong.leadloan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.entity.MobileSoftware;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<MobileSoftware> list = null;
	private Context mContext;

	public SortAdapter(Context mContext, List<MobileSoftware> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * ��ListView���ݷ����仯ʱ,���ô˷���������ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<MobileSoftware> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final MobileSoftware mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.contact_list_item, null);
			viewHolder.selector = (CheckBox) view
					.findViewById(R.id.item_selector);
			viewHolder.itemName = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// ����position��ȡ���������ĸ��char asciiֵ
		int section = getSectionForPosition(position);

		// �����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		viewHolder.itemName.setText(this.list.get(position).getPhoneName());

		viewHolder.selector.setChecked(mContent.isChecked());
		viewHolder.selector.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mContent.toggleCheck();
			}
		});
		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView itemName;
		CheckBox selector;
	}

	/**
	 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
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