package com.yongchun.library.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yongchun.library.R;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by dee on 15/11/25.
 */
public class ImagePreviewFragment extends Fragment {
    public static final String PATH = "path";
    public static final String IS_LOCAL = "isLocal";

    public static ImagePreviewFragment getInstance(String path, boolean isLocal) {
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        bundle.putBoolean(IS_LOCAL, isLocal);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_image_preview, container, false);
        final ImageView imageView = (ImageView) contentView.findViewById(R.id.preview_image);
        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
        if (getArguments().getBoolean(IS_LOCAL)) {
            Glide.with(container.getContext())
                    .asBitmap()
                    .load(new File(getArguments().getString(PATH)))
                    .into(new SimpleTarget<Bitmap>(480, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                            mAttacher.update();
                        }
                    });
        } else {
            Glide.with(container.getContext())
                    .asBitmap()
                    .load(getArguments().getString(PATH))
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.place)
                            .error(R.mipmap.place))
                    .into(new SimpleTarget<Bitmap>(750, 1334) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                            mAttacher.update();
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            Log.e("路径", "：" + getArguments().getString(PATH));
                            imageView.setImageResource(R.mipmap.place);
                            mAttacher.update();
                        }
                    });
        }
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                ImagePreviewActivity activity = (ImagePreviewActivity) getActivity();
                activity.switchBarVisibility();
            }
        });
        return contentView;
    }

}
