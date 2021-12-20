package com.clyr.base;

import android.content.Context;

/**
 * Created by 11635 of clyr on 2021/12/13.
 */

public class AppKit {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }
    public static Context getContext(){
        return mContext;
    }
}
