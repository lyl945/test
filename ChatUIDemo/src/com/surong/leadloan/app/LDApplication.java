package com.surong.leadloan.app;

import java.util.Map;

import android.graphics.Color;

import com.easemob.EMCallBack;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.domain.User;
import com.pj.core.BaseApplication;
import com.pj.core.viewholders.NavigationViewHolder;
import com.pj.core.viewholders.ViewHolder;

public class LDApplication extends BaseApplication {
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";
    public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

    @Override
    public void onCreate() {
        super.onCreate();
        NavigationViewHolder.NavigationViewParams.NavigationBarBackgroundColor = Color
                .parseColor("#e2343f");
        NavigationViewHolder.NavigationViewParams.NavigationContentBackgroundColor = Color.WHITE;

        ViewHolder.NavigationBarParams.TitleTextColor = Color.WHITE;
        ViewHolder.NavigationBarParams.TitleTextSize = 20;
        ViewHolder.NavigationBarParams.GobackButtonPlainTextColor = Color.WHITE;
        ViewHolder.NavigationBarParams.ItemButtonPlainTextColor = Color.WHITE;
        ViewHolder.NavigationBarParams.GobackButtonTextSize = 16;
        ViewHolder.NavigationBarParams.ItemButtonTextSize = 16;

        /**
         * this function will initialize the HuanXin SDK
         * 
         * @return boolean true if caller can continue to call HuanXin related
         *         APIs after calling onInit, otherwise false.
         * 
         *         环信初始化SDK帮助函数
         *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         * 
         *         for example: 例子：
         * 
         *         public class DemoHXSDKHelper extends HXSDKHelper
         * 
         *         HXHelper = new DemoHXSDKHelper();
         *         if(HXHelper.onInit(context)){ // do HuanXin related work }
         */
        hxSDKHelper.onInit(this);
    }

    /**
     * 获取内存中好友user list
     * 
     * @return
     */
    public Map<String, User> getContactList() {
        return hxSDKHelper.getContactList();
    }
    

    /**
     * 设置好友user list到内存中
     * 
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        hxSDKHelper.setContactList(contactList);
    }

    /**
     * 获取当前登陆用户名
     * 
     * @return
     */
    public String getUserName() {
        return hxSDKHelper.getHXId();
    }

    /**
     * 获取密码
     * 
     * @return
     */
    public String getPassword() {
        return hxSDKHelper.getPassword();
    }

    /**
     * 设置用户名
     * 
     * @param user
     */
    public void setUserName(String username) {
        hxSDKHelper.setHXId(username);
    }

    /**
     * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
     * 内部的自动登录需要的密码，已经加密存储了
     * 
     * @param pwd
     */
    public void setPassword(String pwd) {
        hxSDKHelper.setPassword(pwd);
    }

    /**
     * 退出登录,清空数据
     */
    public void logout(final EMCallBack emCallBack) {
        // 先调用sdk logout，在清理app中自己的数据
        hxSDKHelper.logout(emCallBack);
    }
}
