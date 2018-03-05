package view;

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

/**
 * Created by Administrator on 2018/3/3.
 */

public class DialogAddDriver extends PopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener {
    private String[] carNumHan = {"京", "津", "沪", "渝", "冀", "豫", "鲁", "晋", "陕", "皖", "苏",
            "浙", "鄂", "湘", "赣", "闽", "粤", "桂", "琼", "川", "贵", "云", "辽", "吉",
            "黑", "蒙", "甘", "宁", "青", "新", "藏", "港", "澳", "台", ""};
    private String[] carNumPin = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private Activity context;
    private TextView cancel;
    private TextView ok;
    private EditText driverName;
    private EditText driverPhone;
    private TextView carNum;
    private ImageView delete;
    private GridView carNumList;
    private LinearLayout inputCarnum;
    private CarNumGridAdapter hanAdapter;
    private CarNumGridAdapter pinAdapter;
    private boolean isShow = false;
    private String carNumText = "";
    private OnAddDriverFinishListener listener;

    public DialogAddDriver(Activity context) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.dialog_add_driver, null);
        cancel = (TextView) view.findViewById(R.id.tv_dialog_cancle);
        ok = (TextView) view.findViewById(R.id.tv_dialog_ok);
        driverName = (EditText) view.findViewById(R.id.et_driver_name);
        driverPhone = (EditText) view.findViewById(R.id.et_driver_phone);
        carNum = (TextView) view.findViewById(R.id.tv_car_num);
        delete = (ImageView) view.findViewById(R.id.iv_input_car_num_del);
        carNumList = (GridView) view.findViewById(R.id.gv_car_number);
        inputCarnum = (LinearLayout) view.findViewById(R.id.ll_input_car_num);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        carNum.setOnClickListener(this);
        delete.setOnClickListener(this);
        hanAdapter = new CarNumGridAdapter(carNumHan);
        pinAdapter = new CarNumGridAdapter(carNumPin);
        carNumList.setAdapter(hanAdapter);
        carNumList.setOnItemClickListener(this);


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
            case R.id.tv_dialog_cancle:
                dismiss();
                break;
            case R.id.tv_dialog_ok:
                if (TextUtils.isEmpty(driverName.getText().toString()) ||
                        TextUtils.isEmpty(driverPhone.getText().toString()) ||
                        TextUtils.isEmpty(carNum.getText().toString())) {
                    ToastUtil.showToast(context, "不能为空，请检查输入内容");
                    return;
                }
                listener.onClick(driverName.getText().toString(), driverPhone.getText().toString(), carNumText);
                dismiss();
                break;
            case R.id.iv_input_car_num_del:
                carNumText = carNumText.substring(0, carNumText.length() - 1);
                if (carNumText.length() == 0) {
                    carNumList.setAdapter(hanAdapter);
                }
                carNum.setText(carNumText);
                break;
            case R.id.tv_car_num:
                if (isShow) {
                    isShow = false;
                    inputCarnum.setVisibility(View.GONE);
                } else {
                    isShow = true;
                    inputCarnum.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (carNumText.length() == 0) {
            carNumText += carNumHan[i];
            carNumList.setAdapter(pinAdapter);
        } else if (carNumText.length() > 0) {
            carNumText += carNumPin[i];
        }
        carNum.setText(carNumText);
        if (carNumText.length() == 7) {
            isShow = false;
            inputCarnum.setVisibility(View.GONE);
        }
    }

    private class CarNumGridAdapter extends BaseAdapter {
        private String[] texts = new String[]{};

        public CarNumGridAdapter(String[] texts) {
            this.texts = texts;
        }

        @Override
        public int getCount() {
            return this.texts.length;
        }

        @Override
        public Object getItem(int i) {
            return this.texts[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(context);
            textView.setPadding(0, 5, 0, 5);
            textView.setGravity(Gravity.CENTER);
            textView.setText(this.texts[i]);
            textView.setTextColor(context.getResources().getColor(R.color.c888888));
            textView.setTextSize(15);
            return textView;
        }
    }

    public interface OnAddDriverFinishListener {
        void onClick(String name, String phone, String car);
    }

    public void setOnAddDriverFinishListener(OnAddDriverFinishListener listener) {
        this.listener = listener;
    }
}

