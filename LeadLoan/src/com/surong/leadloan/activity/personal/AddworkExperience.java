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
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.entity.WorkExperience;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.TimeUtils;

public class AddworkExperience extends CommonActivity implements
		OnClickListener {

	private View view;
	private Context context;
	// private DbUtils dbUtils;
	private EditText company, job, workDescribe;
	private MyHttpUtils myHttpUtils;
	private TextView timeStart, timeEnd, save;
	private AlertDialog dialog;
	private Calendar mCalendar;
	private String companyString, jobString, timeStartString, timeEndString,
			workDescribeString;
	private WorkExperience data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// dbUtils = DbUtils.create(this);
		view = View.inflate(context, R.layout.add_work_experience, null);
		addContentView(view);

		myHttpUtils.myInstance();
		data = (WorkExperience) getIntent().getSerializableExtra(
				"workExperience");
		if (data != null) {
			changeTitle("修改工作经历");
		} else {
			changeTitle("添加工作经历");
		}

		init();
	}

	private void init() {
		company = (EditText) view.findViewById(R.id.editText_company);
		job = (EditText) view.findViewById(R.id.editText_job);
		workDescribe = (EditText) view.findViewById(R.id.editText_job_describe);
		timeStart = (TextView) view.findViewById(R.id.time_start);
		timeEnd = (TextView) view.findViewById(R.id.time_end);
		save = (TextView) view.findViewById(R.id.save);
		timeStart.setOnClickListener(this);
		timeEnd.setOnClickListener(this);
		save.setOnClickListener(this);
		mCalendar = Calendar.getInstance();

		if (data != null) {
			company.setText(data.getEnterprise());
			job.setText(data.getJob());
			timeStart.setText(data.getBeginDate());
			timeEnd.setText(data.getEndDate());
			workDescribe.setText(data.getDescription());
		}

	}

	private boolean checkAll() {

		if (company.getText().toString().trim().equals("")) {
			Constans.Toast(context, "您的公司不能为空!");
			return false;
		} else if (job.getText().toString().trim().equals("")) {
			Constans.Toast(context, "您的职位不能为空!");
			return false;
		} else if (timeStart.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请选择起始时间");
			return false;
		} else if (timeEnd.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请选择终止时间");
			return false;
		} else if (workDescribe.getText().toString().trim().equals("")) {
			Constans.Toast(context, "请填写您的工作经历");
			return false;
		}
		return true;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			companyString = company.getText().toString().trim();
			jobString = job.getText().toString().trim();
			timeStartString = timeStart.getText().toString().trim();
			timeEndString = timeEnd.getText().toString().trim();
			workDescribeString = workDescribe.getText().toString().trim();

			if (checkAll()) {
				addWorkExperience();
			}
			break;

		case R.id.time_start:
			showDateDialog(0);
			break;

		case R.id.time_end:
			showDateDialog(1);
			break;

		default:
			break;
		}

	}

	private void addWorkExperience() {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token",
				SharedPreferencesHelp.getString(context, "token"));
		params.addQueryStringParameter("beginDate", timeStartString);
		params.addQueryStringParameter("endDate", timeEndString);
		params.addQueryStringParameter("enterprise", companyString);
		params.addQueryStringParameter("job", jobString);
		params.addQueryStringParameter("description", workDescribeString);
		if (data != null) {
			params.addQueryStringParameter("id", data.getId());
			myHttpUtils.getHttpJsonString(params,
					Constans.workExperienceUpdateUrl, handler, context, 1,
					Constans.thod_Get_0);
		} else {

			myHttpUtils.getHttpJsonString(params,
					Constans.workExperienceAddUrl, handler, context, 0,
					Constans.thod_Get_0);
		}
	}

	private void showDateDialog(final int type) {
		dialog = new AlertDialog.Builder(AddworkExperience.this).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.date_dialog);
		final DatePicker datePicker = (DatePicker) dialog.getWindow()
				.findViewById(R.id.datePicker1);
		dialog.getWindow().findViewById(R.id.comfirm)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mCalendar.set(Calendar.YEAR, datePicker.getYear());
						mCalendar.set(Calendar.MONTH, datePicker.getMonth());
						mCalendar.set(Calendar.DAY_OF_MONTH,
								datePicker.getDayOfMonth());
						String date = TimeUtils.transformToTime1(mCalendar
								.getTimeInMillis());
						Intent i1 = new Intent(AddworkExperience.this,
								AddworkExperience.class);
						i1.putExtra("date", date);
						startActivity(i1);
						dialog.dismiss();

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
		dialog.getWindow().findViewById(R.id.cancel)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			finish();
			switch (msg.what) {
			case 0:
				JSONObject object = (JSONObject) msg.obj;
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

			default:
				break;
			}
		};
	};

}
