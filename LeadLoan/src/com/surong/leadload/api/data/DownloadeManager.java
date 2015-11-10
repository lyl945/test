package com.surong.leadload.api.data;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.surong.leadloan.R;



public class DownloadeManager {

    private Context mContext;
    private String apkNames;
    private String apkUrl;
    //提示语
//   private String updateMsg = "有最新的软件包哦，快下载吧~";      
    //返回的安装包url
    // private String apkUrl = "http://hiao.com/android/bus/QingDaoBus.apk";
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private String savePath = "";

    private String saveFileName = "";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    public boolean isdown = false;
    private TextView updatePercentTextView;
    private TextView updateCurrentTextView;
    private TextView updateTotalTextView;
    private int apkLength;
    private int apkCurrentDownload;

    public DownloadeManager(Context mContext, String apkNames, String apkUrl) {
        this.mContext = mContext;
        this.apkNames = apkNames;
        this.apkUrl = apkUrl;
        this.savePath =  Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
    			+ "Android/surong.leadload.api/file/";
        this.saveFileName = savePath + apkNames;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    updatePercentTextView.setText(progress + "" + "%");
                    try {
                        int currentM, currentK, totalM, totalK;
                        currentM = apkCurrentDownload / 1024 / 1024;
                        currentK = apkCurrentDownload / 1024 * 100 /1024 - apkCurrentDownload / 1024 / 1024 *100 ;
                        totalM = apkLength / 1024 / 1024;
                        totalK = apkLength / 1024 * 100 /1024 - apkLength / 1024 / 1024 *100 ;
                        updateCurrentTextView.setText(currentM + "." + currentK + "MB/");
                        updateTotalTextView.setText(totalM + "." + totalK + "MB");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }
        ;
    };


    //外部接口让主Activity调用
    public void checkUpdateInfo(String alert_msg) {
        showNoticeDialog(alert_msg);
    }


    private void showNoticeDialog(String alertInfo) {
        Builder builder = new Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(alertInfo);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    public void showDownloadDialog() {
        Builder builder = new Builder(mContext);
        builder.setTitle("领贷App下载");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        updatePercentTextView = (TextView) v.findViewById(R.id.updatePercentTextView);
        updateCurrentTextView = (TextView) v.findViewById(R.id.updateCurrentTextView);
        updateTotalTextView = (TextView) v.findViewById(R.id.updateTotalTextView);
        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               
                interceptFlag = true;
            }
        }); 
//        builder.setNeutralButton("暂停", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                try {
//					dialog.wait();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
//        });
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                apkLength = conn.getContentLength();
                System.out.println();
                InputStream is = conn.getInputStream();
                apkCurrentDownload = 0;
                byte buf[] = new byte[1024];
                int length = -1 ;
                while((length = is.read(buf))!=-1){
                	 apkCurrentDownload += length;
                     progress = (int) (((float) apkCurrentDownload / apkLength) * 100);
                     //更新进度   
                     mHandler.sendEmptyMessage(DOWN_UPDATE);
                     fos.write(buf, 0, length);
                     if (apkCurrentDownload == apkLength) {
                         //下载完成通知安装   
                         mHandler.sendEmptyMessage(DOWN_OVER);
                         break;
                     }
                     if(interceptFlag ){
                    	 ApkFile.delete();
                    	 break;
                     }
                }
                fos.close();
                is.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        try {
            if (downloadDialog!=null&&downloadDialog.isShowing())
            downloadDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}  

