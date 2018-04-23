package com.lit.xiaomei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lit.xiaomei.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminis on 2018/4/20.
 */

public class SelectCityAdapter extends BaseAdapter {
    private List<String> cities = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;

    public SelectCityAdapter(Context context, List<String> cities, View.OnClickListener listener) {
        this.context = context;
        this.cities = cities;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return cities.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.adapter_select_city, parent, false);
        RelativeLayout cityLayout = (RelativeLayout) convertView.findViewById(R.id.rl_city);
        TextView cityName = (TextView) convertView.findViewById(R.id.tv_city);
        RelativeLayout doLayout = (RelativeLayout) convertView.findViewById(R.id.rl_do);
        if (cities.size() != 0) {
            if (position == cities.size()) {
                cityLayout.setVisibility(View.GONE);
                doLayout.setVisibility(View.VISIBLE);
            } else {
                String city = cities.get(position);
                cityLayout.setVisibility(View.VISIBLE);
                doLayout.setVisibility(View.GONE);
                cityName.setText(city);
                cityLayout.setTag(city);
            }
        } else {
            cityLayout.setVisibility(View.GONE);
            doLayout.setVisibility(View.VISIBLE);
        }
        cityLayout.setOnClickListener(listener);
        doLayout.setOnClickListener(listener);
        return convertView;
    }

    public void setData(List<String> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }
}
