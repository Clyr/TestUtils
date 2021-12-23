package com.clyr.testutils.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.NotificationUtils;
import com.clyr.utils.SystemUtils;
import com.clyr.utils.ToastUtils;
import com.clyr.view.captcha.SwipeCaptchaView;

import java.util.Arrays;

import me.leolin.shortcutbadger.ShortcutBadger;

public class SystemUtilActivity extends BaseActivity {
    private final String TAG = SystemUtilActivity.class.getSimpleName();

    Vibrator vibrator;
    private int badgeCount = 0;
    public static String shortcuts = "SHORTCUTS";

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


        findViewById(R.id.keyboard_show).setOnClickListener(v -> showSoftInputFromWindow(this, findViewById(R.id.edittext_none)));
        findViewById(R.id.keyboard_hide).setOnClickListener(v -> hideInput());
        findViewById(R.id.keyboard_hide2).setOnClickListener(v -> hintKeyBoard());


        findViewById(R.id.fingerprint).setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                ToastUtils.showShort("当前设备不支持该功能");
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                SystemUtils.BiometricPrint();
            } else {
                SystemUtils.Fingerprint(this);
            }
        });


        //showSoftInputFromWindow(this, findViewById(R.id.edittext_none));




        //获取电量
        findViewById(R.id.batterystate).setOnClickListener(v -> getBatteryState());

        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        ToastUtils.showShort("当前电量， " + battery + " %");

        registerReceiver(mbatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        unregisterReceiver(mbatteryReceiver);


        findViewById(R.id.shortcuts).setOnClickListener(v -> {
            ShortcutManager shortcutManager;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
                shortcutManager = getSystemService(ShortcutManager.class);

                ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "shortcut1")
                        .setShortLabel("TestUtils")
                        .setLongLabel("TestUtils-Github")
                        .setIcon(Icon.createWithResource(this, R.drawable.test))
                        .setIntent(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/Clyr/TestUtils.git")))
                        .build();
                Intent intent = new Intent(SystemUtilActivity.this, MainActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("SHORTCUTS", shortcuts);
                ShortcutInfo shortcut2 = new ShortcutInfo.Builder(this, "shortcut2")
                        .setShortLabel("TestUtils")
                        .setLongLabel("TestUtils-Main")
                        .setIcon(Icon.createWithResource(this, R.drawable.logo))
                        .setIntent(intent)
                        .build();

                Intent intent2 = new Intent(SystemUtilActivity.this, SystemUtilActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                //intent.putExtra("SHORTCUTS", shortcuts);
                ShortcutInfo shortcut3 = new ShortcutInfo.Builder(this, "shortcut3")
                        .setShortLabel("系统工具")
                        .setLongLabel("打开系统工具")
                        .setIcon(Icon.createWithResource(this, R.drawable.logo))
                        .setIntent(intent)
                        .build();

                shortcutManager.setDynamicShortcuts(Arrays.asList(shortcut3, shortcut2, shortcut1));


            }


            /*if (Build.VERSION.SDK_INT >= 25) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(this, "shortcut_id_search")
                        .setShortLabel(getString(R.string.lable_shortcut_static_search_disable))

                        .setLongLabel(getString(R.string.lable_shortcut_static_search_disable))
                        .setIcon(Icon.createWithResource(this, R.drawable.test))
                        .setIntent(intent)
                        .build();

                ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
                //这样就可以通过长按图标显示出快捷方式了
                shortcutManager.setDynamicShortcuts(Arrays.asList(shortcutInfo));

            }*/
        });
    }


    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }



    private void getBatteryState() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = registerReceiver(null, ifilter);
        //如果设备正在充电，可以提取当前的充电状态和充电方式（无论是通过 USB 还是交流充电器），如下所示：
        // Are we charging / charged?
        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        // How are we charging?
        int chargePlug = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        int batteryPct = level * 100 / scale;
        if (batteryPct >= 100) {
            NotificationUtils.init().sendNotification(this, MainActivity.class, R.drawable.logo, R.raw.fadeout);
        }
        String nowbattery = "当前电量 " + batteryPct + "%";

        if (isCharging) {
            if (usbCharge) {
                Toast.makeText(SystemUtilActivity.this, "USB连接！ " + nowbattery, Toast.LENGTH_SHORT).show();
            } else if (acCharge) {
                Toast.makeText(SystemUtilActivity.this, "通过电源充电中！ " + nowbattery, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SystemUtilActivity.this, "未连接USB线！ " + nowbattery, Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver mbatteryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    ToastUtils.showShort("充电中!");
                }
            } else {
                ToastUtils.showShort("未充电!");
            }
        }
    };

}