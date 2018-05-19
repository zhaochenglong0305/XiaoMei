package com.lit.xiaomei.application;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareConfig;

/**
 * Created by Adminis on 2018/4/25.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        SDKInitializer.initialize(getApplicationContext());
        UMConfigure.init(this,"5aff915ab27b0a53f4000063","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setWeixin("wx1e1aa0e79162f697","f52e303463adf1f17a75d57b8783a6eb");
    }
}
