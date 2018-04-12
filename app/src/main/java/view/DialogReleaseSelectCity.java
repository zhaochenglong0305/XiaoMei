package view;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lit.xiaomei.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import adapter.CityAdapter;
import bean.City;
import bean.GlobalVariable;
import bean.Province;
import bean.Zone;
import utils.DataBaseUtils.CityDbHandler;
import utils.DataBaseUtils.DaoFactory;
import utils.DataBaseUtils.DbSqlite;
import utils.DataBaseUtils.IBaseDao;

/**
 * Created by Administrator on 2018/2/26.
 */

public class DialogReleaseSelectCity extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Activity context;
    private TextView cityText;
    private TextView cityBack;
    private GridView cityGrid;
    private TextView cancel;
    private TextView ok;
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
    private OnCitySeectListener onCitySeectListener;

    public DialogReleaseSelectCity(Activity context) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.dialog_release_select_city, null);
        cityText = (TextView) view.findViewById(R.id.tv_selected_city);
        cityBack = (TextView) view.findViewById(R.id.tv_city_back);
        cityGrid = (GridView) view.findViewById(R.id.gv_city_grid);
        cancel = (TextView) view.findViewById(R.id.tv_selected_city_cancel);
        ok = (TextView) view.findViewById(R.id.tv_selected_city_ok);
        provinceAdapter = new CityAdapter(context, 1, provinces);
        cityAdapter = new CityAdapter(context, 2, cities);
        zoneAdapter = new CityAdapter(context, 3, zones);
        cityGrid.setOnItemClickListener(this);
        cityBack.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
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
            case R.id.tv_selected_city_cancel:
                dismiss();
                break;
            case R.id.tv_selected_city_ok:
                onCitySeectListener.onClick(selectCity);
                dismiss();
                break;
        }
    }

    /**
     * 回调接口
     */
    public interface OnCitySeectListener {
        void onClick(String city);
    }

    public void setSelectCityListener(OnCitySeectListener onCitySeectListener) {
        this.onCitySeectListener = onCitySeectListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
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
                    zones.add(0, new Zone(selectProvince));
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
                    zoneAdapter.setSelect(selectCity);
                    ok.setVisibility(View.VISIBLE);
                } else {
                    City city = cities.get(i);
                    selectCity = city.getCityName();
                    zones = zoneIBaseDao.query("CityID=?", new String[]{city.getCitySort()});
                    zones.add(0, new Zone(selectCity));
                    cityGrid.setAdapter(zoneAdapter);
                    zoneAdapter.setDatas(3, zones);
                    cityText.setText(cityText.getText().toString() + " — " + selectCity);
                    searchCityType = 3;
                }
                break;
            case 3:
                Zone zone = zones.get(i);
                selectCity = zone.getZoneName();
                zoneAdapter.setSelect(selectCity);
                ok.setVisibility(View.VISIBLE);
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
}