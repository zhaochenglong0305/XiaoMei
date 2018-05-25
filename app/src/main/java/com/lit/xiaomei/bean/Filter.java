package com.lit.xiaomei.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminis on 2018/5/24.
 */

public class Filter implements Serializable{
    private static final long serialVersionUID = -7527249703259607461L;
    private List<String> filter1 = new ArrayList<>();
    private List<String> filter2 = new ArrayList<>();

    public List<String> getFilter1() {
        return filter1;
    }

    public void setFilter1(List<String> filter1) {
        this.filter1 = filter1;
    }

    public List<String> getFilter2() {
        return filter2;
    }

    public void setFilter2(List<String> filter2) {
        this.filter2 = filter2;
    }
}
