package com.clyr.three_sdk.baidumapsdk.demo.createmap;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.SupportMapFragment;


/**
 * 此demo演示了用SupportMapFragment 来创建地图
 */
public class MapFragmentDemo extends FragmentActivity {
    @SuppressWarnings("unused")
    private static final String LTAG = MapFragmentDemo.class.getSimpleName();
    // SupportMapFragment 中已经实现地图生命周期的管理
    private SupportMapFragment map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Intent intent = getIntent();
        MapStatus.Builder builder = new MapStatus.Builder();
        if (intent.hasExtra("x") && intent.hasExtra("y")) {
            // 当用intent参数时，设置中心点为指定点
            Bundle bundle = intent.getExtras();
            LatLng centre = new LatLng(bundle.getDouble("y"), bundle.getDouble("x"));
            builder.target(centre);
        }
        builder.overlook(-20).zoom(15);
        BaiduMapOptions baiduMapOptions = new BaiduMapOptions().mapStatus(builder.build())
                .compassEnabled(false) // 设置是否允许指南针，默认允许。
                .zoomControlsEnabled(false); // 设置是否显示缩放控件
        map = SupportMapFragment.newInstance(baiduMapOptions);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.map, map, "map_fragment").commit();
    }
}
