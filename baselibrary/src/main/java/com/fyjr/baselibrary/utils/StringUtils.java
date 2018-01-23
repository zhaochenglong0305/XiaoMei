package com.fyjr.baselibrary.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StringUtils {

    /**
     * 生成唯一号
     *
     * @return
     */
    public static String get36UUID() {
        UUID uuid = UUID.randomUUID();
        String uniqueId = uuid.toString();
        return uniqueId;
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    /**
     * string转为Utf8
     *
     * @param str
     * @return
     */
    public static String toUtf8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 简单判断是否json
     *
     * @param str
     * @return
     */
    public static boolean isJson(String str) {

        if (str.trim().startsWith("{") && str.trim().endsWith("}")) {
            return true;
        }

        return false;

    }

    /**
     * 按照固定長度截取字符串
     *
     * @param string 原字符串
     * @param lenght 規定長度
     * @return
     */
    public static String subStringAsLenght(String string, int lenght) {
        String str = "";
        if (string.length() > lenght) {
            str = string;
        } else {
            str = string.substring(0, lenght) + "...";
        }

        return str;
    }


    /**
     * 時間戳格式化成 yyyy-MM-dd
     *
     * @param milliseconds
     * @return
     */
    public static String strToDate(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(Long.valueOf(milliseconds)));

    }

    /**
     * 時間戳格式化成 MM-dd
     *
     * @param milliseconds
     * @return
     */
    public static String strTodate(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
        return formatter.format(new Date(Long.valueOf(milliseconds + "000")));

    }

    /**
     * 時間戳格式化成 yyyy-MM-dd HH:mm
     *
     * @param milliseconds
     * @return
     */
    public static String strToTime(String milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(new Date(Long.valueOf(milliseconds) * 1000));

    }

    /**
     * yyyy年MM月dd日 HH:mm转换时间戳
     *
     * @param time
     * @return
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * yyyy-MM-dd HH:mm转换时间戳
     *
     * @param time
     * @return
     */
    public static long getStrToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * yyyy-MM-dd HH:mm转换时间戳
     *
     * @param time
     * @return
     */
    public static long getStrTodate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * check number length
     *
     * @param num
     * @param min
     * @param max
     * @return
     */
    public static boolean checkNumLength(String num, int min, int max) {

        if (null == num || num.equals(""))
            return false;
        if (num.length() < min || num.length() > max)
            return false;
        return true;
    }


    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 完整的判断中文汉字和符号
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证手机号
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3578]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        boolean isNum = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c > 58 || c < 47) {
                isNum = false;
                break;
            }
        }
        return isNum;
    }

    /**
     * 解码base64
     */
    private final static String CODE_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private final static int ORGINAL_LEN = 8;
    private final static int NEW_LEN = 6;

    public static String decodeBase64(String encodeStr) throws Exception {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < encodeStr.length(); i++) {

            char c = encodeStr.charAt(i);       //把"1tC5sg=="字符串一个个分拆  
            int k = CODE_STR.indexOf(c);        //分拆后的字符在CODE_STR中的位置,从0开始,如果是'=',返回-1  
            if (k != -1) {                        //如果该字符不是'='
                String tmpStr = Integer.toBinaryString(k);
                int n = 0;
                while (tmpStr.length() + n < NEW_LEN) {
                    n++;
                    sb.append("0");
                }
                sb.append(tmpStr);
            }
        }

        /**
         * 8个字节分拆一次，得到总的字符数 
         * 余数是加密的时候补的，舍去 
         */
        int newByteLen = sb.length() / ORGINAL_LEN;

        /**
         * 二进制转成字节数组 
         */
        byte[] b = new byte[newByteLen];
        for (int j = 0; j < newByteLen; j++) {
            b[j] = (byte) Integer.parseInt(sb.substring(j * ORGINAL_LEN, (j + 1) * ORGINAL_LEN), 2);
        }

        /**
         * 字节数组还原成String 
         */
        return new String(b, "gb2312");
    }
}
