package com.lit.xiaomei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.TimeUtils;
import android.text.TextUtils;

import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.utils.Log;
import com.fyjr.baselibrary.utils.TimeUtil;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.Constants;
import com.lit.xiaomei.databinding.ActivityWelcomeBinding;
import com.lit.xiaomei.manager.UseInfoManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding> {
    private GetNetIPHandler getNetIPHandler;
    private static final int GETNETIPFAIL = 0;
    private static final int GETNETIPSUCCESS = 1;
    private static final int GETNETIPNOTFIND = 2;
    private boolean isStopCount = false;
    private Handler mHandler = new Handler();
    private long timer = 0;
    private long finishTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_welcome);
    }

    @Override
    public void initView() {
        super.initView();
        getNetIPHandler = new GetNetIPHandler();
        initNetIP();
    }

    private void initNetIP() {
        countTimer();
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    String serverURL = "http://blog.sina.com.cn/s/blog_1669867c40102x4m7.html";
                    // String serverURL =
                    // "http://blog.sina.com.cn/s/blog_1669867c40102x0mb.html";
                    Document document = Jsoup.connect(serverURL).get();
                    Elements elements = document.select("div");
                    Element element = elements.get(15);
                    if (!TextUtils.isEmpty(element.text())) {
                        msg.what = GETNETIPSUCCESS;
                        msg.obj = element.text();
                    } else {
                        msg.what = GETNETIPNOTFIND;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = GETNETIPFAIL;
                }
                getNetIPHandler.sendMessage(msg);
            }
        }.start();
    }

    private class GetNetIPHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<String> ips = new ArrayList<>();
            switch (msg.what) {
                case GETNETIPFAIL:
//                    113.6.252.165
                    ips.add("http://113.6.252.165:8081/");
                    break;
                case GETNETIPSUCCESS:
                    String res = (String) msg.obj;
                    if (res.contains("ABC") && res.contains("DEF")) {
                        String sub = res.substring(res.indexOf("ABC") + 3, res.indexOf("DEF"));
                        if (sub.contains(",")) {
                            String[] subs = sub.split(",");
                            for (int i = 0; i < subs.length; i++) {
                                ips.add("http://" + subs[i] + ":8081/");
                            }
                        } else {
                            ips.add(sub);
                        }
                    } else {
                        ips.add("http://113.6.252.165:8081/");
                    }
                    break;
                case GETNETIPNOTFIND:
                    ips.add("http://113.6.252.165:8081/");
                    break;
            }
            finishTime = timer;
            Log.e("long", finishTime + "====" + timer);
            UseInfoManager.putStringArraylist(WelcomeActivity.this, "NetIPs", ips);
        }
    }


    private Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            if (finishTime >= 2) {
                Log.e("long", "finishTime==" + finishTime);
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            } else {
                if (timer >= 2) {
                    Log.e("long", "timer" + timer);
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }
    };

    private Runnable TimerRunnable = new Runnable() {

        @Override
        public void run() {
            if (!isStopCount) {
                timer += 1;
            }
            countTimer();
        }
    };

    private void countTimer() {
        mHandler.postDelayed(TimerRunnable, 1000);
        mHandler.postDelayed(loginRunnable, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(TimerRunnable);
        mHandler.removeCallbacks(loginRunnable);
    }
}
