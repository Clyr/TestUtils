package com.clyr.testutils.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;

public class MapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    @Override
    protected void initView() {

    }
}