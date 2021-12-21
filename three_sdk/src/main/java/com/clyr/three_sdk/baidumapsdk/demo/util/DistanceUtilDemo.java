package com.clyr.three_sdk.baidumapsdk.demo.util;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.clyr.three_sdk.R;


/**
 * 演示两点之间距离计算
 */

public class DistanceUtilDemo extends AppCompatActivity implements BaiduMap.OnMarkerDragListener {

    private MapView mapView;
    private BaiduMap mBaiduMap;
    private Marker mMarkerA;
    private TextView mTextView;
    private Marker mMarkerB;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mBitmapA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    private BitmapDescriptor mBitmapB = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        mapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setOnMarkerDragListener(this);
        mTextView = (TextView)findViewById(R.id.text);
        initOverlay();
    }

    public void initOverlay(){
        LatLng llA = new LatLng(39.963175, 116.400244);
        LatLng llB = new LatLng(39.906965, 116.401394);

        MarkerOptions ooA = new MarkerOptions().position(llA).icon(mBitmapA).zIndex(9).draggable(true);
        mMarkerA = (Marker)mBaiduMap.addOverlay(ooA);

        MarkerOptions ooB = new MarkerOptions().position(llB).icon(mBitmapB).zIndex(5).draggable(true);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        double dis = DistanceUtil. getDistance(mMarkerA.getPosition(), mMarkerB.getPosition());
        mTextView.setText("两点间距离："+ String.valueOf(dis));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mBitmapA.recycle();
        mBitmapB.recycle();
    }
}
