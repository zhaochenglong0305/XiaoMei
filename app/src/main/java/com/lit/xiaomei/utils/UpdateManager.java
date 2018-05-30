package com.lit.xiaomei.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.fyjr.baselibrary.utils.VersionUtil;
import com.lit.xiaomei.R;
import com.lit.xiaomei.activity.MainActivity;

public class UpdateManager {
    ArrayList<String> getCode = new ArrayList<String>();
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_START = 10;
    private static final String apkName = "assist" + System.currentTimeMillis();

    private static final int DOWNLOAD_FINISH = 2;
    private static final int DOWNLOAD_ERROR = 3;
    private AlertDialog updateYesOrNo;

    public String getUrlStr() {
        return urlStr;
    }

    public int getServiceVersionCode() {
        return serviceVersionCode;
    }

    String urlStr = null;
    int serviceVersionCode = 0;
    int downloadCount = 0;
    private String mSavePath;
    private int progress;
    private boolean cancelUpdate = false;
    private Context mContext;
    private NotificationManager manager;
    private Notification notif;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD:
                    System.out.println(progress);
                    notif.contentView.setTextViewText(R.id.content_view_text1,
                            "正在下载... " + progress + "%");
                    notif.contentView.setProgressBar(R.id.content_view_progress,
                            100, progress, false);
                    manager.notify(0, notif);
                    break;
                case DOWNLOAD_START:
                    Toast.makeText(mContext, "正在下载。。。", Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOAD_FINISH:
                    Toast.makeText(mContext, "下载完成！", Toast.LENGTH_SHORT).show();
                    notif.contentView.setTextViewText(R.id.content_view_text1,
                            "下载完成！");
                    notif.contentView.setProgressBar(R.id.content_view_progress,
                            100, 100, false);
                    manager.notify(0, notif);
                    installApk();
                    break;
                case DOWNLOAD_ERROR:
                    Toast.makeText(mContext, "下载失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    public UpdateManager(Context context, String apkUrl) {
        this.mContext = context;
        this.urlStr = apkUrl;

    }

    private boolean must = false;

    public void checkUpdate(int versionCode) {
        int old = VersionUtil.getVersionCode(mContext);
        if (versionCode > old) {
            if (versionCode - old >= 3) {
                must = true;
            } else {
                must = false;
            }
            showNoticeDialog();
        }
    }


    private void showNoticeDialog() {
        if (updateYesOrNo == null) {
            updateYesOrNo = new AlertDialog.Builder(mContext).create();
            updateYesOrNo.setCancelable(false);
            updateYesOrNo.show();
            updateYesOrNo.getWindow().setContentView(
                    R.layout.update_yesorno_dialog);
            View okButton = updateYesOrNo.findViewById(R.id.ok);
            okButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    updateYesOrNo.dismiss();
                    showDownloadDialog();
                    // Intent intent = new Intent();
                    // intent.setAction("android.intent.action.VIEW");
                    // Uri content_url =
                    // Uri.parse("http://www.chengtianhuiju.com/liuyu/apk/Walker.apk");
                    // intent.setData(content_url);
                    // mContext.startActivity(intent);

                }
            });
            View cancelButton = updateYesOrNo.findViewById(R.id.cancel);
            cancelButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (must) {
                        System.exit(0);
                    } else {
                        updateYesOrNo.dismiss();
                    }
                }
            });
        } else {
            if (!updateYesOrNo.isShowing())
                updateYesOrNo.show();
        }
    }

    private void showDownloadDialog() {
        // 点击通知栏后打开的activity
        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent,
                0);
        manager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notif = new Notification();
        notif.icon = R.mipmap.tiger;
        notif.tickerText = "更新通知";
        // 通知栏显示所用到的布局文件
        notif.contentView = new RemoteViews(mContext.getPackageName(),
                R.layout.p);
        notif.contentIntent = pIntent;
        manager.notify(0, notif);
        downloadApk();
    }

    private void downloadApk() {
        // TODO Auto-generated method stub
        new DownloadApkThread().start();
    }

    private class DownloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    String sdpath = Environment.getExternalStorageDirectory()
                            + "/";
                    mSavePath = sdpath + "wlt" + "/";
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestProperty("Accept-Encoding", "identity");
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, apkName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    mHandler.sendEmptyMessage(DOWNLOAD_START);
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        if (downloadCount == 0 || progress - 5 > downloadCount) {
                            downloadCount += 5;
                            mHandler.sendEmptyMessage(DOWNLOAD);
                        }
                        if (numread <= 0) {
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            }
        }
    }

    private void installApk() {
        File apkfile = new File(mSavePath, apkName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.lit.xiaomei.fileProvider", apkfile);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }
}
