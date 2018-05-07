package com.lit.xiaomei.fragment.goods;


import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fyjr.baselibrary.base.BaseFragment;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.views.RefreshLayout;
import com.lit.xiaomei.activity.CommonLineActivity;
import com.lit.xiaomei.activity.InformationDetailsActivity;
import com.lit.xiaomei.activity.MainActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.CheckAuthority;
import com.lit.xiaomei.bean.News;
import com.lit.xiaomei.databinding.FragmentInformationBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lit.xiaomei.adapter.CityAdapter;
import com.lit.xiaomei.adapter.InformationAdapter;
import com.lit.xiaomei.bean.City;
import com.lit.xiaomei.bean.DetecatedInformation;
import com.lit.xiaomei.bean.GlobalVariable;
import com.lit.xiaomei.bean.Information;
import com.lit.xiaomei.bean.Province;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.bean.Zone;
import com.lit.xiaomei.fragment.GoodsFragment;
import com.lit.xiaomei.manager.LocationManager;
import com.lit.xiaomei.manager.UseInfoManager;
import com.lit.xiaomei.utils.CreateSendMsg;
import com.lit.xiaomei.utils.DataBaseUtils.CityDbHandler;
import com.lit.xiaomei.utils.DataBaseUtils.DaoFactory;
import com.lit.xiaomei.utils.DataBaseUtils.DbSqlite;
import com.lit.xiaomei.utils.DataBaseUtils.IBaseDao;
import com.lit.xiaomei.utils.FormatString;
import com.lit.xiaomei.view.AdvertisementDialog;
import com.lit.xiaomei.view.DialogADedicatedLine;
import com.lit.xiaomei.view.DialogCall;
import com.yxp.permission.util.lib.PermissionInfo;
import com.yxp.permission.util.lib.PermissionUtil;
import com.yxp.permission.util.lib.callback.PermissionOriginResultCallBack;

/**
 * 信息Fragment
 */
public class InformationFragment extends BaseFragment<FragmentInformationBinding> implements RefreshLayout.OnLoadListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private MainActivity mainActivity;
    private IntentFilter intentFilter;
    private ReceiveMsgReceiver receiveMsgReceiver;
    private InformationAdapter adapter;
    private User.ListDataBean listDataBean = new User.ListDataBean();
    private List<Information.SearchINFOBean> searchINFOBeans = new ArrayList<>();
    private boolean isStartReceive = false;
    private List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private List<Zone> zones = new ArrayList<>();
    private List<Province> searchProvinces = new ArrayList<>();
    private List<City> searchCities = new ArrayList<>();
    private List<Zone> searchZones = new ArrayList<>();
    private IBaseDao<Province> provinceIBaseDao;
    private IBaseDao<City> cityIBaseDao;
    private IBaseDao<Zone> zoneIBaseDao;
    private boolean isFromLayoutShow = false;
    private boolean isSearchLayoutShow = false;
    private boolean isSearchLv2LayoutShow = false;
    private CityAdapter fromProvinceAdapter;
    private CityAdapter fromCityAdapter;
    private CityAdapter fromZoneAdapter;
    private CityAdapter searchProvinceAdapter;
    private CityAdapter searchCityAdapter;
    private CityAdapter searchZoneAdapter;
    private boolean isLoad = false;

    private String doProvince = "";
    private String doCity = "";
    /**
     * GridView显示的城市类型
     * 1:省，2:市，3:区
     */
    private int cityType = 1;
    private int searchCityType = 1;
    //所选的省
    private String selectProvince = "";
    private String searchSelectProvince = "";
    //所选的市
    private String selectCity = "";
    private String searchSelectCity = "";
    //所选的区
    private String selectZone = "";
    private String searchSelectZone = "";
    private List<String> addCities = new ArrayList<>();
    private List<String> searchEdits = new ArrayList<>();
    private List<String> filterText = new ArrayList<>();

    private InformationHandler handler;
    private ArrayList<String> texts = new ArrayList<>();
    private RecordAdapter recordAdapter;
    private String searchLv2 = "";
    private String AuthorityType = "QB";
    private boolean isNear = false;
    private boolean isShowTitle = false;
    private boolean isLeft = true;


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
        initDateBase();
        requestPermission();
        space(binding.space);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        ArrayList<String> records = UseInfoManager.getStringArraylist(getContext(), "Record");
        if (records != null) {
            texts = records;
        }
        handler = new InformationHandler();
        listDataBean = UseInfoManager.getUser(getContext()).getListData().get(0);
        doProvince = listDataBean.getPR();
        doCity = listDataBean.getCT();
        String informationMsg = CreateSendMsg.createInformationMsg(getActivity(), listDataBean.getPR(), listDataBean.getCT());
//        mainActivity.sendMsgToSocket(informationMsg);
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalVariable.ReceiverAction.REAL_TIME_MSG);
        receiveMsgReceiver = new ReceiveMsgReceiver();
        getContext().registerReceiver(receiveMsgReceiver, intentFilter);
        adapter = new InformationAdapter(getContext(), searchINFOBeans, this);
        binding.tvFrom.setText(listDataBean.getCT());
        binding.lvInformation.setAdapter(adapter);
        binding.lvInformation.setOnItemClickListener(this);
        binding.reRefresh.setListView(binding.lvInformation);
        binding.reRefresh.setOnLoadListener(this);
        binding.reRefresh.setRefreshing(true);
        binding.rlFrom.setOnClickListener(this);
        binding.llFrom.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
        binding.gvCitylevel1.setOnItemClickListener(new FromOnItemClick());
        binding.gvCitylevel2.setOnItemClickListener(new SearchOnItemClick());
        fromProvinceAdapter = new CityAdapter(getContext(), 1, provinces);
        fromCityAdapter = new CityAdapter(getContext(), 2, cities);
        fromZoneAdapter = new CityAdapter(getContext(), 3, zones);
        searchProvinceAdapter = new CityAdapter(getContext(), 1, provinces);
        searchCityAdapter = new CityAdapter(getContext(), 2, cities);
        searchZoneAdapter = new CityAdapter(getContext(), 3, zones);
        binding.tvTitleLeft.setOnClickListener(this);
        binding.tvTitleRight.setOnClickListener(this);
        binding.tvCommonLine.setOnClickListener(this);
        binding.tvCityBackLevel1.setOnClickListener(this);
        binding.tvCityBackLevel2.setOnClickListener(this);
        binding.btnAddCity.setOnClickListener(this);
        binding.btnDoSearch.setOnClickListener(this);
        binding.llSearchLv2.setOnClickListener(this);
        recordAdapter = new RecordAdapter();
        binding.lvSearchLv3Record.setAdapter(recordAdapter);
        binding.lvSearchLv3Record.setOnItemClickListener(new OnSearchLv2ItemClickListener());
        binding.tvClearRecord.setOnClickListener(this);
        binding.btnAddCityLv2.setOnClickListener(this);
        binding.btnAddCityLv2Cancle.setOnClickListener(this);
        binding.ivNewsDelete.setOnClickListener(this);
        getNews("1");
        doSearch("", doProvince, doCity, "", "");
    }


    private class OnSearchLv2ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int positon, long l) {
            String keyText = texts.get(positon);
            isSearchLv2LayoutShow = false;
            initSearchFromLv2Layout();
            binding.tvSearchLv2.setText(keyText);
            binding.etAddCityLv2.setText(keyText);
            searchEdits.clear();
            if (searchLv2.contains(",")) {
                String[] keys = keyText.split(",");
                for (int i = 0; i < keys.length; i++) {
                    searchEdits.add(keys[i]);
                }
            } else if (searchLv2.contains("，")) {
                String[] keys = keyText.split("，");
                for (int i = 0; i < keys.length; i++) {
                    searchEdits.add(keys[i]);
                }
            } else {
                searchEdits.add(keyText);
            }
            isLoad = false;
            isNear = false;
            doSearch("", doProvince, doCity, CityListToString(addCities), CityListToString(searchEdits));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onRefresh() {
        isNear = false;
        isShowTitle = false;
        isLoad = false;
        doSearch("", doProvince, doCity, CityListToString(addCities), CityListToString(searchEdits));
    }

    @Override
    public void onLoad() {
        isLoad = true;
        binding.reRefresh.addFooterView();
        binding.reRefresh.showLoading();
        doSearch(searchINFOBeans.get(searchINFOBeans.size() - 1).getXH(), doProvince, doCity,
                CityListToString(addCities), CityListToString(searchEdits));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_from:
//                initFromLayout();
                break;
            case R.id.ll_from:
                isSearchLayoutShow = false;
                isSearchLv2LayoutShow = false;
                initSearchFromLayout();
                initSearchFromLv2Layout();
                if (isFromLayoutShow) {
                    isFromLayoutShow = false;
                    initFromLayout();
                } else {
                    isFromLayoutShow = true;
                    showFromLayout();
                }
                break;
            case R.id.ll_search:
                isFromLayoutShow = false;
                isSearchLv2LayoutShow = false;
                initFromLayout();
                initSearchFromLv2Layout();
                if (isSearchLayoutShow) {
                    isSearchLayoutShow = false;
                    initSearchFromLayout();
                } else {
                    isSearchLayoutShow = true;
                    showSearchFromLayout();
                }
                break;
            case R.id.ll_search_lv2:
                isFromLayoutShow = false;
                isSearchLayoutShow = false;
                initFromLayout();
                initSearchFromLayout();
                if (isSearchLv2LayoutShow) {
                    isSearchLv2LayoutShow = false;
                    initSearchFromLv2Layout();
                } else {
                    isSearchLv2LayoutShow = true;
                    showSearchFromLv2Layout();
                }
                break;
            case R.id.tv_city_back_level_1:
                switch (cityType) {
                    case 1:
                        break;
                    case 2:
                        binding.tvSelectedCityLevel1.setText("选择省份");
                        binding.gvCitylevel1.setAdapter(fromProvinceAdapter);
                        binding.tvCityBackLevel1.setVisibility(View.GONE);
                        cityType = 1;
                        break;
                    case 3:
                        binding.tvSelectedCityLevel1.setText("当前所在：" + selectProvince);
                        binding.gvCitylevel1.setAdapter(fromCityAdapter);
                        cityType = 2;
                        break;
                }
                break;
            case R.id.tv_city_back_level_2:
                switch (searchCityType) {
                    case 1:
                        break;
                    case 2:
                        binding.tvSelectedCityLevel2.setText("选择省份");
                        binding.gvCitylevel2.setAdapter(searchProvinceAdapter);
                        binding.tvCityBackLevel2.setVisibility(View.GONE);
                        searchCityType = 1;
                        break;
                    case 3:
                        binding.tvSelectedCityLevel2.setText("当前所在：" + searchSelectProvince);
                        binding.gvCitylevel2.setAdapter(searchCityAdapter);
                        searchCityType = 2;
                        break;
                }
                break;
            case R.id.btn_add_city:
                String city = binding.etAddCity.getText().toString();
                if (TextUtils.isEmpty(city)) {
                    showMessage("没有输入城市");
                    return;
                }
                if (addCities.contains(city)) {
                    showMessage("该城市已存在");
                    return;
                }
                if (addCities.size() == 10) {
                    showMessage("最多只能添加10个城市");
                    return;
                }
                addCities.add(city);
                addCity();
                binding.etAddCity.setText("");
                break;
            case R.id.btn_do_search:
                isLoad = false;
                isSearchLayoutShow = false;
                initSearchFromLayout();
                binding.tvSearch.setText(showText(1, addCities));
                isNear = false;
                doSearch("", doProvince, doCity, CityListToString(addCities), CityListToString(searchEdits));
                break;
            case R.id.iv_call:
                String phone = (String) v.getTag();
                showPhone(phone);
                break;
            case R.id.btn_add_city_lv2:
                searchLv2 = binding.etAddCityLv2.getText().toString();
                if (TextUtils.isEmpty(searchLv2)) {
                    showMessage("内容不能为空");
                    return;
                }
                texts.add(0, searchLv2);
                UseInfoManager.putStringArraylist(getContext(), "Record", texts);
                recordAdapter.notifyDataSetChanged();
                isSearchLv2LayoutShow = false;
                initSearchFromLv2Layout();
                binding.tvSearchLv2.setText(searchLv2);
                searchEdits.clear();
                if (searchLv2.contains(",")) {
                    String[] keys = searchLv2.split(",");
                    for (int i = 0; i < keys.length; i++) {
                        searchEdits.add(keys[i]);
                    }
                } else if (searchLv2.contains("，")) {
                    String[] keys = searchLv2.split("，");
                    for (int i = 0; i < keys.length; i++) {
                        searchEdits.add(keys[i]);
                    }
                } else {
                    searchEdits.add(searchLv2);
                }
                isLoad = false;
                isNear = false;
                doSearch("", doProvince, doCity, CityListToString(addCities), CityListToString(searchEdits));
                break;
            case R.id.btn_add_city_lv2_cancle:
                binding.tvSearchLv2.setText("二级搜索");
                searchLv2 = "";
                binding.etAddCityLv2.setText("");
                isSearchLv2LayoutShow = false;
                initSearchFromLv2Layout();
                searchEdits.clear();
                isLoad = false;
                isNear = false;
                doSearch("", doProvince, doCity, CityListToString(addCities), CityListToString(searchEdits));
                break;
            case R.id.tv_clear_record:
                texts.clear();
                UseInfoManager.putStringArraylist(getContext(), "Record", texts);
                recordAdapter.notifyDataSetChanged();
                break;
            case R.id.iv_news_delete:
                binding.rlNews.setVisibility(View.GONE);
                break;
            case R.id.tv_title_left:
                isLeft = true;
                isNear = false;
                switchTitle(0);
                doSearch("", doProvince, doCity, CityListToString(addCities), CityListToString(searchEdits));
                getContext().sendBroadcast(new Intent().putExtra("fragmentType", 0));
                break;
            case R.id.tv_title_right:
                isNear = true;
                isLeft = false;
                switchTitle(1);
                doSearch("", doProvince, doCity, "", CityListToString(searchEdits));
                break;
            case R.id.tv_common_line:
                startActivity(new Intent(getContext(), CommonLineActivity.class));
                getContext().sendBroadcast(new Intent().putExtra("fragmentType", 0));
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Information.SearchINFOBean bean = searchINFOBeans.get(position);
        if (UseInfoManager.getBoolean(getContext(), "checkAuthority", true)) {
            Intent intent = new Intent(getContext(), InformationDetailsActivity.class);
            intent.putExtra("Details", bean);
            startActivity(intent);
            checkAuthority(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(),
                    listDataBean.getPR(), listDataBean.getCT(), bean.getXH(), AuthorityType, bean);
        } else {
            showMessage(UseInfoManager.getString(getContext(), "checkAuthorityMsg"));
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
                    UseInfoManager.putBoolean(getContext(), "checkAuthority", true);
                } else {
                    UseInfoManager.putBoolean(getContext(), "checkAuthority", false);
                    UseInfoManager.putString(getContext(), "checkAuthorityMsg", data.getStatus());
                }
            }

            @Override
            public void onFail(int errorCode, String msg) {
//                showMessage("网络异常！");
            }
        });

    }

    private class FromOnItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (cityType) {
                case 1:
                    Province province = provinces.get(i);
                    selectProvince = province.getProName();
                    if (TextUtils.equals(selectProvince, "北京") ||
                            TextUtils.equals(selectProvince, "天津") ||
                            TextUtils.equals(selectProvince, "上海") ||
                            TextUtils.equals(selectProvince, "重庆")) {
                        isFromLayoutShow = false;
                        initFromLayout();
                        selectCity = selectProvince;
                        doProvince = selectProvince;
                        doCity = selectCity;
                        isLoad = false;
                        doSearch("", doProvince, doCity, CityListToString(addCities), CityListToString(searchEdits));
                        binding.tvFrom.setText(selectCity);
                    } else {
                        cities = cityIBaseDao.query("ProID=?", new String[]{province.getProSort()});
                        if (TextUtils.equals(selectProvince, "辽宁")) {
                            cities.add(new City("鲅鱼圈"));
                            cities.add(new City("集装箱"));
                        }
                        binding.gvCitylevel1.setAdapter(fromCityAdapter);
                        fromCityAdapter.setDatas(2, cities);
                        binding.tvSelectedCityLevel1.setText("当前所在：" + selectProvince);
                        binding.tvCityBackLevel1.setVisibility(View.VISIBLE);
                        cityType = 2;
                    }
                    break;
                case 2:
                    isFromLayoutShow = false;
                    initFromLayout();
                    City city = cities.get(i);
                    selectCity = city.getCityName();
                    doProvince = selectProvince;
                    doCity = selectCity;
                    isLoad = false;
                    isNear = false;
                    doSearch("", doProvince, doCity, CityListToString(addCities), CityListToString(searchEdits));
                    binding.tvFrom.setText(selectCity);
                    break;
                case 3:
                    break;
            }

        }
    }

    private class SearchOnItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (searchCityType) {
                case 1:
                    Province province = searchProvinces.get(i);
                    searchSelectProvince = province.getProName();
                    if (TextUtils.equals(searchSelectProvince, "北京") ||
                            TextUtils.equals(searchSelectProvince, "天津") ||
                            TextUtils.equals(searchSelectProvince, "上海") ||
                            TextUtils.equals(searchSelectProvince, "重庆")) {
                        searchCities = cityIBaseDao.query("ProID=?", new String[]{province.getProSort()});
                        searchZones = zoneIBaseDao.query("CityID=?", new String[]{searchCities.get(0).getCitySort()});
                        searchZones.add(0, new Zone(searchSelectProvince));
                        binding.gvCitylevel2.setAdapter(searchZoneAdapter);
                        searchZoneAdapter.setDatas(3, searchZones);
                    } else {
                        searchCities = cityIBaseDao.query("ProID=?", new String[]{province.getProSort()});
                        binding.gvCitylevel2.setAdapter(searchCityAdapter);
                        searchCityAdapter.setDatas(2, searchCities);
                    }
                    binding.tvSelectedCityLevel2.setText("当前所在：" + searchSelectProvince);
                    binding.tvCityBackLevel2.setVisibility(View.VISIBLE);
                    searchCityType = 2;
                    break;
                case 2:
                    if (TextUtils.equals(searchSelectProvince, "北京") ||
                            TextUtils.equals(searchSelectProvince, "天津") ||
                            TextUtils.equals(searchSelectProvince, "上海") ||
                            TextUtils.equals(searchSelectProvince, "重庆")) {
                        Zone zone = searchZones.get(i);
                        searchSelectCity = zone.getZoneName();
                        if (addCities.contains(searchSelectCity)) {
                            showMessage("该城市已存在");
                            return;
                        }
                        if (addCities.size() == 10) {
                            showMessage("最多只能添加10个城市");
                            return;
                        }
                        addCities.add(searchSelectCity);
                        addCity();
                    } else {
                        City city = searchCities.get(i);
                        searchSelectCity = city.getCityName();
                        searchZones = zoneIBaseDao.query("CityID=?", new String[]{city.getCitySort()});
                        searchZones.add(0, new Zone(searchSelectCity));
                        binding.gvCitylevel2.setAdapter(searchZoneAdapter);
                        searchZoneAdapter.setDatas(3, searchZones);
                        binding.tvSelectedCityLevel2.setText(binding.tvSelectedCityLevel2.getText().toString() + " — " + searchSelectCity);
                        searchCityType = 3;
                    }
                    break;
                case 3:
                    Zone zone = searchZones.get(i);
                    searchSelectCity = zone.getZoneName();
                    if (addCities.contains(searchSelectCity)) {
                        showMessage("该城市已存在");
                        return;
                    }
                    if (addCities.size() == 10) {
                        showMessage("最多只能添加10个城市");
                        return;
                    }
                    addCities.add(searchSelectCity);
                    addCity();
                    break;
            }
        }
    }

    private class ReceiveMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("Msg");
            Log.e("long", "InformationFragment获得数据：" + msg);
            Information.SearchINFOBean bean = FormatString.formatInformation(msg);
            if (bean != null && isStartReceive) {
                if (filterText.size() == 0) {
                    adapter.addMsg(bean);
                } else {
                    for (String filter : filterText) {
                        if (bean.getMS().contains(filter)) {
                            adapter.addMsg(bean);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiveMsgReceiver);
    }


    private void doSearch(String INXH, final String PROV, final String CITY,
                          final String endCity, final String INFOR) {
        filterText.clear();
        if (!isNear && (!TextUtils.isEmpty(endCity) || !TextUtils.isEmpty(INFOR))) {
            UseInfoManager.putBoolean(getContext(), "isStartReceive", true);
            filterText.addAll(addCities);
            filterText.addAll(searchEdits);
        }else {
            UseInfoManager.putBoolean(getContext(), "isStartReceive", false);
        }
        if (TextUtils.isEmpty(endCity) && TextUtils.isEmpty(INFOR)) {
            AuthorityType = "QB";
        } else {
            AuthorityType = "SS";
        }
        if (!isLoad) {
            mainActivity.sendMsgToSocket(CreateSendMsg.createInformationMsg(getContext(), PROV, CITY));
            binding.reRefresh.setRefreshing(true);
        }
        isStartReceive = false;
        if (isNear) {
            searchNearbyInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "",
                    PROV, CITY, endCity, "", INFOR, "0");
        } else {
            searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "",
                    PROV, CITY, endCity, "", INFOR);
        }
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


    private void searchNearbyInformation(String USER, String PASS, String KEYY,
                                         String INXH, String PROV, String BeginCITY,
                                         final String EndCITY, String INPHONE,
                                         final String INFOR, String StatusFormat) {
        HttpUtil.getInstance().searchNearbyInformation(USER, PASS, KEYY, INXH, PROV, BeginCITY,
                EndCITY, "货", INPHONE, INFOR, StatusFormat, new HttpCallBack<Information>() {
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

    private void initDateBase() {
        if (!new File(GlobalVariable.DateBaseMsg.DB_PATH).exists()) {
            try {
                FileOutputStream out = new FileOutputStream(GlobalVariable.DateBaseMsg.DB_PATH);
                InputStream in = getContext().getAssets().open("city.db");
                byte[] buffer = new byte[1024];
                int readBytes = 0;
                while ((readBytes = in.read(buffer)) != -1)
                    out.write(buffer, 0, readBytes);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SQLiteDatabase db = new CityDbHandler().getDataBase(getContext());
        DbSqlite dbSqlite = new DbSqlite(getContext(), db);
        provinceIBaseDao = DaoFactory.createGenericDao(dbSqlite, Province.class);
        cityIBaseDao = DaoFactory.createGenericDao(dbSqlite, City.class);
        zoneIBaseDao = DaoFactory.createGenericDao(dbSqlite, Zone.class);
        provinces = provinceIBaseDao.queryAll();
        searchProvinces = provinceIBaseDao.queryAll();
        binding.gvCitylevel1.setAdapter(fromProvinceAdapter);
        binding.gvCitylevel2.setAdapter(searchProvinceAdapter);
        fromProvinceAdapter.setDatas(1, provinces);
        searchProvinceAdapter.setDatas(1, searchProvinces);
    }

    private void initFromLayout() {
        binding.llSearchLevel1.setVisibility(View.GONE);
        binding.reRefresh.setVisibility(View.VISIBLE);
        binding.tvFrom.setTextColor(getResources().getColor(R.color.c888888));
        binding.ivFromBiaoHuang.setImageResource(R.mipmap.select_xia_huang);
    }

    private void showFromLayout() {
        binding.llSearchLevel1.setVisibility(View.VISIBLE);
        binding.reRefresh.setVisibility(View.GONE);
        binding.tvFrom.setTextColor(getResources().getColor(R.color.cFD933C));
        binding.ivFromBiaoHuang.setImageResource(R.mipmap.select_shang_huang);
    }

    private void initSearchFromLayout() {
        binding.llSearchLevel2.setVisibility(View.GONE);
        binding.reRefresh.setVisibility(View.VISIBLE);
        binding.tvSearch.setTextColor(getResources().getColor(R.color.c888888));
        binding.ivSearchBiaoHuang.setImageResource(R.mipmap.select_xia_huang);
    }

    private void showSearchFromLayout() {
        binding.llSearchLevel2.setVisibility(View.VISIBLE);
        binding.reRefresh.setVisibility(View.GONE);
        binding.tvSearch.setTextColor(getResources().getColor(R.color.cFD933C));
        binding.ivSearchBiaoHuang.setImageResource(R.mipmap.select_shang_huang);
    }

    private void initSearchFromLv2Layout() {
        binding.reRefresh.setVisibility(View.VISIBLE);
        binding.llSearchLevel3.setVisibility(View.GONE);
        binding.tvSearchLv2.setTextColor(getResources().getColor(R.color.c888888));
        binding.ivSearchBiaoHuangLv2.setImageResource(R.mipmap.select_xia_huang);
    }

    private void showSearchFromLv2Layout() {
        binding.llSearchLevel3.setVisibility(View.VISIBLE);
        binding.reRefresh.setVisibility(View.GONE);
        binding.tvSearchLv2.setTextColor(getResources().getColor(R.color.cFD933C));
        binding.ivSearchBiaoHuangLv2.setImageResource(R.mipmap.select_shang_huang);
    }


    private void addCity() {
        binding.wlAddCity.removeAllViews();
        // 循环添加TextView到容器
        for (int i = 0; i < addCities.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_information_add_city, null, false);
            TextView text = (TextView) view.findViewById(R.id.tv_city);
            text.setText(addCities.get(i));
            text.setTag(addCities.get(i));
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String city = (String) view.getTag();
                    addCities.remove(city);
                    addCity();
                }
            });
            binding.wlAddCity.addView(view);
        }
    }

    private String CityListToString(List<String> list) {
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

    /**
     * 处理显示的城市
     *
     * @param type 类型：1为一级搜索，2为二级搜索
     * @param list
     * @return
     */
    private String showText(int type, List<String> list) {
        String text = "";
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    text += list.get(i) + "，";
                } else {
                    text += list.get(i);
                }
            }
        } else {
            switch (type) {
                case 1:
                    text = "搜索";
                    break;
                case 2:
                    text = "搜索";
                    break;
            }
        }
        return text;
    }

    private String searchContext = "";


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
        dialogCall.showAtLocation(binding.llInformationMain, Gravity.CENTER, 0, 0);
    }

    private void showDedicatedLine() {
        DialogADedicatedLine dialogADedicatedLine = new DialogADedicatedLine(getActivity(), doCity);
        dialogADedicatedLine.showAtLocation(binding.llInformationMain, Gravity.CENTER, 0, 0);
        dialogADedicatedLine.setOnSetFinishListener(new DialogADedicatedLine.OnSetFinishListener() {
            @Override
            public void onClick(List<DetecatedInformation> detecatedInformations) {

            }
        });
    }

    private class InformationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isLoad) {
                        if (isLeft && (!isNear)) {
                            showMessage("手慢了，货都定了！");
                            isNear = true;
                            doSearch("", doProvince, doCity, "", "");
                        } else {
                            showMessage("没有更多了！");
                            binding.reRefresh.removeFooterView();
                        }
                    } else {
                        if (isLeft && (!isNear)) {
                            showMessage("手慢了，货都定了！");
                            isNear = true;
                            doSearch("", doProvince, doCity, "", "");
                        } else {
                            showMessage("没有数据！");
                            searchINFOBeans.clear();
                            adapter.clear();
                            binding.reRefresh.setRefreshing(false);

                        }

                    }
                    break;
                case 1:
                    Information data = (Information) msg.obj;
                    if (isLoad) {
                        if (data.getSearchINFO().size() == 0 || data == null) {
                            if (isNear) {
                                showMessage("没有更多了！");
                                binding.reRefresh.setNoMoreData();
                            } else {
                                showMessage("手慢了，货都定了！");
                                isNear = true;
                                doSearch("", doProvince, doCity, "", "");
                            }
                        } else {
                            if (isLeft && isNear && (!isShowTitle)) {
                                isShowTitle = true;
                                adapter.setLine(searchINFOBeans.size());
                            } else {
                                adapter.setLine(-1);
                            }
                            adapter.addListMsg(data.getSearchINFO());
                        }
                    } else {
                        binding.reRefresh.removeFooterView();
                        binding.lvInformation.smoothScrollToPosition(0);
                        binding.tvNotData.setVisibility(View.GONE);
                        binding.reRefresh.setRefreshing(false);
                        searchINFOBeans.clear();
                        if (isLeft && isNear && (!isShowTitle)) {
                            isShowTitle = true;
                            adapter.setLine(searchINFOBeans.size());
                        } else {
                            adapter.setLine(-1);
                        }
                        searchINFOBeans = data.getSearchINFO();
                        adapter.clear();
                        adapter.setData(searchINFOBeans);
                        isStartReceive = true;
                    }
                    adapter.setFilter(filterText);
                    break;
            }
        }
    }

    private class RecordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return texts.size();
        }

        @Override
        public Object getItem(int i) {
            return texts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_record, viewGroup, false);
            TextView context = (TextView) view.findViewById(R.id.tv_context);
            context.setText(texts.get(i));
            return view;
        }
    }

    private void getNews(final String ClassID) {
        HttpUtil.getInstance().getNews(ClassID, new HttpCallBack<News>() {
            @Override
            public void onSuccess(News data, String msg) {
                if (TextUtils.equals("1", ClassID)) {
                    String newsContent = data.getListData().get(0).getContentTxt();
                    if (!TextUtils.isEmpty(newsContent)) {
                        binding.rlNews.setVisibility(View.VISIBLE);
                        binding.tvNews.setText(newsContent);
                    } else {
                        binding.rlNews.setVisibility(View.GONE);
                    }
                    getNews("2");
                } else if (TextUtils.equals("2", ClassID)) {
                    String imgUrl = data.getListData().get(0).getImageName();
                    if (!TextUtils.isEmpty(imgUrl)) {
                        AdvertisementDialog dialog = new AdvertisementDialog(getContext(), imgUrl);
                        dialog.showAtLocation(binding.llInformationMain, Gravity.CENTER, 0, 0);
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String msg) {
                binding.rlNews.setVisibility(View.GONE);
            }
        });

    }

    private void doLocation() {
        final LocationManager locationManager = new LocationManager(getContext());
        locationManager.doLocation(new LocationManager.OnLocationSuccessListener() {

            @Override
            public void onSuccess(HashMap<String, Object> result) {
                boolean isSuccess = false;
                isSuccess = (Boolean) result.get("isSuccess");
                if (isSuccess) {
                    String province = (String) result.get("province");
                    String city = (String) result.get("city");
                    if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city)) {
                        return;
                    }
                    final String provinceRes = province.substring(0, province.length() - 1);
                    final String cityRes = city.substring(0, city.length() - 1);
                    if (TextUtils.equals(doCity, cityRes)) {
                        return;
                    }
                    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
                    normalDialog.setTitle("定位成功");
                    normalDialog.setMessage("是否切换到" + city + "?");
                    normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doProvince = provinceRes;
                            doCity = cityRes;
                            binding.tvFrom.setText(doCity);
                            doSearch("", doProvince, doCity, "", "");
                        }
                    });
                    normalDialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    // 显示
                    normalDialog.show();
                }
            }
        });
        locationManager.mLocationClient.start();
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
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        PermissionUtil.getInstance().request(getActivity(), new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionOriginResultCallBack() {
                    @Override
                    public void onResult(List<PermissionInfo> acceptList, List<PermissionInfo> rationalList, List<PermissionInfo> deniedList) {
                        doLocation();
                    }
                }
        );
    }
}
