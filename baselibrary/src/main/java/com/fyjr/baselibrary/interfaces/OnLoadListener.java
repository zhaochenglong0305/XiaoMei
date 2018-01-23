package com.fyjr.baselibrary.interfaces;

/**
 * 用户实现类状态
 * Created by Administrator on 2016/4/7.
 */
public interface OnLoadListener<T> {
    void onSuccess(T t);

    void onFailed(String msg);
}
