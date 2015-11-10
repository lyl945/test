package com.surong.leadloan.utils2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class Constans {

//	public static final String HTTP_URL = "192.168.1.230:8080";
	 public static final String HTTP_URL = "113.106.95.52:8082";
	// public static final String HTTP_URL = "192.168.1.33:8080";

	// public static final String HTTP_URL = "192.168.1.27:8080";//肖菊

	public static final String PAGE = "4";

	public final static String setproject = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/editItem";

	public final static String republishProject = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/republishItem";

	public final static String deletefund = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/editItem";

	public final static String lookproject = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/itemOwnDetail";

	public final static String lookseekfund = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/consult";

	public final static String lookfund = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/fundOwnDetail";

	public final static String setfund = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/editFund";

	public final static String republishFund = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/republishFund";

	public final static String IAMGES_PATH = Environment
			.getExternalStorageDirectory().getPath()
			+ "/linkGroup/record/images";

	public static String loginUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/login";

	public static String codeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/captcha/send";

	public static String findcodeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/password/send";

	public static String set_pwd = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/register";

	public static String registerUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/register";

	public static String spinnerUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/province";

	public static String institutionUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/open/user/institu";

	public static String dictionaryUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/common/dict";

	public static String reviseUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/password/change";

	public static String headimgUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/headimg/update";

	public static String publishPro1 = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/publishItem";

	public static String setupUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/setup";

	public static String changeSetup = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/reviseSetup";

	public static String getconsumeinfo = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/pack/consumeInfo";

	public static String personalUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/get";

	public static String resetlUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/reset";

	public static String orderPersonalConsumptionUrl = "http://"
			+ Constans.HTTP_URL + "/surong/api/item/consumeLoad";

	public static String orderCreditUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/creditLoad";

	public static String orderPurchaseUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/fhgb";

	public static String orderCompanyLoadUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/companyLoad";

	public static String orderVehiclePledgeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/vehiclePledge";

	public static String orderProDetailUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/itemDetail";

	public static String MyOwnValidProjectUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/ownValidItem";

	public static String MyExpireProjectUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/ownInvalidItem";

	public static String orderCollectUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/collect";

	public static String orderPraiseUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/praise";

	public static String orderNegativeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/negative";

	public static String getMore = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/seniorSearch";

	public static String publishApt = "http://" + Constans.HTTP_URL
			+ "/surong/api/common/companyApt";

	public static String projectDelets = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/delete";

	public static String fundDelets = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/delete";

	public static String foundfundstick = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/rank";

	public static String foundprojectstick = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/rank";

	public static String fundstick = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/fundStick";

	public static String projectstick = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/itemStick";

	public static String fundDetail = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/fundOwnDetail";

	public static String funPersonalConsumptionUrl = "http://"
			+ Constans.HTTP_URL + "/surong/api/fund/consumeLoad";

	public static String funCreditLoad = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/creditLoad";

	public static String expireFund = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/ownInvalidFund";

	public static String funPurchaseUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/fhgb";

	public static String funCompanyLoad = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/companyLoad";

	public static String funVehiclePledgeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/vehiclePledge";

	public static String FundDetailUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/fundDetail";

	public static String pledge = "http://" + Constans.HTTP_URL
			+ "/surong/api/common/pledge";

	public static String funCollectUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/collect";

	public static String funPraiseUrl = "http://" + Constans.HTTP_URL
			+ "surong/api/fund/praise";

	public static String funNegativeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/negative";

	public static String funMoveUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/seniorSearch";

	public static String MyOwnValidFundUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/ownValidFund";

	public static String lookSeek = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/consult";

	public static String toTop = "http://" + Constans.HTTP_URL
			+ "/surong/api/item/itemStick";

	public static String fun = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/publishFund";

	public static String chatUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/im/text/send";

	public static String matchAdd = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/profiles/macth";

	public static String addByPhone = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/addByPhone";

	public static String addById = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/add";

	public static String fundPraiseUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/praise";

	public static String fundNegativeUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/fund/negative";

	public static String baiduSetup = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/baidu/info/setup";

	public static String deleteFriend = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/delete";

	public static String nearPeer = "http://" + Constans.HTTP_URL
			+ "/surong/api/contacts/accessory";

	public static String sameIndustry = "http://" + Constans.HTTP_URL
			+ "/surong/api/contacts/sameIndustry";

	public static final String acceptFriend = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/pass";

	public static final String friendList = "http://" + Constans.HTTP_URL
			+ "/surong/api/friend/v2/list";

	public static final String possibleKnow = "http://" + Constans.HTTP_URL
			+ "/surong/api/contacts/possible";

	public static String addressBookUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/user/addressBook/macth";

	public static String friendRecommendUrl = "http://" + Constans.HTTP_URL
			+ "/surong/api/contacts/sameIndustry";

	public static final int HUNDRED = 0x64;
	public static final int SUCCESS = 0x0;

	public static void Toast(Context activity, String context) {
		Toast.makeText(activity, context, Toast.LENGTH_SHORT).show();
	}

	public static boolean isMobileNO(String mobile) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	public static boolean isPwdNo(String pwd) {
		Pattern p = Pattern.compile("^(?![a-z]+$)(?!\\d+$)[a-z0-9]{8,16}$");
		Matcher m = p.matcher(pwd);
		return m.matches();
	}

}
