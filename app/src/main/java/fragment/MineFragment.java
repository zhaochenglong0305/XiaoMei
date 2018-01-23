package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentMineBinding;

public class MineFragment extends BaseFragment<FragmentMineBinding> {


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

}
