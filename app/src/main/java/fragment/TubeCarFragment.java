package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.utils.ToastUtil;
import com.fyjr.baselibrary.views.RefreshLayout;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentTubeCarBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.TubeCarAdapter;
import bean.Drivers;
import bean.SimpleBean;
import bean.User;
import manager.UseInfoManager;
import view.DialogAddDriver;
import view.DialogSearchDriver;

/**
 * 管车Fragment
 */
public class TubeCarFragment extends BaseFragment<FragmentTubeCarBinding> implements RefreshLayout.OnLoadListener, View.OnClickListener {
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
        binding.tvTubeAdd.setOnClickListener(this);
        binding.llSearchDirver.setOnClickListener(this);
        getDrivers(listDataBean.getUS(), "", "");
    }

    @Override
    public void onRefresh() {
        getDrivers(listDataBean.getUS(), "", "");
    }

    @Override
    public void onLoad() {

    }

    private void getDrivers(String NetID, String LicensePlate, String CarID) {
        HttpUtil.getInstance().searchDrivers(NetID, LicensePlate, CarID, new HttpCallBack<Drivers>() {
            @Override
            public void onSuccess(Drivers data, String msg) {
                binding.tvNotData.setVisibility(View.GONE);
                binding.reRefresh.setRefreshing(false);
                listDataBeans = data.getListData();
                adapter.setData(listDataBeans);
            }

            @Override
            public void onFail(int errorCode, String msg) {
                binding.tvNotData.setVisibility(View.VISIBLE);
                binding.reRefresh.setRefreshing(false);
                showMessage("没有数据");
            }
        });
    }

    private void addDriver(String name, String phone, String car) {
        HttpUtil.getInstance().addDrivers("", phone, "", "", name, phone,
                "", "", "", "", car, "", "", listDataBean.getUS(), new HttpCallBack<SimpleBean>() {
                    @Override
                    public void onSuccess(SimpleBean data, String msg) {
                        ToastUtil.showToast(getContext(), "添加成功");
                        getDrivers(listDataBean.getUS(), "", "");
                    }

                    @Override
                    public void onFail(int errorCode, String msg) {
                        ToastUtil.showToast(getContext(), "添加失败");
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tube_add:
                showAddDriverDialog();
                break;
            case R.id.ll_search_dirver:
                showSearchDriverDialog();
                break;
        }
    }

    private void showAddDriverDialog() {
        DialogAddDriver addDriver = new DialogAddDriver(getActivity());
        addDriver.showAtLocation(binding.llTubeCarMain, Gravity.CENTER, 0, 0);
        addDriver.setOnAddDriverFinishListener(new DialogAddDriver.OnAddDriverFinishListener() {
            @Override
            public void onClick(String name, String phone, String car) {
                addDriver(name, phone, car);
            }
        });
    }

    private void showSearchDriverDialog(){
        DialogSearchDriver searchDriver = new DialogSearchDriver(getActivity());
        searchDriver.showAtLocation(binding.llTubeCarMain, Gravity.CENTER, 0, 0);
        searchDriver.setOnDoSearchDriverListener(new DialogSearchDriver.OnDoSearchDriverListener() {
            @Override
            public void onClick(String key) {
                getDrivers(listDataBean.getUS(), "", key);
            }
        });
    }
}
