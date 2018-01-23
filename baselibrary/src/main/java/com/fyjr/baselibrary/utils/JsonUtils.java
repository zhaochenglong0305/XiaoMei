package com.fyjr.baselibrary.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * JsonUtils
 */
public class JsonUtils {

    /**
     * 转换成json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }


    /**
     * json转换成类
     *
     * @param data
     * @param class1
     * @return
     */
    public static <T> T parse(String data, Class<T> class1) {
        return new Gson().fromJson(data, class1);
    }

    /**
     * json转换成列表
     *
     * @param data
     * @param class1
     * @return
     */
    public static <T> List<T> parseList(String data, Class<T> class1) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        List<T> mList = new ArrayList<T>();
        try {
            JSONArray mArray = new JSONArray(data);
            final int size = mArray.length();
            for (int i = 0; i < size; i++) {
                T t = parse(mArray.getJSONObject(i).toString(), class1);
                mList.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }

}
