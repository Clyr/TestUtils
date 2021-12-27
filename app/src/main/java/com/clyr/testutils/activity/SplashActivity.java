package com.clyr.testutils.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {
        //StatusBarUtil.setColor(this, Color.WHITE,0);
        mHideHandler.postDelayed(mRunnable, 1000);
    }

    private final Handler mHideHandler = new Handler();
    private final Runnable mRunnable = () -> {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    };

}