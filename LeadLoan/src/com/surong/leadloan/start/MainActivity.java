package com.surong.leadloan.start;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.activity.ChatAllHistoryFragment;
import com.easemob.chatuidemo.activity.ContactlistFragment;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.easemob.chatuidemo.activity.SettingsFragment;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.domain.User;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.lidroid.xutils.exception.DbException;
import com.pj.core.NotificationCenter;
import com.pj.core.NotificationCenter.NotificationListener;
import com.pj.core.datamodel.DataWrapper;
import com.pj.core.managers.LogManager;
import com.pj.core.utilities.StringUtility;
import com.surong.leadload.database.EASEDatabaseApply;
import com.surong.leadload.database.EASEDatabaseUserInfo;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.personal.ApplyCertification;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.fragment.CRMFragment;
import com.surong.leadloan.fragment.ContactGroupFragment;
import com.surong.leadloan.fragment.MessageFragment;
import com.surong.leadloan.fragment.OrderFragment;
import com.surong.leadloan.fragment.PersonalFragment;
import com.surong.leadloan.ui.FiltDialog;
import com.surong.leadloan.utils.CreateFloatView;
import com.surong.leadloan.utils.Utils;
import com.umeng.analytics.MobclickAgent;

/*
 * @author 胡田
 * 登录主界面
 */
public class MainActivity extends BaseActivity implements OnClickListener,
		NotificationListener {

	/** 被好友接受 */
	public static final int N_APPLY_ACCEPTED = 82800;
	/** 被好友接受 */
	public static final int N_DELETED_BY_FRIEND = 82801;

	private TextView button[];// tab按钮
	private RelativeLayout relativeLayout[];// tab relative
	private ImageView imageButton[];// tab图片
	private int[] imageIds;
	private int[] imagePressedIds;
	private ContactGroupFragment contactGroupFragment;// 人脉圈
	private OrderFragment orderFragment;// 拿单甩单
	private CRMFragment crmFragment;// 客户管理
	private MessageFragment messageFragment;// 发现
	private PersonalFragment personalFragment;// 个人中心
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private CreateFloatView createFloatView;
	private Context context;
	private int lastClick;
	private int currentClick;
	private FiltDialog filtDialog;
	private View filtView;
	private static final int notifiId = 11;
	protected static final String TAG = "BaseFragment";
	// 未读消息textview
	private TextView unreadLabel;
	// 未读通讯录textview
	private TextView unreadAddressLable;
	int fragId;

	private Button[] mTabs;
	private ContactlistFragment contactListFragment;
	// private ChatHistoryFragment chatHistoryFragment;
	private ChatAllHistoryFragment chatHistoryFragment;
	private SettingsFragment settingFragment;
	private Fragment[] fragments;
	private int index;
	// 当前fragment的index
	private int currentTabIndex;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;

	private FrameLayout mFraLayout;
	private LinearLayout layout1, layout2, layout3, layout4, layout5;
	private TextView iknow1, iknow2, iknow3, iknow4, lookatBtn,
			accreditationBtn;
	private boolean isFirstIn = false;
	private Personal personal;
	private String stateString;

	/**
	 * 单点登录监听器
	 */
	private EMConnectionListener confligLoginListener = new EMConnectionListener() {

		@Override
		public void onDisconnected(int error) {
			if (error == EMError.CONNECTION_CONFLICT) {
				showConflictDialog();
			}
		}

		@Override
		public void onConnected() {

		}
	};

	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		Utils.initData(context);
		findView();
		initData();
		initAction();
		isFirstLogin();// 判断是否是第一次登入

		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED,
						false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理

			((LDApplication) LDApplication.getInstance()).logout(null);
			this.finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null
				&& savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			this.finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}

		// MobclickAgent.setDebugMode( true );
		// --?--
		MobclickAgent.updateOnlineConfig(this);

		if (this.getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (this.getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED,
				false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}

		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		// 这个fragment只显示好友和群组的聊天记录
		// chatHistoryFragment = new ChatHistoryFragment();
		// 显示所有人消息记录的fragment
		// chatHistoryFragment = new ChatAllHistoryFragment();
		// contactListFragment = new ContactlistFragment();
		// settingFragment = new SettingsFragment();
		// fragments = new Fragment[] { chatHistoryFragment,
		// contactListFragment, settingFragment };
		// 添加显示第一个fragment
		// getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
		// chatHistoryFragment)
		// .add(R.id.fragment_container,
		// contactListFragment).hide(contactListFragment).show(chatHistoryFragment).commit();

		// 注册一个接收消息的BroadcastReceiver
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		this.registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		this.registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// 注册一个透传消息的BroadcastReceiver
		IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getCmdMessageBroadcastAction());
		// cmdMessageIntentFilter.setPriority(3);
		// this.registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);

		cmdMessageIntentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		this.registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);

		// 注册一个离线消息的BroadcastReceiver
		offlineMessageReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String[] users = intent.getStringArrayExtra("fromuser");
				String[] groups = intent.getStringArrayExtra("fromgroup");
				if (users != null) {
					for (String user : users) {
						System.out.println("收到user离线消息：" + user);
						EMMessage message = EMChatManager.getInstance()
								.getMessage(user);
						if (message != null
								&& message.getType() == EMMessage.Type.CMD) {
							handleCmdMessage(message);
						}
					}
				}
				if (groups != null) {
					for (String group : groups) {
						System.out.println("收到group离线消息：" + group);
					}
				}
			}
		};
		IntentFilter offlineMessageIntentFilter = new IntentFilter(
				EMChatManager.getInstance().getOfflineMessageBroadcastAction());
		registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);

		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// 注册一个监听连接状态的listener
		// EMChatManager.getInstance().addConnectionListener( new
		// MyConnectionListener());
		EMChatManager.getInstance().addConnectionListener(confligLoginListener);

		// // 注册群聊相关的listener
		// EMGroupManager.getInstance().addGroupChangeListener(new
		// MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();

		NotificationCenter.getDefaultCenter().addNotificationListener(this,
				null, N_APPLY_ACCEPTED);
	}

	private void isFirstLogin() {
		SharedPreferences preferences = getSharedPreferences("config",
				MODE_PRIVATE);
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirst", false);
		if (isFirstIn) {
			mFraLayout.setVisibility(View.GONE);
		}
	}

	public void switchContent(Fragment from, Fragment to, String toTag) {
		if (from != to) {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(R.id.content, to, toTag).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
			// currentFragment = to;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(cmdMessageReceiver);
		} catch (Exception e) {
		}

		try {
			unregisterReceiver(offlineMessageReceiver);
		} catch (Exception e) {
		}

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}
		EMChatManager.getInstance().removeConnectionListener(
				confligLoginListener);
		NotificationCenter.getDefaultCenter().removeNotificationListener(this,
				null, N_APPLY_ACCEPTED);
	}

	private void findView() {

		button = new TextView[5];
		button[0] = (TextView) findViewById(R.id.button1);
		button[1] = (TextView) findViewById(R.id.button2);
		button[2] = (TextView) findViewById(R.id.button3);
		button[3] = (TextView) findViewById(R.id.button4);
		button[4] = (TextView) findViewById(R.id.button5);
		relativeLayout = new RelativeLayout[5];
		relativeLayout[0] = (RelativeLayout) findViewById(R.id.relative_1);
		relativeLayout[1] = (RelativeLayout) findViewById(R.id.relative_2);
		relativeLayout[2] = (RelativeLayout) findViewById(R.id.relative_3);
		relativeLayout[3] = (RelativeLayout) findViewById(R.id.relative_4);
		relativeLayout[4] = (RelativeLayout) findViewById(R.id.relative_5);
		imageButton = new ImageView[5];
		imageButton[0] = (ImageView) findViewById(R.id.image_1);
		imageButton[1] = (ImageView) findViewById(R.id.image_2);
		imageButton[2] = (ImageView) findViewById(R.id.image_3);
		imageButton[3] = (ImageView) findViewById(R.id.image_4);
		imageButton[4] = (ImageView) findViewById(R.id.image_5);
		// 首次登入布局
		mFraLayout = (FrameLayout) findViewById(R.id.frame_explain);
		layout1 = (LinearLayout) findViewById(R.id.linearlayout_1);
		layout2 = (LinearLayout) findViewById(R.id.linearlayout_2);
		layout3 = (LinearLayout) findViewById(R.id.linearlayout_3);
		layout4 = (LinearLayout) findViewById(R.id.linearlayout_4);
		layout5 = (LinearLayout) findViewById(R.id.linearlayout_5);
		iknow1 = (TextView) findViewById(R.id.i_know_1);
		iknow2 = (TextView) findViewById(R.id.i_know_2);
		iknow3 = (TextView) findViewById(R.id.i_know_3);
		iknow4 = (TextView) findViewById(R.id.i_know_4);
		lookatBtn = (TextView) findViewById(R.id.look_btn);
		accreditationBtn = (TextView) findViewById(R.id.accreditation_btn);
		hideApplyCertification();
		iknow1.setOnClickListener(this);
		iknow2.setOnClickListener(this);
		iknow3.setOnClickListener(this);
		iknow4.setOnClickListener(this);
		lookatBtn.setOnClickListener(this);
		accreditationBtn.setOnClickListener(this);
		layout1.setVisibility(View.VISIBLE);

	}

	private void hideApplyCertification() {
		// TODO Auto-generated method stub
		try {
			personal = CommonActivity.db.findFirst(Personal.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (personal != null) {
			stateString = personal.getAuthStatus();
		}
			if ("02".equals(stateString)) {
				accreditationBtn.setVisibility(View.GONE);
			}
	}

	private void initData() {

		filtDialog = new FiltDialog(context, R.style.filt_dialog);
		fragId = getIntent().getIntExtra("crm", -1);
		currentClick = 0;
		// lastClick = currentClick;
		// change(lastClick, currentClick);
		// 初始化tab图片信息
		TypedArray tab_image = context.getResources().obtainTypedArray(
				R.array.tab_image);
		TypedArray tab_image_pressed = context.getResources().obtainTypedArray(
				R.array.tab_image_pressed);
		int len = tab_image.length();
		imageIds = new int[len];
		imagePressedIds = new int[len];
		for (int i = 0; i < tab_image.length(); i++)
			imageIds[i] = tab_image.getResourceId(i, 0);
		for (int i = 0; i < tab_image_pressed.length(); i++)
			imagePressedIds[i] = tab_image_pressed.getResourceId(i, 0);
		tab_image.recycle();
		tab_image_pressed.recycle();
		// 初始化fragment
		contactGroupFragment = new ContactGroupFragment();
		contactGroupFragment.setmActivity(this);
		orderFragment = new OrderFragment();
		orderFragment.setmActivity(this);

		crmFragment = new CRMFragment();
		crmFragment.setmActivity(this);

		messageFragment = new MessageFragment();
		messageFragment.setmActivity(this);
		personalFragment = new PersonalFragment();
		personalFragment.setmActivity(this);

		fragmentManager = getFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content, contactGroupFragment);
		fragmentTransaction.commit();
		/*
		 * 创建悬浮框
		 */
		/*
		 * createFloatView = new CreateFloatView();
		 * createFloatView.createFloatView(context);
		 */

	}

	private void initAction() {

		for (int i = 0; i < relativeLayout.length; i++) {
			relativeLayout[i].setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		switch (i) {
		case R.id.relative_1:
			lastClick = currentClick;
			currentClick = 0;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, contactGroupFragment);
			fragmentTransaction.commit();
			break;
		case R.id.relative_2:
			lastClick = currentClick;
			currentClick = 1;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, orderFragment);
			fragmentTransaction.commit();
			break;
		case R.id.relative_3:
			lastClick = currentClick;
			currentClick = 2;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, messageFragment);
			fragmentTransaction.commit();
			break;
		case R.id.relative_4:
			lastClick = currentClick;
			currentClick = 3;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, crmFragment);
			fragmentTransaction.commit();
			break;
		case R.id.relative_5:
			lastClick = currentClick;
			currentClick = 4;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, personalFragment);
			fragmentTransaction.commit();
			break;
		case R.id.relative_back:

			break;

		case R.id.i_know_1:
			lastClick = currentClick;
			currentClick = 1;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, orderFragment);
			fragmentTransaction.commit();
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			SharedPreferences sharedPre = getSharedPreferences("config",
					MODE_PRIVATE);
			// 获取Editor对象
			Editor editor = sharedPre.edit();
			editor.putBoolean("isFirst", true);
			editor.commit();
			break;
		case R.id.i_know_2:
			lastClick = currentClick;
			currentClick = 2;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, messageFragment);
			fragmentTransaction.commit();
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.VISIBLE);
			break;
		case R.id.i_know_3:
			lastClick = currentClick;
			currentClick = 3;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, crmFragment);
			fragmentTransaction.commit();
			layout3.setVisibility(View.GONE);
			layout4.setVisibility(View.VISIBLE);
			break;
		case R.id.i_know_4:
			lastClick = currentClick;
			currentClick = 4;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, personalFragment);
			fragmentTransaction.commit();
			layout4.setVisibility(View.GONE);
			layout5.setVisibility(View.VISIBLE);
			break;
		case R.id.look_btn:
			lastClick = currentClick;
			currentClick = 0;
			change(lastClick, currentClick);
			fragmentTransaction.replace(R.id.content, contactGroupFragment);
			fragmentTransaction.commit();
			layout5.setVisibility(View.GONE);
			break;
		case R.id.accreditation_btn:
			Intent intent = new Intent(this, ApplyCertification.class);
			startActivity(intent);
			layout5.setVisibility(View.GONE);
			break;

		}

	}

	private void change(int lastClick, int currentClick) {
		// 上一个按钮恢复原状
		relativeLayout[lastClick].setBackgroundColor(getResources().getColor(
				R.color.tab_back));
		button[lastClick].setTextColor(getResources()
				.getColor(R.color.text_tab));
		imageButton[lastClick].setBackgroundResource(imageIds[lastClick]);
		// 当前按钮被点击
		relativeLayout[currentClick].setBackgroundColor(getResources()
				.getColor(R.color.white));
		button[currentClick].setTextColor(getResources().getColor(
				R.color.text_tab_click));
		imageButton[currentClick]
				.setBackgroundResource(imagePressedIds[currentClick]);

	}

	// /**
	// * 刷新未读消息数
	// */
	// public void updateUnreadLabel() {
	// int count = getUnreadMsgCountTotal();
	// if (count > 0) {
	// unreadLabel.setText(String.valueOf(count));
	// unreadLabel.setVisibility(View.VISIBLE);
	// } else {
	// unreadLabel.setVisibility(View.INVISIBLE);
	// }
	// }

	// /**
	// * 刷新申请与通知消息数
	// */
	// public void updateUnreadAddressLable() {
	// runOnUiThread(new Runnable() {
	// public void run() {
	// int count = getUnreadAddressCountTotal();
	// if (count > 0) {
	// unreadAddressLable.setText(String.valueOf(count));
	// unreadAddressLable.setVisibility(View.VISIBLE);
	// } else {
	// unreadAddressLable.setVisibility(View.INVISIBLE);
	// }
	// }
	// });
	//
	// }

	/**
	 * 获取未读申请与通知消息
	 *
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (((LDApplication) LDApplication.getInstance()).getContactList().get(
				Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = ((LDApplication) LDApplication
					.getInstance()).getContactList()
					.get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 *
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	/**
	 * 新消息广播接收者
	 *
	 *
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

			String from = intent.getStringExtra("from");
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			// 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
			if (ChatActivity.activityInstance != null) {
				if (message.getChatType() == ChatType.GroupChat) {
					if (message.getTo().equals(
							ChatActivity.activityInstance.getToChatUsername()))
						return;
				} else {
					if (from.equals(ChatActivity.activityInstance
							.getToChatUsername()))
						return;
				}
			}

			if (message.getType() == Type.CMD) {
				handleCmdMessage(message);
			}

			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();

			notifyNewMessage(message);

			// 刷新bottom bar消息未读数
			// updateUnreadLabel();
			if (currentTabIndex == 0) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (chatHistoryFragment != null) {
					chatHistoryFragment.refresh();
				}
			}

		}
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

					// 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
					if (ChatActivity.activityInstance != null) {
						if (msg.getChatType() == ChatType.Chat) {
							if (from.equals(ChatActivity.activityInstance
									.getToChatUsername()))
								return;
						}
					}

					msg.isAcked = true;
				}
			}

		}
	};

	/**
	 * 透传消息BroadcastReceiver
	 */
	private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			abortBroadcast();
			EMLog.d(TAG, "收到透传消息");
			// 获取cmd message对象
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = intent.getParcelableExtra("message");
			handleCmdMessage(message);
		}
	};

	/**
	 * 普通消息
	 */
	private BroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 设置消息提醒小圆点
			findViewById(R.id.messageHint).setVisibility(View.VISIBLE);
			// 获取cmd message对象
			// String msgId = intent.getStringExtra("msgid");
			// EMMessage message = intent.getParcelableExtra("message");
			// abortBroadcast();
			// EMLog.d(TAG, "收到SYSTEM消息");
			// handleCmdMessage(message);
		}
	};

	/**
	 * 离线消息BroadcastReceiver sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI
	 * 有哪些人发来了离线消息 UI 可以做相应的操作，比如下载用户信息
	 */
	private BroadcastReceiver offlineMessageReceiver = null;

	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	/***
	 * 好友变化listener
	 *
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = ((LDApplication) LDApplication
					.getInstance()).getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			if (currentTabIndex == 1)
				contactListFragment.refresh();

		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// // 被删除
			// Map<String, User> localUsers =
			// DemoApplication.getInstance().getContactList();
			// for (String username : usernameList) {
			// localUsers.remove(username);
			// userDao.deleteContact(username);
			// inviteMessgeDao.deleteMessage(username);
			// }
			// runOnUiThread(new Runnable() {
			// public void run() {
			// // 如果正在与此用户的聊天页面
			// String st10 =
			// getResources().getString(R.string.have_you_removed);
			// if (ChatActivity.activityInstance != null &&
			// usernameList.contains(ChatActivity.activityInstance.getToChatUsername()))
			// {
			// Toast.makeText(MainActivity.this,
			// ChatActivity.activityInstance.getToChatUsername() + st10,
			// 1).show();
			// ChatActivity.activityInstance.finish();
			// }
			// updateUnreadLabel();
			// // 刷新ui
			// if (currentTabIndex == 1)
			// contactListFragment.refresh();
			// else if(currentTabIndex == 0)
			// chatHistoryFragment.refresh();
			// }
			// });

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
			Log.d(username, username + "拒绝了你的好友请求");
		}

	}

	/**
	 * 保存提示新消息
	 *
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(this.getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		// updateUnreadAddressLable();
		// 刷新好友页面ui
		if (currentTabIndex == 1)
			contactListFragment.refresh();
	}

	/**
	 * 保存邀请等msg
	 *
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = ((LDApplication) LDApplication.getInstance())
				.getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * set head
	 *
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}

	// /**
	// * 连接监听listener
	// *
	// */
	// private class MyConnectionListener implements EMConnectionListener {
	//
	// @Override
	// public void onConnected() {
	// // runOnUiThread(new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// // chatHistoryFragment.errorItem.setVisibility(View.GONE);
	// // }
	// //
	// // });
	// }
	//
	// @Override
	// public void onDisconnected(final int error) {
	// // final String st1 =
	// // getResources().getString(R.string.Less_than_chat_server_connection);
	// // final String st2 =
	// // getResources().getString(R.string.the_current_network);
	// // runOnUiThread(new Runnable() {
	// //
	// // @Override
	// // public void run() {
	// // if(error == EMError.USER_REMOVED){
	// // // 显示帐号已经被移除
	// // showAccountRemovedDialog();
	// // }else if (error == EMError.CONNECTION_CONFLICT) {
	// // // 显示帐号在其他设备登陆dialog
	// showConflictDialog();
	// // } else {
	// // chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
	// // if (NetUtils.hasNetwork(MainActivity.this))
	// // chatHistoryFragment.errorText.setText(st1);
	// // else
	// // chatHistoryFragment.errorText.setText(st2);
	// //
	// // }
	// // }
	// //
	// // });
	// }
	// }

	@Override
	protected void onResume() {
		super.onResume();
		if (!isConflict || !isCurrentAccountRemoved) {
			// updateUnreadLabel();
			// updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

		if (fragId > 0 && fragId == 4) {
			fragId = -1;
			lastClick = currentClick;
			currentClick = 3;
			change(lastClick, currentClick);
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.content, crmFragment);
			fragmentTransaction.commit();
		} else if (fragId > 0 && fragId == 3) {
			fragId = -1;
			lastClick = currentClick;
			currentClick = 2;
			change(lastClick, currentClick);
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.content, messageFragment);
			fragmentTransaction.commit();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private android.app.AlertDialog.Builder accountRemovedBuilder;
	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		Message msg = new Message();
		msg.what = 0;
		handler.sendMessage(msg);
	}

	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		((LDApplication) LDApplication.getInstance()).logout(null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage(R.string.em_user_remove);
				accountRemovedBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								accountRemovedBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(TAG,
						"---------color userRemovedBuilder error"
								+ e.getMessage());
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra("conflict", false)
				&& !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)
				&& !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}

	}

	/*
	 * long time = 0; public boolean onKeyDown(int keyCode, KeyEvent event) { //
	 * 
	 * if (keyCode == event.KEYCODE_BACK) { if (System.currentTimeMillis() -
	 * time > 2000) { Constans.Toast(MainActivity.this, "再按一次退出速融壹佰"); time =
	 * System.currentTimeMillis(); } else {
	 * 
	 * finish(); Intent i =
	 * context.getPackageManager().getLaunchIntentForPackage(
	 * context.getPackageName()); i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 * context.startActivity(i); } return true; }
	 * 
	 * return super.onKeyDown(keyCode, event); }
	 */

	private void handleCmdMessage(EMMessage message) {
		sendBroadcast(new Intent("shua"));
		// 获取消息body
		CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
		String action = cmdMsgBody.action;// 获取自定义action

		LogManager.i(getClass().getSimpleName(), action);

		if ("sendAddFriend".equalsIgnoreCase(action)) {
			// 好友申请来了

			String userName = message.getStringAttribute("displayName", "--");
			if (StringUtility.isEmpty(userName)) {
				userName = "--";
			}
			String msg = message.getStringAttribute("message", userName
					+ " 请求加你为好友");
			String time = message.getStringAttribute("addTime", "");
			if (StringUtility.isEmpty(msg)) {
				msg = userName + " 请求加你为好友";
			}
			String userId = message.getFrom();
			String img = message.getStringAttribute("headImgPath", "");
			// NSString *to = message.to;

			DataWrapper wrapper = new DataWrapper();
			wrapper.setObject(EASEDatabaseApply.ApplyApplicantID, userId);
			wrapper.setObject(EASEDatabaseApply.ApplyApplicantNickname,
					userName);
			wrapper.setObject(EASEDatabaseApply.ApplyApplicantImage, img);
			wrapper.setObject(EASEDatabaseApply.ApplyType,
					Integer.valueOf(EASEDatabaseApply.ApplyStyleFriend));
			wrapper.setObject(EASEDatabaseApply.ApplyReason, msg);
			wrapper.setObject(EASEDatabaseApply.ApplyTime, message.getMsgTime());
			messageFragment.addNewApply(wrapper);

			EMMessage tmpMessage = EMMessage
					.createReceiveMessage(EMMessage.Type.TXT);
			tmpMessage.setFrom(userName);
			tmpMessage.addBody(new TextMessageBody(msg));
			notifyNewMessage(message);

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification();
			notification.icon = R.drawable.logo_icon;
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE;

			// 手机通知栏消息
			RemoteViews mRemoteViews = new RemoteViews(
					context.getPackageName(), R.layout.notifycation_activity);
			mRemoteViews.setTextViewText(R.id.notify_title, userName);
			mRemoteViews.setTextViewText(R.id.notify_time, time);
			mRemoteViews.setTextViewText(R.id.notify_content, msg);
			notification.contentView = mRemoteViews;
			Intent intent1 = new Intent(context, MainActivity.class);
			if ("sendAddFriend".equalsIgnoreCase(action)) {
				intent1.putExtra("crm", 3);// 传个值给activity,在跳转到相应的fragment
			}
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pendingIntent = PendingIntent.getActivity(context,
					R.string.app_name, intent1,
					PendingIntent.FLAG_UPDATE_CURRENT);
			notification.contentIntent = pendingIntent;
			notification.contentView = mRemoteViews;
			notificationManager.notify(1000, notification);

			return;
		} else if ("sendDeleteFriend".equalsIgnoreCase(action)) {
			// 被删除了
			String userId = message.getFrom(); // 操作方ID

			EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(
					this);
			databaseUserInfo.setIsMyFriend(false, userId);

			NotificationCenter.getDefaultCenter().sendNotification(this,
					N_DELETED_BY_FRIEND, userId);

		} else if ("sendAcceptedFriend".equalsIgnoreCase(action)) {
			// 加好友被接受
			String userId = message.getFrom(); // 操作方ID

			EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(
					this);
			databaseUserInfo.setIsMyFriend(true, userId);

			NotificationCenter.getDefaultCenter().sendNotification(this,
					N_DELETED_BY_FRIEND, userId);

			String userName = message.getStringAttribute("displayName", "--");
			if (StringUtility.isEmpty(userName)) {
				userName = "--";
			}
			String msg = message.getStringAttribute("message", userName
					+ " 同意了你的好友请求");
			if (StringUtility.isEmpty(msg)) {
				msg = userName + " 同意了你的好友请求";
			}
			String img = message.getStringAttribute("headImgPath", "");
			DataWrapper wrapper = new DataWrapper();
			wrapper.setObject(EASEDatabaseApply.ApplyApplicantID, userId);
			wrapper.setObject(EASEDatabaseApply.ApplyApplicantNickname,
					userName);
			wrapper.setObject(EASEDatabaseApply.ApplyApplicantImage, img);
			wrapper.setObject(EASEDatabaseApply.ApplyType,
					Integer.valueOf(EASEDatabaseApply.ApplyFriend));
			wrapper.setObject(EASEDatabaseApply.ApplyReason, msg);
			wrapper.setObject(EASEDatabaseApply.ApplyTime, message.getMsgTime());
			messageFragment.addNewApply(wrapper);

			// String from = message.getFrom();
			// EMMessage tmpMessage = EMMessage
			// .createReceiveMessage(EMMessage.Type.TXT);
			// tmpMessage.setFrom(userId);
			// tmpMessage.addBody(new TextMessageBody(msg));
			// notifyNewMessage(message);
			//
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification();
			notification.defaults = Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE;
			// notification.flags |= Notification.FLAG_INSISTENT;// 一直进行
			// notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音
			// notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动
			// notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动
			//
			notificationManager.notify(1000, notification);

		} else if ("sendOrderAction".equalsIgnoreCase(action)
				|| "sendAuthPassAction".equalsIgnoreCase(action)) {
			String nofityContent, nofityTime, notifyTitle;
			try {
				nofityContent = message.getStringAttribute("message");
				nofityTime = message.getStringAttribute("addTime");
				notifyTitle = message.getStringAttribute("displayName");

				NotificationManager notificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification();
				notification.tickerText = "系统消息";
				notification.icon = R.drawable.logo_icon;
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				notification.defaults = Notification.DEFAULT_SOUND
						| Notification.DEFAULT_VIBRATE;

				// 手机通知栏消息
				RemoteViews mRemoteViews = new RemoteViews(
						context.getPackageName(),
						R.layout.notifycation_activity);
				mRemoteViews.setTextViewText(R.id.notify_title, notifyTitle);
				mRemoteViews.setTextViewText(R.id.notify_time, nofityTime);
				mRemoteViews
						.setTextViewText(R.id.notify_content, nofityContent);

				notification.contentView = mRemoteViews;

				if ("sendAuthPassAction".equalsIgnoreCase(action)) {// 申请认证通过的透传消息
					Utils.reLogin(context);// 重新登陆 刷新数据
				}
				Intent intent1 = new Intent(context, MainActivity.class);
				if ("sendOrderAction".equalsIgnoreCase(action)) {
					intent1.putExtra("crm", 4);// 传个值给activity,在跳转到相应的fragment
				}
				intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, R.string.app_name, intent1,
						PendingIntent.FLAG_UPDATE_CURRENT);
				notification.contentIntent = pendingIntent;
				notification.contentView = mRemoteViews;
				notificationManager.notify(1000, notification);
			} catch (EaseMobException e) {
				e.printStackTrace();
			}
			// 获取扩展属性 此处省略
			// message.getStringAttribute("");
			EMLog.d(TAG,
					String.format("透传消息：action:%s,message:%s", action,
							message.toString()));
			String st9 = getResources().getString(
					R.string.receive_the_passthrough);
		} else {

		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 有其他人登陆
			case 0:
				isConflictDialogShow = true;
				((LDApplication) LDApplication.getInstance()).logout(null);
				String st = getResources().getString(
						R.string.Logoff_notification);
				if (!MainActivity.this.isFinishing()) {
					// clear up global variables
					try {
						if (conflictBuilder == null)
							conflictBuilder = new android.app.AlertDialog.Builder(
									MainActivity.this);
						conflictBuilder.setTitle(st);
						conflictBuilder.setMessage(R.string.connect_conflict);
						conflictBuilder.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										conflictBuilder = null;
										// 显示帐号在其他设备登陆
										// 清除数据库的member
										try {
											// DbUtils dbUtils = DbUtils
											// .create(getApplication());
											CommonActivity.db
													.dropTable(Personal.class);
										} catch (DbException e) {
											e.printStackTrace();
										}

										// 退出环信
										EMChatManager.getInstance().logout();// 此方法为同步方法

										Intent i = context
												.getPackageManager()
												.getLaunchIntentForPackage(
														context.getPackageName());
										i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(i);

										finish();
									}
								});
						conflictBuilder.setCancelable(false);
						conflictBuilder.create().show();
						isConflict = true;
					} catch (Exception e) {
						e.printStackTrace();
						EMLog.e(TAG,
								"---------color conflictBuilder error"
										+ e.getMessage());
					}

				}
				break;
			}
		}
	};

	@Override
	public void onReceivedNotification(Object arg0, int arg1, Object arg2) {
		if (arg0 != MainActivity.this) {
			contactGroupFragment.refreshFriendList();
		}
	}
}
