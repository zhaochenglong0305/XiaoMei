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
    private List<String> carLong = new ArrayList<>();
    private List<String> carType = new ArrayList<>();

    public Line() {

    }

    public Line(List<String> fromCities, List<String> toCities,List<String> carLong,List<String> carType) {
        setFromCities(fromCities);
        setToCities(toCities);
        setCarLong(carLong);
        setCarType(carType);
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

    public List<String> getCarLong() {
        return carLong;
    }

    public void setCarLong(List<String> carLong) {
        this.carLong = carLong;
    }

    public List<String> getCarType() {
        return carType;
    }

    public void setCarType(List<String> carType) {
        this.carType = carType;
    }
}
