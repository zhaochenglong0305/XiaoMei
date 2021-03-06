package com.lit.xiaomei.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lit.xiaomei.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adminis on 2018/1/26.
 */

public class AttributeAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private List<String> attributes = new ArrayList<>();
    private boolean isMuchSelect = false;
    private List<String> selects = new ArrayList<>();
    private boolean isShowM = false;


    public AttributeAdapter(boolean isShowM, Activity context, boolean isMuchSelect, List<String> attributes) {
        mInflater = LayoutInflater.from(context);
        this.isShowM = isShowM;
        this.context = context;
        this.isMuchSelect = isMuchSelect;
        this.attributes = attributes;
    }

    @Override
    public int getCount() {
        return attributes.size();
    }

    @Override
    public Object getItem(int position) {
        return attributes.get(0);
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
        String text = attributes.get(position);
        if (isShowM) {
            holder.text.setText(text + "米");
        } else {
            holder.text.setText(text);
        }
        if (isMuchSelect) {
            if (selects.contains(text)) {
                holder.text.setTextColor(context.getResources().getColor(R.color.cFD933C));
                holder.text.setBackgroundResource(R.drawable.boder_fillet_city_select);
            } else {
                holder.text.setTextColor(context.getResources().getColor(R.color.c888888));
                holder.text.setBackgroundResource(R.drawable.boder_fillet_city);
            }
        } else {
            if (TextUtils.equals(selects.get(0), text)) {
                holder.text.setTextColor(context.getResources().getColor(R.color.cFD933C));
                holder.text.setBackgroundResource(R.drawable.boder_fillet_city_select);
            } else {
                holder.text.setTextColor(context.getResources().getColor(R.color.c888888));
                holder.text.setBackgroundResource(R.drawable.boder_fillet_city);
            }
        }
        return view;
    }


    public void selects(List<String> ss) {
        selects = ss;
        notifyDataSetChanged();
    }


    private class ViewHolder {
        TextView text;
    }
}
