package com.clyr.testutils.activity;

import android.os.Bundle;
import android.os.Vibrator;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.ToastUtils;

import me.leolin.shortcutbadger.ShortcutBadger;

public class SystemUtilActivity extends BaseActivity {

    Vibrator vibrator;
    private int badgeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_util);
    }

    @Override
    protected void initView() {
        initBar();
        //    long[] patter = {1000, 1000, 2000, 50};
        //    vibrator.vibrate(patter,0);
        //    vibrator.cancel();
        //    vibrator.vibrate(10);
        //    long[] patter = {1000, 1000, 2000, 50};
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        findViewById(R.id.vibrator_short).setOnClickListener(v -> {
            long[] patter = {10, 10, 10, 10};
            vibrator.vibrate(patter, -1);
        });
        findViewById(R.id.vibrator_long).setOnClickListener(v -> {
            if (vibrator.hasVibrator()) {
                vibrator.cancel();
            } else {
                vibrator.vibrate(3000);
            }
        });


        findViewById(R.id.badge_add).setOnClickListener(v -> {
            badgeCount++;
            ShortcutBadger.applyCount(this, badgeCount);
        });
        findViewById(R.id.badge_subtract).setOnClickListener(v -> {
            if (badgeCount <= 0) {
                ToastUtils.showShort("已经清空了");
                return;
            }
            badgeCount--;
            ShortcutBadger.applyCount(this, 0);
        });
        findViewById(R.id.badge_remove).setOnClickListener(v -> ShortcutBadger.removeCount(this));
    }
}