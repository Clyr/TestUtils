package com.clyr.testutils.push;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.clyr.testutils.App;
import com.clyr.testutils.R;
import com.clyr.testutils.activity.MainActivity;
import com.clyr.testutils.activity.SplashActivity;
import com.clyr.utils.MessageEvent;
import com.clyr.utils.MyLog;
import com.clyr.utils.PublicTools;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Set;

/**
 * Created by 11635 of clyr on 2021/12/20.
 */

public class PushUtils {
    public static final String TAG = PushUtils.class.getSimpleName();
    public static final String pushTag = "PushUtils";
    public static final String notificationId = "IM_PUSH";

    @SuppressLint("StaticFieldLeak")
    private static PushUtils pushUtils;
    private final Context mContext = App.getContext();
    String dType = Build.MANUFACTURER;

    public static PushUtils init() {
        if (pushUtils == null) {
            synchronized (PushUtils.class) {
                if (pushUtils == null) {
                    pushUtils = new PushUtils();
                }
            }
        }
        return pushUtils;
    }

    public void initJPush() {
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(mContext);
//        JPushInterface.setAlias(mContext, 0, "123");
    }

    public void initGtPush() {
//        PushManager.getInstance().initialize(mContext);
//        PushManager.getInstance().bindAlias(mContext, "123");
    }

    /**
     * 初始化推送服务
     *
     * @return this
     */
    public PushUtils registerPush() {
        //huweiTest();
        //初始化push推送服务
        if (isXiaomi()) {
            if (shouldInit()) {
                //MiPushClient.registerPush(mContext, PushConst.APP_XIAOMI_ID, PushConst.APP_XIAOMI_KEY);
            }
        } else if (isHuawei()) {
            getHCMToken();
        } else if (isOppo()) {
            createNotificationChannel();
        } else if (isVivo()) {
            vivoRegisterPush();
        } else if (isMeizu()) {
            //com.meizu.cloud.pushsdk.PushManager.register(mContext, PushConst.APP_MEIZU_ID, PushConst.APP_MEIZU_KEY);
        } else {
            MyLog.d(TAG, "当前设备类型 - " + dType + "，未开启离线推送");
        }
        return this;
    }


    /**
     * 设置推送别名 & 打开推送（暂时用不到别名推送，通过唯一标识或者token）
     *
     * @param alias ...
     */
    public void setAlias(String alias) {
        MyLog.d(TAG, "当前设备类型 - " + dType + " - alias = " + alias);
        if (isXiaomi()) {
            /*MiPushClient.setAlias(mContext, alias, null);
            MiPushClient.turnOnPush(mContext, codeResult -> {

            });*/
        } else if (isVivo()) {
            /*PushClient.getInstance(mContext).bindAlias(alias, i -> {

            });
            PushClient.getInstance(mContext).turnOnPush(i -> {

            });*/
        } else if (isMeizu()) {
            //com.meizu.cloud.pushsdk.PushManager.subScribeAlias(mContext, PushConst.APP_MEIZU_ID, PushConst.APP_MEIZU_KEY, com.meizu.cloud.pushsdk.PushManager.getPushId(mContext), alias);
        } else if (isHuawei()) {

        } else if (isOppo()) {

        } else {

        }

    }

    /**
     * 解绑别名 暂停推送
     *
     * @param alias ...
     */
    public void unSetAlias(String alias) {
        if (alias == null) {
            MyLog.d(TAG, dType + " 解绑失败");
            return;
        }
        /*if (isXiaomi()) {
            MiPushClient.unsetAlias(mContext, alias, null);
            //MiPushClient.unregisterPush(mContext);
            MiPushClient.turnOffPush(mContext, codeResult -> {

            });
        } else if (isHuawei()) {
            HmsMessaging.getInstance(mContext).turnOffPush();
            //deleteHCMToken();
        } else if (isOppo()) {
            oppoUnRegister();
        } else if (isVivo()) {
            PushClient.getInstance(mContext).unBindAlias(alias, i -> {

            });
            PushClient.getInstance(mContext).turnOffPush(i -> {

            });
        } else if (isMeizu()) {
            com.meizu.cloud.pushsdk.PushManager.unSubScribeAlias(mContext, PushConst.APP_MEIZU_ID, PushConst.APP_MEIZU_KEY, com.meizu.cloud.pushsdk.PushManager.getPushId(mContext), alias);
            com.meizu.cloud.pushsdk.PushManager.unRegister(mContext, PushConst.APP_MEIZU_ID, PushConst.APP_MEIZU_KEY);
        } else {

        }*/

    }

    /**
     * 设置点击跳转
     * 小米/vivo
     *
     * @param context ...
     */
    public void setIntentData(Context context) {
        if (PublicTools.isAppAlive(mContext)) {
            EventBus.getDefault().post(new MessageEvent("", MessageEvent.PUSH));
            try {
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.heiguang.hgrcwandroid");
                intent.putExtra(PushUtils.pushTag, PushUtils.pushTag);
                mContext.startActivity(intent);
            } catch (Exception e) {
                MyLog.e("Exception", e.getMessage());
            }
            return;
        }
        //EventBus.getDefault().post(new MessageEvent("", MessageEvent.PUSH));
        Intent splashIntent = new Intent(context, SplashActivity.class);
        splashIntent.setAction(Intent.ACTION_VIEW);
        splashIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        splashIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        splashIntent.putExtra(PushUtils.pushTag, PushUtils.pushTag);
        context.startActivity(splashIntent);
    }

    public void setIntentDataForMeizu(Context context, boolean isAlive) {
        MyLog.loge("setIntentDataForMeizu - " + isAlive);
        if (isAlive) {
            EventBus.getDefault().post(new MessageEvent("", MessageEvent.PUSH));
            try {
                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.heiguang.hgrcwandroid");
                intent.putExtra(PushUtils.pushTag, PushUtils.pushTag);
                mContext.startActivity(intent);
            } catch (Exception e) {
                MyLog.e("Exception", e.getMessage());
            }
        } else {
            Intent splashIntent = new Intent(context, SplashActivity.class);
            splashIntent.setAction(Intent.ACTION_VIEW);
            splashIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            splashIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            splashIntent.putExtra(PushUtils.pushTag, PushUtils.pushTag);
            context.startActivity(splashIntent);
        }
    }

    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }


    public boolean getActivityIsForground() {
        Intent intent = new Intent(mContext, MainActivity.class);
        ComponentName componentName = intent.resolveActivity(mContext.getPackageManager());
        ActivityManager am = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(10);
        ActivityManager.RunningTaskInfo info = runningTasks.get(0);
        for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
            MyLog.loge(taskInfo.baseActivity.getClassName() + taskInfo.baseActivity.getShortClassName());
        }
        return false;
    }


    /**
     * VIVO 初始化推送服务
     */
    private void vivoRegisterPush() {
        /*try {
            PushClient.getInstance(mContext).initialize();
            PushClient.getInstance(mContext).turnOnPush(i -> {
                // 开关状态处理， 0代表成功
                if (0 == i) {
                    //setOfflinePushConfig(PushClient.getInstance(mContext).getRegId());
                } else {
                    MyLog.d(TAG, dType + "初始化失败");
                }
            });
        } catch (Exception e) {
            MyLog.e(TAG, dType + e.getMessage());
        }*/

    }


    /**
     * OPPO 创建的 ChannelID
     * 按照 OPPO 官网要求，在 OPPO Android 8.0 及以上系统版本必须配置 ChannelID，
     * 否则推送消息无法展示。您需要先在 App 中创建对应的 ChannelID
     */
    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(notificationId, mContext.getString(R.string.off_noti_name), importance);
            channel.setDescription(mContext.getString(R.string.off_tip));
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        /*HeytapPushManager.init(mContext, true);
        HeytapPushManager.register(mContext, PushConst.APP_OPPO_KEY, PushConst.APP_OPPO_APPSECRET, new ICallBackResultService() {
            @Override
            public void onRegister(int i, String registerID) {
                //setOfflinePushConfig(registerID);
                HeytapPushManager.resumePush();
                HeytapPushManager.requestNotificationPermission();
            }

            @Override
            public void onUnRegister(int i) {

            }

            @Override
            public void onSetPushTime(int i, String s) {

            }

            @Override
            public void onGetPushStatus(int i, int i1) {

            }

            @Override
            public void onGetNotificationStatus(int i, int i1) {

            }
        });
//        HeytapPushManager.getRegister();
//        HeytapPushManager.resumePush();*/
    }

    private void oppoUnRegister() {
        //HeytapPushManager.unRegister();
        //HeytapPushManager.pausePush();
    }

    /**
     * 获取 intentUri 的测试方法
     */
    public void huweiTest() {
        //华为

        //Intent intent = new Intent(Intent.ACTION_VIEW);
        Intent intent = new Intent(mContext, SplashActivity.class);
        // Scheme协议（pushscheme://com.huawei.codelabpush/deeplink?）需要开发者自定义
        intent.setData(Uri.parse("hgrcw"));
        // 往intent中添加参数，用户可以根据自己的需求进行添加参数：
        intent.putExtra(pushTag, pushTag);
       /* // 必须带上该Flag
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
        // 打印出的intentUri值就是设置到推送消息中intent字段的值
        MyLog.d("intentUri", intentUri);
        //  hgrcw#Intent;launchFlags=0x4000000;component=com.heiguang.hgrcwandroid/.activity.SplashActivity;S.PushUtils=PushUtils;end

        Intent intent2 = new Intent();
        intent2.setData(Uri.parse("pushscheme://com.tencent.qcloud.tim/detail"));
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri2 = intent2.toUri(Intent.URI_INTENT_SCHEME);
        MyLog.i(TAG, "intentUri = " + intentUri2);

        //OPPO regId = OPPO_CN_4e352261284646ed4d6da306cca5e691 / bid = 20595
*/
        //Intent intent = new Intent(this, CustomActivity.class);

        //intent.setData(Uri.parse("vpushscheme://com.vivo.pushtest/detail?key=value"));

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        String intentUrl = intent.toUri(Intent.URI_INTENT_SCHEME);
        //hgrcw#Intent;launchFlags=0x10000000;component=com.heiguang.hgrcwandroid/.activity.SplashActivity;S.PushUtils=PushUtils;end
        MyLog.d("intentUri", intentUrl);
    }

    /**
     * 华为推送
     */
    private void getHCMToken() {
        /*new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(mContext).getString("client/app_id");
                    String token = HmsInstanceId.getInstance(mContext).getToken(appId, "HCM");
                    MyLog.i(TAG, "get token:" + token);
                    HmsMessaging.getInstance(mContext).turnOnPush();
                    if (!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }
                } catch (ApiException e) {
                    MyLog.e(TAG, "get token failed, " + e);
                }
            }
        }.start();*/
    }

    /**
     * 华为推送
     */
    public void sendRegTokenToServer(String token) {
        MyLog.i(TAG, "sending token to server. token:" + token);
        //setOfflinePushConfig(token);
    }

    /**
     * 华为推送
     */
    public void deleteHCMToken() {
        /*new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(mContext).getString("client/app_id");
                    HmsInstanceId.getInstance(mContext).deleteToken(appId, "HCM");
                    MyLog.i(TAG, "deleteToken success.");
                } catch (ApiException e) {
                    MyLog.e(TAG, "deleteToken failed." + e);
                }
            }
        }.start();*/
    }

    /**
     * 小米推送的
     *
     * @return ...
     */
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = mContext.getApplicationInfo().processName;
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


    public boolean isHuawei() {
        return "HUAWEI".equalsIgnoreCase(dType) || "HUAWEI".equals(Build.BRAND);
    }

    public boolean isXiaomi() {
        return "XIAOMI".equalsIgnoreCase(dType) || "XIAOMI".equals(Build.BRAND);
    }

    public boolean isVivo() {
        return "vivo".equalsIgnoreCase(dType) || "vivo".equals(Build.BRAND);
    }

    public boolean isOppo() {
        return "oppo".equalsIgnoreCase(dType) || "oppo".equals(Build.BRAND);

    }

    public boolean isMeizu() {
        return "meizu".equalsIgnoreCase(Build.BRAND)
                || "meizu".equalsIgnoreCase(dType)
                || "22c4185e".equalsIgnoreCase(Build.BRAND);
    }

    public boolean isOther() {
        return !isHuawei() && !isMeizu() && !isOppo() && !isVivo() && !isXiaomi();
    }

    /**
     * 程序置于后台的时候是否使用本地自创建的通知
     * 华为和oppo使用的服务端创建的intent方式打开程序，只能跳转到splash
     *
     * @return ...
     */
    public boolean isLocalNotification() {
        return isHuawei() || isOppo() || isMeizu();
    }

    /**
     * 处理Oppo手机推送点击事件
     *
     * @param intent ...
     * @return ...
     */
    public String detailPushClick(Intent intent) {
        /*OfflineMessageBean omb = parseOfflineMessage(intent);
        if (omb != null && pushTag.equals(omb.pushTag)) {
            return pushTag;
        }*/
        return "";
    }


   /* public OfflineMessageBean parseOfflineMessage(Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return null;
            }
            String ext = bundle.getString("ext");
            MyLog.i(TAG, "push custom data ext: " + ext);
            if (TextUtils.isEmpty(ext)) {
                ext = getOPPOMessage(bundle);
                return getOfflineMessageBean(ext);
            } else {
                return getOfflineMessageBeanFromContainer(ext);
            }
        } catch (Exception e) {
            MyLog.Log(e.getMessage());
            return null;
        }
    }*/


    public String getOPPOMessage(Bundle bundle) {
        Set<String> set = bundle.keySet();
        if (set != null) {
            for (String key : set) {
                Object value = bundle.get(key);
                MyLog.i(TAG, "push custom data key: " + key + " value: " + value);
                if (TextUtils.equals("entity", key)) {
                    return value.toString();
                }
            }
        }
        return null;
    }

   /* private OfflineMessageBean getOfflineMessageBean(String ext) {
        if (TextUtils.isEmpty(ext)) {
            return null;
        }
        OfflineMessageBean bean = new Gson().fromJson(ext, OfflineMessageBean.class);
        return offlineMessageBeanValidCheck(bean);
    }

    private OfflineMessageBean getOfflineMessageBeanFromContainer(String ext) {
        if (TextUtils.isEmpty(ext)) {
            return null;
        }
        OfflineMessageContainerBean bean = null;
        try {
            bean = new Gson().fromJson(ext, OfflineMessageContainerBean.class);
        } catch (Exception e) {
            MyLog.e(TAG, "getOfflineMessageBeanFromContainer: " + e.getMessage());
        }
        if (bean == null) {
            return null;
        }
        return offlineMessageBeanValidCheck(bean.entity);
    }

    private OfflineMessageBean offlineMessageBeanValidCheck(OfflineMessageBean bean) {
        if (bean == null) {
            return null;
        } else if (bean.version != 1
                || (bean.action != OfflineMessageBean.REDIRECT_ACTION_CHAT
                && bean.action != OfflineMessageBean.REDIRECT_ACTION_CALL)) {
            PackageManager packageManager = mContext.getPackageManager();
            String label = String.valueOf(packageManager.getApplicationLabel(mContext.getApplicationInfo()));
            HGToast.showToast(mContext.getString(R.string.you_app) + label + mContext.getString(R.string.low_version));
            MyLog.e(TAG, "unknown version: " + bean.version + " or action: " + bean.action);
            return null;
        }
        return bean;
    }*/

    /**
     * oppo设置角标
     *
     * @param context ...
     * @param number  ...
     */
    public void setBadgeNumberForOppo(Context context, int number) {
        if (!isOppo()) {
            return;
        }
        try {
            if (number == 0) {
                number = -1;
            }
            Intent intent = new Intent("com.oppo.unsettledevent");
            intent.putExtra("pakeageName", context.getPackageName());
            intent.putExtra("number", number);
            intent.putExtra("upgradeNumber", number);
            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent);
            } else {
                try {
                    Bundle extras = new Bundle();
                    extras.putInt("app_badge_count", number);
                    context.getContentResolver().call(Uri.parse("content://com.android.badge/badge"), "setAppBadgeCount", null, extras);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }

    /**
     * vivo设置角标 仅支持在线设置
     *
     * @param context ...
     * @param number  ...
     */
    @SuppressLint("WrongConstant")
    public void setBadgeNumberForVivo(Context context, int number) {
        if (!isVivo()) {
            return;
        }
        try {
            Intent intent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            String launchClassName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
            intent.putExtra("className", launchClassName);
            intent.putExtra("notificationNum", number);
            intent.addFlags(0x01000000);//FLAG_RECEIVER_INCLUDE_BACKGROUND
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
