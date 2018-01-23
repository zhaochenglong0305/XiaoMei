package com.fyjr.baselibrary.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by \(^o^)/~ on 2016/7/11.
 */
public class DialogUtil {


    private static ProgressDialog dialog;

    public static void showProgress(Context context, String msg) {
        showProgress(context, msg, true);
    }

    public static void showProgress(Context context, String msg, boolean isCancel) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(isCancel);
            dialog.setMessage(msg);
            dialog.show();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog1) {
                    dialog = null;
                }
            });
        }
    }


    public static void closeProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
