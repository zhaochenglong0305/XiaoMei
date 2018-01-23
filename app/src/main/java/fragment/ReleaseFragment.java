package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentReleaseBinding;

public class ReleaseFragment extends BaseFragment<FragmentReleaseBinding> {


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

}
