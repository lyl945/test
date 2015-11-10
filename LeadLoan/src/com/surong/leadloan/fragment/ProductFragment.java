package com.surong.leadloan.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.personal.MShopActivity;
import com.surong.leadloan.adapter.ProductAdapter;
import com.surong.leadloan.entity.Product;
import com.surong.leadloan.imageutils.ImageFetcher;

public class ProductFragment extends BaseFragment {
	private View view;
	private List<Product> list;
	private ProductAdapter productAdapter;
	private LinearLayout linearLayoutContent;
	private String logoPath;
	private MShopActivity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.personal_product, container, false);

		initView();
		return view;
	}

	private static class ViewHolder {
		TextView name, main_con, money_con, date_con, sum_con, way_con,
				province_con;
		ImageView instituteLogo;
	}

	private void initView() {
		linearLayoutContent = (LinearLayout) view
				.findViewById(R.id.linearLayoutContent);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Product product = list.get(i);
				ViewHolder viewHolder = new ViewHolder();
				View convertView = View.inflate(getActivity(),
						R.layout.product_item, null);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.name);
				viewHolder.main_con = (TextView) convertView
						.findViewById(R.id.main_con);
				viewHolder.money_con = (TextView) convertView
						.findViewById(R.id.money_con);
				viewHolder.date_con = (TextView) convertView
						.findViewById(R.id.date_con);
				viewHolder.sum_con = (TextView) convertView
						.findViewById(R.id.sum_con);
				viewHolder.way_con = (TextView) convertView
						.findViewById(R.id.way_con);
				viewHolder.province_con = (TextView) convertView
						.findViewById(R.id.province_con);
				viewHolder.instituteLogo = (ImageView) convertView
						.findViewById(R.id.person_icon);
				if (isNull(product.getProdName())) {
					viewHolder.name.setText(" ");
				} else {
					viewHolder.name.setText(product.getProdName());
				}
				if (isNull(product.getCustType())) {
					viewHolder.main_con.setText(" ");
				} else {
					viewHolder.main_con.setText(product.getCustType());
				}

				if (isNull(product.getLoanAmount())) {
					viewHolder.money_con.setText(" ");
				} else {
					viewHolder.money_con.setText(product.getLoanAmount());
				}

				if (isNull(product.getLoanPeriod())) {
					viewHolder.date_con.setText(" ");
				} else {
					viewHolder.date_con.setText(product.getLoanPeriod());
				}

				if (isNull(product.getCostExplain())) {
					viewHolder.sum_con.setText(" ");
				} else {
					viewHolder.sum_con.setText(product.getCostExplain());
				}

				if (isNull(product.getGuaranteeType())) {
					viewHolder.way_con.setText(" ");
				} else {
					viewHolder.way_con.setText(product.getGuaranteeType());
				}

				if (isNull(product.getCityName())) {
					viewHolder.province_con.setText(" ");
				} else {
					viewHolder.province_con.setText(product.getCityName());
				}
				ImageFetcher mImageFetcher = ImageFetcher.Instance(
						getActivity(), 0);
				if (null != logoPath && !"".equals(logoPath)) {
					mImageFetcher
							.addTask(viewHolder.instituteLogo, logoPath, 1);
					viewHolder.instituteLogo.setTag(logoPath);
				}
				linearLayoutContent.addView(convertView);
			}
		} else {
			View noInfo = View.inflate(getActivity(), R.layout.tip_no_content,
					null);
			TextView showNoInfo = (TextView) noInfo
					.findViewById(R.id.show_no_info);
			showNoInfo.setText("暂无信贷产品");
			linearLayoutContent.addView(noInfo);
		}
	}

	private boolean isNull(String custType) {
		if (null == custType || "".equals(custType) || "null".equals(custType)) {
			return true;
		}
		return false;
	}

	public ProductFragment() {
		super();
	}

	public void setmActivity(MShopActivity mActivity) {
		this.mActivity = mActivity;
		this.list = mActivity.list;
		this.logoPath = mActivity.logoPath;
	}

}
