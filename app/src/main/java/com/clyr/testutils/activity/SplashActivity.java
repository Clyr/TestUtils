package com.clyr.testutils.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;


public class SplashActivity extends BaseActivity {

    private int delayMillis = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {
        mHideHandler.postDelayed(mRunnable, delayMillis);
    }

    private final Handler mHideHandler = new Handler();
    private final Runnable mRunnable = () -> {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    };


}