package com.easemob.chatuidemo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.activity.BaseActivity;

public class CommonActivity extends BaseActivity implements OnClickListener {
    private Button back;
    private TextView center_title, text_right;
    public LinearLayout body;
    private RelativeLayout relative_back, relative_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_title);

        back = (Button) findViewById(R.id.back);
        center_title = (TextView) findViewById(R.id.center_title);
        relative_right = (RelativeLayout) findViewById(R.id.right);
        text_right = (TextView) findViewById(R.id.text_right);
        relative_right.setOnClickListener(this);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        relative_back.setOnClickListener(this);
        body = (LinearLayout) findViewById(R.id.body);

        relative_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }

    public void addContentView(View view) {
        body.addView(view);
    }

    public void changeTitle(String str) {
        center_title.setText(str);
    }

    public void setRight(String str) {
        text_right.setText(str);
    }

    public void setRightColor(int color) {
        text_right.setTextColor(color);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
