package fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.adapters.AdapterViewBindingAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.base.BaseFragment;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.views.RefreshLayout;
import com.lit.xiaomei.MainActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentInformationBinding;

import java.util.ArrayList;
import java.util.List;

import adapter.InformationAdapter;
import bean.GlobalVariable;
import bean.Information;
import bean.User;
import manager.UseInfoManager;
import utils.CreateSendMsg;
import utils.FormatString;

/**
 * 信息Fragment
 */
public class InformationFragment extends BaseFragment<FragmentInformationBinding> implements RefreshLayout.OnLoadListener {
    private MainActivity mainActivity;
    private IntentFilter intentFilter;
    private ReceiveMsgReceiver receiveMsgReceiver;
    private InformationAdapter adapter;
    private User.ListDataBean listDataBean = new User.ListDataBean();
    private List<Information.SearchINFOBean> searchINFOBeans = new ArrayList<>();

    public InformationFragment() {
    }

    public static InformationFragment newInstance() {
        InformationFragment fragment = new InformationFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_information;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        space(binding.space);
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "", "", listDataBean.getCT(),
                "", "", "", "", "1");
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        listDataBean = UseInfoManager.getUser(getContext()).getListData().get(0);
        String informationMsg = CreateSendMsg.createInformationMsg(getActivity(), "辽宁", "集装箱");
        mainActivity.sendMsgToSocket(informationMsg);
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalVariable.ReceiverAction.REAL_TIME_MSG);
        receiveMsgReceiver = new ReceiveMsgReceiver();
        getContext().registerReceiver(receiveMsgReceiver, intentFilter);
        adapter = new InformationAdapter(getContext(), searchINFOBeans);
        binding.lvInformation.setAdapter(adapter);
        binding.reRefresh.setListView(binding.lvInformation);
        binding.reRefresh.setOnLoadListener(this);
        binding.reRefresh.setRefreshing(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onRefresh() {
        searchINFOBeans.clear();
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "", "", listDataBean.getCT(),
                "", "", "", "", "1");
    }

    @Override
    public void onLoad() {

    }

    private class ReceiveMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("Msg");
            Log.e("long", "InformationFragment获得数据：" + msg);
            Information.SearchINFOBean bean = FormatString.formatInformation(msg);
            if (bean != null) {
                adapter.addMsg(bean);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiveMsgReceiver);
    }

    private void searchInformation(String USER, String PASS, String KEYY,
                                   String INXH, String PROV, String CITY,
                                   String INCITY, String INCLASS, String INPHONE,
                                   String INFOR, String TextFormat) {
        HttpUtil.getInstance().searchInformation(USER, PASS, KEYY, INXH, PROV, CITY, INCITY, INCLASS, INPHONE, INFOR, TextFormat, new HttpCallBack<Information>() {
            @Override
            public void onSuccess(Information data, String msg) {
                binding.reRefresh.setRefreshing(false);
                searchINFOBeans = data.getSearchINFO();
                adapter.setData(searchINFOBeans);
            }

            @Override
            public void onFail(int errorCode, String msg) {
                binding.reRefresh.setRefreshing(false);
                showMessage("获取失败！");
            }
        });
    }

}
