package bean;

import utils.DataBaseUtils.Table;

/**
 * Created by Adminis on 2018/1/26.
 */
@Table(name = "T_City")
public class City {
    /**
     * 城市名
     */
    @Table.Column(name = "CityName", type = Table.Column.TYPE_STRING, isNull = false)
    private String CityName;
    /**
     * 省ID
     */
    @Table.Column(name = "ProID", type = Table.Column.TYPE_STRING)
    private String ProID;
    /**
     * 城市ID
     */
    @Table.Column(name = "CitySort", type = Table.Column.TYPE_STRING, isPrimaryKey = true)
    private String CitySort;

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getProID() {
        return ProID;
    }

    public void setProID(String proID) {
        ProID = proID;
    }

    public String getCitySort() {
        return CitySort;
    }

    public void setCitySort(String citySort) {
        CitySort = citySort;
    }
}
