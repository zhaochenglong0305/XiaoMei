package com.lit.xiaomei.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lit.xiaomei.R;

import java.util.ArrayList;
import java.util.List;

import com.lit.xiaomei.bean.City;
import com.lit.xiaomei.bean.Province;
import com.lit.xiaomei.bean.Zone;

/**
 * Created by Adminis on 2018/1/26.
 */

public class CityAdapter<T> extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<T> citys = new ArrayList<>();
    // 1：省，2：市，3：区
    private int CityType = 0;
    private String select = "";

    public CityAdapter(Context context, int CityType, List<T> citys) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.citys = citys;
        this.CityType = CityType;

    }

    @Override
    public int getCount() {
        return citys.size();
    }

    @Override
    public Object getItem(int position) {
        return citys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.adapter_information_city, viewGroup, false);//加载布局
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.tv_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        switch (CityType) {
            case 1:
                Province province = (Province) citys.get(position);
                holder.text.setText(province.getProName());
                if (TextUtils.equals(select, province.getProName())) {
                    holder.text.setTextColor(context.getResources().getColor(R.color.cFD933C));
                    holder.text.setBackgroundResource(R.drawable.boder_fillet_city_select);
                }else {
                    holder.text.setTextColor(context.getResources().getColor(R.color.black));
                    holder.text.setBackgroundResource(R.drawable.boder_fillet_city);
                }
                break;
            case 2:
                City city = (City) citys.get(position);
                holder.text.setText(city.getCityName());
                if (TextUtils.equals(select, city.getCityName())) {
                    holder.text.setTextColor(context.getResources().getColor(R.color.cFD933C));
                    holder.text.setBackgroundResource(R.drawable.boder_fillet_city_select);
                }else {
                    holder.text.setTextColor(context.getResources().getColor(R.color.black));
                    holder.text.setBackgroundResource(R.drawable.boder_fillet_city);
                }
                break;
            case 3:
                Zone zone = (Zone) citys.get(position);
                String zoneName = zone.getZoneName();
                holder.text.setText(zoneName);
                if (TextUtils.equals(select, zoneName)) {
                    holder.text.setTextColor(context.getResources().getColor(R.color.cFD933C));
                    holder.text.setBackgroundResource(R.drawable.boder_fillet_city_select);
                }else {
                    holder.text.setTextColor(context.getResources().getColor(R.color.black));
                    holder.text.setBackgroundResource(R.drawable.boder_fillet_city);
                }
                break;
            default:
                holder.text.setText("");
                break;
        }

        return view;
    }

    public void setDatas(int CityType, List<T> citys) {
        this.citys = citys;
        this.CityType = CityType;
        this.select = "";
        notifyDataSetChanged();
    }

    public void setSelect(String select) {
        this.select = select;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView text;
    }
}
