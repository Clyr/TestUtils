package com.clyr.utils;

import static android.content.Context.NOTIFICATION_SERVICE;

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
import android.provider.Settings;

/**
 * Created by M S I of clyr on 2019/11/15.
 */
public class NotificationUtils {
    public static Activity activity;
    public static final int NOTIFICATION_ID = 1001;
    public static final int NOTIFICATION_ID3 = 1005;

    /**
     * 状态栏通知
     * @param cla 点击跳转的页面
     * @param drawable  设置图标
     * @param raw 提示声音
     */
    public static void sendNotification(Class<? extends Activity> cla, int drawable, int raw) {
        //1、NotificationManager
        NotificationManager manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        /** 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*/
        Intent inte = new Intent(activity, cla);
        PendingIntent contentIntent =
                PendingIntent.getActivity(activity, 0, inte, 0);
        long[] vibrate = new long[]{0, 500, 1000, 1500};

        Notification.Builder builder = new Notification.Builder(activity);
        builder.setContentInfo("Content info")
                .setContentTitle("TestListTitle")//设置通知标题
                .setContentText("Notification ContentText")//设置通知内容
                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), drawable))
                .setSmallIcon(drawable)//不能缺少的一个属性 通知栏小图标 默认圆头安卓很丑
                .setSubText("Subtext")
                .setTicker("滚动消息......")
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + raw))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001", "TestList通知测试", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        Notification noti = builder.build();
        noti.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        //3、manager.notify()
        manager.notify(NOTIFICATION_ID, noti);
    }


    public static void clearNotification() {
        //单利的系统服务
        NotificationManager manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
    }


    public static void sendProgressNotification(int drawable) {
        final NotificationManager manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(activity);
        builder.setSmallIcon(drawable)
                .setContentTitle("TestList")
                .setContentText("正在下载...")
                .setProgress(1000, 10, true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("005", "download_channel", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder.setChannelId("005");
        }

        Notification n = builder.build();
        n.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        manager.notify(NOTIFICATION_ID3, n);
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
                    builder.setProgress(1000, i * 1, false);
                    manager.notify(NOTIFICATION_ID3, n);
                }
                //更新通知内容
                manager.cancel(NOTIFICATION_ID3);
                builder.setProgress(0, 0, false);
                builder.setContentText("下载完毕");
//                Notification n = builder.build();
                n.flags = Notification.FLAG_AUTO_CANCEL;//不能删除通知
                manager.notify(NOTIFICATION_ID3, n);
            }
        }.start();
    }

    public static void sendNotification(Context context, Class<? extends Activity> cla, int drawable, int raw) {
        //1、NotificationManager
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        /** 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*/
        Intent inte = new Intent(context, cla);
        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, inte, 0);
        long[] vibrate = new long[]{0, 500, 1000, 1500};

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentInfo("TestList")
                .setContentTitle("TestList")//设置通知标题
                .setContentText("电源已充满")//设置通知内容
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), drawable))
                .setSmallIcon(drawable)//不能缺少的一个属性 通知栏小图标 默认圆头安卓很丑
                .setSubText("Subtext")
                .setTicker("滚动消息......")
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + raw))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001", "TestList电源通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        Notification noti = builder.build();
        // noti.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        noti.flags = Notification.FLAG_AUTO_CANCEL;//不能删除通知
        //3、manager.notify()
        manager.notify(NOTIFICATION_ID, noti);
    }
    /**
     * @param context  上下文
     * @param title    标题
     * @param content  内容
     * @param cla      点击跳转的Activity
     * @param drawable 图标
     * @return
     */
    public static Notification showNotfi(Context context, String title, String content, Class<? extends Activity> cla, int drawable) {
        String channelOneId = "getui_" + context.getString(R.string.app_name);
        CharSequence channelName = "个推";

        Intent intent1 = new Intent(context, cla);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = null;
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
        manager.notify(10001, notification);
        return notification;
    }

    public static Notification showNotfi(Context context, String title, String content, int drawable) {
        String channelOneId = context.getString(R.string.app_name) + "_Test";
        CharSequence channelName = context.getString(R.string.app_name);

        Notification notification = null;
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
        return notification;
    }
}
