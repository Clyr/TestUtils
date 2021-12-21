package com.clyr.three_sdk.baidumapsdk.demo.util;

import android.app.Activity;

/**
 * Demo信息类
 */

public class DemoInfo {
    public final int title;
    public final int desc;
    public final Class<? extends Activity> demoClass;

    public DemoInfo(int title, int desc,
                    Class<? extends Activity> demoClass) {
        this.title = title;
        this.desc = desc;
        this.demoClass = demoClass;
    }
}
