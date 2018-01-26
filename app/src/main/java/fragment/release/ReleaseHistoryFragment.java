package fragment.release;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentReleaseHistoryBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReleaseHistoryFragment extends BaseFragment<FragmentReleaseHistoryBinding> {


    public ReleaseHistoryFragment() {
        // Required empty public constructor
    }
    public static ReleaseHistoryFragment newInstance() {
        ReleaseHistoryFragment fragment = new ReleaseHistoryFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_release_history;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

}
