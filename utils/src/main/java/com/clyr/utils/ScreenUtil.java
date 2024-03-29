package com.clyr.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.clyr.base.AppKit;

import java.lang.reflect.Method;


public class ScreenUtil {

    private static final String TAG = ScreenUtil.class.getSimpleName();

    private static int navigationBarHeight = 0;
    private static int SOFT_INPUT_HEIGHT = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean checkNavigationBarShow(@NonNull Context context, @NonNull Window window) {
        boolean show;
        Display display = window.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);

        View decorView = window.getDecorView();
        Configuration conf = context.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            show = (point.x != contentView.getWidth());
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            show = (rect.bottom != point.y);
        }
        return show;
    }

    public static int getNavigationBarHeight() {
        if (navigationBarHeight != 0)
            return navigationBarHeight;
        Resources resources = AppKit.getContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        navigationBarHeight = height;
        return height;
    }

    public static int[] getScreenSize() {
        int[] size = new int[2];
        DisplayMetrics dm = AppKit.getContext().getResources().getDisplayMetrics();
        size[0] = dm.widthPixels;
        size[1] = dm.heightPixels;
        return size;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = AppKit.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = AppKit.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getPxByDp(float dp) {
        float scale = AppKit.getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        if (SOFT_INPUT_HEIGHT > 0)
            return SOFT_INPUT_HEIGHT;
        int totalHeight = getDpi(context);
        int contentHeight = getScreenHeight(context);
        SOFT_INPUT_HEIGHT = totalHeight - contentHeight;
        return SOFT_INPUT_HEIGHT;
    }

    public static int[] scaledSize(int containerWidth, int containerHeight, int realWidth, int realHeight) {
        MyLog.i(TAG, "scaledSize  containerWidth: " + containerWidth + " containerHeight: " + containerHeight
                + " realWidth: " + realWidth + " realHeight: " + realHeight);
        float deviceRate = (float) containerWidth / (float) containerHeight;
        float rate = (float) realWidth / (float) realHeight;
        int width;
        int height;
        if (rate < deviceRate) {
            height = containerHeight;
            width = (int) (containerHeight * rate);
        } else {
            width = containerWidth;
            height = (int) (containerWidth / rate);
        }
        return new int[]{width, height};
    }

    public static int dip2px(float dpValue) {
        final float scale = AppKit.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
