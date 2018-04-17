package com.lit.xiaomei.view;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fyjr.baselibrary.utils.ToastUtil;
import com.lit.xiaomei.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.lit.xiaomei.adapter.CityAdapter;
import com.lit.xiaomei.bean.City;
import com.lit.xiaomei.bean.DetecatedInformation;
import com.lit.xiaomei.bean.GlobalVariable;
import com.lit.xiaomei.bean.Province;
import com.lit.xiaomei.bean.Zone;
import com.lit.xiaomei.utils.DataBaseUtils.CityDbHandler;
import com.lit.xiaomei.utils.DataBaseUtils.DaoFactory;
import com.lit.xiaomei.utils.DataBaseUtils.DbSqlite;
import com.lit.xiaomei.utils.DataBaseUtils.IBaseDao;

/**
 * Created by Administrator on 2018/2/26.
 */

public class DialogADedicatedLine extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Activity context;
    private TextView ok;
    private TextView cityFrom;
    private TextView cityTo;
    private RelativeLayout cityToLayout;
    private LinearLayout selectCityLayout;
    private ImageView toIcon;
    private LinearLayout dedcatedLineLayout;
    private TextView cityText;
    private TextView cityBack;
    private GridView cityGrid;
    private ArrayList<String> texts = new ArrayList<>();
    private OnSetFinishListener listener;
    private IBaseDao<Province> provinceIBaseDao;
    private IBaseDao<City> cityIBaseDao;
    private IBaseDao<Zone> zoneIBaseDao;
    private List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private List<Zone> zones = new ArrayList<>();
    private CityAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private CityAdapter zoneAdapter;
    private int searchCityType = 1;
    //所选的省
    private String selectProvince = "";
    //所选的市
    private String selectCity = "";
    //所选的区
    private String selectZone = "";
    private boolean isShowSelectCity = false;
    private List<DetecatedInformation> detecatedInformations = new ArrayList<>();
    private GridView dedicatedList;
    private DedicatedAdapter adapter;
    private Button dedicatedCar;
    private Button dedicatedGoods;
    private boolean isAdd = true;


    public DialogADedicatedLine(final Activity context, String from) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.dialog_a_dedicated_line, null);
        ok = (TextView) view.findViewById(R.id.tv_dialog_ok);
        cityFrom = (TextView) view.findViewById(R.id.tv_dedicated_line_from_text);
        cityFrom.setText(from);
        cityTo = (TextView) view.findViewById(R.id.tv_dedicated_line_to_text);
        cityToLayout = (RelativeLayout) view.findViewById(R.id.rl_dedicated_line_to);
        selectCityLayout = (LinearLayout) view.findViewById(R.id.ll_select_city);
        dedicatedList = (GridView) view.findViewById(R.id.gv_dedcated_msg);
        adapter = new DedicatedAdapter();
        dedicatedList.setAdapter(adapter);
        toIcon = (ImageView) view.findViewById(R.id.iv_line_to_right_icon);
        dedcatedLineLayout = (LinearLayout) view.findViewById(R.id.ll_dedicated_line);
        dedicatedCar = (Button) view.findViewById(R.id.btn_dedicate_car);
        dedicatedGoods = (Button) view.findViewById(R.id.btn_dedicate_goods);
        cityText = (TextView) view.findViewById(R.id.tv_selected_city);
        cityBack = (TextView) view.findViewById(R.id.tv_city_back);
        cityGrid = (GridView) view.findViewById(R.id.gv_city_grid);
        provinceAdapter = new CityAdapter(context, 1, provinces);
        cityAdapter = new CityAdapter(context, 2, cities);
        zoneAdapter = new CityAdapter(context, 3, zones);
        ok.setOnClickListener(this);
        cityGrid.setOnItemClickListener(this);
        cityBack.setOnClickListener(this);
        cityToLayout.setOnClickListener(this);
        dedicatedCar.setOnClickListener(this);
        dedicatedGoods.setOnClickListener(this);
        initDateBase();


        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置弹出窗体的宽和高
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = context.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//        ViewGroup.LayoutParams.MATCH_PARENT
        // 设置外部可点击
        this.setOutsideTouchable(false);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (searchCityType) {
            case 1:
                Province province = provinces.get(i);
                selectProvince = province.getProName();
                if (TextUtils.equals(selectProvince, "北京") ||
                        TextUtils.equals(selectProvince, "天津") ||
                        TextUtils.equals(selectProvince, "上海") ||
                        TextUtils.equals(selectProvince, "重庆")) {
                    cities = cityIBaseDao.query("ProID=?", new String[]{province.getProSort()});
                    zones = zoneIBaseDao.query("CityID=?", new String[]{cities.get(0).getCitySort()});
                    cityGrid.setAdapter(zoneAdapter);
                    zoneAdapter.setDatas(3, zones);
                } else {
                    cities = cityIBaseDao.query("ProID=?", new String[]{province.getProSort()});
                    cityGrid.setAdapter(cityAdapter);
                    cityAdapter.setDatas(2, cities);
                }
                cityText.setText("当前所在：" + selectProvince);
                cityBack.setVisibility(View.VISIBLE);
                searchCityType = 2;
                break;
            case 2:
                if (TextUtils.equals(selectProvince, "北京") ||
                        TextUtils.equals(selectProvince, "天津") ||
                        TextUtils.equals(selectProvince, "上海") ||
                        TextUtils.equals(selectProvince, "重庆")) {
                    Zone zone = zones.get(i);
                    selectCity = zone.getZoneName();
                    cityAdapter.setSelect(selectCity);
                    cityTo.setText(selectCity);
                    isShowSelectCity = false;
                    selectCityLayout.setVisibility(View.GONE);
                    dedcatedLineLayout.setVisibility(View.VISIBLE);
                    toIcon.setImageResource(R.mipmap.select_xia_huang);
                } else {
                    City city = cities.get(i);
                    selectCity = city.getCityName();
                    zones = zoneIBaseDao.query("CityID=?", new String[]{city.getCitySort()});
                    cityGrid.setAdapter(zoneAdapter);
                    zoneAdapter.setDatas(3, zones);
                    cityText.setText(cityText.getText().toString() + " — " + selectCity);
                    searchCityType = 3;
                }
                break;
            case 3:
                Zone zone = zones.get(i);
                selectCity = zone.getZoneName();
                cityAdapter.setSelect(selectCity);
                cityTo.setText(selectCity);
                isShowSelectCity = false;
                selectCityLayout.setVisibility(View.GONE);
                dedcatedLineLayout.setVisibility(View.VISIBLE);
                toIcon.setImageResource(R.mipmap.select_xia_huang);
                break;
        }
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnSetFinishListener {
        public void onClick(List<DetecatedInformation> detecatedInformations);
    }

    public void setOnSetFinishListener(OnSetFinishListener onSearchListener) {
        this.listener = onSearchListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_city_back:
                switch (searchCityType) {
                    case 1:
                        break;
                    case 2:
                        cityText.setText("选择省份");
                        cityGrid.setAdapter(provinceAdapter);
                        cityBack.setVisibility(View.GONE);
                        searchCityType = 1;
                        break;
                    case 3:
                        cityText.setText("当前所在：" + selectProvince);
                        cityGrid.setAdapter(cityAdapter);
                        searchCityType = 2;
                        break;
                }
                break;
            case R.id.rl_dedicated_line_to:
                if (isShowSelectCity) {
                    isShowSelectCity = false;
                    selectCityLayout.setVisibility(View.GONE);
                    dedcatedLineLayout.setVisibility(View.VISIBLE);
                    toIcon.setImageResource(R.mipmap.select_xia_huang);
                } else {
                    selectCityLayout.setVisibility(View.VISIBLE);
                    dedcatedLineLayout.setVisibility(View.GONE);
                    toIcon.setImageResource(R.mipmap.select_shang_huang);
                    isShowSelectCity = true;
                }
                break;
            case R.id.btn_dedicate_car:
                if (TextUtils.isEmpty(selectCity)) {
                    ToastUtil.showToast(context, "请先选择目的地！");
                    return;
                }
                isAdd = true;
                DetecatedInformation car = new DetecatedInformation();
                car.setTo(selectCity);
                car.setType("车源");
                for (DetecatedInformation information : detecatedInformations) {
                    if (TextUtils.equals(information.getTo(), car.getTo()) && TextUtils.equals(information.getType(), car.getType())) {
                        ToastUtil.showToast(context, "请勿重复添加！");
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    detecatedInformations.add(car);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_dedicate_goods:
                if (TextUtils.isEmpty(selectCity)) {
                    ToastUtil.showToast(context, "请先选择目的地！");
                    return;
                }
                isAdd = true;
                DetecatedInformation good = new DetecatedInformation();
                good.setTo(selectCity);
                good.setType("货源");
                for (DetecatedInformation information : detecatedInformations) {
                    if (TextUtils.equals(information.getTo(), good.getTo()) && TextUtils.equals(information.getType(), good.getType())) {
                        ToastUtil.showToast(context, "请勿重复添加！");
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    detecatedInformations.add(good);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_dialog_ok:
                listener.onClick(detecatedInformations);
                dismiss();
                break;
        }
    }

    private void initDateBase() {
        if (!new File(GlobalVariable.DateBaseMsg.DB_PATH).exists()) {
            try {
                FileOutputStream out = new FileOutputStream(GlobalVariable.DateBaseMsg.DB_PATH);
                InputStream in = context.getAssets().open("city.db");
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
        SQLiteDatabase db = new CityDbHandler().getDataBase(context);
        DbSqlite dbSqlite = new DbSqlite(context, db);
        provinceIBaseDao = DaoFactory.createGenericDao(dbSqlite, Province.class);
        cityIBaseDao = DaoFactory.createGenericDao(dbSqlite, City.class);
        zoneIBaseDao = DaoFactory.createGenericDao(dbSqlite, Zone.class);
        provinces = provinceIBaseDao.queryAll();
        cityGrid.setAdapter(provinceAdapter);
        provinceAdapter.setDatas(1, provinces);
    }

    private class DedicatedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return detecatedInformations.size();
        }

        @Override
        public Object getItem(int i) {
            return detecatedInformations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_dedicated_list, viewGroup, false);
            TextView to = (TextView) view.findViewById(R.id.tv_dedicated_to);
            TextView type = (TextView) view.findViewById(R.id.tv_dedicated_type);
            ImageView del = (ImageView) view.findViewById(R.id.iv_dedicated_del);
            final DetecatedInformation detecatedInformation = detecatedInformations.get(i);
            to.setText(detecatedInformation.getTo());
            type.setText(detecatedInformation.getType());
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detecatedInformations.remove(detecatedInformation);
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
