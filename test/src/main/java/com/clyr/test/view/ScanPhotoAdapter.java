package com.clyr.test.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by heiguang on 2017/5/26.
 */

public class ScanPhotoAdapter extends PagerAdapter {

    Context mContext;
    List<String> photoDatas;
    OnTapListener tapListener;

    public ScanPhotoAdapter(Context mContext, List<String> imageDatas, OnTapListener tapListener) {
        this.mContext = mContext;
        this.photoDatas = imageDatas;
        this.tapListener = tapListener;
    }

    @Override
    public int getCount() {
        return photoDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        PhotoView photoView = new PhotoView(mContext);
        photoView.setMaximumScale(2.0f);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(mContext).load(photoDatas.get(position)).into(photoView);

        if (tapListener != null) {
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    tapListener.onTap();
                }
            });
        }

        container.addView(photoView);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnTapListener {
        void onTap();
    }
}
