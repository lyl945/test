package com.surong.leadloan.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.res.EASEConstants;
import com.pj.core.NotificationCenter;
import com.pj.core.NotificationCenter.NotificationListener;
import com.surong.leadload.api.data.AddFriendEntry;
import com.surong.leadload.api.data.FriendList;
import com.surong.leadload.api.data.Member;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.personal.MShopActivity;
import com.surong.leadloan.entity.Category;
import com.surong.leadloan.entity.Contact;
import com.surong.leadloan.fragment.ContactGroupFragment;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.imageutils.ImageFetcher;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;

public class CategoryAdapter extends BaseAdapter implements
		NotificationListener {

	private static final int TYPE_CATEGORY_ITEM = 0;
	private static final int TYPE_ITEM[] = { 1, 2, 3 };// 1-δ��ӣ�2-������
														// 3-�����

	private ArrayList<Category> mListData;
	private LayoutInflater mInflater;
	private TextView name;
	private String titleName;
	private Context context;
	private Drawable toAdd, toInvite;
	/* private PersonalHttpServiceBean http; */
	private String token;
	private Contact mo;
	private Member members = new Member();
	private Member memberss = new Member();
	// private List<Member> mMembers = new ArrayList<Member>();
	private ViewHolder viewHolder1;
	private MyHttpUtils myHttpUtils;
	private List<FriendList> friend;
	private ImageFetcher mImageFetcher;
	private Set<String> displayname = new HashSet<String>();

	public CategoryAdapter(Context context, ArrayList<Category> pData) {
		mListData = pData;
		this.context = context;
		mInflater = LayoutInflater.from(context);

		toAdd = context.getResources().getDrawable(R.drawable.botton_add);
		toInvite = context.getResources().getDrawable(R.drawable.please_button);

		token = SharedPreferencesHelp.getString(context, "token");
		myHttpUtils = MyHttpUtils.myInstance();
		mo = new Contact();
		friend = ContactGroupFragment.friends;
		NotificationCenter.getDefaultCenter().removeNotificationListener(this,
				null, EASEConstants.N_FRIEND_APPLY);
		NotificationCenter.getDefaultCenter().addNotificationListener(this,
				(Object) null, EASEConstants.N_FRIEND_APPLY);
	}

	@Override
	public int getCount() {
		int count = 0;

		if (null != mListData) {
			// ���з�����item���ܺ���ListVIew Item���ܸ���
			for (Category category : mListData) {
				count += category.getItemCount();
			}
		}

		return count;
	}

	@Override
	public Object getItem(int position) {

		// �쳣�������
		if (null == mListData || position < 0 || position > getCount()) {
			return null;
		}

		// ͬһ�����ڣ���һ��Ԫ�ص�����ֵ
		int categroyFirstIndex = 0;

		for (Category category : mListData) {
			int size = category.getItemCount();
			// �ڵ�ǰ�����е�����ֵ
			int categoryIndex = position - categroyFirstIndex;
			// item�ڵ�ǰ������
			if (categoryIndex < size) {
				return category.getItemName(categoryIndex);
			}

			// �����ƶ�����ǰ�����β������һ�������һ��Ԫ������
			categroyFirstIndex += size;
		}

		return null;
	}

	/*
	 * @param �ֶ�λ��
	 * 
	 * @return MoblieSoft����
	 */

	public Contact getItemMobile(int position) {

		// �쳣�������
		if (null == mListData || position < 0 || position > getCount()) {
			return null;
		}

		// ͬһ�����ڣ���һ��Ԫ�ص�����ֵ
		int categroyFirstIndex = 0;

		for (Category category : mListData) {
			int size = category.getItemCount();
			// �ڵ�ǰ�����е�����ֵ
			int categoryIndex = position - categroyFirstIndex;
			// item�ڵ�ǰ������
			if (categoryIndex < size) {
				return category.getItem(categoryIndex);
			}

			// �����ƶ�����ǰ�����β������һ�������һ��Ԫ������
			categroyFirstIndex += size;
		}

		return null;
	}

	@Override
	public int getItemViewType(int position) {
		// 返回数据类型
		if (null == mListData || position < 0 || position > getCount()) {
			return TYPE_ITEM[0];
		}
		int size = 0;
		int categroyFirstIndex = 0;
		for (int i = 0; i < mListData.size(); i++) {
			Category category = mListData.get(i);
			int flag = position - size;
			if (flag == 0) {
				return TYPE_CATEGORY_ITEM;
			}
			size += category.getItemCount();
			if (position > categroyFirstIndex && position < size) {
				titleName = (String) category.getmCategoryName();
				if (titleName.equals("未添加")) {
					return TYPE_ITEM[0];
				}
				if (titleName.equals("发邀请")) {
					return TYPE_ITEM[1];
				}
				if (titleName.equals("已添加")) {
					return TYPE_ITEM[2];
				}

			}

			categroyFirstIndex = size;
		}

		return TYPE_ITEM[0];
	}

	@Override
	public int getViewTypeCount() {
		return mListData.size() + 1;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		// System.out.println(position);
		int itemViewType = getItemViewType(position);
		switch (itemViewType) {
		case TYPE_CATEGORY_ITEM:
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.address_book_header,
						null);
			}
			TextView textView = (TextView) convertView
					.findViewById(R.id.header);
			String itemValue = (String) getItem(position);
			textView.setText(itemValue);
			break;

		case 1:
			mo = getItemMobile(position);

			if (null == convertView) {
				System.out.println("create viewHolder1");
				convertView = mInflater.inflate(R.layout.address_not_add_item,
						null);
				viewHolder1 = new ViewHolder();
				viewHolder1.content = (TextView) convertView
						.findViewById(R.id.name);
				viewHolder1.head = (ImageView) convertView
						.findViewById(R.id.buddy_listview_child_avatar);
				viewHolder1.institue = (TextView) convertView
						.findViewById(R.id.buddy_listview_child_trends);
				viewHolder1.btn = (Button) convertView.findViewById(R.id.state);
				viewHolder1.relativeLayout_mshop = (RelativeLayout) convertView.findViewById(R.id.relativeLayout_mshop);
				convertView.setTag(viewHolder1);
			} else {
				System.out.println("viewHolder1 is not null");
				viewHolder1 = (ViewHolder) convertView.getTag();
			}

			viewHolder1.btn.setTag(mo.getMobile());
			// �����
			members.setRealName(mo.getName());
			members.setDisplayName(mo.getName());
			members.setInstituName(mo.getInstitue());
			members.setHeadImgPath(mo.getHeadImgPath());
			viewHolder1.content.setText(mo.getName());
			if (displayname.contains(members.getDisplayName())) {
				viewHolder1.btn.setText(context
						.getString(R.string.waiting_accepted));
			} else {
			}
			viewHolder1.institue.setText(mo.getInstitue());
			mImageFetcher = ImageFetcher.Instance(context, 0);
			mImageFetcher.addTask(viewHolder1.head, mo.getHeadImgPath(), 0);
			viewHolder1.head.setTag(mo.getHeadImgPath());
			viewHolder1.relativeLayout_mshop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i1 = new Intent(context, MShopActivity.class);
					i1.putExtra("memberId", getItemMobile(position).getId());
					i1.putExtra("mobile", getItemMobile(position).getMobile());
					i1.putExtra("displayName", getItemMobile(position).getName());
					context.startActivity(i1);
					
				}
				
				
			});
				viewHolder1.btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					memberss.setRealName(getItemMobile(position).getName());
					memberss.setMobile(getItemMobile(position).getMobile());
					memberss.setDisplayName(getItemMobile(position).getName());
					memberss.setInstituName(getItemMobile(position)
							.getInstitue());
					memberss.setHeadImgPath(getItemMobile(position)
							.getHeadImgPath());
					memberss.setId(getItemMobile(position).getId());
					// Contact mobile = new Contact();
					// mobile = getItemMobile(3);
					AddFriendEntry addFriendEntry = new AddFriendEntry();
					addFriendEntry.member = memberss;
					addFriendEntry.type = AddFriendEntry.TYPE_ID;
					addFriendEntry.extraObject = memberss;

					Intent intent = new Intent(EASEConstants.ACTION_ADD_FRIEND);
					intent.putExtra(EASEConstants.KEY_ADD_FRIEND_ENTRY,
							addFriendEntry);
					context.startActivity(intent);
					// CustomProgressDialog.startProgressDialog(context);
					// String phone = (String) ((Button)v).getTag();
					// RequestParams params = new RequestParams();
					// params.addQueryStringParameter("token", token);
					// params.addQueryStringParameter("phone", phone);
					// myHttpUtils.getHttpJsonString(params,
					// Constans.addFriendUrl, handler,context, 0,
					// Constans.thod_Get_0);
					// System.out.println(mo.getMobile());
					// ((Button)v).setBackground(null);
					// ((Button)v).setText("�ȴ���֤");
					// Constans.Toast(context, "添加好友");
					// mo.setState(4);
				}
			});

			// members.setHeadImgPath(R.drawable.head);
			/*
			 * if(mo.getState()==4){
			 * viewHolder1.btn.setBackgroundDrawable(null);
			 * viewHolder1.btn.setText("�ȴ���֤"); }else{
			 * viewHolder1.btn.setBackgroundDrawable(toAdd);
			 * viewHolder1.btn.setText(""); }
			 */
			notifyDataSetChanged();
			break;
		case 2:// 发邀请
			mo = getItemMobile(position);
			ViewHolder viewHolder2 = null;
			if (null == convertView) {
				System.out.println("create viewHolder2");
				convertView = mInflater.inflate(
						R.layout.address_to_invite_item, null);
				viewHolder2 = new ViewHolder();
				viewHolder2.content = (TextView) convertView
						.findViewById(R.id.name);
				viewHolder2.btn = (Button) convertView.findViewById(R.id.state);
				viewHolder2.btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String phone = (String) ((Button) v).getTag();
						Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
						sendIntent.setData(Uri.parse("smsto:" + phone));
						String string = context.getResources().getString(
								R.string.message);

						sendIntent.putExtra("sms_body", string);
						sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(sendIntent);
					}
				});
				convertView.setTag(viewHolder2);
			} else {
				System.out.println("viewHolder2 is not null");
				viewHolder2 = (ViewHolder) convertView.getTag();
			}
			viewHolder2.btn.setTag(mo.getMobile());
			// �����
			viewHolder2.content.setText((String) getItem(position));
			viewHolder2.btn.setBackgroundDrawable(toInvite);
			break;
		case 3:
			ViewHolder viewHolder3 = null;
			if (null == convertView) {
				System.out.println("create viewHolder3");
				convertView = mInflater.inflate(R.layout.address_added_item,
						null);
				viewHolder3 = new ViewHolder();
				viewHolder3.content = (TextView) convertView
						.findViewById(R.id.name);
				viewHolder3.btn = (Button) convertView.findViewById(R.id.state);
				viewHolder3.head = (ImageView) convertView
						.findViewById(R.id.buddy_listview_child_avatar);
				viewHolder3.institue = (TextView) convertView
						.findViewById(R.id.buddy_listview_child_trends);
				convertView.setTag(viewHolder3);
			} else {
				System.out.println("viewHolder3 is not null");
				viewHolder3 = (ViewHolder) convertView.getTag();
			}
			for (int i = 0; i < friend.size(); i++) {
				for (int j = 0; j < friend.get(i).getFriendList().size(); j++) {
					if (friend.get(i).getFriendList().get(j).getMobile().trim()
							.equals(getItemMobile(position).getMobile())) {
						viewHolder3.content.setText((String) getItem(position));
						viewHolder3.institue.setText(friend.get(i)
								.getFriendList().get(j).getInstituShortName());
						mImageFetcher.addTask(viewHolder3.head, friend.get(i)
								.getFriendList().get(j).getHeadImgPath(), 0);
						viewHolder3.head.setTag(friend.get(i).getFriendList()
								.get(j).getHeadImgPath());
						viewHolder3.btn.setBackgroundDrawable(null);
						viewHolder3.btn.setText("已添加");
					}
				}
			}

			break;
		}
		// notifyDataSetChanged();
		return convertView;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return getItemViewType(position) != TYPE_CATEGORY_ITEM;
		// return false;
	}

	private class ViewHolder {
		ImageView head;
		TextView content;
		TextView institue;
		Button btn;
		RelativeLayout relativeLayout_mshop;
	}

	// public void onReceivedNotification(Object arg0, int arg1, Object arg2) {
	// if (arg1 == EASEConstants.N_FRIEND_APPLY) {
	// if (arg2 != null) {
	// AddFriendEntry entry = (AddFriendEntry) arg2;
	// if (entry.success) {// 成功添加
	// displaynames = entry.member.getDisplayName();
	// if (!TextUtils.isEmpty(displaynames)) {
	// viewHolder1.btn.setText("好了");
	//
	// }
	// }
	// }
	// }
	// }

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			if (msg.what == 0) {

				// provinceId":"440000","instituName":"深圳市亚联财小额信贷有限公司","cityId":"440300","cityName":"深圳市","provinceName":"广东省","instituId":"f1e27e0e441ee18001441fa027760014","typeName":"小贷公司","id":"f1eadfb3478bdc2f01479f8a36b90048","realName":"邓江","shortName":"亚联财","headImgPath":"http:\/\/f.surong100.com\/upload\/personal\/HeadImgPathf1eadfb3478bdc2f01479f8a36b90048\/headImg.jpg","displayName":"邓经理","typeId":"02","mobile":"18588276027","memberLevel":"01"},
				// member.setp
				AddFriendEntry addFriendEntry = new AddFriendEntry();
				addFriendEntry.member = memberss;
				addFriendEntry.type = AddFriendEntry.TYPE_ID;
				addFriendEntry.extraObject = memberss;
				Intent intent = new Intent(EASEConstants.ACTION_ADD_FRIEND);
				intent.putExtra(EASEConstants.KEY_ADD_FRIEND_ENTRY,
						addFriendEntry);
				// Context.startActivity(intent);
				// Intent intent = new Intent(context,AddFriendActivity.class);
				context.startActivity(intent);
			}
		};
	};

	@Override
	public void onReceivedNotification(Object arg0, int arg1, Object arg2) {
		if (arg1 == EASEConstants.N_FRIEND_APPLY) {
			if (arg2 != null) {
				AddFriendEntry entry = (AddFriendEntry) arg2;
				if (entry.success) {// 成功添加
					displayname.add(entry.member.getDisplayName());
					notifyDataSetChanged();
				}
			}
		}
	}

}