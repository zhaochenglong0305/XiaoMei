package com.fyjr.baselibrary.http;


import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

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
        Log.e("ting", "doGetPath:" + path);
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
    public void againLogin(String NetID, String PWord, String key, String Type, String TextFormat, Callback callBack) {
        retrofitInstance.againInit();
        Map<String, Object> map = new HashMap<>();
        map.put("NetID", NetID);
        map.put("PWord", PWord);
        map.put("key", key);
        map.put("Type", Type);
        map.put("TextFormat", TextFormat);
        doGet(HttpUrl.LOGIN + pinUrl(map), callBack);
    }


    /**
     * 版本更新
     * http://113.6.252.164:8081/sjfwq/web/ApkUpdate/CheckApkUpdate_List.aspx
     * 版本上传
     *
     * @param callBack
     */
    public void checkVersion(Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("ID", 10);
        doGet(HttpUrl.CHECKVERSION + pinUrl(map), callBack);
    }

    /**
     * 信息搜索
     *
     * @param USER
     * @param PASS
     * @param KEYY
     * @param PROV
     * @param CITY
     * @param INCITY
     * @param INCLASS
     * @param INPHONE
     * @param INFOR
     * @param callBack
     */
    public void searchInformation(String USER, String PASS, String KEYY,
                                  String ID, String PROV, String CITY,
                                  String INCITY, String INCLASS, String INPHONE,
                                  String INFOR, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("USER", USER);
        map.put("PASS", PASS);
        map.put("KEYY", KEYY);
        map.put("ID", ID);
        map.put("PROV", PROV);
        map.put("CITY", CITY);
        map.put("INCITY", INCITY);
        map.put("INCLASS", INCLASS);
        map.put("INPHONE", INPHONE);
        map.put("INFOR", INFOR);
        map.put("TextFormat", "1");
        doGet(HttpUrl.SEARCHINFORMATION + pinUrl(map), callBack);
    }

    public void searchNearbyInformation(String USER, String PASS, String KEYY,
                                        String ID, String PROV, String BeginCITY,
                                        String EndCITY, String INCITY, String INCLASS, String INPHONE,
                                        String INFOR, String StatusFormat, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("USER", USER);
        map.put("PASS", PASS);
        map.put("KEYY", KEYY);
        map.put("ID", ID);
        map.put("PROV", PROV);
        map.put("BeginCITY", BeginCITY);
        map.put("EndCITY", EndCITY);
        map.put("INCITY", INCITY);
        map.put("INCLASS", INCLASS);
        map.put("INPHONE", INPHONE);
        map.put("INFOR", INFOR);
        map.put("TextFormat", "1");
        map.put("StatusFormat", StatusFormat);
        doGet(HttpUrl.SEARCHNEARBYINFORMATION + pinUrl(map), callBack);
    }


    /**
     * 根据货站网号得到司机信息
     *
     * @param NetID
     * @param callBack
     */
    public void searchDrivers(String NetID, String LicensePlate, String CarID, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("NetID", NetID);
        map.put("LicensePlate", LicensePlate);
        map.put("CarID", CarID);
        doGet(HttpUrl.SEARCHDRIVERS + pinUrl(map), callBack);
    }

    /**
     * 获得验证码
     *
     * @param phone    手机号
     * @param callBack
     */
    public void getYZM(String phone, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        doGet(HttpUrl.GETYZM + pinUrl(map), callBack);
    }

    /**
     * 核对验证码
     *
     * @param phone
     * @param yzm
     * @param callBack
     */
    public void checkYZM(String phone, String yzm, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("yzm", yzm);
        doGet(HttpUrl.CHECKYZM + pinUrl(map), callBack);
    }


    /**
     * 修改密码
     *
     * @param NetID
     * @param PWD
     * @param callBack
     */
    public void updatePassword(String NetID, String PWD, String key, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("NetID", NetID);
        map.put("PWD", PWD);
        map.put("key", key);
        doGet(HttpUrl.UPDATEPASSWORD + pinUrl(map), callBack);
    }

    /**
     * 获得新闻信息
     *
     * @param ClassID
     * @param callBack
     */
    public void getNews(String ClassID, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("ClassID", ClassID);
        doGet(HttpUrl.GETNEWS + pinUrl(map), callBack);
    }

    /**
     * 获得个人信息
     *
     * @param NetID
     * @param PWord
     * @param key
     * @param Type
     * @param callBack
     */
    public void getUserInformation(String NetID, String PWord, String key, String Type, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("NetID", NetID);
        map.put("PWord", PWord);
        map.put("key", key);
        map.put("Type", Type);
        map.put("TextFormat", "1");
        doGet(HttpUrl.GETUSERINFORMATION + pinUrl(map), callBack);
    }

    /**
     * 核对推荐人
     *
     * @param TJRID
     * @param Province
     * @param City
     * @param callBack
     */
    public void checkTJR(String TJRID, String Province, String City, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("TJRID", TJRID);
        map.put("Province", Province);
        map.put("City", City);
        doGet(HttpUrl.CHECKTJR + pinUrl(map), callBack);
    }

    /**
     * 获得各城市客服信息
     *
     * @param callBack
     */
    public void getCityPhone(String DName, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("DName", DName);
        doGet(HttpUrl.GETCITYPHONE + pinUrl(map), callBack);
    }

    /**
     * 检查用户权限
     *
     * @param NetID
     * @param PWord
     * @param key
     * @param PR
     * @param CT
     * @param XH
     * @param QC
     * @param callBack
     */
    public void checkAuthority(String NetID, String PWord, String key, String PR, String CT, String XH, String QC, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("NetID", NetID);
        map.put("PWord", PWord);
        map.put("key", key);
        map.put("PR", PR);
        map.put("CT", CT);
        map.put("XH", XH);
        map.put("QC", QC);
        doGet(HttpUrl.CHECKAUTHORITY + pinUrl(map), callBack);

    }

    /**
     * 用户注册
     *
     * @param user
     * @param pwd
     * @param name
     * @param station
     * @param phone
     * @param sheng
     * @param city
     * @param key
     * @param TJRID
     * @param yzm
     * @param callBack
     */
    public void doRegist(String user, String pwd, String name, String station, String phone,
                         String sheng, String city, String key, String TJRID, String yzm, String ClassID, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("NetID", user);
        map.put("PWord", pwd);
        map.put("Name", name);
        map.put("Tel1", user);
        map.put("Tel2", phone);
        map.put("Province", sheng);
        map.put("City", city);
        map.put("Caption", station);
        map.put("key", key);
        map.put("TJRID", TJRID);
        map.put("yzm", yzm);
        map.put("ClassID", ClassID);
        doGet(HttpUrl.REGIST + pinUrl(map), callBack);
    }

    /**
     * 添加司机信息
     *
     * @param OPENID
     * @param NetID
     * @param PWord
     * @param key
     * @param Name
     * @param Tel1
     * @param Tel2
     * @param Province
     * @param City
     * @param cardcode
     * @param LicensePlate
     * @param vehicleLength
     * @param motorcycleType
     * @param FatherNetID
     * @param callBack
     */
    public void addDrivers(String OPENID, String NetID, String PWord,
                           String key, String Name, String Tel1,
                           String Tel2, String Province, String City,
                           String cardcode, String LicensePlate, String vehicleLength,
                           String motorcycleType, String FatherNetID, Callback callBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("OPENID", OPENID);
        map.put("NetID", NetID);
        map.put("PWord", PWord);
        map.put("key", key);
        map.put("Name", Name);
        map.put("Tel1", Tel1);
        map.put("Tel2", Tel2);
        map.put("Province", Province);
        map.put("City", City);
        map.put("cardcode", cardcode);
        map.put("LicensePlate", LicensePlate);
        map.put("vehicleLength", vehicleLength);
        map.put("motorcycleType", motorcycleType);
        map.put("FatherNetID", FatherNetID);
        doGet(HttpUrl.ADDDRIVERSMESSAGE + pinUrl(map), callBack);
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
