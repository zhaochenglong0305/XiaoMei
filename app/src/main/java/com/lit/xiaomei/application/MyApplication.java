package com.lit.xiaomei.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Adminis on 2018/4/25.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
