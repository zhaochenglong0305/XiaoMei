package com.lit.xiaomei.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/27.
 */

public class Drivers {

    /**
     * ListData : [{"NetID":"","Name":"","LicensePlate":"","Tel1":"","LocationCity":"2018-1-16 20:08:44"},{"NetID":"041704170","Name":"测试","LicensePlate":"辽A88888","Tel1":"222222","LocationCity":"2018-1-27 14:37:17"}]
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
        /**
         * NetID :
         * Name :
         * LicensePlate :
         * Tel1 :
         * LocationCity : 2018-1-16 20:08:44
         */

        private String NetID;
        private String Name;
        private String LicensePlate;
        private String Tel1;
        private String Tel2;
        private String LocationCity;

        public String getTel2() {
            return Tel2;
        }

        public void setTel2(String tel2) {
            Tel2 = tel2;
        }


        public String getNetID() {
            return NetID;
        }

        public void setNetID(String NetID) {
            this.NetID = NetID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getLicensePlate() {
            return LicensePlate;
        }

        public void setLicensePlate(String LicensePlate) {
            this.LicensePlate = LicensePlate;
        }

        public String getTel1() {
            return Tel1;
        }

        public void setTel1(String Tel1) {
            this.Tel1 = Tel1;
        }

        public String getLocationCity() {
            return LocationCity;
        }

        public void setLocationCity(String LocationCity) {
            this.LocationCity = LocationCity;
        }
    }
}
