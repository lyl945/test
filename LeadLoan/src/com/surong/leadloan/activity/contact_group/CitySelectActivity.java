package com.surong.leadloan.activity.contact_group;

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
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.ui.MyListView;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils2.Constans;

public class CitySelectActivity extends CommonActivity {

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
    String provinces[][];
    private Handler handler;
    private String cityName;
    private String cityId;
    /*
     * 上个activity传过来的城市
     */
    private String cityString;
    /*
     * 用来放省市自治区
     */
    List<City> city = new ArrayList<City>();
    /*
     * 用来放省市自治区管辖下的县等
     */
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
                            city = City.parse(new JSONObject(
                                    responseInfo.result)
                                    .getString("provinceArray"));
                            myAdapter = new MyAdapter(getApplicationContext(),
                                    city);
                            listView.setAdapter(myAdapter);

                            for (int i = city.size() - 1; i >= 0; i--) {
                                int k = city.get(i).getCityArray().size() - 1;
                                for (int j = city.get(i).getCityArray().size() - 1; j >= 0; j--) {

                                    if (cityString.contains(city.get(i)
                                            .getCityArray().get(j).getName())) {
                                        /*
										 * 第一个listView所显示的位置
										 */
                                        k--;
                                        listView.setSelection(i);
                                        cityArray.clear();
                                        CityArray array = new CityArray();
                                        if (k < 0) {
                                            array.setSelect(true);
                                        }
                                        array.setName("全部");
                                        cityArray.add(array);
                                        cityArray.addAll(city.get(i)
                                                .getCityArray());
                                        city.get(i).setSelect(true);
                                        cityArray.get(j + 1).setSelect(true);
                                        subAdapter = new SubAdapter(
                                                getApplicationContext(),
                                                cityArray, city.get(i));
                                        subListView.setAdapter(subAdapter);
										/*
										 * 第二个listView所显示的位置
										 */
                                        if (k < 0) {
                                            subListView.setSelection(0);
                                        } else {
                                            subListView.setSelection(j + 1);
                                        }

                                    }
                                    if (!city.get(i).getCityArray().get(j)
                                            .isSelect()) {
                                        continue;
                                    } else {
                                        if (j == city.get(i).getCityArray()
                                                .size() - 1) {
                                            cityArray.get(0).setSelect(true);
                                        }

                                    }
                                    // }

                                }

                            }

                            // HandlerUtil.showError(context, code);
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
                cityArray.clear();

                final int location = position;
                CityArray array = new CityArray();
                for (int k = 0; k < city.get(position).getCityArray().size(); k++) {
                    if (!city.get(position).getCityArray().get(k).isSelect()) {
                        break;
                    } else {
                        if (k == city.get(position).getCityArray().size() - 1) {
                            array.setSelect(true);
                        }
                    }

                }
                array.setName("全部");
                cityArray.add(array);
                cityArray.addAll(city.get(position).getCityArray());
                // city.get(position).setSelect(true);
                // for (int i = 0; i <cityArray.size() ; i++) {
                // cityArray.get(i).setSelect(true);
                // }
                subAdapter = new SubAdapter(getApplicationContext(), cityArray,
                        city.get(position));
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

                subListView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        subAdapter.setSelectedPosition(position);
                        subAdapter.notifyDataSetInvalidated();
                        Toast.makeText(getApplicationContext(),
                                cityArray.get(position).getId(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // myAdapter.setSelectedPosition(position);
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
            return foods.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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
            // ����ѡ��Ч��
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
            return cities.size();
        }

        @Override
        public Object getItem(int position) {
            return getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder1 viewHolder = null;

            final int location = position;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.sublist_item,
                        null);
                viewHolder = new ViewHolder1();
                viewHolder.textView1 = (TextView) convertView
                        .findViewById(R.id.textview1);
                viewHolder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.itemcheckbox);
                viewHolder.mylinearLayout = (LinearLayout) convertView
                        .findViewById(R.id.mylinearLayout);
                // ccBox = (CheckBox)
                // convertView.findViewById(R.id.itemcheckbox);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder1) convertView.getTag();
            }
            viewHolder.checkBox.setChecked(cities.get(position).isSelect());
            if (cities.get(position).isSelect()) {
                viewHolder.textView1.setTextColor(Color.RED);
            } else {
                viewHolder.textView1.setTextColor(Color.BLACK); // viewHolder.textView.setBackgroundColor(Color.argb(255,
            }
            final CheckBox mBox = viewHolder.checkBox;
            final LinearLayout mylinearLayouts = viewHolder.mylinearLayout;
            final TextView textView1 = viewHolder.textView1;
            viewHolder.mylinearLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean isSelect = cities.get(position).isSelect();
                    cities.get(position).setSelect(!isSelect);
					/*
					 * 如果全选的checkbox是true，所有的checkbox都设置成true
					 * 如果全选的checkbox是false，所有的checkbox都设置成false
					 */
                    if (position == 0) {
                        if (cities.get(0).isSelect()) {
                            for (int i = 0; i < cities.size(); i++) {
                                cities.get(i).setSelect(true);
                                mBox.setChecked(cities.get(i).isSelect());
                            }
                        } else {
                            for (int i = 0; i < cities.size(); i++) {
                                cities.get(i).setSelect(false);
                                mBox.setChecked(cities.get(i).isSelect());
                            }
                        }
                    }
					/*
					 * 第二个listview的checkbox影响第一个listview的checkbox
					 */
                    for (int i = 0; i < cities.size(); i++) {
                        if (cities.get(i).isSelect()) {
                            city.setSelect(true);
                            break;
                        } else {
                            city.setSelect(false);
                        }
                    }
					/*
					 * 如果有一个checkbox没选上 全选的checkbox就设置成false
					 */
                    for (int i = 0; i < cities.size(); i++) {
                        if (!cities.get(i).isSelect()) {
                            cities.get(0).setSelect(false);
                            mBox.setChecked(cities.get(0).isSelect());
                        }
                    }
					/*
					 * 如果checkbox全选上了，全部的checkbox设置成true
					 */
                    for (int i = 1; i < cities.size(); i++) {
                        if (!cities.get(i).isSelect()) {
                            break;
                        } else {
                            if (i == cities.size() - 1) {
                                cities.get(0).setSelect(true);
                                mBox.setChecked(cities.get(0).isSelect());
                            }
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                    notifyDataSetChanged();

                }
            });

            viewHolder.textView1.setText(cities.get(position).getName());

            return convertView;
        }

        public class ViewHolder1 {
            public CheckBox checkBox;
            public LinearLayout mylinearLayout;
            public TextView textView1;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

    }

    @Override
    public void onBackPressed() {
        getCityName();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.relative_back:
                getCityName();
                break;
            default:
                break;
        }
    }

    private void getCityName() {
		/*
		 * 判断所有的checkbox，有选上的 就取它所对应的值加到cityName里
		 */
        cityName = "";
        cityId = "";
        // TODO Auto-generated method stub
        for (int i = 0; i < city.size(); i++) {
            for (int j = 0; j < city.get(i).getCityArray().size(); j++) {
                if (city.get(i).getCityArray().get(j).isSelect()) {
                    if (!TextUtils.isEmpty(cityName)) {
                        cityId = cityId + ";"
                                + city.get(i).getCityArray().get(j).getId();
                        cityName = cityName + ";"
                                + city.get(i).getCityArray().get(j).getName();
                    } else {
                        cityId = city.get(i).getCityArray().get(j).getId();
                        cityName = city.get(i).getCityArray().get(j).getName();
                    }

                }
            }
        }
        Intent result = new Intent();
        result.putExtra("cityname", cityName);
        result.putExtra("cityId", cityId);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
