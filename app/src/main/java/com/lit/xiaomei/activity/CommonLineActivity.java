package com.lit.xiaomei.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.adapter.SelectCityAdapter;
import com.lit.xiaomei.databinding.ActivityCommonLineBinding;

import java.util.ArrayList;
import java.util.List;

public class CommonLineActivity extends BaseActivity<ActivityCommonLineBinding> {
    private SelectCityAdapter fromAdapter;
    private SelectCityAdapter toAdapter;
    private List<String> fromSelect = new ArrayList<>();
    private List<String> toSelect = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_common_line);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("常用线路");
        setTitleTextColor("#ffffff");
        fromSelect.add("北京");
        fromAdapter = new SelectCityAdapter(this, fromSelect);
        toAdapter = new SelectCityAdapter(this, toSelect);
        binding.gvFromCity.setAdapter(fromAdapter);
        binding.gvToCity.setAdapter(toAdapter);
    }

}
