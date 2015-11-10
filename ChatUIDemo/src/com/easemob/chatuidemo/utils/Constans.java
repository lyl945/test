package com.easemob.chatuidemo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/*
 * 接口常量
 */

public class Constans {
    public static final String HTTP_URL = "113.106.95.52:8082";
    // public static final String HTTP_URL = "192.168.1.230:8080";// 发布外网地址
    // public static final String HTTP_URL = "192.168.1.27:8080";//肖菊
    // public static final String HTTP_URL = "192.168.1.33:8080";//邓江斌

    public static final int thod_Get_0 = 0;// get请求
    public static final int thod_Post_1 = 1;// post请求
    public static final String CRM_ACTION_NAME = "crm_filt";// 客户管理筛选器

    // 设置好友备注
    public static String friendRemarkUrl = "http://" + Constans.HTTP_URL
            + "/surong/api/friend/remark";
    // 移动好友
    public static String friendMoveUrl = "http://" + Constans.HTTP_URL
            + "/surong/api/friend/move";
    // 删除好友
    public static String friendDeleteUrl = "http://" + Constans.HTTP_URL
            + "/surong/api/friend/delete";

    // 吐息
    public static void Toast(Context activity, String context) {
        Toast.makeText(activity, context, 0).show();
    }

    /*
     * 检验手机号是否有效
     */
    public static boolean isMobileNO(String mobile) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(14[0,5-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /*
     * 验证密码格式
     */
    public static boolean isPwdNo(String pwd) {
        Pattern p = Pattern.compile("^(?![a-z]+$)(?!\\d+$)[a-z0-9]{8,16}$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    public static int distanceBetween(double lat1, double lon1, double lat2,
            double lon2) {
        double R = 6370996.81;
        double PI = Math.PI;
        return (int) (R * Math.acos(Math.cos(lat1 * PI / 180)
                * Math.cos(lat2 * PI / 180)
                * Math.cos(lon1 * PI / 180 - lon2 * PI / 180)
                + Math.sin(lat1 * PI / 180) * Math.sin(lat2 * PI / 180)));
    }
}
