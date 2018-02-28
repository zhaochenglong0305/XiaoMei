package view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
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
import java.util.List;

import manager.UseInfoManager;

/**
 * Created by Administrator on 2018/2/26.
 */

public class DialogCall extends PopupWindow implements View.OnClickListener,AdapterView.OnItemClickListener {
    private Activity context;
    private List<String> phones = new ArrayList<>();
    private TextView cancel;
    private ListView phonesView;

    public DialogCall(final Activity context, List<String> phones) {
        super(context);
        this.context = context;
        this.phones = phones;
        View view = View.inflate(context, R.layout.dialog_information_call, null);
        cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        phonesView = (ListView) view.findViewById(R.id.lv_phone_list);
        phonesView.setAdapter(new CallAdapter());
        phonesView.setOnItemClickListener(this);
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
//        ViewGroup.LayoutParams.MATCH_PARENT
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
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String phone = phones.get(position);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private class CallAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return phones.size();
        }

        @Override
        public Object getItem(int position) {
            return phones.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(context);
            textView.setHeight(100);
            textView.setText(phones.get(position));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(15);
            textView.setTextColor(context.getResources().getColor(R.color.c595a6e));
            return textView;
        }
    }


}
