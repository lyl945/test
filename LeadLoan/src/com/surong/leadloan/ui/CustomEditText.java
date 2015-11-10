package com.surong.leadloan.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author ��˼ά �ؼ�˵���������ִ���100000����С��㳤�ȴ���4λ����ʱ��������ʾ�û����������(�Է�����û�����ж�)
 * */

public class CustomEditText extends EditText {

	private Context context;
	private int number;

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomEditText(Context context) {
		super(context);
		init();
	}

	private void init() {
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (0 == number) {
					number = 100000;
				}
				String str = arg0.toString();
				if (str.equals("")) {// ���ַ������ж�
					return;
				}
				if(str.equals(".")){//��һ������Ϊ.
					setText("");
					return;
				}
				try {
					if (str.length() > 0 && str.charAt(str.length() - 1) != '.') {
						Double.parseDouble(str);
					}
				} catch (NumberFormatException e) {
					setText("");
					return;
				}
				if (str.indexOf(".") > 0) {// �����С��㣬С���λ���Ȳ��ܴ���4λ
					if (str.substring(str.indexOf(".") + 1, str.length())
							.length() > 4) {
						str = str.substring(0, str.length() - 1);
						setText(str);
					}
					if (Integer.parseInt(str.substring(0, str.indexOf("."))) > 1000000) {
						str = str.substring(1, str.length());
						setText(str);
					}
				} else {// ����ֵ����100000
					if (Integer.parseInt(str) > 1000000) {
						str = str.substring(0, str.length() - 1);
						setText(str);

					}
				}
				setSelection(str.length());
			}
		});
	}

}
