package com.surong.leadloan.start;

/*
 * @author胡田
 * 登录页面
 */

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.applib.model.EaseUser;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.res.EASEConstants;
import com.lidroid.xutils.http.RequestParams;
import com.pj.core.datamodel.DataWrapper;
import com.pj.core.utilities.ConvertUtility;
import com.surong.leadload.database.EASEDatabaseApply;
import com.surong.leadloan.R;
import com.surong.leadloan.activity.CommonActivity;
import com.surong.leadloan.activity.contact_group.PeerActivity;
import com.surong.leadloan.app.LDApplication;
import com.surong.leadloan.entity.Personal;
import com.surong.leadloan.httpservice.MyHttpUtils;
import com.surong.leadloan.utils.Constans;
import com.surong.leadloan.utils.CustomProgressDialog;
import com.surong.leadloan.utils.MyApplication;
import com.surong.leadloan.utils.SharedPreferencesHelp;
import com.surong.leadloan.utils.Utils;

public class LoginActivity extends CommonActivity implements OnClickListener {
    private EditText ediText_phone;
    private EditText ediText_password;
    private Button btn_login, btn_regist;
    private TextView text_forgetpwd;
    private TextView service_phone;
    private ImageView imageView1, imageView2;
    private View view;
    private Context context;
    private int flag_login = 0;
    private String mobile, password;

    // private DbUtils db;
    private Personal per;

    private boolean autoLogin = false;
    private static final int GO_TO_MAIN = 100;
    private static final int GO_TO_EXPLAIN = 101;
    private static final String DB_NAME = "stu_db.db";
    private static final String TABLE_NAME = "stu_table";
    private static String dbFilePath = "";
    private SQLiteDatabase database;
    private int num1;
    private int num2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.login, null);
        // 初始化commonActivity数据
        context = this;
        addContentView(view);
        changeTitle("登录");
        findView();
        // initData();
        initAction();
        // 获取上一次登陆的用户名和密码
        getUsernameAndPassword();
    }

    private void createOrOpenDatabase() {

        String dataPath = getApplicationContext().getFilesDir().getAbsolutePath();
        File dir = new File(dataPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dbFilePath = dataPath + "/" + DB_NAME;
        database = SQLiteDatabase.openOrCreateDatabase(dbFilePath, null);
    }

    private void createTable() {
        try {
            String stu_table = "create table " + TABLE_NAME + "(_id integer primary key autoincrement, phonenum varchar(60))";
            database.execSQL(stu_table);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

	/*
     * if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) { //透明状态栏
	 * getWindow().addFlags
	 * (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //透明导航栏 getWindow
	 * ().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); }
	 */

    public void getUsernameAndPassword() {
        ArrayList<String> phoneStrings = new ArrayList<String>();
        SharedPreferences sharedPre = getSharedPreferences("config",
                MODE_PRIVATE);
        boolean IsPhone = false;
        if (sharedPre != null) {
            mobile = sharedPre.getString("username", "");
            password = sharedPre.getString("password", "");
            createOrOpenDatabase();
            createTable();
            find(mobile);
            Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
            if (null == cursor || cursor.getCount() < 1) {
                return;
            }
            // 判断游标是否为空
            if (cursor.moveToFirst()) {
                // 遍历游标
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    int id = cursor.getInt(0);
                    String phonenum = cursor.getString(1);
                    phoneStrings.add(phonenum);
                }
            }
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
                    R.layout.spinner_header, phoneStrings);
            // 设置Adapter
            ediText_phone.setText(mobile);
            ediText_password.setText(password);
        }
    }

    private void find(String mobile) {
        Cursor cursor = database.query(TABLE_NAME, null, "phonenum like ?", new String[]{mobile}, null, null, null);
        if (null == cursor || cursor.getCount() < 1) {
            ContentValues values = new ContentValues();
            values.put("phonenum", mobile);
            database.insert(TABLE_NAME, null, values);
        } else {
            String whereClause3 = "phonenum = ?";
            String[] whereArgs3 = {String.valueOf(mobile)};
            database.delete(TABLE_NAME, whereClause3, whereArgs3);
            //先删除原先数据再重新添加
            ContentValues values = new ContentValues();
            values.put("phonenum", mobile);
            database.insert(TABLE_NAME, null, values);
        }
    }

    public static void saveLoginInfo(Context context, String username,
                                     String password) {
        // 获取SharedPreferences对象
        SharedPreferences sharedPre = context.getSharedPreferences("config",
                context.MODE_PRIVATE);
        // 获取Editor对象
        Editor editor = sharedPre.edit();
        // 设置参数
        editor.putString("username", username);
        editor.putString("password", password);

        // 提交
        editor.commit();
    }

    @Override
    protected void onStop() {

        CustomProgressDialog.stopProgressDialog();
        super.onStop();
    }

    private void initAction() {
        btn_login.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
        text_forgetpwd.setOnClickListener(this);
        service_phone.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        ediText_phone.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(ediText_phone.getText().toString())) {
                    imageView1.setVisibility(View.VISIBLE);
                } else {
                    imageView1.setVisibility(View.GONE);
                }
                imageView2.setVisibility(View.GONE);
            }
        });

        ediText_password.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(ediText_password.getText().toString())) {
                    imageView2.setVisibility(View.VISIBLE);
                } else {
                    imageView2.setVisibility(View.GONE);
                }
                imageView1.setVisibility(View.GONE);
            }
        });

        ediText_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                num2++;
                if (!TextUtils.isEmpty(mobile)) {
                    if (!arg0.toString().trim().equals(mobile) && num2 == 2) {
                        ediText_password.setText("");
                    }
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(ediText_phone.getText().toString())) {
                    imageView1.setVisibility(View.GONE);
                } else {
                    imageView1.setVisibility(View.VISIBLE);
                }

            }
        });
        ediText_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                num1++;
                if (!TextUtils.isEmpty(arg0)) {
                    if (arg0.toString().contains(password) && num1 == 2) {
                        String lylString = arg0.toString().substring(password.length(), arg0.toString().length());
                        ediText_password.setText(lylString);
                        ediText_password.setSelection(1);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub


            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(ediText_password.getText().toString())) {
                    imageView2.setVisibility(View.GONE);
                } else {
                    imageView2.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void findView() {

        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        imageView2 = (ImageView) view.findViewById(R.id.imageView2);
        ediText_phone = (EditText) view.findViewById(R.id.ediText_phone);
        ediText_password = (EditText) view.findViewById(R.id.ediText_password);
        btn_login = (Button) view.findViewById(R.id.btn_login);
        btn_regist = (Button) view.findViewById(R.id.btn_regist);
        text_forgetpwd = (TextView) view.findViewById(R.id.text_forgetpwd);
        text_forgetpwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 设置下划线
        service_phone = (TextView) view.findViewById(R.id.service_phone);
        findViewById(R.id.relative_back).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:// 登录
                mobile = ediText_phone.getText().toString().trim();
                password = ediText_password.getText().toString().trim();
                saveLoginInfo(context, mobile, password);
                ;
                if (chcekNull(mobile, password)) {
                    RequestParams params = new RequestParams();
                    params.addQueryStringParameter("mobile", mobile);
                    params.addQueryStringParameter("password", password);
                    String path = Constans.loginUrl;
                    CustomProgressDialog.startProgressDialog(context);
                    MyHttpUtils myHttpUtils = MyHttpUtils.myInstance();
                    myHttpUtils.getHttpJsonString(params, path, handler, context,
                            flag_login, Constans.thod_Get_0);

                }

                break;
            case R.id.btn_regist:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.imageView1:
                ediText_phone.setText("");
                ediText_password.setText("");
                break;
            case R.id.imageView2:
                ediText_password.setText("");
                break;

            case R.id.text_forgetpwd:
                startActivity(new Intent(LoginActivity.this,
                        ForgetPasswordActivity.class));
                break;
            case R.id.service_phone:// 拨打客服电话
                Uri uri = Uri.parse("tel:" + service_phone.getText());
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private boolean progressShow;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    JSONObject object = (JSONObject) msg.obj;
                    String psd = "",
                            account = "";
                    try {
                        String token = object.getString("token");

                        LDApplication.getInstance().addSessionData(
                                EASEConstants.TOKEN, token);
                        SharedPreferencesHelp.SavaString(context, "token", token);
                        Personal personal = Utils.JsonGetPersonal(object);
                        Utils.saveOrUpdatePersonal(personal, CommonActivity.db);
                        Utils.saveOrUpdateEaseUser(object.getJSONObject("member"),
                                context);

                        //
                        EaseUser user = (EaseUser) LDApplication.getInstance()
                                .getSessionData(EASEConstants.CURRENT_USER);
                        if (user != null) {
                            try {
                                int uploaded = ConvertUtility.parseInt(object
                                        .getString("hasPhoneBook"));
                                user.setHasUploadContacts(uploaded == 1);
                            } catch (Exception e) {
                            }
                        }

                        psd = personal.getHuanxinPassword();
                        account = personal.getHuanxinAccount();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // //环信开始
                    // // 如果用户名密码都有，直接进入主页面
                    // if (DemoHXSDKHelper.getInstance().isLogined()) {
                    // autoLogin = true;
                    // startActivity(new Intent(LoginActivity.this,
                    // MainActivity.class));
                    //
                    // return;
                    // }

                    CustomProgressDialog.stopProgressDialog();
                    if (!progressShow) {
                        progressShow = true;
                        final ProgressDialog pd = new ProgressDialog(
                                LoginActivity.this);

                        pd.setCanceledOnTouchOutside(false);
                        pd.setOnCancelListener(new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                progressShow = false;
                            }
                        });
                        pd.setMessage(getString(R.string.Is_landing));
                        if (!pd.isShowing())
                            pd.show();
                        final String currentUsername = account;
                        final String currentPassword = psd;
                        final long start = System.currentTimeMillis();
                        // 调用sdk登陆方法登陆聊天服务器
                        EMChatManager.getInstance().login(currentUsername,
                                currentPassword, new EMCallBack() {

                                    @Override
                                    public void onSuccess() {
                                        // umeng自定义事件，开发者可以把这个删掉
                                        // loginSuccess2Umeng(start);
                                        //
                                        // 登陆成功，保存用户名密码
                                        ((LDApplication) LDApplication
                                                .getInstance())
                                                .setUserName(currentUsername);
                                        ((LDApplication) LDApplication
                                                .getInstance())
                                                .setPassword(currentPassword);
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                pd.setMessage(getString(R.string.list_is_for));
                                            }
                                        });
                                        try {
                                            // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                                            // ** manually load all local groups and
                                            // conversations in case we are auto
                                            // login
                                            // EMGroupManager.getInstance().loadAllGroups();
                                            // EMChatManager.getInstance().loadAllConversations();
                                            // 处理好友和群组 暂时屏蔽
                                            // processContactsAndGroups();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            // 取好友或者群聊失败，不让进入主页面
                                            runOnUiThread(new Runnable() {

                                                public void run() {
                                                    ((LDApplication) LDApplication
                                                            .getInstance())
                                                            .logout(null);
                                                    Toast.makeText(
                                                            getApplicationContext(),
                                                            R.string.login_failure_failed,
                                                            1).show();
                                                }
                                            });
                                            return;
                                        } finally {
                                            progressShow = false;
                                            pd.dismiss();
                                        }
                                        // 更新当前用户的nickname
                                        // 此方法的作用是在ios离线推送时能够显示用户nick
                                        // boolean updatenick =
                                        // EMChatManager.getInstance().updateCurrentUserNick(DemoApplication.currentUserNick.trim());
                                        // if (!updatenick) {
                                        // Log.e("LoginActivity",
                                        // "update current user nick fail");
                                        // }
                                        if (!LoginActivity.this.isFinishing())
                                            pd.dismiss();
                                        Intent intentM = new Intent(
                                                LoginActivity.this,
                                                MainActivity.class);
                                        startActivity(intentM);
                                        finish();

                                    }

                                    @Override
                                    public void onProgress(int progress,
                                                           String status) {
                                    }

                                    @Override
                                    public void onError(final int code,
                                                        final String message) {
                                        // loginFailure2Umeng(start,code,message);
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                progressShow = false;
                                                pd.dismiss();
                                                Toast.makeText(
                                                        getApplicationContext(),
                                                        getString(R.string.Login_failed)
                                                                + message,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                    }

                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 验证手机和密码是否为空
     */
    public boolean chcekNull(String mobile, String pwd) {
        if ("".equals(mobile)) {
            Constans.Toast(this, "手机号码不能为空");
            return false;
        }
//		else {
//			if (!Constans.isMobileNO(mobile)) {
//				Constans.Toast(this, "手机号码格式有误");
//				return false;
//			}
//		}
        if ("".equals(pwd)) {
            Constans.Toast(this, "密码不能为空");
            return false;
        }
        return true;
    }

    // @Override
    // protected void onResume() {
    //
    // }
}
