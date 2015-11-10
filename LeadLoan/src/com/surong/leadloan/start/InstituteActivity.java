package com.surong.leadloan.start;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.adapter.InstituteApapter;
import com.surong.leadloan.entity.Institu;

public class InstituteActivity extends CommonActivity implements
		OnItemClickListener, OnQueryTextListener {
	private ListView list;
	// 数据库管理对象
	// private DbUtils db;
	// 得到选中的城市索引
	private int position;
	// 机构适配器
	InstituteApapter adapter;
	// 保存机构集合
	private List<Institu> instituList;
	private List<Institu> tempInstitusList;

	private RelativeLayout rela_leftMenu;
	private TextView center_title;
	private SearchView search_view;
	private View view;
	private Button sureButton;
	private String contentString;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.institute, null);
		context = this;
		addContentView(view);
		changeTitle("任职机构");
		init();
		initProvince();
		initAciton();
	}

	private void initAciton() {
		search_view.setOnQueryTextListener(this);
		search_view.setSubmitButtonEnabled(false);

	}

	private void onQueryTextChange() {

	}

	/**
	 * 初始化View
	 */
	private void init() {
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		rela_leftMenu = (RelativeLayout) findViewById(R.id.rela_leftMenu);
		rela_leftMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		list = (ListView) view.findViewById(R.id.mlistView);
		list.setVisibility(View.GONE);
		center_title = (TextView) view.findViewById(R.id.center_title);
		search_view = (SearchView) view.findViewById(R.id.search_view);
		sureButton = (Button) view.findViewById(R.id.sure);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("institute", contentString);
				setResult(10002, intent);
				finish();

			}
		});
		center_title.setText("任职机构选择");
		list.setOnItemClickListener(this);
	}

	// 设置省份级联效果
	public void initProvince() {
		try {
			// 得到所有省份
			instituList = db.findAll(Institu.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		// 判断省份是否为空
		if (null == instituList || instituList.size() <= 0) {
			return;
		}
		// 初始化省份适配器
		tempInstitusList = new ArrayList<Institu>();
		for (Institu institu : instituList) {
			tempInstitusList.add(institu);
		}
		adapter = new InstituteApapter(getApplicationContext(), instituList);
		// 把适配器添加到listView
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		intent.putExtra("institute", instituList.get(arg2).getInstituteName());
		this.setResult(10002, intent);
		this.finish();
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		contentString = arg0;
		if (TextUtils.isEmpty(arg0)) {
			instituList.clear();
			list.setVisibility(View.GONE);
			for (Institu institu : tempInstitusList) {
				instituList.add(institu);
			}
			System.out.println(instituList.size());
			adapter.notifyDataSetChanged();
		} else {
			list.setVisibility(View.VISIBLE);
			for (int i = 0; i < instituList.size(); i++) {
				if (instituList.get(i).getInstituteName().contains(arg0)) {
					System.out.println(instituList.get(i).getInstituteName());
				} else {
					instituList.remove(i);
					i--;
				}
			}
			System.out.println(instituList.size());
			adapter.notifyDataSetChanged();
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
}
