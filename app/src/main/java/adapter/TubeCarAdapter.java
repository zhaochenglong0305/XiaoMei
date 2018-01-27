package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lit.xiaomei.R;

import java.util.ArrayList;
import java.util.List;

import bean.Drivers;

/**
 * Created by Administrator on 2018/1/27.
 */

public class TubeCarAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<Drivers.ListDataBean> listDataBeans = new ArrayList<>();

    public TubeCarAdapter(Context context, List<Drivers.ListDataBean> drivers) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.listDataBeans = drivers;
    }

    @Override
    public int getCount() {
        return listDataBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return listDataBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.adapter_tube_car, viewGroup, false);//加载布局
            holder = new ViewHolder();
            holder.driverName = (TextView) view.findViewById(R.id.tv_driver_name);
            holder.carNumber = (TextView) view.findViewById(R.id.tv_car_number);
            holder.phone = (TextView) view.findViewById(R.id.tv_phone);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Drivers.ListDataBean driver = listDataBeans.get(i);
        holder.driverName.setText(driver.getName());
        holder.carNumber.setText(driver.getLicensePlate());
        holder.phone.setText(driver.getTel1());
        return view;
    }

    public void setData(List<Drivers.ListDataBean> drivers) {
        this.listDataBeans = drivers;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView driverName, carNumber, phone;
    }
}
