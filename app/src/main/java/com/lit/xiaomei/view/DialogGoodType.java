package com.lit.xiaomei.view;

import android.app.Activity;
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

import com.lit.xiaomei.adapter.AttributeAdapter;

/**
 * Created by Administrator on 2018/2/28.
 */

public class DialogGoodType extends PopupWindow implements View.OnClickListener {
    private List<String> goodType = new ArrayList<>();
    private List<String> goodTypeSelects = new ArrayList<>();
    private Activity context;
    private AttributeAdapter goodTypeAdapter;
    private GridView goodTypeGrid;
    private TextView cancel;
    private TextView ok;
    private OnGoodTypeListener listener;
    private String goodTypeR = "";

    public DialogGoodType(Activity context) {
        super(context);
        initData();
        this.context = context;
        View view = View.inflate(context, R.layout.dialog_good_type, null);
        goodTypeAdapter = new AttributeAdapter(false,context, true, goodType);
        goodTypeGrid = (GridView) view.findViewById(R.id.gv_good_type);
        cancel = (TextView) view.findViewById(R.id.tv_cancel);
        ok = (TextView) view.findViewById(R.id.tv_ok);
        goodTypeGrid.setAdapter(goodTypeAdapter);
        goodTypeGrid.setOnItemClickListener(new OnGoodTypeItemClickListener());
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

    private void initData(){
        goodType.add("普货");
        goodType.add("重货");
        goodType.add("泡货");
        goodType.add("设备");
        goodType.add("配件");
        goodType.add("百货");
        goodType.add("建材");
        goodType.add("食品");
        goodType.add("饮料");
        goodType.add("化工");
        goodType.add("农药");
        goodType.add("水果");
        goodType.add("蔬菜");
        goodType.add("木材");
        goodType.add("煤炭");
        goodType.add("石材");
        goodType.add("瓷砖");
        goodType.add("粮食");
        goodType.add("树苗");
        goodType.add("其他");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ok:
                if (goodTypeSelects.size() != 0) {
                    listener.onClick(goodTypeSelects.get(0));
                } else {
                    listener.onClick("");
                }
                dismiss();
                break;
        }

    }


    private class OnGoodTypeItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            goodTypeSelects.clear();
            goodTypeSelects.add(goodType.get(i));
            goodTypeAdapter.selects(goodTypeSelects);
            ok.setVisibility(View.VISIBLE);
        }
    }

    public interface OnGoodTypeListener {
        void onClick(String goodType);
    }

    public void setOnGoodTypeListener(OnGoodTypeListener listener) {
        this.listener = listener;
    }

}
