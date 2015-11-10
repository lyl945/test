package com.surong.leadloan.adapter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.applib.model.EaseUser;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.res.EASEConstants;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.pj.core.NotificationCenter;
import com.pj.core.datamodel.DataWrapper;
import com.pj.core.http.HttpImage;
import com.pj.core.http.HttpRequest;
import com.pj.core.http.HttpRequest.HttpRequestListener;
import com.pj.core.http.HttpResult;
import com.pj.core.utilities.DimensionUtility;
import com.pj.core.utilities.StringUtility;
import com.surong.leadload.api.BaiduMapTool;
import com.surong.leadload.api.data.AddFriendEntry;
import com.surong.leadload.database.EASEDatabaseApply;
import com.surong.leadload.database.EASEDatabaseUserInfo;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.personal.MShopActivity;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.fragment.MenuDialog;
import com.surong.leadloan.start.LoginActivity;
import com.surong.leadloan.start.MainActivity;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;

public class MessageAdapter extends BaseAdapter implements
		View.OnClickListener, HttpRequestListener, View.OnLongClickListener {
	private LayoutInflater inflater;
	private String mText;
	private List<EMConversation> conversationList = Collections.EMPTY_LIST;

	private List<DataWrapper> applyList;
	private EASEDatabaseApply databaseApply;
	private EASEDatabaseUserInfo databaseUserInfo;
	// private Context mContext;
	private DisplayImageOptions options;

	private Activity activity;

	class Holder {
		ImageView icon;
		TextView title;
		TextView message;
		TextView time;

		Button unreadMsgCount;
	}

	public MessageAdapter(Activity activity) {
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
		databaseApply = new EASEDatabaseApply(activity);
		databaseUserInfo = new EASEDatabaseUserInfo(activity);
		applyList = new ArrayList<DataWrapper>();

		loadApplys();
	}

	private void loadApplys() {
		EaseUser user = (EaseUser) LDApplication.getInstance().getSessionData(
				EASEConstants.CURRENT_USER);
		if (user == null) {
			return;
		}
		List<DataWrapper> list = databaseApply.listApplicant(
				user.getEaseMobAccount(), EASEDatabaseApply.ApplyStateAgree
						| EASEDatabaseApply.ApplyStateIgnore
						| EASEDatabaseApply.ApplyStatePending
						| EASEDatabaseApply.ApplyStateRefuse);
		if (list != null) {
			applyList.addAll(list);

			notifyDataSetChanged();
		}
	}

	public MessageAdapter setData(List<EMConversation> data) {
		conversationList = data;
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.default_avatar)
		.showImageOnFail(R.drawable.default_avatar).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer()).build();
		return this;
	}

	public MessageAdapter setText(String text) {
		mText = text;
		return this;
	}

	@Override
	public int getCount() {
		return conversationList.size() + applyList.size();
	}

	@Override
	public Object getItem(int position) {

		return isApplyLocation(position) ? applyList.get(position)
				: conversationList.get(getConversionLocation(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void removeItem(int pos) {
		if (isApplyLocation(pos)) {
			applyList.remove(pos);
		} else {
			conversationList.remove(getConversionLocation(pos));
		}

		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return isApplyLocation(position) ? 0 : 1;
	}

	public void addNewApply(DataWrapper wrapper) {
		String applyUsername = wrapper
				.getString(EASEDatabaseApply.ApplyApplicantID);
		int style = wrapper.getInt(EASEDatabaseApply.ApplyType);

		if (!StringUtility.isEmpty(applyUsername)) {

			for (int i = applyList.size() - 1; i >= 0; i--) {
				DataWrapper oldEntity = applyList.get(i);
				int oldStyle = oldEntity.getInt(EASEDatabaseApply.ApplyType);
				if (oldStyle == style
						&& applyUsername.equals(oldEntity
								.getString(EASEDatabaseApply.ApplyApplicantID))) {
					if (style != EASEDatabaseApply.ApplyStyleFriend) {
						// String newGroupid = wrapper
						// .getString(EASEDatabaseApply.ApplyGroupId);
						// if (!StringUtility.isEmpty(newGroupid)
						// || newGroupid
						// .equals(oldEntity
						// .getString(EASEDatabaseApply.ApplyGroupId))) {
						//
						// break;
						oldEntity
								.setObject(
										EASEDatabaseApply.ApplyReason,
										wrapper.getString(EASEDatabaseApply.ApplyReason));
						oldEntity
								.setObject(
										EASEDatabaseApply.ApplyState,
										Integer.valueOf(EASEDatabaseApply.ApplyStateAgree));
						oldEntity.setObject(EASEDatabaseApply.ApplyTime,
								wrapper.getObject(EASEDatabaseApply.ApplyTime));
						databaseApply.insertOrUpdateApplicant(oldEntity);

						applyList.remove(oldEntity);
						applyList.add(0, oldEntity);

						notifyDataSetChanged();
						return;

					}

					oldEntity.setObject(EASEDatabaseApply.ApplyReason,
							wrapper.getString(EASEDatabaseApply.ApplyReason));
					oldEntity.setObject(EASEDatabaseApply.ApplyState, Integer
							.valueOf(EASEDatabaseApply.ApplyStatePending));
					oldEntity.setObject(EASEDatabaseApply.ApplyTime,
							wrapper.getObject(EASEDatabaseApply.ApplyTime));
					databaseApply.insertOrUpdateApplicant(oldEntity);

					applyList.remove(oldEntity);
					applyList.add(0, oldEntity);

					notifyDataSetChanged();

					return;
				}
			}

			// new apply

			EaseUser myself = (EaseUser) LDApplication.getInstance()
					.getSessionData(EASEConstants.CURRENT_USER);
			if (myself == null) {
				return;
			}
			String loginUserName = StringUtility.select(true,
					myself.getDisplayName(), myself.getRealName(), "--");

			String myId = myself.getEaseMobAccount();

			wrapper.setObject(EASEDatabaseApply.ApplyReceiverID, myId);
			wrapper.setObject(EASEDatabaseApply.ApplyReceiverNickname,
					loginUserName);
			if (style == EASEDatabaseApply.ApplyFriend) {
				wrapper.setObject(EASEDatabaseApply.ApplyState,
						Integer.valueOf(EASEDatabaseApply.ApplyStateAgree));

			} else {
				wrapper.setObject(EASEDatabaseApply.ApplyState,
						Integer.valueOf(EASEDatabaseApply.ApplyStatePending));

			}
			wrapper.setObject(EASEDatabaseApply.ApplyReceiverImage,
					myself.getHeadImgPath());

			databaseApply.insertOrUpdateApplicant(wrapper);

			applyList.add(0, wrapper);
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		if (isApplyLocation(position)) {
			if (view == null) {
				view = inflater.inflate(
						R.layout.message_listview_friend_notice_item, parent,
						false);
			}

			TextView time = (TextView) view
					.findViewById(R.id.message_item_time);
			TextView title = (TextView) view
					.findViewById(R.id.message_item_title);
			TextView message = (TextView) view
					.findViewById(R.id.message_item_message);
			Button addButton = (Button) view
					.findViewById(R.id.message_item_add_friend);

			final DataWrapper wrapper = applyList.get(position);
			title.setText(wrapper
					.getString(EASEDatabaseApply.ApplyApplicantNickname));
			message.setText(wrapper.getString(EASEDatabaseApply.ApplyReason));
			try {
				time.setText(new Date(wrapper
						.getLong(EASEDatabaseApply.ApplyTime)).toLocaleString());
			} catch (Exception e) {
				time.setText("");
			}

			addButton.setTag(R.id.message_item_add_friend, wrapper);
			addButton.setOnClickListener(this);

			int state = wrapper.getInt(EASEDatabaseApply.ApplyState);
			if (state == EASEDatabaseApply.ApplyStateAgree) {
				addButton.setText(R.string.apply_accepted);
			} else if (state == EASEDatabaseApply.ApplyStatePending) {
				addButton.setText(R.string.apply_add);
			} else if (state == EASEDatabaseApply.ApplyStateIgnore) {
				addButton.setText(R.string.apply_ignored);
			} else if (state == EASEDatabaseApply.ApplyStateRefuse) {
				addButton.setText(R.string.apply_irefused);
			}

			addButton.setEnabled(state == EASEDatabaseApply.ApplyStatePending);
			view.setTag(R.layout.message, Integer.valueOf(position));
			view.setOnLongClickListener(this);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent i1 = new Intent(activity, MShopActivity.class);
					i1.putExtra("messageAddId", wrapper
							.getString(EASEDatabaseApply.ApplyApplicantID));
					activity.startActivity(i1);
				}
			});
			return view;
		}

		Holder holder = null;
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.message_listview_item, parent,
					false);

			view.setTag(holder);

			holder.icon = (ImageView) view.findViewById(R.id.message_item_icon);
			holder.time = (TextView) view.findViewById(R.id.message_item_time);
			holder.title = (TextView) view
					.findViewById(R.id.message_item_title);
			holder.message = (TextView) view
					.findViewById(R.id.message_item_message);
			holder.unreadMsgCount = (Button) view
					.findViewById(R.id.message_item_msg_count);
		} else {
			holder = (Holder) view.getTag();
		}

		EMConversation data = conversationList
				.get(getConversionLocation(position));
		String name = conversationList.get(getConversionLocation(position))
				.getUserName();

		holder.message.setText(getSubTitleMessageByConversation(
				inflater.getContext(), data));
		holder.time.setText(new Time(data.getLastMessage().getMsgTime())
				.toLocaleString());

		DataWrapper info = databaseUserInfo.getUserInfoByUserID(data
				.getUserName());

		if (info == null) {
			info = new DataWrapper();
		}

		holder.title.setText(StringUtility.select(true,
				info.getString(EASEDatabaseUserInfo.UserDisplayName),
				info.getString(EASEDatabaseUserInfo.UserRealName), "--"));

		int unreadCount = data.getUnreadMsgCount();
		if (unreadCount < 1) {
			holder.unreadMsgCount.setVisibility(View.INVISIBLE);
		} else {
			holder.unreadMsgCount.setVisibility(View.VISIBLE);

			holder.unreadMsgCount.setText(unreadCount + "");
		}
//
//		HttpImage.getInstance().setImage(
//				info.getString(EASEDatabaseUserInfo.UserImg), holder.icon,
//				R.drawable.default_avatar, DimensionUtility.dp2px(40) * 0.5f);
		
		ImageLoader.getInstance().displayImage(info.getString(EASEDatabaseUserInfo.UserImg),
				holder.icon, options, null);
		view.setOnClickListener(this);
		view.setOnLongClickListener(this);
		view.setTag(R.layout.message, Integer.valueOf(position));
		view.setTag(R.id.msg_item_ctr, data);

		return view;
	}

	public boolean isApplyLocation(int loc) {
		return (loc < applyList.size() && loc > -1);
	}

	public int getConversionLocation(int loc) {
		return loc - applyList.size();
	}

	// 得到最后消息文字或者类型
	public static Spannable getSubTitleMessageByConversation(Context context,
			EMConversation conversation) {

		String ret = "";
		EMMessage lastMessage = conversation.getLastMessage();
		if (lastMessage != null) {
			switch (lastMessage.getType()) {
			case IMAGE: {
				ret = "[图片]";
				break;
			}
			case TXT: {
				// 表情映射。
				TextMessageBody txtBody = (TextMessageBody) lastMessage
						.getBody();
				return SmileUtils.getSmiledText(context, txtBody.getMessage());
			}
			case VOICE: {
				ret = "[声音]";
			}
				break;
			case LOCATION: {
				ret = "[位置]";
			}
				break;
			case VIDEO: {
				ret = "[视频]";
			}
				break;
			default: {
			}
				break;
			}
		}

		return Spannable.Factory.getInstance().newSpannable(ret);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.message_item_add_friend) {
			DataWrapper wrapper = (DataWrapper) v.getTag(v.getId());
			if (wrapper != null) {
				// 加好友
				agreeFriendApply(
						Integer.toString(AddFriendEntry.CATEGORY_FRIEND),
						wrapper.getString(EASEDatabaseApply.ApplyApplicantID),
						wrapper);
			}
		} else if (v.getId() == R.id.msg_item_ctr) {
			EMConversation conversation = (EMConversation) v.getTag(v.getId());
			if (conversation != null) {
				Intent intent = new Intent(v.getContext(), ChatActivity.class);

				// it is single chat
				intent.putExtra("latitude", BaiduMapTool.latitude);
				intent.putExtra("longitude", BaiduMapTool.longitude);
				intent.putExtra("userId", conversation.getUserName());
				v.getContext().startActivity(intent);
			}
		}
	}

	private void agreeFriendApply(String group, String friendId,
			DataWrapper entry) {
//		HttpRequest request = new HttpRequest(inflater.getContext().getString(
//				R.string.base_urls)
//				+ "/surong/api/friend/pass", 0);
		HttpRequest request = new HttpRequest(Constans.addFriend
				, 0);
		request.setMethod(HttpRequest.METHOD_POST);
		request.setResponseDataFormat(HttpRequest.RESPONSE_JSON);
		request.setExpectedDataFormat(HttpRequest.EXPECTED_DATAWRAPPER);

		request.setParameter("token", LDApplication.getInstance()
				.getSessionData(EASEConstants.TOKEN));
		request.setParameter("friendId", friendId);
		request.setParameter("label_code", group);
		request.setHttpRequestListener(this);
		request.setExtraData(entry);
		request.startAsynchronousRequest();
	}

	@Override
	public void beforeHttpRequest(HttpRequest arg0) {
		CustomProgressDialog.startProgressDialog(inflater.getContext());
	}

	@Override
	public void onHttpRequestCancelled(HttpRequest arg0) {
		CustomProgressDialog.stopProgressDialog();
	}

	@Override
	public void onHttpResponse(HttpRequest arg0, HttpResult arg1) {
		CustomProgressDialog.stopProgressDialog();
		DataWrapper resp = arg1.getResponseData();
		if (resp != null) {
			int err = resp.getInt("code");
			if (err != 0) {
				String message = null;
				if (resp.getInt("code") != null) {
					message = resp.getString("msg");
				} else {
					message = inflater.getContext().getString(
							R.string.accept_friend_apply_fail);
				}
				showTip(message);
				if (err == 40008) {
					// 跳转到登陆页
					activity.finish();
					activity.startActivity(new Intent(activity,
							LoginActivity.class));

				}
			} else {
				DataWrapper entry = (DataWrapper) arg0.getExtraData();
				showTip(inflater
						.getContext()
						.getString(
								R.string.accept_friend_apply_succ,
								entry.getString(EASEDatabaseApply.ApplyApplicantNickname)));
				entry.setObject(EASEDatabaseApply.ApplyState,
						Integer.valueOf(EASEDatabaseApply.ApplyStateAgree));
				databaseApply.insertOrUpdateApplicant(entry);
				notifyDataSetChanged();
				NotificationCenter.getDefaultCenter().sendNotification(this,
						MainActivity.N_APPLY_ACCEPTED, entry);
			}
		}
	}

	private void showTip(String msg) {
		Toast.makeText(inflater.getContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onLongClick(View v) {
		Object tagObject = v.getTag(R.layout.message);
		if (!(tagObject instanceof Integer)) {
			return true;
		}
		final MenuDialog menuDialog = new MenuDialog(inflater.getContext());
		menuDialog.setCancelable(true);
		menuDialog.setTitle("删除消息");
		menuDialog.addItem(
				inflater.getContext().getString(R.string.label_delete), 0,
				tagObject);
		menuDialog.show();

		menuDialog
				.setOnItemClickListener(new MenuDialog.MenuDialogItemClickListener() {

					@Override
					public boolean onItemClick(int id, TextView item) {
						Object object = item.getTag();
						if (object instanceof Integer) {
							int pos = ((Integer) object).intValue();

							if (isApplyLocation(pos)) {
								databaseApply.delete(applyList.get(pos));
							} else {
									EMChatManager
											.getInstance()
											.deleteConversation(
													conversationList
															.get(getConversionLocation(pos))
															.getUserName());
								
							}

							removeItem(pos);
						}

						Toast.makeText(inflater.getContext(), "删除成功",
								Toast.LENGTH_SHORT).show();
						return true;
					}
				});
		return true;
	}
}
