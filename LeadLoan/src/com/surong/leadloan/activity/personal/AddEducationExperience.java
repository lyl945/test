package com.surong.leadloan.activity.personal;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.entity.Education;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.TimeUtils;

public class AddEducationExperience extends CommonActivity implements
		OnClickListener {

	private View view;
	private Context context;
	// private DbUtils dbUtils;
	private EditText school, major, education, eduDescribe;
	private MyHttpUtils myHttpUtils;
	private TextView timeStart, timeEnd, save;
	private AlertDialog dialog1;
	private Calendar mCalendar;
	private String schoolString, majorString, timeStartString, timeEndString,
			eduDescribeString, educationString;
	private Education data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// dbUtils = DbUtils.create(this);
		view = View.inflate(context, R.layout.add_education_experience, null);
		addContentView(view);
		data = (Education) getIntent().getSerializableExtra("education");
		if (data != null) {
			changeTitle("修改教育经历");
		} else {
			changeTitle("添加教育经历");
		}
		initView();
	}

	private void initView() {
		school = (EditText) view.findViewById(R.id.editText_school);
		major = (EditText) view.findViewById(R.id.editText_profession);
		education = (EditText) view.findViewById(R.id.editText_education);
		eduDescribe = (EditText) view
				.findViewById(R.id.editText_education_describe);
		timeStart = (TextView) view.findViewById(R.id.time_start1);
		timeEnd = (TextView) view.findViewById(R.id.time_end1);
		save = (TextView) view.findViewById(R.id.save);
		save.setOnClickListener(this);
		timeStart.setOnClickListener(this);
		timeEnd.setOnClickListener(this);
		mCalendar = Calendar.getInstance();
		if (data != null) {
			school.setText(data.getSchoolName());
			major.setText(data.getMajor());
			education.setText(data.getEducation());
			eduDescribe.setText(data.getDescription());
			timeStart.setText(data.getBeginDate());
			timeEnd.setText(data.getEndDate());
		}

	}

	// 调用 增加教育经历 接口
	private void addEduExperience() {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getString(context, "token"));
		params.addQueryStringParameter("beginDate", timeStartString);
		params.addQueryStringParameter("endDate", timeEndString);
		params.addQueryStringParameter("schoolName", schoolString);
		params.addQueryStringParameter("major", majorString);
		params.addQueryStringParameter("description", eduDescribeString);
		params.addQueryStringParameter("education", educationString);
		myHttpUtils.myInstance();
		if (data != null) {
			params.addQueryStringParameter("id", data.getId());
			myHttpUtils.getHttpJsonString(params,
					Constans.educationExperienceUpdateUrl, handler, context, 1,
					Constans.thod_Get_0);
		} else {

			myHttpUtils.getHttpJsonString(params,
					Constans.educationExperienceAddUrl, handler, context, 0,
					Constans.thod_Get_0);
		}
	}

	// 显示时间弹出框
	private void showDateDialog(final int type) {
		dialog1 = new AlertDialog.Builder(AddEducationExperience.this).create();
		dialog1.show();
		dialog1.getWindow().setContentView(R.layout.date_dialog);
		final DatePicker datePicker = (DatePicker) dialog1.getWindow()
				.findViewById(R.id.datePicker1);
		dialog1.getWindow().findViewById(R.id.comfirm)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mCalendar.set(Calendar.YEAR, datePicker.getYear());
						mCalendar.set(Calendar.MONTH, datePicker.getMonth());
						mCalendar.set(Calendar.DAY_OF_MONTH,
								datePicker.getDayOfMonth());
						String date = TimeUtils.transformToTime1(mCalendar
								.getTimeInMillis());
						Intent i1 = new Intent(AddEducationExperience.this,
								AddEducationExperience.class);
						i1.putExtra("date", date);
						startActivity(i1);
						dialog1.dismiss();

						switch (type) {
						case 0:
							timeStart.setText(date);
							break;
						case 1:
							timeEnd.setText(date);
						default:
							break;
						}
					}
				});
		dialog1.getWindow().findViewById(R.id.cancel)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog1.dismiss();
					}
				});

	}

	private boolean checkAll() {
		if (school.getText().toString().trim().equals("")) {
			Constans.Toast(context, "您的学校不能为空!");
			return false;
		} else if (major.getText().toString().trim().equals("")) {
			Constans.Toast(context, "您的专业不能为空!");
			return false;
		} else if (education.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请填写您的学历");
			return false;
		} else if (timeStart.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请选择起始时间");
			return false;
		} else if (timeEnd.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请选择终止时间");
			return false;
		} else if (eduDescribe.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请填写您的教育经历");
			return false;
		}
		return true;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			schoolString = school.getText().toString().trim();
			majorString = major.getText().toString().trim();
			timeStartString = timeStart.getText().toString().trim();
			timeEndString = timeEnd.getText().toString().trim();
			eduDescribeString = eduDescribe.getText().toString().trim();
			educationString = education.getText().toString().trim();

			if (checkAll()) {
				addEduExperience();
				finish();
			}

			break;

		case R.id.time_start1:
			showDateDialog(0);
			break;

		case R.id.time_end1:
			showDateDialog(1);
			break;

		default:
			break;
		}

	}

	// 服务器返回数据处理
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				JSONObject object = (JSONObject) msg.obj;
				LogUtils.d("object=" + object.toString());
				try {
					int status = object.getInt("code");
					if (status == 0) {
						Constans.Toast(context, "添加成功");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				JSONObject object1 = (JSONObject) msg.obj;
				try {
					int status = object1.getInt("code");
					if (status == 0) {
						Constans.Toast(context, "修改成功");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		};
	};

}
