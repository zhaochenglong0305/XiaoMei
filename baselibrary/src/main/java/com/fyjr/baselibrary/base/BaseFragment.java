package com.fyjr.baselibrary.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyjr.baselibrary.R;
import com.fyjr.baselibrary.utils.DialogUtil;
import com.fyjr.baselibrary.utils.StatusBarUtil;
import com.fyjr.baselibrary.utils.ToastUtil;


public class BaseFragment<T extends ViewDataBinding> extends Fragment {

    public T binding;
    private static Handler handler;

    private OnFragmentInteractionListener mListener;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        initView();
        return binding.getRoot();
    }

    public void initView() {
    }

    public int getLayoutId() {
        return R.layout.fragment_base;
    }

    public void space(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = StatusBarUtil.getStatusBarHeight(getContext());
            view.setLayoutParams(params);
        }
    }

    /**
     * 显示加载框
     *
     * @param msg
     */
    public void showLoading(String msg) {
        DialogUtil.showProgress(getActivity(), TextUtils.isEmpty(msg) ? "加载中" : msg);
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
            ToastUtil.showToast(getActivity(), msg);
    }

    protected final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public View getSlidableView() {
        return null;
    }

}
