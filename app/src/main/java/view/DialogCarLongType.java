package view;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import adapter.AttributeAdapter;

/**
 * Created by Administrator on 2018/2/28.
 */

public class DialogCarLongType extends PopupWindow implements View.OnClickListener {
    private String[] lingDan = {"不是", "是"};
    private List<String> lingDanSelects = new ArrayList<>();
    private String[] carLong = {"不限", "4.2米", "4.5米", "6.2米", "6.8米", "7.2米", "8.2米", "8.6米", "9.6米", "11.7米", "12.5米", "13米", "13.5米", "14米", "17米", "17.5米", "18米"};
    private List<String> carLongSelects = new ArrayList<>();
    private String[] carType = {"不限", "平板", "高栏", "厢式", "高低板", "保温", "冷藏", "危险品"};
    private List<String> carTypeSelects = new ArrayList<>();
    private Activity context;
    private AttributeAdapter lingDanAdapter;
    private AttributeAdapter carLongAdapter;
    private AttributeAdapter carTypeAdapter;
    private GridView lingDanGrid;
    private GridView carLongGrid;
    private GridView carTypeGrid;
    private TextView cancel;
    private TextView ok;
    private OnCarFinishListener listener;
    private boolean isLD = false;

    public DialogCarLongType(Activity context) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.dialog_car_long_type, null);
        lingDanAdapter = new AttributeAdapter(context, false, lingDan);
        lingDanSelects.add(lingDan[0]);
        lingDanAdapter.selects(lingDanSelects);
        carLongAdapter = new AttributeAdapter(context, true, carLong);
        carLongSelects.add(carLong[0]);
        carLongAdapter.selects(carLongSelects);
        carTypeAdapter = new AttributeAdapter(context, true, carType);
        carTypeSelects.add(carType[0]);
        carTypeAdapter.selects(carTypeSelects);
        lingDanGrid = (GridView) view.findViewById(R.id.gv_ling_dan);
        carLongGrid = (GridView) view.findViewById(R.id.gv_car_long);
        carTypeGrid = (GridView) view.findViewById(R.id.gv_car_type);
        cancel = (TextView) view.findViewById(R.id.tv_cancel);
        ok = (TextView) view.findViewById(R.id.tv_ok);
        lingDanGrid.setAdapter(lingDanAdapter);
        carLongGrid.setAdapter(carLongAdapter);
        carTypeGrid.setAdapter(carTypeAdapter);
        lingDanGrid.setOnItemClickListener(new OnLingDanItemClickListener());
        carLongGrid.setOnItemClickListener(new OnCarLongItemClickListener());
        carTypeGrid.setOnItemClickListener(new OnCarTypeItemClickListener());
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
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
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ok:
                String carLongR = "";
                String carTypeR = "";
                if (!carLongSelects.contains("不限")) {
                    for (int i = 0; i < carLongSelects.size(); i++) {
                        if (i != carLongSelects.size() - 1) {
                            carLongR = carLongR + carLongSelects.get(i) + "，";
                        } else {
                            carLongR = carLongR + carLongSelects.get(i);
                        }
                    }
                }
                if (!carTypeSelects.contains("不限")) {
                    for (int i = 0; i < carTypeSelects.size(); i++) {
                        if (i != carLongSelects.size() - 1) {
                            carTypeR = carTypeR + carTypeSelects.get(i) + "，";
                        } else {
                            carTypeR = carTypeR + carTypeSelects.get(i);
                        }
                    }
                }
                listener.onClick(isLD, carLongR, carTypeR);
                dismiss();
                break;
        }

    }

    public interface OnCarFinishListener {
        void onClick(boolean isLD, String carlLong, String carType);
    }

    public void setOnCarFinishListener(OnCarFinishListener listener) {
        this.listener = listener;
    }

    private class OnLingDanItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            lingDanSelects.clear();
            lingDanSelects.add(lingDan[i]);
            lingDanAdapter.selects(lingDanSelects);
            if (i == 0) {
                isLD = false;
            } else {
                isLD = true;
            }
        }
    }

    private class OnCarLongItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                carLongSelects.clear();
                carLongSelects.add(carLong[i]);
            } else {
                if (carLongSelects.contains(carLong[0])) {
                    carLongSelects.remove(carLong[0]);
                    carLongSelects.add(carLong[i]);
                } else if (carLongSelects.contains(carLong[i])) {
                    carLongSelects.remove(carLong[i]);
                } else {
                    carLongSelects.add(carLong[i]);
                }
            }
            carLongAdapter.selects(carLongSelects);
        }
    }

    private class OnCarTypeItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            carTypeSelects.clear();
            carTypeSelects.add(carType[i]);
            carTypeAdapter.selects(carTypeSelects);
        }
    }

}
