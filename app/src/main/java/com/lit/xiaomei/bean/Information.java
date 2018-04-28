package com.lit.xiaomei.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Adminis on 2018/1/25.
 */

public class Information implements Serializable {


    private static final long serialVersionUID = -6728122286758903842L;
    /**
     * SearchINFO : [{"ID":"130","XH":"9253","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:41:35","NA":"鑫达东方","WH":"","PH":"02425182595 15640299456","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有28吨货要17.5米箱车"},{"ID":"129","XH":"9252","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:41:35","NA":"鑫达东方","WH":"","PH":"02425182595 15640299456","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有28吨货要17.5米箱车"},{"ID":"128","XH":"9054","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:36:33","NA":"畅通冷藏","WH":"","PH":"02429805287 02466689990 18602485061","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有20吨货要9.6米冷藏车"},{"ID":"127","XH":"8815","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:30:40","NA":"三姐配货站","WH":"","PH":"02485813040 18604043531 13889122659","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有大飘货要17.5米车"},{"ID":"126","XH":"8711","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:28:22","NA":"安馨","WH":"","PH":"02425363622 02425362219 25375300","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要5.2米箱车"},{"ID":"125","XH":"8696","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:28:11","NA":"安馨","WH":"","PH":"02425363622 02425362219 25375300","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要4.5米箱车"},{"ID":"124","XH":"8694","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:28:11","NA":"安馨","WH":"","PH":"02425363622 02425362219 25375300","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要4.5米箱车"},{"ID":"123","XH":"8434","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:22:02","NA":"二付货运","WH":"","PH":"02485902161 15640247051 13840586389","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有2吨货要4.2米箱车(蔬菜)"},{"ID":"122","XH":"8287","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:18:52","NA":"鑫达东方","WH":"","PH":"02425182595 15640299456","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有28吨货要19米箱车"},{"ID":"121","XH":"8122","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:15:07","NA":"鑫达东方","WH":"","PH":"02425182595 15640299456","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有28吨货要18米箱车"},{"ID":"120","XH":"8118","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:15:01","NA":"鑫达东方","WH":"","PH":"02425182595 15640299456","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有28吨货要16米箱车"},{"ID":"119","XH":"8012","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:13:09","NA":"恒通海霞","WH":"","PH":"02425362545 02425374656 13889302927","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有9吨货要6.8米车"},{"ID":"118","XH":"7911","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:10:30","NA":"南塔","WH":"","PH":"02423803002 02423803016 23803017","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要12.5米车"},{"ID":"117","XH":"7807","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:07:25","NA":"成圣","WH":"","PH":"02425360328 02424224998","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要6.8米高栏车"},{"ID":"116","XH":"7660","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:03:50","NA":"明辉货运","WH":"","PH":"02425814660 15640305759 67885233","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要6.8米车(包车)"},{"ID":"115","XH":"7659","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:03:50","NA":"明辉货运","WH":"","PH":"02425814660 15640305759 67885233","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要6.8米车(包车)"},{"ID":"114","XH":"7639","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:03:25","NA":"明辉货运","WH":"","PH":"02425814660 15640305759 67885233","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要5.8米车(包车)"},{"ID":"113","XH":"7616","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:02:33","NA":"方舟","WH":"","PH":"02425316996 02425325972 13889802629","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要17.5米板车"},{"ID":"112","XH":"7615","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:02:33","NA":"方舟","WH":"","PH":"02425316996 02425325972 13889802629","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有货要17.5米板车"},{"ID":"111","XH":"7596","PR":"辽宁","CT":"沈阳","CH":"货源","SF":"沈阳","MD":"北京","DT":"2018-4-28 10:02:18","NA":"鑫达东方","WH":"","PH":"02425182595 15640299456","WD":"","RZ":"1","LY":"lnsj165","MS":"北京有32吨货要13米高栏车"}]
     * status : 1
     * Code : 1
     * Msg : 成功
     */

    private String status;
    private String Code;
    private String Msg;
    private List<SearchINFOBean> SearchINFO;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<SearchINFOBean> getSearchINFO() {
        return SearchINFO;
    }

    public void setSearchINFO(List<SearchINFOBean> SearchINFO) {
        this.SearchINFO = SearchINFO;
    }

    public static class SearchINFOBean implements Serializable {
        private static final long serialVersionUID = -2992405241533500068L;
        /**
         * ID : 130
         * XH : 9253
         * PR : 辽宁
         * CT : 沈阳
         * CH : 货源
         * SF : 沈阳
         * MD : 北京
         * DT : 2018-4-28 10:41:35
         * NA : 鑫达东方
         * WH :
         * PH : 02425182595 15640299456
         * WD :
         * RZ : 1
         * LY : lnsj165
         * MS : 北京有28吨货要17.5米箱车
         */

        private String ID;
        private String XH;
        private String PR;
        private String CT;
        private String CH;
        private String SF;
        private String MD;
        private String DT;
        private String NA;
        private String WH;
        private String PH;
        private String WD;
        private String RZ;
        private String LY;
        private String MS;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getXH() {
            return XH;
        }

        public void setXH(String XH) {
            this.XH = XH;
        }

        public String getPR() {
            return PR;
        }

        public void setPR(String PR) {
            this.PR = PR;
        }

        public String getCT() {
            return CT;
        }

        public void setCT(String CT) {
            this.CT = CT;
        }

        public String getCH() {
            return CH;
        }

        public void setCH(String CH) {
            this.CH = CH;
        }

        public String getSF() {
            return SF;
        }

        public void setSF(String SF) {
            this.SF = SF;
        }

        public String getMD() {
            return MD;
        }

        public void setMD(String MD) {
            this.MD = MD;
        }

        public String getDT() {
            return DT;
        }

        public void setDT(String DT) {
            this.DT = DT;
        }

        public String getNA() {
            return NA;
        }

        public void setNA(String NA) {
            this.NA = NA;
        }

        public String getWH() {
            return WH;
        }

        public void setWH(String WH) {
            this.WH = WH;
        }

        public String getPH() {
            return PH;
        }

        public void setPH(String PH) {
            this.PH = PH;
        }

        public String getWD() {
            return WD;
        }

        public void setWD(String WD) {
            this.WD = WD;
        }

        public String getRZ() {
            return RZ;
        }

        public void setRZ(String RZ) {
            this.RZ = RZ;
        }

        public String getLY() {
            return LY;
        }

        public void setLY(String LY) {
            this.LY = LY;
        }

        public String getMS() {
            return MS;
        }

        public void setMS(String MS) {
            this.MS = MS;
        }
    }
}
