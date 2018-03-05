package fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentReleaseBinding;

import java.util.ArrayList;

import bean.TabEntity;
import fragment.release.ReleaseHistoryFragment;
import fragment.release.ReleaseInformationFragment;

/**
 * 发布Fragment
 */
public class ReleaseFragment extends BaseFragment<FragmentReleaseBinding> implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private String[] mTitles = {"发布", "历史"};
    private ArrayList<CustomTabEntity> mTabEntities;
    private ArrayList<Fragment> mFragments;
    private boolean isTab = false;

    public ReleaseFragment() {
    }

    public static ReleaseFragment newInstance() {
        ReleaseFragment fragment = new ReleaseFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_release;
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
        mFragments = new ArrayList<>();
        mFragments.add(ReleaseInformationFragment.newInstance());
        mFragments.add(ReleaseHistoryFragment.newInstance());
        mTabEntities = new ArrayList<>();
        for (String mTitle : mTitles) {
            mTabEntities.add(new TabEntity(mTitle));
        }
        binding.tvTitleLeft.setOnClickListener(this);
        binding.viewPager.setAdapter(new ReleaseFragmentAdapter(getActivity().getSupportFragmentManager()));
        binding.viewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动切换页面
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        switchTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    /**
     * 发布信息页Adapter
     */
    private class ReleaseFragmentAdapter extends FragmentPagerAdapter {
        public ReleaseFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
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
