package com.clyr.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.clyr.view.R;

import java.util.List;

/**
 * Created by 11635 of clyr on 2021/12/21.
 */

public class ScanAdapter extends PagerAdapter {
    private final Context mContext;
    List<Object> photoDatas;

    public ScanAdapter(Context mContext, List<Object> imageDatas) {
        this.mContext = mContext;
        this.photoDatas = imageDatas;

    }

    @Override
    public int getCount() {
        return photoDatas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View inflate = LayoutInflater.from(container.getContext()).inflate(R.layout.scan_item, container, false);
        ImageView photoView = inflate.findViewById(R.id.image_view);

        /*ImageView photoView = new ImageView(mContext);
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(matchParent, matchParent);
        photoView.setLayoutParams(params);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);*/

        Glide.with(mContext).load(photoDatas.get(position)).into(photoView);

        container.addView(inflate);

        return inflate;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


}
