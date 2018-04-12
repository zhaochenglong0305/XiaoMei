package bean;

import utils.DataBaseUtils.Table;

/**
 * Created by Adminis on 2018/1/26.
 */
@Table(name = "T_Zone")
public class Zone {
    public Zone(){

    }
    public Zone(String zoneName){
        this.setZoneName(zoneName);
    }
    /**
     * 区/街道ID
     */
    @Table.Column(name = "ZoneID", type = Table.Column.TYPE_STRING, isPrimaryKey = true, isNull = false)
    private String ZoneID;
    /**
     * 区/街道名称
     */
    @Table.Column(name = "ZoneName", type = Table.Column.TYPE_STRING)
    private String ZoneName;
    /**
     * 城市ID
     */
    @Table.Column(name = "CityID", type = Table.Column.TYPE_STRING)
    private String CityID;

    public String getZoneID() {
        return ZoneID;
    }

    public void setZoneID(String zoneID) {
        ZoneID = zoneID;
    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String zoneName) {
        ZoneName = zoneName;
    }

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }
}
