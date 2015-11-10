package com.surong.leadloan.activity.order;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.order.DirectMechanismActivity.MyWebViewClient;
import com.surong.leadloan.activity.personal.AccountActivity;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.utils.CustomProgressDialog;

public class CommissionDetailWebActivity extends CommonActivity {
	View view;
	Context context;
	WebView myWebView;
	String creditManagerId;
	private RelativeLayout back;
	private String ProdId;
	private String mCameraFilePath;
	WebChromeClient mChromeClient;
	ValueCallback<Uri> mUploadMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.direct_mechanism_activity, null);
		addContentView(view);
		changeTitle("订单详情");
		ProdId = getIntent().getStringExtra("Id");
		init();

	}

	private Intent createDefaultOpenableIntent() {
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("image/*");
		Intent chooser = createChooserIntent(createCameraIntent());
		chooser.putExtra(Intent.EXTRA_INTENT, i);
		return chooser;
	}

	private Intent createChooserIntent(Intent... intents) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
		chooser.putExtra(Intent.EXTRA_TITLE, "选择图片");
		return chooser;
	}

	@SuppressWarnings("static-access")
	private Intent createCameraIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File externalDataDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		File cameraDataDir = new File(externalDataDir.getAbsolutePath()
				+ File.separator + "515aaa");
		cameraDataDir.mkdirs();
		String mCameraFilePath = cameraDataDir.getAbsolutePath()
				+ File.separator + System.currentTimeMillis() + ".jpg";
		this.mCameraFilePath = mCameraFilePath;
		cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(mCameraFilePath)));
		return cameraIntent;
	}

	private void init() {
		back = this.relative_back;
		myWebView = (WebView) findViewById(R.id.web_view);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		back.setOnClickListener(this);
		myWebView.getSettings().setJavaScriptEnabled(true);

		mChromeClient = new WebChromeClient() {
			@SuppressWarnings("unused")
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				startActivityForResult(createDefaultOpenableIntent(), 1006);
			}
		};

		myWebView.setWebChromeClient(mChromeClient);
		myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		myWebView.setWebViewClient(new MyWebViewClient(context));
		myWebView.loadUrl("http://m.surong100.com/instituThrough/edit?id="
				+ ProdId);

	}

	public class MyWebViewClient extends WebViewClient {

		private Context context;

		public MyWebViewClient(Context context) {
			this.context = context;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			if (url != null
					&& url.contains("http://m.surong100.com/instituThrough/Data.htm")) {
				Intent intent = new Intent(CommissionDetailWebActivity.this,
						ProgressQueryActivity.class);
				context.startActivity(intent);
				finish();
				return true;
			}
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			CustomProgressDialog.stopProgressDialog();
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			CustomProgressDialog.startProgressDialog(context);
			super.onPageStarted(view, url, favicon);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relative_back:
			if (myWebView.canGoBack()) {
				myWebView.goBack();
			} else {
				finish();
			}
			break;
		default:
			break;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
			// 返回键退回
			myWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1006) {
			if (null == mUploadMessage)
				return;
			Uri result = data == null || resultCode != RESULT_OK ? null : data
					.getData();
			if (result == null && data == null && resultCode == RESULT_OK) {
				File cameraFile = new File(mCameraFilePath);
				if (cameraFile.exists()) {
					result = Uri.fromFile(cameraFile);
					// Broadcast to the media scanner that we have a new photo
					// so it will be added into the gallery for the user.
					sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
				}
			}
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}
}

// private void init() {
// back = this.relative_back;
// myWebView = (WebView) findViewById(R.id.web_view);
// WebSettings webSettings = myWebView.getSettings();
// webSettings.setJavaScriptEnabled(true);
// back.setOnClickListener(this);
// myWebView.setWebViewClient(new WebViewClient() {
// @Override
// public void onPageFinished(WebView view, String url) {
// CustomProgressDialog.stopProgressDialog();
// super.onPageFinished(view, url);
// }
// @Override
// public void onPageStarted(WebView view, String url, Bitmap favicon) {
// // TODO Auto-generated method stub
// super.onPageStarted(view, url, favicon);
// CustomProgressDialog.startProgressDialog(context);
// }
//
// });
// // myWebView
// //
// .loadUrl("http://m.surong100.com/instituThrough/index?app=android&creditManagerId="
// // + creditManagerId);
// myWebView
// .loadUrl("http://m.surong100.com/instituThrough/edit?id="+ProdId);
//
// }
// public void onClick(View v) {
// switch (v.getId()) {
// case R.id.relative_back:
// if (myWebView.canGoBack()) {
// myWebView.goBack();
// } else {
// finish();
// }
//
// break;
//
// default:
// break;
// }
// };
//
// @Override
// public boolean onKeyDown(int keyCode, KeyEvent event) {
// if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
// // 返回键退回
// myWebView.goBack();
// return true;
// }
// return super.onKeyDown(keyCode, event);
// }
//
// }
