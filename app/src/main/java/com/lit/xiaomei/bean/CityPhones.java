package com.lit.xiaomei.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CityPhones {

    /**
     * oprates : [{"ID":"0","departno":"","DName":"总台","kefu":"024-31677009","weixin":""}]
     * status : 0
     */

    private String status;
    private List<OpratesBean> oprates;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OpratesBean> getOprates() {
        return oprates;
    }

    public void setOprates(List<OpratesBean> oprates) {
        this.oprates = oprates;
    }

    public static class OpratesBean {
        /**
         * ID : 0
         * departno :
         * DName : 总台
         * kefu : 024-31677009
         * weixin :
         */

        private String ID;
        private String departno;
        private String DName;
        private String kefu;
        private String weixin;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getDepartno() {
            return departno;
        }

        public void setDepartno(String departno) {
            this.departno = departno;
        }

        public String getDName() {
            return DName;
        }

        public void setDName(String DName) {
            this.DName = DName;
        }

        public String getKefu() {
            return kefu;
        }

        public void setKefu(String kefu) {
            this.kefu = kefu;
        }

        public String getWeixin() {
            return weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }
    }
}
