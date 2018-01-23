package com.fyjr.baselibrary.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by \(^o^)/~ on 2016/8/16.
 */
public class DensityUtil {

    private DensityUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVal * scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxVal / scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 计算两点之间距离
     *
     * @param latitude1
     * @param lngitude1
     * @param latitude2
     * @param lngitude2
     * @return 米
     */
    public static double getDistance(double latitude1, double lngitude1, double latitude2, double lngitude2) {
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;

        double lon1 = (Math.PI / 180) * lngitude1;
        double lon2 = (Math.PI / 180) * lngitude2;

        //地球半径
        double R = 6371;

        //两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d * 1000;
    }

    /**
     * 动态设置ListView高度
     *
     * @param listView
     */
    public static int setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            Log.e("item高度：" + i, "" + listItem.getMeasuredHeight());
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        return params.height;
    }
}
