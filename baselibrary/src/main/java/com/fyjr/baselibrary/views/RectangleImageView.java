package com.fyjr.baselibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fyjr.baselibrary.R;


/**
 * Created by QNapex on 2016/11/4.
 */

public class RectangleImageView extends ImageView {

    private int widthRec = 16, heightRec = 9;

    public RectangleImageView(Context context) {
        this(context, null);
    }

    public RectangleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RectangleImageView);
        widthRec = a.getInteger(R.styleable.RectangleImageView_rectangle_width, 16);
        heightRec = a.getInteger(R.styleable.RectangleImageView_rectangle_height, 9);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec / widthRec * heightRec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width / widthRec * heightRec);
    }
}
