package com.fyjr.baselibrary.http;


import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by QNapex on 2016/12/5.
 * 请求接口
 */

public interface RetrofitService {

    @POST
    Call<Object> postByJson(@Url String url, @Body Map map);

    @POST
    Call<Object> postByJsonWithToken(@Url String url, @Header("loginToken") String loginToken, @Body Map map);

    @Multipart
    @POST
    Call<Object> uploadFile(@Url String url, @Query("butlerId") String butlerId, @Part("file\"; filename=\"file.jpg") RequestBody avatar);

    @Multipart
    @POST
    Call<Object> updateData(@Url String url, @Part("headImg\"; filename=\"headImg.jpg") RequestBody headImg, @Part("userId") RequestBody userId,
                            @Part("nickName") RequestBody nickName, @Part("remarks") RequestBody remarks,
                            @Part("province") RequestBody province, @Part("city") RequestBody city,
                            @Part("birthday") RequestBody birthday, @Part("favoriteCuisine") RequestBody favoriteCuisine,
                            @Part("favoriteDrink") RequestBody favoriteDrink, @Part("favoriteEatPlace") RequestBody favoriteEatPlace,
                            @Part("appetite") RequestBody appetite, @Part("drinker") RequestBody drinker);

    @Multipart
    @POST
    Call<Object> updateBusinessData(@Url String url, @Part("headImg\"; filename=\"headImg.jpg") RequestBody headImg,
                                    @Part("userId") RequestBody userId, @Part("businessHours") RequestBody businessHours,
                                    @Part("tel") RequestBody tel, @Part("introduce") RequestBody introducer);

    @Multipart
    @POST
    Call<Object> publishVideo(@Url String url, @Header("loginToken") String loginToken, @Part("photo\"; filename=\"photo.jpg") RequestBody photo,
                              @Part("video\"; filename=\"video.mp4") RequestBody video,
                              @Part("type") RequestBody type, @Part("content") RequestBody context,
                              @Part("longitude") RequestBody longitude, @Part("latitude") RequestBody latitude,
                              @Part("address") RequestBody address);

    @Multipart
    @POST
    Call<Object> publishVideo1(@Url String url, @Header("loginToken") String loginToken,
                               @Part("type") RequestBody type, @Part("content") RequestBody context,
                               @Part("longitude") RequestBody longitude, @Part("latitude") RequestBody latitude,
                               @Part("address") RequestBody address, @Part("videoId") RequestBody videoId
                                , @Part("imageUrl") RequestBody imageUrl);

    @Multipart
    @POST
    Call<Object> publishImages(@Url String url, @Header("loginToken") String loginToken,
                               @PartMap Map<String, RequestBody> images,
                               @Part("type") RequestBody type, @Part("content") RequestBody context,
                               @Part("longitude") RequestBody longitude, @Part("latitude") RequestBody latitude,
                               @Part("address") RequestBody address);

    @Multipart
    @POST
    Call<Object> publishText(@Url String url, @Header("loginToken") String loginToken,
                             @Part("type") RequestBody type, @Part("content") RequestBody context,
                             @Part("longitude") RequestBody longitude, @Part("latitude") RequestBody latitude,
                             @Part("address") RequestBody address);


    @Multipart
    @POST
    Call<Object> uploadCardImg(@Url String url, @Query("butlerId") String butlerId, @Query("butleAttachmentType") String butleAttachmentType, @Part("img\"; filename=\"img.jpg") RequestBody avatar);

    @GET
    Call<Object> get(@Url String url);

    @Multipart
    @POST
    Call<Object> getChannelMsg(@Url String url, @Header("loginToken") String loginToken, @Part("city") RequestBody city, @Part("longitude") RequestBody longitude,
                               @Part("latitude") RequestBody latitude, @Part("userId") RequestBody userId, @Part("headImg") RequestBody headImg, @Part("ext") RequestBody ext,
                               @Part("title") RequestBody title, @Part("channelImg\"; filename=\"channelImg.jpg") RequestBody channelImg);

}
