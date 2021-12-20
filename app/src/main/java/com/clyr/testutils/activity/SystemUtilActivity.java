package com.clyr.testutils.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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


        findViewById(R.id.keyboard_show).setOnClickListener(v -> showSoftInputFromWindow(this, findViewById(R.id.edittext_none)));
        findViewById(R.id.keyboard_hide).setOnClickListener(v -> hideInput());
        findViewById(R.id.keyboard_hide2).setOnClickListener(v -> hintKeyBoard());
        showSoftInputFromWindow(this, findViewById(R.id.edittext_none));
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
}