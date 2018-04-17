package com.lit.xiaomei.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentServiceBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends BaseFragment<FragmentServiceBinding> {


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
}
