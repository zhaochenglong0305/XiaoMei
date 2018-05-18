package com.lit.xiaomei.bean;

/**
 * Created by Adminis on 2018/1/25.
 */

public class GlobalVariable {
    public static class ReceiverAction {
        public static final String REAL_TIME_MSG = "com.lit.xiaomei.REAL_TIME_MSG";
        public static final String RELEASE_RESULT = "com.lit.xiaomei.RELEASE_RESULT";
        public static final String UPDATE_HISTORY = "com.lit.xiaomei.UPDATE_HISTORY";
        public static final String CHANGE_FRAGMENT = "com.lit.xiaomei.CHANGE_FRAGMENT";
        public static final String LINE_MSG = "com.lit.xiaomei.LINE_MSG";
        public static final String GET_LINE_MSG = "com.lit.xiaomei.GET_LINE_MSG";
        public static final String UPDATE_INFORMATION = "com.lit.xiaomei.UPDATE_INFORMATION";
        public static final String RECEIVER_LINE_MSG = "com.lit.xiaomei.RECEIVER_LINE_MSG";
    }

    public static class DateBaseMsg{
        public static final String DB_PATH = "/data/data/com.lit.xiaomei/databases/city.db";
        public static final String DB_NAME = "city.db";
    }
}
