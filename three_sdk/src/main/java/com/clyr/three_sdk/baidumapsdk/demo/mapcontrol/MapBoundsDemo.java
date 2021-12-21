package com.clyr.three_sdk.baidumapsdk.demo.mapcontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.clyr.three_sdk.R;


/**
 * 演示更新地图状态，设置地图显示范围，设置地图中心点以及缩放级别，设置显示在规定宽高中的地图地理范围，
 *
 * 设置显示在指定相对于MapView的padding中的地图地理范围，放大地图，缩放地图的介绍。
 */

public class MapBoundsDemo extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatLng mSouthwest;
    private LatLng mNortheast;
    private boolean isShowBoundsPadding = true;
    private Button mBoundsPaddingBtn;
    private Marker mMarker;
    private BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapbounds);
        mSouthwest = new LatLng(39.92235, 116.380338);
        mNortheast = new LatLng(39.947246, 116.414977);
        mBoundsPaddingBtn = findViewById(R.id.latLng_bounds_padding);
        mMapView = (MapView) findViewById(R.id.bmap);
        mBaiduMap = mMapView.getMap();

        // 设置地图缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16.5f));
        LatLng center = new LatLng(39.914603,116.404269);
        MarkerOptions markerOptions = new MarkerOptions().position(center).icon(mBitmap);
        mMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
    }

    /**
     * 设置地图显示范围
     *
     * @param v
     */
    public void setMapBounds(View v) {
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(mNortheast)// 东北角坐标
                .include(mSouthwest)// 西南角坐标
                .build();
        // 设置显示在屏幕中的地图地理范围
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(bounds));
    }

    /**
     * 设置地图新状态
     *
     * @param v
     */
    public void setNewMapStatus(View v) {
        LatLng latLng = new LatLng(31.245059,121.509144);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.5f).overlook(-21f).rotate(0);
        // 更新地图状态
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mMarker.setPosition(latLng);
    }

    /**
     * 设置地图中心点以及缩放级别
     *
     * @param v
     */
    public void setNewLatLngZoom(View v) {
        LatLng latLng = new LatLng(22.537611,114.001108);
        // 地图中心点以及缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(latLng,18f));
        mMarker.setPosition(latLng);
    }

    /**
     * 设置显示在规定宽高中的地图地理范围
     *
     * @param v
     */
    public void setNewLatLngBounds(View v) {
        LatLngBounds bounds = new LatLngBounds.Builder().include(mNortheast).include(mSouthwest).build();
        MapStatus mapStatus = mBaiduMap.getMapStatus();
        if (null != mapStatus){
            int width = mapStatus.winRound.right - mapStatus.winRound.left - 400;
            int height = mapStatus.winRound.bottom - mapStatus.winRound.top - 400;
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(bounds,width,height));
        }
    }

    /**
     * 设置显示在指定相对于MapView的padding中的地图地理范围
     *
     * @param v
     */
    public void setLatLngBoundsPadding(View v) {
        LatLng latLng = new LatLng(39.9137187, 116.404556);
        mMarker.setPosition(latLng);
        LatLngBounds bounds = new LatLngBounds.Builder().include(mNortheast).include(mSouthwest).build();
        if (isShowBoundsPadding){
            mBoundsPaddingBtn.setText("没有padding值的地图地理范围");
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(bounds,0,0,0,600));
        }else{
            mBoundsPaddingBtn.setText("展示有padding值的地图地理范围");
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(bounds,0,0,0,0));
        }
        isShowBoundsPadding = !isShowBoundsPadding;
    }

    /**
     * 放大地图缩放级别
     *
     * @param v
     */
    public void setZoomIn(View v) {
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
    }

    /**
     * 缩小地图缩放级别
     *
     * @param v
     */
    public void setZoomOut(View v) {
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
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
        mBitmap.recycle();
        // 清除所有图层
        mBaiduMap.clear();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }
}
