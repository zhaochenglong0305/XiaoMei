package com.lit.xiaomei.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.activity.MineActivity;
import com.lit.xiaomei.activity.MyIncomeInformationActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.activity.SteupActivity;
import com.lit.xiaomei.databinding.FragmentMineBinding;

import com.lit.xiaomei.manager.UseInfoManager;

/**
 * 我的Fragment
 */
public class MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener {

    private String uss = "", bds = "", eds = "", cps = "", ads = "", phs = "", prs = "", cts = "";

    public MineFragment() {

    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        space(binding.space);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        binding.rlGerenziliao.setOnClickListener(this);
        binding.tvIncomeAll.setOnClickListener(this);
        binding.rlSetup.setOnClickListener(this);
        binding.rlAboutUs.setOnClickListener(this);
        binding.llShareMa.setOnClickListener(this);
        binding.llInvitingFriends.setOnClickListener(this);
        binding.llCarLocation.setOnClickListener(this);
        binding.llMileageQuery.setOnClickListener(this);
        binding.llParkingLot.setOnClickListener(this);
        binding.llGasStation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_income_all:
                Intent intent = new Intent(getContext(), MyIncomeInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_gerenziliao:
                startActivity(new Intent(getContext(), MineActivity.class));
                break;
            case R.id.rl_setup:
                startActivity(new Intent(getContext(), SteupActivity.class));
                break;
        }
    }
}
