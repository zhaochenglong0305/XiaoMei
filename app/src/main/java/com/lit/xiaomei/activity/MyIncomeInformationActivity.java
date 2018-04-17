package com.lit.xiaomei.activity;

import android.os.Bundle;
import android.app.Activity;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.ActivityMyIncomeInformationBinding;

public class MyIncomeInformationActivity extends BaseActivity<ActivityMyIncomeInformationBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_my_income_information);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("收入详情");
        setTitleTextColor("#ffffff");
    }
}
