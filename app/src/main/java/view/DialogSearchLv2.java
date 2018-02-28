package view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fyjr.baselibrary.utils.ToastUtil;
import com.lit.xiaomei.R;

import java.util.ArrayList;
import java.util.List;

import manager.UseInfoManager;

/**
 * Created by Administrator on 2018/2/26.
 */

public class DialogSearchLv2 extends PopupWindow implements View.OnClickListener {
    private Activity context;
    private ArrayList<String> texts = new ArrayList<>();
    private EditText inputCity;
    private Button search;
    private TextView clear;
    private TextView cancel;
    private ListView record;
    private RecordAdapter adapter;
    private OnSearchListener listener;

    public DialogSearchLv2(final Activity context) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.dialog_information_search_lv2, null);
        ArrayList<String> records = UseInfoManager.getStringArraylist(context, "Record");
        if (records != null) {
            texts = records;
        }
        adapter = new RecordAdapter();
        inputCity = (EditText) view.findViewById(R.id.et_add_city);
        search = (Button) view.findViewById(R.id.btn_add_city);
        clear = (TextView) view.findViewById(R.id.tv_clear_record);
        cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        record = (ListView) view.findViewById(R.id.lv_search_lv2_record);
        record.setAdapter(adapter);
        clear.setOnClickListener(this);
        search.setOnClickListener(this);
        cancel.setOnClickListener(this);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置弹出窗体的宽和高
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = context.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置外部可点击
        this.setOutsideTouchable(false);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimBottom);
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
        public void onClick(boolean isDo, String searchText);
    }

    public void setSearchCListener(OnSearchListener onSearchListener) {
        this.listener = onSearchListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_city:
                String input = inputCity.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    ToastUtil.showToast(context, "内容不能为空");
                    return;
                }
                listener.onClick(true, input);
                texts.add(input);
                UseInfoManager.putStringArraylist(context, "Record", texts);
                dismiss();
                break;
            case R.id.tv_clear_record:
                texts.clear();
                UseInfoManager.putStringArraylist(context, "Record", texts);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_dialog_cancel:
                listener.onClick(false, "");
                dismiss();
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
            TextView context = (TextView) view.findViewById(R.id.tv_context);
            context.setText(texts.get(i));
            return view;
        }
    }
}
