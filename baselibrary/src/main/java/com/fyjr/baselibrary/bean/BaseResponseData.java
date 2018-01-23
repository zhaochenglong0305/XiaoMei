package com.fyjr.baselibrary.bean;

/**
 * Created by QNapex on 2016/12/4.
 * 返回数据基本格式
 */

public class BaseResponseData {

    /**
     * 成功
     */
    public static final String STATE_OK = "ok";
    /**
     * 失败
     */
    public static final String STATE_FAIL = "fail";

    private String state;
    private String msg;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseResponseData{" +
                "state='" + state + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
