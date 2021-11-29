package com.clyr.testutils.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;

public class EmptyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
    }

    @Override
    protected void initView() {

    }
}