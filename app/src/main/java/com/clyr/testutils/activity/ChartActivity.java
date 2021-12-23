package com.clyr.testutils.activity;

import android.os.Bundle;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;

public class ChartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.radar).setOnClickListener(v -> startActivity(RadarActivity.class));
        findViewById(R.id.table).setOnClickListener(v -> startActivity(TableActivity.class));
        findViewById(R.id.chart).setOnClickListener(v -> startActivity(TableActivity.class));
    }
}