package com.lit.xiaomei;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.databinding.ActivityEditReleaseMsgBinding;

import java.util.Random;

public class EditReleaseMsgActivity extends BaseActivity<ActivityEditReleaseMsgBinding> {
    /**
     * 显示的文字
     */
    private String[] mDatas = new String[]{"有", "车", "货", "需", "求", "米", "吨", "要", "双桥", "高栏", "箱车", "货到自提", "有车速联系", "有货速联系", "价高急走", "装车就走", "随便装", "求零担", "顺路装"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_edit_release_msg);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("信息");
        setTitleTextColor("#FFFFFF");
        setRightText("完成");
        // 循环添加TextView到容器
        for (int i = 0; i < mDatas.length; i++) {
            final TextView view = new TextView(this);
            view.setText(mDatas[i]);
            view.setTextColor(getResources().getColor(R.color.c595a6e));
            view.setPadding(10, 10, 10, 10);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(15);

            // 设置点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.etContent.setText(binding.etContent.getText().toString() + view.getText().toString());
                    binding.etContent.setSelection(binding.etContent.getText().toString().length());
                }
            });

            // 设置彩色背景
            GradientDrawable normalDrawable = new GradientDrawable();
            normalDrawable.setShape(GradientDrawable.RECTANGLE);
//            int a = 255;
//            int r = 50 + random.nextInt(150);
//            int g = 50 + random.nextInt(150);
//            int b = 50 + random.nextInt(150);
            normalDrawable.setColor(getResources().getColor(R.color.cF3F6FA));

            // 设置按下的灰色背景
            GradientDrawable pressedDrawable = new GradientDrawable();
            pressedDrawable.setShape(GradientDrawable.RECTANGLE);
            pressedDrawable.setColor(Color.WHITE);

            // 背景选择器
            StateListDrawable stateDrawable = new StateListDrawable();
            stateDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
            stateDrawable.addState(new int[]{}, normalDrawable);

            // 设置背景选择器到TextView上
            view.setBackground(stateDrawable);

            binding.flowLayout.addView(view);
        }
    }

    @Override
    public void onRightClick(View view) {
        super.onRightClick(view);
        setResult(1, new Intent().putExtra("Text", binding.etContent.getText().toString()));
        finish();
    }
}
