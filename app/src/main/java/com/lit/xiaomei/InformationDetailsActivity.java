package com.lit.xiaomei;

import android.os.Bundle;
import android.app.Activity;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.databinding.ActivityInformationDetailsBinding;

public class InformationDetailsActivity extends BaseActivity<ActivityInformationDetailsBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_information_details);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("详细信息");
    }
}
