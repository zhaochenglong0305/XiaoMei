package com.lit.xiaomei.fragment.goods;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.lit.xiaomei.adapter.CityAdapter;
import com.lit.xiaomei.adapter.InformationAdapter;
import com.lit.xiaomei.bean.City;
import com.lit.xiaomei.bean.GlobalVariable;
import com.lit.xiaomei.bean.Information;
import com.lit.xiaomei.bean.Province;
import com.lit.xiaomei.bean.Zone;
import com.lit.xiaomei.databinding.FragmentPeripheryBinding;
import com.lit.xiaomei.utils.DataBaseUtils.CityDbHandler;
import com.lit.xiaomei.utils.DataBaseUtils.DaoFactory;
import com.lit.xiaomei.utils.DataBaseUtils.DbSqlite;
import com.lit.xiaomei.utils.DataBaseUtils.IBaseDao;

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
    private List<String> searchEdits = new ArrayList<>();

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
        initDateBase();
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
                    binding.ivSearchCity.setImageResource(R.mipmap.select_shang_huang);
                    binding.ivSearchKey.setImageResource(R.mipmap.select_xia_huang);
                }

                break;
            case R.id.rl_search_key:
                if (showAddKeyView) {
                    showAddKeyView = false;
                    showAddCityView = true;
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

        }
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
                                   String INXH, String PROV, String CITY,
                                   String INCITY, String INPHONE,
                                   String INFOR) {
        if (TextUtils.isEmpty(INCITY) && TextUtils.isEmpty(INFOR)) {
            AuthorityType = "QB";
        } else {
            AuthorityType = "SS";
        }
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoad() {

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
