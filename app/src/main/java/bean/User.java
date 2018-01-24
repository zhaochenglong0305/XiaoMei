package bean;

import java.util.List;

/**
 * Created by Adminis on 2018/1/24.
 */

public class User {

    /**
     * ListData : [{"BG":"RIGH","US":"18741778588","KY":"00000000","PH":"18741778588 18741778588","PR":"辽宁","CT":"集装箱","CM":"","XS":"-258","RZ":"3","QX":"0","CK":"","JZ":"","KF":"0417-3332535","WX":"15941718740","TY":"1"}]
     * Code : 1
     */

    private String Code;
    private List<ListDataBean> ListData;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public List<ListDataBean> getListData() {
        return ListData;
    }

    public void setListData(List<ListDataBean> ListData) {
        this.ListData = ListData;
    }

    public static class ListDataBean {

        private String BG;
        private String US;
        private String PW;
        private String KY;
        private String PH;
        private String PR;
        private String CT;
        private String CM;
        private String XS;
        private String RZ;
        private String QX;
        private String CK;
        private String JZ;
        private String KF;
        private String WX;
        private String TY;

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

        public String getPW() {
            return PW;
        }

        public void setPW(String PW) {
            this.PW = PW;
        }

        public String getKY() {
            return KY;
        }

        public void setKY(String KY) {
            this.KY = KY;
        }

        public String getPH() {
            return PH;
        }

        public void setPH(String PH) {
            this.PH = PH;
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

        public String getCM() {
            return CM;
        }

        public void setCM(String CM) {
            this.CM = CM;
        }

        public String getXS() {
            return XS;
        }

        public void setXS(String XS) {
            this.XS = XS;
        }

        public String getRZ() {
            return RZ;
        }

        public void setRZ(String RZ) {
            this.RZ = RZ;
        }

        public String getQX() {
            return QX;
        }

        public void setQX(String QX) {
            this.QX = QX;
        }

        public String getCK() {
            return CK;
        }

        public void setCK(String CK) {
            this.CK = CK;
        }

        public String getJZ() {
            return JZ;
        }

        public void setJZ(String JZ) {
            this.JZ = JZ;
        }

        public String getKF() {
            return KF;
        }

        public void setKF(String KF) {
            this.KF = KF;
        }

        public String getWX() {
            return WX;
        }

        public void setWX(String WX) {
            this.WX = WX;
        }

        public String getTY() {
            return TY;
        }

        public void setTY(String TY) {
            this.TY = TY;
        }
    }
}
