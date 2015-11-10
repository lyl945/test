package com.surong.leadloan.activity.personal;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easemob.chatuidemo.res.EASEConstants;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.pj.core.datamodel.DataWrapper;
import com.surong.leadload.api.data.AddFriendEntry;
import com.surong.leadload.api.data.Member;
import com.surong.leadload.database.EASEDatabaseUserInfo;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.entity.Product;
import com.surong.leadloan.fragment.AboutUsFragment;
import com.surong.leadloan.fragment.DynamicFragment;
import com.surong.leadloan.fragment.ProductFragment;
import com.surong.leadloan.fragment.PublishFragment;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.imageutils.ImageFetcher;
import com.surong.leadloan.ui.CircleImageView;
import com.surong.leadloan.utils.Analyze;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class MShopActivity extends FragmentActivity implements
        OnPageChangeListener, OnClickListener {

    private View view;
    private ViewPager vPager_Sc;
    private TextView centerTitle;
    private TextView txtUs, txtPro, txtProgrem, txtDynamic;
    private FragmentManager fragmentManager;
    private AboutUsFragment aboutUsFragment;
    private ProductFragment productFragment;
    private PublishFragment pulishFragment;
    private DynamicFragment dynamicFragment;
    private RelativeLayout relative_back;
    private MyHttpUtils myHttpUtils;
    private Context context;
    private String token;
    public String instituName, logoPath, displayName, personDuty, instituDesc,
            headImage, cityName;
    private TextView name, institute, perDuty, insDesc, id_certif,
            institute_certify;
    public List<Product> list;
    private LinearLayout contentLayout;
    private LinearLayout addFriendLayout;

    private FragmentTransaction fragmentTransaction;
    private TextView[] textViews;
    public JSONObject object;
    private String authStatus;
    private ScrollView mScrollView;

    // private DbUtils dbUtils;
    private Personal personal;
    private CircleImageView person_icon;
    private ImageFetcher mImageFetcher;
    private Bundle bundle;
    public static String memberId, messageAddID, mobile;
    private TextView city;
    UMSocialService mController;
    private TextView share;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_shop);
        context = this;
        options = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.default_avator)
                .showImageForEmptyUri(R.drawable.default_avatar)
                .showImageOnFail(R.drawable.default_avatar).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer()).build();
        bundle = getIntent().getExtras();
        memberId = getIntent().getStringExtra("memberId");
        mobile = getIntent().getStringExtra("mobile");
        displayName = getIntent().getStringExtra("displayName");
        messageAddID = getIntent().getStringExtra("messageAddId");
        findView();
        initAciton();
        initData();
        initShare();
    }

    private void findView() {
        share = (TextView) findViewById(R.id.right_share);
        share.setOnClickListener(this);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mScrollView.isSmoothScrollingEnabled();
        vPager_Sc = (ViewPager) findViewById(R.id.vPager_Sc);
        centerTitle = (TextView) findViewById(R.id.center_title);
        textViews = new TextView[4];
        textViews[0] = (TextView) findViewById(R.id.txt_1);
        textViews[1] = (TextView) findViewById(R.id.txt_2);
        textViews[2] = (TextView) findViewById(R.id.txt_3);
        textViews[3] = (TextView) findViewById(R.id.txt_4);
        relative_back = (RelativeLayout) findViewById(R.id.relative_back);
        name = (TextView) findViewById(R.id.name);
        institute = (TextView) findViewById(R.id.institute);
        perDuty = (TextView) findViewById(R.id.duty);
        insDesc = (TextView) findViewById(R.id.instituDesc);
        contentLayout = (LinearLayout) findViewById(R.id.content);
        addFriendLayout = (LinearLayout) findViewById(R.id.add_friend);
        addFriendLayout.setOnClickListener(this);
        EASEDatabaseUserInfo databaseUserInfo = new EASEDatabaseUserInfo(this);
        DataWrapper info = null;
        if (bundle != null && bundle.getString("userId") != null) {
            addFriendLayout.setVisibility(View.GONE);
        }
        if (memberId != null) {
            info = databaseUserInfo.getUserInfoByUserID(memberId);
            addFriendLayout.setVisibility(View.VISIBLE);
        }
        if (messageAddID != null) {
            info = databaseUserInfo.getUserInfoByUserID(messageAddID);
            if (info != null
                    && "Y".equals(info
                    .getString(databaseUserInfo.UserIsMyFriend))) {
                addFriendLayout.setVisibility(View.GONE);
            } else {
                addFriendLayout.setVisibility(View.VISIBLE);
            }
        }
        if (bundle == null && memberId == null && messageAddID == null) {
            addFriendLayout.setVisibility(View.GONE);
        }
        city = (TextView) findViewById(R.id.area);
        id_certif = (TextView) findViewById(R.id.id_certify);
        institute_certify = (TextView) findViewById(R.id.institute_certify);
        person_icon = (CircleImageView) findViewById(R.id.person_icon);

    }

    private void initAciton() {
        vPager_Sc.setOnPageChangeListener(this);
        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setOnClickListener(this);
        }
        relative_back.setOnClickListener(this);

    }

    private void initData() {
        list = new ArrayList<Product>();
        mImageFetcher = ImageFetcher.Instance(context, 0);
        try {
            personal = CommonActivity.db.findFirst(Personal.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        myHttpUtils = MyHttpUtils.myInstance();
        token = SharedPreferencesHelp.getString(context, "token");
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("token", token);
        if (bundle != null && bundle.getString("userId") != null) {
            params.addQueryStringParameter("memberId",
                    bundle.getString("userId"));
        }
        if (memberId != null) {
            params.addQueryStringParameter("memberId", memberId);
        }
        if (messageAddID != null) {
            params.addQueryStringParameter("memberId", messageAddID);
        }
        CustomProgressDialog.startProgressDialog(context);
        myHttpUtils.getHttpJsonString(params, Constans.mShopUrl, handler,
                context, 0, Constans.thod_Get_0);
    }

    private void initShare() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        // 设置分享内容
        mController
                .setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(this, "http://www.baidu.com"));
        // 设置分享图片，参数2为本地图片的资源引用
        // mController.setShareMedia(new UMImage(getActivity(),
        // R.drawable.icon));
        // 设置分享图片，参数2为本地图片的路径(绝对路径)
        // mController.setShareMedia(new UMImage(getActivity(),
        // BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

        // 设置分享音乐
        // UMusic uMusic = new
        // UMusic("http://sns.whalecloud.com/test_music.mp3");
        // uMusic.setAuthor("GuGu");
        // uMusic.setTitle("天籁之音");
        // 设置音乐缩略图
        // uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
        // mController.setShareMedia(uMusic);

        // 设置分享视频
        // UMVideo umVideo = new UMVideo(
        // "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
        // 设置视频缩略图
        // umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
        // umVideo.setTitle("友盟社会化分享!");
        // mController.setShareMedia(umVideo);

        String appId = "wx81e04987a13cad97";
        String appSecret = "1c3616af7a15fe43c0c05eeda8b426c3";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(MShopActivity.this, appId,
                appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(MShopActivity.this,
                appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MShopActivity.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
                MShopActivity.this, "100424468",
                "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

    }

    // /*
    // * 打开新浪和腾讯微薄的SSO授权
    // */
    // private void openSSO() {
    // mController.getConfig().setSsoHandler(new SinaSsoHandler());
    // mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
    // mController.getConfig().setSsoHandler(
    // new RenrenSsoHandler(MShopActivity.this, "201874",
    // "28401c0964f04a72a14c812d6132fcef",
    // "3bf66e42db1e4fa9829b955cc300b737"));
    // }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    // Constans.Toast(context, "获取E店信息成功");
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    CustomProgressDialog.stopProgressDialog();

                    try {
                        object = jsonObject.getJSONObject("mStore");
                        JSONObject instituInfo = object
                                .getJSONObject("instituInfo");
                        instituDesc = Utils.getStringWithKey("instituDesc",
                                instituInfo);
                        instituName = Utils.getStringWithKey("instituName",
                                instituInfo);
                        // instituName = personal.getInstituName();
                        logoPath = Utils.getStringWithKey("logoPath", instituInfo);

                        JSONObject memberInfo = object.getJSONObject("memberInfo");
                        if (!TextUtils.isEmpty(Utils.getStringWithKey(
                                "displayName", memberInfo))) {
                            displayName = Utils.getStringWithKey("displayName",
                                    memberInfo);
                        }
                        cityName = Utils.getStringWithKey("cityName", memberInfo);
                        // displayName = personal.getDisplayName();
                        personDuty = Utils.getStringWithKey("stdFlag", memberInfo);
                        authStatus = Utils.getStringWithKey("authStatus",
                                memberInfo);
                        String productList = Utils.getStringWithKey("productList",
                                object);
                        headImage = Utils.getStringWithKey("headImgPath",
                                memberInfo);
                        if (null != productList) {
                            list = Analyze.analyzePersonalProduct(productList);
                        }
                        setData();
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

    private void setData() {

        if (notNull(displayName)) {
            name.setText(displayName);

            if (bundle != null && bundle.getString("userId") != null
                    || messageAddID != null || memberId != null) {
                if (TextUtils.isEmpty(bundle.getString("remarkname"))) {
                    centerTitle.setText(displayName + "的E店");
                } else {
                    centerTitle.setText(bundle.getString("remarkname") + "的E店");
                }

            }
        } else {
            name.setText("");
        }
        if (notNull(cityName)) {
            city.setText(cityName);
        } else {
            // city.setText(" ");
        }
        if (notNull(instituName)) {
            institute.setText(instituName);
        } else {
            institute.setText("无机构信息");
        }

        if (notNull(authStatus)) {
            id_certif.setText(authStatus);
            institute_certify.setText(authStatus);
        } else {
            id_certif.setText("未申请身份认证");
            institute_certify.setText("未申请机构认证");
        }
        if (notNull(personDuty)) {
            perDuty.setText(personDuty);
        } else {
            perDuty.setText(" ");
        }

        if (notNull(instituDesc)) {
            insDesc.setText(instituDesc);
        } else {
            insDesc.setText("无机构介绍信息");
        }

        if (null != headImage) {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiskCache();
            ImageLoader.getInstance().displayImage(headImage,
                    person_icon, options, null);
            sendBroadcast(new Intent("shua"));
        }
        aboutUsFragment = new AboutUsFragment();
        aboutUsFragment.setmActivity(this);
        productFragment = new ProductFragment();
        productFragment.setmActivity(this);
        pulishFragment = new PublishFragment();
        dynamicFragment = new DynamicFragment();

        List<Fragment> listFragments = new ArrayList<Fragment>();
        listFragments.add(aboutUsFragment);
        listFragments.add(productFragment);
        listFragments.add(pulishFragment);
        listFragments.add(dynamicFragment);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        changColor(0);
        fragmentTransaction.replace(R.id.content, aboutUsFragment);
        fragmentTransaction.commit();
        CustomProgressDialog.stopProgressDialog();
        // 动态设置viewpager的高度
        /*
         * final int w = View.MeasureSpec.makeMeasureSpec(0,
		 * View.MeasureSpec.UNSPECIFIED); final int h =
		 * View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		 * vPager_Sc.measure(w, h); ViewTreeObserver vto =
		 * vPager_Sc.getViewTreeObserver(); vto.addOnGlobalLayoutListener(new
		 * OnGlobalLayoutListener() {
		 * 
		 * public void onGlobalLayout() {
		 * 
		 * vPager_Sc.getViewTreeObserver() .removeGlobalOnLayoutListener(this);
		 * View view = vPager_Sc.getChildAt(vPager_Sc.getCurrentItem());
		 * view.measure(w, h); LayoutParams params = new
		 * LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		 * params.height = view.getMeasuredHeight();
		 * vPager_Sc.setLayoutParams(params);
		 * 
		 * } });
		 * 
		 * fragmentManager = getSupportFragmentManager(); myOrderPageAdapter =
		 * new MyOrderPageAdapter(fragmentManager, vPager_Sc, listFragments); //
		 * 里面额参数几时你要缓存页面的个数 vPager_Sc.setOffscreenPageLimit(1);
		 * vPager_Sc.setAdapter(myOrderPageAdapter);
		 * 
		 * vPager_Sc.setCurrentItem(1);
		 */

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (v.getId()) {

                case R.id.txt_1:
                    changColor(0);
                    // vPager_Sc.setCurrentItem(0);
                    fragmentTransaction.replace(R.id.content, aboutUsFragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.txt_2:
                    changColor(1);
                    // vPager_Sc.setCurrentItem(1);
                    fragmentTransaction.replace(R.id.content, productFragment);
                    fragmentTransaction.commit();
                    break;
                case R.id.txt_3:
                    changColor(2);
                    // vPager_Sc.setCurrentItem(2);
                /*
                 * fragmentTransaction.replace(R.id.content, pulishFragment);
				 * fragmentTransaction.commit();
				 */
                    break;
                case R.id.txt_4:
                    changColor(3);
                    // vPager_Sc.setCurrentItem(3);
				/*
				 * fragmentTransaction.replace(R.id.content, dynamicFragment);
				 * fragmentTransaction.commit();
				 */
                    break;
                case R.id.relative_back:
                    finish();
                    break;
                case R.id.right_share:
                    mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
                            SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ,
                            SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA,
                            SHARE_MEDIA.TENCENT);
                    mController.openShare(MShopActivity.this, false);
                    break;

                case R.id.add_friend:
                    AddFriendEntry addFriendEntry = new AddFriendEntry();
                    Member member = new Member();
                    member.setDisplayName(displayName);
                    member.setInstituName(instituName);
                    member.setAuthStatus(authStatus);
                    member.setHeadImgPath(headImage);
                    member.setMobile(mobile);
                    member.setId(memberId);
                    addFriendEntry.member = member;
                    addFriendEntry.type = AddFriendEntry.TYPE_ID;
                    addFriendEntry.extraObject = member;
                    Intent intent = new Intent(EASEConstants.ACTION_ADD_FRIEND);
                    intent.putExtra(EASEConstants.KEY_ADD_FRIEND_ENTRY,
                            addFriendEntry);
                    // RequestParams params = new RequestParams();
                    // params.addQueryStringParameter("token", token);
                    // params.addQueryStringParameter("phone", phone);
                    // myHttpUtils.getHttpJsonString(params, Constans.addFriendUrl,
                    // handler,context, 0, Constans.thod_Get_0);

                    startActivity(intent);

                    // Intent i1 = new Intent(MShopActivity.this,
                    // AddFriendActivity.class);
                    // startActivity(i1);
                    break;
                default:
                    break;
            }
        }
    }

    private void changColor(int i) {
        for (int k = 0; k < textViews.length; k++) {
            if (i == 1) {
                textViews[1].setTextColor(getResources()
                        .getColor(R.color.white));
                textViews[1].setBackgroundResource(R.drawable.red_right_shaper);
                textViews[0].setTextColor(getResources().getColor(
                        R.color.mshop_text_black));
                textViews[0].setBackgroundResource(R.drawable.shape_left);
            } else {
                if (k == i) {
                    textViews[k].setTextColor(getResources().getColor(
                            R.color.white));
                    textViews[k]
                            .setBackgroundResource(R.drawable.red_left_shaper);

                } else {
                    textViews[k].setTextColor(getResources().getColor(
                            R.color.mshop_text_black));
                    textViews[k].setBackgroundResource(R.drawable.shape_right);
                }
            }

        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        switch (arg0) {
            case 0:
                txtUs.performClick();
                break;
            case 1:
                txtPro.performClick();
                break;
            case 2:
                txtProgrem.performClick();
                break;
            case 3:
                txtDynamic.performClick();
                break;

            default:
                break;
        }

    }

    private boolean notNull(String displayName2) {
        if (null == displayName2 || "".equals(displayName2)) {
            return false;
        }
        return true;
    }
}
