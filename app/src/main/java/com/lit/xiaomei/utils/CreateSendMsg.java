package com.lit.xiaomei.utils;

import android.content.Context;
import android.util.Base64;

import com.lit.xiaomei.manager.UseInfoManager;

/**
 * Created by Adminis on 2018/1/25.
 */

public class CreateSendMsg {
    /**
     * 心跳包
     *
     * @param context
     * @return
     */
    public static String createHeartBeatMsg(Context context) {
        // BG:CONN,US:123456,PW:1234567890,KY:1234567890,ND|
        String s = "BG:CONN";
        s = s + ",US:" + UseInfoManager.getUser(context).getListData().get(0).getUS();
        s = s + ",PW:" + UseInfoManager.getUser(context).getListData().get(0).getPW();
        s = s + ",KY:" + UseInfoManager.getUser(context).getListData().get(0).getKY();
        s = s + ",ND";
        String s64 = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
        s64 = s64 + "|";
        return s64;
    }

    /**
     * 用户切换城市，开始获得信息
     *
     * @param context
     * @param sheng   省份
     * @param city    城市
     * @return
     */
    public static String createInformationMsg(Context context, String sheng, String city) {
//        BG:SJQQ,US:123456,PW:1234567890,KY:1234567890,PR:省份,CT:城市,PH:电话,MS:信息内容,TY:1,FU:10098,ND|
        String s = "BG:SJQQ";
        s = s + ",US:" + UseInfoManager.getUser(context).getListData().get(0).getUS();
        s = s + ",PW:" + UseInfoManager.getUser(context).getListData().get(0).getPW();
        s = s + ",KY:" + UseInfoManager.getUser(context).getListData().get(0).getKY();
        s = s + ",PR:" + sheng;
        s = s + ",CT:" + city;
        s = s + ",PH:" + "";
        s = s + ",MS:" + "";
        s = s + ",TY:" + "1";
        s = s + ",FU:" + "";
        s = s + ",ND";
        String s64 = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
        s64 = s64 + "|";
        return s64;
    }

    /**
     * 数据上报
     *
     * @param context
     * @param type     数据类型
     * @param fromcity 出发城市
     * @param tocity   到达城市
     * @param msg      信息
     * @return
     */
    public static String createReleaseMsg(Context context, String type,
                                          String fromcity, String tocity, String msg) {
        String s = "BG:SJSB";
        s = s + ",US:" + UseInfoManager.getUser(context).getListData().get(0).getUS();
        s = s + ",PW:" + UseInfoManager.getUser(context).getListData().get(0).getPW();
        s = s + ",KY:" + UseInfoManager.getUser(context).getListData().get(0).getKY();
        s = s + ",CH:" + type;
        s = s + ",PR:" + UseInfoManager.getUser(context).getListData().get(0).getPR();
        s = s + ",GS:" + UseInfoManager.getUser(context).getListData().get(0).getCT();
        s = s + ",SF:" + fromcity;
        s = s + ",MD:" + tocity;
        s = s + ",MS:" + msg;
        s = s + ",PH:" + UseInfoManager.getUser(context).getListData().get(0).getPH();
        s = s + ",CM:" + UseInfoManager.getUser(context).getListData().get(0).getCM();
        s = s + ",ND";
        String s64 = Base64.encodeToString(s.getBytes(), Base64.DEFAULT);
        s64 = s64 + "|";
        return s64;
    }
}
