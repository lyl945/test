package com.easemob.chatuidemo.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.httpserver.MyHttpUtils;
import com.easemob.chatuidemo.utils.Constans;
import com.easemob.chatuidemo.utils.CustomProgressDialog;
import com.easemob.chatuidemo.utils.SharedPreferencesHelp;
import com.lidroid.xutils.http.RequestParams;
import com.pj.core.datamodel.DataWrapper;

public class RemarkFriendActivity extends BaseActivity implements
        OnClickListener {

    private EditText remark_friend;
    private TextView textLeft, textTitle, textRight;
    private String remarkFriend;
    private MyHttpUtils myHttpUtils;
    private Context context;
    private String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remark_friend_activity);
        context = this;
        myHttpUtils.instance();
        friendId = getIntent().getStringExtra("userId");
        remark_friend = (EditText) findViewById(R.id.remark_friend);

        textLeft = (TextView) findViewById(R.id.top_textView_left);
        textTitle = (TextView) findViewById(R.id.top_textview_title);
        textRight = (TextView) findViewById(R.id.top_textView_right);
        textLeft.setOnClickListener(this);
        textLeft.setVisibility(View.VISIBLE);
        textTitle.setText("备注好友");
        textRight.setOnClickListener(this);
        textRight.setText("保存");

        SharedPreferences sharedPre = getSharedPreferences("config",
                MODE_PRIVATE);
        if (sharedPre != null) {
            remarkFriend = sharedPre.getString("remarkName", "");
            remark_friend.setText(getIntent().getStringExtra("displayname"));
        }

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
                JSONObject object = (JSONObject) msg.obj;
                CustomProgressDialog.stopProgressDialog();
                try {
                    int code = object.getInt("code");
                    if (code == 0) {
                    	sendBroadcast(new Intent("shua"));
                        Constans.Toast(context, "备注成功");
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

    public static void saveInfo(Context context, String remarkname) {
        // 获取SharedPreferences对象
        SharedPreferences sharedPre = context.getSharedPreferences("config",
                context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("remarkName", remarkname);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.top_textView_left) {
            finish();
        } else if (id == R.id.top_textView_right) {
            remarkFriend = remark_friend.getText().toString().trim();
            saveInfo(context, remarkFriend);
            CustomProgressDialog.startProgressDialog(context);
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("token",
                    SharedPreferencesHelp.getString(this, "token"));
            params.addQueryStringParameter("remarkName", remarkFriend);
            params.addQueryStringParameter("friendId", friendId);
            myHttpUtils.getHttpJsonString(params, Constans.friendRemarkUrl,
                    handler, context, 0, Constans.thod_Get_0);
            Intent intent = new Intent();
            intent.putExtra("remarkFriend", remarkFriend);
            setResult(1001, intent);
            finish();
        }
    }
}
