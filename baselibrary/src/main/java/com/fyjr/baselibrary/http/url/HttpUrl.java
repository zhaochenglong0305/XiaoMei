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
    public static String BASE_URL = "http://113.6.252.165:8081/";

    /**
     * 登录
     */
    public static String LOGIN = "webservice/Json/Login.aspx";

    /**
     * 信息搜索
     */
    public static String SEARCHINFORMATION = "webservice/search/GetSearDataNew.aspx";

    /**
     * 周边信息搜索
     */
    public static String SEARCHNEARBYINFORMATION = "webservice/search/GetSearchDataYx.aspx";
    /**
     * 根据货站网号得到司机信息
     */
    public static String SEARCHDRIVERS = "webservice/Car/JSon/GetVarList.aspx";
    /**
     * 添加司机信息
     */
    public static String ADDDRIVERSMESSAGE = "webservice/Car/JSon/RegCar.aspx";
    /**
     * 获得验证码
     */
    public static String GETYZM = "webservice/Message/UpPhone.aspx";
    /**
     * 核对验证码
     */
    public static String CHECKYZM = "webservice/Message/UpPhone.aspx";
    /**
     * 修改密码
     */
    public static String UPDATEPASSWORD = "webservice/Json/ChangePass.aspx";
    /**
     * 获得新闻信息
     */
    public static String GETNEWS = "webservice/Json/ReturnBaseGuanggao.aspx";
    /**
     * 获得个人信息
     */
    public static String GETUSERINFORMATION = "webservice/search/GetUserReg.aspx";
    /**
     * 校验推荐人
     */
    public static String CHECKTJR = "webservice/Json/checkTJR.aspx";
    /**
     * 注册
     */
    public static String REGIST = "webservice/Json/RegUser.aspx";
    /**
     * 检查用户权限
     */
    public static String CHECKAUTHORITY = "webservice/search/GetInfoPhone.aspx";
    /**
     * 获得各城市的客服信息
     */
    public static String GETCITYPHONE = "webservice/Json/ReturnCityPhone.aspx";


}
