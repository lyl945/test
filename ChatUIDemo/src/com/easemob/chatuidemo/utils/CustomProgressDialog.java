/**************************************************************************************
 * [Project]
 *       MyProgressDialog
 * [Package]
 *       com.lxd.widgets
 * [FileName]
 *       CustomProgressDialog.java
 * [Copyright]
 *       Copyright 2012 LXD All Rights Reserved.
 * [History]
 *       Version          Date              Author                        Record
 *--------------------------------------------------------------------------------------
 *       1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
 **************************************************************************************/

package com.easemob.chatuidemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;

import com.easemob.chatuidemo.R;

/********************************************************************
 * [Summary] TODO 请在此处简要描述此类所实现的功能。因为这项注释主要是为了在IDE环境中生成tip帮助，务必简明扼要 [Remarks]
 * TODO 请在此处详细描述类的功能、调用方法、注意事项、以及与其它类的关系.
 *******************************************************************/

public class CustomProgressDialog extends Dialog {

	private static CustomProgressDialog customProgressDialog = null;

	public CustomProgressDialog(Context context) {
		super(context);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, R.style.MyDialogStyle);
	}

	public static CustomProgressDialog createDialog(Context context) {
		customProgressDialog = new CustomProgressDialog(context,
				R.style.MyDialogStyle);
		customProgressDialog.setCancelable(false);
		customProgressDialog.setContentView(R.layout.customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		return customProgressDialog;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//
		if(keyCode==event.KEYCODE_BACK){
			stopProgressDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * 显示dialog
	 */
	public static void startProgressDialog(Context context) {
		if (customProgressDialog == null) {
			createDialog(context);
		}
		customProgressDialog.show();
	}

	/*
	 * 隐藏dialog
	 */
	public static void stopProgressDialog() {
		if (customProgressDialog != null&& customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
			customProgressDialog = null;
		}
	}

}
