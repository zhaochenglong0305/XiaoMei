package com.lit.xiaomei.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CheckAuthority {


    /**
     * INFOPhone : [{"BG":"SPNE","US":"网号","PH":"电话","NA":"货站名","SH":"1"}]
     * statusId : 1
     * status : 允许查看电话
     */

    private String statusId;
    private String status;
    private List<INFOPhoneBean> INFOPhone;
    private String Code;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<INFOPhoneBean> getINFOPhone() {
        return INFOPhone;
    }

    public void setINFOPhone(List<INFOPhoneBean> INFOPhone) {
        this.INFOPhone = INFOPhone;
    }

    public static class INFOPhoneBean {
        /**
         * BG : SPNE
         * US : 网号
         * PH : 电话
         * NA : 货站名
         * SH : 1
         */

        private String BG;
        private String US;
        private String PH;
        private String NA;
        private String SH;

        public String getBG() {
            return BG;
        }

        public void setBG(String BG) {
            this.BG = BG;
        }

        public String getUS() {
            return US;
        }

        public void setUS(String US) {
            this.US = US;
        }

        public String getPH() {
            return PH;
        }

        public void setPH(String PH) {
            this.PH = PH;
        }

        public String getNA() {
            return NA;
        }

        public void setNA(String NA) {
            this.NA = NA;
        }

        public String getSH() {
            return SH;
        }

        public void setSH(String SH) {
            this.SH = SH;
        }
    }
}
