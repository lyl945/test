package com.surong.leadloan.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.pj.core.datamodel.DataWrapper;
import com.pj.core.managers.LogManager;
import com.pj.core.utilities.StringUtility;
import com.surong.leadload.database.EASEDatabaseUserInfo;
import com.surong.leadloan.R;
import com.surong.leadloan.adapter.MessageAdapter;
import com.surong.leadloan.utils.ViewUtils;

public class MessageFragment extends BaseFragment {

	private View view;
	private ListView list;
	private List<EMConversation> conversationList;
	private MessageAdapter adapter;
	private EMConversation conversation;

	private NewMessageBroadcastReceiver msgReceiver;
	private TextView back, title, right;
	private boolean issystem;

	// public MessageFragment(Activity mainActivity) {
	// super();
	// mActivity = mainActivity;
	//
	// conversationList = new ArrayList<EMConversation>();
	// adapter = new MessageAdapter(mainActivity).setData(conversationList);
	// }
	public MessageFragment() {
		super();
		conversationList = new ArrayList<EMConversation>();
	}

	public void setmActivity(Activity mActivity) {
		super.setmActivity(mActivity);
		adapter = new MessageAdapter(mActivity).setData(conversationList);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// 设置消息提醒小圆点 消失
		mActivity.findViewById(R.id.messageHint).setVisibility(View.INVISIBLE);

		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(4);
		activity.registerReceiver(msgReceiver, intentFilter);

		LogManager.i(getClass().getSimpleName(), "onAttach");
	}

	@Override
	public void onDetach() {
		// 设置消息提醒小圆点 消失
		mActivity.findViewById(R.id.messageHint).setVisibility(View.INVISIBLE);
		mActivity.unregisterReceiver(msgReceiver);
		super.onDetach();
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
			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			// abortBroadcast();

			((BaseActivity) mActivity).notifyNewMessage(message);
			refreshConversions();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.message, container, false);
		back = (TextView) view.findViewById(R.id.top_textView_left);
		title = (TextView) view.findViewById(R.id.top_textview_title);
		right = (TextView) view.findViewById(R.id.top_textView_right);
		title.setText("消息");
		list = (ListView) view.findViewById(R.id.message_list);
		ViewUtils.setEmptyView(getActivity(), "暂无消息", list);
		list.setAdapter(adapter);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		refreshConversions();
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
			// EMMessage message =
			// EMMessage.createSendMessage(EMMessage.Type.TXT);
			// message.setChatType(ChatType.GroupChat);
			// TextMessageBody txtBody = new TextMessageBody("hh");
			// // 设置消息body
			// message.addBody(txtBody);
			// // 设置要发给谁,用户username或者群聊groupid
			// message.setReceipt("system");
			// // 把messgage加到conversation中
			// conversation = EMChatManager.getInstance().getConversation(
			// "system");
			// conversation.addMessage(message);
			// conversationList.add(conversation);

			EMMessage cmdMessage = EMMessage
					.createSendMessage(EMMessage.Type.CMD);
			CmdMessageBody body = new CmdMessageBody("action.system");
			body.params = new HashMap<String, String>();
				String tip = "我是领贷管家，您可以在这里进行建议、反馈，和我们一对一的沟通。";
			body.params.put("message", tip);
			cmdMessage.addBody(body);
			// 如果是群聊，设置chattype,默认是单聊
//			cmdMessage.setChatType(ChatType.GroupChat);
			cmdMessage.setReceipt("system");
			conversation = EMChatManager.getInstance()
					.getConversation("system");
			// 把messgage加到conversation中
			conversation.addMessage(cmdMessage);
			conversationList.add(conversation);
		}

		if (tmp != null) {
			conversationList.addAll(tmp);

			adapter.notifyDataSetChanged();
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
		sortConversationByLastChatTime(list);
		return list;
	}

	private void sortConversationByLastChatTime(
			List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1,
					final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}

	public void addNewApply(DataWrapper wrapper) {
		// 设置消息提醒小圆点
		mActivity.findViewById(R.id.messageHint).setVisibility(View.VISIBLE);
		adapter.addNewApply(wrapper);
	}
}
