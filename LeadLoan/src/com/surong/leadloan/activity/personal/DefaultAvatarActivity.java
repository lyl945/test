package com.surong.leadloan.activity.personal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.entity.Education;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.entity.WorkExperience;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.imageutils.ImageFetcher;
import com.surong.leadloan.ui.AutoLineFeedLayout;
import com.surong.leadloan.ui.MyButton;
import com.surong.leadloan.ui.NonScrollableListView;
import com.surong.leadloan.ui.ShowDialog;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.BitmapUtil;
import com.surong.leadloan.utils.ClickUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;

/**
 * @author hedongsheng
 * 
 */
public class DefaultAvatarActivity extends CommonActivity implements
		OnClickListener {
	private Context context;
	private View view;
	private ImageView imageView[];
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.add_photo, null);
		addContentView(view);
		changeTitle("添加默认头像");
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		imageView = new ImageView[12];
		imageView[0] = (ImageView) view.findViewById(R.id.ImageView_add1);
		imageView[1] = (ImageView) view.findViewById(R.id.ImageView_add2);
		imageView[2] = (ImageView) view.findViewById(R.id.ImageView_add3);
		imageView[3] = (ImageView) view.findViewById(R.id.ImageView_add4);
		imageView[4] = (ImageView) view.findViewById(R.id.ImageView_add5);
		imageView[5] = (ImageView) view.findViewById(R.id.ImageView_add6);
		imageView[6] = (ImageView) view.findViewById(R.id.ImageView_add7);
		imageView[7] = (ImageView) view.findViewById(R.id.ImageView_add8);
		imageView[8] = (ImageView) view.findViewById(R.id.ImageView_add9);
		imageView[9] = (ImageView) view.findViewById(R.id.ImageView_add10);
		imageView[10] = (ImageView) view.findViewById(R.id.ImageView_add11);
		imageView[11] = (ImageView) view.findViewById(R.id.ImageView_add12);
		for (int i = 0; i <imageView.length; i++) {
			imageView[i].setOnClickListener(this);
		}
	}
	@Override
	public void onClick(View v) {
		intent = new Intent();
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ImageView_add1:
			intent.putExtra("head", "1");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add2:
			intent.putExtra("head", "2");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add3:
			intent.putExtra("head", "3");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add4:
			intent.putExtra("head", "4");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add5:
			intent.putExtra("head", "5");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add6:
			intent.putExtra("head", "6");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add7:
			intent.putExtra("head", "7");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add8:
			intent.putExtra("head", "8");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add9:
			intent.putExtra("head", "9");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add10:
			intent.putExtra("head", "10");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add11:
			intent.putExtra("head", "11");
			setResult(1001, intent);
			finish();
			break;
		case R.id.ImageView_add12:
			intent.putExtra("head", "12");
			setResult(1001, intent);
			finish();
			break;

		default:
			break;
		}
	}

}
