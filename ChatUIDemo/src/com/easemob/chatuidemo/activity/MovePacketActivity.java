package com.easemob.chatuidemo.activity;

import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.httpserver.MyHttpUtils;
import com.easemob.chatuidemo.utils.Constans;
import com.easemob.chatuidemo.utils.CustomProgressDialog;
import com.easemob.chatuidemo.utils.SharedPreferencesHelp;
import com.easemob.chatuidemo.widget.ChoseGroupEntry;
import com.lidroid.xutils.http.RequestParams;
import com.pj.core.http.HttpImage;
import com.pj.core.utilities.DimensionUtility;

public class MovePacketActivity extends BaseActivity implements OnClickListener {

    private TextView textLeft, textTitle, textRight;
    private MyHttpUtils myHttpUtils;
    private Context context;
    private ImageView imageView;
    private TextView name;
    private TextView inst;
    private Button group;
    private String[] groups;
    private Bundle bundle;
    private ChoseGroupEntry entry;
    private MyHttpUtils mHttpUtils;
    private PopupWindow mPopupWindow;
    private TextView groupFriend, groupWorkmate;
    TreeMap<String, String> map;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_packet_activity);
        bundle = getIntent().getExtras();
        mHttpUtils.instance();
        entry = new ChoseGroupEntry();
        textLeft = (TextView) findViewById(R.id.top_textView_left);
        textTitle = (TextView) findViewById(R.id.top_textview_title);
        textRight = (TextView) findViewById(R.id.top_textView_right);
        textLeft.setOnClickListener(this);
        textTitle.setText("移动分组");
        textRight.setOnClickListener(this);
        textRight.setText("确定");

        imageView = (ImageView) findViewById(R.id.add_friend_header);
        name = (TextView) findViewById(R.id.add_friend_name);
        inst = (TextView) findViewById(R.id.add_friend_inst);
        group = (Button) findViewById(R.id.add_friend_choose_group);
        group.setOnClickListener(this);

        groups = new String[] { "好友", "同事" };
        map = new TreeMap<String, String>();
        map.put("好友", "0");
        map.put("同事", "1");
        initData();

        // 获取group的宽度
        ViewTreeObserver vto = group.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                width = group.getMeasuredWidth();
                initMenu(width);
            }
        });

    }

    // 获取到聊天界面传过来的bundle,并给控件赋值
    private void initData() {
        if (bundle.getString("name") != null) {
            name.setText(bundle.getString("name").toString().trim());
        }
        if (bundle.getString("institute") != null) {
            inst.setText(bundle.getString("institute").toString().trim());
        }
        if (bundle.getString("headimage") != null) {
            HttpImage.getInstance().setImage(bundle.getString("headimage"),
                    imageView, R.drawable.default_avatar,
                    DimensionUtility.dp2px(48) * 0.5f);
        }
        setGroup(ChoseGroupEntry.CATEGORY_FRIEND);
    }

    private void setGroup(int category) {
        if (entry != null) {
            entry.category = category;
        }

        group.setText(groups[category]);
    }

    // 初始化分组弹出框
    private void initMenu(int width) {
        View menu = getLayoutInflater().inflate(R.layout.groups, null, false);
        mPopupWindow = new PopupWindow(menu,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth(width);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        groupFriend = (TextView) menu.findViewById(R.id.group_friend);
        groupWorkmate = (TextView) menu.findViewById(R.id.group_workmate);

        groupFriend.setOnClickListener(this);
        groupWorkmate.setOnClickListener(this);

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.update();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_friend_choose_group) {// 分组栏
            if (!mPopupWindow.isShowing()) {
                mPopupWindow.showAsDropDown(group);
            } else {
                mPopupWindow.dismiss();
            }
        }

        if (entry == null) {
            return;
        }

        if (v.getId() == R.id.group_friend) {// 好友
            setGroup(ChoseGroupEntry.CATEGORY_FRIEND);
            mPopupWindow.dismiss();
        } else if (v.getId() == R.id.group_workmate) {// 同事
            setGroup(ChoseGroupEntry.CATEGORY_WORKMATE);
            mPopupWindow.dismiss();
        } else if (v.getId() == R.id.top_textView_right) {
            // 发送申请
            sendApply();
            finish();
        } else if (v.getId() == R.id.top_textView_left) {// 返回
            finish();
        }

    }

    private void sendApply() {

        sendApplyRequest(entry);
    }

    @SuppressWarnings("static-access")
    private void sendApplyRequest(ChoseGroupEntry mEntry) {

//        CustomProgressDialog.startProgressDialog(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("token",
                SharedPreferencesHelp.getString(this, "token"));
        params.addQueryStringParameter("friendId", bundle.getString("userId"));
        params.addQueryStringParameter("labelCode",
                map.get(group.getText().toString()));
        myHttpUtils.getHttpJsonString(params, Constans.friendMoveUrl, handler,
                context, 0, Constans.thod_Get_0);

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            JSONObject object = (JSONObject) msg.obj;
//            CustomProgressDialog.stopProgressDialog();
            try {
                int code = object.getInt("code");
                if (code == 0) {
                	sendBroadcast(new Intent("shua"));
                    Constans.Toast(MovePacketActivity.this, "分组成功");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    };
}
