package com.surong.leadloan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.start.MainActivity;

public class AddressBookGet extends CommonActivity {

	private View view;
	private Context context;
	private TextView get_detail;// 了解详情
	private Button get_addressbook;// 立即同步

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		view = View.inflate(context, R.layout.address_get, null);
		super.onCreate(savedInstanceState);
		addContentView(view);
		changeTitle("通讯录");
		setRight("跳过");
		findView();
		initData();
		initAction();
	}

	private void findView() {
		get_detail = (TextView) view.findViewById(R.id.get_detail);
		get_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		get_addressbook = (Button) view.findViewById(R.id.get_addressbook);

	}

	private void initData() {

	}

	private void initAction() {
		get_detail.setOnClickListener(this);
		get_addressbook.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_detail:
			startActivity(new Intent(AddressBookGet.this,
					AddressBookExplain.class));
			break;
		case R.id.right:
			finish();
			startActivity(new Intent(AddressBookGet.this, MainActivity.class));
			break;
		case R.id.get_addressbook:
			startActivity(new Intent(AddressBookGet.this, AddressBook.class));
			break;
		default:
			break;
		}
	}
}
