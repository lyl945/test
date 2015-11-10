package com.surong.leadloan.activity.personal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
public class FinishPersonalInformation extends CommonActivity implements
		OnClickListener {
	private View view;
	private Context context;
	private Personal personal;
	private WorkExperience we;
	private Button btn_sure;
	private TextView workTime, thisTime, addWork, addEducation;
	private MyHttpUtils myHttpUtils;
	private List<String> workTimes = new ArrayList<String>();
	private AlertDialog timesDialog;
	private int index;
	public static int refresh;
	private AutoLineFeedLayout hobbyAutoLayout; // 兴趣爱好表单
	private AutoLineFeedLayout vocationLayout; // 职业标签表单
	private EditText level, email, weixin, qq;
	Map<String, String> hobby = new HashMap<String, String>();// 兴趣爱好
	Map<String, String> vocation = new HashMap<String, String>();// 职业标签
	LinkedHashMap<String, String> timesMap;
	LinkedHashMap<String, String> timesMap1;
	private ImageFetcher mImageFetcher;
	private ImageView headIcon;
	private ShowDialog imageDialog;
	private String headImgPath;// 头像图片路径
	private File headImgFile;
	private String hobbyString, vocationString, qqString, weixinString,
			emailString, instituteString, nameString, workTimeString,
			thisTimeString;
	private TextView name, institute;
	private LinearLayout educationListLayout, workLayout;

	private NonScrollableListView mNonScrollableListView;

	List<Education> educationList = new ArrayList<Education>();
	List<WorkExperience> workList = new ArrayList<WorkExperience>();
	private Bitmap bitmaps;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.surong.leadloan.activity.CommonActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View
				.inflate(context, R.layout.finish_personal_information, null);
		addContentView(view);
		changeTitle("完善个人资料");
		myHttpUtils.instance();
		getExperience();
		initView();
		initData();
		initMybutton();

	}

	@Override
	protected void onResume() {
		super.onResume();
		getExperience();
	}

	// @Override
	// protected void onStart() {
	// super.onStart();
	// getExperience();
	// }

	private void initView() {
		workLayout = (LinearLayout) view.findViewById(R.id.work_list);
		educationListLayout = (LinearLayout) view
				.findViewById(R.id.education_list);
		headIcon = (ImageView) view.findViewById(R.id.head_image);
		headIcon.setOnClickListener(this);
		addWork = (TextView) view.findViewById(R.id.add_work_experience);
		addEducation = (TextView) view
				.findViewById(R.id.add_education_experience);
		addWork.setOnClickListener(this);
		addEducation.setOnClickListener(this);

		btn_sure = (Button) view.findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		addWork.setOnClickListener(this);
		addEducation.setOnClickListener(this);

		name = (TextView) view.findViewById(R.id.name);
		institute = (TextView) view.findViewById(R.id.institute);
		email = (EditText) view.findViewById(R.id.email);
		weixin = (EditText) view.findViewById(R.id.weixin);
		qq = (EditText) view.findViewById(R.id.qq);
		workTime = (TextView) view.findViewById(R.id.work_time);
		thisTime = (TextView) view.findViewById(R.id.this_time);
		workTime.setOnClickListener(this);
		thisTime.setOnClickListener(this);

	}

	/**
	 * @param 添加教育经历
	 */
	private void addEducationview(List<Education> list) {

		educationListLayout.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.education_experience_item, null);
			TextView schoolName = (TextView) v.findViewById(R.id.item_school);
			TextView degree = (TextView) v.findViewById(R.id.item_degree);
			TextView startTime = (TextView) v.findViewById(R.id.time_start);
			TextView endTime = (TextView) v.findViewById(R.id.time_end);
			ImageView redDot = (ImageView) v.findViewById(R.id.red_dot);
			View line = v.findViewById(R.id.line);
			View horizonLine = v.findViewById(R.id.horizon_line);
			if (i == 0) {
				redDot.setVisibility(View.VISIBLE);
				horizonLine.setVisibility(View.GONE);
			} else {
				horizonLine.setVisibility(View.VISIBLE);
				redDot.setVisibility(View.INVISIBLE);
			}
			if (i == list.size() - 1) {
				redDot.setVisibility(View.INVISIBLE);
				line.setVisibility(View.INVISIBLE);
			} else {
				line.setVisibility(View.VISIBLE);
			}
			final Education edu = list.get(i);
			schoolName.setText(edu.getSchoolName());
			degree.setText(edu.getMajor() + "," + edu.getEducation());
			startTime.setText(edu.getBeginDate().substring(0, 4));
			endTime.setText(edu.getEndDate().substring(0, 4));
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					new AlertDialog.Builder(FinishPersonalInformation.this)

							.setPositiveButton("删除",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											deleteEducation(edu.getId());
											getExperience();
										}
									})
							.setNegativeButton("修改",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent iw = new Intent(
													getApplicationContext(),
													AddEducationExperience.class);
											iw.putExtra("education", edu);
											startActivity(iw);
										}
									}).show();
				}
			});

			educationListLayout.addView(v);
		}

	}

	/**
	 * @param 添加工作经历
	 */
	private void addWorkview(List<WorkExperience> list) {

		workLayout.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			View v = LayoutInflater.from(context).inflate(
					R.layout.work_experience_item, null);
			TextView company = (TextView) v.findViewById(R.id.item_company);
			TextView job = (TextView) v.findViewById(R.id.item_job);
			TextView description = (TextView) v
					.findViewById(R.id.item_description);
			TextView startTime = (TextView) v.findViewById(R.id.time_start);
			TextView endTime = (TextView) v.findViewById(R.id.time_end);
			ImageView redDot = (ImageView) v.findViewById(R.id.red_dot);
			View line = v.findViewById(R.id.line);
			View horizonLine = v.findViewById(R.id.horizon_line);
			if (i == 0) {
				redDot.setVisibility(View.VISIBLE);
				horizonLine.setVisibility(View.GONE);
			} else {
				horizonLine.setVisibility(View.VISIBLE);
				redDot.setVisibility(View.INVISIBLE);
			}
			if (i == list.size() - 1) {
				redDot.setVisibility(View.INVISIBLE);
				line.setVisibility(View.INVISIBLE);
			} else {
				line.setVisibility(View.VISIBLE);
			}

			final WorkExperience we = list.get(i);
			company.setText(we.getEnterprise());
			job.setText(we.getJob());
			description.setText(we.getDescription());
			startTime.setText(we.getBeginDate().substring(0, 4));
			endTime.setText(we.getEndDate().substring(0, 4));
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					new AlertDialog.Builder(FinishPersonalInformation.this)
							.setPositiveButton("删除",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											deleteWork(we.getId());
											getExperience();
										}
									})
							.setNegativeButton("修改",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent iw = new Intent(
													getApplicationContext(),
													AddworkExperience.class);
											iw.putExtra("workExperience", we);
											startActivity(iw);
										}
									}).show();
				}

			});

			workLayout.addView(v);
		}

	}

	/**
	 * @param 删除教育经历
	 */
	private void deleteEducation(String id) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getString(context, "token"));
		params.addQueryStringParameter("id", id);
		myHttpUtils.getHttpJsonString(params,
				Constans.educationExperienceDeleteUrl, handler, context, 2,
				Constans.thod_Get_0);
	}

	/**
	 * @param 删除工作经历
	 */
	private void deleteWork(String id) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getString(context, "token"));
		params.addQueryStringParameter("id", id);
		myHttpUtils.getHttpJsonString(params, Constans.workExperienceDeleteUrl,
				handler, context, 2, Constans.thod_Get_0);
	}

	private void initData() {
		mImageFetcher = ImageFetcher.Instance(context, 0);
		imageDialog = new ShowDialog(this, myHandler);
		// 从业时间的map
		timesMap = new LinkedHashMap<String, String>();
		timesMap.put("一年整", "1");
		timesMap.put("两年整", "2");
		timesMap.put("三年整", "3");
		timesMap.put("四年整", "4");
		timesMap.put("五年整", "5");
		timesMap.put("六年整", "6");
		timesMap.put("七年整", "7");
		timesMap.put("八年整", "8");
		timesMap.put("九年整", "9");
		timesMap.put("十年整及十年以上", "10");

		timesMap1 = new LinkedHashMap<String, String>();
		timesMap1.put("1", "一年整");
		timesMap1.put("2", "两年整");
		timesMap1.put("3", "三年整");
		timesMap1.put("4", "四年整");
		timesMap1.put("5", "五年整");
		timesMap1.put("6", "六年整");
		timesMap1.put("7", "七年整");
		timesMap1.put("8", "八年整");
		timesMap1.put("9", "九年整");
		timesMap1.put("10", "十年整及十年以上");

		Iterator<Entry<String, String>> iterator = timesMap1.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			workTimes.add(entry.getValue());
		}
		try {
			// 从缓存中获取数据
			personal = CommonActivity.db.findFirst(Personal.class);
			if (personal != null) {
				name.setText(personal.getDisplayName());
				institute.setText(personal.getInstituName());
				if (personal.getWorkingTime() != null) {
					workTime.setText(timesMap1.get(personal.getWorkingTime()));
				}
				if (personal.getInstituTermOfOffice() != null) {
					thisTime.setText(timesMap1.get(personal
							.getInstituTermOfOffice()));
				}
				if (personal.getHeadImgPath() != null) {
					mImageFetcher.addTask(headIcon, personal.getHeadImgPath(),
							1);
					headIcon.setTag(personal.getBizCardImg());
				}
				email.setText(personal.getEmail());
				qq.setText(personal.getQq());
				weixin.setText(personal.getWebChat());
				if (personal.getHobby() != null) {
					String[] hobbyArray = personal.getHobby().split(";");
					for (int i = 0; i < hobbyArray.length; i++) {
						String[] s = hobbyArray[i].split("-");
						if (s.length > 1) {
							hobby.put(s[0], s[1]);
						} else
							hobby.put(s[0], "");
					}
				}
				if (personal.getTag() != null) {
					String[] tagArray = personal.getTag().split(";");
					Log.e("123", "tagArray=" + tagArray);
					for (int i = 0; i < tagArray.length; i++) {
						String[] s1 = tagArray[i].split("-");
						if (s1.length > 1) {
							vocation.put(s1[0], s1[1]);
						} else
							vocation.put(s1[0], "");
					}
				}

			}

		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param 获取
	 *            (工作,教育)经历
	 */
	private void getExperience() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getString(context, "token"));
		myHttpUtils.getHttpJsonString(params, Constans.userDetailUrl, handler,
				context, 0, Constans.thod_Get_0);
	}

	private void initMybutton() {
		hobbyAutoLayout = (AutoLineFeedLayout) view.findViewById(R.id.hobby);

		hobbyAutoLayout.removeAllViews();
		// 从字典中获取兴趣爱好数据
		Map<String, String> hobbyMap = MyApplication.dictionaryMap2
				.get("hobby");
		Iterator<Entry<String, String>> iterator = hobbyMap.entrySet()
				.iterator();
		// 添加兴趣爱好button集合
		while (iterator.hasNext()) {
			MyButton button = (MyButton) View
					.inflate(context, R.layout.c, null);
			button.setTextColor(Color.argb(255, 51, 51, 51));
			Entry<String, String> entry = iterator.next();
			button.setId(Integer.valueOf(entry.getValue()));
			button.setText(entry.getKey());
			if (hobby.get(entry.getValue()) != null) {
				button.setBackgroundResource(R.drawable.shape_red_bg);
				button.setTextColor(Color.rgb(38, 71, 103));
				button.setTag("true");
			}

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Button button = (Button) v;
					if ("true".equals(v.getTag())) {
						v.setTag("false");
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.shape_bg));
						button.setTextColor(Color.argb(255, 51, 51, 51));

					} else {
						v.setTag("true");
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.shape_red_bg));
						button.setTextColor(Color.rgb(38, 71, 103));
					}
				}
			});

			hobbyAutoLayout.addView(button);
		}

		/**
		 * 职业标签
		 */
		vocationLayout = (AutoLineFeedLayout) view
				.findViewById(R.id.vocation_layout);
		Map<String, String> vocationMap = MyApplication.dictionaryMap2
				.get("tag");
		Iterator<Entry<String, String>> iterator1 = vocationMap.entrySet()
				.iterator();
		while (iterator1.hasNext()) {
			MyButton button1 = (MyButton) View.inflate(context, R.layout.c,
					null);
			button1.setTextColor(Color.argb(255, 51, 51, 51));
			Entry<String, String> entry = iterator1.next();
			button1.setId(Integer.valueOf(entry.getValue()));
			button1.setText(entry.getKey());
			if (vocation.get(entry.getValue()) != null) {
				button1.setBackgroundResource(R.drawable.shape_red_bg);
				button1.setTextColor(Color.rgb(38, 71, 103));
				button1.setTag("true");
			}
			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Button button = (Button) v;
					if ("true".equals(v.getTag())) {
						v.setTag("false");
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.shape_bg));
						button.setTextColor(Color.argb(255, 51, 51, 51));

					} else {
						v.setTag("true");
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.shape_red_bg));
						button.setTextColor(Color.rgb(38, 71, 103));
					}
				}
			});
			vocationLayout.addView(button1);

		}

	}

	/**
	 * 修改个人信息方法
	 */
	private void updateUserData() {
		// 获得选中的兴趣爱好
		hobbyString = "";
		for (int i = 0; i < hobbyAutoLayout.getChildCount(); i++) {
			MyButton button = (MyButton) hobbyAutoLayout.getChildAt(i);
			if ("true".equals(button.getTag())) {
				hobbyString += button.getId() + ";";
			}
		}
		if (hobbyString.length() > 1) {
			hobbyString = hobbyString.substring(0, hobbyString.length() - 1);
		}
		// 获得选中的职业标签
		vocationString = "";
		for (int i = 0; i < vocationLayout.getChildCount(); i++) {
			MyButton button1 = (MyButton) vocationLayout.getChildAt(i);
			if ("true".equals(button1.getTag())) {
				vocationString += button1.getId() + ";";
			}
		}
		if (vocationString.length() > 1) {
			vocationString = vocationString.substring(0,
					vocationString.length() - 1);
		}

		qqString = qq.getText().toString().trim();
		weixinString = weixin.getText().toString().trim();
		emailString = email.getText().toString().trim();
		workTimeString = workTime.getText().toString().trim();
		thisTimeString = thisTime.getText().toString().trim();
		nameString = name.getText().toString().trim();
		instituteString = institute.getText().toString().trim();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getString(context, "token"));
		params.addQueryStringParameter("qq", qqString);
		params.addQueryStringParameter("webChat", weixinString);
		params.addQueryStringParameter("email", emailString);
		try {
			if (workTimeString != null) {
				params.addQueryStringParameter("workingTime",
						timesMap.get(workTimeString).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (thisTimeString != null) {
				params.addQueryStringParameter("instituTermOfOffice", timesMap
						.get(thisTimeString).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		params.addQueryStringParameter("hobby", hobbyString);
		params.addQueryStringParameter("tag", vocationString);
		if (headImgFile != null) {
			params.addBodyParameter("headImgFile", headImgFile);
		}
		CustomProgressDialog.startProgressDialog(context);
		myHttpUtils.getHttpJsonString(params, Constans.registerOne, handler,
				context, 1, Constans.thod_Post_1);

	}

	/**
	 * @param 弹出的从业时间的dialog
	 */
	private void showTimeDialog(final int type) {
		timesDialog = new AlertDialog.Builder(FinishPersonalInformation.this)
				.create();
		timesDialog.show();
		timesDialog.getWindow().setContentView(R.layout.times_dialog);
		ListView listView = (ListView) timesDialog.getWindow().findViewById(
				R.id.listview);

		class TimesAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return workTimes.size();
			}

			@Override
			public Object getItem(int arg0) {
				return workTimes.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder1 viewHolder1 = null;
				final int pos = position;
				if (convertView == null) {
					viewHolder1 = new ViewHolder1();
					convertView = LayoutInflater.from(context).inflate(
							R.layout.times_item, null);
					viewHolder1.itemContent = (TextView) convertView
							.findViewById(R.id.item_content);
					convertView.setTag(viewHolder1);
				} else {
					viewHolder1 = (ViewHolder1) convertView.getTag();
				}
				viewHolder1.itemContent.setText(workTimes.get(position));
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						index = pos;

						switch (type) {
						case 1:
							workTime.setText(workTimes.get(pos));

							break;
						case 2:
							thisTime.setText(workTimes.get(pos));
							break;
						default:
							break;
						}
						timesDialog.dismiss();
					}
				});
				return convertView;
			}
		}
		listView.setAdapter(new TimesAdapter());

	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bitmap photo;
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			headImgPath = BitmapUtil.createFile(photo);
			headImgFile = new File(headImgPath);
			headIcon.setImageBitmap(photo);
		}
	}

	static class ViewHolder {
		TextView company, job, description, endTime, startTime;
	}

	static class ViewHolder1 {
		TextView itemContent;
	}

	// 数据处理
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			CustomProgressDialog.stopProgressDialog();
			switch (msg.what) {
			case 0:
				JSONObject object = (JSONObject) msg.obj;
				try {
					if (!object.isNull("workExperience")) {
						String workString = object.getString("workExperience");
						List<WorkExperience> list1 = Analyze
								.analyzeWorkExperience(workString);
						workList.clear();
						workList.addAll(list1);
						addWorkview(workList);
						workLayout.setVisibility(View.VISIBLE);
					} else {
						workLayout.setVisibility(View.GONE);
					}
					if (!object.isNull("educationExperience")) {
						String educationString = object
								.getString("educationExperience");
						List<Education> list2 = Analyze
								.analyzeEducationExperience(educationString);
						educationList.clear();
						educationList.addAll(list2);
						addEducationview(educationList);
						educationListLayout.setVisibility(View.VISIBLE);
					} else {
						educationListLayout.setVisibility(View.GONE);
					}

				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				break;

			case 1:
				JSONObject ob1 = (JSONObject) msg.obj;
				try {
					int code = ob1.getInt("code");
					if (code == 0) {
						Constans.Toast(FinishPersonalInformation.this, "修改成功");
						// 保存数据到本地
						personal.setHobby(hobbyString);
						personal.setTag(vocationString);
						personal.setQq(qqString);
						personal.setEmail(emailString);
						personal.setWebChat(weixinString);
						personal.setDisplayName(nameString);
						personal.setInstituName(instituteString);
						try {
							if (workTimeString != null) {
								personal.setWorkingTime(timesMap.get(
										workTimeString).toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							if (thisTimeString != null) {
								personal.setInstituTermOfOffice(timesMap.get(
										thisTimeString).toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (ob1.getString("headImgPath") != null) {
							personal.setHeadImgPath(ob1
									.getString("headImgPath"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Utils.saveOrUpdatePersonal(personal, db);
				finish();
				break;

			case 2:
				JSONObject ob2 = (JSONObject) msg.obj;
				try {
					int code = ob2.getInt("code");
					if (code == 0) {
						Constans.Toast(FinishPersonalInformation.this, "删除成功");
						getExperience();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
			case R.id.text_addphoto:
				startActivityForResult(new Intent(
						FinishPersonalInformation.this,
						DefaultAvatarActivity.class), 1005);
				imageDialog.dismiss();
				break;
			case R.id.text_exit:
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if (null != data) {
				startPhotoZoom(data.getData());
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			try {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/xiaoma.jpg");
				FileInputStream fis = new FileInputStream(temp);
				int length = fis.available();
				startPhotoZoom(Uri.fromFile(temp));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			if (data != null) {
				if (null != data.getExtras().getString("head")) {
					loadHeadimg(data.getExtras().getString("head"));
				}
			}
			break;

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
		headIcon.setImageBitmap(bitmaps);
		headImgPath = BitmapUtil.createFile(bitmaps);
		headImgFile = new File(headImgPath);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.relative_back:// 返回
			finish();
			break;
		case R.id.btn_sure:// 确定按钮
			if (checkAll()) {
				if (!ClickUtils.isFastDoubleClick()) {
					updateUserData();
				}
			}
			break;

		case R.id.add_work_experience:// 添加工作经历
			Intent i1 = new Intent(this, AddworkExperience.class);
			startActivity(i1);
			break;

		case R.id.add_education_experience:// 添加教育经历
			Intent i2 = new Intent(this, AddEducationExperience.class);
			startActivity(i2);

			break;
		case R.id.work_time:// 从业时间
			showTimeDialog(1);
			break;

		case R.id.this_time:// 本机构任职时间
			showTimeDialog(2);
			break;
		case R.id.head_image:
			View view1 = View.inflate(context, R.layout.headphoto_dialog, null);
			imageDialog.createDialog(view1, new int[] { R.id.text_addphoto,
					R.id.text_paizhao, R.id.text_xiangce, R.id.text_exit },
					ShowDialog.TYPE_BOTTOM);
			break;
		default:
			break;
		}

	}

	private boolean checkAll() {
		if (!TextUtils.isEmpty(email.getText().toString())) {
			if (!Constans.isEmailNO(email.getText().toString())) {
				Constans.Toast(this, "电子邮箱码格式有误");
				return false;
			}
		}
		if (!TextUtils.isEmpty(weixin.getText().toString())) {
			if (!Constans.isweiXinNO(weixin.getText().toString())) {
				Constans.Toast(this, "请核对您的微信号");
				return false;
			}
		}
		if (!TextUtils.isEmpty(qq.getText().toString())) {
			if (!Constans.isQQNO(qq.getText().toString())) {
				Constans.Toast(this, "请核对您的qq号");
				return false;
			}
		}
		return true;
	}

}
