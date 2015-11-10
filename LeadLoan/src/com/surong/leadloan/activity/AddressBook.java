package com.surong.leadloan.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.adapter.CategoryAdapter;
import com.surong.leadloan.entity.Category;
import com.surong.leadloan.entity.Contact;
import com.surong.leadloan.entity.MobileSoftware;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.start.MainActivity;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.ContactDataSource;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;

public class AddressBook extends CommonActivity implements OnClickListener,
		OnQueryTextListener {

	private SearchView search_view;
	private ListView listview;
	private List<MobileSoftware> mlist;
	private Button back;
	// 软键盘管理类（用于隐藏和显示软键盘）
	private InputMethodManager imm;
	/*
	 * private PersonalHttpServiceBean http;//匹配通讯录接口
	 */private String token;
	private String phone;// 存通讯录的电话信息
	private ArrayList<Contact> phoneList;
	private CategoryAdapter mCustomBaseAdapter;
	private Context context;

	private ArrayList<Category> myList;// 存放请求的list结果
	private ArrayList<Category> listData;// myList的临时存放集合
	private List<Category> listResult;// 搜索结果
	Category categoryOne, categoryTwo, categoryThree;
	private ArrayList<Category> mySectionArrayList;
	private boolean isFirstHttp;// 通讯录第一次请求
	private Boolean isLastHttp;// 通讯录最后一次请求
	private View view;
	private int pageNo;// 长传的页数
	private int pageSize = 200;// 上传的大小
	private MyHttpUtils myHttpUtils;
	private final int flag_addressbook = 0;
	private boolean flag_upload = false;// 是否上传完的标志
	private int k = 0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.personal_address_book, null);
		context = this;

		addContentView(view);
		changeTitle("通讯录");
		setRight("完成");

		categoryOne = new Category("未添加");
		categoryTwo = new Category("发邀请");
		categoryThree = new Category("已添加");
		mySectionArrayList = new ArrayList<Category>();
		listData = new ArrayList<Category>();
		isFirstHttp = true;
		context = this;
		search_view = (SearchView) findViewById(R.id.search_view);
		listview = (ListView) findViewById(R.id.list_address);

		imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(search_view.getWindowToken(), 0);
		pageNo = 1;
		myHttpUtils = MyHttpUtils.myInstance();
		getData();
		// getMyData();
		initAction();
	}

	@SuppressLint("NewApi")
	private void initAction() {
		getActionBar();
		// back.setOnClickListener(this);
		listview.setTextFilterEnabled(true);

		search_view.setOnQueryTextListener(this);
		search_view.setSubmitButtonEnabled(false);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.right:
			finish();
			startActivity(new Intent(AddressBook.this, MainActivity.class));
			break;

		default:
			break;
		}
	}

	public void getData() {
		// 从通讯录获取联系人信息
		mlist = ContactDataSource.getMobileContacts(this);
		// 将号码单独取出来
		phoneList = new ArrayList<Contact>();
		Iterator itr = mlist.iterator();
		while (itr.hasNext()) {
			MobileSoftware mo = (MobileSoftware) itr.next();
			if (checkPhone(mo.getPhoneNum())) {
				Contact contact = new Contact();
				contact.setName(mo.getPhoneName());
				contact.setMobile(mo.getPhoneNum());
				phoneList.add(contact);
			}

			/*
			 * if (phoneList.size()%10==0) { http(); pageNo++;
			 * phoneList.clear(); }
			 */
			if (phoneList.size() % 50 == 0) {
				http();
				// pageNo++;
				phoneList.clear();
			}
		}
		http();
		flag_upload = true;

	}

	public void http() {
		// 将号码转换成json数组
		Gson gson = new Gson();
		String arrJson = gson.toJson(phoneList);
		String phoneString = "{\"addressBook\":" + arrJson + "}";
		RequestParams params = new RequestParams();
		token = SharedPreferencesHelp.getString(context, "token");
		params.addQueryStringParameter("token", token);
		// params.addQueryStringParameter("pageNo", pageNo+"");
		// params.addQueryStringParameter("pageSize", pageSize+"");
		params.addQueryStringParameter("addressBook", phoneString);
		CustomProgressDialog.startProgressDialog(this);
		myHttpUtils.getHttpJsonString(params, Constans.addressBookUrl, handler,
				context, flag_addressbook, Constans.thod_Post_1);

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			k++;
			if (k == 1) {
				Constans.Toast(AddressBook.this, "上传通讯录成功");
			}
			myList = new ArrayList<Category>();
			if (msg.what == 0) {// flag_addressbook
				JSONObject object = (JSONObject) msg.obj;
				System.out.println(object.toString());
				JSONArray toAdd = new JSONArray(), toInvite = new JSONArray(), added = new JSONArray(), noRespond = new JSONArray();
				try {

					toAdd = object.getJSONArray("memberNotFriend");// 未添加
					// noRespond = object.getJSONArray("addFriendPhone");
					toInvite = object.getJSONArray("notMember");// 发邀请
					added = object.getJSONArray("memberFriend");// 已添加

				} catch (JSONException e) {
					e.printStackTrace();
				}
				getList(toAdd, toInvite, added);

			}
			// 上传完成
			if (flag_upload == true) {
				if (msg.what == 3000) {
					Constans.Toast(AddressBook.this, "没有匹配的号码");
					getAll();
				}
				if (isFirstHttp) {
					listData.add(categoryOne);
					listData.add(categoryTwo);
					listData.add(categoryThree);
					isFirstHttp = false;
					mCustomBaseAdapter = new CategoryAdapter(context, listData);
					listview.setAdapter(mCustomBaseAdapter);
				} else {
					mCustomBaseAdapter.notifyDataSetChanged();
				}
				// 将获取到的list临时存放在myList中
				for (Category category : listData) {
					Category myCategory = new Category(
							category.getmCategoryName());
					int j = category.getItemCount();
					for (int i = 1; i < category.getItemCount(); i++) {
						myCategory.addItem(category.getItem(i));
					}
					myList.add(myCategory);
					System.out.println("list长度" + myList.size());

				}
			}

		};
	};

	/*
	 * 当有匹配号码时候，获取匹配号码的名称，去分类展示
	 */

	private ArrayList<Category> getMyData() {
		ArrayList<Category> mList = new ArrayList<Category>();
		Category c = new Category("发邀请");
		for (int i = 0; i < 3; i++) {

			Contact mo = new Contact();
			mo.setName("胡天");
			mo.setMobile("13480928639");
			categoryTwo.addItem(mo);
		}
		Category c2 = new Category("未添加");
		for (int i = 0; i < 3; i++) {

			Contact mo = new Contact();
			mo.setName("胡天");
			mo.setMobile("13480928639");
			categoryThree.addItem(mo);
		}

		mList.add(c);
		mList.add(c2);
		return mList;
	}

	private void getList(JSONArray toAdd, JSONArray toInvite, JSONArray added) {

		ArrayList<Category> listData = new ArrayList<Category>();

		if (toAdd.length() != 0) {// 未添加里面包括 添加和等待验证的
			// Category categoryOne = new Category("未添加");

			if (toAdd.length() != 0) {

				for (int i = 0; i < toAdd.length(); i++) {

					try {
						Contact contact = new Contact();
						Gson gson = new Gson();
						System.out.println("444444444" + toAdd.get(i));
						JSONObject jsonObject = (JSONObject) toAdd.get(i);
						contact.setName(jsonObject.getString("displayName"));
						contact.setMobile(jsonObject.getString("mobile"));
						contact.setInstitue(jsonObject.getString("instituName"));
						contact.setHeadImgPath(jsonObject
								.getString("headImgPath"));
						contact.setId(jsonObject.getString("id"));
						/*
						 * //toAdd.get(i); JSONStringer stringer =
						 * (JSONStringer) toAdd.get(i); contact =
						 * gson.fromJson(stringer.toString(), Contact.class);
						 */
						categoryOne.addItem(contact);
						/*
						 * if(mlist.get(j).getPhoneNum().equals(toAdd.get(i))){
						 * System.out.println(mlist.get(j).getPhoneName());
						 * System.out.println(mlist.get(j).getPhoneNum());
						 * categoryOne.addItem(mlist.get(j)); }
						 */
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			/*
			 * if(noRespond.length()!=0){
			 * 
			 * for(int i = 0;i<noRespond.length();i++){ for(int j =
			 * 0;j<mlist.size();j++){ try {
			 * if(mlist.get(j).getPhoneNum().equals(noRespond.get(i))){
			 * mlist.get(j).setState(4);//4代表等待验证
			 * categoryOne.addItem(mlist.get(j)); } } catch (JSONException e) {
			 * 
			 * 
			 * }
			 */

			// listData.add(categoryOne);
		}
		if (toInvite.length() != 0) {
			// Category categoryTwo = new Category("发邀请");
			for (int i = 0; i < toInvite.length(); i++) {
				try {
					Contact contact = new Contact();
					/*
					 * Gson gson = new Gson(); JSONStringer stringer =
					 * (JSONStringer) toInvite.get(i); contact =
					 * gson.fromJson(stringer.toString(), Contact.class);
					 */
					JSONObject jsonObject = (JSONObject) toInvite.get(i);
					contact.setName(jsonObject.getString("name"));
					contact.setMobile(jsonObject.getString("mobile"));
					categoryTwo.addItem(contact);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			// listData.add(categoryTwo);
		}

		if (added.length() != 0) {
			// Category categoryThree = new Category("已添加");
			for (int i = 0; i < added.length(); i++) {

				try {
					Contact contact = new Contact();
					/*
					 * Gson gson = new Gson(); JSONStringer stringer =
					 * (JSONStringer) added.get(i); contact =
					 * gson.fromJson(stringer.toString(), Contact.class);
					 */
					JSONObject jsonObject = (JSONObject) added.get(i);
					contact.setName(jsonObject.getString("name"));
					contact.setMobile(jsonObject.getString("mobile"));
					categoryThree.addItem(contact);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			// listData.add(categoryThree);

		}
		// return listData;
	}

	/*
	 * 当上传通讯录没有匹配号码时，没有返回值，将通讯录里面的号码都展示为发邀请
	 */
	private void getAll() {
		ArrayList<Category> listData = new ArrayList<Category>();
		// Category categoryTwo = new Category("发邀请");
		for (int i = 0; i < mlist.size(); i++) {
			String phone = mlist.get(i).getPhoneNum();
			System.out.println(phone);
			if (checkPhone(phone)) {
				String name = mlist.get(i).getPhoneName();
				Contact contact = new Contact();
				contact.setName(mlist.get(i).getPhoneName());
				contact.setMobile(mlist.get(i).getPhoneNum());
				categoryTwo.addItem(contact);
			}

		}
		// Category categoryThree = new Category("已添加");
		// listData.add(categoryTwo);
		// listData.add(categoryThree);

		// return listData;
	}

	/*
	 * 排除座机号,匹配以1开头，后面10都是数字的号码
	 * 
	 * @param 手机号
	 * 
	 * @return boolean
	 */

	private boolean checkPhone(String phone) {
		Pattern p = Pattern.compile("1\\d{10}");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	public void myStartActivity(Intent intent) {
		startActivity(intent);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (TextUtils.isEmpty(newText)) {
			listData.clear();
			for (Category category : myList) {
				Category myCategory = new Category(category.getmCategoryName());
				int j = category.getItemCount();
				for (int i = 1; i < category.getItemCount(); i++) {
					myCategory.addItem(category.getItem(i));
				}
				listData.add(myCategory);
			}
			mCustomBaseAdapter.notifyDataSetChanged();
		} else {
			// listview.setFilterText(newText);
			for (int i = 0; i < listData.size(); i++) {
				Category category = listData.get(i);
				for (int j = 1; j < category.getItemCount(); j++) {

					if (category.getItemName(j).contains(newText)) {
						// listResult.add(list.get(i));

					} else {
						category.removeItem(j - 1);
						j--;
					}
				}

			}
			mCustomBaseAdapter.notifyDataSetChanged();
		}
		return false;
	}

}
