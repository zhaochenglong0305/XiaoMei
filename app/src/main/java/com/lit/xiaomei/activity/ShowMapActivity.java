package com.lit.xiaomei.activity;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.ActivityShowMapBinding;
import com.lit.xiaomei.manager.LocationManager;

import java.util.HashMap;

public class ShowMapActivity extends BaseActivity<ActivityShowMapBinding> {
    private BaiduMap baiduMap;
    private LocationClient mLocationClient;
    private LatLng latLng;
    private boolean isFirstLoc = true; // 是否首次定位
    private BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_show_map);
    }

    @Override
    public void initView() {
        super.initView();
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
        baiduMap = binding.map.getMap();
        baiduMap.setMyLocationEnabled(true);
        //默认显示普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mLocationClient = new LocationClient(getApplicationContext());
        //配置定位SDK参数
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        //开启定位
        mLocationClient.start();
        //图片点击事件，回到定位点
        mLocationClient.requestLocation();
    }

    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            baiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            //mBaiduMap.setMyLocationEnabled(false);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(ShowMapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(ShowMapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(ShowMapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(ShowMapActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(ShowMapActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(ShowMapActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
        locationManager.mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.map.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.map.onPause();
    }

}
