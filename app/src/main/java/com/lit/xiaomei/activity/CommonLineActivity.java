package com.lit.xiaomei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.adapter.SelectCityAdapter;
import com.lit.xiaomei.bean.Line;
import com.lit.xiaomei.databinding.ActivityCommonLineBinding;
import com.lit.xiaomei.manager.UseInfoManager;
import com.lit.xiaomei.view.DialogReleaseSelectCity;

import java.util.ArrayList;
import java.util.List;

public class CommonLineActivity extends BaseActivity<ActivityCommonLineBinding> implements View.OnClickListener, AdapterView.OnItemClickListener {
    private SelectCityAdapter fromAdapter;
    private SelectCityAdapter toAdapter;
    private List<String> fromSelect = new ArrayList<>();
    private List<String> toSelect = new ArrayList<>();
    private int type = 0;
    private ArrayList<Line> lines = new ArrayList<>();
    private LineListAdapter lineListAdapter;
    private boolean isShowUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_common_line);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("常用路线");
        setTitleTextColor("#ffffff");
        fromAdapter = new SelectCityAdapter(this, fromSelect, new OnFromCityClickListener());
        toAdapter = new SelectCityAdapter(this, toSelect, new OnToCityClickListener());
        lineListAdapter = new LineListAdapter();
        if (UseInfoManager.getLineArraylist(this) != null) {
            lines = UseInfoManager.getLineArraylist(this);
        }
        if (lines.size() > 0) {
            binding.llAddLine.setVisibility(View.GONE);
            binding.llLineList.setVisibility(View.VISIBLE);
        }
        binding.tvLineNum.setText(lines.size() + "");
        binding.gvFromCity.setAdapter(fromAdapter);
        binding.gvToCity.setAdapter(toAdapter);
        binding.lvLineList.setAdapter(lineListAdapter);
        binding.lvLineList.setOnItemClickListener(this);
        binding.btnLineAdd.setOnClickListener(this);
        binding.tvUpdate.setOnClickListener(this);
        binding.btnSelectLineCity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_line_add:
                if (lines.size() == 0) {
                    addLine();
                } else {
                    if (fromSelect.size() != 0 || toSelect.size() != 0) {
                        addLine();
                    } else {
                        binding.llAddLine.setVisibility(View.GONE);
                        binding.llLineList.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.tv_update:
                isShowUpdate = true;
                lineListAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_select_line_city:
                if (lines.size() == 5) {
                    showMessage("最多添加5条线路");
                    return;
                }
                binding.llAddLine.setVisibility(View.VISIBLE);
                binding.llLineList.setVisibility(View.GONE);
                break;
        }
    }

    private void addLine() {
        if (fromSelect.size() == 0) {
            showMessage("出发城市不能为空！");
            return;
        }
        if (toSelect.size() == 0) {
            showMessage("到达城市不能为空！");
            return;
        }
        lines.add(new Line(fromSelect, toSelect));
        UseInfoManager.putLineArraylist(this, lines);
        binding.llAddLine.setVisibility(View.GONE);
        binding.llLineList.setVisibility(View.VISIBLE);
        binding.tvLineNum.setText(lines.size() + "");
        fromSelect.clear();
        toSelect.clear();
        fromAdapter.notifyDataSetChanged();
        toAdapter.notifyDataSetChanged();
        lines = UseInfoManager.getLineArraylist(this);
        lineListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(this, InformationForLineActivity.class);
        intent.putExtra("line", lines.get(position));
        startActivity(intent);
    }

    private class OnFromCityClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_city:
                    String delCity = (String) view.getTag();
                    if (fromSelect.contains(delCity)) {
                        fromSelect.remove(delCity);
                        fromAdapter.setData(fromSelect);
                    }
                    break;
                case R.id.rl_do:
                    if (fromSelect.size() >= 5) {
                        showMessage("最多添加5个城市");
                        return;
                    }
                    type = 1;
                    showCityDialog();
                    break;
            }


        }
    }

    private class OnToCityClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_city:
                    String delCity = (String) view.getTag();
                    if (toSelect.contains(delCity)) {
                        toSelect.remove(delCity);
                        toAdapter.setData(toSelect);
                    }
                    break;
                case R.id.rl_do:
                    if (toSelect.size() >= 5) {
                        showMessage("最多添加5个城市");
                        return;
                    }
                    type = 2;
                    showCityDialog();
                    break;
            }

        }
    }

    private void showCityDialog() {
        DialogReleaseSelectCity dialogReleaseSelectCity = new DialogReleaseSelectCity(this);
        dialogReleaseSelectCity.setSelectCityListener(new DialogReleaseSelectCity.OnCitySeectListener() {
            @Override
            public void onClick(String select) {
                switch (type) {
                    case 1:
                        fromSelect.add(select);
                        fromAdapter.setData(fromSelect);
                        break;
                    case 2:
                        toSelect.add(select);
                        toAdapter.setData(toSelect);
                        break;
                }
            }
        });
        dialogReleaseSelectCity.showAtLocation(binding.llCommonMain, Gravity.CENTER, 0, 0);
    }

    private class LineListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lines.size();
        }

        @Override
        public Object getItem(int position) {
            return lines.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(CommonLineActivity.this).inflate(R.layout.adapter_line_list, parent, false);
            TextView from = convertView.findViewById(R.id.tv_from);
            TextView to = convertView.findViewById(R.id.tv_to);
            ImageView del = convertView.findViewById(R.id.iv_lines_del);
            String fromText = "";
            String toText = "";
            for (int i = 0; i < lines.get(position).getFromCities().size(); i++) {
                if (i != lines.get(position).getFromCities().size() - 1) {
                    fromText = fromText + lines.get(position).getFromCities().get(i) + "/";
                } else {
                    fromText = fromText + lines.get(position).getFromCities().get(i);
                }
            }
            for (int i = 0; i < lines.get(position).getToCities().size(); i++) {
                if (i != lines.get(position).getToCities().size() - 1) {
                    toText = toText + lines.get(position).getToCities().get(i) + "/";
                } else {
                    toText = toText + lines.get(position).getToCities().get(i);
                }
            }
            from.setText(fromText);
            to.setText(toText);
            if (isShowUpdate) {
                del.setVisibility(View.VISIBLE);
            } else {
                del.setVisibility(View.GONE);
            }
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lines.remove(lines.get(position));
                    UseInfoManager.putLineArraylist(CommonLineActivity.this, lines);
                    binding.tvLineNum.setText(lines.size() + "");
                    isShowUpdate = false;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

}
