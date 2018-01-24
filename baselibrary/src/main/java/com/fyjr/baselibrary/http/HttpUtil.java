package com.fyjr.baselibrary.http;


import android.provider.Settings;
import android.text.TextUtils;

import com.fyjr.baselibrary.http.url.HttpUrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;

/**
 * Created by QNapex on 2016/12/5.
 * 网络请求
 */

public class HttpUtil {

    private RetrofitInstance retrofitInstance;

    private HttpUtil() {
        retrofitInstance = RetrofitInstance.getInstance();
    }

    public static HttpUtil getInstance() {
        return HttpUtilHolder.HTTP_UTIL;
    }

    private static class HttpUtilHolder {
        private static final HttpUtil HTTP_UTIL = new HttpUtil();
    }

//    private String token;

//    public String getToken() {
//        return token;
//    }

//    public void setToken(String token) {
//        this.token = token;
//    }

    /**
     * post请求
     *
     * @param path
     * @param map
     * @param callBack
     */
    private void doPost(String path, Map map, Callback callBack) {
        retrofitInstance.post(path, map, callBack);
//        if (TextUtils.isEmpty(token)) {
//            retrofitInstance.post(path, map, callBack);
//        } else {
//            retrofitInstance.postWithToken(path, token, map, callBack);
//        }
    }


    /**
     * get请求
     *
     * @param path
     * @param callBack
     */
    private void doGet(String path, Callback callBack) {
        retrofitInstance.get(path, callBack);
    }

    /**
     * 登录
     *
     * @param NetID      用户名
     * @param PWord      密码
     * @param key        唯一标识
     * @param Type       用户类型：1司机，0货主
     * @param TextFormat 返回格式  字符串 0 | json 1
     * @param callBack
     */
    public void login(String NetID, String PWord, String key, String Type, String TextFormat, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("NetID", NetID);
        map.put("PWord", PWord);
        map.put("key", key);
        map.put("Type", Type);
        map.put("TextFormat", TextFormat);
//        doPost(HttpUrl.LOGIN, map, callBack);
        doGet(HttpUrl.LOGIN + pinUrl(map), callBack);
    }

    private String pinUrl(Map<String, Object> map) {
        String pin = "?";
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            pin = pin + entry.getKey() + "=" + entry.getValue() + "&";
        }
        return pin;
    }


}
