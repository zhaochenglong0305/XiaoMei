package com.lit.xiaomei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lit.xiaomei.R;

import java.util.ArrayList;
import java.util.List;

import com.lit.xiaomei.bean.Drivers;
import com.lit.xiaomei.bean.Information;

/**
 * Created by Administrator on 2018/1/27.
 */

public class TubeCarAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<Information.SearchINFOBean> searchINFOBeans = new ArrayList<>();
    private View.OnClickListener listener;

    public TubeCarAdapter(Context context, List<Information.SearchINFOBean> searchINFOBeans, View.OnClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.searchINFOBeans = searchINFOBeans;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return searchINFOBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return searchINFOBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.adapter_information, viewGroup, false);//加载布局
            holder = new ViewHolder();
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            holder.tv_information = (TextView) view.findViewById(R.id.tv_information);
            holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            holder.tv_huozhan = (TextView) view.findViewById(R.id.tv_huozhan);
            holder.iv_call = (ImageView) view.findViewById(R.id.iv_call);
            holder.ll_information = (LinearLayout) view.findViewById(R.id.ll_information);
            holder.dedicatedIcon = (TextView) view.findViewById(R.id.tv_dedicated_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Information.SearchINFOBean searchINFOBean = searchINFOBeans.get(i);
        String timet = searchINFOBean.getDT();
        if (timet.contains(" ")) {
            String[] timets = timet.split(" ");
            if (timets.length > 1) {
                String ttt = timets[1];
                holder.tv_time.setText(ttt.substring(0, ttt.length() - 3));
            }
        } else {
            holder.tv_time.setText(timet);
        }
        holder.tv_information.setText(searchINFOBean.getSF() + "出发：" + searchINFOBean.getMS());
        holder.iv_call.setTag(searchINFOBean.getPH());
        holder.iv_call.setOnClickListener(listener);
        return view;
    }
    public void clear() {
        this.searchINFOBeans.clear();
        notifyDataSetChanged();
    }
    public void addListMsg(List<Information.SearchINFOBean> searchINFOBeans) {
        this.searchINFOBeans.addAll(searchINFOBeans);
        notifyDataSetChanged();
    }
    public void setData(List<Information.SearchINFOBean> searchINFOBeans) {
        this.searchINFOBeans = searchINFOBeans;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tv_time, tv_information, tv_huozhan, tv_phone, dedicatedIcon;
        ImageView iv_call;
        LinearLayout ll_information;
    }
}
