package com.clyr.testutils.activity;

import android.os.Bundle;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.three_sdk.baidumapsdk.WebViewForBaiduMap;
import com.clyr.three_sdk.baidumapsdk.demo.BMapApiDemoMain;

public class MapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.baidumap).setOnClickListener(v -> startActivity(BMapApiDemoMain.class));
        findViewById(R.id.baidumap_webview).setOnClickListener(v -> startActivity(WebViewForBaiduMap.class));
    }
}