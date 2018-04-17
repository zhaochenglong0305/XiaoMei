package com.lit.xiaomei.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CityPhones {

    /**
     * oprates : [{"ID":"2","departno":"02","DName":"沈阳","kefu":"024-31677009","weixin":"18640557055"},{"ID":"3","departno":"03","DName":"营口","kefu":"0417-3332770","weixin":"yk3332770"},{"ID":"4","departno":"04","DName":"鞍山","kefu":"0412-8880588","weixin":"as04128880588"},{"ID":"5","departno":"05","DName":"鲅鱼圈","kefu":"0417-3332535","weixin":"15941718740"},{"ID":"6","departno":"05","DName":"集装箱","kefu":"0417-3332535","weixin":"15941718740"},{"ID":"7","departno":"06","DName":"铁岭","kefu":"","weixin":""},{"ID":"8","departno":"08","DName":"集装箱","kefu":"0417-3332535","weixin":"15941718740"}]
     * nonestatus : 0
     * status : 1
     */

    private String nonestatus;
    private String status;
    private List<OpratesBean> oprates;

    public String getNonestatus() {
        return nonestatus;
    }

    public void setNonestatus(String nonestatus) {
        this.nonestatus = nonestatus;
    }

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
         * ID : 2
         * departno : 02
         * DName : 沈阳
         * kefu : 024-31677009
         * weixin : 18640557055
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
