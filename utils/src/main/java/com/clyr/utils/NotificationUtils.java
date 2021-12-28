package com.clyr.utils;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.clyr.base.AppKit;
import com.clyr.base.bean.NotificationType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M S I of clyr on 2019/11/15.
 */
public class NotificationUtils {
    public final int NOTIFICATION_ID = 1001;
    private final int NOTIFICATION_ID3 = 1005;

    private final String TAG = NotificationUtils.class.getSimpleName();

    private final String NOTIFICATION_NAME_ID = "notification_clyr";
    private final String NOTIFICATION_TEST_ID = "notification_clyr_test";
    private final String NOTIFICATION_DOWNLOAD_ID = "notification_clyr_download";
    private final int NOTIFICATION_ID_COMMON = 20211100;
    // 超时时间1天
    private final int DIALING_DURATION = 60 * 1000 * 60 * 24;
    private final Context mContext = AppKit.getContext();
    private final NotificationManager mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    @SuppressLint("StaticFieldLeak")
    private static NotificationUtils notificationUtils;
    private int flagNoClear = Notification.FLAG_AUTO_CANCEL;

    private final List<NotificationType> mList = new ArrayList<>();


    public static NotificationUtils init() {
        if (notificationUtils == null) {
            synchronized (NotificationUtils.class) {
                if (notificationUtils == null) {
                    notificationUtils = new NotificationUtils();
                }
            }
        }
        return notificationUtils;
    }

    /**
     * 状态栏通知
     *
     * @param cla      点击跳转的页面
     * @param drawable 设置图标
     * @param raw      提示声音
     */
    public void sendNotification(Class<? extends Activity> cla, int drawable, int raw) {
        /* 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*/
        Intent inte = new Intent(mContext, cla);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent contentIntent =
                PendingIntent.getActivity(mContext, 0, inte, 0);
        long[] vibrate = new long[]{0, 500, 1000, 1500};

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setContentTitle("TestListTitle")//设置通知标题
                .setContentText("Notification ContentText")//设置通知内容
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), drawable))
                .setSmallIcon(drawable)//不能缺少的一个属性 通知栏小图标 默认圆头安卓很丑
                .setSubText("Subtext")
                .setTicker("滚动消息......")
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.clyr.testutils/" + raw))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_TEST_ID, "TestList通知测试", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            mManager.createNotificationChannel(channel);
            builder.setChannelId(NOTIFICATION_TEST_ID);
        }

        Notification notification = builder.build();

        notification.flags = flagNoClear;//不能删除通知
        //3、manager.notify()
        mManager.notify(NOTIFICATION_ID, notification);
        addIDToList(new NotificationType(NOTIFICATION_ID, NotificationType.NotifiType.normal));

    }


    public void sendProgressNotification(int drawable) {
        final Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(drawable)
                .setContentTitle("TestList")
                .setContentText("正在下载...")
                .setProgress(1000, 10, true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(NOTIFICATION_DOWNLOAD_ID, "download_channel", NotificationManager.IMPORTANCE_LOW);
            mManager.createNotificationChannel(channel);
            builder.setChannelId(NOTIFICATION_DOWNLOAD_ID);
        }

        Notification n = builder.build();
        n.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        mManager.notify(NOTIFICATION_ID3, n);
        //每隔1秒更新进度条进度
        //启动工作线程
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Notification n = builder.build();
                //manager.notify(NOTIFICATION_ID3, n);
                for (int i = 1; i <= 1000; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //发通知
                    builder.setProgress(1000, i, false);
                    mManager.notify(NOTIFICATION_ID3, n);
                }
                //更新通知内容
                mManager.cancel(NOTIFICATION_ID3);
                builder.setProgress(0, 0, false);
                builder.setContentText("下载完毕");
//                Notification n = builder.build();
                n.flags = Notification.FLAG_AUTO_CANCEL;//不能删除通知
                mManager.notify(NOTIFICATION_ID3, n);
            }
        }.start();
        addIDToList(new NotificationType(NOTIFICATION_ID3, NotificationType.NotifiType.process));
    }

    public void sendNotification(Context context, Class<? extends Activity> cla, int drawable, int raw) {
        //1、NotificationManager
        /* 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*/
        Intent inte = new Intent(context, cla);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, inte, 0);
        long[] vibrate = new long[]{0, 500, 1000, 1500};

        Notification.Builder builder = new Notification.Builder(context);
        String app_name = context.getResources().getString(R.string.app_name);
        builder.setContentTitle(app_name)//设置通知标题
                .setContentText("电源已充满")//设置通知内容
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), drawable))
                .setSmallIcon(drawable)//不能缺少的一个属性 通知栏小图标 默认圆头安卓很丑
                .setSubText("Subtext")
                .setTicker("滚动消息......")
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.clyr.testutils/" + raw))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_TEST_ID, app_name + "电源通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            mManager.createNotificationChannel(channel);
            builder.setChannelId(NOTIFICATION_TEST_ID);
        }

        Notification notification = builder.build();
        // notification.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        notification.flags = flagNoClear;//不能删除通知
        //3、manager.notify()
        mManager.notify(NOTIFICATION_ID, notification);
        addIDToList(new NotificationType(NOTIFICATION_ID, NotificationType.NotifiType.normal));
    }

    /**
     * @param context  上下文
     * @param title    标题
     * @param content  内容
     * @param cla      点击跳转的Activity
     * @param drawable 图标
     * @return
     */
    public Notification showNotification(Context context, String title, String content, Class<? extends Activity> cla, int drawable) {
        String channelOneId = "getui_" + context.getString(R.string.app_name);
        CharSequence channelName = "个推";

        Intent intent1 = new Intent(context, cla);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Uri mUri = Settings.System.DEFAULT_NOTIFICATION_URI;

            NotificationChannel mChannel = new NotificationChannel(channelOneId, channelName, NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("description");
            mChannel.setSound(mUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            mManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context, channelOneId)
                    .setChannelId(channelOneId)
                    .setSmallIcon(drawable)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();

        } else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(drawable)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();
        }
        notification.defaults = Notification.DEFAULT_ALL;
        mManager.notify(10001, notification);
        addIDToList(new NotificationType(10001, NotificationType.NotifiType.normal));
        return notification;
    }

    public Notification showNotification(Context context, String title, String content, int drawable) {
        String channelOneId = context.getString(R.string.app_name) + "_Test";
        CharSequence channelName = context.getString(R.string.app_name);

        Notification notification;
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Uri mUri = Settings.System.DEFAULT_NOTIFICATION_URI;

            NotificationChannel mChannel = new NotificationChannel(channelOneId, channelName, NotificationManager.IMPORTANCE_LOW);
            mChannel.setDescription("description");
            mChannel.setSound(mUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            manager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context, channelOneId)
                    .setChannelId(channelOneId)
                    .setSmallIcon(drawable)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .build();

        } else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(drawable)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .build();
        }
        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(10001, notification);
        addIDToList(new NotificationType(10001, NotificationType.NotifiType.normal));
        return notification;
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    public void createNotificationChannel(@NotNull String title, @NotNull String desc) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel;
            channel = new NotificationChannel(NOTIFICATION_NAME_ID, mContext.getString(R.string.notification_name), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(mContext.getString(R.string.description_tip));
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            mManager.createNotificationChannel(channel);
        }

        int notifiId = (int) SystemUtils.getSystemTime() / 100;

        final Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(mContext, NOTIFICATION_NAME_ID);
            builder.setTimeoutAfter(DIALING_DURATION);
        } else {
            builder = new Notification.Builder(mContext);
        }

        String tickerStr = mContext.getString(R.string.new_msg);
        builder.setTicker(tickerStr).setWhen(System.currentTimeMillis());

        builder.setContentTitle(title);
        builder.setContentText(desc);

        builder.setSmallIcon(R.drawable.logo);

        /*Intent launch;
        launch = mContext.getPackageManager().getLaunchIntentForPackage("com.heiguang.hgrcwandroid");
        launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        launch = new Intent(mContext, MainActivity.class);
        launch.putExtra("", "");
        builder.setContentIntent(PendingIntent.getActivity(mContext,
                (int) SystemClock.uptimeMillis(), launch, PendingIntent.FLAG_UPDATE_CURRENT));*/
        builder.setAutoCancel(true);
        Notification notification = builder.build();

        mManager.cancel(notifiId);
        //mManager.cancelAll();
        notification.flags = flagNoClear;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notification.defaults = Notification.DEFAULT_ALL;
        }
        mManager.notify(notifiId, notification);
        addIDToList(new NotificationType(notifiId, NotificationType.NotifiType.normal));
    }


    public void cancelNotification(int notifiId) {
        mManager.cancel(notifiId);
        deleteIDToList(notifiId);
    }

    public void cancelAll() {
        mManager.cancelAll();
        mList.clear();
    }

    public NotificationUtils setFlagClear() {
        flagNoClear = Notification.FLAG_AUTO_CANCEL;
        return this;
    }

    public NotificationUtils setFlagNoClear() {
        flagNoClear = Notification.FLAG_NO_CLEAR;
        return this;
    }

    private void addIDToList(NotificationType notificationType) {
        if (!mList.contains(notificationType)) {
            mList.add(notificationType);
        } else {
            MyLog.loge("已经有这个id了");
        }
    }

    private void deleteIDToList(int notifiId) {
        for (NotificationType notification : mList) {
            if (notification.getNotifiId() == notifiId) {
                mList.remove(notification);
                return;
            }
        }
    }
}
