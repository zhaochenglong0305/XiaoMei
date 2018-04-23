package com.lit.xiaomei.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.activity.InvitationFriendActivity;
import com.lit.xiaomei.activity.MineActivity;
import com.lit.xiaomei.activity.MyIncomeInformationActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.activity.SteupActivity;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.databinding.FragmentMineBinding;

import com.lit.xiaomei.manager.UseInfoManager;

/**
 * 我的Fragment
 */
public class MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener {
    private User.ListDataBean user = new User.ListDataBean();
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
        user = UseInfoManager.getUser(getContext()).getListData().get(0);
        binding.tvUsername.setText(user.getUS());
        binding.rlGerenziliao.setOnClickListener(this);
        binding.tvIncomeAll.setOnClickListener(this);
        binding.rlSetup.setOnClickListener(this);
        binding.rlAboutUs.setOnClickListener(this);
        binding.rlInvitationFriend.setOnClickListener(this);
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
            case R.id.rl_invitation_friend:
                startActivity(new Intent(getContext(), InvitationFriendActivity.class));
                break;
        }
    }
}
