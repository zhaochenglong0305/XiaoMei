package com.lit.xiaomei.bean;

/**
 * Created by Administrator on 2018/5/12.
 */

public class Version {

    /**
     * FileName : app-release.apk
     * apkURL : http://113.6.252.165:8081/sjfwq/apk/sjphw.apk
     * versionCode : 1
     * status : 1
     */

    private String FileName;
    private String apkURL;
    private String versionCode;
    private String status;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getApkURL() {
        return apkURL;
    }

    public void setApkURL(String apkURL) {
        this.apkURL = apkURL;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
