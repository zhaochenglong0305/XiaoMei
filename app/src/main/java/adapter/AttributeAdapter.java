package adapter;

import android.app.Activity;
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

import bean.City;
import bean.Province;
import bean.Zone;

/**
 * Created by Adminis on 2018/1/26.
 */

public class AttributeAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity context;
    private String[] attributes = {};
    private String select = "";
    private boolean multiselect = false;

    public AttributeAdapter(Activity context, String[] attributes) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.attributes = attributes;
    }

    @Override
    public int getCount() {
        return attributes.length;
    }

    @Override
    public Object getItem(int position) {
        return attributes[position];
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
        holder.text.setText(attributes[position]);
        if (TextUtils.equals(select, attributes[position])) {
            holder.text.setTextColor(context.getResources().getColor(R.color.cFD933C));
            holder.text.setBackgroundResource(R.drawable.boder_fillet_city_select);
        } else {
            if (!multiselect) {
                holder.text.setTextColor(context.getResources().getColor(R.color.c888888));
                holder.text.setBackgroundResource(R.drawable.boder_fillet_city);
            }
        }
        return view;
    }

    public void isMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }

    public void setSelect(String select) {
        this.select = select;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView text;
    }
}
