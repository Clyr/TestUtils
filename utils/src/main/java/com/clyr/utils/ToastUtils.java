package com.clyr.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by clyr on 2018/3/12 0012.
 * 弹出Toast提示的快捷方式 不需要输入context 和 时长
 */

public class ToastUtils {
    String mTag = "ToastUtils";

    public static void showLong(String string) {
        Toast.makeText(UtilsKit.getContext(), string, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static void showLong(int str) {
        Toast.makeText(UtilsKit.getContext(), str, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String string) {
        Toast.makeText(UtilsKit.getContext(), string, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int str) {
        Toast.makeText(UtilsKit.getContext(), str, Toast.LENGTH_SHORT).show();
    }
}
