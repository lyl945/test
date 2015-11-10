package com.surong.leadloan.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.surong.leadloan.R;

public class ShowDialog {
	private Dialog dialog;
	private Context context;
	private Handler handler;
	private DialogButton dialogs = new DialogButton();
	private View view;
	private TextView t_title, t_message;
	private Button b_queding, b_quxiao;

	/**
	 * TYPE_CENTER :dialog居中显示
	 * */
	public final static int TYPE_CENTER = 1;
	/**
	 * TYPE_CENTER :dialog居底部显示
	 * */
	public final static int TYPE_BOTTOM = 0;

	// 返回Dialog
	public Dialog getAlertDialog() {
		return dialog;
	}

	/* 构造方法 */
	public ShowDialog(Context argActivity, Handler argHandler) {
		this.handler = argHandler;
		this.context = argActivity;
	}

	public ShowDialog() {
		super();
	}

	/**
	 * dialog里控件点击事件
	 * 
	 * @author 宕斿缓
	 * 
	 */
	class DialogButton implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.close:// 关闭对话框
				dismiss();
				break;
			default:
				handler.sendEmptyMessage(arg0.getId());
				break;
			}
		}
	}

	/*
	 * 给dialog里的控件设置点击事件
	 */
	public void createDialog(View viewExit, int[] viewids, int type) {
		showDialog(viewExit, type);
		if (viewids != null) {
			for (int i = 0; i < viewids.length; i++) {
				viewExit.findViewById(viewids[i]).setOnClickListener(dialogs);
			}
		}
		// 关闭按钮单击事件
		if (viewExit.findViewById(R.id.close) != null) {
			viewExit.findViewById(R.id.close).setOnClickListener(dialogs);
		}
		viewids = null;
	}

	/*
	 * 创建dialog
	 */
	public void showDialog(View v, int type) {
		dialog = new Dialog(context, R.style.dialog);
		Window window = dialog.getWindow();
		if (TYPE_BOTTOM == type) {
			window.setGravity(Gravity.BOTTOM); // dialog底部显示
			window.setWindowAnimations(R.style.animation); // 动漫显示dialog
		} else {
			window.setGravity(Gravity.CENTER); // dialog居中显示
			window.setWindowAnimations(R.style.animation_out); // 动漫显示dialog
		}
		dialog.setContentView(v);
		dialog.show();
	}

	/**
	 * 显示普通对话框，包含标题，提示内容，确定按钮，取消按钮
	 * 
	 * @param title
	 *            :标题内容;
	 * @param message
	 *            :提示内容;
	 * @param queding
	 *            :确定按钮显示内容;
	 * @param quxiao
	 *            :取消按钮显示内容;
	 * @param quedingClick
	 *            :确定按钮的单击事件
	 * @param quxiaoClick
	 *            :取消按钮单击事件
	 * @return void;
	 * */
	public void showCustomDialog(String title, String message, String queding,
			String quxiao, OnClickListener quedingClick,
			OnClickListener quxiaoClick) {
		view = View.inflate(context, R.layout.cust_diaolog, null);
		t_title = (TextView) view.findViewById(R.id.title);
		t_message = (TextView) view.findViewById(R.id.text_message);
		b_queding = (Button) view.findViewById(R.id.btn_quedingWf);
		b_quxiao = (Button) view.findViewById(R.id.btn_quXiaoWf);
		t_title.setText(title);
		t_message.setText(message);
		b_queding.setText(queding);
		b_quxiao.setText(quxiao);
		b_queding.setOnClickListener(quedingClick);
		b_quxiao.setOnClickListener(quxiaoClick);
		this.createDialog(view, null, TYPE_CENTER);
	}

	// 关闭dialog
	public void dismiss() {
		if (dialog != null)
			dialog.dismiss();
		dialog = null;
	}

	/* handler传递 */
	public void sendMessage(int what, String message) {
		if (null != handler) {
			Message msg = handler.obtainMessage();
			if (null != message) {
				msg.obj = message;
			}
			msg.what = what;
			handler.sendMessage(msg);
			dialog.dismiss();
		}
	}
}
