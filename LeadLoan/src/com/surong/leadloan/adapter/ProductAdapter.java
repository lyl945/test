package com.surong.leadloan.adapter;

public class ProductAdapter {
	// extends BaseAdapter {
	// private List<Product> list;
	// private Context context;
	// private String logoPath;
	// private ImageFetcher mImageFetcher;
	//
	// public ProductAdapter(String logoPath, List<Product> list, Context
	// context) {
	// super();
	// this.list = list;
	// this.context = context;
	// this.logoPath = logoPath;
	// }
	//
	// @Override
	// public int getCount() {
	// return list.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return list.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// Product product = list.get(position);
	// ViewHolder viewHolder;
	// if (convertView == null) {
	// viewHolder = new ViewHolder();
	// convertView = View.inflate(context, R.layout.product_item, null);
	// viewHolder.name = (TextView) convertView.findViewById(R.id.name);
	// viewHolder.main_con = (TextView) convertView
	// .findViewById(R.id.main_con);
	// viewHolder.money_con = (TextView) convertView
	// .findViewById(R.id.money_con);
	// viewHolder.date_con = (TextView) convertView
	// .findViewById(R.id.date_con);
	// viewHolder.sum_con = (TextView) convertView
	// .findViewById(R.id.sum_con);
	// viewHolder.way_con = (TextView) convertView
	// .findViewById(R.id.way_con);
	// viewHolder.province_con = (TextView) convertView
	// .findViewById(R.id.province_con);
	// viewHolder.instituteLogo = (ImageView) convertView
	// .findViewById(R.id.person_icon);
	// convertView.setTag(viewHolder);
	// } else {
	// viewHolder = (ViewHolder) convertView.getTag();
	// }
	// if (isNull(product.getProdName())) {
	// viewHolder.name.setText(" ");
	// } else {
	// viewHolder.name.setText(product.getProdName());
	// }
	// if (isNull(product.getCustType())) {
	// viewHolder.main_con.setText(" ");
	// } else {
	// viewHolder.main_con.setText(product.getCustType());
	// }
	//
	// if (isNull(product.getLoanAmount())) {
	// viewHolder.money_con.setText(" ");
	// } else {
	// viewHolder.money_con.setText(product.getLoanAmount());
	// }
	//
	// if (isNull(product.getLoanPeriod())) {
	// viewHolder.date_con.setText(" ");
	// } else {
	// viewHolder.date_con.setText(product.getLoanPeriod());
	// }
	//
	// if (isNull(product.getCostExplain())) {
	// viewHolder.sum_con.setText(" ");
	// } else {
	// viewHolder.sum_con.setText(product.getCostExplain());
	// }
	//
	// if (isNull(product.getGuaranteeType())) {
	// viewHolder.way_con.setText(" ");
	// } else {
	// viewHolder.way_con.setText(product.getGuaranteeType());
	// }
	//
	// if (isNull(product.getCityName())) {
	// viewHolder.province_con.setText(" ");
	// } else {
	// viewHolder.province_con.setText(product.getCityName());
	// }
	// mImageFetcher = ImageFetcher.Instance(context, 0);
	// if (null != logoPath && !"".equals(logoPath)) {
	// mImageFetcher.addTask(viewHolder.instituteLogo, logoPath, 1);
	// viewHolder.instituteLogo.setTag(logoPath);
	// }
	//
	// return convertView;
	// }
	//
	// private boolean isNull(String custType) {
	// if (null == custType || "".equals(custType) || "null".equals(custType)) {
	// return true;
	// }
	// return false;
	// }
	//
	// private class ViewHolder {
	// TextView name, main_con, money_con, date_con, sum_con, way_con,
	// province_con;
	// ImageView instituteLogo;
	// }

}
