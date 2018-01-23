package com.fyjr.baselibrary.utils;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by rongchengtianxia on 2016/6/5.
 */
public class PriceUtil {
    /**
     * 保留两位小数
     *
     * @param price
     * @return
     */
    public static String getTwo(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(price);
    }

    /**
     * 保留一位小数
     *
     * @param price
     * @return
     */
    public static String getOne(double price) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(price);
    }

    /**
     * 取整
     *
     * @param price
     * @return
     */
    public static String getInt(double price) {
        return String.valueOf(Math.round(price));
    }

    public static String getWan(double price) {
        if (price > 10000 || price == 10000) {
            return PriceUtil.getTwo(price / 10000) + "万";
        } else {
            return PriceUtil.getInt(price);
        }
    }

    /**
     * 获取*号加银行卡号后四位
     *
     * @param cardNum
     * @return
     */
    public static String getBankCardNum(String cardNum) {
        String num = "";
        try {
            if (cardNum.length() < 5) {
                num = cardNum;
            } else {
                num = cardNum.substring(cardNum.length() - 4);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format(Locale.CHINA, "**** **** **** %s", num);
    }
}
