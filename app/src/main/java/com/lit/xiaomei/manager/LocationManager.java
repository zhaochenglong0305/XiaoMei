package com.lit.xiaomei.manager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;

/**
 * Created by QNapex on 2017/11/8. 定位信息管理
 */

public class LocationManager implements BDLocationListener {
    public Context context;
    public LocationClient mLocationClient = null; // LocationClient类是定位SDK的核心类
    private OnLocationSuccessListener listener;

    public LocationManager(Context context) {
        this.context = context;
    }

    public void doLocation(OnLocationSuccessListener listener) {
        this.listener = listener;
        // 声明LocationClient类
        mLocationClient = new LocationClient(context.getApplicationContext());
        // 注册监听函数
        mLocationClient.registerLocationListener(this);
        // 初始化定位
        initLocation();
        mLocationClient.start();
    }


    public interface OnLocationSuccessListener {
        void onSuccess(HashMap<String, Object> result);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        // 可选，默认gcj02，设置返回的定位结果坐标系

        int span = 0;
        option.setScanSpan(span);
        // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        // 可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        // 可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        // 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        // 可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        // 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        // 获取定位结果
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("time", location.getTime());// 获取定位时间
        hm.put("nerror code", location.getLocType());// 获取类型类型
        hm.put("nlatitude", location.getLatitude());// 获取纬度信息
        hm.put("nlontitude", location.getLongitude());// 获取经度信息
        hm.put("nradius", location.getRadius());// 获取定位精准度

        if (location.getLocType() == BDLocation.TypeGpsLocation) {

            // GPS定位结果
            hm.put("isSuccess", true);// 定位成功
            hm.put("nspeed", location.getSpeed());// 单位：公里每小时
            hm.put("nsatellite", location.getSatelliteNumber());// 获取卫星数
            hm.put("nheight", location.getAltitude());// 获取海拔高度信息，单位米
            hm.put("ndirection", location.getDirection());// 获取方向信息，单位度
            hm.put("naddr", location.getAddrStr());// 获取地址信息
            hm.put("province", location.getProvince());// 获取省
            hm.put("city", location.getCity());// 获取市
            hm.put("ndescribe", "gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

            // 网络定位结果
            hm.put("isSuccess", true);// 定位成功
            hm.put("naddr", location.getAddrStr());// 获取地址信息
            hm.put("noperationers", location.getOperators());// 获取运营商信息
            hm.put("province", location.getProvince());// 获取省
            hm.put("city", location.getCity());// 获取市
            hm.put("ndescribe", "网络定位成功");

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            hm.put("isSuccess", true);// 定位成功
            hm.put("naddr", location.getAddrStr());// 获取地址信息
            hm.put("province", location.getProvince());// 获取省
            hm.put("city", location.getCity());// 获取市
            hm.put("ndescribe", "离线定位成功，离线定位结果也是有效的");

        } else if (location.getLocType() == BDLocation.TypeServerError) {
            hm.put("isSuccess", false);// 定位失败
            hm.put("ndescribe", "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            hm.put("isSuccess", false);// 定位失败
            hm.put("ndescribe", "网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            hm.put("isSuccess", false);// 定位失败
            hm.put("ndescribe", "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        hm.put("nlocationdescribe", location.getLocationDescribe());// 位置语义化信息
        Log.i("long", hm.toString());
        listener.onSuccess(hm);
        mLocationClient.stop();
    }


}
