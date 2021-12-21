/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.clyr.three_sdk.baidumapsdk.demo.createmap;

import android.os.Bundle;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.TextureMapView;
import com.clyr.three_sdk.R;


/**
 * 此Demo用来说明用TextureMapView显示地图，使用TextureMapView必须在AndroidManifest.xml中开启硬件加速
 */
public class TextureMapViewDemo extends AppCompatActivity {

    private TextureMapView mMapViewOne;
    private TextureMapView mMapViewTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_map_view_demo);
        mMapViewOne = (TextureMapView) findViewById(R.id.mTexturemap);
        mMapViewTwo = (TextureMapView) findViewById(R.id.mTexturemap2);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("texturehint").setIndicator("功能说明", null).setContent(R.id.texturehint));
        tabHost.addTab(tabHost.newTabSpec("mTexturemap").setIndicator("地图").setContent(R.id.mTexturemap));
        tabHost.addTab(tabHost.newTabSpec("textdesc").setIndicator("Scrollview页").setContent(R.id.textdesc));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时必须调用mMapView. onPause ()
        mMapViewOne.onPause();
        mMapViewTwo.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        mMapViewOne.onResume();
        mMapViewTwo.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapViewOne.onDestroy();
        mMapViewTwo.onDestroy();
    }
}
