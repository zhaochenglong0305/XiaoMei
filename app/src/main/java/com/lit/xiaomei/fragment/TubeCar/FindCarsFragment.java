package com.lit.xiaomei.fragment.TubeCar;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.views.RefreshLayout;
import com.lit.xiaomei.R;
import com.lit.xiaomei.adapter.TubeCarAdapter;
import com.lit.xiaomei.bean.Information;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.databinding.FragmentFindCarsBinding;
import com.lit.xiaomei.manager.UseInfoManager;
import com.lit.xiaomei.view.DialogCall;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindCarsFragment extends BaseFragment<FragmentFindCarsBinding> implements RefreshLayout.OnLoadListener, View.OnClickListener {
    private List<Information.SearchINFOBean> searchINFOBeans = new ArrayList<>();
    private User.ListDataBean listDataBean = new User.ListDataBean();
    private TubeCarAdapter adapter;
    private InformationHandler handler;
    private boolean isLoad = false;
    private boolean isDo = true;
    private String key = "";

    public FindCarsFragment() {
    }

    public static FindCarsFragment newInstance() {
        FindCarsFragment fragment = new FindCarsFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find_cars;
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
        handler = new InformationHandler();
        listDataBean = UseInfoManager.getUser(getContext()).getListData().get(0);
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "",
                listDataBean.getPR(), listDataBean.getCT(),
                "", "", "");
        adapter = new TubeCarAdapter(getContext(), searchINFOBeans, this);
        binding.lvDrivers.setAdapter(adapter);
        binding.reRefresh.setListView(binding.lvDrivers);
        binding.reRefresh.setOnLoadListener(this);
        binding.reRefresh.setRefreshing(true);
        binding.tvFindCars.setOnClickListener(this);
    }

    private void searchInformation(String USER, String PASS, String KEYY,
                                   String INXH, String PROV, String CITY,
                                   String INCITY, String INPHONE,
                                   String INFOR) {
        HttpUtil.getInstance().searchInformation(false, USER, PASS, KEYY, INXH, PROV, CITY, INCITY, "车", INPHONE, INFOR, new HttpCallBack<Information>() {
            @Override
            public void onSuccess(Information data, String msg) {
                Message message = new Message();
                message.what = 1;
                message.obj = data;
                handler.sendMessage(message);
            }

            @Override
            public void onFail(int errorCode, String msg) {
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onRefresh() {
        isLoad = false;
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "",
                listDataBean.getPR(), listDataBean.getCT(),
                "", "", "");
    }

    @Override
    public void onLoad() {
        isLoad = true;
        binding.reRefresh.addFooterView();
        binding.reRefresh.showLoading();
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), searchINFOBeans.get(searchINFOBeans.size() - 1).getXH(),
                listDataBean.getPR(), listDataBean.getCT(),
                "", "", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_call:
                String phone = (String) view.getTag();
                showPhone(phone);
                break;
            case R.id.tv_find_cars:
                if (isDo) {
                    key = binding.etDoSearch.getText().toString();
                    if (TextUtils.isEmpty(key)) {
                        showMessage("搜索内容不能为空！");
                        return;
                    }
                    initSearchBtn();
                } else {
                    key = "";
                    binding.etDoSearch.setText("");
                    initSearchBtn();
                }
                break;
        }

    }

    private void initSearchBtn() {
        if (isDo) {
            isDo = false;
            binding.tvFindCars.setText("取消");
        } else {
            isDo = true;
            binding.tvFindCars.setText("搜索");
        }
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "",
                listDataBean.getPR(), listDataBean.getCT(),
                "", "", key);
    }

    private void showPhone(String phoneString) {
        List<String> phones = new ArrayList<>();
        if (phoneString.contains("，")) {
            String[] pds = phoneString.split("，");
            for (int i = 0; i < pds.length; i++) {
                phones.add(pds[i]);
            }

        } else if (phoneString.contains(",")) {
            String[] pxs = phoneString.split(",");
            for (int i = 0; i < pxs.length; i++) {
                phones.add(pxs[i]);
            }
        } else if (phoneString.contains(" ")) {
            String[] pks = phoneString.split(" ");
            for (int i = 0; i < pks.length; i++) {
                phones.add(pks[i]);
            }
        } else {
            phones.add(phoneString);
        }
        DialogCall dialogCall = new DialogCall(getActivity(), phones);
        dialogCall.showAtLocation(binding.llFindcarsMain, Gravity.CENTER, 0, 0);
    }

    private class InformationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isLoad) {
                        binding.reRefresh.removeFooterView();
                    } else {
                        binding.reRefresh.setRefreshing(false);
                    }
                    break;
                case 1:
                    Information data = (Information) msg.obj;
                    if (isLoad) {
                        if (data.getSearchINFO().size() == 0 || data == null) {
                            binding.reRefresh.setNoMoreData();
                        } else {
                            adapter.addListMsg(data.getSearchINFO());
                        }
                    } else {
                        binding.tvNotData.setVisibility(View.GONE);
                        binding.reRefresh.setRefreshing(false);
                        searchINFOBeans.clear();
                        searchINFOBeans = data.getSearchINFO();
                        adapter.setData(searchINFOBeans);
                    }
                    break;
            }
        }
    }
}
