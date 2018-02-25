package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.views.RefreshLayout;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentTubeCarBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.TubeCarAdapter;
import bean.Drivers;
import bean.User;
import manager.UseInfoManager;

/**
 * 管车Fragment
 */
public class TubeCarFragment extends BaseFragment<FragmentTubeCarBinding> implements RefreshLayout.OnLoadListener {
    private List<Drivers.ListDataBean> listDataBeans = new ArrayList<>();
    private TubeCarAdapter adapter;
    private User.ListDataBean listDataBean = new User.ListDataBean();

    public TubeCarFragment() {
    }

    public static TubeCarFragment newInstance() {
        TubeCarFragment fragment = new TubeCarFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tube_car;
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
        listDataBean = UseInfoManager.getUser(getContext()).getListData().get(0);
        adapter = new TubeCarAdapter(getContext(), listDataBeans);
        binding.lvDrivers.setAdapter(adapter);
        binding.reRefresh.setListView(binding.lvDrivers);
        binding.reRefresh.setOnLoadListener(this);
        binding.reRefresh.setRefreshing(true);
        getDrivers(listDataBean.getUS(),"","");
    }

    @Override
    public void onRefresh() {
        getDrivers(listDataBean.getUS(),"","");
    }

    @Override
    public void onLoad() {

    }

    private void getDrivers(String NetID, String LicensePlate, String CarID) {
        HttpUtil.getInstance().searchDrivers(NetID, LicensePlate, CarID, new HttpCallBack<Drivers>() {
            @Override
            public void onSuccess(Drivers data, String msg) {
                binding.reRefresh.setRefreshing(false);
                listDataBeans = data.getListData();
                adapter.setData(listDataBeans);
            }

            @Override
            public void onFail(int errorCode, String msg) {
                binding.reRefresh.setRefreshing(false);
                showMessage("没有数据");
            }
        });
    }
}
