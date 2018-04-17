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
    private String[] goodType = {"普货", "重货", "泡货", "设备", "配件", "百货", "建材", "食品", "饮料", "化工", "农药", "水果", "蔬菜", "木材", "煤炭", "石材", "瓷砖", "其他"};
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
        this.context = context;
        View view = View.inflate(context, R.layout.dialog_good_type, null);
        goodTypeAdapter = new AttributeAdapter(context, true, goodType);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ok:
                if (goodTypeSelects.size() != 0) {
                    listener.onClick(goodTypeSelects.get(0));
                }else{
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
            goodTypeSelects.add(goodType[i]);
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
