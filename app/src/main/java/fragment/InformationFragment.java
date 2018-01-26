package fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
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
    private IBaseDao<Province> provinceIBaseDao;
    private IBaseDao<City> cityIBaseDao;
    private IBaseDao<Zone> zoneIBaseDao;
    private boolean isFromLayoutShow = false;
    private CityAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CityAdapter zoneAdapter;
    private boolean isLoad = false;

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
        initDateBase();
        return binding.getRoot();
    }

    @Override
    public void initView() {
        super.initView();
        listDataBean = UseInfoManager.getUser(getContext()).getListData().get(0);
        String informationMsg = CreateSendMsg.createInformationMsg(getActivity(), listDataBean.getPR(), listDataBean.getCT());
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
        binding.rlFrom.setOnClickListener(this);
        provinceAdapter = new CityAdapter(getContext(), 1, provinces);
        cityAdapter = new CityAdapter(getContext(), 2, cities);
        zoneAdapter = new CityAdapter(getContext(), 3, zones);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onRefresh() {
        isLoad = false;
        isStartReceive = false;
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), "", "", listDataBean.getCT(),
                "", "", "", "", "1");
    }

    @Override
    public void onLoad() {
        isLoad = true;
        binding.reRefresh.addFooterView();
        binding.reRefresh.showLoading();
        searchInformation(listDataBean.getUS(), listDataBean.getPW(), listDataBean.getKY(), searchINFOBeans.get(searchINFOBeans.size() - 1).getXH(),
                "", listDataBean.getCT(), "", "", "", "", "1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_from:
                initFromLayout();
                break;
        }

    }

    private class ReceiveMsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("Msg");
            Log.e("long", "InformationFragment获得数据：" + msg);
            Information.SearchINFOBean bean = FormatString.formatInformation(msg);
            if (bean != null && isStartReceive) {
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
                if (isLoad) {
                    if (data.getSearchINFO().size() == 0 || data == null) {
                        binding.reRefresh.setNoMoreData();
                    }else {
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
        binding.gvCity.setAdapter(provinceAdapter);
        provinceAdapter.setDatas(1, provinces);
    }

    private void initFromLayout() {
        if (isFromLayoutShow) {
            isFromLayoutShow = false;
            binding.llSearchLevel1.setVisibility(View.GONE);
            binding.reRefresh.setVisibility(View.VISIBLE);
            binding.ivFromBiao.setImageResource(R.mipmap.select_xia);
        } else {
            isFromLayoutShow = true;
            binding.llSearchLevel1.setVisibility(View.VISIBLE);
            binding.reRefresh.setVisibility(View.GONE);
            binding.ivFromBiao.setImageResource(R.mipmap.select_shang);
        }
    }


}
