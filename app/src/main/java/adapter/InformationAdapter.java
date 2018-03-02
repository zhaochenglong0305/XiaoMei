package adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import utils.StringUtil;

/**
 * Created by Adminis on 2018/1/25.
 */

public class InformationAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<Information.SearchINFOBean> searchINFOBeans = new ArrayList<>();
    private View.OnClickListener listener;
    private List<String> filters = new ArrayList<>();

    public InformationAdapter(Context context, List<Information.SearchINFOBean> searchINFOBeans, View.OnClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.searchINFOBeans = searchINFOBeans;
        this.listener = listener;
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
        String information = StringUtil.formatString(searchINFOBean.getMS());
        if (filters.size() != 0) {
            CharSequence sequence = matcherSearchText(R.color.cFD933C, information, filters);
            holder.tv_information.setText(searchINFOBean.getSF() + "出发：" + sequence);
        } else {
            holder.tv_information.setText(searchINFOBean.getSF() + "出发：" + information);
        }
        holder.tv_phone.setText("电话：" + searchINFOBean.getPH());
        if (!TextUtils.isEmpty(searchINFOBean.getNA())) {
            holder.tv_huozhan.setVisibility(View.VISIBLE);
            holder.tv_huozhan.setText("货站名：" + searchINFOBean.getNA());
        } else {
            holder.tv_huozhan.setVisibility(View.GONE);
        }
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
        holder.iv_call.setTag(searchINFOBean.getPH());
        holder.iv_call.setOnClickListener(listener);
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

    public void setFilter(List<String> filters) {
        this.filters = filters;
        if (filters.size()==0){
            return;
        }
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

    private CharSequence matcherSearchText(int color, String string, List<String> keyWords) {
        SpannableStringBuilder builder = new SpannableStringBuilder(string);
        for (String keyWord : keyWords) {
            int indexOf = string.indexOf(keyWord);
            if (indexOf != -1) {
                builder.setSpan(new ForegroundColorSpan(color), indexOf, indexOf + keyWord.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }
}
