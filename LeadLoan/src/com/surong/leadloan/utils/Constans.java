package com.surong.leadloan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/*
 * 接口常量
 */

public class Constans {

	public static final String HTTP_URL = "113.106.95.52:8082";// 发布外网地址
	// public static final String HTTP_URL = "192.168.1.27:8080";//肖菊
	// public static final String HTTP_URL = "192.168.1.33:8080";//邓江斌

	public static final int thod_Get_0 = 0;// get请求
	public static final int thod_Post_1 = 1;// post请求
	public static final String CRM_ACTION_NAME = "crm_filt";// 客户管理筛选器

	public final static String saveOrder = "http://" + Constans.HTTP_URL
			+ "/surong/api/loanassi/saveOrder";
	// 机构数据
	public final static String instituteUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/institu";
	// 省市
	public final static String cityUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/province";
	// 字典
	public final static String mapUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/common/dict";
	// 图片存放地址
	public final static String IAMGES_PATH = Environment
			.getExternalStorageDirectory().getPath()
			+ "/linkGroup/record/images";
	// 登入
	public static String loginUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/v2/login";
	// 注册验证码
	public static String codeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/captcha/send";
	// 注册
	public static String registerUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/v2/register";
	// 修改头像接口
	public static String headimgUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/headimg/update";
	// 修改个人资料--注册1
	public static String registerOne = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/reset";

	// 注册2
	public static String registerTwo = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/v2/registerExt";
	// 上传通讯录
	public static String addressBookUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/addressBook/macth";
	// 加好友 号码
	public static String addFriendUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/addByPhone";
	// 客户管理，查看订单
	public static String findOrderUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/order/findOrder";
	// 客户管理，查看最新订单
	public static String findNewOrderUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/customer/list";
	// 客户管理，查看个人历史订单
	public static String findOrderHistoryUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/customer/order/history";

	public static String timeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/account/getCashRecord";

	public static String versionUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/version/getVersion";
	public static String integrationUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/account/getPointRecord";

	public static String ashRecord = "http://" + Constans.HTTP_URL
			+ "/surong/api/account/getCashRecord";

	// 客户管理，查看订单详情
	public static String findOrderDetailsUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/order/findOrderDetails";

	// 信贷经理详细
	public static String userDetailUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/get";
	// 进入设置
	public static String setUpUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/setup";
	// 修改設置
	public static String changeSetUpUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/reviseSetup";
	// 申请认证
	public static String authUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/auth";
	// E店
	public static String mShopUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/mstore/v2/get";
	// 修改订单状态
	public static String editOrderUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/order/editOrderStatus";
	// 查看认证资料
	public static String getAuthInfoUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/getAuthInfo";

	// 获取验证码
	public static String forgetPasswordUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/password/send";

	// 重置密码
	public static String resetUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/password/reset";
	// 修改密码
	public static String changeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/password/change";

	// 添加工作经历
	public static String workExperienceAddUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/workExperience/v2/add";
	// 修改工作经历
	public static String workExperienceUpdateUrl = "http://"
			+ Constans.HTTP_URL + "/surong/api/workExperience/v2/update";
	// 删除工作经历
	public static String workExperienceDeleteUrl = "http://"
			+ Constans.HTTP_URL + "/surong/api/workExperience/v2/delete";

	// 添加教育经历
	public static String educationExperienceAddUrl = "http://"
			+ Constans.HTTP_URL + "/surong/api/educationExperience/v2/add";
	// 修改教育经历
	public static String educationExperienceUpdateUrl = "http://"
			+ Constans.HTTP_URL + "/surong/api/educationExperience/v2/update";
	// 删除教育经历
	public static String educationExperienceDeleteUrl = "http://"
			+ Constans.HTTP_URL + "/surong/api/educationExperience/v2/delete";
	// 修改基本信息
	public static String resetUserInfoUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/reset";

	// 查看推广状态
	public static String findPromStatusUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/promotion/findPromStatus";

	// 产品推广
	public static String productPromotionUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/promotion/promProd";
	// 取消产品推广
	public static String cancelProdPromUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/promotion/cancelProdProm";

	// 店铺推广
	public static String promStoreUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/promotion/promStore";
	// 取消店铺推广
	public static String cancelStorePromUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/promotion/cancelStoreProm";
	// 设置好友备注
	public static String friendRemarkUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/remark";
	// 移动好友
	public static String friendMoveUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/move";
	// 删除好友
	public static String friendDeleteUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/delete";
	// 超级经纪人
	public static String loanAssistantUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/loanassi/home";
	// 超级经纪人订单列表
	public static String loanAssistantListUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/loanassi/list";
	// 超级经纪人订单详情
	public static String loanAssistantDetailUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/loanassi/findDetail";
	// 客户管理 退点券或申报奖励
	public static String applyAuthUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/customer/order/applyAuth";
	//同意加好友
	public static String addFriend = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/pass";

	// 吐息
	public static void Toast(Context activity, String context) {
		Toast.makeText(activity, context, 0).show();
	}

	/*
	 * 检验手机号是否有效
	 */
	public static boolean isMobileNO(String mobile) {
		Pattern p = Pattern.compile("^(1[3-9])\\d{9}$");
		// .compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(14[0,5-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	public static boolean isEmailNO(String email) {
		Pattern p = Pattern
				.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		// .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isQQNO(String qq) {
		Pattern p = Pattern.compile("^\\d{5,12}$");
		// .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher m = p.matcher(qq);
		return m.matches();
	}

	public static boolean isweiXinNO(String weixin) {
		Pattern p = Pattern.compile("^[a-z_\\d]+$");
		Matcher m = p.matcher(weixin);
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

	/**
	 * @param 将文字中的标点改为全角
	 *            ,解决排版不齐的问题
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
