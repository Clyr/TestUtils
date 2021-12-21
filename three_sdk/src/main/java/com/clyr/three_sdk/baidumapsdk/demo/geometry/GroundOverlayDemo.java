package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.clyr.three_sdk.R;


/**
 * 介绍GroundOverlay的绘制
 */

public class GroundOverlayDemo extends AppCompatActivity {

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ground_overlay);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groundoverlay);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 添加GroundOverlay
        addGroundOverlay();
    }

    public void addGroundOverlay(){
        LatLng southwest = new LatLng(39.92235, 116.380338);
        LatLng northeast = new LatLng(39.947246, 116.414977);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();

        OverlayOptions ooGround = new GroundOverlayOptions().positionFromBounds(bounds).image(mBitmap).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);

        // 设置地图中心点以及缩放级别
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(bounds.getCenter(),15.0f);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清除所有图层
        mBaiduMap.clear();
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
        // 资源回收，防止内存泄露
        mBitmap.recycle();
    }
}
