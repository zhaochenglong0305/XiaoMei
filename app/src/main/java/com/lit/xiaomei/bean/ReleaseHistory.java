package com.lit.xiaomei.bean;

/**
 * Created by Adminis on 2018/4/13.
 */

public class ReleaseHistory {
    private String releaseContext = "";
    private long time = 0;
    private int againTime = 0;
    private int againNum = 0;
    private String releaseType = "";
    private String from = "";
    private boolean isAgaining = false;


    public boolean isAgaining() {
        return isAgaining;
    }

    public void setAgaining(boolean againing) {
        isAgaining = againing;
    }

    public ReleaseHistory() {

    }

    public ReleaseHistory(String releaseContext, long time, int againTime, int againNum, String type,String from) {
        setReleaseContext(releaseContext);
        setTime(time);
        setAgainTime(againTime);
        setAgainNum(againNum);
        setReleaseType(type);
        setFrom(from);
    }

    public String getReleaseType() {
        return releaseType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getReleaseContext() {
        return releaseContext;
    }

    public void setReleaseContext(String releaseContext) {
        this.releaseContext = releaseContext;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAgainTime() {
        return againTime;
    }

    public void setAgainTime(int againTime) {
        this.againTime = againTime;
    }

    public int getAgainNum() {
        return againNum;
    }

    public void setAgainNum(int againNum) {
        this.againNum = againNum;
    }
}
