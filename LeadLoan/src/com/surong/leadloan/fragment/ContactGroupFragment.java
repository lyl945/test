package com.surong.leadloan.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.applib.model.EaseUser;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.res.EASEConstants;
import com.easemob.chatuidemo.utils.SharedPreferencesHelp;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.pj.core.NotificationCenter;
import com.pj.core.datamodel.DataWrapper;
import com.pj.core.http.HttpImage;
import com.pj.core.http.HttpRequest;
import com.pj.core.http.HttpRequest.HttpRequestListener;
import com.pj.core.http.HttpResult;
import com.pj.core.utilities.ArrayUtility;
import com.pj.core.utilities.DimensionUtility;
import com.pj.core.utilities.StringUtility;
import com.pj.core.utilities.TextUtility;
import com.surong.leadload.api.BaiduMapTool;
import com.surong.leadload.api.data.Friend;
import com.surong.leadload.api.data.FriendList;
import com.surong.leadload.database.EASEDatabaseUserInfo;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.contact_group.ContactListActivity;
import com.surong.leadloan.activity.contact_group.FriendsActivity;
import com.surong.leadloan.activity.contact_group.NearPeerActivity;
import com.surong.leadloan.activity.contact_group.PeerActivity;
import com.surong.leadloan.activity.contact_group.PossibleKnowActivity;
import com.surong.leadloan.activity.personal.ApplyCertification;
import com.surong.leadloan.adapter.BuddyAdapter;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.imageutils.ImageFetcher;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;

public class ContactGroupFragment extends BaseFragment implements
		OnClickListener, OnItemLongClickListener, HttpRequestListener {
	private View view;
	private InputMethodManager imm;
	private EditText searchView;
	private ExpandableListView expandablelistview;
	private View head;
	private String url;
	private int friendNum = 1;
	private int showFriendNum[];
	private int PerNum;
	private MyHttpUtils myHttpUtils;
	private RelativeLayout relativeLayout[];
	// 假数据
	private String[] group;
	private String stateString;
	private TextView id_certify_situation;
	private TextView institute_certify_situation;
	private ImageView auth_head;

	private List<List<Friend>> buddy;
	private BuddyAdapter mAdapter;
	// private List<MobileSoftware> mList;

	// sectionAuth 认证情况
	// sectionUploadContacts 上传通讯录
	// sectionContactFriends 通讯录好友
	// sectionXdjl 认识信贷经理

	private View sectionAuth, sectionContactFriends, sectionUploadContacts,
			sectionXdjl;

	private boolean shouldLoadContactFriends;
	private boolean shouldLoadXdjl;
	private Button gotorenzheng;
	// private DbUtils dbUtils;
	private Context context;
	private Personal personal;
	private String displayName;
	private String instituName;
	private String createDate;
	private String point;
	private String amount;
	private String headImgPath;
	private ImageFetcher mImageFetcher;
	// 认证
	private LinearLayout authentication;
	private TextView back, title, right;
	public static List<FriendList> friends;
	private int num[];
	private int start_index, end_index;
	// 判断是不是第一次进Fragment
	private boolean flage;

	public void setmActivity(Activity mActivity) {
		super.setmActivity(mActivity);
		url = "http://" + Constans.HTTP_URL;
		group = new String[] {};
		buddy = new ArrayList<List<Friend>>();

		mAdapter = new BuddyAdapter(mActivity);
		mAdapter.setGroup(group);
		mAdapter.setData(buddy);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();
		myHttpUtils.instance();
		BaiduMapTool.initLocationPosition(getActivity());
		view = inflater.inflate(R.layout.contact_group, container, false);
		head = view.findViewById(R.id.buddy_head);

		sectionAuth = head.findViewById(R.id.section_auth);
		sectionContactFriends = head.findViewById(R.id.section_contact_friends);
		sectionUploadContacts = head.findViewById(R.id.section_upload_contact);
		gotorenzheng = (Button) head.findViewById(R.id.gotorenzheng);
		id_certify_situation = (TextView) head
				.findViewById(R.id.id_certify_situation);
		institute_certify_situation = (TextView) head
				.findViewById(R.id.institute_certify_situation);
		authentication = (LinearLayout) head.findViewById(R.id.authentication);
		sectionXdjl = head.findViewById(R.id.section_xdjl);
		sectionAuth.setVisibility(view.GONE);
		sectionXdjl.setVisibility(view.GONE);
		sectionContactFriends.setVisibility(view.GONE);
		back = (TextView) view.findViewById(R.id.top_textView_left);
		title = (TextView) view.findViewById(R.id.top_textview_title);
		right = (TextView) view.findViewById(R.id.top_textView_right);
		title.setText("人脉圈");

		findView();
		initData();
		initAction();
		// imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		hideSoftInput();

		// loadFriends();
		if (!flage) {
			getFriendList();
			flage = !flage;
		}

		resetDefaultSections(true);

		// // 从通讯录获取联系人信息
		// mList = ContactDataSource.getMobileContacts(mActivity);
		// // 将号码单独取出来
		// List<Contact> phoneList = new ArrayList<Contact>();
		// Iterator<MobileSoftware> itr = mList.iterator();
		// while (itr.hasNext()) {
		// MobileSoftware mo = (MobileSoftware) itr.next();
		// if (checkPhone(mo.getPhoneNum())) {
		// Contact contact = new Contact();
		// contact.setName(mo.getPhoneName());
		// contact.setMobile(mo.getPhoneNum());
		// phoneList.add(contact);
		// }
		//
		// }

		return view;
	}

	private void resetDefaultSections(boolean hasFriends) {
		EaseUser user = (EaseUser) LDApplication.getInstance().getSessionData(
				EASEConstants.CURRENT_USER);
		if (user == null) {
			getActivity().finish();
		}

		// if (user.getAuthStatus() != 02) {// 2才是认证通过，其他状态全部都要显示
		// // 未认证,显示认证
		// sectionAuth.setVisibility(View.VISIBLE);
		//
		// // head.findViewById(R.id.auth_action).setOnClickListener(this);
		// // HttpImage.getInstance().setImage(user.getHeadImgPath(), headImg,
		// // R.drawable.default_avatar, DimensionUtility.dp2px(52) * 0.5f);
		//
		// } else {
		// sectionAuth.setVisibility(View.GONE);
		// }

		if (!hasFriends) {
			if (user.isHasUploadContacts()) {
				// 隐藏上传通讯录，显示通讯录好友推荐
				sectionUploadContacts.setVisibility(View.GONE);
				// sectionContactFriends.setVisibility(View.VISIBLE);
				// shouldLoadContactFriends = true;
			} else {
				sectionUploadContacts.setVisibility(View.VISIBLE);
				// sectionContactFriends.setVisibility(View.VISIBLE);
				// shouldLoadContactFriends = true;
				// 隐藏通讯录好友推荐，显示上传通讯录
				// sectionUploadContacts.setVisibility(View.VISIBLE);
				// sectionContactFriends.setVisibility(View.GONE);
				// shouldLoadContactFriends = false;
			}

		} else {
			// shouldLoadContactFriends = true;
			sectionUploadContacts.setVisibility(View.GONE);
			// sectionContactFriends.setVisibility(View.VISIBLE);
		}

		sectionContactFriends.findViewById(R.id.section_contact_friend_handler)
				.setOnClickListener(this);
		sectionXdjl.findViewById(R.id.section_xdjl_handler).setOnClickListener(
				this);
		sectionUploadContacts.setOnClickListener(this);
	}

	private void loadContactFriends() {
		HttpRequest request = new HttpRequest(
				url + "/surong/api/contacts/home", 0);

		request.setResponseDataFormat(HttpRequest.RESPONSE_JSON);
		request.setExpectedDataFormat(HttpRequest.EXPECTED_DATAWRAPPER);

		request.addParameter("token", LDApplication.getInstance()
				.getSessionData(EASEConstants.TOKEN));

		request.setHttpRequestListener(new HttpListenerAdapter() {
			@Override
			public void onHttpResponse(HttpRequest arg0, HttpResult arg1) {
				super.onHttpResponse(arg0, arg1);
				// 处理
				DataWrapper resp = arg1.getResponseData();
				if (resp == null) {
					return;
				}
				int err = resp.getInt("code");
				if (err == 0) {
					List<DataWrapper> friendList = (List<DataWrapper>) resp
							.getObject("notFriendList");
					handleContactFriends(friendList,
							resp.getInt("notFriendCount"));
				}
			}
		});

		request.startAsynchronousRequest();
	}

	private void handleContactFriends(List<DataWrapper> friendList, int all) {
//		if (friendList == null || friendList.size() <= 1) {
//			sectionContactFriends.setVisibility(View.VISIBLE);
//			return;
//		}else {
//			sectionContactFriends.setVisibility(View.GONE);
//		}
//		4000077100/surong100
		ViewGroup container = (ViewGroup) sectionContactFriends
				.findViewById(R.id.section_contact_fiend_headers);
		int cc = container.getChildCount();
		ArrayList<String> nameList = new ArrayList<String>(4);
		for (int i = 0; i < cc; i++) {
			ImageView imageView = (ImageView) container.getChildAt(i);

			if (i < friendList.size()) {
				DataWrapper inf = friendList.get(i);
				nameList.add(StringUtility.select(true,
						inf.getString("displayName"),
						inf.getString("realName"), "--"));
				HttpImage.getInstance().setImage(inf.getString("headImgPath"),
						imageView, R.drawable.default_avatar,
						DimensionUtility.dp2px(52) * 0.5f);
			} else {
				imageView.setImageResource(R.drawable.transparent);
			}
		}

		int textColor = Color.parseColor("#707070");
		TextView textView = (TextView) sectionContactFriends
				.findViewById(R.id.label_contact_friends);
		Spannable spannable = TextUtility.foreGroundSpan(new String[] {
				"您通讯录的好友", ArrayUtility.join(nameList.toArray(), "、"), "等",
				friendList.size() + "", "个好友也在这里，去打个招呼吧！" }, new int[] {
				textColor, Color.parseColor("#0075c0"), textColor, Color.RED,
				textColor });
		textView.setText(spannable);
	}

	private void loadXdjl() {
		HttpRequest request = new HttpRequest(
				url + "/surong/api/contacts/home", 0);

		request.setResponseDataFormat(HttpRequest.RESPONSE_JSON);
		request.setExpectedDataFormat(HttpRequest.EXPECTED_DATAWRAPPER);

		request.addParameter("token", LDApplication.getInstance()
				.getSessionData(EASEConstants.TOKEN));

		request.setHttpRequestListener(new HttpListenerAdapter() {
			@Override
			public void onHttpResponse(HttpRequest arg0, HttpResult arg1) {
				super.onHttpResponse(arg0, arg1);
				// 处理
				DataWrapper resp = arg1.getResponseData();
				if (resp == null) {
					return;
				}
				int err = resp.getInt("code");
				if (err == 0) {
					List<DataWrapper> xdjlList = (List<DataWrapper>) resp
							.getObject("memberArray");
					handleXdjl(xdjlList);
				}
			}
		});

		request.startAsynchronousRequest();
	}

	private void handleXdjl(List<DataWrapper> xdjlList) {
		if (xdjlList == null || xdjlList.size() < 1) {
			return;
		}
		ViewGroup container = (ViewGroup) sectionXdjl
				.findViewById(R.id.section_xdjl_headers);
		int cc = container.getChildCount();
		for (int i = 0; i < cc; i++) {
			ImageView imageView = (ImageView) container.getChildAt(i);

			if (i < xdjlList.size()) {
				DataWrapper inf = xdjlList.get(i);
				HttpImage.getInstance().setImage(inf.getString("headImgPath"),
						imageView, R.drawable.default_avatar,
						DimensionUtility.dp2px(52) * 0.5f);
			} else {
				imageView.setImageResource(R.drawable.transparent);
			}
		}
	}

	public void onResume() {
		super.onResume();
		if (friendNum <= 1) {
			shouldLoadContactFriends = true;
			loadContactFriends();
			sectionXdjl.setVisibility(View.VISIBLE);
			shouldLoadXdjl = true;
			loadXdjl();
		} else {
			sectionContactFriends.setVisibility(View.GONE);
			shouldLoadContactFriends = false;
			sectionXdjl.setVisibility(View.GONE);
			shouldLoadXdjl = false;
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("shua");
		getActivity().registerReceiver(broadcastReceiver, intentFilter);

		// if (friendNum <= 1) {
		// shouldLoadContactFriends = true;
		// loadContactFriends();
		// sectionXdjl.setVisibility(View.VISIBLE);
		// shouldLoadXdjl = true;
		// loadXdjl();
		// } else {
		// sectionContactFriends.setVisibility(View.GONE);
		// shouldLoadContactFriends = false;
		// sectionXdjl.setVisibility(View.GONE);
		// shouldLoadXdjl = false;
		// }

		// registerReceiver(broadcastReceiver, intentFilter);
		// loadFriends();
		// getFriendList();
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshFriendList();
		}
	};

	public void refreshFriendList() {
		// loadFriends();
		getFriendList();
	}

	private void loadFriends() {
		HttpRequest request = new HttpRequest(
				com.surong.leadloan.utils2.Constans.friendList, 0);

		request.setResponseDataFormat(HttpRequest.RESPONSE_JSON);
		request.setExpectedDataFormat(HttpRequest.EXPECTED_STRING);

		request.addParameter("token", LDApplication.getInstance()
				.getSessionData(EASEConstants.TOKEN));

		request.setHttpRequestListener(new HttpListenerAdapter() {

			@Override
			public void beforeHttpRequest(HttpRequest arg0) {
			}

			@Override
			public void onHttpRequestCancelled(HttpRequest arg0) {
			}

			@Override
			public void onHttpResponse(HttpRequest arg0, HttpResult arg1) {
				// 处理
				try {
					if (arg1.getResponseData() != null) {
						// List<FriendList> friendList = FriendList
						// .parse(new JSONObject((String) arg1
						// .getResponseData())
						// .getString("labelArray"));
						friends = FriendList.parse(new JSONObject((String) arg1
								.getResponseData()).getString("labelArray"));
						buddy.clear();
						if (friends != null || friends.size() > 0) {

							resetDefaultSections(true);

							group = new String[friends.size()];
							num = new int[friends.size()];

							EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(
									mActivity);
							friendNum = 0;
							for (int i = 0; i < group.length; i++) {
								friendNum += friends.get(i).getFriendList()
										.size();
							}
							// friendNum =
							// friendList.get(0).getFriendList().size();
							if (friendNum <= 1) {
								shouldLoadContactFriends = true;
								loadContactFriends();
								sectionXdjl.setVisibility(View.VISIBLE);
								shouldLoadXdjl = true;
								loadXdjl();
							} else {
								sectionContactFriends.setVisibility(View.GONE);
								shouldLoadContactFriends = false;
								sectionXdjl.setVisibility(View.GONE);
								shouldLoadXdjl = false;
							}

							for (int i = 0; i < group.length; i++) {
								FriendList list = friends.get(i);
								group[i] = list.getLabelName() + " ("
										+ list.getFriendList().size() + ")";
								num[i] = list.getFriendList().size();
								List<Friend> friends = list.getFriendList();
								buddy.add(friends);

								// 存到数据库
								for (Friend friend : friends) {
									DataWrapper info = new DataWrapper();
									info.setObject(EASEDatabaseUserInfo.UserID,
											friend.getFriendId());
									info.setObject(
											EASEDatabaseUserInfo.UserDisplayName,
											friend.getRemarkName());
									info.setObject(
											EASEDatabaseUserInfo.UserImg,
											friend.getHeadImgPath());
									info.setObject(
											EASEDatabaseUserInfo.UserIsMyFriend,
											friend.getIsFriend());
									info.setObject(
											EASEDatabaseUserInfo.UserOrganizationName,
											friend.getInstituShortName());
									info.setObject(
											EASEDatabaseUserInfo.UserRealName,
											friend.getDisplayName());
									databaseUserInfo
											.insertOrUpdateUserTmpInfo(info);
								}

								expandablelistview.expandGroup(i);
							}

						} else {
							group = new String[] {};
							resetDefaultSections(false);
							if (shouldLoadContactFriends) {
								loadContactFriends();
							}
							if (shouldLoadXdjl) {
								loadXdjl();
							}
						}
						mAdapter.setGroup(group);
						mAdapter.setData(buddy);
						mAdapter.notifyDataSetChanged();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					group = new String[] {};
					mAdapter.setGroup(group);
					mAdapter.setData(buddy);
					mAdapter.notifyDataSetChanged();

					resetDefaultSections(false);
					if (shouldLoadContactFriends) {
						loadContactFriends();
					}
					if (shouldLoadXdjl) {
						loadXdjl();
					}
				}
			}
		});

		request.startAsynchronousRequest();
	}

	private boolean checkPhone(String phone) {
		Pattern p = Pattern.compile("1\\d{10}");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	private void hideSoftInput() {
		if (imm != null) {
			View v = mActivity.getCurrentFocus();
			if (v == null) {
				return;
			}

			imm.hideSoftInputFromWindow(searchView.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			searchView.clearFocus();
		}
	}

	private void initAction() {

		// expandablelistview
		// .setOnGroupExpandListener(new OnGroupExpandListener() {
		// public void onGroupExpand(int groupPosition) {
		// }
		// });
		//
		// expandablelistview
		// .setOnGroupCollapseListener(new OnGroupCollapseListener() {
		// public void onGroupCollapse(int groupPosition) {
		// }
		// });


		expandablelistview.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupPosition, int childPosition, long arg4) {
				Friend friend = (Friend) mAdapter.getChild(groupPosition,
						childPosition);
				Intent intent = new Intent(mActivity, ChatActivity.class);
				// it is single chat
				// Toast.makeText(mActivity, BaiduMapTool.latitude+"",
				// Toast.LENGTH_SHORT).show();
				intent.putExtra("latitude", BaiduMapTool.latitude);
				intent.putExtra("longitude", BaiduMapTool.longitude);
				intent.putExtra("userId", friend.getFriendId());
				intent.putExtra("name", friend.getDisplayName());
				intent.putExtra("headimage", friend.getHeadImgPath());
				// Bundle bundle = new Bundle();
				// bundle.putString("userId", friend.getFriendId());
				// bundle.putString("name", friend.getDisplayName());
				// bundle.putString("institute", friend.getInstituShortName());
				// bundle.putString("headimage", friend.getHeadImgPath());
				// bundle.putInt("labelcode", friend.getLabelCode());
				// bundle.putString("memberlevel", friend.getMemberLevel());
				// intent.putExtras(bundle);
				startActivity(intent);
				// Toast.makeText(
				// mActivity,
				// group[groupPosition] + " : "
				// + buddy[groupPosition][childPosition],
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});
		relativeLayout[0].setOnClickListener(this);
		relativeLayout[1].setOnClickListener(this);
		relativeLayout[2].setOnClickListener(this);
		relativeLayout[3].setOnClickListener(this);

	}

	private void initData() {
		mImageFetcher = ImageFetcher.Instance(context, 0);
		expandablelistview.setAdapter(mAdapter);
		expandablelistview.setGroupIndicator(null);

		expandablelistview.setOnItemLongClickListener(this);

		imm = (InputMethodManager) mActivity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		try {
			setData();
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	private void setData() throws DbException {
		personal = CommonActivity.db.findFirst(Personal.class);
		displayName = personal.getDisplayName();
		instituName = personal.getInstituName();
		createDate = personal.getCreateDate();
		point = personal.getPoint();
		amount = personal.getAmount();
		stateString = personal.getAuthStatus();
		headImgPath = personal.getHeadImgPath();

		// 如果数据库没有获取到数据
		// if (notNull(displayName)) {
		// name.setText(displayName);
		// } else {
		// name.setText("无姓名信息");
		// }
		//
		// if (notNull(instituName)) {
		// institute.setText(instituName);
		// } else {
		// institute.setText("无机构信息");
		// }
		//
		// if (notNull(amount)) {
		// money.setText(amount);
		// }
		// if (notNull(point)) {
		// fen.setText(point);
		// }
		//
		// if (notNull(createDate)) {
		// create_time.setText(createDate);
		// }

		if (notNull(stateString)) {
			// String statusString = stateString;
			String statusChina = "";
			if ("00".equals(stateString)) {
				statusChina = "未认证";
			}
			if ("01".equals(stateString)) {
				statusChina = "认证未通过";
			}
			if ("02".equals(stateString)) {
				statusChina = "认证通过";
			}
			if ("03".equals(stateString)) {
				statusChina = "认证中";
			}
			if ("04".equals(stateString)) {
				statusChina = "申请认证中";
			}
			if ("05".equals(stateString)) {
				statusChina = "无效申请";
			}
			id_certify_situation.setText(statusChina);
			institute_certify_situation.setText(statusChina);
			if (id_certify_situation.getText().toString().trim().equals("未认证")
					|| id_certify_situation.getText().toString().trim()
							.equals("无效申请")) {
				sectionAuth.setVisibility(View.VISIBLE);
			} else {
				sectionAuth.setVisibility(View.GONE);
			}
		}

		if (null != headImgPath && !"".equals(headImgPath)) {
			mImageFetcher.addTask(auth_head, headImgPath, 0);
			auth_head.setTag(headImgPath);
		}
		//
		// CustomProgressDialog.stopProgressDialog();

	}

	private boolean notNull(String displayName2) {
		if (null == displayName2 || "".equals(displayName2)) {
			return false;
		}
		return true;
	}

	private void findView() {
		gotorenzheng.setOnClickListener(this);
		authentication.setOnClickListener(this);
		auth_head = (ImageView) head.findViewById(R.id.auth_head);
		expandablelistview = (ExpandableListView) view
				.findViewById(R.id.buddy_expandablelistview);
		searchView = (EditText) view.findViewById(R.id.search);
		searchView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				for (int j = 0; j < group.length; j++) {
					expandablelistview.expandGroup(j);
				}
				mAdapter.search(arg0.toString());
				if (!TextUtils.isEmpty(arg0)) {
					head.setVisibility(View.GONE);
				} else {
					head.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		relativeLayout = new RelativeLayout[4];
		relativeLayout[0] = (RelativeLayout) head.findViewById(R.id.relative_1);
		relativeLayout[1] = (RelativeLayout) head.findViewById(R.id.relative_2);
		relativeLayout[2] = (RelativeLayout) head.findViewById(R.id.relative_3);
		relativeLayout[3] = (RelativeLayout) head.findViewById(R.id.relative_4);

		// expandablelistview.addHeaderView(head);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relative_1:// 同行
			Intent i2 = new Intent(mActivity, PeerActivity.class);
			// it is single chat
			// Toast.makeText(mActivity, BaiduMapTool.latitude+"",
			// Toast.LENGTH_SHORT).show();
			i2.putExtra("latitude", BaiduMapTool.latitude);
			i2.putExtra("longitude", BaiduMapTool.longitude);
			startActivity(i2);
			break;

		case R.id.gotorenzheng:// 认证
			startActivity(new Intent(mActivity, ApplyCertification.class));
			break;
		case R.id.authentication:// 认证
			startActivity(new Intent(mActivity, ApplyCertification.class));
			break;
		case R.id.relative_2:// 附近的同行
			Intent myintent = new Intent(mActivity, NearPeerActivity.class);
			myintent.putExtra("latitude", BaiduMapTool.latitude);
			myintent.putExtra("longitude", BaiduMapTool.longitude);
			// Toast.makeText(mActivity,BaiduMapTool.latitude
			// +""+BaiduMapTool.longitude, Toast.LENGTH_SHORT).show();
			startActivity(myintent);
			break;
		case R.id.relative_4:// 可能认识的的人
			startActivity(new Intent(mActivity, PossibleKnowActivity.class));
			// Toast.makeText(mActivity,BaiduMapTool.latitude
			// +""+BaiduMapTool.longitude, Toast.LENGTH_SHORT).show();

			break;
		case R.id.relative_3:// 通讯录

			startActivity(new Intent(mActivity, ContactListActivity.class));
			break;

		case R.id.section_contact_friend_handler:// 点击通讯录好友推荐
			startActivity(new Intent(mActivity, ContactListActivity.class));
			break;

		case R.id.section_xdjl_handler:// 点击活跃的信贷经理
			Intent i1 = new Intent(mActivity, FriendsActivity.class);
			startActivity(i1);
			break;

		case R.id.section_upload_contact:// 点击上传通讯录
			startActivity(new Intent(mActivity, ContactListActivity.class));
			break;

		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1000) {
			if (resultCode == Activity.RESULT_OK) {
				showTip("申请认证中");
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Object object = parent.getItemAtPosition(position);

		if (!(object instanceof Friend)) {
			return true;
		}
		Friend friend = (Friend) object;
		final MenuDialog menuDialog = new MenuDialog(mActivity);
		menuDialog.setCancelable(true);

		menuDialog.setTitle(getString(
				R.string.title_delete_friend,
				StringUtility.select(true, friend.getRemarkName(),
						friend.getDisplayName(), "--")));
		menuDialog.addItem(getString(R.string.label_delete), 0, friend);
		menuDialog.show();

		menuDialog
				.setOnItemClickListener(new MenuDialog.MenuDialogItemClickListener() {

					@Override
					public boolean onItemClick(int id, TextView item) {
						Object object = item.getTag();
						if (object instanceof Friend) {
							Friend friend = (Friend) object;
							friendNum = 0;
							for (int i = 0; i < group.length; i++) {
								FriendList list = friends.get(i);
								for (int j = 0; j < list.getFriendList().size(); j++) {
									if (list.getFriendList().get(j).getMobile()
											.trim().equals(friend.getMobile())) {
										num[i]--;
										friendNum += num[i];
									}
								}
								group[i] = list.getLabelName() + " (" + num[i]
										+ ")";
							}

							if (friendNum <= 1) {

								sectionContactFriends
										.setVisibility(View.VISIBLE);
								shouldLoadContactFriends = true;
								loadContactFriends();
								sectionXdjl.setVisibility(View.VISIBLE);
								shouldLoadXdjl = true;
								loadXdjl();
							} else {
								sectionContactFriends.setVisibility(View.GONE);
								shouldLoadContactFriends = false;
								sectionXdjl.setVisibility(View.GONE);
								shouldLoadXdjl = false;
							}
							deleteFriend(friend.getFriendId(), friend);
							mAdapter.notifyDataSetChanged();
						}
						return true;
					}
				});
		return true;
	}

	private void deleteFriend(String friendId, Object extraData) {

		if (friendId.equals("system")) {
			Constans.Toast(getActivity(), "系统好友,不能删除");
			return;
		}
		HttpRequest request = new HttpRequest(
				url + "/surong/api/friend/delete", 0);
		request.setMethod(HttpRequest.METHOD_POST);
		request.setResponseDataFormat(HttpRequest.RESPONSE_JSON);
		request.setExpectedDataFormat(HttpRequest.EXPECTED_DATAWRAPPER);
		request.setParameter("token", LDApplication.getInstance()
				.getSessionData(EASEConstants.TOKEN));
		request.setParameter("friendId", friendId);
		request.setHttpRequestListener(this);
		request.setExtraData(extraData);
		request.startAsynchronousRequest();
	}

	@Override
	public void beforeHttpRequest(HttpRequest arg0) {
		CustomProgressDialog.createDialog(mActivity);
		CustomProgressDialog.startProgressDialog(mActivity);
	}

	@Override
	public void onHttpRequestCancelled(HttpRequest arg0) {
		CustomProgressDialog.stopProgressDialog();
	}

	private void showTip(String msg) {
		Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onHttpResponse(HttpRequest arg0, HttpResult arg1) {
		CustomProgressDialog.stopProgressDialog();
		DataWrapper resp = arg1.getResponseData();
		if (resp != null) {
			int err = resp.getInt("code");
			if (err != 0) {
				showTip(getString(R.string.delete_fail));
			} else {
				Friend friend = (Friend) arg0.getExtraData();
				EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(
						mActivity);
				databaseUserInfo.setIsMyFriend(false, friend.getFriendId());

				EMChatManager.getInstance().deleteConversation(
						friend.getFriendId());// 删除聊天内容

				NotificationCenter.getDefaultCenter().sendNotification(this, 0,
						friend.getFriendId());

				int all = 0;
				for (List<Friend> list : buddy) {
					list.remove(friend);
					all += list.size();

				}

				mAdapter.notifyDataSetChanged();

				showTip(getString(R.string.delete_succ));

				if (all < 1) {
					mAdapter.setGroup(new String[] {});
					mAdapter.notifyDataSetChanged();
					// 全删了
					resetDefaultSections(false);
					if (shouldLoadContactFriends) {
						loadContactFriends();
					}
					if (shouldLoadXdjl) {
						loadXdjl();
					}
				}
			}
		}
	}

	private class HttpListenerAdapter implements HttpRequestListener {

		private CustomProgressDialog progressDialog;

		public HttpListenerAdapter() {
			progressDialog = new CustomProgressDialog(mActivity,
					R.style.MyDialogStyle);
			progressDialog.setCancelable(false);
			progressDialog.setContentView(R.layout.customprogressdialog);
			progressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		}

		@Override
		public void beforeHttpRequest(HttpRequest arg0) {
			if (progressDialog != null && !progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		public void onHttpRequestCancelled(HttpRequest arg0) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
		}

		@Override
		public void onHttpResponse(HttpRequest arg0, HttpResult arg1) {
			progressDialog.dismiss();
		}

	}

	private void getFriendList() {
		CustomProgressDialog.startProgressDialog(context);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getString(context, "token"));
		myHttpUtils.getHttpJsonString(params,
				com.surong.leadloan.utils2.Constans.friendList, handler,
				context, 0, Constans.thod_Get_0);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					JSONObject object = (JSONObject) msg.obj;
					String lab = object.getString("labelArray");

					List<FriendList> friendList = FriendList.parse(lab);
					friends = FriendList.parse(lab);
					buddy.clear();
					if (friendList != null || friendList.size() > 0) {

						resetDefaultSections(true);

						group = new String[friendList.size()];
						showFriendNum = new int[friendList.size()];
						num = new int[friendList.size()];

						EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(
								mActivity);
						friendNum = 0;
						for (int i = 0; i < group.length; i++) {
							friendNum += friendList.get(i).getFriendList()
									.size();
						}
						// friendNum =
						// friendList.get(0).getFriendList().size();
						if (friendNum <= 1) {
							shouldLoadContactFriends = true;
							loadContactFriends();
							sectionXdjl.setVisibility(View.VISIBLE);
							shouldLoadXdjl = true;
							loadXdjl();
						} else {
							sectionContactFriends.setVisibility(View.GONE);
							shouldLoadContactFriends = false;
							sectionXdjl.setVisibility(View.GONE);
							shouldLoadXdjl = false;
						}

						// for (int i = 0; i < group.length; i++) {
						// FriendList list = friendList.get(i);
						// group[i] = list.getLabelName() + " ("
						// + list.getFriendList().size() + ")";
						// num[i] = list.getFriendList().size();
						// List<Friend> friends = list.getFriendList();
						//
						// buddy.add(friends);
						for (int i = 0; i < group.length; i++) {
							

							FriendList list = friends.get(i);
							group[i] = list.getLabelName() + " ("
									+ list.getFriendList().size() + ")";
							num[i] = list.getFriendList().size();
							List<Friend> friends = list.getFriendList();
							List<Friend> friends2 = new ArrayList<Friend>();
								friends2 = list.getFriendList();
								buddy.add(friends2);

							expandablelistview.expandGroup(i);

							// 存到数据库
							for (Friend friend : friends2) {
								DataWrapper info = new DataWrapper();

								info.setObject(EASEDatabaseUserInfo.UserID,
										friend.getFriendId());
								info.setObject(
										EASEDatabaseUserInfo.UserDisplayName,
										friend.getRemarkName());
								info.setObject(EASEDatabaseUserInfo.UserImg,
										friend.getHeadImgPath());
								info.setObject(
										EASEDatabaseUserInfo.UserIsMyFriend,
										friend.getIsFriend());
								info.setObject(
										EASEDatabaseUserInfo.UserOrganizationName,
										friend.getInstituShortName());
								info.setObject(
										EASEDatabaseUserInfo.UserRealName,
										friend.getDisplayName());

								databaseUserInfo
										.insertOrUpdateUserTmpInfo(info);
							}

							expandablelistview.expandGroup(i);
						}

					} else {
						group = new String[] {};
						resetDefaultSections(false);
						if (shouldLoadContactFriends) {
							loadContactFriends();
						}
						if (shouldLoadXdjl) {
							loadXdjl();
						}
					}
					mAdapter.setGroup(group);
					mAdapter.setData(buddy);
					mAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
					group = new String[] {};
					mAdapter.setGroup(group);
					mAdapter.setData(buddy);
					mAdapter.notifyDataSetChanged();

					resetDefaultSections(false);
					if (shouldLoadContactFriends) {
						loadContactFriends();
					}
					if (shouldLoadXdjl) {
						loadXdjl();
					}
				}
				CustomProgressDialog.stopProgressDialog();

				break;

			default:
				break;
			}
		};
	};

}
