package com.clyr.testutils.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.MyLog;
import com.google.gson.Gson;


public class SplashActivity extends BaseActivity {

    private int delayMillis = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initView() {
        //StatusBarUtil.setColor(this, Color.WHITE,0);
        mHideHandler.postDelayed(mRunnable, delayMillis);

        initTestData();
    }

    private void initTestData() {
        NumberDouble numberDouble = new NumberDouble();
        numberDouble.setCount(1.0);
        numberDouble.setName("NumberDouble");

        MyLog.loge(new Gson().toJson(numberDouble));

        String json = new Gson().toJson(numberDouble);
        NumberInt numberInt = new Gson().fromJson(json, NumberInt.class);
        MyLog.loge(new Gson().toJson(numberInt));
        MyLog.loge(numberInt.toString());
    }

    private final Handler mHideHandler = new Handler();
    private final Runnable mRunnable = () -> {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    };

    class NumberInt{
        String name;
        int count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "NumberInt{" +
                    "name='" + name + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
    class NumberDouble{
        String name;
        double count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }
    }
}