package fragment.release;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentRelesaeInformationBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReleaseInformationFragment extends BaseFragment<FragmentRelesaeInformationBinding> {


    public ReleaseInformationFragment() {
    }
    public static ReleaseInformationFragment newInstance() {
        ReleaseInformationFragment fragment = new ReleaseInformationFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_relesae_information;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

}
