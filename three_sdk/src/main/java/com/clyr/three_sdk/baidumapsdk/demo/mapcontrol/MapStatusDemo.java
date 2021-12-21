package com.clyr.three_sdk.baidumapsdk.demo.mapcontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.clyr.three_sdk.R;


/**
 * 缩放、旋转、俯视等事件导致地图状态发生改变的监听
 */
public class MapStatusDemo extends AppCompatActivity {

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    // 用于显示地图状态的面板
    private TextView mStateView;
    private EditText mZoomLevelEdit;
    private EditText mRotateAngleEdit;
    private EditText mOverLookAngleEdit;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapstatus);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mStateView = (TextView) findViewById(R.id.state);
        mZoomLevelEdit = (EditText) findViewById(R.id.zoomlevel);
        mRotateAngleEdit = (EditText) findViewById(R.id.rotateangle);
        mOverLookAngleEdit = (EditText) findViewById(R.id.overlookangle);
        initListener();
    }


    private void initListener(){
        // 地图状态发生变化
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
                updateMapState();
            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                updateMapState();
            }

            @Override
            public void onMapStatusChange(MapStatus status) {
                // v7.1.0版本之后，onMapStatusChange回调在异步线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateMapState();
                    }
                });
            }
        });
    }

    /**
     * 更新地图状态显示面板
     */
    private void updateMapState() {
        if (mStateView == null) {
            return;
        }
        String state = "";
        MapStatus mapStatus = mBaiduMap.getMapStatus();
        if (null != mapStatus){
            state += String.format("zoom=%.1f rotate=%d overlook=%d", mapStatus.zoom, (int) mapStatus.rotate, (int) mapStatus.overlook);
            mStateView.setText(state);
        }
    }

    /**
     * 处理缩放 sdk 缩放级别范围： [4.0,21.0]
     */
    public void perfomZoom(View v) {
        try {
            float zoomLevel = Float.parseFloat(mZoomLevelEdit.getText().toString());
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(zoomLevel);
            mBaiduMap.animateMapStatus(mapStatusUpdate);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的缩放级别", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理旋转 旋转角范围： 0 ~ 360 , 单位：度
     */
    public void perfomRotate(View v ) {
        try {
            int rotateAngle = Integer.parseInt(mRotateAngleEdit.getText().toString());
            MapStatus mapStatus = mBaiduMap.getMapStatus();
            if (null != mapStatus){
                MapStatus build = new MapStatus.Builder(mapStatus).rotate(rotateAngle).build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(build);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的旋转角度", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理俯视 设置俯角范围： -45 ~ 0 , 单位： 度
     */
    public void perfomOverlook(View v) {
        try {
            int overlookAngle = Integer.parseInt(mOverLookAngleEdit.getText().toString());
            MapStatus mapStatus = mBaiduMap.getMapStatus();
            if (null != mapStatus){
                MapStatus build = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(overlookAngle).build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(build);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确的俯角", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新地图状态
     */
    public void perfomAll(View v) {
        try {
            float zoomLevel = Float.parseFloat(mZoomLevelEdit.getText().toString());
            int rotateAngle = Integer.parseInt(mRotateAngleEdit.getText().toString());
            int overlookAngle = Integer.parseInt(mOverLookAngleEdit.getText().toString());
            MapStatus mapStatus = mBaiduMap.getMapStatus();
            if (null != mapStatus){
                MapStatus build = new MapStatus.Builder(mBaiduMap.getMapStatus()).rotate(rotateAngle).zoom(zoomLevel)
                        .overlook(overlookAngle).build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(build);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确参数，旋转角和俯角需为整数", Toast.LENGTH_SHORT).show();
        }
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
