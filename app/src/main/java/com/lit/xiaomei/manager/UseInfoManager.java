package com.lit.xiaomei.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fyjr.baselibrary.utils.DataCleanManager;
import com.fyjr.baselibrary.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lit.xiaomei.activity.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.lit.xiaomei.bean.ReleaseHistory;
import com.lit.xiaomei.bean.User;

/**
 * Created by QNapex on 2017/11/6.
 * 使用信息管理
 */

public class UseInfoManager {

    private static SharedPreferences mSharedPreferences;

    /**
     * 是否为第一次打开应用
     */
    public static String isFirst = "isFirst";
    /**
     * 用户信息
     */
    public static String userInfo = "userInfo";

    private static synchronized SharedPreferences getPreferneces(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return mSharedPreferences;
    }

    /**
     * 保存字符串
     *
     * @return
     */
    public static void putString(Context context, String key, String value) {
        getPreferneces(context).edit().putString(key, value).apply();
    }

    /**
     * 读取字符串
     *
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getPreferneces(context).getString(key, "");
    }

    public static void putReleseaeHistoryArraylist(Context context, ArrayList<ReleaseHistory> releaseHistories) {
        Gson gson = new Gson();
        String json = gson.toJson(releaseHistories);
        getPreferneces(context).edit().putString("ReleaseHistory", json).apply();
    }

    public static ArrayList<ReleaseHistory> getReleseaeHistoryArraylist(Context context) {
        String json = getPreferneces(context).getString("ReleaseHistory", "");
        ArrayList<ReleaseHistory> releaseHistories = new ArrayList<>();
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ReleaseHistory>>() {
            }.getType();
            releaseHistories = gson.fromJson(json, type);
        }
        return releaseHistories;
    }

    /**
     * 保存数组
     *
     * @param context
     * @param key
     * @param values
     */
    public static void putStringArraylist(Context context, String key, ArrayList<String> values) {
        Gson gson = new Gson();
        String json = gson.toJson(values);
        getPreferneces(context).edit().putString(key, json).apply();
    }

    /**
     * 获得数组
     *
     * @param context
     * @param key
     * @return
     */
    public static ArrayList<String> getStringArraylist(Context context, String key) {
        String json = getPreferneces(context).getString(key, "");
        ArrayList<String> arrayList = new ArrayList<>();
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            arrayList = gson.fromJson(json, type);
        }
        return arrayList;
    }

    /**
     * 保存布尔值
     *
     * @return
     */
    public static void putBoolean(Context context, String key, Boolean value) {
        getPreferneces(context).edit().putBoolean(key, value).apply();
    }

    /**
     * 读取布尔值
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getPreferneces(context).getBoolean(key, defValue);
    }

    /**
     * 打印所有
     */
    public static void print(Context context) {
        System.out.println(getPreferneces(context).getAll());
    }

    /**
     * 移除字段
     *
     * @return
     */
    public static void removeString(Context context, String key) {
        getPreferneces(context).edit().remove(key).apply();
    }

    /**
     * 清空保存在默认SharePreference下的所有数据
     */
    public static void clear(Context context) {
        getPreferneces(context).edit().clear().apply();
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param userBean
     */
    public static void saveUser(Context context, User userBean) {
        putString(context, userInfo, JsonUtils.toJson(userBean));
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @return
     */
    public static User getUser(Context context) {
        return JsonUtils.parse(getString(context, userInfo), User.class);
    }

    /**
     * 清空用户信息
     *
     * @param context
     */
    public static void clearUser(Context context) {
        removeString(context, userInfo);
    }

    /**
     * 注销登录
     *
     * @param context
     */
    public static void logout(Context context, boolean isRest) {
        DataCleanManager.cleanApplicationData(context, "");
        clearUser(context);
        if (isRest) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("isRest", true);
            context.startActivity(intent);
        }
    }
}
