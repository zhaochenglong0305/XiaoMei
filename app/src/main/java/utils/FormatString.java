package utils;

import bean.Information;

/**
 * Created by Adminis on 2018/1/25.
 */

public class FormatString {
    public static Information.SearchINFOBean formatInformation(String msg) {
        Information.SearchINFOBean searchINFOBean = new Information.SearchINFOBean();
        try {
            if (msg.contains("BG") && msg.contains("ND")) {
                if (msg.contains("XH") && msg.contains("PR")) {
                    searchINFOBean.setXH(msg.substring(msg.indexOf("XH") + 2,
                            msg.indexOf("PR")));
                }
                if (msg.contains("PR") && msg.contains("CT")) {
                    searchINFOBean.setPR(msg.substring(msg.indexOf("PR") + 2,
                            msg.indexOf("CT")));
                }
                if (msg.contains("CT") && msg.contains("CH")) {
                    searchINFOBean.setCT(msg.substring(msg.indexOf("CT") + 2,
                            msg.indexOf("CH")));
                }
                if (msg.contains("CH") && msg.contains("SF")) {
                    searchINFOBean.setCH(msg.substring(msg.indexOf("CH") + 2,
                            msg.indexOf("SF")));
                }
                if (msg.contains("SF") && msg.contains("MD")) {
                    searchINFOBean.setSF(msg.substring(msg.indexOf("SF") + 2,
                            msg.indexOf("MD")));
                }
                if (msg.contains("MD") && msg.contains("DT")) {
                    searchINFOBean.setMD(msg.substring(msg.indexOf("MD") + 2,
                            msg.indexOf("DT")));
                }
                if (msg.contains("DT") && msg.contains("NA")) {
                    searchINFOBean.setDT(msg.substring(msg.indexOf("DT") + 2,
                            msg.indexOf("NA")));
                }
                if (msg.contains("NA") && msg.contains("WH")) {
                    searchINFOBean.setNA(msg.substring(msg.indexOf("NA") + 2,
                            msg.indexOf("WH")));
                }
                if (msg.contains("WH") && msg.contains("PH")) {
                    searchINFOBean.setWH(msg.substring(msg.indexOf("WH") + 2,
                            msg.indexOf("PH")));
                }
                if (msg.contains("PH") && msg.contains("WD")) {
                    searchINFOBean.setPH(msg.substring(msg.indexOf("PH") + 2,
                            msg.indexOf("WD")));
                }
                if (msg.contains("WD") && msg.contains("RZ")) {
                    searchINFOBean.setWD(msg.substring(msg.indexOf("WD") + 2,
                            msg.indexOf("RZ")));
                }
                if (msg.contains("RZ") && msg.contains("LY")) {
                    searchINFOBean.setRZ(msg.substring(msg.indexOf("RZ") + 2,
                            msg.indexOf("LY")));
                }
                if (msg.contains("LY") && msg.contains("MS")) {
                    searchINFOBean.setLY(msg.substring(msg.indexOf("LY") + 2,
                            msg.indexOf("MS")));
                }
                try {
                    if (msg.contains("MS") && msg.contains("ND")) {
                        searchINFOBean.setMS(msg.substring(msg.indexOf("MS") + 2,
                                msg.indexOf("ND") - 1));
                    }
                } catch (Exception e) {
                }
            } else {
                searchINFOBean = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            searchINFOBean = null;
        }

        return searchINFOBean;
    }
}