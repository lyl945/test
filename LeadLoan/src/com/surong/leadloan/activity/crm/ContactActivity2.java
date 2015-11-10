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
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

public class ContactActivity2 extends CommonActivity implements OnClickListener {

	private View view;
	private Context context;
	private TextView myTextView;
	private String aString;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.contact2, null);
		addContentView(view);
		changeTitle("通讯录2");
		aString = getIntent().getStringExtra("tellNmu");
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		myTextView = (TextView) findViewById(R.id.myTextView);
		myTextView.setText(aString);
	}

}
