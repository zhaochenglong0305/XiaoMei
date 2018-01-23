package com.fyjr.baselibrary.http;


import android.text.TextUtils;

import com.fyjr.baselibrary.http.url.HttpUrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
     * @param callBack
     */
    public void login(String userName, String passWord, String deviceCode, String nickName, String headUrl, int loginType, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        map.put("passWord", passWord);
        map.put("deviceCode", deviceCode);
        map.put("nickName", nickName);
        map.put("headUrl", headUrl);
        map.put("loginType", loginType);
        doPost(HttpUrl.LOGIN, map, callBack);
    }

}
