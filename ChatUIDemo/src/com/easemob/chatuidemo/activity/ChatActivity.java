/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.GroupReomveListener;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.chatuidemo.R;
import com.easemob.chatuidemo.adapter.ExpressionAdapter;
import com.easemob.chatuidemo.adapter.ExpressionPagerAdapter;
import com.easemob.chatuidemo.adapter.MessageAdapter;
import com.easemob.chatuidemo.adapter.VoicePlayClickListener;
import com.easemob.chatuidemo.httpserver.MyHttpUtils;
import com.easemob.chatuidemo.res.ImageBucketChooseActivity;
import com.easemob.chatuidemo.res.ImageItem;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.chatuidemo.utils.Constans;
import com.easemob.chatuidemo.utils.CustomProgressDialog;
import com.easemob.chatuidemo.utils.IntentConstants;
import com.easemob.chatuidemo.utils.MenuDialog;
import com.easemob.chatuidemo.utils.SharedPreferencesHelp;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.easemob.chatuidemo.widget.PasteEditText;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.lidroid.xutils.http.RequestParams;
import com.pj.core.datamodel.DataWrapper;
import com.pj.core.utilities.StringUtility;
import com.surong.leadload.database.EASEDatabaseUserInfo;
import com.surong.leadloan.app.LDApplication;

/**
 * 聊天页面
 *
 */
public class ChatActivity extends BaseActivity implements OnClickListener {

    public static final String ACTION_SEND_NOT_FRIEND_MESSAGE = "action.notfriend";
    public static final String ACTION_SEND_SYSTEM = "action.system";
    public static final String ACTION_MESSAGE = "message";

    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;

    public static final String COPY_IMAGE = "EASEMOBIMG";
    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private ListView listView;
    private PasteEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    // private ViewPager expressionViewpager;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer;
    private ImageView locationImgview;
    private View more;
    private int position;
    private ClipboardManager clipboard;
    private ViewPager expressionViewpager;
    private InputMethodManager manager;
    private List<String> reslist;
    private Drawable[] micImages;
    private int chatType;
    private EMConversation conversation;
    private NewMessageBroadcastReceiver receiver;
    public static ChatActivity activityInstance = null;
    // 给谁发送消息
    private String toChatUsername;
    private VoiceRecorder voiceRecorder;
    private MessageAdapter adapter;
    private File cameraFile;
    static int resendPos;

    private GroupListener groupListener;

    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittext_layout;
    private ProgressBar loadmorePB;
    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;
    private Button btnMore;
    public String playMsgId;
    private PopupWindow mPopupWindow;
    private TextView remarkFriend, shopM, movePacket, deleteFriend;
    private RelativeLayout moreMenuLayout;
    private DataWrapper userInfoWrapper;
    private MyHttpUtils myHttpUtils;
    private Bundle bundle;
    private TextView chatName;
    private String displaynames;
    public static double latitudess;
    public static double longitudess;
    private String displayName;
    private List<EMConversation> conversationList;
    private boolean issystem;

    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };
    private EMGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        conversationList = new ArrayList<EMConversation>();
        initView();
        setUpView();
        onUserInfoUpdate();
        initMenu();
        myHttpUtils.instance();
        bundle = getIntent().getExtras();
        latitudess = bundle.getDouble("latitude");
        longitudess = bundle.getDouble("longitude");
        displayName = StringUtility
                .select(true, userInfoWrapper
                                .getString(EASEDatabaseUserInfo.UserDisplayName),
                        userInfoWrapper
                                .getString(EASEDatabaseUserInfo.UserRealName),
                        toChatUsername);
        // Toast.makeText(this, latitudess+"hh"+longitudess, 0).show();
    }

    private void onUserInfoUpdate() {
        EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(this);
        userInfoWrapper = databaseUserInfo.getUserInfoByUserID(toChatUsername);
        if (chatType == CHATTYPE_SINGLE && userInfoWrapper != null) { // 单聊
            ((TextView) findViewById(R.id.name)).setText(StringUtility.select(
                    true, userInfoWrapper
                            .getString(EASEDatabaseUserInfo.UserDisplayName),
                    userInfoWrapper
                            .getString(EASEDatabaseUserInfo.UserRealName),
                    toChatUsername));
        }
    }

    /**
     * initView
     */
    protected void initView() {
        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        listView = (ListView) findViewById(R.id.list);
        mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        locationImgview = (ImageView) findViewById(R.id.btn_location);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
        btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        more = findViewById(R.id.more);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
        moreMenuLayout = (RelativeLayout) findViewById(R.id.container_remove);
        moreMenuLayout.setOnClickListener(this);
        if (getIntent().getStringExtra("userId").equals("system")) {
            moreMenuLayout.setVisibility(View.GONE);
            refreshConversions();
        }

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{
                getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02),
                getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04),
                getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06),
                getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08),
                getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10),
                getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12),
                getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14),};

        // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        edittext_layout.requestFocus();
        voiceRecorder = new VoiceRecorder(micImageHandler);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout
                            .setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout
                            .setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        mEditTextContent.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        edittext_layout
                                .setBackgroundResource(R.drawable.input_bar_bg_active);
                        more.setVisibility(View.GONE);
                        iv_emoticons_normal.setVisibility(View.VISIBLE);
                        iv_emoticons_checked.setVisibility(View.INVISIBLE);
                        emojiIconContainer.setVisibility(View.GONE);
                        btnContainer.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    // popupWindow控件 初始化
    private void initMenu() {
        View menu = getLayoutInflater()
                .inflate(R.layout.more_menu, null, false);
        mPopupWindow = new PopupWindow(menu,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        remarkFriend = (TextView) menu.findViewById(R.id.remark_friend);
        shopM = (TextView) menu.findViewById(R.id.check_m);
        movePacket = (TextView) menu.findViewById(R.id.move_packet);
        deleteFriend = (TextView) menu.findViewById(R.id.delete_friend);
        remarkFriend.setOnClickListener(this);
        shopM.setOnClickListener(this);
        movePacket.setOnClickListener(this);
        deleteFriend.setOnClickListener(this);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.update();
    }

    private void setUpView() {
        activityInstance = this;
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        // position = getIntent().getIntExtra("position", -1);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);

        if (chatType == CHATTYPE_SINGLE) { // 单聊
            toChatUsername = getIntent().getStringExtra("userId");
            displaynames = getIntent().getStringExtra("name");
            ((TextView) findViewById(R.id.name)).setText(toChatUsername);
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername,false);
        } else {
            // 群聊
            findViewById(R.id.container_to_group).setVisibility(View.VISIBLE);
            findViewById(R.id.container_remove).setVisibility(View.GONE);
            findViewById(R.id.container_voice_call).setVisibility(View.GONE);
            findViewById(R.id.container_video_call).setVisibility(View.GONE);
            toChatUsername = getIntent().getStringExtra("groupId");
            group = EMGroupManager.getInstance().getGroup(toChatUsername);
            if (group != null)
                ((TextView) findViewById(R.id.name)).setText(group
                        .getGroupName());
            else
                ((TextView) findViewById(R.id.name)).setText(toChatUsername);
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername,true);
        }
        conversation = EMChatManager.getInstance().getConversation(
                toChatUsername);
        // 把此会话的未读数置为0
        conversation.resetUnreadMsgCount();
        adapter = new MessageAdapter(this, toChatUsername, chatType);
        // 显示消息
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count - 1);
        }

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });
        // 注册接收消息广播
        receiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager
                .getInstance().getNewMessageBroadcastAction());
        // 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        intentFilter.setPriority(5);
        registerReceiver(receiver, intentFilter);

        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
                .getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(5);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

        // 注册一个消息送达的BroadcastReceiver
        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(
                EMChatManager.getInstance()
                        .getDeliveryAckMessageBroadcastAction());
        deliveryAckMessageIntentFilter.setPriority(5);
        registerReceiver(deliveryAckMessageReceiver,
                deliveryAckMessageIntentFilter);

        // 监听当前会话的群聊解散被T事件
        groupListener = new GroupListener();
        EMGroupManager.getInstance().addGroupChangeListener(groupListener);

        // show forward message if the message is not null
        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
             if (forward_msg_id != null) {
            // 显示发送要转发的消息
            // forwardMessage(forward_msg_id);
        }

    }

    /**
     * onActivityResult
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_EXIT_GROUP) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (resultCode == 1001) {
            if (TextUtils.isEmpty(data.getExtras().getString("remarkFriend"))) {
                ((TextView) findViewById(R.id.name)).setText(displaynames);
            } else {
                EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(
                        getApplicationContext());

                displayName = data.getExtras().getString("remarkFriend");
                DataWrapper info = databaseUserInfo
                        .getUserInfoByUserID(toChatUsername);
                info.setObject(EASEDatabaseUserInfo.UserDisplayName,
                        displayName);
                databaseUserInfo.insertOrUpdateUserTmpInfo(info);
                ((TextView) findViewById(R.id.name)).setText(data.getExtras()
                        .getString("remarkFriend"));
            }

        }

        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case RESULT_CODE_COPY: // 复制消息
                    EMMessage copyMsg = ((EMMessage) adapter.getItem(data
                            .getIntExtra("position", -1)));
                    // clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
                    // ((TextMessageBody) copyMsg.getBody()).getMessage()));
                    clipboard.setText(((TextMessageBody) copyMsg.getBody())
                            .getMessage());
                    break;
                case RESULT_CODE_DELETE: // 删除消息
                    EMMessage deleteMsg = (EMMessage) adapter.getItem(data
                            .getIntExtra("position", -1));
                    conversation.removeMessage(deleteMsg.getMsgId());
                    adapter.refresh();
                    listView.setSelection(data.getIntExtra("position",
                            adapter.getCount()) - 1);
                    break;

                case RESULT_CODE_FORWARD: // 转发消息
                    EMMessage forwardMsg = (EMMessage) adapter.getItem(data
                            .getIntExtra("position", 0));
                    // Intent intent = new Intent(this,
                    // ForwardMessageActivity.class);
                    // intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
                    // startActivity(intent);

                    break;

                default:
                    break;
            }
        }
        if (resultCode == RESULT_OK) { // 清空消息
            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
                // 清空会话
                EMChatManager.getInstance().clearConversation(toChatUsername);
                adapter.refresh();
            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists())
                    sendPicture(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频

                int duration = data.getIntExtra("dur", 0);
                String videoPath = data.getStringExtra("path");
                File file = new File(PathUtil.getInstance().getImagePath(),
                        "thvideo" + System.currentTimeMillis());
                Bitmap bitmap = null;
                FileOutputStream fos = null;
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                    if (bitmap == null) {
                        EMLog.d("chatactivity",
                                "problem load video thumbnail bitmap,use default icon");
                        bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.app_panel_video_icon);
                    }
                    fos = new FileOutputStream(file);

                    bitmap.compress(CompressFormat.JPEG, 100, fos);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fos = null;
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                }
                sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                // if (data != null) {
                // Uri selectedImage = data.getData();
                // if (selectedImage != null) {
                // sendPicByUri(selectedImage);
                // }
                // }
                // getTempFromPref();
                List<ImageItem> incomingDataList = (List<ImageItem>) data
                        .getSerializableExtra(IntentConstants.EXTRA_IMAGE_LIST);
                if (incomingDataList != null) {
                    for (int i = 0; i < incomingDataList.size(); i++) {
                        sendPicture(incomingDataList.get(i).sourcePath);
                        // /storage/emulated/0/linkGroup/record/images/20150508100559.png
                        // Uri lUri = incomingDataList.get(i);
                        // sendPicByUri(selectedImage);
                        // sendPicByUri(incomingDataList.get(i).sourcePath);
                    }
                    // mDataList.addAll(incomingDataList);
                }
            } else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFile(uri);
                    }
                }

            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    more(more);
                    sendLocationMsg(latitude, longitude, "", locationAddress);
                } else {
                    String st = getResources().getString(
                            R.string.unable_to_get_loaction);
                    Toast.makeText(this, st, 0).show();
                }
                // 重发消息
            } else if (requestCode == REQUEST_CODE_TEXT
                    || requestCode == REQUEST_CODE_VOICE
                    || requestCode == REQUEST_CODE_PICTURE
                    || requestCode == REQUEST_CODE_LOCATION
                    || requestCode == REQUEST_CODE_VIDEO
                    || requestCode == REQUEST_CODE_FILE) {
                resendMessage();
            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
                // 粘贴
                if (!TextUtils.isEmpty(clipboard.getText())) {
                    String pasteText = clipboard.getText().toString();
                    if (pasteText.startsWith(COPY_IMAGE)) {
                        // 把图片前缀去掉，还原成正常的path
                        sendPicture(pasteText.replace(COPY_IMAGE, ""));
                    }

                }
            } else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
                EMMessage deleteMsg = (EMMessage) adapter.getItem(data
                        .getIntExtra("position", -1));
                addUserToBlacklist(deleteMsg.getFrom());
            } else if (conversation.getMsgCount() > 0) {
                adapter.refresh();
                setResult(RESULT_OK);
            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
                adapter.refresh();
            }
        }
    }

    /**
     * 消息图标点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        String st1 = getResources().getString(R.string.not_connect_to_server);
        int id = view.getId();
        if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
            String s = mEditTextContent.getText().toString();
            sendText(s);
        } else if (id == R.id.btn_take_picture) {
            selectPicFromCamera();// 点击照相图标
        }
        // 点击图片图标
        else if (id == R.id.btn_picture) {

            Intent intent = new Intent(ChatActivity.this,
                    ImageBucketChooseActivity.class);
            intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE, 9);
            startActivityForResult(intent, REQUEST_CODE_LOCAL);
            // selectPicFromLocal();
        } else if (id == R.id.btn_location) { // 位置
            startActivityForResult(new Intent(ChatActivity.this,
                    BaiduMapActivity.class), REQUEST_CODE_MAP);
        } else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
            more.setVisibility(View.VISIBLE);
            iv_emoticons_normal.setVisibility(View.INVISIBLE);
            iv_emoticons_checked.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.GONE);
            emojiIconContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
            more.setVisibility(View.GONE);

        } else if (id == R.id.btn_video) {
            // 点击摄像图标
            Intent intent = new Intent(ChatActivity.this,
                    ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
        } else if (id == R.id.btn_file) { // 点击文件图标
            selectFileFromLocal();
        } else if (id == R.id.btn_voice_call) { // 点击语音电话图标
            if (!EMChatManager.getInstance().isConnected())
                Toast.makeText(this, st1, 0).show();
            else
                startActivity(new Intent(ChatActivity.this,
                        VoiceCallActivity.class).putExtra("username",
                        toChatUsername).putExtra("isComingCall", false));
        } else if (id == R.id.btn_video_call) { // 视频通话
            if (!EMChatManager.getInstance().isConnected())
                Toast.makeText(this, st1, 0).show();
            else
                startActivity(new Intent(this, VideoCallActivity.class)
                        .putExtra("username", toChatUsername).putExtra(
                                "isComingCall", false));
        } else if (id == R.id.container_remove) {
            if (!mPopupWindow.isShowing()) {
                mPopupWindow.showAsDropDown(moreMenuLayout);
            } else {
                mPopupWindow.dismiss();
            }
        } else if (id == R.id.remark_friend) {// 好友备注
            Intent i1 = new Intent(ChatActivity.this,
                    RemarkFriendActivity.class);
            Log.e("123", toChatUsername);

            i1.putExtra("displayname", displayName);
            i1.putExtra("userId", toChatUsername);
            startActivityForResult(i1, 1001);

            mPopupWindow.dismiss();
        } else if (id == R.id.check_m) {// E店
            Intent i2 = new Intent(Intent.ACTION_SENDTO);
            bundle.putString("remarkname", displayName);
            i2.putExtras(bundle);
            startActivity(i2);
            mPopupWindow.dismiss();
        } else if (id == R.id.move_packet) {// 移动分组
            Intent i3 = new Intent(ChatActivity.this, MovePacketActivity.class);
            i3.putExtras(bundle);
            startActivity(i3);
            mPopupWindow.dismiss();

        } else if (id == R.id.delete_friend) {// 删除好友

            final MenuDialog menuDialog = new MenuDialog(ChatActivity.this);
            menuDialog.setCancelable(true);
            menuDialog.setTitle("确认删除好友?");
            menuDialog.addItem(getString(R.string.label_delete), 0,
                    toChatUsername);
            menuDialog.show();

            menuDialog
                    .setOnItemClickListener(new MenuDialog.MenuDialogItemClickListener() {

                        @Override
                        public boolean onItemClick(int id, TextView item) {
                            deleteFriend();
                            return true;
                        }
                    });

        }

    }

    // private int getAvailableSize()
    // {
    // int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
    // if (availSize >= 0)
    // {
    // return availSize;
    // }
    // return 0;
    // }

    private void deleteFriend() {
        CustomProgressDialog.startProgressDialog(this);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("token",
                SharedPreferencesHelp.getString(this, "token"));
        params.addQueryStringParameter("friendId", toChatUsername);

        myHttpUtils.getHttpJsonString(params, Constans.friendDeleteUrl,
                handler, this, 0, Constans.thod_Get_0);

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    CustomProgressDialog.stopProgressDialog();
                    JSONObject object = (JSONObject) msg.obj;
                    try {
                        int code = object.getInt("code");
                        if (code == 0) {
                            sendBroadcast(new Intent("shua"));
                            Constans.Toast(ChatActivity.this, "删除成功");
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            String st = getResources().getString(
                    R.string.sd_card_does_not_exist);
            Toast.makeText(getApplicationContext(), st, 0).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(),
                ((LDApplication) LDApplication.getInstance()).getUserName()
                        + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 选择文件
     */
    private void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    private boolean shouldSendMessage(EMMessage message) {
        EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(this);
        boolean ret = databaseUserInfo.isMyFriend(toChatUsername);
        if (!ret) {
            // 非好友,显示加好友label
            EMMessage cmdMessage = EMMessage
                    .createSendMessage(EMMessage.Type.CMD);
            CmdMessageBody body = new CmdMessageBody(
                    ACTION_SEND_NOT_FRIEND_MESSAGE);
            body.params = new HashMap<String, String>();

            DataWrapper inf = databaseUserInfo
                    .getUserInfoByUserID(toChatUsername);
            if (inf != null) {
                String tip = String
                        .format("你和\"%s\"已经不是好友，请加好友后聊天[点击加好友]",
                                StringUtility.select(
                                        true,
                                        inf.getString(EASEDatabaseUserInfo.UserDisplayName),
                                        inf.getString(EASEDatabaseUserInfo.UserRealName),
                                        toChatUsername, "--"));
                body.params.put(ACTION_MESSAGE, tip);

            }
            cmdMessage.addBody(body);

            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                cmdMessage.setChatType(ChatType.GroupChat);

            cmdMessage.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.addMessage(cmdMessage);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);

            setResult(RESULT_CANCELED);
        }
        return ret;
    }

    /**
     * 发送文本消息
     *
     * @param content
     *            message content
     * @param isResend
     *            boolean resend
     */
    private void sendText(String content) {

        if (content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            if (!shouldSendMessage(message)) {
                mEditTextContent.setText("");
                return;
            }
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(ChatType.GroupChat);
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.addMessage(message);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            mEditTextContent.setText("");

            setResult(RESULT_OK);

        }
    }

    /**
     * 发送语音
     *
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length,
                           boolean isResend) {
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            final EMMessage message = EMMessage
                    .createSendMessage(EMMessage.Type.VOICE);

            if (!shouldSendMessage(message)) {
                return;
            }

            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(ChatType.GroupChat);
            message.setReceipt(toChatUsername);
            int len = Integer.parseInt(length);
            VoiceMessageBody body = new VoiceMessageBody(new File(filePath),
                    len);
            message.addBody(body);

            conversation.addMessage(message);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            setResult(RESULT_OK);
            // send file
            // sendVoiceSub(filePath, fileName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath
     */
    private void sendPicture(final String filePath) {
        String to = toChatUsername;
        // create and add image message in view
        final EMMessage message = EMMessage
                .createSendMessage(EMMessage.Type.IMAGE);

        if (!shouldSendMessage(message)) {
            return;
        }

        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(ChatType.GroupChat);

        message.setReceipt(to);
        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        conversation.addMessage(message);

        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);
        // more(more);
    }

    /**
     * 发送视频消息
     */
    private void sendVideo(final String filePath, final String thumbPath,
                           final int length) {
        final File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            return;
        }
        try {
            EMMessage message = EMMessage
                    .createSendMessage(EMMessage.Type.VIDEO);

            if (!shouldSendMessage(message)) {
                return;
            }

            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(ChatType.GroupChat);
            String to = toChatUsername;
            message.setReceipt(to);
            VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
                    length, videoFile.length());
            message.addBody(body);
            conversation.addMessage(message);
            listView.setAdapter(adapter);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            setResult(RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) {
        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, null, null,
                null, null);
        String st8 = getResources().getString(R.string.cant_find_pictures);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendPicture(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendPicture(file.getAbsolutePath());
        }

    }

    /**
     * 发送位置信息
     *
     * @param latitude
     * @param longitude
     * @param imagePath
     * @param locationAddress
     */
    private void sendLocationMsg(double latitude, double longitude,
                                 String imagePath, String locationAddress) {
        EMMessage message = EMMessage
                .createSendMessage(EMMessage.Type.LOCATION);

        if (!shouldSendMessage(message)) {
            return;
        }

        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(ChatType.GroupChat);
        LocationMessageBody locBody = new LocationMessageBody(locationAddress,
                latitude, longitude);
        message.addBody(locBody);
        message.setReceipt(toChatUsername);
        conversation.addMessage(message);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);

    }

    /**
     * 发送文件
     *
     * @param uri
     */
    private void sendFile(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, projection, null,
                        null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            String st7 = getResources().getString(R.string.File_does_not_exist);
            Toast.makeText(getApplicationContext(), st7, 0).show();
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            String st6 = getResources().getString(
                    R.string.The_file_is_not_greater_than_10_m);
            Toast.makeText(getApplicationContext(), st6, 0).show();
            return;
        }

        // 创建一个文件消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);

        if (!shouldSendMessage(message)) {
            return;
        }

        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(ChatType.GroupChat);

        message.setReceipt(toChatUsername);
        // add message body
        NormalFileMessageBody body = new NormalFileMessageBody(new File(
                filePath));
        message.addBody(body);
        conversation.addMessage(message);
        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);
    }

    /**
     * 重发消息
     */
    private void resendMessage() {
        EMMessage msg = null;
        msg = conversation.getMessage(resendPos);
        // msg.setBackSend(true);
        msg.status = EMMessage.Status.CREATE;

        if (!shouldSendMessage(msg)) {
            return;
        }

        adapter.refresh();
        listView.setSelection(resendPos);
    }

    /**
     * 显示语音图标按钮
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);

    }

    /**
     * 显示键盘图标
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        // mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
        // {
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        // if(hasFocus){
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // }
        // }
        // });
        edittext_layout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            btnMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 点击清空聊天记录
     *
     * @param view
     */
    public void emptyHistory(View view) {
        String st5 = getResources().getString(
                R.string.Whether_to_empty_all_chats);
        startActivityForResult(
                new Intent(this, AlertDialog.class)
                        .putExtra("titleIsCancel", true).putExtra("msg", st5)
                        .putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
    }

    /**
     * 点击进入群组详情
     *
     * @param view
     */
    public void toGroupDetails(View view) {
        if (group == null) {
            Toast.makeText(getApplicationContext(), R.string.gorup_not_found, 0)
                    .show();
            return;
        }
        startActivityForResult(
                (new Intent(this, GroupDetailsActivity.class).putExtra(
                        "groupId", toChatUsername)), REQUEST_CODE_GROUP_DETAIL);
    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void more(View view) {
        if (more.getVisibility() == View.GONE) {
            System.out.println("more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
        } else {
            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 消息广播接收者
     *
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 记得把广播给终结掉
            abortBroadcast();
            String username = intent.getStringExtra("from");
            String msgid = intent.getStringExtra("msgid");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgid);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(toChatUsername)) {
                // 消息不是发给当前会话，return
                notifyNewMessage(message);
                return;
            }
            refreshConversions();
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername);
            // 通知adapter有新消息，更新ui
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);

        }
    }

    private void refreshConversions() {
        conversationList.clear();
        // 非好友,显示加好友label
        List<EMConversation> tmp = loadConversationsWithRecentChat();
        for (int i = 0; i < tmp.size(); i++) {
            if (tmp.get(i).getUserName().equals("system")) {
                issystem = true;
            }
        }
        if (!issystem) {
            EMMessage cmdMessage = EMMessage
                    .createSendMessage(EMMessage.Type.CMD);
            CmdMessageBody body = new CmdMessageBody("action.system");
            body.params = new HashMap<String, String>();
            String tip = "我是领贷管家，您可以在这里进行建议、反馈，和我们一对一的沟通。";
            body.params.put("message", tip);
            cmdMessage.addBody(body);
            // 如果是群聊，设置chattype,默认是单聊
            cmdMessage.setChatType(ChatType.GroupChat);
            cmdMessage.setReceipt("system");
            conversation = EMChatManager.getInstance()
                    .getConversation("system");
            // 把messgage加到conversation中
            conversation.addMessage(cmdMessage);
            conversationList.add(conversation);

        }
    }

    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager
                .getInstance().getAllConversations();
        List<EMConversation> list = new ArrayList<EMConversation>();
        // 过滤掉messages seize为0的conversation
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() > 0)
                list.add(conversation);
        }
        // 排序
        return list;
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance()
                    .getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isAcked = true;
                }
            }
            adapter.notifyDataSetChanged();

        }
    };

    /**
     * 消息送达BroadcastReceiver
     */
    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance()
                    .getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isDelivered = true;
                }
            }

            adapter.notifyDataSetChanged();
        }
    };
    private PowerManager.WakeLock wakeLock;

    /**
     * 按住说话listener
     *
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.isExitsSdcard()) {
                        String st4 = getResources().getString(
                                R.string.Send_voice_need_sdcard_support);
                        Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT)
                                .show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        wakeLock.acquire();
                        if (VoicePlayClickListener.isPlaying)
                            VoicePlayClickListener.currentPlayListener
                                    .stopPlayVoice();
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint
                                .setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voiceRecorder.startRecording(null, toChatUsername,
                                getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(ChatActivity.this, R.string.recoding_fail,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint
                                .setText(getString(R.string.release_to_cancel));
                        recordingHint
                                .setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint
                                .setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();

                    } else {
                        // stop recording and send voice file
                        String st1 = getResources().getString(
                                R.string.Recording_without_permission);
                        String st2 = getResources().getString(
                                R.string.The_recording_time_is_too_short);
                        String st3 = getResources().getString(
                                R.string.send_failure_please);
                        try {
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {
                                sendVoice(voiceRecorder.getVoiceFilePath(),
                                        voiceRecorder
                                                .getVoiceFileName(toChatUsername),
                                        Integer.toString(length), false);
                            } else if (length == EMError.INVALID_FILE) {
                                Toast.makeText(getApplicationContext(), st1,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), st2,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ChatActivity.this, st3,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null)
                        voiceRecorder.discardRecording();
                    return false;
            }
        }
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class
                                    .forName("com.easemob.chatuidemo.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            mEditTextContent.append(SmileUtils.getSmiledText(
                                    ChatActivity.this, (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent
                                        .getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = mEditTextContent.getText()
                                            .toString();
                                    String tempStr = body.substring(0,
                                            selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i,
                                                selectionStart);
                                        if (SmileUtils.containsKey(cs
                                                .toString()))
                                            mEditTextContent.getEditableText()
                                                    .delete(i, selectionStart);
                                        else
                                            mEditTextContent.getEditableText()
                                                    .delete(selectionStart - 1,
                                                            selectionStart);
                                    } else {
                                        mEditTextContent.getEditableText()
                                                .delete(selectionStart - 1,
                                                        selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
        // 注销广播
        try {
            unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception e) {
        }
        try {
            unregisterReceiver(ackMessageReceiver);
            ackMessageReceiver = null;
            unregisterReceiver(deliveryAckMessageReceiver);
            deliveryAckMessageReceiver = null;
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (group != null)
            ((TextView) findViewById(R.id.name)).setText(group.getGroupName());
        adapter.refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
        if (VoicePlayClickListener.isPlaying
                && VoicePlayClickListener.currentPlayListener != null) {
            // 停止语音播放
            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }

        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                recordingContainer.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 加入到黑名单
     *
     * @param username
     */
    private void addUserToBlacklist(String username) {
        String st11 = getResources().getString(
                R.string.Move_into_blacklist_success);
        String st12 = getResources().getString(
                R.string.Move_into_blacklist_failure);
        try {
            EMContactManager.getInstance().addUserToBlackList(username, false);
            Toast.makeText(getApplicationContext(), st11, 0).show();
        } catch (EaseMobException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), st12, 0).show();
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * 覆盖手机返回键
     */
    @Override
    public void onBackPressed() {
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * listview滑动监听listener
     *
     */
    private class ListScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isloading
                            && haveMoreData) {
                        loadmorePB.setVisibility(View.VISIBLE);
                        // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
                        List<EMMessage> messages;
                        try {
                            // 获取更多messges，调用此方法的时候从db获取的messages
                            // sdk会自动存入到此conversation中
                            if (chatType == CHATTYPE_SINGLE)
                                messages = conversation.loadMoreMsgFromDB(adapter
                                        .getItem(0).getMsgId(), pagesize);
                            else
                                messages = conversation.loadMoreGroupMsgFromDB(
                                        adapter.getItem(0).getMsgId(), pagesize);
                        } catch (Exception e1) {
                            loadmorePB.setVisibility(View.GONE);
                            return;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                        if (messages.size() != 0) {
                            // 刷新ui
                            adapter.notifyDataSetChanged();
                            listView.setSelection(messages.size() - 1);
                            if (messages.size() != pagesize)
                                haveMoreData = false;
                        } else {
                            haveMoreData = false;
                        }
                        loadmorePB.setVisibility(View.GONE);
                        isloading = false;

                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    /**
     * 转发消息
     *
     * @param forward_msg_id
     */
    // protected void forwardMessage(String forward_msg_id) {
    // EMMessage forward_msg = EMChatManager.getInstance().getMessage(
    // forward_msg_id);
    // EMMessage.Type type = forward_msg.getType();
    // switch (type) {
    // case TXT:
    // // 获取消息内容，发送消息
    // String content = ((TextMessageBody) forward_msg.getBody())
    // .getMessage();
    // sendText(content);
    // break;
    // case IMAGE:
    // // 发送图片
    // String filePath = ((ImageMessageBody) forward_msg.getBody())
    // .getLocalUrl();
    // if (filePath != null) {
    // File file = new File(filePath);
    // if (!file.exists()) {
    // // 不存在大图发送缩略图
    // filePath = ImageUtils.getThumbnailImagePath(filePath);
    // }
    // sendPicture(filePath);
    // }
    // break;
    // default:
    // break;
    // }
    // }

    /**
     * 监测群组解散或者被T事件
     *
     */
    class GroupListener extends GroupReomveListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            runOnUiThread(new Runnable() {
                String st13 = getResources().getString(R.string.you_are_group);

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(ChatActivity.this, st13, 1).show();
                        if (GroupDetailsActivity.instance != null)
                            GroupDetailsActivity.instance.finish();
                        finish();
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(final String groupId, String groupName) {
            // 群组解散正好在此页面，提示群组被解散，并finish此页面
            runOnUiThread(new Runnable() {
                String st14 = getResources().getString(
                        R.string.the_current_group);

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(ChatActivity.this, st14, 1).show();
                        if (GroupDetailsActivity.instance != null)
                            GroupDetailsActivity.instance.finish();
                        finish();
                    }
                }
            });
        }

    }

    public String getToChatUsername() {
        return toChatUsername;
    }

}
