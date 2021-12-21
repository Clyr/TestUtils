package com.clyr.three_sdk.baidumapsdk.demo.util;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.AreaUtil;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 演示点与圆，多边形位置关系
 */

public class SpatialRelationDemo extends AppCompatActivity implements BaiduMap.OnMapClickListener{

    private MapView mapView;
    private BaiduMap mBaiduMap;
    private Marker marker;
    private TextView mTextView;
    private List<LatLng> mLatLngList;
    private LatLng mCenter;
    private int radius = 5000; // 圆半径
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spatial_relation);
        // 初始化地图
        mapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setOnMapClickListener(this);
        mTextView = (TextView)findViewById(R.id.text);
        mTextView.setText("单击地图");
        initOverlay();
    }


    public void initOverlay(){
        // 添加多边形
        LatLng latLngA = new LatLng(39.93923, 116.357428);
        LatLng latLngB = new LatLng(39.91923, 116.327428);
        LatLng latLngC = new LatLng(39.89923, 116.347428);
        LatLng latLngD = new LatLng(39.89923, 116.367428);
        LatLng latLngE = new LatLng(39.91923, 116.387428);
        mLatLngList = new ArrayList<>();
        mLatLngList.add(latLngA);
        mLatLngList.add(latLngB);
        mLatLngList.add(latLngC);
        mLatLngList.add(latLngD);
        mLatLngList.add(latLngE);
        OverlayOptions ooPolygon = new PolygonOptions().points(mLatLngList).stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00);
        mBaiduMap.addOverlay(ooPolygon);

        // 添加圆
        mCenter = new LatLng(39.97923, 116.357428);
        OverlayOptions ooCircle2 = new CircleOptions().fillColor(0xAAFF0000).center(mCenter)
                .stroke(new Stroke(10, 0xAA0000FF)).radius(radius);
        mBaiduMap.addOverlay(ooCircle2);
    }

    /**
     *
     * 计算地图上任意的多边形面积。
     */
    public void calculateArea(View view){
        if (mLatLngList.size() == 0){
            return;
        }
        // 计算多边形面积，返回单位：平方米
        double polygonArea = AreaUtil.calculateArea(mLatLngList);
        // 转换成平方千米
        double area = polygonArea/1000000;
        Toast.makeText(SpatialRelationDemo.this,"多边形面积为："+ area+"平方千米", Toast.LENGTH_SHORT).show();
    }

    /**
     * 单击地图
     */
    public void onMapClick(LatLng point) {
        if (marker != null){
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions().position(point).icon(bitmapA).zIndex(9);
        marker = (Marker)mBaiduMap.addOverlay(markerOptions);
        // 判断点是否在多边形内
        boolean isPolygonContains = SpatialRelationUtil.isPolygonContainsPoint(mLatLngList, point);
        // 判断点是否在圆内
        boolean isCircleContains = SpatialRelationUtil.isCircleContainsPoint(mCenter, radius, point);
        Toast.makeText(SpatialRelationDemo.this,"是否在多边形内："+ isPolygonContains + " 是否在圆内: " + isCircleContains, Toast.LENGTH_SHORT).show();
    }

    /**
     * 单击地图中的POI点
     */
    public void onMapPoiClick(MapPoi poi) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmapA.recycle();
        mapView.onDestroy();
    }
}
