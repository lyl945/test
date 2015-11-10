package com.surong.leadloan.activity.order;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.personal.ApplyCertification;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.entity.Promotion;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.imageutils.ImageFetcher;
import com.surong.leadloan.ui.CircleImageView;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;

public class PromotionManagerActivity extends CommonActivity implements
		OnClickListener {

	View view, v1;

	private TextView name, institute, money, overheadTimes, recharge;

	private CircleImageView imageView;

	private ListView mListView;

	private String token;

	private Context context;

	private MyHttpUtils myHttpUtils;

	private String nameString, instituteString, overheadString, imagePath;

	public static String moneyString;

	private ImageFetcher mImageFetcher;// 加载图片

	private List<Promotion> list = new ArrayList<Promotion>();

	private AlertDialog modifyDialog;

	private int index;

	int amount;

	String loginId;

	String status;

	// private DbUtils dbUtils;

	private Personal personal;

	private String stdFlag;// 判断是否信贷经理S还是信贷顾问A

	private LinearLayout sLayout, aLayout;

	private LinearLayout tipLayout;
	private TextView tip1, tip2, tip3;
	private FrameLayout listLayout;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				JSONObject object = (JSONObject) msg.obj;
				CustomProgressDialog.stopProgressDialog();

				try {
					JSONObject member = object.getJSONObject("member");
					if (!member.isNull("displayName")) {
						nameString = member.getString("displayName");
					}
					if (!member.isNull("instituName")) {
						instituteString = member.getString("instituName");
					}
					if (!member.isNull("amount")) {
						moneyString = member.getString("amount");
					}
					if (!member.isNull("overheadTimes")) {
						overheadString = member.getString("overheadTimes");
					}
					if (!member.isNull("headImgPath")) {
						imagePath = member.getString("headImgPath");
					}

					if (!object.isNull("promList")) {
						JSONArray promList = object.getJSONArray("promList");
						List<Promotion> list1 = new ArrayList<Promotion>();
						for (int i = 0; i < promList.length(); i++) {
							JSONObject prom = promList.getJSONObject(i);

							String promMethod = prom.getString("promMethod");
							String promStatus = prom.getString("promStatus");
							String orderCount = prom.getString("orderCount");
							String minAddPrice = prom.getString("minAddPrice");
							String basePrice = prom.getString("basePrice");
							String id = null;
							String prodName = null;
							if ((!stdFlag.equals("")) && stdFlag.equals("S")) {// 如果是S经理
																				// 才解析
								id = prom.getString("id");
								prodName = prom.getString("prodName");
							}
							String currentPrice = "0";
							if (promStatus.equals("推广中")) {
								currentPrice = prom.getString("currentPrice");
							}

							Promotion mPromotion = new Promotion();
							mPromotion.setBasePrice(basePrice);
							mPromotion.setCurrentPrice(currentPrice);
							if ((!stdFlag.equals("")) && stdFlag.equals("S")) {
								mPromotion.setProdName(prodName);
								mPromotion.setId(id);
							}
							mPromotion.setPromMethod(promMethod);
							mPromotion.setPromStatus(promStatus);
							mPromotion.setOrderCount(orderCount);
							mPromotion.setMinAddPrice(minAddPrice);
							list1.add(mPromotion);
						}
						list.clear();
						list.addAll(list1);
						getPromlistinfo(list);
						setData();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;

			case 1:
				JSONObject object1 = (JSONObject) msg.obj;
				CustomProgressDialog.stopProgressDialog();
				int code;
				try {
					code = object1.getInt("code");
					if (code == 0) {
						Constans.Toast(context, "发布成功");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				JSONObject object2 = (JSONObject) msg.obj;
				CustomProgressDialog.stopProgressDialog();
				int code2;
				try {
					code2 = object2.getInt("code");
					if (code2 == 0) {
						Constans.Toast(context, "取消成功");
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		view = View.inflate(context, R.layout.promotion_manager_activity, null);
		addContentView(view);
		changeTitle("推广管理");
		myHttpUtils.myInstance();
		token = SharedPreferencesHelp.getString(context, "token");
		initView();

	}

	@Override
	protected void onResume() {
		super.onResume();
		getInfo();
	}

	private void initView() {

		sLayout = (LinearLayout) findViewById(R.id.credit_manager);
		aLayout = (LinearLayout) findViewById(R.id.credit_consultant);
		name = (TextView) findViewById(R.id.name);
		institute = (TextView) findViewById(R.id.institute);
		money = (TextView) findViewById(R.id.money);
		overheadTimes = (TextView) findViewById(R.id.overhead_times);
		recharge = (TextView) findViewById(R.id.recharge);
		imageView = (CircleImageView) findViewById(R.id.person_icon);
		recharge.setOnClickListener(this);

		try {
			personal = CommonActivity.db.findFirst(Personal.class);
			if (personal.getStdFlag() != null) {
				stdFlag = personal.getStdFlag();

				if (stdFlag.equals("S")) {
					sLayout.setVisibility(View.VISIBLE);
					aLayout.setVisibility(View.GONE);
				} else {
					sLayout.setVisibility(View.GONE);
					aLayout.setVisibility(View.VISIBLE);

				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

		tipLayout = (LinearLayout) view.findViewById(R.id.prom_tip_layout);
		listLayout = (FrameLayout) view.findViewById(R.id.list_layout);
		tip1 = (TextView) view.findViewById(R.id.tip1);
		tip2 = (TextView) view.findViewById(R.id.tip2);
		tip3 = (TextView) view.findViewById(R.id.tip3);

	}

	// 查询推广状态接口调用
	private void getInfo() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", token);
		CustomProgressDialog.startProgressDialog(context);
		myHttpUtils.getHttpJsonString(params, Constans.findPromStatusUrl,
				handler, context, 0, Constans.thod_Get_0);

	}

	// 给界面赋值
	private void setData() {
		mImageFetcher = ImageFetcher.Instance(context, 0);
		name.setText(nameString);
		institute.setText(instituteString);
		money.setText(moneyString);
		overheadTimes.setText(overheadString);
		if (null != imagePath && !"".equals(imagePath)) {
			mImageFetcher.addTask(imageView, imagePath, 0);
			imageView.setTag(imagePath);
		}

		if (list != null && list.size() > 0) {
			listLayout.setVisibility(View.VISIBLE);
			tipLayout.setVisibility(View.GONE);
		} else {
			listLayout.setVisibility(View.GONE);
			tipLayout.setVisibility(View.VISIBLE);
		}

		try {
			personal = CommonActivity.db.findFirst(Personal.class);
			amount = Integer.parseInt(moneyString);
			loginId = personal.getMobile();
			status = personal.getAuthStatus();

			if (status.equals("00")) {
				tip1.setText("亲，还未认证哦!");
				tip2.setText("更多精彩功能等着你，快来认证吧！");
				tip3.setText("去认证");
				tip3.setVisibility(View.VISIBLE);
				tip3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(context,
								ApplyCertification.class));
					}
				});
			}
			if (status.equals("03") || status.equals("04")) {
				tip1.setText(" 亲，还在认证中哦！");
				tip2.setText(" 更多精彩功能等着你，请耐心等待吧！");
				tip3.setVisibility(View.GONE);
			}
			if (status.equals("02") && amount < 28) {
				tip1.setText("亲，您的点券余额不足哦!");
				tip2.setText("充值后即可推广，坐等一手订单哗哗来");
				tip3.setText("去充值");
				tip3.setVisibility(View.VISIBLE);
				tip3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Uri uri = Uri
								.parse("http://www.surong100.com/ffshcredit/index?redirectPagePath=http://www.surong100.com/ffshcredit/account/recharge&loginId="
										+ loginId);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					}
				});

			}
			if (status.equals("02") && amount > 28 && list.size() == 0) {
				tip1.setText("亲，您目前还未录入产品!");
				tip2.setText("电脑端登陆，录入产品更方便！");
				tip3.setText("录入产品");
				tip3.setVisibility(View.VISIBLE);
				tip3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Uri uri = Uri
								.parse("http://www.surong100.com/ffshcredit/index?redirectPagePath=http://www.surong100.com/ffshcredit/product/list&loginId="
										+ loginId);
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
					}
				});

			}

		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	// 点击弹出的修改价格的dialog
	private void showPriceDialog(final String productIdString,
			String currentPriceString, String basePriceString,
			String minPriceString) {
		modifyDialog = new AlertDialog.Builder(PromotionManagerActivity.this)
				.create();
		modifyDialog.show();
		modifyDialog.getWindow().setContentView(R.layout.modify_price_dialog);
		modifyDialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

		// 初始化dialog里面的控件
		TextView dialogTitle = (TextView) modifyDialog.getWindow()
				.findViewById(R.id.dialog_title);
		LinearLayout mLayout = (LinearLayout) modifyDialog.getWindow()
				.findViewById(R.id.LinearLayout_current_price);
		TextView currentPrice = (TextView) modifyDialog.getWindow()
				.findViewById(R.id.current_price);
		final TextView basePrice = (TextView) modifyDialog.getWindow()
				.findViewById(R.id.base_price);
		final TextView minPrice = (TextView) modifyDialog.getWindow()
				.findViewById(R.id.min_price);
		final EditText price = (EditText) modifyDialog.getWindow()
				.findViewById(R.id.input_price);
		TextView publishCommit = (TextView) modifyDialog.getWindow()
				.findViewById(R.id.publish_commit);
		ImageView close = (ImageView) modifyDialog.getWindow().findViewById(
				R.id.close_btn);

		// 给dialog赋值
		currentPrice.setText(currentPriceString);
		basePrice.setText(basePriceString);
		minPrice.setText(minPriceString);

		if (currentPriceString.equals("0")) {
			dialogTitle.setText("立即推广");
			mLayout.setVisibility(View.GONE);
		} else {
			dialogTitle.setText("修改出价");
			mLayout.setVisibility(View.VISIBLE);
		}
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				modifyDialog.dismiss();
			}
		});
		publishCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String priceString = price.getText().toString().trim();// 出价
				String baseprice = basePrice.getText().toString().trim();// 最低价
				String minprice = minPrice.getText().toString().trim();// 最小加价单位

				int price2 = Integer.parseInt(baseprice);
				int price3 = Integer.parseInt(minprice);
				if (priceString.equals("")) {
					Constans.Toast(context, "您的出价必须大于或等于底价");
					return;
				} else {
					int price1 = Integer.parseInt(priceString);
					if (price1 < price2) {
						Constans.Toast(context, "您的出价必须大于或等于底价");
						return;
					}
					if ((price1 - price2) % price3 != 0) {
						Constans.Toast(context, "您的出价必须是最小加价单位的倍数");
						return;
					}

				}

				if ((!stdFlag.equals("")) && stdFlag.equals("S")) {// 信贷经理
					RequestParams params = new RequestParams();
					params.addQueryStringParameter("token", token);
					params.addQueryStringParameter("price", priceString);
					params.addQueryStringParameter("prodId", productIdString);
					CustomProgressDialog.startProgressDialog(context);
					myHttpUtils.getHttpJsonString(params,
							Constans.productPromotionUrl, handler, context, 1,
							Constans.thod_Get_0);

				} else {// 信贷顾问
					RequestParams params = new RequestParams();
					params.addQueryStringParameter("token", token);
					params.addQueryStringParameter("price", priceString);
					CustomProgressDialog.startProgressDialog(context);
					myHttpUtils.getHttpJsonString(params,
							Constans.promStoreUrl, handler, context, 1,
							Constans.thod_Get_0);
				}
				getInfo();
				modifyDialog.dismiss();

			}
		});

	}

	/**
	 * @author hedongsheng 获取推广列表的方法
	 */
	private void getPromlistinfo(List<Promotion> list) {
		sLayout.removeAllViews();
		aLayout.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			final Promotion data = list.get(i);
			TextView promotionName;
			if ((!stdFlag.equals("")) && stdFlag.equals("S")) {// 加载经理列表的v
				v1 = View.inflate(context, R.layout.promotion_manager_item,
						null);
				promotionName = (TextView) v1.findViewById(R.id.product_name);
				promotionName.setText(data.getProdName());
			} else {// 加载顾问的v
				v1 = View.inflate(context, R.layout.credit_adviser, null);
			}
			TextView promotionWay = (TextView) v1
					.findViewById(R.id.promotion_way);
			TextView orderAcount = (TextView) v1
					.findViewById(R.id.order_acount);
			final TextView amount = (TextView) v1.findViewById(R.id.item_price);
			final TextView status = (TextView) v1
					.findViewById(R.id.promotion_status);
			final TextView modifyPrice = (TextView) v1
					.findViewById(R.id.modify_price);
			final TextView promotionBtn = (TextView) v1
					.findViewById(R.id.item_promotion);

			promotionWay.setText(data.getPromMethod());
			orderAcount.setText(data.getOrderCount());
			amount.setText(data.getCurrentPrice());
			status.setText(data.getPromStatus());

			if (amount.getText().toString().equals("0")) {
				// 立即推广按钮
				promotionBtn.setText("立即推广");
				promotionBtn.setTextColor(Color.WHITE);
				promotionBtn
						.setBackgroundResource(R.drawable.promotion_red_shape);
				modifyPrice.setVisibility(View.GONE);
				if (promotionBtn.getText().toString().equals("立即推广")) {
					promotionBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if ((!stdFlag.equals("")) && stdFlag.equals("S")) {
								showPriceDialog(data.getId(),
										data.getCurrentPrice(),
										data.getBasePrice(),
										data.getMinAddPrice());
							} else {// 顾问店铺没有id,传个null
								showPriceDialog(null, data.getCurrentPrice(),
										data.getBasePrice(),
										data.getMinAddPrice());

							}
						}
					});
				}
			} else {
				promotionBtn.setText("取消推广");
				promotionBtn.setTextColor(Color.BLACK);
				promotionBtn
						.setBackgroundResource(R.drawable.promotion_white_shape);
				modifyPrice.setVisibility(View.VISIBLE);

				modifyPrice.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if ((!stdFlag.equals("")) && stdFlag.equals("S")) {
							showPriceDialog(data.getId(),
									data.getCurrentPrice(),
									data.getBasePrice(), data.getMinAddPrice());
						} else {// 顾问店铺没有id,传个null
							showPriceDialog(null, data.getCurrentPrice(),
									data.getBasePrice(), data.getMinAddPrice());
						}

					}
				});
				// 取消推广按钮
				if (promotionBtn.getText().toString().equals("取消推广")) {

					promotionBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if ((!stdFlag.equals("")) && stdFlag.equals("S")) {
								RequestParams params = new RequestParams();
								params.addQueryStringParameter("token", token);
								params.addQueryStringParameter("prodId",
										data.getId());
								CustomProgressDialog
										.startProgressDialog(context);
								myHttpUtils.getHttpJsonString(params,
										Constans.cancelProdPromUrl, handler,
										context, 2, Constans.thod_Get_0);

							} else {
								RequestParams params = new RequestParams();
								params.addQueryStringParameter("token", token);
								CustomProgressDialog
										.startProgressDialog(context);
								myHttpUtils.getHttpJsonString(params,
										Constans.cancelStorePromUrl, handler,
										context, 2, Constans.thod_Get_0);

							}
							getInfo();
						}
					});
				}
			}
			if ((!stdFlag.equals("")) && stdFlag.equals("S")) {

				sLayout.addView(v1);
			} else {
				aLayout.addView(v1);
			}
		}
	}

	@Override
	public void onClick(View v) {

	}

}
