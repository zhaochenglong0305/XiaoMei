package com.fyjr.baselibrary.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Toast mToast = null;

    private static void showToast(Context context, String text, int duration) {
        try {
            if (mToast == null) {
                mToast = Toast.makeText(context, text, duration);
            } else {
                mToast.setText(text);
                mToast.setDuration(duration);
            }
            mToast.show();
            Log.e("显示Toast通知", "" + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示Toast通知
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        showToast(context, text, 0);
    }

    /**
     * 长时间显示通知
     *
     * @param context
     * @param text
     */
    public static void showLongToast(Context context, String text) {
        showToast(context, text, 1);
    }
}
