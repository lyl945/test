package com.surong.leadloan.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.surong.leadloan.R;

public class MyButton extends Button {

	private boolean isClick;
	private int id;// 标识button的id
	private Context context;
	private boolean check;

	public MyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyButton(Context context) {
		this(context, null);
		init();
	}

	public MyButton(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.buttonStyle);
		init();
	}

	public MyButton(Context context, int id) {
		super(context);
		this.id = id;
		isClick = false;
		this.context = context;
//		 myOnClickListener = new MyOnClickListener();
//		 setOnClickListener((OnClickListener) this);
//		 setFocusable(true);
//		 setClickable(true);
		init();
	}
	

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	private void init() {
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isClick) {
					isClick = false;
					setBackgroundDrawable(getResources().getDrawable(
							R.drawable.shape_bg));
					setTextColor(Color.argb(255, 51, 51, 51));

				} else {
					isClick = true;
					setBackgroundDrawable(getResources().getDrawable(
							R.drawable.shape_red_bg));
					setTextColor(Color.WHITE);
				}
			}
		});

	}
 
	/*
	 * @Override public void onClick(View v) { if (isClick) { isClick = false;
	 * setBackgroundDrawable
	 * (getResources().getDrawable(R.drawable.botton_cancel_h));
	 * 
	 * } else { isClick = true;
	 * setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_button));
	 * } }
	 */

	public int getValues() {
		if (isClick) {
			return id;
		} else {
			return -1;
		}
	}

}
