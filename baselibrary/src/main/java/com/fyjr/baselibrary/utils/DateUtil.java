package com.fyjr.baselibrary.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 转换成年月日
     *
     * @param beginDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String changeTime(long beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sb = sdf.format(new Date(beginDate));
        return sb;
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 转换成年月日时分
     *
     * @param beginDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String changeTime1(long beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sb = sdf.format(new Date(beginDate));
        return sb;
    }

    /**
     * 转换成时分
     *
     * @param beginDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String changeTime2(long beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String sb = sdf.format(new Date(beginDate));
        return sb;
    }

    /**
     * 转换成年月日时分秒
     *
     * @param beginDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String changeTime3(long beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sb = sdf.format(new Date(beginDate));
        return sb;
    }

    /**
     * 转换成时
     *
     * @param beginDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String changeTime4(long beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String sb = sdf.format(new Date(beginDate));
        return sb;
    }

    /**
     * 转换成分
     *
     * @param beginDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String changeTime5(long beginDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String sb = sdf.format(new Date(beginDate));
        return sb;
    }


    /**
     * 将String转换为Date
     *
     * @param dateStr
     * @return
     */
    public static Date stringToDate(String dateStr) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取月日
     *
     * @param dateStr
     * @return
     */
    public static String getMonthDay(String dateStr) {
        return dateStr.length() > 9 ? dateStr.substring(5) : dateStr;
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param dateStartStr
     * @param dateEndStr
     * @return
     */
    public static int getDiscrepantDays(String dateStartStr, String dateEndStr) {
        Date dateStart = stringToDate(dateStartStr);
        Date dateEnd = stringToDate(dateEndStr);
        if (dateStart == null || dateEnd == null) {
            return 0;
        }
        return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
    }

    /**
     * 获取两位数
     *
     * @param mon
     * @return
     */
    public static String getMon(long mon) {
        String month = "";
        if (mon < 10) {
            month = "0" + mon;
        } else {
            month = "" + mon;
        }
        return month;
    }

    public static String getTimeStamp() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }
}
