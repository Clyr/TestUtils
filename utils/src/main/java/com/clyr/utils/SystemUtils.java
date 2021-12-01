package com.clyr.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;

import com.clyr.base.bean.IpsSohu;
import com.clyr.utils.utilshelper.SystemStatusManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 用于获取和修改系统配置
 * Created by M S I of clyr on 2019/11/14.
 */
public class SystemUtils {

    /**
     * 获取当前本地apk的版本 versionCode
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称 versionName
     *
     * @param context 上下文
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取versionCode
     *
     * @param context
     * @return
     */
    public static String getAppVersionCode(Context context) {
        PackageInfo packageInfo = null;
        PackageManager pm = context.getPackageManager();
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionCode + "";
    }
    /**
     * 获取APP版本
     *
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String getAppVersion(Context context) {
        PackageInfo packageInfo = null;
        PackageManager pm = context.getPackageManager();
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param color
     */
    public static void setTranslucentStatus(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            // 透明状态栏
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);
            window.getDecorView().setFitsSystemWindows(true);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(R.color.blue9);
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }*/


    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        // 透明状态栏
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        SystemStatusManager tintManager = new SystemStatusManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue9);
        activity.getWindow().getDecorView().setFitsSystemWindows(true);
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 系统剪贴板-复制
     *
     * @param context
     * @param s       内容
     */

    public static void setCopy(Context context, String s) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null, s);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }

    /**
     * 系统剪贴板-获取:
     *
     * @param context ...
     * @return string
     */

    public static String getCopy(Context context) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 返回数据
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            // 从数据集中获取（粘贴）第一条文本数据
            return clipData.getItemAt(0).getText().toString();
        }
        return null;
    }

    public static String getText(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboard.getText().toString();
    }

    public static void setText(Context context, CharSequence string) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(string);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    static Activity activity;
    //TODO 指纹验证
    //此类基于Java加密API的一个包装类,用于防止在指纹扫描中被第三方恶意攻击
    static FingerprintManager.CryptoObject cryptoObject;
    static FingerprintManager manager;//访问指纹硬件的类
    static KeyguardManager keyManager;//管理锁屏的类
    static CancellationSignal signal = new CancellationSignal();

    @TargetApi(Build.VERSION_CODES.M)
    public static void Fingerprint(Activity act) {
        activity = act;
        //通过V4包获得对象
        manager = (FingerprintManager) activity.getSystemService(Context.FINGERPRINT_SERVICE);
        keyManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        if (isFingerprint()) {
            startListen();
        }
    }

    public static int i = 0;

    /**
     * 开始指纹识别
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void startListen() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //LoadingDialog.showLoadingBall(activity, "指纹识别中");
        FingerprintManager.AuthenticationCallback callBack = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //LoadingDialog.cancelLoading();
                ToastUtils.showShort(activity, "操作过于频繁,请稍后再试");
            }


            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }


            //指纹识别成功
            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //LoadingDialog.cancelLoading();
                ToastUtils.showShort(activity, "指纹识别成功");
            }


            //指纹识别失败
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //LoadingDialog.cancelLoading();
                ToastUtils.showShort(activity, "指纹识别失败");
                i++;
                if (i == 3) {
                    ToastUtils.showShort(activity, "失败次数过多,请输入锁屏密码");
                    showLockScreenPass(keyManager);
                    i = 0;
                }
            }
        };
        manager.authenticate(cryptoObject, signal, 0, callBack, null);


    }

    //取消指纹识别
    public void stopListening() {
        if (signal != null) {
            // selfCancelled = true;
            signal.cancel();
            signal = null;
            ToastUtils.showShort(activity, "您已经取消指纹识别");
        }
    }


    /**
     * 指纹识别错误次数过多,显示手机锁屏密码
     *
     * @param keyManager
     */
    public static final int REQUST_CODE = 1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showLockScreenPass(KeyguardManager keyManager) {
        Intent intent = keyManager.createConfirmDeviceCredentialIntent("finger", "开启锁屏密码");
        if (intent != null) {
            activity.startActivityForResult(intent, REQUST_CODE);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isFingerprint() {
        //此方法为了保证判断是否支持支持指纹不报错
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showShort(activity, "没有指纹解锁的权限");
            return false;
        }
        //硬件设备是否支持指纹解锁功能
        if (!manager.isHardwareDetected()) {
            ToastUtils.showShort(activity, "该手机不支持指纹解锁");
            return false;
        }
        //判断是否有锁屏密码
        if (!keyManager.isKeyguardSecure()) {
            ToastUtils.showShort(activity, "请设置锁屏密码");
            return false;
        }
        //判断是否录入指纹
        if (!manager.hasEnrolledFingerprints()) {
            ToastUtils.showShort(activity, "没有录入指纹");
            return false;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.P)
    public static void BiometricPrint() {
        BiometricPrompt mBiometricPrompt;
        CancellationSignal mCancellationSignal;
        BiometricPrompt.AuthenticationCallback mAuthenticationCallback;
        mBiometricPrompt = new BiometricPrompt.Builder(activity)
                .setTitle("指纹验证")
//                .setDescription("描述")
                .setNegativeButton("取消", activity.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtils.showShort(activity, "Cancel button clicked");
                    }
                })
                .build();

        mCancellationSignal = new CancellationSignal();
        mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                //handle cancel result
                ToastUtils.showShort(activity, "Canceled");
            }
        });

        mAuthenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                ToastUtils.showShort(activity, "onAuthenticationError " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                ToastUtils.showShort(activity, "验证成功");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                ToastUtils.showShort(activity, "onAuthenticationFailed ");
            }
        };

        mBiometricPrompt.authenticate(mCancellationSignal, activity.getMainExecutor(), mAuthenticationCallback);

    }

    /**
     * 是否使用的是VPN
     *
     * @return
     */
    public static boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    MyLog.d("isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前ip
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * 获取外网的IP(要访问Url，要放到后台线程里处理)
     *
     * @param @return
     * @return String
     * @throws
     * @Title: GetNetIp
     * @Description:
     */
    public static String GetNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL("http://ip168.com/");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");

                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Matcher matcher = pattern.matcher(strber.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (httpConnection != null)
                    httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ipLine;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> enIp = ni.getInetAddresses();
                while (enIp.hasMoreElements()) {
                    InetAddress inet = enIp.nextElement();
                    if (!inet.isLoopbackAddress()
                            && (inet instanceof Inet4Address)) {
                        return inet.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "0";
    }

    public static void getNetIp() {
        OkHttpUtils.get().url("http://pv.sohu.com/cityjson?ie=utf-8").build().execute(new StringCallback() {

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                MyLog.e(e.toString());
                ToastUtils.showShort(activity, getLocalIpAddress());
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.d(response);
                if (response != null) {
                    int start = response.indexOf("{");
                    int end = response.indexOf("}") + 1;
                    String string = response.substring(start, end);
                    MyLog.d(string);
                    Gson gson = new Gson();
                    IpsSohu IpsSohu = gson.fromJson(string, IpsSohu.class);
                    MyLog.d(IpsSohu.cip);
                    ToastUtils.showShort(activity, "外网IP：" + IpsSohu.cip);
                }
                ToastUtils.showShort(activity, getLocalIpAddress());
            }
        });
    }

    public static long getSystemTime() {
        return new Date().getTime();
    }


    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {

        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {

        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }





    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * WiFi是否连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 移动网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 动态设置listView的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * utf8转gbk
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String utf8ToGBK(String str) {
        String utf8;
        try {
            utf8 = new String(str.getBytes("UTF-8"));
            String unicode = new String(utf8.getBytes(), "UTF-8");
            String gbk = new String(unicode.getBytes("GBK"));
            return gbk;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 文件删除
     *
     * @param filepath
     */
    public static boolean del(String filepath) {
        boolean flag = false;
        //定义文件路径
        File f = new File(filepath);
        if (f.exists() && f.isDirectory()) {
            //判断是文件还是目录
            if (f.listFiles().length == 0) {
                // 文件下没有目录直接删除
                return f.delete();
            } else {
                //有目录将目录下文件存放到数组中，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        //递归调用该方法并取得子目录路径
                        boolean b = del(delFile[j].getAbsolutePath());
                        if (!b) {
                            return false;
                        }
                    }
                    //删除文件
                    flag = delFile[j].delete();
                }
            }
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 图片变换为圆形
     *
     * @param bitmap
     * @param ratio
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, float ratio) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);

        return bitmap;

    }

    /**
     * Unicode转中文
     *
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }

                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    /**
     * 合并ArrayList并去掉重复数据
     *
     * @param list1
     * @param list2
     * @return
     */
    public static List unionList(List list1, List list2) {
        HashSet hashSet = new HashSet();
        Iterator iterator1 = list1.iterator();
        while (iterator1.hasNext()) {
            Object o = iterator1.next();
            hashSet.add(o);
        }
        if (list2 != null && list2.size() > 0) {
            Iterator iterator2 = list2.iterator();
            while (iterator2.hasNext()) {
                Object o = iterator2.next();
                hashSet.add(o);
            }
        }
        return new ArrayList(hashSet);
    }

    /**
     * 校验只输入中文
     *
     * @param str
     * @return
     */
    public static boolean isChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    public static boolean isAppAlive(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        String packageName = context.getPackageName();
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            MyLog.e("md5", "加密失败");
        }
        return "";
    }

    /**
     * 获取下载目录
     * <pre>path: /storage/emulated/0/Download</pre>
     *
     * @return 下载目录
     */
    public static String getExtDownloadsPath() {
        return Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath();
    }
}


