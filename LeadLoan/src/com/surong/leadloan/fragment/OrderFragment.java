package com.surong.leadloan.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.surong.leadloan.R;
import com.surong.leadloan.activity.order.DirectMechanismActivity;
import com.surong.leadloan.activity.order.ProgressQueryActivity;
import com.surong.leadloan.activity.order.PromotionManagerActivity;
import com.surong.leadloan.activity.order.QuickRecommendationActivity;
import com.surong.leadloan.utils.CustomProgressDialog;

//import com.surong.leadloan.activity.order.PromotionManagerActivity;

public class OrderFragment extends BaseFragment implements OnClickListener {
	private View view;
	private LinearLayout publishProject, publishFund;
	private LinearLayout orderPlatform, projectMarket;
	private LinearLayout promotionManager, knowMorePeer, answerUserQuestion;
	private LinearLayout loanAssistant, commissionManage, directMechanism;
	private TextView title;
	private static final String SHAREDPREFERENCES = "config";
	private boolean isFirstIn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.order, container, false);
		// SharedPreferences preferences = getActivity().getSharedPreferences(
		// SHAREDPREFERENCES, getActivity().MODE_PRIVATE);
		// // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		// isFirstIn = preferences.getBoolean("lyl", false);
		initView();
		return view;
	}

	@Override
	public void onStart() {
		SharedPreferences preferences = getActivity().getSharedPreferences(
				SHAREDPREFERENCES, getActivity().MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("first_is", true);
		super.onStart();
	}

	private void initView() {

		commissionManage = (LinearLayout) view
				.findViewById(R.id.commission_account_management);
		directMechanism = (LinearLayout) view
				.findViewById(R.id.direct_mechanism);
		publishProject = (LinearLayout) view.findViewById(R.id.publish_project);
		publishFund = (LinearLayout) view.findViewById(R.id.publish_fund);
		orderPlatform = (LinearLayout) view.findViewById(R.id.order_platform);
		projectMarket = (LinearLayout) view.findViewById(R.id.project_market);
		promotionManager = (LinearLayout) view
				.findViewById(R.id.promotion_manager);
		loanAssistant = (LinearLayout) view.findViewById(R.id.loan_assistant);
		knowMorePeer = (LinearLayout) view.findViewById(R.id.know_more_peer);
		answerUserQuestion = (LinearLayout) view
				.findViewById(R.id.answer_user_question);

		title = (TextView) view.findViewById(R.id.top_textview_title);
		title.setText("拿单甩单");

		publishProject.setOnClickListener(this);
		publishFund.setOnClickListener(this);
		orderPlatform.setOnClickListener(this);
		projectMarket.setOnClickListener(this);
		promotionManager.setOnClickListener(this);
		loanAssistant.setOnClickListener(this);
		knowMorePeer.setOnClickListener(this);
		answerUserQuestion.setOnClickListener(this);
		commissionManage.setOnClickListener(this);
		directMechanism.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish_project:// 发布项目

			break;
		case R.id.publish_fund:// 发布资金

			break;
		case R.id.order_platform:// 领单平台

			break;
		case R.id.project_market:// 项目市场

			break;
		case R.id.promotion_manager:// 优化推广管理
			Intent iPromotion = new Intent(getActivity(),
					PromotionManagerActivity.class);
			startActivity(iPromotion);

			break;
		case R.id.loan_assistant:// 超级经纪人
			Intent intent = null;
			intent = new Intent(getActivity(),
					QuickRecommendationActivity.class);
			/*
			 * if (!isFirstIn) { intent = new Intent(getActivity(),
			 * QuickRecommendationActivity.class); } else { intent = new
			 * Intent(getActivity(), LoanAssistant2Activity.class); }
			 */
			startActivity(intent);

			// Intent loan_assistant = new Intent(getActivity(),
			// LoanAssistant2Activity.class);
			// startActivity(loan_assistant);

			break;
		case R.id.know_more_peer:// 认识更多同行

			break;
		case R.id.answer_user_question:// 回答用户提问

			break;
		case R.id.commission_account_management:// 交单管理
			startActivity(new Intent(getActivity(), ProgressQueryActivity.class));

			break;
		case R.id.direct_mechanism:// 机构直通车
			startActivity(new Intent(getActivity(),
					DirectMechanismActivity.class));

			break;

		default:
			break;
		}

	}

}
