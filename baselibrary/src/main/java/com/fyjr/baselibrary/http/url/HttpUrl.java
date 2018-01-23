package com.fyjr.baselibrary.http.url;

/**
 * Created by QNapex on 2016/12/4.
 * 接口地址
 */

public class HttpUrl {

    /**
     * 线上服务器
     */
//    public static String BASE_URL = "http://www.shijiupindao.com/";

    /**
     * 测试服务器
     */
    public static String BASE_URL = "http://192.168.0.17:8080/";

    /**
     * 登录
     */
    public static String LOGIN = "clubApp/login";


    /**
     * 注册
     */
    public static String REGISTER = "clubApp/login/register";
    /**
     * 手机发送验证码
     */
    public static String SENDSMS = "clubApp/login/sendSMS";
    /**
     * 修改密码或者找回密码
     */
    public static String UPDATEPASSWORD = "clubApp/login/updatePassWord";

}
