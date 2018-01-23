package com.fyjr.baselibrary.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyjr.baselibrary.R;
import com.fyjr.baselibrary.dispatchactivity.SwipeWindowHelper;
import com.fyjr.baselibrary.utils.DialogUtil;
import com.fyjr.baselibrary.utils.StatusBarUtil;
import com.fyjr.baselibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by QNapex on 2016/12/1.
 */

public class BaseActivity<T extends ViewDataBinding> extends FragmentActivity {

    public T binding;
    public int systemType;
    private static Handler handler;
    public static int REQUEST_CODE = 77;
    public static int RESULT_CODE = 277;
    /**
     * 打开的activity
     **/
    private List<Activity> activities = new ArrayList<Activity>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 添加布局
     *
     * @param layoutResID 布局ID
     */
    public void setContentView(Activity activity, @LayoutRes int layoutResID) {
        activities.add(activity);
        binding = DataBindingUtil.setContentView(activity, layoutResID);
        try {
            setFitsSystemWindows(getFitsSystemWindows());
//            setStatusBarLightMode();
            if (getChangeStatusBarColor()) {
                setStatusBarColor(R.color.colorPrimary);
            }
            initToolbar();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示标题栏
     */
    public void initToolbar() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_base);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackClick();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化View
     */
    public void initView() {
    }

    /**
     * 添加标题栏文字
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        try {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置标题栏文字颜色
     */
    public void setTitleTextColor(String color) {
        try {
            ((TextView) findViewById(R.id.tv_title)).setTextColor(Color.parseColor(color));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置标题栏背景透明
     */
    public void setTitleTransparent() {
        try {
            (findViewById(R.id.space)).setBackgroundColor(Color.parseColor("#00000000"));
            ((Toolbar) findViewById(R.id.toolbar_base)).setBackgroundColor(Color.parseColor("#00000000"));
            (findViewById(R.id.line)).setBackgroundColor(Color.parseColor("#00000000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把返回图片设置成白色
     */
    public void setToolbarBackImage() {
        try {
            ((Toolbar) findViewById(R.id.toolbar_base)).setNavigationIcon(R.mipmap.arrow_back_white);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 返回按钮点击事件
     */
    public void onBackClick() {
        finish();
    }

    /**
     * 留出状态栏空间
     *
     * @param fitsSystemWindows
     */
    public void setFitsSystemWindows(boolean fitsSystemWindows) {
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            parentView.setFitsSystemWindows(fitsSystemWindows);
        }

        try {
            View view = findViewById(R.id.space);
            if (!fitsSystemWindows && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = StatusBarUtil.getStatusBarHeight(this);
                view.setLayoutParams(params);
            } else {
                view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态栏黑色文字
     */
    public void setStatusBarLightMode() {
        systemType = StatusBarUtil.StatusBarLightMode(this);
    }

    /**
     * 设置状态栏白色文字
     */
    public void setStatusBarDarkMode() {
        StatusBarUtil.StatusBarDarkMode(this, systemType);
    }

    /**
     * 设置状态栏颜色
     *
     * @param colorId
     */
    public void setStatusBarColor(int colorId) {
        StatusBarUtil.setStatusBarColor(this, colorId);
    }

    /**
     * 修改状态栏为透明
     */
    public void transparencyBar() {
        StatusBarUtil.transparencyBar(this);
    }

    /**
     * 获取是否修改状态栏颜色
     *
     * @return
     */
    public boolean getChangeStatusBarColor() {
        return false;
    }

    /**
     * 获取是否填冲状态栏
     *
     * @return
     */
    public boolean getFitsSystemWindows() {
        return false;
    }

    /**
     * 是否支持滑动返回
     *
     * @return
     */
    public boolean supportSlideBack() {
        return true;
    }

    /**
     * 设置右侧按钮颜色
     *
     * @param textColor
     * @param bgColor
     */
    public void setRightTextColor(String textColor, String bgColor) {
        try {
            TextView tvRight = (TextView) findViewById(R.id.tv_right);
            tvRight.setTextColor(Color.parseColor(textColor));
            tvRight.setBackgroundColor(Color.parseColor(bgColor));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示右侧按钮
     *
     * @param text
     */
    public void setRightText(String text) {
        try {
            TextView tvRight = (TextView) findViewById(R.id.tv_right);
            tvRight.setText(text);
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setTextColor(Color.WHITE);
            tvRight.setBackgroundColor(Color.parseColor("#00000000"));
            tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightClick(v);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示右侧图片
     *
     * @param id
     */
    public void setRightImg(int id) {
        try {
            ImageView imgRight = findViewById(R.id.img_right);
            imgRight.setVisibility(View.VISIBLE);
            imgRight.setImageResource(id);
            imgRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightClick(v);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageView getRightRightImg() {
        ImageView imgRight = findViewById(R.id.img_right);
        return imgRight;
    }

    /**
     * 右侧按钮点击事件
     *
     * @param view
     */
    public void onRightClick(View view) {
    }

    /**
     * 获取ListView的Header
     *
     * @return
     */
    public View getListViewHeader() {
        return DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.listview_header, null, false).getRoot();
    }

    /**
     * 显示加载框
     *
     * @param msg
     */
    public void showLoading(String msg) {
        DialogUtil.showProgress(this, TextUtils.isEmpty(msg) ? "加载中" : msg);
    }

    public void showLoading() {
        showLoading("");
    }

    /**
     * 隐藏加载框
     */
    public void hideLoading() {
        DialogUtil.closeProgress();
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    public void showMessage(String msg) {
        if (!TextUtils.isEmpty(msg))
            ToastUtil.showToast(this, msg);
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
        }
        return handler;
    }

    /**
     * 设置屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    private SwipeWindowHelper mSwipeWindowHelper;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        点击空白位置 隐藏软键盘
        if (supportHideKeyBoard() && ev.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideSoftInput();
            }
        }
//        滑动返回
        if (!supportSlideBack()) {
            return super.dispatchTouchEvent(ev);
        }
        if (mSwipeWindowHelper == null) {
            mSwipeWindowHelper = new SwipeWindowHelper(getWindow());
        }
        return mSwipeWindowHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 是否支持触摸其他位置收起键盘
     *
     * @return
     */
    public boolean supportHideKeyBoard() {
        return true;
    }

    /**
     * 隐藏键盘
     */
    public boolean hideSoftInput() {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        onBackClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    /**
     * 新建了一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
