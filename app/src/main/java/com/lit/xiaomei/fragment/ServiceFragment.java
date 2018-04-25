package com.lit.xiaomei.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.activity.ShowMapActivity;
import com.lit.xiaomei.databinding.FragmentServiceBinding;
import com.lit.xiaomei.manager.LocationManager;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends BaseFragment<FragmentServiceBinding> implements View.OnClickListener {
    private List<Integer> imgs = new ArrayList<>();
    private PoiSearch poiSearch = null;

    public ServiceFragment() {
        // Required empty public constructor
    }

    public static ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        space(binding.space);
        return binding.getRoot();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    public void initView() {
        super.initView();
        initData();
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new onGetPoiListener());
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        binding.banner.setIndicatorGravity(BannerConfig.CENTER);
        binding.banner.setDelayTime(4000);
        binding.banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                imageView.setImageResource((Integer) path);
            }
        });
        binding.banner.setImages(imgs);
        binding.banner.start();
        binding.llJiayou.setOnClickListener(this);

    }

    private void initData() {
        imgs.add(R.mipmap.banner1);
        imgs.add(R.mipmap.banner2);
        imgs.add(R.mipmap.banner3);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), ShowMapActivity.class);
        switch (v.getId()) {
            case R.id.ll_jiayou:
//                doPoiSearch("沈阳","加油站");
                intent.putExtra("mapType", 1);
                break;
        }
        startActivity(intent);
    }

    private void doPoiSearch(String city, String keyword) {
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
        citySearchOption.city(city);// 城市
        citySearchOption.keyword(keyword);// 关键字
        citySearchOption.pageCapacity(10);// 默认每页10条
        citySearchOption.pageNum(1);// 分页编号
        poiSearch.searchInCity(citySearchOption);
    }

    private class onGetPoiListener implements OnGetPoiSearchResultListener {

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            poiResult.getAllPoi();
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    }
}
