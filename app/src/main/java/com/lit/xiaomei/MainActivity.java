package com.lit.xiaomei;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import fragment.InformationFragment;
import fragment.MineFragment;
import fragment.ReleaseFragment;
import fragment.TubeCarFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements RadioGroup.OnCheckedChangeListener {
    private List<Fragment> fragments;
    private TubeCarFragment tubeCarFragment;
    private ReleaseFragment releaseFragment;
    private InformationFragment informationFragment;
    private MineFragment mineFragment;
    private int currentIndex = 0;
    private int oldIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_main);
    }

    @Override
    public void initView() {
        super.initView();
        initFragments();
        binding.bottomBarRg.setOnCheckedChangeListener(this);
    }

    private void initFragments() {
        tubeCarFragment = TubeCarFragment.newInstance();
        releaseFragment = ReleaseFragment.newInstance();
        informationFragment = InformationFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        fragments = new ArrayList<>();
        fragments.add(tubeCarFragment);
        fragments.add(releaseFragment);
        fragments.add(informationFragment);
        fragments.add(mineFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, informationFragment)
                .add(R.id.frame_layout, tubeCarFragment)
                .show(tubeCarFragment)
                .commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_live:
                currentIndex = 0;
                break;
            case R.id.rb_find:
                currentIndex = 1;
                break;
            case R.id.rb_message:
                currentIndex = 2;
                break;
            case R.id.rb_mine:
                currentIndex = 3;
                break;
        }
        showCurrentFragment(currentIndex);
    }

    /**
     * 展示当前选中的Fragment
     *
     * @param currentIndex
     */
    private void showCurrentFragment(int currentIndex) {
        if (currentIndex != oldIndex) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.hide(fragments.get(oldIndex));
            if (!fragments.get(currentIndex).isAdded()) {
                ft.add(R.id.frame_layout, fragments.get(currentIndex));
            }
            ft.show(fragments.get(currentIndex)).commit();
            oldIndex = currentIndex;
        }
    }
}
