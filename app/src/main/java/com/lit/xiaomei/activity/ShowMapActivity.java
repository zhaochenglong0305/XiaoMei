package com.lit.xiaomei.activity;

import android.os.Bundle;
import android.app.Activity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.ActivityShowMapBinding;
import com.lit.xiaomei.manager.LocationManager;

import java.util.HashMap;

public class ShowMapActivity extends BaseActivity<ActivityShowMapBinding> {
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_show_map);
    }

    @Override
    public void initView() {
        super.initView();
        baiduMap = binding.map.getMap();
        baiduMap.setMyLocationEnabled(true);
        switch (getIntent().getIntExtra("mapType", 0)) {
            case 1:
                setTitle("附近加油站");
                break;
            case 2:
                setTitle("附近加气站");
                break;
            case 3:
                setTitle("附近停车场");
                break;
            case 4:
                setTitle("附近物流园");
                break;
            case 5:
                setTitle("附近餐馆");
                break;
            case 6:
                setTitle("附近酒店");
                break;
            case 7:
                setTitle("附近维修站");
                break;
        }
        setTitleTextColor("#ffffff");
        doLocation();
    }

    private void doLocation() {
        final LocationManager locationManager = new LocationManager(this);
        locationManager.doLocation(new LocationManager.OnLocationSuccessListener() {
            @Override
            public void onSuccess(HashMap<String, Object> result) {
                boolean isSuccess = false;
                isSuccess = (Boolean) result.get("isSuccess");
                if (isSuccess) {
                    double lat = 0.0;
                    double lon = 0.0;
                    if (result.get("nlatitude") != null && result.get("nlontitude") != null) {
                        lat = (Double) result.get("nlatitude");
                        lon = (Double) result.get("nlontitude");
                    }
                    MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
                    locationBuilder.latitude(lat);
                    locationBuilder.longitude(lon);
                    MyLocationData locationData = locationBuilder.build();
                    baiduMap.setMyLocationData(locationData);
                }
            }
        });
    }
}
