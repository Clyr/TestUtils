package com.clyr.three_sdk.baidumapsdk.demo.layers;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;


/**
 * 演示3D 楼宇是否显示
 */
public class LayerBuildingDemo extends AppCompatActivity {

    // MapView 地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layer_building);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(31.241246,121.51733);
        float zoom = 19.0f;
        float overlook = -45.0f;
        builder.target(center).zoom(zoom).overlook(overlook);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 设置3D 楼宇是否显示
     */
    public  void setBuildingEnable(View v){
        // 设置setBuildingsEnabled之后必须更新下地图
        mBaiduMap.setBuildingsEnabled(((CheckBox) v).isChecked());
        MapStatus mapStatus = mBaiduMap.getMapStatus();
        if (null != mapStatus){
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            // 设置地图状态
            mBaiduMap.setMapStatus(mapStatusUpdate);
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
