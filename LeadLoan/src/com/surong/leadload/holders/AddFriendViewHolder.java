package com.surong.leadload.holders;

import java.io.Serializable;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chatuidemo.res.EASEConstants;
import com.pj.core.BaseActivity;
import com.pj.core.NotificationCenter;
import com.pj.core.datamodel.DataWrapper;
import com.pj.core.dialog.CacheableDialog;
import com.pj.core.dialog.DialogListener;
import com.pj.core.dialog.HolderPopupWindow;
import com.pj.core.http.HttpImage;
import com.pj.core.http.HttpRequest;
import com.pj.core.http.HttpResult;
import com.pj.core.managers.LogManager;
import com.pj.core.utilities.DimensionUtility;
import com.pj.core.utilities.StringUtility;
import com.pj.core.viewholders.HttpViewHolder;
import com.surong.leadload.api.data.AddFriendEntry;
import com.surong.leadloan.R;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.start.LoginActivity;
import com.surong.leadloan.utils.Constans;

public class AddFriendViewHolder extends HttpViewHolder implements View.OnClickListener, DialogListener{
	
	private AddFriendEntry entry;
	
	private ImageView imageView;
	private TextView  name;
	private TextView  inst;
	private Button    group;
	private EditText message;
	private String[] groups;
	
	private boolean success = false;

	public AddFriendViewHolder(BaseActivity activity) {
		super(activity);
		setLayoutResource(R.layout.add_friend);
	}
	
	@Override
	protected void initialize(BaseActivity activity, View view) {
		super.initialize(activity, view);
		
		getNavigationBar().setTitle(R.string.add_friend_title);
//		View item = getNavigationBar().newNavigationBarItem(getString(R.string.add_action), this);
//		getNavigationBar().setNavigationRightView(item);
		Button back = getNavigationBar().getDefaultGobackButton();
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
				
			}
		});
		getNavigationBar().setNavigationLeftView(back);
		groups = new String[]{getString(R.string.group_friend),getString(R.string.group_workmate)};
	}

	@Override
	protected void onApplyView(View arg0) {
		assignClickListener(this, R.id.add_friend_send_apply,R.id.add_friend_choose_group);
		
		imageView = findViewById(R.id.add_friend_header);
		name = findViewById(R.id.add_friend_name);
		inst = findViewById(R.id.add_friend_inst);
		group = findViewById(R.id.add_friend_choose_group);
		message = findViewById(R.id.add_friend_msg);
		name.setText("");
		inst.setText("");
		
		
		Intent intent = getActivity().getIntent();
		Serializable object = intent.getSerializableExtra(EASEConstants.KEY_ADD_FRIEND_ENTRY);
		if (object!=null) {
			entry = (AddFriendEntry) object;
			name.setText(StringUtility.select(true, entry.member.getDisplayName(),entry.member.getRealName(),"--"));
			inst.setText(StringUtility.select(true, entry.member.getInstituName(),"--"));
			
			HttpImage.getInstance().setImage(entry.member.getHeadImgPath(), imageView, R.drawable.default_avatar, DimensionUtility.dp2px(48)*0.5f);
			
			setGroup(AddFriendEntry.CATEGORY_FRIEND);
		}
	}

	private void setGroup(int category) {
		if (entry!=null) {
			entry.category = category;
		}
		
		group.setText(groups[category]);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.add_friend_choose_group) {
			ChooseGroupViewHolder chooseGroupViewHolder = new ChooseGroupViewHolder(getActivity());
			HolderPopupWindow popupWindow = new HolderPopupWindow(chooseGroupViewHolder,group.getMeasuredWidth(), LayoutParams.WRAP_CONTENT);
			popupWindow.showAsDropDown(group);
			chooseGroupViewHolder.setListener(this);
			return;
		}
		
		if (entry == null) {
			return;
		}
		
		if (v.getId() == R.id.group_friend) {
			setGroup(AddFriendEntry.CATEGORY_FRIEND);
		}else if (v.getId() == R.id.group_workmate) {
			setGroup(AddFriendEntry.CATEGORY_WORKMATE);
		}else {
			// 发送申请
			sendApply();
		}
	}

	private void sendApply() {
		String msg = message.getText().toString().trim();
		entry.message = msg;
		
		sendApplyRequest(entry);
	}
	private void sendApplyRequest(AddFriendEntry addEntry){
		String url = null;
	    String key = null;
	    String param = null;
	    
	    if (addEntry.type == AddFriendEntry.TYPE_ID) {
	        url = ("/surong/api/friend/add");
	        key = "friendId";
	        param = addEntry.member.getId();
	    }else{
	        url = ("/surong/api/friend/addByPhone");
	        key = "phone";
	        param = addEntry.member.getMobile();
	    }
	    
	    HttpRequest request = makeRequest(0, url, addEntry, "token",LDApplication.getInstance().getSessionData(EASEConstants.TOKEN),key,param,"label_code",Integer.toString(addEntry.category),"message",addEntry.message);
	    request.setResponseDataFormat(HttpRequest.RESPONSE_JSON);
	    request.setExpectedDataFormat(HttpRequest.EXPECTED_DATAWRAPPER);
	    
	    asyncRequest(request);
	}
	
	@Override
	public void onHttpResponse(HttpRequest request, HttpResult result) {
		super.onHttpResponse(request, result);
//		Toast.makeText(getActivity(), result+"",
//				Toast.LENGTH_SHORT).show();
		if (request.getRequestCode() == 0) {
			DataWrapper resp = result.getResponseData();
			if (resp == null) {
				return;
			}
	        int error = resp.getInt("code");
	        if (error == 0) {
	            // 添加成功
	            success = true;
	            getActivity().showMessageDialog(0, getString(R.string.add_friend_title), getString(R.string.add_friend_success), getString(R.string.label_ok), this);
	        }else{
	        	 if (error == 40008) {
			        	//跳转到登陆页
		            	getActivity().finish();
		            	getActivity().startActivity(new Intent(getActivity(),
		    					LoginActivity.class));

			        }
	            success = false;
	            LogManager.i(getClass().getSimpleName(),"附近的人 添加好友失败 %s",resp.getString("msg"));
	            getActivity().showMessageDialog(1, getString(R.string.add_friend_title),resp.getString("msg") , getString(R.string.label_ok), this);
	           
	        
	        }
	    }
	}

	
	@Override
	public String getUrlByAction(String action) {
		return "http://"+Constans.HTTP_URL+action;
//		return getString(R.string.base_urls)+action;
	}

	@Override
	public void onDialogClose(int arg0, CacheableDialog arg1, int arg2,
			Object arg3) {
		if (arg0 == 0) {
			getActivity().finish();
		}
	}
	
	@Override
	public void onViewDidDisappear(boolean animated) {
		super.onViewDidDisappear(animated);
		entry.success = success;
		NotificationCenter.getDefaultCenter().sendNotification(this, EASEConstants.N_FRIEND_APPLY, entry);
	}
}
