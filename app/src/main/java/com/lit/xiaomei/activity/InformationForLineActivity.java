package com.lit.xiaomei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.fyjr.baselibrary.base.BaseActivity;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.views.RefreshLayout;
import com.lit.xiaomei.R;
import com.lit.xiaomei.adapter.InformationAdapter;
import com.lit.xiaomei.bean.CheckAuthority;
import com.lit.xiaomei.bean.Information;
import com.lit.xiaomei.bean.Line;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.databinding.ActivityInformationForLineBinding;
import com.lit.xiaomei.manager.UseInfoManager;
import com.lit.xiaomei.utils.CreateSendMsg;
import com.lit.xiaomei.view.DialogCall;

import java.util.ArrayList;
import java.util.List;

public class InformationForLineActivity extends BaseActivity<ActivityInformationForLineBinding> implements View.OnClickListener,
        AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener {
    private String fromText = "";
    private String toText = "";
    private Line line = new Line();
    private InformationHandler handler;
    private InformationAdapter adapter;
    private List<Information.SearchINFOBean> searchINFOBeans = new ArrayList<>();
    private boolean isLoad = false;
    private User.ListDataBean listDataBean = new User.ListDataBean();
    private String doProvince = "";
    private String doCity = "";
    private String AuthorityType = "QB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_information_for_line);
    }

    @Override
    public void initView() {
        super.initView();
        handler = new InformationHandler();
        line = (Line) getIntent().getSerializableExtra("line");
        listDataBean = UseInfoManager.getUser(this).getListData().get(0);
        doProvince = listDataBean.getPR();
        doCity = listDataBean.getCT();
        for (int i = 0; i < line.getFromCities().size(); i++) {
            if (i != line.getFromCities().size() - 1) {
                fromText = fromText + line.getFromCities().get(i) + "/";
            } else {
                fromText = fromText + line.getFromCities().get(i);
            }
        }
        for (int i = 0; i < line.getToCities().size(); i++) {
            if (i != line.getToCities().size() - 1) {
                toText = toText + line.getToCities().get(i) + "/";
            } else {
                toText = toText + line.getToCities().get(i);
            }
        }
        setTitle(fromText + " — " + toText);
        setTitleTextColor("#ffffff");
        adapter = new InformationAdapter(this, searchINFOBeans, this);
        binding.lvInformation.setAdapter(adapter);
        binding.lvInformation.setOnItemClickListener(this);
        binding.reRefresh.setListView(binding.lvInformation);
        binding.reRefresh.setOnLoadListener(this);
        binding.reRefresh.setRefreshing(true);
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "", doProvince, CityListString(line.getFromCities()),
                CityListString(line.getToCities()), "", "");
    }

    private void searchInformation(String USER, String PASS, String KEYY,
                                   String INXH, final String PROV, final String CITY,
                                   final String INCITY, String INPHONE,
                                   final String INFOR) {
        HttpUtil.getInstance().searchInformation(USER, PASS, KEYY, INXH, PROV, CITY, INCITY, "货", INPHONE, INFOR, new HttpCallBack<Information>() {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_call:
                String phone = (String) v.getTag();
                showPhone(phone);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Information.SearchINFOBean bean = searchINFOBeans.get(position);
        if (UseInfoManager.getBoolean(this, "checkAuthority", true)) {
            Intent intent = new Intent(this, InformationDetailsActivity.class);
            intent.putExtra("Details", bean);
            startActivity(intent);
            checkAuthority(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(),
                    listDataBean.getPR(), listDataBean.getCT(), bean.getXH(), AuthorityType, bean);
        } else {
            showMessage(UseInfoManager.getString(this, "checkAuthorityMsg"));
        }
    }

    /**
     * 检查用户权限
     */
    private void checkAuthority(String NetID, String PWord, String key, String PR, String CT, String XH, String QC, final Information.SearchINFOBean bean) {
        HttpUtil.getInstance().checkAuthority(NetID, PWord, key, PR, CT, XH, QC, new HttpCallBack<CheckAuthority>() {
            @Override
            public void onSuccess(CheckAuthority data, String msg) {
                if (TextUtils.equals("1", data.getStatusId())) {
                    UseInfoManager.putBoolean(InformationForLineActivity.this, "checkAuthority", true);
                } else {
                    UseInfoManager.putBoolean(InformationForLineActivity.this, "checkAuthority", false);
                    UseInfoManager.putString(InformationForLineActivity.this, "checkAuthorityMsg", data.getStatus());
                }
            }

            @Override
            public void onFail(int errorCode, String msg) {
//                showMessage("网络异常！");
            }
        });

    }

    @Override
    public void onRefresh() {
        isLoad = false;
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "", doProvince, CityListString(line.getFromCities()),
                CityListString(line.getToCities()), "", "");
    }

    @Override
    public void onLoad() {
        isLoad = true;
        binding.reRefresh.addFooterView();
        binding.reRefresh.showLoading();
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), searchINFOBeans.get(searchINFOBeans.size() - 1).getXH(), doProvince, CityListString(line.getFromCities()),
                CityListString(line.getToCities()), "", "");
    }

    private class InformationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isLoad) {
                        showMessage("没有更多了！");
                        binding.reRefresh.removeFooterView();
                    } else {
                        showMessage("刷新失败！");
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
                        binding.reRefresh.removeFooterView();
                        binding.lvInformation.smoothScrollToPosition(0);
                        binding.reRefresh.setRefreshing(false);
                        searchINFOBeans.clear();
                        searchINFOBeans = data.getSearchINFO();
                        adapter.clear();
                        adapter.setData(searchINFOBeans);
                    }
                    break;
            }
        }
    }

    private String CityListString(List<String> list) {
        String text = "";
        for (int i = 0; i < list.size(); i++) {
            if (i != list.size() - 1) {
                text += list.get(i) + "~";
            } else {
                text += list.get(i);
            }
        }
        return text;
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
        DialogCall dialogCall = new DialogCall(this, phones);
        dialogCall.showAtLocation(binding.llLineMain, Gravity.CENTER, 0, 0);
    }
}
