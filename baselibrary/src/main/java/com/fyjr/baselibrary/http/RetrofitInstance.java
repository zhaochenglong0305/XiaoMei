package com.fyjr.baselibrary.http;


import com.fyjr.baselibrary.http.callback.NoActionCallBack;
import com.fyjr.baselibrary.http.log.LogInterceptor;
import com.fyjr.baselibrary.http.url.HttpUrl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by QNapex on 2016/12/5.
 * 网络请求
 */

public class RetrofitInstance {

    private Retrofit retrofit;
    private OkHttpClient client;
    private RetrofitService service;
    private Call<Object> call;

    private RetrofitInstance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.retryOnConnectionFailure(true)
                .addNetworkInterceptor(new LogInterceptor())
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(HttpUrl.BASE_URL)
                .client(client)
                .build();
        service = retrofit.create(RetrofitService.class);
    }

    /**
     * post请求
     *
     * @param path
     * @param map
     * @param callBack
     */
    public void post(String path, Map map, Callback callBack) {
        call = service.postByJson(path, map);
        enqueue(callBack);
    }

    /**
     * post请求
     *
     * @param path
     * @param token
     * @param map
     * @param callBack
     */
    public void postWithToken(String path, String token, Map map, Callback callBack) {
        call = service.postByJsonWithToken(path, token, map);
        enqueue(callBack);
    }

    /**
     * 上传图片和字段
     *
     * @param path
     * @param butlerId
     * @param body
     * @param callBack
     */
    public void uploadFile(String path, String butlerId, RequestBody body, Callback callBack) {
        call = service.uploadFile(path, butlerId, body);
        enqueue(callBack);
    }

    /**
     * 上传证件照
     *
     * @param path
     * @param butlerId
     * @param butleAttachmentType
     * @param body
     * @param callBack
     */
    public void uploadCardImg(String path, String butlerId, String butleAttachmentType, RequestBody body, Callback callBack) {
        call = service.uploadCardImg(path, butlerId, butleAttachmentType, body);
        enqueue(callBack);
    }

    public void updateData(String path, RequestBody rheadImg, RequestBody ruserId,
                           RequestBody rnickName, RequestBody rremarks, RequestBody rprovince, RequestBody rcity,
                           RequestBody rbirthday, RequestBody rfavoriteCuisine, RequestBody rfavoriteDrink,
                           RequestBody rfavoriteEatPlace, RequestBody rappetite, RequestBody rdrinker, Callback callBack) {
        call = service.updateData(path, rheadImg, ruserId, rnickName, rremarks, rprovince, rcity, rbirthday, rfavoriteCuisine,
                rfavoriteDrink, rfavoriteEatPlace, rappetite, rdrinker);
        enqueue(callBack);
    }

    public void updateBusinessData(String path, RequestBody rheadImg, RequestBody ruserId,
                                   RequestBody rbusinessHours, RequestBody rtel, RequestBody rintroduce, Callback callBack) {
        call = service.updateBusinessData(path, rheadImg, ruserId, rbusinessHours, rtel, rintroduce);
        enqueue(callBack);
    }

    public void publishVideo(String path, String token, RequestBody rbPhoto, RequestBody rbVideo, RequestBody rbtype, RequestBody rbContext, RequestBody rbLongitude,
                             RequestBody rbLatitude, RequestBody rbAddress, Callback callBack) {
        call = service.publishVideo(path, token, rbPhoto, rbVideo, rbtype, rbContext, rbLongitude, rbLatitude, rbAddress);
        enqueue(callBack);
    }

    public void publishVideo1(String path, String token, RequestBody rbtype, RequestBody rbContext, RequestBody rbLongitude,
                              RequestBody rbLatitude, RequestBody rbAddress, RequestBody rbvideoId, RequestBody rbimageUrl, Callback callBack) {
        call = service.publishVideo1(path, token, rbtype, rbContext, rbLongitude, rbLatitude, rbAddress, rbvideoId, rbimageUrl);
        enqueue(callBack);
    }

    public void publishImages(String path, String token, Map<String, RequestBody> rbImgs, RequestBody rbtype, RequestBody rbContext, RequestBody rbLongitude,
                              RequestBody rbLatitude, RequestBody rbAddress, Callback callBack) {
        call = service.publishImages(path, token, rbImgs, rbtype, rbContext, rbLongitude, rbLatitude, rbAddress);
        enqueue(callBack);
    }

    public void publishText(String path, String token, RequestBody rbtype, RequestBody rbContext, RequestBody rbLongitude,
                            RequestBody rbLatitude, RequestBody rbAddress, Callback callBack) {
        call = service.publishText(path, token, rbtype, rbContext, rbLongitude, rbLatitude, rbAddress);
        enqueue(callBack);
    }


    /**
     * get请求
     *
     * @param path
     * @param callBack
     */
    public void get(String path, Callback callBack) {
        call = service.get(path);
        enqueue(callBack);
    }

    /**
     * 开启直播间
     *
     * @param url
     * @param loginToken
     * @param callback
     */
    public void getChannelMsg(String url, String loginToken, RequestBody city, RequestBody longitude, RequestBody latitude, RequestBody userId,
                              RequestBody headImg, RequestBody ext, RequestBody title, RequestBody channelImg, Callback callback) {
        call = service.getChannelMsg(url, loginToken, city, longitude, latitude, userId, headImg, ext, title, channelImg);
        enqueue(callback);
    }

    /**
     * 异步请求
     *
     * @param callBack
     */
    private void enqueue(Callback callBack) {
        try {
            call.enqueue(callBack != null ? callBack : new NoActionCallBack());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final RetrofitInstance getInstance() {
        return RetrofitInstanceHolder.RETROFIT_INSTANCE;
    }

    private static class RetrofitInstanceHolder {
        private static final RetrofitInstance RETROFIT_INSTANCE = new RetrofitInstance();
    }
}
