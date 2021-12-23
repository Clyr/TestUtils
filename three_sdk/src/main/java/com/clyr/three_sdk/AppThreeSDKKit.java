package com.clyr.three_sdk;

import android.annotation.SuppressLint;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by 11635 of clyr on 2021/12/13.
 */

public class AppThreeSDKKit {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void initBMap() {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        // 默认本地个性化地图初始化方法
        SDKInitializer.initialize(mContext);
    }
}
