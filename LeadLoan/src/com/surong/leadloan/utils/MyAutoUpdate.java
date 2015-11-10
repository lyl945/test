package com.surong.leadloan.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.surong.leadloan.R;

/**
 * �汾��⣬�Զ�����
 * 
 *  1.ͨ��Url������ 2.���ز���װ���� 3.ɾ����ʱ·��
 * 
 */
public class MyAutoUpdate {
        // ���ø��µ�Activity
        public Activity activity = null;
        // ��ǰ�汾��
        public int versionCode ;
        //�������˰汾��
        public int serverApkVersion ;
        // ��ǰ�汾����
        public String versionName = "";
        // ����̨��Ϣ��ʶ
        private static final String TAG = "AutoUpdate";
        // �ļ���ǰ·��
        private String currentFilePath = "";
        // ��װ���ļ���ʱ·��
        private String currentTempFilePath = "";
        // ����ļ���չ���ַ���
        private String fileEx = "";
        // ����ļ����ַ���
        private String fileNa = "";
        // ��������ַ
        private String strURL ;
        private ProgressDialog dialog;
        private String log;//������־
        String UPDATE_SERVERAPK = "SurongApp.apk";

        /**
         * ���췽������õ�ǰ�汾��Ϣ
         * 
         * @param activity
         */
        public MyAutoUpdate(Activity activity) {
                this.activity = activity;
                // ��õ�ǰ�汾
                getCurrentVersion();
                getServiceVersion();
        }

 
        private void getServiceVersion() {
			//HttpServiceBean.instance().httpUpdate(myhandler, activity);
			
		}
        
        Handler myhandler = new Handler(){
        	public void handleMessage(android.os.Message msg) {
        		switch (msg.what) {
				case 0:
					JSONObject object = (JSONObject) msg.obj;
					JSONObject version = null;
					
					try {
						String string = object.getString("versions");
						JSONArray array = new JSONArray(string);
						version = (JSONObject) array.get(0);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						serverApkVersion = Integer.valueOf(version.getString("number"));
						strURL = version.getString("downloadLink");
						log = version.getString("log");
						check();
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;

				default:
					break;
				}
        	};
        };


		public void check() {
              if(versionCode==serverApkVersion){
            	 System.out.println("�汾��ͬ");
             }
             if (versionCode<serverApkVersion) {
            	 showUpdateDialog();
             }
        }
        
       
        /**
         * ����Ƿ��п�������
         * 
         * @param context
         * @return ��������״̬
         */
        public static boolean isNetworkAvailable(Context context) {
                try {
                        ConnectivityManager cm = (ConnectivityManager) context
                                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                        // ��ȡ������Ϣ
                        NetworkInfo info = cm.getActiveNetworkInfo();
                        // ���ؼ�������״̬
                        return (info != null && info.isConnected());
                } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                }
        }

        /**
         * �����Ի���ѡ���Ƿ���Ҫ���°汾
         */
        public void showUpdateDialog() {
                @SuppressWarnings("unused")
                AlertDialog alert = new AlertDialog.Builder(this.activity)
                                .setTitle("�°汾").setIcon(R.drawable.logo_icon)
                                .setMessage(log)
                                .setPositiveButton("��", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                                // ͨ����ַ�����ļ�
                                        	downFile(strURL);
                                                // ��ʾ����״̬��������
                                                showWaitDialog();
                                        }
                                })
                                .setNegativeButton("��", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                        }
                                }).show();
        }

        /**
         * ��ʾ����״̬��������
         */
        public void showWaitDialog() {
                dialog = new ProgressDialog(activity);
                dialog.setMessage("���ڸ��£����Ժ�...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.show();
        }

        /**
         * ��õ�ǰ�汾��Ϣ
         */
        public void getCurrentVersion() {
                try {
                        // ��ȡӦ�ð���Ϣ
                        PackageInfo info = activity.getPackageManager().getPackageInfo(
                                        activity.getPackageName(), 0);
                        this.versionCode = info.versionCode;
                        this.versionName = info.versionName;
                } catch (NameNotFoundException e) {
                        e.printStackTrace();
                }
        }

        /**
         * ��ȡ�ļ����Ʋ�ִ������
         * 
         * @param strPath
         */
        public void downFile(final String url){
	        new Thread(){
	            public void run(){
	                HttpClient client = new DefaultHttpClient();
	                HttpGet get = new HttpGet(url);
	                HttpResponse response;
	                try {
	                    response = client.execute(get);
	                    HttpEntity entity = response.getEntity();
	                    long length = entity.getContentLength();
	                    InputStream is =  entity.getContent();
	                    FileOutputStream fileOutputStream = null;
	                    if(is != null){
	                        File file = new File(Environment.getExternalStorageDirectory(),UPDATE_SERVERAPK);
	                        fileOutputStream = new FileOutputStream(file);
	                        byte[] b = new byte[1024];
	                        int charb = -1;
	                        int count = 0;
	                        while((charb = is.read(b))!=-1){
	                            fileOutputStream.write(b, 0, charb);
	                            count += charb;
	                        }
	                    }
	                    fileOutputStream.flush();
	                    if(fileOutputStream!=null){
	                        fileOutputStream.close();
	                    }
	                    down();
	                }  catch (Exception e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            }
	        }.start();
	    }
	
	  Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {

	            super.handleMessage(msg); 
	            dialog.dismiss();
	            update();
	        }
	    };
	   
	    /**
	     * ������ɣ�ͨ��handler�����ضԻ���ȡ��
	     */
	    public void down(){
	        new Thread(){
	            public void run(){
	                Message message = handler.obtainMessage();
	                handler.sendMessage(message);
	            }
	        }.start();
	    }
	   
	    /**
	     * ��װӦ��
	     */
	    public void update(){
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),UPDATE_SERVERAPK))
	                , "application/vnd.android.package-archive");
	        activity.startActivity(intent);
	    }
	 

        /**
         * ��������ļ�������
         * 
         * @param f
         *            �ļ�����
         * @return �ļ�����
         */
        private String getMIMEType(File f) {
                String type = "";
                // ����ļ�����
                String fName = f.getName();
                // ����ļ���չ��
                String end = fName
                                .substring(fName.lastIndexOf(".") + 1, fName.length())
                                .toLowerCase();
                if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
                        type = "audio";
                } else if (end.equals("3gp") || end.equals("mp4")) {
                        type = "video";
                } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                                || end.equals("jpeg") || end.equals("bmp")) {
                        type = "image";
                } else if (end.equals("apk")) {
                        type = "application/vnd.android.package-archive";
                } else {
                        type = "*";
                }
                if (end.equals("apk")) {
                } else {
                        type += "/*";
                }
                return type;
        }
}
