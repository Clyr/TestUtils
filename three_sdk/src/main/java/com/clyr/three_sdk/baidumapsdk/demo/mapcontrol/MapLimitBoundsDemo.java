package com.clyr.three_sdk.baidumapsdk.demo.mapcontrol;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.clyr.three_sdk.R;


/**
 * 设置地图的可移动区域
 */

public class MapLimitBoundsDemo extends AppCompatActivity implements BaiduMap.OnMapLoadedCallback{

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limitbounds);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
    }

    /**
     * 设置地图的可移动区域，只有在 OnMapLoadedCallback.onMapLoaded() 之后设置才生效
     */
    @Override
    public void onMapLoaded() {
        LatLng southwest = new LatLng(39.92235, 116.380338);
        LatLng northeast = new LatLng(39.947246, 116.414977);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(northeast)// 东北角坐标
                .include(southwest)// 西南角坐标
                .build();
        // 设置地图的可移动区域
        mBaiduMap.setMapStatusLimits(bounds);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }
}
