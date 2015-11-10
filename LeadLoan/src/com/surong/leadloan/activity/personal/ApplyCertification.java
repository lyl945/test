package com.surong.leadloan.activity.personal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.util.PathUtil;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.pj.core.managers.LogManager;
import com.pj.core.utilities.StringUtility;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.imageutils.ImageFetcher;
import com.surong.leadloan.start.CitySelectActivity2;
import com.surong.leadloan.ui.CircleProgressBars;
import com.surong.leadloan.ui.ShowDialog;
import com.surong.leadloan.utils.BitmapUtil;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.IDcardVerification;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;

public class ApplyCertification extends CommonActivity {
	private View view;
	private Context context;
	private MyHttpUtils myHttpUtils;
	private EditText edit_name, edit_diaplay_name, edit_id, instituName,
			instituTypeId;
	private EditText officeAddr;
	private RadioGroup group_sex, group_work;
	private ImageButton idFrontImg, apply_image, card_image;
	private Button btn_infirm;
	private String sex = "0";
	// 声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：
	static final int DATE_DIALOG_ID = 0;
	private int mYear, mMonth, mDay;
	private ShowDialog imageDialog, imageDialog2, imageDialog3;
	private File headImgFile, apply_image_file, card_image_file,camera;
	private String headImgPath;// 头像图片路径
	private Personal personal;

	private String imageString = "badgeImg";
	private RadioButton sex_man, sex_woman, work_1, work_2;
	private ImageFetcher mImageFetcher;
	private Map<String, String> map;
	private Map<String, String> newMap;
	private TextView edit_id_date, cityName;
	private AlertDialog verificationDialog;
	private TextView t_title, t_message;
	private Button b_queding, b_quxiao;
	private String cityId;
	private String cityname;
	private File cameraFile;
	private CircleProgressBars cirProgressBars;
	private TextView uploadingTextView;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.apply_certification, null);
		addContentView(view);
		changeTitle("申请认证");
//		options =new DisplayImageOptions.Builder()
//		// .showImageOnLoading(R.drawable.default_avator)
//		.showImageForEmptyUri(R.drawable.default_avatar)
//		.showImageOnFail(R.drawable.default_avatar).cacheInMemory(true)
//		.cacheOnDisk(true).considerExifParams(true)
//		.displayer(new SimpleBitmapDisplayer()).build();
		initView();
		initAction();
		initData();
		myHttpUtils.myInstance();
		

	}

	private void initData() {
		map = new HashMap<String, String>();
		newMap = new HashMap<String, String>();
		map = MyApplication.dictionaryMap2.get("MAP_INSTITU_TYPE");
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			newMap.put(entry.getValue(), entry.getKey());
		}
		mImageFetcher = ImageFetcher.Instance(context, 0);
		try {
			personal = db.findFirst(Personal.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
		/*
		 * dbUtils = DbUtils.create(context); try { personal =
		 * dbUtils.findFirst(Personal.class); if(null!=personal){ setData(); } }
		 * e.printStackTrace(); }
		 */

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getString(context, "token"));
		CustomProgressDialog.startProgressDialog(context);
		myHttpUtils.getHttpJsonString(params, Constans.getAuthInfoUrl,
				myHandler, context, 20, Constans.thod_Get_0);
		// 获得当前的日期：
		final Calendar currentDate = Calendar.getInstance();
		mYear = currentDate.get(Calendar.YEAR);
		mMonth = currentDate.get(Calendar.MONTH);
		mDay = currentDate.get(Calendar.DAY_OF_MONTH);
		// 设置文本的内容：
		edit_id_date.setText("");
		imageDialog = new ShowDialog(this, myHandler);
		imageDialog2 = new ShowDialog(this, myHandler2);
		imageDialog3 = new ShowDialog(this, myHandler3);
		/*
		 * if(null!=personal){ setData(); }
		 */

	}

	private boolean checkAll() {

		if (edit_name.getText().toString().trim().equals("")) {
			Constans.Toast(context, "您的姓名不能为空!");
			return false;
		} else if (edit_diaplay_name.getText().toString().trim().equals("")) {
			Constans.Toast(context, "您的显示名不能为空!");
			return false;
		} else if (!IDcardVerification.IDCardValidate(context, edit_id
				.getText().toString().trim())) {
			return false;
		} else if (edit_id_date.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请填写您的身份证到期时间");
			return false;
		} else if (personal.getIdFrontImg() == null && headImgFile == null) {
			Constans.Toast(context, "请上传您的身份证正面照片");
			return false;
		} else if (instituName.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请填写您所在的机构全称");
			return false;
		} else if (officeAddr.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请填写您的工作地址");
			return false;
		} else if (personal.getBadgeImg() == null
				&& personal.getBizCardImg() == null && apply_image_file == null
				&& card_image_file == null) {
			Constans.Toast(context, "请上传您的公司工牌或名片");
			return false;
		}
		return true;

	}

	private void setData() {
		if (isNotNull(personal.getRealName())) {
			edit_name.setText(personal.getRealName());
		}
		if (isNotNull(personal.getDisplayName())) {
			edit_diaplay_name.setText(personal.getDisplayName());
		}
		if (isNotNull(personal.getCityName())) {
			cityname = personal.getCityName();
			cityName.setText(personal.getCityName());
		}
		if (isNotNull(personal.getCityId())) {
			cityId = personal.getCityId();
		}
		if (isNotNull(personal.getInstituName())) {
			instituName.setText(personal.getInstituName());
		}

		if (isNotNull(personal.getTypeName())) {
			instituTypeId.setText(personal.getTypeName());
		}

		if (isNotNull(personal.getGender())) {
			if ("0".equals(personal.getGender())) {
				sex_man.setChecked(true);
			} else {
				sex_woman.setChecked(true);
			}
		}
		if (isNotNull(personal.getIdNo())) {
			edit_id.setText(personal.getIdNo());
		}
		if (isNotNull(personal.getIdExpiredDate())) {
			edit_id_date.setText(personal.getIdExpiredDate().toString());
		}
		if (isNotNull(personal.getOfficeAddr())) {
			officeAddr.setText(personal.getOfficeAddr().toString());
		}

		if (isNotNull(personal.getIdFrontImg())) {
			mImageFetcher.addTask(idFrontImg, personal.getIdFrontImg(), 3);
			idFrontImg.setTag(personal.getIdFrontImg());
//			ImageLoader.getInstance().displayImage(personal.getIdFrontImg(),
//					idFrontImg, options, null);
		}
		if (isNotNull(personal.getBadgeImg())) {
			work_1.setChecked(true);
			mImageFetcher.addTask(apply_image, personal.getBadgeImg(), 3);
			apply_image.setTag(personal.getBadgeImg());
		}
		if (isNotNull(personal.getBizCardImg())) {
			work_2.setChecked(true);
			mImageFetcher.addTask(card_image, personal.getBizCardImg(), 3);
			card_image.setTag(personal.getBizCardImg());
		}

	}

	private boolean isNotNull(String realName) {
		if (null == realName || "".equals(realName) || "null".equals(realName)) {
			return false;
		}
		return true;
	}

	private void initAction() {
		btn_infirm.setOnClickListener(this);
		group_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.sex_man:
					sex = "0";// 男-0 女-1
					break;

				default:
					sex = "1";
					break;
				}

			}
		});

		group_work.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.work_1:// 工牌
					imageString = "badgeImg";
					findViewById(R.id.cardLinearLayout)
							.setVisibility(View.GONE);
					findViewById(R.id.applyLinearLayout).setVisibility(
							View.VISIBLE);
					break;
				case R.id.work_2:// 公司名片
					imageString = "bizCardImg";
					findViewById(R.id.cardLinearLayout).setVisibility(
							View.VISIBLE);
					findViewById(R.id.applyLinearLayout).setVisibility(
							View.GONE);
					break;

				default:
					break;
				}

			}
		});

		cityName.setOnClickListener(this);
		edit_id_date.setOnClickListener(this);
		idFrontImg.setOnClickListener(this);
		apply_image.setOnClickListener(this);
		card_image.setOnClickListener(this);

	}

	private void initView() {
		edit_name = (EditText) view.findViewById(R.id.edit_name);
		cityName = (TextView) view.findViewById(R.id.cityName);
		edit_diaplay_name = (EditText) view
				.findViewById(R.id.edit_diaplay_name);
		edit_id = (EditText) view.findViewById(R.id.edit_id);
		edit_id_date = (TextView) view.findViewById(R.id.edit_id_date);
		group_sex = (RadioGroup) view.findViewById(R.id.group_sex);
		sex_man = (RadioButton) view.findViewById(R.id.sex_man);
		sex_woman = (RadioButton) view.findViewById(R.id.sex_woman);
		idFrontImg = (ImageButton) view.findViewById(R.id.idFrontImg);

		instituName = (EditText) view.findViewById(R.id.instituName);
		instituTypeId = (EditText) view.findViewById(R.id.instituTypeId);
		officeAddr = (EditText) view.findViewById(R.id.officeAddr);
		group_work = (RadioGroup) view.findViewById(R.id.group_work);
		work_1 = (RadioButton) view.findViewById(R.id.work_1);
		work_2 = (RadioButton) view.findViewById(R.id.work_2);

		apply_image = (ImageButton) view.findViewById(R.id.apply_image);
		card_image = (ImageButton) view.findViewById(R.id.card_image);
		btn_infirm = (Button) view.findViewById(R.id.btn_infirm);
	}
//	Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			cirProgressBars.setProgress(msg.what);
//			if (i <= cirProgressBars.getMax()) {
//				handler.sendEmptyMessageDelayed(i++, 100);
//			}
//		};
//	};

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.idFrontImg:
			View view1 = View.inflate(context, R.layout.headphoto_dialog, null);
			View view = view1.findViewById(R.id.view);
			view.setVisibility(view1.GONE);
			TextView text_addphoto = (TextView) view1.findViewById(R.id.text_addphoto);
			text_addphoto.setVisibility(view1.GONE);
			imageDialog.createDialog(view1, new int[] { 
					R.id.text_paizhao, R.id.text_xiangce, R.id.text_exit },
					ShowDialog.TYPE_BOTTOM);

			break;
		case R.id.apply_image:
			View view2 = View.inflate(context, R.layout.headphoto_dialog, null);
			imageDialog2
					.createDialog(view2, new int[] { R.id.text_paizhao,
							R.id.text_xiangce, R.id.text_exit },
							ShowDialog.TYPE_BOTTOM);
			break;
		case R.id.card_image:
			View view3 = View.inflate(context, R.layout.headphoto_dialog, null);
			imageDialog3
					.createDialog(view3, new int[] { R.id.text_paizhao,
							R.id.text_xiangce, R.id.text_exit },
							ShowDialog.TYPE_BOTTOM);
			break;
		case R.id.relative_back:
			finish();
			break;
		case R.id.cityName:
			Intent ii = new Intent(ApplyCertification.this,
					CitySelectActivity2.class);
			ii.putExtra("cityname", cityname);
			// startActivity(new
			// Intent(PeerActivity.this,CitySelectActivity.class));
			startActivityForResult(ii, 105);
			break;
		case R.id.btn_infirm:
		
//			myHttpUtils.load(context, verificationDialog, cirProgressBars, uploadingTextView);
			
			
			personal.setAuthStatus("04");
			personal.setRealName(edit_name.getText().toString());
			personal.setDisplayName(edit_diaplay_name.getText().toString());
			personal.setInstituName(instituName.getText().toString());
			personal.setTypeName(instituTypeId.getText().toString());
			personal.setGender("");
			personal.setIdNo(edit_id.getText().toString());
			personal.setIdExpiredDate(edit_id_date.getText().toString());
			personal.setOfficeAddr(officeAddr.getText().toString());
			personal.setCityId(cityId);
			personal.setCityName(cityname);
			if (checkAll()) {
				if (sex_man.isChecked()) {
					personal.setGender("0");
				} else {
					personal.setGender("1");
				}
				// personal.setIdFrontImg(idFrontImg)
				// if (isNotNull(personal.getGender())) {
				// if ("0".equals(personal.getGender())) {
				// sex_man.setChecked(true);
				// } else {
				// sex_woman.setChecked(true);
				// }
				// }
				// if (isNotNull(personal.getIdFrontImg())) {
				// mImageFetcher.addTask(idFrontImg, personal.getIdFrontImg(),
				// 1);
				// idFrontImg.setTag(personal.getIdFrontImg());
				// }
				// if (isNotNull(personal.getBadgeImg())) {
				// work_2.setChecked(true);
				// mImageFetcher.addTask(apply_image, personal.getBadgeImg(),
				// 1);
				// apply_image.setTag(personal.getBadgeImg());
				// }
				// if (isNotNull(personal.getBizCardImg())) {
				// work_1.setChecked(true);
				// mImageFetcher.addTask(card_image, personal.getBizCardImg(),
				// 1);
				// card_image.setTag(personal.getBizCardImg());
				// }

				final RequestParams params = new RequestParams();
				// CustomProgressDialog.startProgressDialog(context);
				// seasionId
				params.addQueryStringParameter("token",
						SharedPreferencesHelp.getString(context, "token"));
				// 身份认证信息
				params.addQueryStringParameter("realName", edit_name.getText()
						.toString().trim());
				params.addQueryStringParameter("displayName", edit_diaplay_name
						.getText().toString().trim());
				// 省份城市id
				params.addQueryStringParameter("cityId", cityId);
				params.addQueryStringParameter("gender", sex);
				params.addQueryStringParameter("idNo", edit_id.getText()
						.toString().trim());
				params.addQueryStringParameter("idExpiredDateStr", edit_id_date
						.getText().toString().trim());
				// 身份证明照
				if (headImgFile != null) {
					params.addBodyParameter("idFrontImg", headImgFile);
				}

				// 机构认证信息
				params.addQueryStringParameter("instituName", instituName
						.getText().toString().trim());
				// 将机构类型名称 转换为机构id
				// String instituteID =
				// newMap.get("instituTypeId.getText().toString().trim()");
				params.addQueryStringParameter("instituTypeId", instituTypeId
						.getText().toString().trim());
				params.addQueryStringParameter("officeAddr", officeAddr
						.getText().toString().trim());
				if (apply_image_file != null) {
					params.addBodyParameter(imageString, apply_image_file);
				}
				if (card_image_file != null) {
					params.addBodyParameter(imageString, card_image_file);
				}
				// params.addQueryStringParameter(,
				// instituName.getText().toString().trim());
				String state = getIntent().getStringExtra("state");
				if (state != null && state.equals("认证通过")) {
					verificationDialog = new AlertDialog.Builder(
							ApplyCertification.this).create();
					verificationDialog.show();
					verificationDialog.getWindow().setContentView(
							R.layout.cust_diaolog);
					t_title = (TextView) verificationDialog.getWindow()
							.findViewById(R.id.title);
					t_message = (TextView) verificationDialog.getWindow()
							.findViewById(R.id.text_message);
					b_queding = (Button) verificationDialog.getWindow()
							.findViewById(R.id.btn_quedingWf);
					b_quxiao = (Button) verificationDialog.getWindow()
							.findViewById(R.id.btn_quXiaoWf);
					t_title.setText("申请认证");
					t_message
							.setText("　　修改个人信息后，系统将重新审核您提交的认证资料，期间您将无法添加好友，管理客户。您确认要提交认证信息吗？");
					b_queding.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							verificationDialog = new AlertDialog.Builder(context).create();
							verificationDialog.show();
							verificationDialog.getWindow().setContentView(
									R.layout.cust_diaolog_test);
							cirProgressBars = (CircleProgressBars) verificationDialog.getWindow().findViewById(R.id.progressBar1);
							cirProgressBars.setMax(100);
							uploadingTextView = (TextView)verificationDialog.getWindow().findViewById(R.id.uploading);
							myHttpUtils.initTextView(uploadingTextView,cirProgressBars);
							verificationDialog.setCanceledOnTouchOutside(false);
							myHttpUtils.getHttpJsonString(params,
									Constans.authUrl, myHandler, context, 10,
									Constans.thod_Post_1);
							verificationDialog.dismiss();
						}
					});
					b_quxiao.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							verificationDialog.dismiss();
						}
					});
				} else {
					verificationDialog = new AlertDialog.Builder(context).create();
					verificationDialog.show();
					verificationDialog.getWindow().setContentView(
							R.layout.cust_diaolog_test);
					cirProgressBars = (CircleProgressBars) verificationDialog.getWindow().findViewById(R.id.progressBar1);
					uploadingTextView = (TextView)verificationDialog.getWindow().findViewById(R.id.uploading);
					verificationDialog.setCanceledOnTouchOutside(false);
					myHttpUtils.initTextView(uploadingTextView,cirProgressBars);
					myHttpUtils.getHttpJsonString(params, Constans.authUrl,
							myHandler, context, 10, Constans.thod_Post_1);
				}
			}
			break;
		case R.id.edit_id_date:
			showDialog(DATE_DIALOG_ID);
			break;
		default:
			break;
		}
	}

	// 需要定义弹出的DatePicker对话框的事件监听器：
	private DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			// 设置文本的内容：

			edit_id_date.setText(new StringBuilder().append(mYear).append("-")
					.append(mMonth + 1).append("-")// 得到的月份+1，因为从0开始
					.append(mDay));

		}
	};

	/**
	 * 当Activity调用showDialog函数时会触发该函数的调用：
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// CustomProgressDialog.stopProgressDialog();
			if (verificationDialog!=null) {
				verificationDialog.dismiss();
			}
			switch (msg.what) {
			case R.id.text_paizhao:
				paizhao(1);
				imageDialog.dismiss();
				break;
			case R.id.text_xiangce:
//				 xiangce(2);
				selectPicFromLocal(19);
				imageDialog.dismiss();
				break;
			case R.id.text_exit:
				imageDialog.dismiss();
				break;
			case 10:
				Utils.saveOrUpdatePersonal(personal, db);
				Constans.Toast(context, "上传成功" );
				Intent intent = new Intent();
				intent.putExtra("tag", "申请认证中");
				setResult(1000, intent);
				 finish();
			case 20:
				CustomProgressDialog.stopProgressDialog();
				JSONObject jsonObject = (JSONObject) msg.obj;
				try {
					JSONObject member = jsonObject.getJSONObject("member");
					personal.setDisplayName(Utils.getStringWithKey(
							"displayName", member));
					personal.setRealName(Utils.getStringWithKey("realName",
							member));
					personal.setGender(Utils.getStringWithKey("gender", member));
					personal.setIdNo(Utils.getStringWithKey("idNo", member));
					personal.setIdExpiredDate(Utils.getStringWithKey(
							"idExpiredDate", member));
					personal.setIdFrontImg(Utils.getStringWithKey("idFrontImg",
							member));
					personal.setInstituName(Utils.getStringWithKey(
							"instituName", member));
					personal.setTypeName(Utils.getStringWithKey("typeName",
							member));
					personal.setOfficeAddr(Utils.getStringWithKey("officeAddr",
							member));
					personal.setBadgeImg(Utils.getStringWithKey("badgeImg",
							member));
					personal.setBizCardImg(Utils.getStringWithKey("bizCardImg",
							member));
					setData();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		};
	};
	private Handler myHandler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case R.id.text_paizhao:
				paizhao(4);
				imageDialog2.dismiss();
				break;
			case R.id.text_xiangce:
				// xiangce(5);
				selectPicFromLocal(20);
				imageDialog2.dismiss();
				break;
			case R.id.text_exit:
				imageDialog2.dismiss();
				break;
			}
		};
	};

	private void sendPicByUri(Uri selectedImage, int i) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, null, null,
				null, null);
		String st8 = getResources().getString(R.string.cant_find_pictures);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			ImageUtil imageUtil = new ImageUtil(this);
			// Bitmap bitmap = imageUtil.loadImage(imagePath);
			photo = imageUtil.loadCacheImage(picturePath);
			if (i == 19) {
				// /storage/emulated/0/DCIM/Camera/IMG20150722174956.jpg
				// storage/emulated/0/linkGroup/record/images/20150810162322.png
				headImgFile = new File(picturePath);
				idFrontImg.setImageBitmap(photo);
			} else if (i == 20) {
				apply_image_file = new File(picturePath);
				apply_image.setImageBitmap(photo);
			} else if (i == 21) {
				card_image.setImageBitmap(photo);
			}

		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			try {
				
				photo = BitmapFactory.decodeStream(new FileInputStream(file
						.getAbsolutePath()));
//				String  lylString2 = BitmapUtil.createFile(photo);
				if (i == 19) {
					// /storage/emulated/0/DCIM/Camera/IMG20150722174956.jpg
					// storage/emulated/0/linkGroup/record/images/20150810162322.png
					headImgFile =file ;
					idFrontImg.setImageBitmap(photo);
				} else if (i == 20) {
					apply_image_file = file ; 
					apply_image.setImageBitmap(photo);
				} else if (i == 21) {
					card_image.setImageBitmap(photo);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private Handler myHandler3 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case R.id.text_paizhao:
				paizhao(7);
				imageDialog3.dismiss();
				break;
			case R.id.text_xiangce:
				// xiangce(8);
				selectPicFromLocal(21);
				imageDialog3.dismiss();
				break;
			case R.id.text_exit:
				imageDialog3.dismiss();
				break;
			}
		};
	};

	/*
	 * 进入系统拍照
	 */
	public void paizhao(int i) {
		/**
		 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
		 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
		 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
		 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
		 */
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
//				Environment.getExternalStorageDirectory(), "xiaoma.jpg")));
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
//				Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg")));

       camera = new File(PathUtil.getInstance().getImagePath(),
                ((LDApplication) LDApplication.getInstance()).getUserName()
                        + System.currentTimeMillis() + ".jpg");
        camera.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(camera)),
                i);

	}

	public void xiangce(int i) {
		/**
		 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
		 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
		 */
		Intent intent = new Intent(Intent.ACTION_PICK, null);

		/**
		 * 下面这句话，与其它方式写是一样的效果，如果：
		 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * intent.setType(""image/*");设置数据类型
		 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
		 * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
		 */
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		if (null != intent) {
			startActivityForResult(intent, i);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 2:
			if (null != data) {
				startPhotoZoom(data.getData(), 3);
			}
			break;
		case 19:
			if (data != null) {
				Uri selectedImage = data.getData();
				if (selectedImage != null) {
					sendPicByUri(selectedImage, 19);
				}
			}

			// List<ImageItem> incomingDataList = (List<ImageItem>) data
			// .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
			// if (incomingDataList != null) {
			// for (int i = 0; i < incomingDataList.size(); i++) {
			// // sendPicture(incomingDataList.get(i).sourcePath);
			// try {
			// photo = BitmapFactory.decodeStream(new FileInputStream(
			// incomingDataList.get(i).sourcePath));
			// idFrontImg.setImageBitmap(photo);
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// // /storage/emulated/0/linkGroup/record/images/20150508100559.png
			// // Uri lUri = incomingDataList.get(i);
			// // sendPicByUri(selectedImage);
			// // sendPicByUri(incomingDataList.get(i).sourcePath);
			// }
			// // mDataList.addAll(incomingDataList);
			// }
			break;
		case 20:
			if (data != null) {
				Uri selectedImage = data.getData();
				if (selectedImage != null) {
					sendPicByUri(selectedImage, 20);
				}
			}
			break;
		case 21:
			if (data != null) {
				Uri selectedImage = data.getData();
				if (selectedImage != null) {
					sendPicByUri(selectedImage, 21);
				}
			}
			break;
		case 105:
			if (!TextUtils.isEmpty(data.getStringExtra("cityname"))) {
				cityname = data.getStringExtra("cityname");
				cityName.setText(cityname);
				cityId = data.getStringExtra("cityId");
			}
			break;
		case 5:
			if (null != data) {
				startPhotoZoom(data.getData(), 6);
			}
			break;
		case 8:
			if (null != data) {
				startPhotoZoom(data.getData(), 9);
			}
			break;
		// 如果是调用相机拍照时
		case 1:
//			File temp = new File(Environment.getExternalStorageDirectory()
//					+ "/"+System.currentTimeMillis()+".jpg");
//			startPhotoZoom(Uri.fromFile(temp), 3);
			sendPicByUri(Uri.fromFile(camera), 19);
			break;
		case 4:
//			File temp2 = new File(Environment.getExternalStorageDirectory()
//					+ "/xiaoma.jpg");
//			startPhotoZoom(Uri.fromFile(temp2), 6);
			sendPicByUri(Uri.fromFile(camera), 20);
			break;
		case 7:
//			File temp3 = new File(Environment.getExternalStorageDirectory()
//					+ "/xiaoma.jpg");
//			startPhotoZoom(Uri.fromFile(temp3), 9);
			sendPicByUri(Uri.fromFile(camera), 21);
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				setPicToView(data, 3);
			}
			break;
		case 6:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				setPicToView(data, 6);
			}
			break;
		case 9:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				setPicToView(data, 9);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void selectPicFromLocal(int i) {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, i);
	}

	/*
	 * 截图
	 */
	public void startPhotoZoom(Uri uri, int i) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");

		// outputX outputY 是裁剪图片宽高
		if (i == 3) {
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 262);
			intent.putExtra("aspectY", 156);
			// intent.putExtra("outputX", 262);
			// intent.putExtra("outputY", 156);

			// intent.putExtra("aspectX",
			// getResources().getDimension(R.dimen.id_width));
			// intent.putExtra("aspectY",
			// getResources().getDimension(R.dimen.id_height));

			int x = (int) getResources().getDimension(R.dimen.id_width);
			int y = (int) getResources().getDimension(R.dimen.id_height);
			intent.putExtra("outputX", x);
			intent.putExtra("outputY", y);
		} else if (i == 6) {
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 208);
			intent.putExtra("aspectY", 316);
			// intent.putExtra("outputX", 207);
			// intent.putExtra("outputY", 316);
			int x = (int) getResources().getDimension(R.dimen.work_width);
			int y = (int) getResources().getDimension(R.dimen.work_height);
			intent.putExtra("outputX", x);
			intent.putExtra("outputY", y);
		} else if (i == 9) {
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 262);
			intent.putExtra("aspectY", 156);
			// intent.putExtra("outputX", 262);
			// intent.putExtra("outputY", 156);

			// intent.putExtra("aspectX",
			// getResources().getDimension(R.dimen.id_width));
			// intent.putExtra("aspectY",
			// getResources().getDimension(R.dimen.id_height));

			int x = (int) getResources().getDimension(R.dimen.id_width);
			int y = (int) getResources().getDimension(R.dimen.id_height);
			intent.putExtra("outputX", x);
			intent.putExtra("outputY", y);
		}
		intent.putExtra("return-data", true);
		startActivityForResult(intent, i);
	}

	private Bitmap photo;
	private Bitmap roundBitmap;

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata, int i) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			headImgPath = BitmapUtil.createFile(photo);
			if (i == 3) {
				// roundBitmap = toRoundBitmap(photo);
				/**
				 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
				 */
				// 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼
				// 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
				// 为我们可以用的图片类型就OK啦...吼吼
				headImgFile = new File(headImgPath);
				idFrontImg.setImageBitmap(photo);
			} else if (i == 6) {
				
				apply_image_file = new File(headImgPath);
				apply_image.setImageBitmap(photo);
			} else if (i == 9) {
				card_image_file = new File(headImgPath);
				card_image.setImageBitmap(photo);
			}
		}
	}

}
