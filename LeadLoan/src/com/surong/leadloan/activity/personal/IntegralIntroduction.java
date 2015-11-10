package com.surong.leadloan.activity.personal;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.surong.leadload.api.ecodeingView;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;

public class IntegralIntroduction extends CommonActivity {

	private View view;
	private Context context;
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		context = this;
//		view = View.inflate(context, R.layout.integral_introduction, null);
//		addContentView(view);
//		changeTitle("积分说明");
//		initView();
		 ecodeingView ev=new ecodeingView(this);
		 addContentView(ev);
		 changeTitle("积分说明");
	}

	private void initView() {
		textView = (TextView) view.findViewById(R.id.TextView1);
		Spanned text = Html
				.fromHtml("货款申请人获得贷款后，信贷经理可以将订单状态更改为已成功放款，速融100工作人员核实正确后，将会奖励和这个订单费用同等的积分给信贷经理，相当于"
						+ "<font color=#e2343f><b>成功订单全免费</b></font>");
		textView.setText(text);

	}
}
