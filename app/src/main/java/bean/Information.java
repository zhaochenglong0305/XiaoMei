package bean;

import java.util.List;

/**
 * Created by Adminis on 2018/1/25.
 */

public class Information {


    private String status;
    private List<SearchINFOBean> SearchINFO;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SearchINFOBean> getSearchINFO() {
        return SearchINFO;
    }

    public void setSearchINFO(List<SearchINFOBean> SearchINFO) {
        this.SearchINFO = SearchINFO;
    }

    public static class SearchINFOBean {
        /**
         * XH : 19754
         * PR : 山西
         * CT : 太原
         * CH : 货源
         * SF : 太原
         * MD : 鞍山
         * DT : 2018-1-25 15:29:36
         * NA :
         * WH :
         * PH : 2451233 13133041111
         * WD :
         * RZ : 0
         * LY : 青岛
         * MS : 太原清徐-.马鞍山26-150吨重货求13-17.5米车.有车速联系.高栏车.底栏车.平板车.车型不限可配货~S
         */

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
