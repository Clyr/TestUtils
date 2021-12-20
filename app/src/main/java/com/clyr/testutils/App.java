package com.clyr.testutils;

import static com.xuexiang.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;

import androidx.multidex.MultiDexApplication;

import com.clyr.base.AppKit;
import com.clyr.testutils.activity.MainActivity;
import com.clyr.testutils.utils.OKHttpUpdateHttpService;
import com.clyr.utils.MyLog;
import com.clyr.utils.UtilsKit;
import com.clyr.utils.utilshelper.ACache;
import com.lzy.okgo.OkGo;
import com.tencent.bugly.crashreport.CrashReport;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;


/**
 * Created by clyr on 2018/4/2 0002.
 */

public class App extends MultiDexApplication {
    private static App mApp = null;
    // user your appid the key.
    private static final String APP_ID = "2882303761518025201";
    // user your appid the key.
    private static final String APP_KEY = "5241802589201";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "com.xiaomi.mipushdemo";

    private static DemoHandler sHandler = null;
    private static MainActivity sMainActivity = null;
    private static Context mContext;
    public static final String CACHE_NAME = "picasso-cache";
    private static ACache mACache;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mApp = this;
        UtilsKit.init(this);
        //设置 1.可以访问所有https  请求时间延长为15s 这个过程会显示dialog 已经设置可以取消--但是网络访问不会取消（某些单页的网络请求过多）
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
        OkGo.init(this);
        if (sHandler == null) {
            sHandler = new DemoHandler(getApplicationContext());
        }

        mContext = this;
        mACache = ACache.get(this, CACHE_NAME);

        AppKit.init(this);

        /* Bugly SDK初始化
         * 参数1：上下文对象
         * 参数2：APPID，平台注册时得到,注意替换成你的appId
         * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
         */
        CrashReport.initCrashReport(getApplicationContext(), "e349a84b8a", true);
        XUpdate.get()
                .debug(true)
                .isWifiOnly(true)                                               //默认设置只在wifi下检查版本更新
                //.isGet(true)                                                    //默认设置使用get请求检查版本
                .isAutoMode(false)                                              //默认设置非自动模式，可根据具体使用配置
                //.param("versionCode", UpdateUtils.getVersionCode(this))         //设置默认公共请求参数
                //.param("appKey", getPackageName())
                .setOnUpdateFailureListener(new OnUpdateFailureListener() {     //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        if (error.getCode() != CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
                            //ToastUtils.toast(error.toString());
                        }
                    }
                })
                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService())           //这个必须设置！实现网络请求功能。
                .init(this);

    }

    public static App getApplication() {
        return mApp;
    }

    public static App getContext() {
        return mApp;
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    public static DemoHandler getHandler() {
        return sHandler;
    }

    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }

    public static class DemoHandler extends Handler {

        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            if (sMainActivity != null) {
//                sMainActivity.refreshLogInfo();
            }
            if (!TextUtils.isEmpty(s)) {
//                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
            if (msg.what == 1) {
                MyLog.d(TAG, s);
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("tag", "share");
                context.startActivity(intent);
            }
        }
    }

    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        //当有未捕获的bug的时候调用的方法
        //ex : 异常
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            System.out.println("哥发现异常了,哥捕获了....");
            try {
                //将异常信息保存到本地文件中
                String s = ex.toString();
                MyLog.e(s);
                ex.printStackTrace(new PrintStream(new File("mnt/sdcard/error.log")));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //自杀
            //闪退操作
            Process.killProcess(Process.myPid());
        }

    }

    //GrenDao
    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
   /* private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "aserbao.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private DaoSession daoSession;
    public DaoSession getDaoSession() {
        return daoSession;
    }*/
}
