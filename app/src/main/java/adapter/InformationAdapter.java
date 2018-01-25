package adapter;

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

import bean.Information;

/**
 * Created by Adminis on 2018/1/25.
 */

public class InformationAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<Information.SearchINFOBean> searchINFOBeans = new ArrayList<>();

    public InformationAdapter(Context context, List<Information.SearchINFOBean> searchINFOBeans) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.searchINFOBeans = searchINFOBeans;
    }

    //    BG:DATA,XH123453PR吉林CT白城CH货源SFMDDT2016-11-03 16:34:34NA洮南万里马货站WHPH04366352756，6594223，15843616188WDRZ0LYhMS洮南168号洮南永茂长春德惠水稻30-32吨每吨100元马上装~hND
    @Override
    public int getCount() {
        return searchINFOBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return searchINFOBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
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
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Information.SearchINFOBean searchINFOBean = searchINFOBeans.get(position);
        holder.tv_information.setText(searchINFOBean.getMS());
        holder.tv_phone.setText(searchINFOBean.getPH());
        holder.tv_huozhan.setText("货站名:" + searchINFOBean.getNA());
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
        return view;
    }

    public void setData(List<Information.SearchINFOBean> searchINFOBeans) {
        this.searchINFOBeans = searchINFOBeans;
        notifyDataSetChanged();
    }

    public void addMsg(Information.SearchINFOBean bean) {
        this.searchINFOBeans.add(0, bean);
        notifyDataSetChanged();
    }

    public void addListMsg(List<Information.SearchINFOBean> searchINFOBeans) {
        this.searchINFOBeans.addAll(searchINFOBeans);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tv_time, tv_information, tv_huozhan, tv_phone;
        ImageView iv_call;
        LinearLayout ll_information;
    }
}
