package com.clyr.view;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 *
 * @author lzy of clyr
 * @date 2022/06/09
 */

public class FixedSpeedScroller extends Scroller {

    private static final int DURATION_DEF = 1500;
    private int mDuration = DURATION_DEF;

    public FixedSpeedScroller(Context context) {
        this(context, DURATION_DEF);
    }

    public FixedSpeedScroller(Context context, int duration) {
        this(context, null, duration);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, DURATION_DEF);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
        super(context, interpolator);
        this.mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        this.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }



    {
        /*//设置viewpager滑动速度
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            //<span style="color:#ff0000;">设置加速度 ，通过改变FixedSpeedScroller这个类中的mDuration来改变动画时间（如mScroller.setmDuration(mMyDuration);）
//            mScroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
            mScroller = new FixedSpeedScroller(mViewPager.getContext());
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setDuration(2000);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
    }

}
