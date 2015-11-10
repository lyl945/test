package com.easemob.chatuidemo.res;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.utils.CustomConstants;
import com.easemob.chatuidemo.utils.ImageFetcher;
import com.easemob.chatuidemo.utils.IntentConstants;

/**
 * 选择相册
 * 
 */

public class ImageBucketChooseActivity extends Activity implements
		OnClickListener {
	private ImageFetcher mHelper;
	private List<ImageBucket> mDataList = new ArrayList<ImageBucket>();
	private ListView mListView;
	private ImageBucketAdapter mAdapter;
	private int availableSize;
	private TextView back, title, right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_image_bucket_choose);
		back = (TextView) findViewById(R.id.top_textView_left);
		title = (TextView) findViewById(R.id.top_textview_title);
		right = (TextView) findViewById(R.id.top_textView_right);
		title.setText("相册");
		right.setText("取消");
//		back.setCompoundDrawablesWithIntrinsicBounds(R.drawable.back, 0, 0, 0);
//		back.setOnClickListener(this);
		right.setOnClickListener(this);

		mHelper = ImageFetcher.getInstance(getApplicationContext());
		initData();
		initView();
	}

	private void initData() {
		mDataList = mHelper.getImagesBucketList(false);
		availableSize = getIntent().getIntExtra(
				IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
				CustomConstants.MAX_IMAGE_SIZE);
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new ImageBucketAdapter(this, mDataList);
		mListView.setAdapter(mAdapter);
		// TextView titleTv = (TextView) findViewById(R.id.title);
		// titleTv.setText("相册");
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				selectOne(position);

				Intent intent = new Intent(ImageBucketChooseActivity.this,
						ImageChooseActivity.class);
				intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
						(Serializable) mDataList.get(position).imageList);
				intent.putExtra(IntentConstants.EXTRA_BUCKET_NAME,
						mDataList.get(position).bucketName);
				intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
						availableSize);

				startActivityForResult(intent, 19);
			}
		});

		// TextView cancelTv = (TextView) findViewById(R.id.action);
		// cancelTv.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
	}

	private void selectOne(int position) {
		int size = mDataList.size();
		for (int i = 0; i != size; i++) {
			if (i == position)
				mDataList.get(i).selected = true;
			else {
				mDataList.get(i).selected = false;
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	protected void onActivityResult(int resultCode, int re, Intent data) {
		if (data == null) {
			return;
		}
		Intent result = new Intent();
		result.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
				data.getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST));
		setResult(Activity.RESULT_OK, result);
		setResult(Activity.RESULT_OK, result);
		finish();
	}
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("不发送图片");
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.top_textView_right) {
			finish();
		}

	}

}
