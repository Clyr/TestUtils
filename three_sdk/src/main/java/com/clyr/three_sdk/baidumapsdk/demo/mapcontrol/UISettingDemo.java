package com.clyr.three_sdk.baidumapsdk.demo.mapcontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;
import com.clyr.three_sdk.R;


/**
 * 演示地图UI控制功能
 */
public class UISettingDemo extends AppCompatActivity {

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;
    private CheckBox mAllGesturesCB;
    private CheckBox mZoomCB;
    private CheckBox mOverlookCB;
    private CheckBox mRotateCB;
    private CheckBox mScrollCB;
    private CheckBox mDoublezoomCB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uisetting);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        mAllGesturesCB = (CheckBox) findViewById(R.id.allGesture);
        mZoomCB = (CheckBox) findViewById(R.id.zoom);
        mScrollCB = (CheckBox) findViewById(R.id.scroll);
        mOverlookCB = (CheckBox) findViewById(R.id.overlook);
        mRotateCB = (CheckBox) findViewById(R.id.rotate);
        mDoublezoomCB = (CheckBox) findViewById(R.id.doublezoom);
        updateGesture();
        mUiSettings.setCompassEnabled(true);
    }

    /**
     * 是否启用缩放手势
     */
    public void setZoomEnable(View v) {
        updateGesture();
    }

    /**
     * 是否启用平移手势
     */
    public void setScrollEnable(View v) {
        updateGesture();
    }

    /**
     * 是否启用旋转手势
     */
    public void setRotateEnable(View v) {
        updateGesture();
    }

    /**
     * 是否启用俯视手势
     */
    public void setOverlookEnable(View v) {
        updateGesture();
    }

    /**
     * 是否启用指南针图层
     */
    public void setCompassEnable(View v) {
        mUiSettings.setCompassEnabled(((CheckBox) v).isChecked());
    }

    /**
     * 禁用所有手势
     */
    public void setAllGestureEnable(View v) {
        updateGesture();
    }

    /**
     * 设置双击地图按照当前地图中心点放大
     */
    public void setCenterWithDoubleClickEnable(View v){
        updateGesture();
    }

    /**
     * 更新手势状态
     */
    public void updateGesture() {
        if (mAllGesturesCB.isChecked()) {
            mUiSettings.setAllGesturesEnabled(!mAllGesturesCB.isChecked());
        } else {
            mUiSettings.setZoomGesturesEnabled(mZoomCB.isChecked());
            mUiSettings.setScrollGesturesEnabled(mScrollCB.isChecked());
            mUiSettings.setRotateGesturesEnabled(mRotateCB.isChecked());
            mUiSettings.setOverlookingGesturesEnabled(mOverlookCB.isChecked());
            mUiSettings.setEnlargeCenterWithDoubleClickEnable(mDoublezoomCB.isChecked());
        }
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
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }
}
