package com.lit.xiaomei.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.adapter.SelectCityAdapter;
import com.lit.xiaomei.databinding.ActivityCommonLineBinding;
import com.lit.xiaomei.view.DialogReleaseSelectCity;

import java.util.ArrayList;
import java.util.List;

public class CommonLineActivity extends BaseActivity<ActivityCommonLineBinding> {
    private SelectCityAdapter fromAdapter;
    private SelectCityAdapter toAdapter;
    private List<String> fromSelect = new ArrayList<>();
    private List<String> toSelect = new ArrayList<>();
    private int type = 0;

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
        fromAdapter = new SelectCityAdapter(this, fromSelect, new OnFromCityClickListener());
        toAdapter = new SelectCityAdapter(this, toSelect, new OnToCityClickListener());
        binding.gvFromCity.setAdapter(fromAdapter);
        binding.gvToCity.setAdapter(toAdapter);
    }

    private class OnFromCityClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_city:
                    String delCity = (String) view.getTag();
                    if (fromSelect.contains(delCity)) {
                        fromSelect.remove(delCity);
                        fromAdapter.setData(fromSelect);
                    }
                    break;
                case R.id.rl_do:
                    if (fromSelect.size() >= 5) {
                        showMessage("最多添加5个城市");
                        return;
                    }
                    type = 1;
                    showCityDialog();
                    break;
            }


        }
    }

    private class OnToCityClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_city:
                    String delCity = (String) view.getTag();
                    if (toSelect.contains(delCity)) {
                        toSelect.remove(delCity);
                        toAdapter.setData(toSelect);
                    }
                    break;
                case R.id.rl_do:
                    if (toSelect.size() >= 5) {
                        showMessage("最多添加5个城市");
                        return;
                    }
                    type = 2;
                    showCityDialog();
                    break;
            }

        }
    }

    private void showCityDialog() {
        DialogReleaseSelectCity dialogReleaseSelectCity = new DialogReleaseSelectCity(this);
        dialogReleaseSelectCity.setSelectCityListener(new DialogReleaseSelectCity.OnCitySeectListener() {
            @Override
            public void onClick(String select) {
                switch (type) {
                    case 1:
                        fromSelect.add(select);
                        fromAdapter.setData(fromSelect);
                        break;
                    case 2:
                        toSelect.add(select);
                        toAdapter.setData(toSelect);
                        break;
                }
            }
        });
        dialogReleaseSelectCity.showAtLocation(binding.llCommonMain, Gravity.CENTER, 0, 0);
    }

}
