package fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.adapters.AdapterViewBindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fyjr.baselibrary.base.BaseFragment;
import com.fyjr.baselibrary.http.HttpUtil;
import com.fyjr.baselibrary.http.callback.HttpCallBack;
import com.fyjr.baselibrary.views.RefreshLayout;
import com.lit.xiaomei.MainActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.FragmentInformationBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import adapter.CityAdapter;
import adapter.InformationAdapter;
import bean.City;
import bean.GlobalVariable;
import bean.Information;
import bean.Province;
import bean.User;
import bean.Zone;
import manager.UseInfoManager;
import utils.CreateSendMsg;
import utils.DataBaseUtils.CityDbHandler;
import utils.DataBaseUtils.DaoFactory;
import utils.DataBaseUtils.DbSqlite;
import utils.DataBaseUtils.IBaseDao;
import utils.FormatString;
import view.DialogCall;
import view.DialogSearchLv2;

/**
 * 信息Fragment
 */
public class InformationFragment extends BaseFragment<FragmentInformationBinding> implements RefreshLayout.OnLoadListener, View.OnClickListener {
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
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "", doProvince, doCity,
                "", "", "", "", "1");
        initDateBase();
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        listDataBean = UseInfoManager.getUser(getContext()).getListData().get(0);
        doProvince = listDataBean.getPR();
        doCity = listDataBean.getCT();
        String informationMsg = CreateSendMsg.createInformationMsg(getActivity(), listDataBean.getPR(), listDataBean.getCT());
        mainActivity.sendMsgToSocket(informationMsg);
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalVariable.ReceiverAction.REAL_TIME_MSG);
        receiveMsgReceiver = new ReceiveMsgReceiver();
        getContext().registerReceiver(receiveMsgReceiver, intentFilter);
        adapter = new InformationAdapter(getContext(), searchINFOBeans, this);
        binding.lvInformation.setAdapter(adapter);
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
        binding.tvCityBackLevel1.setOnClickListener(this);
        binding.tvCityBackLevel2.setOnClickListener(this);
        binding.btnAddCity.setOnClickListener(this);
        binding.btnDoSearch.setOnClickListener(this);
        binding.tvInputKey.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onRefresh() {
        isLoad = false;
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "", doProvince, doCity,
                "", "", "", "", "1");
    }

    @Override
    public void onLoad() {
        isLoad = true;
        binding.reRefresh.addFooterView();
        binding.reRefresh.showLoading();
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), searchINFOBeans.get(searchINFOBeans.size() - 1).getXH(),
                doProvince, doCity, "", "", "", "", "1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_from:
//                initFromLayout();
                break;
            case R.id.ll_from:
                isSearchLayoutShow = false;
                initSearchFromLayout();
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
                initFromLayout();
                if (isSearchLayoutShow) {
                    isSearchLayoutShow = false;
                    initSearchFromLayout();
                } else {
                    isSearchLayoutShow = true;
                    showSearchFromLayout();
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
                break;
            case R.id.btn_do_search:
                isSearchLayoutShow = false;
                initSearchFromLayout();
                binding.tvSearch.setText(showText(1, addCities));
                filterText.clear();
                filterText.addAll(addCities);
                filterText.addAll(searchEdits);
                break;
            case R.id.tv_input_key:
                showInputKeyDialog();
                break;
            case R.id.iv_call:
                String phone = (String) v.getTag();
                showPhone(phone);
                break;
        }

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
                        cities = cityIBaseDao.query("ProID=?", new String[]{province.getProSort()});
                        zones = zoneIBaseDao.query("CityID=?", new String[]{cities.get(0).getCitySort()});
                        binding.gvCitylevel1.setAdapter(fromZoneAdapter);
                        fromZoneAdapter.setDatas(3, zones);
                    } else {
                        cities = cityIBaseDao.query("ProID=?", new String[]{province.getProSort()});
                        binding.gvCitylevel1.setAdapter(fromCityAdapter);
                        fromCityAdapter.setDatas(2, cities);
                    }
                    binding.tvSelectedCityLevel1.setText("当前所在：" + selectProvince);
                    binding.tvCityBackLevel1.setVisibility(View.VISIBLE);
                    cityType = 2;
                    break;
                case 2:
                    City city = cities.get(i);
                    selectCity = city.getCityName();
                    doProvince = selectProvince;
                    doCity = selectCity;
                    mainActivity.sendMsgToSocket(CreateSendMsg.createInformationMsg(getActivity(), doProvince, doCity));
                    searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "", doProvince, doCity,
                            "", "", "", "", "1");
                    isFromLayoutShow = false;
                    initFromLayout();
                    binding.tvFrom.setText(selectCity);
//                    if (TextUtils.equals(selectProvince, "北京") ||
//                            TextUtils.equals(selectProvince, "天津") ||
//                            TextUtils.equals(selectProvince, "上海") ||
//                            TextUtils.equals(selectProvince, "重庆")) {
//                        selectCity = selectProvince;
//
//
//                    } else {
//                        City city = cities.get(i);
//                        selectCity = city.getCityName();
//                        zones = zoneIBaseDao.query("CityID=?", new String[]{city.getCitySort()});
//                        binding.gvCitylevel1.setAdapter(fromZoneAdapter);
//                        fromZoneAdapter.setDatas(3, zones);
//                        binding.tvSelectedCityLevel1.setText(binding.tvSelectedCityLevel1.getText().toString() + " — " + selectCity);
//                        cityType = 3;
//                    }
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

    private void searchInformation(String USER, String PASS, String KEYY,
                                   String INXH, String PROV, String CITY,
                                   String INCITY, String INCLASS, String INPHONE,
                                   String INFOR, String TextFormat) {
        isStartReceive = false;
        HttpUtil.getInstance().searchInformation(USER, PASS, KEYY, INXH, PROV, CITY, INCITY, INCLASS, INPHONE, INFOR, TextFormat, new HttpCallBack<Information>() {
            @Override
            public void onSuccess(Information data, String msg) {
                if (isLoad) {
                    if (data.getSearchINFO().size() == 0 || data == null) {
                        binding.reRefresh.setNoMoreData();
                    } else {
                        adapter.addListMsg(data.getSearchINFO());
                    }
                } else {
                    binding.reRefresh.setRefreshing(false);
                    searchINFOBeans.clear();
                    searchINFOBeans = data.getSearchINFO();
                    adapter.setData(searchINFOBeans);
                    isStartReceive = true;
                }
            }

            @Override
            public void onFail(int errorCode, String msg) {
                binding.reRefresh.setRefreshing(false);
                showMessage("获取失败！");
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
        searchProvinceAdapter.setDatas(1, provinces);
    }

    private void initFromLayout() {
        binding.llSearchLevel1.setVisibility(View.GONE);
        binding.reRefresh.setVisibility(View.VISIBLE);
        binding.ivFromBiaoHuang.setImageResource(R.mipmap.select_xia_huang);
    }

    private void showFromLayout() {
        binding.llSearchLevel1.setVisibility(View.VISIBLE);
        binding.reRefresh.setVisibility(View.GONE);
        binding.ivFromBiaoHuang.setImageResource(R.mipmap.select_shang_huang);
    }

    private void initSearchFromLayout() {
        binding.llSearchLevel2.setVisibility(View.GONE);
        binding.reRefresh.setVisibility(View.VISIBLE);
        binding.ivSearchBiaoHuang.setImageResource(R.mipmap.select_xia_huang);
    }

    private void showSearchFromLayout() {
        binding.llSearchLevel2.setVisibility(View.VISIBLE);
        binding.reRefresh.setVisibility(View.GONE);
        binding.ivSearchBiaoHuang.setImageResource(R.mipmap.select_shang_huang);
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
        for (int i = 0; i < list.size() - 1; i++) {
            if (i != list.size()) {
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

    private void showInputKeyDialog() {
        DialogSearchLv2 dialogSearchLv2 = new DialogSearchLv2(getActivity());
        dialogSearchLv2.showAtLocation(binding.llInformationMain, Gravity.CENTER, 0, 0);
        dialogSearchLv2.setSearchCListener(new DialogSearchLv2.OnSearchListener() {
            @Override
            public void onClick(boolean isDo, String searchText) {
                isFromLayoutShow = false;
                initFromLayout();
                isSearchLayoutShow = false;
                initSearchFromLayout();
                if (isDo) {
                    searchContext = searchText;
                    binding.tvInputKey.setText(searchText);
                    if (searchText.contains("，")) {
                        String[] searchTexts = searchText.split("，");
                        for (int i = 0; i < searchTexts.length; i++) {
                            filterText.add(searchTexts[i]);
                        }
                    } else {
                        filterText.add(searchText);
                    }
                } else {
                    binding.tvInputKey.setText("请输入关键字");
                    if (!TextUtils.isEmpty(searchContext)) {
                        if (searchContext.contains("，")) {
                            String[] searchTexts = searchContext.split("，");
                            for (int i = 0; i < searchTexts.length; i++) {
                                String text = searchTexts[i];
                                if (filterText.contains(text)) {
                                    filterText.remove(text);
                                }
                            }
                        } else {
                            if (filterText.contains(searchContext)) {
                                filterText.remove(searchContext);
                            }
                        }
                    }
                }
            }
        });
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
        dialogCall.showAtLocation(binding.llInformationMain, Gravity.CENTER, 0, 0);
    }


}
