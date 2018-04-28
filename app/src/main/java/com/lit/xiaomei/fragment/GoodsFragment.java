package com.lit.xiaomei.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.activity.CommonLineActivity;
import com.lit.xiaomei.bean.GlobalVariable;
import com.lit.xiaomei.bean.TabEntity;
import com.lit.xiaomei.databinding.FragmentGoodsBinding;
import com.lit.xiaomei.fragment.goods.InformationFragment;
import com.lit.xiaomei.fragment.goods.PeripheryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsFragment extends BaseFragment<FragmentGoodsBinding> implements View.OnClickListener {
    //    private ArrayList<CustomTabEntity> mTabEntities;
    private List<Fragment> fragments;
    private InformationFragment informationFragment;
    private PeripheryFragment peripheryFragment;
    private int currentIndex = 0;
    private int oldIndex = 0;
    private ChangeFragmentReceiver changeFragmentReceiver;

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
        return binding.getRoot();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    public void initView() {
        super.initView();
        changeFragmentReceiver = new ChangeFragmentReceiver();
        getContext().registerReceiver(changeFragmentReceiver, new IntentFilter(GlobalVariable.ReceiverAction.CHANGE_FRAGMENT));
        initFragment();
    }

    private void initFragment() {
        informationFragment = InformationFragment.newInstance();
        peripheryFragment = PeripheryFragment.newInstance();
        fragments = new ArrayList<>();
        fragments.add(informationFragment);
        fragments.add(peripheryFragment);
        getFragmentManager().beginTransaction()
                .add(R.id.frame_layout, peripheryFragment)
                .add(R.id.frame_layout, informationFragment)
                .show(informationFragment)
                .commit();
    }


    @Override
    public void onClick(View view) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(changeFragmentReceiver);
    }

    /**
     * 展示当前选中的Fragment
     *
     * @param currentIndex
     */
    public void showCurrentFragment(int currentIndex) {
        if (currentIndex != oldIndex) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(fragments.get(oldIndex));
            if (!fragments.get(currentIndex).isAdded()) {
                ft.add(R.id.frame_layout, fragments.get(currentIndex));
            }
            ft.show(fragments.get(currentIndex)).commit();
            oldIndex = currentIndex;
        }
    }

    private class ChangeFragmentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("fragmentType", 0);
            showCurrentFragment(type);
        }
    }
}
