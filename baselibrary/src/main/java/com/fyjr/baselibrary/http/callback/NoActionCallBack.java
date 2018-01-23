package com.fyjr.baselibrary.http.callback;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by QNapex on 2016/12/5.
 * 不需要返回数据的回调
 */

public class NoActionCallBack implements Callback {

    @Override
    public void onResponse(Call call, Response response) {

    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}
