package com.surong.leadloan.activity.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.adapter.SortAdapter;
import com.surong.leadloan.entity.MobileSoftware;
import com.surong.leadloan.ui.ClearEditText;
import com.surong.leadloan.ui.SideBar;
import com.surong.leadloan.ui.SideBar.OnTouchingLetterChangedListener;
import com.surong.leadloan.utils.ContactDataSource;
import com.surong.leadloan.activity.crm.StickyListHeadersListView;

public class ContactActivity extends CommonActivity implements OnClickListener {

	private View view;
	private Context context;
	private List<MobileSoftware> mList;
	private CheckBox allSelector;
	private SideBar sideBar;
	private SortAdapter2 adapter;

	private StickyListHeadersListView sortListView;
	/**
	 * 显示字母的TextView
	 */
	private ClearEditText mClearEditText;
	private List<MobileSoftware> phoneList = new ArrayList<MobileSoftware>();
	private TextView overlay, textView1;
	private OverlayThread overlayThread = new OverlayThread();

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.contact, null);
		addContentView(view);
		changeTitle("通讯录");
		initView();
	}

	private void initView() {

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidebar);

		allSelector = (CheckBox) findViewById(R.id.all_selector);
		overlay = (TextView) findViewById(R.id.tvLetter);
		textView1 = (TextView) findViewById(R.id.textView1);
		allSelector.setOnClickListener(this);
		textView1.setOnClickListener(this);
		mList = ContactDataSource.getMobileContacts(this);
		Iterator<MobileSoftware> itr = mList.iterator();

		while (itr.hasNext()) {
			MobileSoftware mo = (MobileSoftware) itr.next();
			MobileSoftware contact = new MobileSoftware();
			contact.setPhoneName(mo.getPhoneName());
			contact.setPhoneNum(mo.getPhoneNum());
			phoneList.add(contact);
		}

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (StickyListHeadersListView) findViewById(R.id.name_list);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(
						getApplication(),
						((MobileSoftware) adapter.getItem(position))
								.getPhoneName(), Toast.LENGTH_SHORT).show();
			}
		});

		mList = filledData(phoneList);

		// 根据a-z进行排序源数据
		Collections.sort(mList, pinyinComparator);
		adapter = new SortAdapter2(this, mList);
		sortListView.setAdapter(adapter);

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		sideBar.setTextView(overlay);
	}

	/**
	 * 为ListView填充数据
	 *
	 * @param date
	 * @return
	 */
	private List<MobileSoftware> filledData(List<MobileSoftware> date) {
		List<MobileSoftware> mSortList = new ArrayList<MobileSoftware>();

		for (int i = 0; i < date.size(); i++) {
			MobileSoftware mobileSoftware = new MobileSoftware();
			mobileSoftware.setPhoneName(date.get(i).getPhoneName());
			mobileSoftware.setPhoneNum(date.get(i).getPhoneNum());
			// 汉字转换成拼音

			String pinyin = getPingYin(date.get(i).getPhoneName());
			// String pinyin = characterParser.getSelling(date.get(i));
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				mobileSoftware.setSortLetters(pinyin.toUpperCase());
			} else {
				mobileSoftware.setSortLetters("#");
			}

			mSortList.add(mobileSoftware);
		}
		return mSortList;

	}


	private class OverlayThread implements Runnable {

		public void run() {
			overlay.setVisibility(View.GONE);
		}

	}

	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 *
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		if (TextUtils.isEmpty(inputString)) {
			return "";
		}
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		String output = "";

		try {
			for (int i = 0; i < input.length; i++) {
				if (java.lang.Character.toString(input[i]).matches(
						"[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							input[i], format);
					if (temp == null || TextUtils.isEmpty(temp[0])) {
						continue;
					}
					output += temp[0].replaceFirst(temp[0].substring(0, 1),
							temp[0].substring(0, 1).toUpperCase());
				} else
					output += java.lang.Character.toString(input[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 *
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<MobileSoftware> filterDateList = new ArrayList<MobileSoftware>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = mList;
		} else {
			filterDateList.clear();
			for (MobileSoftware mobileSoftware : mList) {
				String name = mobileSoftware.getPhoneName();
				if (name.toUpperCase().indexOf(
						filterStr.toString().toUpperCase()) != -1
						|| getPingYin(name).toUpperCase().startsWith(
						filterStr.toString().toUpperCase())) {
					filterDateList.add(mobileSoftware);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	class PinyinComparator implements Comparator<MobileSoftware> {

		public int compare(MobileSoftware o1, MobileSoftware o2) {
			if (o2.getSortLetters().equals("#")) {
				return -1;
			} else if (o1.getSortLetters().equals("#")) {
				return 1;
			} else {
				return o1.getSortLetters().compareTo(o2.getSortLetters());
			}
		}
	}

	public class SortAdapter2 extends BaseAdapter implements SectionIndexer,
			StickyListHeadersAdapter {
		private List<MobileSoftware> list = null;
		private Context mContext;
		private LayoutInflater mInflater;

		public SortAdapter2(Context mContext, List<MobileSoftware> list) {
			this.mContext = mContext;
			this.list = list;
			mInflater = LayoutInflater.from(context);
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
				view = mInflater.inflate(
						R.layout.contact_list_item, null);
				viewHolder.selector = (CheckBox) view
						.findViewById(R.id.item_selector);
				viewHolder.itemName = (TextView) view.findViewById(R.id.title);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			// ����position��ȡ���������ĸ��char asciiֵ
			int section = getSectionForPosition(position);

			// �����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���

			viewHolder.itemName.setText(this.list.get(position).getPhoneName());

			viewHolder.selector.setChecked(mContent.isChecked());
			viewHolder.selector.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mContent.toggleCheck();
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).isChecked()) {
							if (i == list.size() - 1) {
								allSelector.setChecked(true);
							}
						} else {
							allSelector.setChecked(false);
							break;
						}
					}
				}
			});
			return view;

		}

		class ViewHolder {
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

		@Override
		public View getHeaderView(int position, View convertView,
								  ViewGroup parent) {
			HeaderViewHolder holder;

			if (convertView == null) {
				holder = new HeaderViewHolder();
				convertView = mInflater.inflate(
						R.layout.header, parent, false);
				holder.text = (TextView) convertView.findViewById(R.id.text1);
				convertView.setTag(holder);
			} else {
				holder = (HeaderViewHolder) convertView.getTag();
			}

			// CharSequence headerChar = mCountries[position].subSequence(0, 1);
			holder.text.setText(list.get(position).getSortLetters()
					.substring(0, 1));

			return convertView;
		}

		@Override
		public long getHeaderId(int position) {
			return list.get(position).getSortLetters().substring(0, 1)
					.charAt(0);
		}
	}

	class HeaderViewHolder {
		TextView text;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.all_selector:
				for (MobileSoftware contact : mList) {
					contact.setChecked(allSelector.isChecked());
				}
				adapter.notifyDataSetChanged();
				break;
			case R.id.textView1:
				String aString = "";
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).isChecked()) {
						aString += mList.get(i).getPhoneNum() + ",";
					}
				}
				Intent intent = new Intent(context, ContactActivity2.class);
				intent.putExtra("tellNmu", aString);
				startActivity(intent);

			default:
				break;
		}
	}

}
