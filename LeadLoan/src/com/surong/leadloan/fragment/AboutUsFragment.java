package com.surong.leadloan.fragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.personal.MShopActivity;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.imageutils.ImageFetcher;
import com.surong.leadloan.ui.AutoLineFeedLayout;
import com.surong.leadloan.ui.EllipsizingTextView;
import com.surong.leadloan.ui.MyButton;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.MyApplication;

public class AboutUsFragment extends Fragment {

	// ///
	private View view;
	private JSONObject object;
	private TextView institute_name, institute_name2, display_name,
			person_duty, create_date, working_time, institu_termOfOffice,
			txt_mobile, txt_email, web_chat, txt_qq, txt_realName, txt_hobby,
			txt_vocation, my_textview1;
	private String instituDesc, instituName, displayName, personDuty,
			createDate, workingTime, instituTermOfOffice, mobile, email,
			webChat, qq, realName, logoPath;
	Map<String, String> hobby = new HashMap<String, String>();// 兴趣爱好
	Map<String, String> vocation = new HashMap<String, String>();// 职业标签
	private Button btn_more;
	private boolean flag = true;
	private ImageView institute_icon;
	private ImageFetcher mImageFetcher;
	private EllipsizingTextView institute_des;
	Map<String, String> timesMap1;
	// private DbUtils dbUtils;
	private Personal personal;
	private AutoLineFeedLayout hobbyAutoLayout; // 兴趣爱好表单
	private AutoLineFeedLayout vocationLayout; // 职业标签表单

	private LinearLayout aboutUsLayout;
	private String memberId;
	private View my_view1, my_view2, my_view3;
	private LinearLayout my_linearLayout1, my_linearLayout2;
	Iterator<String> iterator;
	JSONObject generalInfo;
	int width, height;

	private MShopActivity mActivity;

	public AboutUsFragment() {

	}

	public void setmActivity(MShopActivity mActivity) {
		this.mActivity = mActivity;
		this.object = mActivity.object;
		memberId = MShopActivity.memberId;
		try {
			personal = CommonActivity.db.findFirst(Personal.class);
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		JSONObject instituInfo;
		try {
			instituInfo = object.getJSONObject("instituInfo");
			// 机构信息
			if (instituInfo.isNull("instituDesc")) {
				instituDesc = " ";
			} else {
				String instituDes = instituInfo.getString("instituDesc");
				instituDesc = Constans.ToDBC(instituDes);
			}

			if (!instituInfo.isNull("logoPath")) {
				logoPath = instituInfo.getString("logoPath");
			}

			if (instituInfo.isNull("instituName")) {
				instituName = " ";
			} else {
				instituName = instituInfo.getString("instituName");
			}
			// 个人信息
			// JSONObject memberInfo = object.getJSONObject("memberInfo");
			// if (memberInfo.isNull("displayName")) {
			// displayName = " ";
			// } else {
			// displayName = memberInfo.getString("displayName");
			// }
			//
			// if (memberInfo.isNull("personDuty")) {
			// personDuty = " ";
			// } else {
			// personDuty = memberInfo.getString("personDuty");
			// }
			//
			// if (memberInfo.isNull("createDate")) {
			// createDate = " ";
			// } else {
			// createDate = memberInfo.getString("createDate");
			// }
			//
			// if (memberInfo.isNull("workingTime")) {
			// workingTime = " ";
			// } else {
			// workingTime = memberInfo.getString("workingTime");
			// }
			// if (memberInfo.isNull("instituTermOfOffice")) {
			// instituTermOfOffice = " ";
			// } else {
			// instituTermOfOffice = memberInfo
			// .getString("instituTermOfOffice");
			// }
			//
			// if (memberInfo.isNull("mobile")) {
			// mobile = " ";
			// } else {
			// mobile = memberInfo.getString("mobile");
			// }
			//
			// if (memberInfo.isNull("email")) {
			// email = " ";
			// } else {
			// email = memberInfo.getString("email");
			// }
			//
			// if (memberInfo.isNull("webChat")) {
			// webChat = " ";
			// } else {
			// webChat = memberInfo.getString("webChat");
			// }
			//
			// if (memberInfo.isNull("qq")) {
			// qq = " ";
			// } else {
			// qq = memberInfo.getString("qq");
			// }
			//
			// if (memberInfo.isNull("realName")) {
			// realName = " ";
			// } else {
			// realName = memberInfo.getString("realName");
			// }

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.about_us, container, false);
		initView();
		initButton();
		initInfoView();

		return view;
	}

	private void initView() {

		my_textview1 = (TextView) view.findViewById(R.id.my_textview1);
		my_view1 = (View) view.findViewById(R.id.my_view1);
		my_view2 = (View) view.findViewById(R.id.my_view2);
		my_view3 = (View) view.findViewById(R.id.my_view3);
		my_linearLayout1 = (LinearLayout) view
				.findViewById(R.id.my_linearLayout1);
		my_linearLayout2 = (LinearLayout) view
				.findViewById(R.id.my_linearLayout2);
		institute_name = (TextView) view.findViewById(R.id.institute_name);
		institute_des = (EllipsizingTextView) view
				.findViewById(R.id.institute_des);
		institute_des.setMaxLines(4);
		institute_name2 = (TextView) view.findViewById(R.id.institute_name2);
		display_name = (TextView) view.findViewById(R.id.display_name);
		person_duty = (TextView) view.findViewById(R.id.person_duty);
		create_date = (TextView) view.findViewById(R.id.create_date);
		working_time = (TextView) view.findViewById(R.id.working_time);
		institu_termOfOffice = (TextView) view
				.findViewById(R.id.institu_termOfOffice);
		txt_mobile = (TextView) view.findViewById(R.id.txt_mobile);
		txt_email = (TextView) view.findViewById(R.id.txt_email);
		web_chat = (TextView) view.findViewById(R.id.web_chat);
		txt_qq = (TextView) view.findViewById(R.id.txt_qq);
		txt_realName = (TextView) view.findViewById(R.id.realName);
		// 机构logo
		institute_icon = (ImageView) view.findViewById(R.id.institute_icon);
		btn_more = (Button) view.findViewById(R.id.btn_more);

		btn_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					institute_des.setEllipsize(null); // 展开
					institute_des.setSingleLine(false);
					btn_more.setText("收起");
					flag = false;
				} else {
					flag = true;
					institute_des.setMaxLines(4);
					btn_more.setText("查看更多内容");
				}
			}
		});

		try {
			personal = CommonActivity.db.findFirst(Personal.class);
			if (personal.getHobby() != null) {
				String[] hobbyArray = personal.getHobby().split(";");
				for (int i = 0; i < hobbyArray.length; i++) {
					String[] s = hobbyArray[i].split("-");
					if (s.length > 1) {
						hobby.put(s[0], s[1]);
					} else
						hobby.put(s[0], "");
				}
			}
			if (personal.getTag() != null) {
				String[] tagArray = personal.getTag().split(";");
				Log.e("123", "tagArray=" + tagArray);
				for (int i = 0; i < tagArray.length; i++) {
					String[] s1 = tagArray[i].split("-");
					if (s1.length > 1) {
						vocation.put(s1[0], s1[1]);
					} else
						vocation.put(s1[0], "");
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

		timesMap1 = new LinkedHashMap<String, String>();
		timesMap1.put("1", "一年整");
		timesMap1.put("2", "两年整");
		timesMap1.put("3", "三年整");
		timesMap1.put("4", "四年整");
		timesMap1.put("5", "五年整");
		timesMap1.put("6", "六年整");
		timesMap1.put("7", "七年整");
		timesMap1.put("8", "八年整");
		timesMap1.put("9", "九年整");
		timesMap1.put("10", "十年以上");

		setData();

	}

	private void initInfoView() {
		aboutUsLayout = (LinearLayout) view
				.findViewById(R.id.LinearLayout_about_us);
		aboutUsLayout.removeAllViews();
		TextView name, content;
		LinearLayout layout;
		boolean check = true;
		try {
			generalInfo = object.getJSONObject("generalInfo");
			iterator = generalInfo.keys();
			while (iterator.hasNext()) {
				View v = View.inflate(getActivity(), R.layout.about_us_item,
						null);
				layout = (LinearLayout) v.findViewById(R.id.layout);
				if (check == true) {
					layout.setBackgroundColor(Color.argb(255, 247, 251, 255));
				} else {
					layout.setBackgroundColor(Color.argb(255, 255, 255, 255));
				}
				name = (TextView) v.findViewById(R.id.item_key);
				content = (TextView) v.findViewById(R.id.item_value);
				String key = iterator.next();
				if (key != null) {
					name.setText(key);
					content.setText(generalInfo.getString(key));
				}
				aboutUsLayout.addView(v);
				check = !check;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void initButton() {

		hobbyAutoLayout = (AutoLineFeedLayout) view.findViewById(R.id.hobby1);

		hobbyAutoLayout.removeAllViews();
		Map<String, String> hobbyMap = MyApplication.dictionaryMap2
				.get("hobby");
		Iterator<Entry<String, String>> iterator = hobbyMap.entrySet()
				.iterator();

		// 添加兴趣爱好button集合
		while (iterator.hasNext()) {
			MyButton button = (MyButton) View.inflate(getActivity(),
					R.layout.c, null);
			Entry<String, String> entry = iterator.next();
			if (hobby.get(entry.getValue()) != null) {
				button.setText(entry.getKey());
				button.setEnabled(false);
				// button.setBackgroundResource(R.drawable.shape_red_bg);
				// button.setTextColor(Color.WHITE);
			} else {
				button.setVisibility(View.GONE);
			}

			hobbyAutoLayout.addView(button);
		}

		vocationLayout = (AutoLineFeedLayout) view.findViewById(R.id.vocation1);
		Map<String, String> vocationMap = MyApplication.dictionaryMap2
				.get("tag");
		Iterator<Entry<String, String>> iterator1 = vocationMap.entrySet()
				.iterator();
		while (iterator1.hasNext()) {
			MyButton button1 = (MyButton) View.inflate(getActivity(),
					R.layout.c, null);
			Entry<String, String> entry = iterator1.next();
			if (vocation.get(entry.getValue()) != null) {
				button1.setText(entry.getKey());
				button1.setBackgroundResource(R.drawable.shape_red_bg);
				button1.setTextColor(Color.WHITE);
				button1.setEnabled(false);
			} else {
				button1.setVisibility(View.GONE);
			}
			vocationLayout.addView(button1);

		}
	}

	private void setData() {

		institute_name2.setText(instituName);
		if (TextUtils.isEmpty(instituDesc) || instituDesc.trim().equals("")) {
			institute_des.setText("暂无公司简介");
			institute_icon.setVisibility(View.GONE);
		} else {
			institute_des.setText("　　" + instituDesc.trim());
			institute_icon.setVisibility(View.VISIBLE);
		}
		institute_name.setText(instituName);
		if (TextUtils.isEmpty(instituDesc.trim())) {
			btn_more.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(memberId) || memberId.trim().equals("")) {

		} else {
			my_textview1.setVisibility(View.GONE);
			// my_view1.setVisibility(View.GONE);
			my_view2.setVisibility(View.GONE);
			my_view3.setVisibility(View.GONE);
			my_linearLayout1.setVisibility(View.GONE);
			my_linearLayout2.setVisibility(View.GONE);
		}
		// display_name.setText(displayName);
		// person_duty.setText(personDuty);
		// create_date.setText(createDate);
		// working_time.setText(timesMap1.get(workingTime));
		// institu_termOfOffice.setText(timesMap1.get(instituTermOfOffice));
		// txt_mobile.setText(mobile);
		// txt_email.setText(email);
		// web_chat.setText(webChat);
		// txt_qq.setText(qq);
		// txt_realName.setText(realName);
		mImageFetcher = ImageFetcher.Instance(getActivity(), 0);
		if (null != logoPath && !"".equals(logoPath)) {
			mImageFetcher.addTask(institute_icon, logoPath, 1);
			institute_icon.setTag(logoPath);

		}
	}
}
