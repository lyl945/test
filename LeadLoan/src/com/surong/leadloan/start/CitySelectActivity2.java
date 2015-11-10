package com.surong.leadloan.start;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.surong.leadload.api.data.City;
import com.surong.leadload.api.data.CityArray;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.contact_group.CitySelectActivity.SubAdapter;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.ui.MyListView;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils2.Constans;

public class CitySelectActivity2 extends CommonActivity {

	private MyListView listView;
	private MyListView subListView;
	private MyAdapter myAdapter;
	private SubAdapter subAdapter;
	private Context context;
	private View view;
	private RelativeLayout back;
	int provinceCount = 0;
	String citys[][];
	int cityCount = 0;
	// private String cities[][];
	// private String foods[];
	String provinces[][];
	private Handler handler;
	private String cityName = "";
	private String cityId = "";
	private String cityString;

	public List<City> cityss = new ArrayList<City>();
	List<CityArray> cityArray = new ArrayList<CityArray>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(this, R.layout.city_select, null);
		addContentView(view);
		changeTitle("省份选择");
		cityString = getIntent().getStringExtra("cityname");
		CustomProgressDialog.startProgressDialog(context);
		MyHttpUtils.getHttpJsonString(context, HttpRequest.HttpMethod.GET,
				Constans.spinnerUrl, null, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						CustomProgressDialog.stopProgressDialog();
						try {
							cityss = City.parse(new JSONObject(
									responseInfo.result)
									.getString("provinceArray"));
							myAdapter = new MyAdapter(getApplicationContext(),
									cityss);
							listView.setAdapter(myAdapter);
							for (int i = 0; i < cityss.size(); i++) {
								// if
								// (city.get(i).getName().trim().equals("广东省"))
								// {
								// CityArray array = new CityArray();
								// array.setName("全部");
								// cityArray.add(array);
								// cityArray.addAll(city.get(i).getCityArray());
								// city.get(position).setSelect(true);
								// for (int i = 0; i <cityArray.size() ; i++) {
								// cityArray.get(i).setSelect(true);
								// }
								for (int j = 0; j < cityss.get(i)
										.getCityArray().size(); j++) {
									if (cityString.contains(cityss.get(i)
											.getCityArray().get(j).getName())) {
										listView.setSelection(i);
										cityArray.clear();
										CityArray array = new CityArray();
										cityArray.addAll(cityss.get(i)
												.getCityArray());
										cityss.get(i).setSelect(true);
										cityArray.get(j).setSelect(true);
										subAdapter = new SubAdapter(
												getApplicationContext(),
												cityArray, cityss.get(i));
										subListView.setAdapter(subAdapter);
									}
									//
								}
							}

						} catch (Exception e) {
							// HandlerUtil.showError(context, -1000);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						CustomProgressDialog.stopProgressDialog();
						Log.i("MyTest", "httpGetSpinner==error===>" + msg);
						// HandlerUtil.showError(context, 100);
					}
				});

		init();

		// selectDefult();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				final int location = position;
				cityArray = cityss.get(position).getCityArray();

				subAdapter = new SubAdapter(getApplicationContext(), cityArray,
						cityss.get(position));
				subListView.setAdapter(subAdapter);

				//
				// if (cityArray.get(0).isSelect()) {
				//
				// for (int i = 0; i <cityArray.size() ; i++) {
				// cityArray.get(i).setSelect(true);
				// }
				// }

				subAdapter.notifyDataSetInvalidated();
				myAdapter.setSelectedPosition(position);
				myAdapter.notifyDataSetInvalidated();

			}
		});

	}

	private void init() {
		back = this.relative_back;
		listView = (MyListView) findViewById(R.id.listView);
		subListView = (MyListView) findViewById(R.id.subListView);
		back.setOnClickListener(this);
	}

	public List<String> getData() {

		List<String> ls = new ArrayList<String>();
		ls = asList(provinces);
		return ls;
	}

	public List<String> asList(String s[][]) {
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < provinceCount; i++) {
			if (s[i][1] != null)
				l.add(s[i][1]);
		}
		return l;
	}

	public class MyAdapter extends BaseAdapter {

		Context context;
		LayoutInflater inflater;
		List<City> foods;
		// String [] foods;
		int last_item;
		int[] images;
		private int selectedPosition = -1;

		public MyAdapter(Context context, List<City> foods) {
			this.context = context;
			this.foods = foods;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return foods.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.mylist_item, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView
						.findViewById(R.id.textview);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.mycheckbox);
				holder.layout = (LinearLayout) convertView
						.findViewById(R.id.colorlayout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (selectedPosition == position) {
				holder.textView.setTextColor(Color.BLUE);

				holder.layout.setBackgroundColor(Color.rgb(241, 241, 241));
			} else {
				holder.textView.setTextColor(Color.WHITE);
				holder.layout.setBackgroundColor(Color.TRANSPARENT);
			}
			holder.checkBox.setChecked(foods.get(position).isSelect());
			if (foods.get(position).isSelect()) {
				holder.textView.setTextColor(Color.RED);
			} else {
				holder.textView.setTextColor(Color.BLACK);
			}

			holder.textView.setText(foods.get(position).getName());

			// // ����ѡ��Ч��
			// if (selectedPosition == position) {
			// holder.textView.setTextColor(Color.BLUE);
			//
			// holder.layout.setBackgroundColor(Color.LTGRAY);
			// } else {
			// holder.textView.setTextColor(Color.WHITE);
			// holder.layout.setBackgroundColor(Color.TRANSPARENT);
			// }
			// holder.checkBox.setChecked(foods.get(position).isSelect());
			// holder.textView.setText(foods.get(position).getName());
			// holder.textView.setTextColor(Color.BLACK);

			return convertView;
		}

		public class ViewHolder {
			public TextView textView;
			public CheckBox checkBox;
			public LinearLayout layout;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

	}

	public class SubAdapter extends BaseAdapter {

		Context context;
		LayoutInflater layoutInflater;
		private int selectedPosition = -1;
		private CheckBox ccBox;
		private City city;
		List<CityArray> cities;
		public int foodpoition;

		public SubAdapter(Context context, List<CityArray> cities, City city) {
			this.context = context;
			this.cities = cities;
			layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.city = city;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cities.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return getItem(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;

			final int location = position;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.sublist_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textview1);
				viewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.itemcheckbox);
				viewHolder.mylinearLayout = (LinearLayout) convertView
						.findViewById(R.id.mylinearLayout);
				// ccBox = (CheckBox)
				// convertView.findViewById(R.id.itemcheckbox);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.checkBox.setChecked(cities.get(position).isSelect());
			if (cities.get(position).isSelect()) {
				viewHolder.textView.setTextColor(Color.RED);
			} else {
				viewHolder.textView.setTextColor(Color.BLACK); // viewHolder.textView.setBackgroundColor(Color.argb(255,
			}
			final CheckBox mBox = viewHolder.checkBox;
			final LinearLayout mylinearLayouts = viewHolder.mylinearLayout;
			viewHolder.mylinearLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean isSelect = cities.get(position).isSelect();
					for (int j = 0; j < cityss.size(); j++) {
						cityss.get(j).setSelect(false);
						for (int i = 0; i < cityss.get(j).getCityArray().size(); i++) {
							cityss.get(j).getCityArray().get(i)
									.setSelect(false);
						}
					}
					cities.get(position).setSelect(!isSelect);
					for (int i = 0; i < cities.size(); i++) {
						if (cities.get(i).isSelect()) {
							city.setSelect(true);
							break;
						} else {
							city.setSelect(false);
						}
					}
					myAdapter.notifyDataSetChanged();
					notifyDataSetChanged();

				}
			});

			viewHolder.textView.setText(cities.get(position).getName());

			return convertView;
		}

		public class ViewHolder {
			public CheckBox checkBox;
			public LinearLayout mylinearLayout;
			public TextView textView;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent result = new Intent();
		for (int i = 0; i < cityss.size(); i++) {
			for (int j = 0; j < cityss.get(i).getCityArray().size(); j++) {
				if (cityss.get(i).getCityArray().get(j).isSelect()) {
					if (!TextUtils.isEmpty(cityName)) {
						cityId = cityId + ";"
								+ cityss.get(i).getCityArray().get(j).getId();
						cityName = cityName + ";"
								+ cityss.get(i).getCityArray().get(j).getName();
					} else {
						cityId = cityss.get(i).getCityArray().get(j).getId();
						cityName = cityss.get(i).getCityArray().get(j)
								.getName();
					}

				}
			}
		}
		result.putExtra("cityname", cityName);
		result.putExtra("cityId", cityId);
		setResult(Activity.RESULT_OK, result);
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.relative_back:

			Intent result = new Intent();
			for (int i = 0; i < cityss.size(); i++) {
				for (int j = 0; j < cityss.get(i).getCityArray().size(); j++) {
					if (cityss.get(i).getCityArray().get(j).isSelect()) {
						if (!TextUtils.isEmpty(cityName)) {
							cityId = cityId
									+ ";"
									+ cityss.get(i).getCityArray().get(j)
											.getId();
							cityName = cityName
									+ ";"
									+ cityss.get(i).getCityArray().get(j)
											.getName();
						} else {
							cityId = cityss.get(i).getCityArray().get(j)
									.getId();
							cityName = cityss.get(i).getCityArray().get(j)
									.getName();
						}

					}
				}
			}
			result.putExtra("cityname", cityName);
			result.putExtra("cityId", cityId);
			setResult(Activity.RESULT_OK, result);
			finish();

			break;

		default:
			break;
		}
	}
}
