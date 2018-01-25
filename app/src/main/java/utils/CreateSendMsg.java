package utils;

import android.content.Context;
import android.util.Base64;

import manager.UseInfoManager;

/**
 * Created by Adminis on 2018/1/25.
 */

public class CreateSendMsg {
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
}
