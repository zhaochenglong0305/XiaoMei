package com.lit.xiaomei;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
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
        addView();
    }

    @Override
    public void onRightClick(View view) {
        super.onRightClick(view);
        setResult(1, new Intent().putExtra("Text", binding.etContent.getText().toString()));
        finish();
    }

    private void addView() {
        binding.warpLayout.removeAllViews();
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
            binding.warpLayout.addView(view);
        }
    }
}
