package com.lit.xiaomei.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminis on 2018/4/26.
 */

public class Line implements Serializable {

    private static final long serialVersionUID = -3945085619445684382L;
    private List<String> fromCities = new ArrayList<>();
    private List<String> toCities = new ArrayList<>();

    public Line() {

    }

    public Line(List<String> fromCities, List<String> toCities) {
        setFromCities(fromCities);
        setToCities(toCities);
    }

    public List<String> getFromCities() {
        return fromCities;
    }

    public void setFromCities(List<String> fromCities) {
        this.fromCities = fromCities;
    }

    public List<String> getToCities() {
        return toCities;
    }

    public void setToCities(List<String> toCities) {
        this.toCities = toCities;
    }


}
