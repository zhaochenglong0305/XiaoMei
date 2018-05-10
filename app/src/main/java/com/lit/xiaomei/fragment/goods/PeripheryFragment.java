package com.lit.xiaomei.fragment.goods;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.lit.xiaomei.R;
import com.lit.xiaomei.activity.InformationDetailsActivity;
import com.lit.xiaomei.adapter.CityAdapter;
import com.lit.xiaomei.adapter.InformationAdapter;
import com.lit.xiaomei.bean.CheckAuthority;
import com.lit.xiaomei.bean.City;
import com.lit.xiaomei.bean.GlobalVariable;
import com.lit.xiaomei.bean.Information;
import com.lit.xiaomei.bean.Province;
import com.lit.xiaomei.bean.User;
import com.lit.xiaomei.bean.Zone;
import com.lit.xiaomei.databinding.FragmentPeripheryBinding;
import com.lit.xiaomei.manager.UseInfoManager;
import com.lit.xiaomei.utils.DataBaseUtils.CityDbHandler;
import com.lit.xiaomei.utils.DataBaseUtils.DaoFactory;
import com.lit.xiaomei.utils.DataBaseUtils.DbSqlite;
import com.lit.xiaomei.utils.DataBaseUtils.IBaseDao;
import com.lit.xiaomei.view.DialogCall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeripheryFragment extends BaseFragment<FragmentPeripheryBinding> implements View.OnClickListener,
        AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener {
    private boolean showAddCityView = false;
    private boolean showAddKeyView = false;
    private String AuthorityType = "QB";
    private InformationHandler handler;
    private List<Information.SearchINFOBean> searchINFOBeans = new ArrayList<>();
    private InformationAdapter adapter;
    private boolean isLoad = false;
    private List<String> filterText = new ArrayList<>();
    private IBaseDao<Province> provinceIBaseDao;
    private IBaseDao<City> cityIBaseDao;
    private IBaseDao<Zone> zoneIBaseDao;
    private List<Province> searchProvinces = new ArrayList<>();
    private List<City> searchCities = new ArrayList<>();
    private List<Zone> searchZones = new ArrayList<>();
    private CityAdapter searchProvinceAdapter;
    private CityAdapter searchCityAdapter;
    private CityAdapter searchZoneAdapter;
    private int searchCityType = 1;
    private String searchSelectProvince = "";
    private String searchSelectCity = "";
    private String searchSelectZone = "";
    private List<String> addCities = new ArrayList<>();
    private List<String> addKeys = new ArrayList<>();
    private User.ListDataBean user = new User.ListDataBean();
    private String doProvince = "";
    private String doCity = "";
    private ArrayList<String> keyTexts = new ArrayList<>();

    public PeripheryFragment() {
    }

    public static PeripheryFragment newInstance() {
        PeripheryFragment fragment = new PeripheryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return binding.getRoot();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_periphery;
    }

    @Override
    public void initView() {
        super.initView();
        user = UseInfoManager.getUser(getContext()).getListData().get(0);
        keyTexts = UseInfoManager.getStringArraylist(getContext(), "Record");
        doProvince = user.getPR();
        doCity = user.getCT();
        handler = new InformationHandler();
        binding.rlSearchCity.setOnClickListener(this);
        binding.rlSearchKey.setOnClickListener(this);
        adapter = new InformationAdapter(getContext(), searchINFOBeans, this);
        searchProvinceAdapter = new CityAdapter(getContext(), 1, searchProvinces);
        searchCityAdapter = new CityAdapter(getContext(), 2, searchCities);
        searchZoneAdapter = new CityAdapter(getContext(), 3, searchZones);
        binding.lvInformation.setAdapter(adapter);
        binding.lvInformation.setOnItemClickListener(this);
        binding.reRefresh.setListView(binding.lvInformation);
        binding.reRefresh.setOnLoadListener(this);
        binding.reRefresh.setRefreshing(true);
        binding.gvCityList.setOnItemClickListener(new SearchOnItemClick());
        binding.btnAddCity.setOnClickListener(this);
        binding.btnDoSearch.setOnClickListener(this);
        binding.btnKeySearch.setOnClickListener(this);
        binding.btnSearchKeyCancle.setOnClickListener(this);
        initDateBase();
        searchInformation(user.getUS(), user.getPW(), user.getKY(), "0", doProvince, doCity, "", "", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search_city:
                if (showAddCityView) {
                    showAddCityView = false;
                    showAddKeyView = false;
                    binding.llSearchCity.setVisibility(View.GONE);
                    binding.llSearchKey.setVisibility(View.GONE);
                    binding.rlContextList.setVisibility(View.VISIBLE);
                    binding.ivSearchCity.setImageResource(R.mipmap.select_xia_huang);
                    binding.ivSearchKey.setImageResource(R.mipmap.select_xia_huang);
                } else {
                    showAddCityView = true;
                    showAddKeyView = false;
                    binding.llSearchCity.setVisibility(View.VISIBLE);
                    binding.llSearchKey.setVisibility(View.GONE);
                    binding.rlContextList.setVisibility(View.GONE);
                    binding.tvSearchCity.setTextColor(getResources().getColor(R.color.cFD933C));
                    binding.ivSearchCity.setImageResource(R.mipmap.select_shang_huang);
                    binding.tvSearchKey.setTextColor(getResources().getColor(R.color.c888888));
                    binding.ivSearchKey.setImageResource(R.mipmap.select_xia_huang);
                }
                break;
            case R.id.rl_search_key:
                if (showAddKeyView) {
                    showAddKeyView = false;
                    showAddCityView = false;
                    binding.llSearchCity.setVisibility(View.GONE);
                    binding.llSearchKey.setVisibility(View.GONE);
                    binding.rlContextList.setVisibility(View.VISIBLE);
                    binding.ivSearchCity.setImageResource(R.mipmap.select_xia_huang);
                    binding.ivSearchKey.setImageResource(R.mipmap.select_xia_huang);
                } else {
                    showAddKeyView = true;
                    showAddCityView = false;
                    binding.llSearchCity.setVisibility(View.GONE);
                    binding.llSearchKey.setVisibility(View.VISIBLE);
                    binding.rlContextList.setVisibility(View.GONE);
                    binding.ivSearchCity.setImageResource(R.mipmap.select_xia_huang);
                    binding.ivSearchKey.setImageResource(R.mipmap.select_shang_huang);
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
                showAddCityView = false;
                showAddKeyView = false;
                binding.llSearchCity.setVisibility(View.GONE);
                binding.llSearchKey.setVisibility(View.GONE);
                binding.rlContextList.setVisibility(View.VISIBLE);
                binding.ivSearchCity.setImageResource(R.mipmap.select_xia_huang);
                binding.ivSearchKey.setImageResource(R.mipmap.select_xia_huang);
                binding.tvSearchCity.setText(showText(1, addCities));
                filterText.clear();
                filterText.addAll(addCities);
                filterText.addAll(addKeys);
                searchInformation(user.getUS(), user.getPW(), user.getKY(), "0", doProvince, doCity,
                        CityListToString(addCities), "", CityListToString(addKeys));
                break;
            case R.id.btn_key_search:
                String keyText = binding.etSarchKey.getText().toString();
                if (TextUtils.isEmpty(keyText)) {
                    showMessage("内容不能为空");
                    return;
                }
                if (keyText.contains(",")) {
                    String[] keys = keyText.split(",");
                    for (int i = 0; i < keys.length; i++) {
                        addKeys.add(keys[i]);
                    }
                } else if (keyText.contains("，")) {
                    String[] keys = keyText.split("，");
                    for (int i = 0; i < keys.length; i++) {
                        addKeys.add(keys[i]);
                    }
                } else {
                    addKeys.add(keyText);
                }
                keyTexts.add(0, keyText);
                UseInfoManager.putStringArraylist(getContext(), "Record", keyTexts);
                showAddKeyView = false;
                showAddCityView = false;
                binding.llSearchCity.setVisibility(View.GONE);
                binding.llSearchKey.setVisibility(View.GONE);
                binding.rlContextList.setVisibility(View.VISIBLE);
                binding.ivSearchCity.setImageResource(R.mipmap.select_xia_huang);
                binding.ivSearchKey.setImageResource(R.mipmap.select_xia_huang);
                binding.tvSearchKey.setText(keyText);
                searchInformation(user.getUS(), user.getPW(), user.getKY(), "0", doProvince, doCity,
                        CityListToString(addCities), "", CityListToString(addKeys));
                break;
            case R.id.btn_search_key_cancle:
                addKeys.clear();
                binding.llSearchCity.setVisibility(View.GONE);
                binding.llSearchKey.setVisibility(View.GONE);
                binding.rlContextList.setVisibility(View.VISIBLE);
                binding.ivSearchCity.setImageResource(R.mipmap.select_xia_huang);
                binding.ivSearchKey.setImageResource(R.mipmap.select_xia_huang);
                binding.tvSearchKey.setText("二级搜索");
                searchInformation(user.getUS(), user.getPW(), user.getKY(), "0", doProvince, doCity,
                        CityListToString(addCities), "", CityListToString(addKeys));
                break;
            case R.id.iv_call:
                String phone = (String) view.getTag();
                showPhone(phone);
                break;

        }
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
        dialogCall.showAtLocation(binding.llPeripheryMain, Gravity.CENTER, 0, 0);
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
        searchProvinces = provinceIBaseDao.queryAll();
        binding.gvCityList.setAdapter(searchProvinceAdapter);
        searchProvinceAdapter.setDatas(1, searchProvinces);
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
                        binding.gvCityList.setAdapter(searchZoneAdapter);
                        searchZoneAdapter.setDatas(3, searchZones);
                    } else {
                        searchCities = cityIBaseDao.query("ProID=?", new String[]{province.getProSort()});
                        binding.gvCityList.setAdapter(searchCityAdapter);
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
                        binding.gvCityList.setAdapter(searchZoneAdapter);
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

    private void searchInformation(String USER, String PASS, String KEYY,
                                   String ID, String PROV, String CITY,
                                   String INCITY, String INPHONE,
                                   String INFOR) {
        if (TextUtils.isEmpty(INCITY) && TextUtils.isEmpty(INFOR)) {
            AuthorityType = "QB";
        } else {
            AuthorityType = "SS";
        }
        HttpUtil.getInstance().searchInformation(USER, PASS, KEYY, ID, PROV, CITY, INCITY, "货", INPHONE, INFOR, new HttpCallBack<Information>() {
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Information.SearchINFOBean bean = searchINFOBeans.get(position);
        if (UseInfoManager.getBoolean(getContext(), "checkAuthority", true)) {
            Intent intent = new Intent(getContext(), InformationDetailsActivity.class);
            intent.putExtra("Details", bean);
            startActivity(intent);
            checkAuthority(user.getUS(), user.getPW(), user.getKY(),
                    user.getPR(), user.getCT(), bean.getXH(), AuthorityType, bean);
        } else {
            showMessage(UseInfoManager.getString(getContext(), "checkAuthorityMsg"));
        }
    }

    /**
     * 检查用户权限
     */
    private void checkAuthority(String NetID, String PWord, String key, String PR, String CT, String XH, String QC, final Information.SearchINFOBean bean) {
        showLoading();
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

    @Override
    public void onRefresh() {
        isLoad = false;
        searchInformation(user.getUS(), user.getPW(), user.getKY(), "0", doProvince, doCity,
                "", "", "");
    }

    @Override
    public void onLoad() {
        isLoad = true;
        binding.reRefresh.addFooterView();
        binding.reRefresh.showLoading();
        searchInformation(user.getUS(), user.getPW(), user.getKY(), searchINFOBeans.get(searchINFOBeans.size() - 1).getID(),
                doProvince, doCity, "", "", "");
    }

    private class InformationHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    binding.reRefresh.setRefreshing(false);
                    showMessage("获取失败！");
                    searchINFOBeans.clear();
                    adapter.clear();
                    binding.tvNotData.setVisibility(View.VISIBLE);
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
                    adapter.setFilter(filterText);
                    break;
            }
        }
    }
}
