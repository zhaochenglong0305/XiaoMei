package com.lit.xiaomei.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.TabEntity;
import com.lit.xiaomei.databinding.FragmentGoodsBinding;
import com.lit.xiaomei.fragment.goods.InformationFragment;
import com.lit.xiaomei.fragment.goods.PeripheryFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsFragment extends BaseFragment<FragmentGoodsBinding> implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private String[] mTitles = {"货源", "周边沿线"};
    private ArrayList<CustomTabEntity> mTabEntities;
    private ArrayList<Fragment> mFragments;

    public GoodsFragment() {
        // Required empty public constructor
    }

    public static GoodsFragment newInstance() {
        GoodsFragment fragment = new GoodsFragment();
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
        return R.layout.fragment_goods;
    }

    @Override
    public void initView() {
        super.initView();
        mFragments = new ArrayList<>();
        mFragments.add(InformationFragment.newInstance());
        mFragments.add(PeripheryFragment.newInstance());
        mTabEntities = new ArrayList<>();
        for (String mTitle : mTitles) {
            mTabEntities.add(new TabEntity(mTitle));
        }
        binding.tvTitleLeft.setOnClickListener(this);
        binding.tvTitleRight.setOnClickListener(this);
        binding.viewPager.setAdapter(new GoodsFragmentAdapter(getChildFragmentManager()));
        binding.viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switchTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class GoodsFragmentAdapter extends FragmentPagerAdapter {

        public GoodsFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_left:
                switchTitle(0);
                break;
            case R.id.tv_title_right:
                switchTitle(1);
                break;
        }
    }
    private void switchTitle(int type) {
        switch (type) {
            case 0:
                binding.tvTitleLeft.setTextColor(getResources().getColor(R.color.cFD933C));
                binding.tvTitleLeft.setBackgroundResource(R.drawable.fillet_release_title_left_select);
                binding.tvTitleRight.setTextColor(Color.WHITE);
                binding.tvTitleRight.setBackgroundResource(R.drawable.fillet_release_title_right_normal);
                break;
            case 1:
                binding.tvTitleLeft.setTextColor(Color.WHITE);
                binding.tvTitleLeft.setBackgroundResource(R.drawable.fillet_release_title_left_normal);
                binding.tvTitleRight.setTextColor(getResources().getColor(R.color.cFD933C));
                binding.tvTitleRight.setBackgroundResource(R.drawable.fillet_release_title_right_select);
                break;
        }
        binding.viewPager.setCurrentItem(type);
    }
}
