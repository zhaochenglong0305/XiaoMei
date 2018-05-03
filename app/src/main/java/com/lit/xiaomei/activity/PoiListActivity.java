package com.lit.xiaomei.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.fyjr.baselibrary.base.BaseActivity;
import com.lit.xiaomei.R;
import com.lit.xiaomei.databinding.ActivityPoiListBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PoiListActivity extends BaseActivity<ActivityPoiListBinding> {
    private ArrayList<PoiInfo> poiInfos = new ArrayList<>();
    private PoiListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this, R.layout.activity_poi_list);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("周边信息");
        setTitleTextColor("#ffffff");
        if (getIntent().getParcelableArrayListExtra("Pois") != null) {
            poiInfos = getIntent().getParcelableArrayListExtra("Pois");
        }
        adapter = new PoiListAdapter();
        binding.lvPoiList.setAdapter(adapter);
    }

    private class PoiListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return poiInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return poiInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = LayoutInflater.from(PoiListActivity.this).inflate(R.layout.adapter_poi_list, parent, false);
            TextView poiName = (TextView) view.findViewById(R.id.tv_poi_name);
            TextView poiAddress = (TextView) view.findViewById(R.id.tv_poi_address);
            PoiInfo poiInfo = poiInfos.get(position);
            poiName.setText((position + 1) + ", " + poiInfo.name);
            poiAddress.setText(poiInfo.address);
            return view;
        }
    }
}
