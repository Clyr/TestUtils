package com.clyr.utils;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by 11635 of clyr on 2021/11/26.
 */

public class UtilsKit {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}
