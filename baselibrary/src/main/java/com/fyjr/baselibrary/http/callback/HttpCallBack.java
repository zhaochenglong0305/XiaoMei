package com.fyjr.baselibrary.http.callback;

import android.text.TextUtils;

import com.fyjr.baselibrary.bean.BaseResponseData;
import com.fyjr.baselibrary.utils.JsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by QNapex on 2016/12/4.
 * 网络请求回调
 */

public abstract class HttpCallBack<T> implements Callback<T> {

    /**
     * 将Json转为泛型类型对象
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (response.isSuccessful()) {
                String value = JsonUtils.toJson(response.body());
                BaseResponseData baseResponseData = JsonUtils.parse(value, BaseResponseData.class);
                if (TextUtils.equals(BaseResponseData.STATE_OK, baseResponseData.getState())) {
                    //根据泛型将json转为对象
                    Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    String typeStr = type.toString();
                    if (typeStr.contains("<") && typeStr.contains(">") && typeStr.contains("List")) {
                        onSuccess((T) JsonUtils.parseList(value, Class.forName(typeStr.substring(typeStr.indexOf("<") + 1, typeStr.length() - 1))), baseResponseData.getMsg());
                    } else {
                        if (((Class<T>) type).isAssignableFrom(String.class)) {
                            onSuccess((T) value, baseResponseData.getMsg());
                        } else {
                            onSuccess(JsonUtils.parse(value, (Class<T>) type), baseResponseData.getMsg());
                        }
                    }
                } else {
                    onFail(1, TextUtils.isEmpty(baseResponseData.getMsg()) ? "请求失败" : baseResponseData.getMsg());
                }
            } else {
                onFail(response.code(), response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFail(response.code(), "请求失败");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFail(0, "请求失败");
    }

    /**
     * 执行成功
     *
     * @param data
     * @param msg
     */
    public abstract void onSuccess(T data, String msg);

    /**
     * 执行失败
     *
     * @param errorCode
     * @param msg
     */
    public abstract void onFail(int errorCode, String msg);
}
