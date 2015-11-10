package com.surong.leadloan.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.personal.AboutActivity;
import com.surong.leadloan.activity.personal.AccountActivity;
import com.surong.leadloan.activity.personal.ApplyCertification;
import com.surong.leadloan.activity.personal.FinishPersonalInformation;
import com.surong.leadloan.activity.personal.MShopActivity;
import com.surong.leadloan.activity.personal.MyEvaluate;
import com.surong.leadloan.activity.personal.SettingActivity;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.imageutils.ImageFetcher;
import com.surong.leadloan.ui.ShowDialog;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;

public class PersonalFragment extends BaseFragment implements OnClickListener {
	private View view;
	private LinearLayout linearLayouts[];
	private String token;
	private MyHttpUtils myHttpUtils;
	private static final int flag_user_detail = 0;
	private String displayName;
	private String instituName;
	private String createDate;
	private String point;
	private String amount;
	public static String stateString;
	private String headImgPath;
	private TextView name, institute, money, fen, create_time, state;
	// private DbUtils dbUtils;
	private Personal personal;
	private ShowDialog showDialog;
	private ImageView person_icon;
	private ImageFetcher mImageFetcher;
	private TextView back, title, right;
	private String statusChina = "";

	@Override
	public void setmActivity(Activity mActivity) {
		super.setmActivity(mActivity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.personal, container, false);
		findView();
		initData();
		ininAction();
		return view;
	}

	private void findView() {
		linearLayouts = new LinearLayout[7];
		linearLayouts[0] = (LinearLayout) view.findViewById(R.id.lin_1);
		linearLayouts[1] = (LinearLayout) view.findViewById(R.id.lin_2);
		linearLayouts[2] = (LinearLayout) view.findViewById(R.id.lin_3);
		linearLayouts[3] = (LinearLayout) view.findViewById(R.id.lin_4);
		linearLayouts[4] = (LinearLayout) view.findViewById(R.id.lin_5);
		linearLayouts[5] = (LinearLayout) view.findViewById(R.id.lin_6);
		linearLayouts[6] = (LinearLayout) view.findViewById(R.id.lin_7);
		name = (TextView) view.findViewById(R.id.name);
		institute = (TextView) view.findViewById(R.id.institute);
		money = (TextView) view.findViewById(R.id.money);
		fen = (TextView) view.findViewById(R.id.fen);
		create_time = (TextView) view.findViewById(R.id.create_time);
		state = (TextView) view.findViewById(R.id.state);
		person_icon = (ImageView) view.findViewById(R.id.person_icon);
		back = (TextView) view.findViewById(R.id.top_textView_left);
		title = (TextView) view.findViewById(R.id.top_textview_title);
		right = (TextView) view.findViewById(R.id.top_textView_right);
		title.setText("个人");
		person_icon.setOnClickListener(this);

	}

	private void initData() {
		mImageFetcher = ImageFetcher.Instance(mActivity, 0);
		// showDialog = new ShowDialog(mActivity, handler);
		token = SharedPreferencesHelp.getString(mActivity, "token");

		myHttpUtils = MyHttpUtils.myInstance();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		CustomProgressDialog.startProgressDialog(mActivity);
		// myHttpUtils.getHttpJsonString(params, Constans.userDetailUrl,
		// handler,
		// mActivity, flag_user_detail, Constans.thod_Get_0);

		/*
		 * dbUtils = DbUtils.create(mActivity); personal = new Personal(); try {
		 * personal = dbUtils.findFirst(Personal.class); if(null!=personal){
		 * setData(); }
		 * 
		 * } catch (DbException e) { e.printStackTrace(); }
		 */
		//

		setData();
	}

	// Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	// case flag_user_detail:
	// JSONObject json = (JSONObject) msg.obj;
	//
	// try {
	// JSONObject jsonObject = json.getJSONObject("member");
//	 displayName = Utils.getStringWithKey("displayName",
	// jsonObject);
	// instituName = Utils.getStringWithKey("instituName",
	// jsonObject);
	// createDate = Utils.getStringWithKey("createDate",
	// jsonObject);
	// point = Utils.getStringWithKey("point", jsonObject);
	// amount = Utils.getStringWithKey("amount", jsonObject);
	// stateString = Utils.getStringWithKey("stateString",
	// jsonObject);
	// headImgPath = Utils.getStringWithKey("headImgPath",
	// jsonObject);
	//
	// setData();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// break;
	//
	// default:
	// break;
	// }
	// };
	// };
	@Override
	public void onStart() {
		super.onStart();
		setData();
	}

	private void setData() {
		try {
			personal = CommonActivity.db.findFirst(Personal.class);
			if (personal != null) {
				displayName = personal.getDisplayName();
				instituName = personal.getInstituName();
				createDate = personal.getCreateDate();
				point = personal.getPoint();
				amount = personal.getAmount();
				stateString = personal.getAuthStatus();
				headImgPath = personal.getHeadImgPath();
			}
			// 如果数据库没有获取到数据
			if (notNull(displayName)) {
				name.setText(displayName);
			} else {
				name.setText("无姓名信息");
			}

			if (notNull(instituName)) {
				institute.setText(instituName);
			} else {
				institute.setText("无机构信息");
			}

			if (notNull(amount)) {
				money.setText(amount);
			}
			if (notNull(point)) {
				fen.setText(point);
			}

			if (notNull(createDate)) {
				create_time.setText(createDate);
			}

			if (notNull(stateString)) {
				// String statusString = stateString;
				if ("00".equals(stateString)) {
					statusChina = "未认证";
				}
				if ("01".equals(stateString)) {
					statusChina = "认证未通过";
				}
				if ("02".equals(stateString)) {
					statusChina = "认证通过";
				}
				if ("03".equals(stateString)) {
					statusChina = "认证中";
				}
				if ("04".equals(stateString)) {
					statusChina = "申请认证中";
				}
				if ("05".equals(stateString)) {
					statusChina = "无效申请";
				}

				state.setText(statusChina);
			}

			if (null != headImgPath && !"".equals(headImgPath)) {
				mImageFetcher.addTask(person_icon, headImgPath, 0);
				person_icon.setTag(headImgPath);
			}

			CustomProgressDialog.stopProgressDialog();
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	private boolean notNull(String displayName2) {
		if (null == displayName2 || "".equals(displayName2)) {
			return false;
		}
		return true;
	}

	private void ininAction() {
		for (int i = 0; i < linearLayouts.length; i++) {
			linearLayouts[i].setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_1:
			startActivity(new Intent(getActivity(), MShopActivity.class));
			break;
		case R.id.lin_2:
			startActivity(new Intent(getActivity(), AccountActivity.class));
			break;
		case R.id.lin_3:// 申请认证
			startActivityForResult((new Intent(getActivity(),
					ApplyCertification.class)).putExtra("state", state
					.getText().toString()), 1000);
			break;
		case R.id.lin_4:
			startActivity(new Intent(getActivity(),
					FinishPersonalInformation.class));
			break;
		case R.id.lin_5:
			startActivity(new Intent(getActivity(), MyEvaluate.class));
			break;
		case R.id.lin_6:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
		case R.id.lin_7:
			startActivity(new Intent(getActivity(), AboutActivity.class));
			break;

		case R.id.person_icon:
			startActivity(new Intent(getActivity(),
					FinishPersonalInformation.class));
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000) {
			state.setText(statusChina);
			if (data != null) {
				if (null != data.getExtras().getString("tag")) {
					state.setText(data.getExtras().getString("tag"));
				}
			}

		}
	}

}
