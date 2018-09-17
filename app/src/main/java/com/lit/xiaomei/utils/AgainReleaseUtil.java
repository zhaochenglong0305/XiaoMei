package com.lit.xiaomei.utils;

import android.os.Handler;
import android.util.Log;

import com.lit.xiaomei.activity.MainActivity;
import com.lit.xiaomei.bean.ReleaseHistory;

/**
 * Created by Administrator on 2018/5/26.
 */

public class AgainReleaseUtil {
    private ReleaseHistory releaseHistory = new ReleaseHistory();
    private MainActivity mainActivity;
    private int againNum = 0;
    private long againTime = 0;
    private Handler mHandler;
    private OnUpdateNumListener listener;

    public AgainReleaseUtil(ReleaseHistory releaseHistory, MainActivity mainActivity, OnUpdateNumListener listener) {
        mHandler = new Handler();
        this.releaseHistory = releaseHistory;
        this.mainActivity = mainActivity;
        this.listener = listener;
    }

    public void doAgain() {
        againNum = releaseHistory.getAgainNum();
//        againTime = 10000;
        againTime = releaseHistory.getAgainTime() * 60 * 1000;
        Log.e("again", releaseHistory.getReleaseContext() + "---重发次数" + againNum + "---间隔时间" + againTime);
        mHandler.postDelayed(releaseRunnable, againTime);
    }

    private Runnable releaseRunnable = new Runnable() {
        @Override
        public void run() {
            if (againNum != 0) {
                String releseMsg = CreateSendMsg.createReleaseMsg(mainActivity, releaseHistory.getReleaseType(),
                        releaseHistory.getFrom(), "", releaseHistory.getReleaseContext());
                mainActivity.sendMsgToSocket(releseMsg, true);
                againNum--;
                Log.e("again", releaseHistory.getReleaseContext() + "---重发成功,剩余次数" + againNum);
                listener.onUpdate(againNum);
                mHandler.postDelayed(this, againTime);
            }
        }
    };

    public interface OnUpdateNumListener {
        void onUpdate(int num);
    }

}
