package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.HoleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonHoleOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 介绍在地图上绘制多边形
 */

public class PolygonDemo extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    // UI相关
    private SeekBar mWidthBar;
    private SeekBar mColorBar;
    private SeekBar mFillAlphaBar;

    private Polygon mPolygon;
    private Polygon mPolygonTwo;
    private int mStrokeWidth = 10;
    private int mColor = 180;
    private int mFillAlpha = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon);

        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 初始化UI
        mWidthBar= (SeekBar) findViewById(R.id.width_bar);
        mColorBar= (SeekBar) findViewById(R.id.color_bar);
        mFillAlphaBar= (SeekBar) findViewById(R.id.fillalpha_bar);
        mWidthBar.setOnSeekBarChangeListener(this);
        mColorBar.setOnSeekBarChangeListener(this);
        mFillAlphaBar.setOnSeekBarChangeListener(this);

        initPolygon();
    }

    /**
     *清除所有图层
     */
    public void clearOverlay(View view) {
        // 清除所有图层
        mBaiduMap.clear();
    }

    /**
     * 重置 Polygon
     */
    public void resetOverlay(View view) {
        // remove可以移除某一个overlay
        mPolygon.remove();
        mPolygonTwo.remove();
        // 还原SeekBar
        mWidthBar.setProgress(10);
        mColorBar.setProgress(180);
        mFillAlphaBar.setProgress(100);
        // 添加 Polygon
        initPolygon();
    }

    /**
     * 添加镂空
     */
    public void addHole(View view){
        if (mPolygon == null || mPolygonTwo == null){
            return;
        }
        LatLng latLngA = new LatLng(39.924342,116.337722);
        LatLng latLngB = new LatLng(39.922572,116.372505);
        LatLng latLngC = new LatLng(39.908972,116.35073);
        List<LatLng> latLngsA = new ArrayList<LatLng>();
        latLngsA.add(latLngA);
        latLngsA.add(latLngB);
        latLngsA.add(latLngC);
        HoleOptions holeOptionsA = new PolygonHoleOptions().addPoints(latLngsA);
        // 设置Polygon的镂空形状选项
        mPolygon.setHoleOption(holeOptionsA);

        LatLng latLngD = new LatLng( 39.94135, 116.390138 );
        LatLng latLngE = new LatLng( 39.92746, 116.390138);
        LatLng latLngF = new LatLng( 39.92746, 116.404177);
        LatLng latLngG = new LatLng( 39.94135, 116.404177);
        List<LatLng> latLngsB = new ArrayList<LatLng>();
        latLngsB.add(latLngD);
        latLngsB.add(latLngE);
        latLngsB.add(latLngF);
        latLngsB.add(latLngG);
        HoleOptions holeOptionsB = new PolygonHoleOptions().addPoints(latLngsB);
        // 设置Polygon的镂空形状选项
        mPolygonTwo.setHoleOption(holeOptionsB);
    }

    public void initPolygon(){
        // 添加多边形
        LatLng latLngA = new LatLng(39.93923, 116.357428);
        LatLng latLngB = new LatLng(39.91923, 116.327428);
        LatLng latLngC = new LatLng(39.89923, 116.347428);
        LatLng latLngD = new LatLng(39.89923, 116.367428);
        LatLng latLngE = new LatLng(39.91923, 116.387428);
        List<LatLng> latLngList = new ArrayList<LatLng>();
        latLngList.add(latLngA);
        latLngList.add(latLngB);
        latLngList.add(latLngC);
        latLngList.add(latLngD);
        latLngList.add(latLngE);

        OverlayOptions ooPolygon = new PolygonOptions()
                .points(latLngList)// 设置多边形坐标点列表
                .stroke(new Stroke(mStrokeWidth,  Color.argb(255, 0, mColor, 0)))// 设置多边形边框信息
                .fillColor(Color.argb(mFillAlpha, 255, 255, 0));// 设置多边形填充颜色
        // 添加覆盖物
        mPolygon = (Polygon) mBaiduMap.addOverlay(ooPolygon);

        // 绘制矩形
        LatLng latLngF = new LatLng( 39.92235, 116.380338 );
        LatLng latLngG = new LatLng( 39.947246, 116.380338);
        LatLng latLngH = new LatLng( 39.947246, 116.414977);
        LatLng latLngL = new LatLng( 39.92235, 116.414977);

        List<LatLng> LatLngs = new ArrayList<LatLng>();
        LatLngs.add(latLngF);
        LatLngs.add(latLngG);
        LatLngs.add(latLngH);
        LatLngs.add(latLngL);

        OverlayOptions overlayOptions = new PolygonOptions()
                .points(LatLngs)// 设置多边形坐标点列表
                .stroke(new Stroke(mStrokeWidth, Color.argb(255, mColor, 0, 0)))// 设置多边形边框信息
                .fillColor(Color.argb(mFillAlpha, 0, 0, 255));// 设置多边形填充颜色
        // 添加覆盖物
        mPolygonTwo = (Polygon) mBaiduMap.addOverlay(overlayOptions);

        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(13.0f);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mPolygon == null || mPolygonTwo == null) {
            return;
        }
        if (seekBar == mWidthBar) {
            mStrokeWidth = progress;
            // 设置边框宽度
            Stroke stroke = new Stroke(mStrokeWidth, Color.argb(255, 0, mColor, 0));
            mPolygon.setStroke(stroke);
            Stroke strokeTwo = new Stroke(mStrokeWidth, Color.argb(255, mColor, 0, 0));
            mPolygonTwo.setStroke(strokeTwo);
        }else if (seekBar == mColorBar) {
            Log.e("onProgressChanged", "onProgressChanged: "+progress);
            mColor = progress;
            // 设置边框颜色
            Stroke stroke = new Stroke(mStrokeWidth, Color.argb(255, 0, mColor, 0));
            mPolygon.setStroke(stroke);
            Stroke stroketTwo = new Stroke( mStrokeWidth, Color.argb(255, mColor, 0, 0));
            mPolygonTwo.setStroke(stroketTwo);
        }else if (seekBar == mFillAlphaBar) {
            mFillAlpha = progress;
            // 设置填充颜色
            mPolygon.setFillColor(Color.argb(mFillAlpha, 255, 255, 0));
            mPolygonTwo.setFillColor(Color.argb(mFillAlpha, 0, 0, 255));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
    }
}
