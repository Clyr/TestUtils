package com.clyr.testutils.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;

public class PushActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
    }

    @Override
    protected void initView() {

    }
}