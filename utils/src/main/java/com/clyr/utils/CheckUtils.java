package com.clyr.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by clyr on 2017/12/27 0027.
 * 获取权限
 */

public class CheckUtils {
    private static final String[] mPermission = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储写
            android.Manifest.permission.READ_EXTERNAL_STORAGE,//存储读
            android.Manifest.permission.CAMERA,//相机
            android.Manifest.permission.RECORD_AUDIO,//录音
            android.Manifest.permission.ACCESS_FINE_LOCATION//GPS
    };
    private static final int REQUST_CODE = 5;

    /**
     * 请求权限
     *
     * @param activity
     */
    public static void checkPermission(Activity activity) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < mPermission.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                list.add(mPermission[i]);
            }
        }
        if (list.size() > 0) {
            String[] strings = new String[list.size()];
            list.toArray(strings);
            //TODO onActivityResult
            //if (requestCode == REQUST_CODE && resultCode == RESULT_OK) {}
            ActivityCompat.requestPermissions(activity, strings, REQUST_CODE);
        }

    }

    /**
     * 非空校验
     *
     * @param obj
     * @return
     */
    public static boolean checkNull(Object obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj instanceof String) {
                if ("".equals(obj) || ((String) obj).length() <= 0) {
                    return true;
                }
            } else if (obj instanceof List) {
                if (((List) obj).size() <= 0) {
                    return true;
                }
            } else if (obj instanceof Object[]) {
                if (((Object[]) obj).length <= 0) {
                    return true;
                }
            } else if (obj instanceof Map) {
                if (((Map) obj).size() <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkNull(String obj) {
        if (obj == null) {
            return true;
        } else {

            if ("".equals(obj) || obj.length() <= 0) {
                return true;
            }

        }
        return false;
    }

    public static boolean checkNull(List obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj.size() <= 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkNull(Map obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj.size() <= 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkNull(Object[] obj) {
        if (obj == null) {
            return true;
        } else {
            if (obj.length <= 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    @Deprecated
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
    /**
     * 判断权限是否被拒绝
     */
    public static int perDenied(Activity activity, int num) {
        if (num > mPermission.length || num < 0) {
            //SDToast.showToast("标记设置错误");
            return -1;
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[num]) == PackageManager.PERMISSION_DENIED)
            return 0;
        return 1;
    }

    public static int perGranted(Activity activity, int num) {
        if (num > mPermission.length || num < 0) {
            //SDToast.showToast("标记设置错误");
            return -1;
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[num]) == PackageManager.PERMISSION_GRANTED)
            return 0;
        return 1;
    }

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean checkAlertWindowsPermission(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1));
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }

    public static void startWindowsPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    public void Other(Context context) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(context, mPermission[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission(context);
            }
        }
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission(Context con) {

        new AlertDialog.Builder(con)
                .setTitle("存储权限不可用")
                .setMessage("由于支付宝需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用支付宝")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
//        ActivityCompat.requestPermissions(this, mPermission, 321);
    }

    // 用户权限 申请 的回调方法
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {
                    }
//                        finish();
                } else {
//                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean shouldShowRequestPermissionRationale(String permission) {
        return false;
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {

        /*dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许支付宝使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();*/
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
       /* Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }*/
    }
}
