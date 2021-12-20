package com.clyr.testutils.activity;

import android.os.Bundle;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.NotificationUtils;

public class NotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.notifi).setOnClickListener(v -> NotificationUtils.init().sendNotification(MainActivity.class, R.drawable.logo, com.clyr.base.R.raw.fadeout));
        findViewById(R.id.notifi_no_cancle).setOnClickListener(v -> NotificationUtils.init().setFlagNoClear().sendNotification(MainActivity.class, R.drawable.logo, com.clyr.base.R.raw.fadeout));
        findViewById(R.id.notifi_process).setOnClickListener(v -> NotificationUtils.init().sendProgressNotification(R.drawable.logo));
        findViewById(R.id.notifi_cancle).setOnClickListener(v -> {
            NotificationUtils init = NotificationUtils.init();
            init.cancelNotification(init.NOTIFICATION_ID);
        });
        findViewById(R.id.notifi_cancle_all).setOnClickListener(v -> NotificationUtils.init().cancelAll());
    }
}