package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.MyIncomeInformationActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentMineBinding;

import manager.UseInfoManager;

/**
 * 我的Fragment
 */
public class MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener {


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
        binding.llAllIncome.setOnClickListener(this);
        binding.cbAutomaticLogon.setChecked(UseInfoManager.getBoolean(getContext(), "AutomaticLogon", false));
        binding.cbAutomaticLogon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                UseInfoManager.putBoolean(getContext(), "AutomaticLogon", isChecked);
            }
        });

        binding.cbShowPhone.setChecked(UseInfoManager.getBoolean(getContext(), "ShowPhone", false));
        binding.cbShowPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                UseInfoManager.putBoolean(getContext(), "ShowPhone", isChecked);
            }
        });
        binding.cbBigText.setChecked(UseInfoManager.getBoolean(getContext(), "BigText", false));
        binding.cbBigText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                UseInfoManager.putBoolean(getContext(), "BigText", isChecked);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_all_income:
                Intent intent = new Intent(getContext(), MyIncomeInformationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
