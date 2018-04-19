package com.lit.xiaomei.view;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fyjr.baselibrary.utils.ToastUtil;
import com.lit.xiaomei.R;
import com.lit.xiaomei.bean.CityPhones;

/**
 * Created by Administrator on 2018/3/3.
 */

public class DialogShowKeFu extends PopupWindow implements View.OnClickListener {
    private Activity context;
    private TextView tv_dialog_cancle;
    private TextView tv_kefu_phone;
    private TextView tv_kefu_weixin;
    private String phone = "";
    private String weixin = "";


    public DialogShowKeFu(Activity context, CityPhones.OpratesBean ob) {
        super(context);
        this.context = context;
        if (ob != null) {
            phone = ob.getKefu();
            weixin = ob.getWeixin();
        }
        View view = View.inflate(context, R.layout.dialog_show_kefu, null);
        tv_dialog_cancle = (TextView) view.findViewById(R.id.tv_dialog_cancle);
        tv_kefu_phone = (TextView) view.findViewById(R.id.tv_kefu_phone);
        tv_kefu_weixin = (TextView) view.findViewById(R.id.tv_kefu_weixin);
        tv_kefu_phone.setText(phone);
        tv_kefu_weixin.setText(weixin);
        tv_dialog_cancle.setOnClickListener(this);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        Window dialogWindow = context.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog_cancle:
                dismiss();
                break;
        }
    }


}

