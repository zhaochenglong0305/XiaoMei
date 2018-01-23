package com.fyjr.baselibrary.utils;

/**
 * Created by QNapex on 2016/12/1.
 */

public class Log {

    public static boolean isLog = true;

    public static int d(String tag, String msg) {
        if (isLog) {
            return android.util.Log.d(tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (isLog) {
            return android.util.Log.e(tag, msg);
        }
        return 0;
    }
}
