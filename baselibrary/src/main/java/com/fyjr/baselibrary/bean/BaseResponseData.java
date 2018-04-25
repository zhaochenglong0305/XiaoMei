package com.fyjr.baselibrary.bean;

/**
 * Created by QNapex on 2016/12/4.
 * 返回数据基本格式
 */

public class BaseResponseData {

    /**
     * 成功
     */
    public static final String Code_OK = "1";
    /**
     * 失败
     */
    public static final String Code_FAIL = "0";

    private String Code;
    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
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
                "Code='" + Code + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
