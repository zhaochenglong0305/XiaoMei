package com.lit.xiaomei.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.lit.xiaomei.R;

public class AdvertisementDialog extends PopupWindow implements OnClickListener {

    public AdvertisementDialog(Context context, String imgUrl) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_advertisement, null);
        ImageView news = (ImageView) view.findViewById(R.id.iv_news);
        Glide.with(context).load(imgUrl).into(news);
        ImageView close = (ImageView) view.findViewById(R.id.iv_close_news);
        close.setOnClickListener(this);
        this.setContentView(view);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        this.setFocusable(true);
        this.setBackgroundDrawable(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_news:
                dismiss();
                break;

        }

    }

}
