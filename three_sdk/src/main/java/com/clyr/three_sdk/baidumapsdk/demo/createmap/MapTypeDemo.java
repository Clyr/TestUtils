package com.clyr.three_sdk.baidumapsdk.demo.createmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

/**
 * 基础地图类型
 */
public class MapTypeDemo extends AppCompatActivity {

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_type);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 构建地图状态
        MapStatus.Builder builder = new MapStatus.Builder();
        // 默认 天安门
        LatLng center = new LatLng(39.915071, 116.403907);
        // 默认 11级
        float zoom = 11.0f;

        // 该Intent是OfflineDemo中查看离线地图调起的
        Intent intent = getIntent();
        if (null != intent) {
            center = new LatLng(intent.getDoubleExtra("y", 39.915071),
                    intent.getDoubleExtra("x", 116.403907));
            zoom = intent.getFloatExtra("level", 11.0f);
        }

        builder.target(center).zoom(zoom);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());

        // 设置地图状态
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 设置底图显示模式
     */
    public void setMapMode(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        int id = view.getId();// 普通图
        if (id == R.id.normal) {
            if (checked) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            }
            // 卫星图
        } else if (id == R.id.statellite) {
            if (checked) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
            // 空白地图
        } else if (id == R.id.none) {
            if (checked) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
            }
        }
    }

    /**
     * 清除地图缓存数据，支持清除普通地图和卫星图缓存，再次进入地图页面生效。
     */
    public void cleanMapCache(View view) {
        if (mBaiduMap == null){
            return;
        }
        int mapType = mBaiduMap.getMapType();
        if (mapType == BaiduMap.MAP_TYPE_NORMAL) {
            // // 清除地图缓存数据
            mBaiduMap.cleanCache(BaiduMap.MAP_TYPE_NORMAL);
        } else if (mapType == BaiduMap.MAP_TYPE_SATELLITE) {
            // 清除地图缓存数据
            mBaiduMap.cleanCache(BaiduMap.MAP_TYPE_SATELLITE);
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
