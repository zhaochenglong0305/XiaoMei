package com.lit.xiaomei.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/27.
 */

public class StringUtil {
    public static String formatString(String msg) {
        Pattern p = Pattern.compile("//s*|/t|/r|/n");
        Matcher m = p.matcher(msg);
        String after = m.replaceAll("");
        after = after.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’、？|-]", "");
        return after;
    }
}
