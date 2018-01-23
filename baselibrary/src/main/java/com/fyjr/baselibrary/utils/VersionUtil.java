package com.fyjr.baselibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by QNapex on 2016/12/21.
 */

public class VersionUtil {

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            Log.e("版本名 " + pi.versionName, "版本号 " + pi.versionCode);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

    public static boolean isNew(String versionName, Context context) {
        return !TextUtils.isEmpty(versionName) && Float.parseFloat(versionName) > getVersionCode(context);
    }
}
