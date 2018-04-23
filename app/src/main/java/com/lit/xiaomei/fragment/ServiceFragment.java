package com.lit.xiaomei.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentServiceBinding;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends BaseFragment<FragmentServiceBinding> {
    private List<Integer> imgs = new ArrayList<>();


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

    }

    private void initData() {
        imgs.add(R.mipmap.banner1);
        imgs.add(R.mipmap.banner2);
        imgs.add(R.mipmap.banner3);
    }
}
