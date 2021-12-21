package com.clyr.three_sdk.baidumapsdk.demo.createmap;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.SupportMapFragment;


/**
 * 在一个Activity中展示多个地图
 */
public class MultiMapViewDemo extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimap);
        initMap();
    }

    /**
     * 初始化Map,设置不同城市为地图中心点，设置Logo不同位置
     */
    private void initMap() {
        LatLng latLngA = new LatLng(39.945, 116.404);
        LatLng latLngB = new LatLng(31.227, 121.481);
        LatLng latLngC = new LatLng(23.155, 113.264);
        LatLng latLngD = new LatLng(22.560, 114.064);
        // 北京为地图中心，logo在左上角
        MapStatusUpdate mapStatusUpdateA = MapStatusUpdateFactory.newLatLng(latLngA);
        SupportMapFragment supportMapFragmentA = (SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map1));
        supportMapFragmentA.getBaiduMap().setMapStatus(mapStatusUpdateA);
        supportMapFragmentA.getMapView().setLogoPosition(LogoPosition.logoPostionleftTop);

        // 上海为地图中心，logo在右上角
        MapStatusUpdate mapStatusUpdateB = MapStatusUpdateFactory.newLatLng(latLngB);
        SupportMapFragment supportMapFragmentB = (SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map2));
        supportMapFragmentB.getBaiduMap().setMapStatus(mapStatusUpdateB);
        supportMapFragmentB.getMapView().setLogoPosition(LogoPosition.logoPostionRightTop);

        // 广州为地图中心，logo在左下角*
        MapStatusUpdate mapStatusUpdateC = MapStatusUpdateFactory.newLatLng(latLngC);
        SupportMapFragment supportMapFragmentC = (SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map3));
        supportMapFragmentC.getBaiduMap().setMapStatus(mapStatusUpdateC);
        supportMapFragmentC.getMapView().setLogoPosition(LogoPosition.logoPostionleftBottom);

        // 深圳为地图中心，logo在右下角
        MapStatusUpdate mapStatusUpdateD = MapStatusUpdateFactory.newLatLng(latLngD);
        SupportMapFragment supportMapFragmentD = (SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.map4));
        supportMapFragmentD.getBaiduMap().setMapStatus(mapStatusUpdateD);
        supportMapFragmentD.getMapView().setLogoPosition(LogoPosition.logoPostionRightBottom);
    }
}
