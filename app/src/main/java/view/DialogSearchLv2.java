package view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lit.xiaomei.R;

import java.util.ArrayList;
import java.util.List;

import manager.UseInfoManager;

/**
 * Created by Administrator on 2018/2/26.
 */

public class DialogSearchLv2 extends PopupWindow implements View.OnClickListener {
    private Context context;
    private List<String> texts = new ArrayList<>();
    private EditText inputCity;
    private Button search;
    private TextView clear;
    private ListView record;
    private RecordAdapter adapter;
    private OnSearchListener listener;

    public DialogSearchLv2(final Context context) {
        super(context);
        this.context = context;
        List<String> records = UseInfoManager.getStringArraylist(context, "Record");
        if (records != null) {
            texts = records;
        }
        adapter = new RecordAdapter();
        View view = View.inflate(context, R.layout.dialog_information_search_lv2, null);
        inputCity = (EditText) view.findViewById(R.id.et_add_city);
        search = (Button) view.findViewById(R.id.btn_add_city);
        clear = (TextView) view.findViewById(R.id.tv_clear_record);
        record = (ListView) view.findViewById(R.id.lv_search_lv2_record);
        record.setAdapter(adapter);
        clear.setOnClickListener(this);
        search.setOnClickListener(this);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);

        //设置SelectPicPopupWindow弹出窗体动画效果
        //		this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnSearchListener {
        public void onClick(String searchText);
    }

    public void setSearchCListener (OnSearchListener onSearchListener){
        this.listener = onSearchListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_city:
                String input = inputCity.getText().toString();
                listener.onClick(input);
                dismiss();
                break;
            case R.id.tv_clear_record:
                UseInfoManager.putStringArraylist(context, "Record", new ArrayList<String>());
                break;
        }
    }


    private class RecordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return texts.size();
        }

        @Override
        public Object getItem(int i) {
            return texts.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_record, viewGroup, false);
            TextView context = (TextView) view.findViewById(R.id.tv_content);
            context.setText(texts.get(i));
            return view;
        }
    }
}
