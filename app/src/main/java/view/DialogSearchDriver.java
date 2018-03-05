package view;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fyjr.baselibrary.utils.ToastUtil;
import com.lit.xiaomei.R;

import java.util.ArrayList;

import manager.UseInfoManager;

/**
 * Created by Administrator on 2018/3/4.
 */

public class DialogSearchDriver extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Activity activity;
    private ArrayList<String> texts = new ArrayList<>();
    private TextView cancel;
    private EditText inputDriverMsg;
    private Button doSearch;
    private TextView clear;
    private ListView driverRecord;
    private RecordAdapter adapter;
    private OnDoSearchDriverListener listener;

    public DialogSearchDriver(Activity activity) {
        super(activity);
        this.activity = activity;
        View view = View.inflate(activity, R.layout.dialog_search_driver, null);
        ArrayList<String> records = UseInfoManager.getStringArraylist(activity, "DriverRecord");
        if (records != null) {
            texts = records;
        }
        cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        inputDriverMsg = (EditText) view.findViewById(R.id.et_input_driver_imformation);
        doSearch = (Button) view.findViewById(R.id.btn_search_driver);
        clear = (TextView) view.findViewById(R.id.tv_clear);
        driverRecord = (ListView) view.findViewById(R.id.lv_search_driver_record);
        adapter = new RecordAdapter();
        driverRecord.setAdapter(adapter);
        cancel.setOnClickListener(this);
        doSearch.setOnClickListener(this);
        driverRecord.setOnItemClickListener(this);
        clear.setOnClickListener(this);


        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置弹出窗体的宽和高
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = activity.getWindow();

        WindowManager m = activity.getWindowManager();
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
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog_cancel:
                dismiss();
                break;
            case R.id.btn_search_driver:
                if (TextUtils.isEmpty(inputDriverMsg.getText().toString())) {
                    ToastUtil.showToast(activity, "请输入司机信息");
                    return;
                }
                listener.onClick(inputDriverMsg.getText().toString());
                texts.add(inputDriverMsg.getText().toString());
                UseInfoManager.putStringArraylist(activity, "DriverRecord", texts);
                dismiss();
                break;
            case R.id.tv_clear:
                texts.clear();
                UseInfoManager.putStringArraylist(activity, "DriverRecord", texts);
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        listener.onClick(texts.get(i));
        dismiss();
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
            view = LayoutInflater.from(activity).inflate(R.layout.adapter_record, viewGroup, false);
            TextView context = (TextView) view.findViewById(R.id.tv_context);
            context.setText(texts.get(i));
            return view;
        }
    }

    public interface OnDoSearchDriverListener {
        void onClick(String key);
    }

    public void setOnDoSearchDriverListener(OnDoSearchDriverListener listener) {
        this.listener = listener;
    }
}
