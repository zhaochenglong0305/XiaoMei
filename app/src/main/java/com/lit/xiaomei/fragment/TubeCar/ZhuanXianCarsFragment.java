package com.lit.xiaomei.fragment.TubeCar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentZhuanXianCarsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZhuanXianCarsFragment extends BaseFragment<FragmentZhuanXianCarsBinding> {


    public ZhuanXianCarsFragment() {
        // Required empty public constructor
    }

    public static ZhuanXianCarsFragment newInstance() {
        ZhuanXianCarsFragment fragment = new ZhuanXianCarsFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_zhuan_xian_cars;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
    }
}
