package com.surong.leadloan.start;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.easemob.chatuidemo.res.EASEConstants;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.contact_group.PeerActivity;
import com.surong.leadloan.activity.personal.DefaultAvatarActivity;
import com.surong.leadloan.activity.personal.FinishPersonalInformation;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.ui.ShowDialog;
import com.surong.leadloan.utils.BitmapUtil;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;

public class RegisterCompleteOneActivity extends CommonActivity implements
		OnClickListener, TextWatcher {

	private View view;
	private ImageView person_icon;
	private ShowDialog imageDialog;
	private Context context;
	private String headImgPath;// 头像图片路径
	private MyHttpUtils myHttpUtils;
	private Button btn_next;// 下一步
	private TextView instituteText;// 机构名称
	private EditText display_nameEditText;// 真是姓名
	private RadioGroup radioGroup;// 入驻方式
	private final int flag_register_one = 0;
	// 注册相关字段
	private String token;
	private File headImgFile;
	private String displayName;
	private String instituName;
	private String stdFlag;
	private Personal personal;
	private RelativeLayout back;
	private EditText ediText_pwd;
	private String cityId = "440300";
	private String cityname = "深圳市";
	private Bitmap bitmaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.register_one, null);
		context = this;
		addContentView(view);
		changeTitle("完善资料");
		findView();
		initData();
		initAction();

	}

	private void findView() {
		back = this.relative_back;
		ediText_pwd = (EditText) view.findViewById(R.id.ediText_pwd);
		instituteText = (TextView) view.findViewById(R.id.auto_institute);
		person_icon = (ImageView) view.findViewById(R.id.person_icon);
		display_nameEditText = (EditText) view.findViewById(R.id.display_name);
		btn_next = (Button) view.findViewById(R.id.btn_next);
		radioGroup = (RadioGroup) view.findViewById(R.id.get_way);
		findViewById(R.id.right).setVisibility(View.GONE);

	}

	private void initData() {
		try {
			getlocal();
		} catch (DbException e) {
			personal = new Personal();
		}
		imageDialog = new ShowDialog(this, myHandler);
		myHttpUtils = MyHttpUtils.myInstance();
		displayName = display_nameEditText.getText().toString().trim();
		instituName = instituteText.getText().toString().trim();
		/*
		 * String[] books = new String[] { "rollen", "rollenholt", "rollenren",
		 * "roll" }; ArrayAdapter<String> av = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_dropdown_item_1line, books);
		 */

	}

	private void getlocal() throws DbException {
		personal = CommonActivity.db.findFirst(Personal.class);
	}

	private void initAction() {
		ediText_pwd.setOnClickListener(this);
		back.setOnClickListener(this);
		person_icon.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		instituteText.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (group.getCheckedRadioButtonId()) {
				case R.id.radio_0:
					stdFlag = "" + 0;
					break;
				case R.id.radio_1:
					stdFlag = "" + 1;
					break;

				default:
					break;
				}
			}
		});
		instituteText.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_icon:
			View view1 = View.inflate(context, R.layout.headphoto_dialog, null);
			imageDialog
					.createDialog(view1, new int[] {  R.id.text_addphoto,R.id.text_paizhao,
							R.id.text_xiangce, R.id.text_exit },
							ShowDialog.TYPE_BOTTOM);

			break;
		case R.id.relative_back:
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addCategory(Intent.CATEGORY_HOME);
			startActivity(i);

			break;
		case R.id.ediText_pwd:
			Intent ii = new Intent(RegisterCompleteOneActivity.this,
					CitySelectActivity2.class);
			ii.putExtra("cityname", cityname);
			// startActivity(new
			// Intent(PeerActivity.this,CitySelectActivity.class));
			startActivityForResult(ii, 105);

			break;
		case R.id.btn_next:
			displayName = display_nameEditText.getText().toString().trim();
			instituName = instituteText.getText().toString().trim();
			RequestParams params = new RequestParams();
			token = SharedPreferencesHelp.getString(context, "token");
			if (getAll()) {
				CustomProgressDialog.startProgressDialog(this);
				params.addQueryStringParameter("token", token);
				params.addQueryStringParameter("displayName", displayName);
				params.addQueryStringParameter("instituName", instituName);
				params.addQueryStringParameter("cityId", cityId);
				params.addQueryStringParameter("stdFlag", stdFlag);
				if (headImgFile != null) {
					params.addBodyParameter("headImgFile", headImgFile);
				}
				myHttpUtils.getHttpJsonString(params, Constans.registerOne,
						handler, context, flag_register_one,
						Constans.thod_Post_1);

			}

			break;
		case R.id.auto_institute:
			Intent intent = new Intent();
			intent.setClass(RegisterCompleteOneActivity.this,
					InstituteActivity.class);
			startActivityForResult(intent, 10001);
			break;
		default:
			break;
		}

	}

	private boolean getAll() {

		if (display_nameEditText.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请填写您的显示名");
			return false;
		}
		if (instituteText.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请选择任职机构");
			return false;
		}
		if (null == headImgFile) {
			// 设置头像非必填项,默认为当前头像
			/*
			 * Constans.Toast(context, "请设置头像信息"); return false;
			 */
			Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.head);

			headImgPath = BitmapUtil.createFile(rawBitmap);
			headImgFile = new File(headImgPath);
		}

		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case flag_register_one:
				JSONObject object = (JSONObject) msg.obj;
				CustomProgressDialog.stopProgressDialog();
				try {
					headImgPath = object.getString("headImgPath");
					personal.setDisplayName(displayName);
					personal.setCityName(cityname);
					personal.setCityId(cityId);
					personal.setDisplayName(displayName);
					personal.setInstituName(instituName);
					personal.setHeadImgPath(headImgPath);
					Utils.saveOrUpdatePersonal(personal, CommonActivity.db);
					String token = object.getString("token");
					LDApplication.getInstance().addSessionData(
							EASEConstants.TOKEN, token);
					SharedPreferencesHelp.SavaString(context, "token", token);
					Utils.saveOrUpdateEaseUser(object.getJSONObject("member"),
							context);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(new Intent(RegisterCompleteOneActivity.this,
						RegisterCompleteTwoActivity.class));
				finish();
				break;

			default:
				break;
			}

		};
	};
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case R.id.text_paizhao:
				paizhao();
				imageDialog.dismiss();
				break;
			case R.id.text_xiangce:
				xiangce();
				imageDialog.dismiss();
				break;
			case R.id.text_exit:
				imageDialog.dismiss();
				break;
			case R.id.text_addphoto:
				startActivityForResult(new Intent(
						RegisterCompleteOneActivity.this,
						DefaultAvatarActivity.class), 1005);
				imageDialog.dismiss();
				break;
			}
		};
	};

	/*
	 * 进入系统拍照
	 */
	public void paizhao() {
		/**
		 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
		 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
		 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
		 * 官方文档太长了就不看了，其实是错的，这个地方小马也错了，必须改正
		 */
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), "xiaoma.jpg")));
		startActivityForResult(intent, 2);
	}

	public void xiangce() {
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
			startActivityForResult(intent, 1);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if (null != data) {
				startPhotoZoom(data.getData());
			}
			break;
		case 105:

			// if (data == null) {
			//
			// return;
			if (!TextUtils.isEmpty(data.getStringExtra("cityname"))) {
				cityname = data.getStringExtra("cityname");
				cityId = data.getStringExtra("cityId");
				ediText_pwd.setText("您所在的城市:" + cityname);
			}

			break;
		// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/xiaoma.jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		case 1005:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (null != data.getExtras().getString("head")) {
				loadHeadimg(data.getExtras().getString("head"));
			}
			break;
		case 10001:
			if (data != null) {
				Bundle bundle = data.getExtras();
				instituteText.setText(bundle.getString("institute"));
			}

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void loadHeadimg(String headString) {

		if (headString.equals("1")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead1);
		}
		if (headString.equals("2")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead2);
		}
		if (headString.equals("3")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead3);
		}
		if (headString.equals("4")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead4);
		}
		if (headString.equals("5")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead5);
		}
		if (headString.equals("6")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead6);
		}
		if (headString.equals("7")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead7);
		}
		if (headString.equals("8")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead8);
		}
		if (headString.equals("9")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead9);
		}
		if (headString.equals("10")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead10);
		}
		if (headString.equals("11")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead11);
		}
		if (headString.equals("12")) {
			bitmaps = BitmapFactory.decodeResource(getResources(),
					R.drawable.mohead12);
		}
		person_icon.setImageBitmap(bitmaps);
		headImgPath = BitmapUtil.createFile(bitmaps);
		headImgFile = new File(headImgPath);
	}

	/*
	 * 截图
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 4);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 261);
		intent.putExtra("outputY", 261);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	private Bitmap photo;
	private Bitmap roundBitmap;

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			roundBitmap = toRoundBitmap(photo);

			/**
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
			 */
			// 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼
			// 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼
			headImgPath = BitmapUtil.createFile(roundBitmap);
			headImgFile = new File(headImgPath);

			/*
			 * RequestParams params = new RequestParams();
			 * params.addQueryStringParameter("token", token);
			 * myHttpUtils.getHttpJsonString(params, path, handler, context,
			 * flag, thod)
			 * PersonalHttpServiceBean.instance().httpUpdateHeadImage(token,
			 * file, httpHandler, context);
			 */
			person_icon.setImageBitmap(roundBitmap);
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

	public void onBackPressed() {
		// 实现Home键效果
		// super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		btn_next.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.next_button1));

	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
