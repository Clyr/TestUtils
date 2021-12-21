package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.Arc;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;


/**
 * 在地图上绘制弧线
 */

public class ArcDemo extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    // UI相关
    private SeekBar mWidthBar;
    private SeekBar mColorBar;
    private Arc mArcOne;
    private Arc mArcTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // UI相关
        mWidthBar = (SeekBar) findViewById(R.id.width_bar);
        mColorBar = (SeekBar) findViewById(R.id.color_bar);

        mWidthBar.setOnSeekBarChangeListener(this);
        mColorBar.setOnSeekBarChangeListener(this);
        initArc();
    }


    /**
     * 清除所有图层
     */
    public void clearOverlay(View view) {
        // 清除所有图层
        mBaiduMap.clear();
    }

    /**
     * 重置 Arc
     */
    public void resetOverlay(View view) {
        // remove 可以删除某一个覆盖物
        mArcOne.remove();
        mArcTwo.remove();
        // 还原SeekBar
        mWidthBar.setProgress(5);
        mColorBar.setProgress(255);
        // 添加 Arc
        initArc();
    }

    public void initArc() {
        // 弧线起点
        LatLng arcStart = new LatLng(39.97923, 116.357428);
        // 弧线中点
        LatLng arcCentre = new LatLng(39.94923, 116.397428);
        // 弧线终点
        LatLng arcEnd = new LatLng(39.97923, 116.437428);

        // 添加弧线
        OverlayOptions overlayOptionsA = new ArcOptions()
                .color(Color.argb(255, 0, 0, 255)) // 设置弧线的颜色
                .width(5) // 设置弧线的线宽
                .points(arcStart, arcCentre, arcEnd); // 设置弧线的起点、中点、终点坐标
        mArcOne = (Arc) mBaiduMap.addOverlay(overlayOptionsA);

        LatLng arcStartA = new LatLng(39.900988, 116.330105);
        LatLng arcEndB = new LatLng(39.873084, 116.465785);

        // 添加弧线
        OverlayOptions overlayOptionsB = new ArcOptions()
                .color(Color.argb(255, 0, 255, 0))// 设置弧线的颜色
                .width(5)// 设置弧线的线宽
                .points(arcStartA, arcCentre, arcEndB);// 设置弧线的起点、中点、终点坐标
        mArcTwo = (Arc) mBaiduMap.addOverlay(overlayOptionsB);

        // 设置地图中心点以及缩放级别
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(arcCentre, 13.0f);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mArcOne == null || mArcTwo == null) {
            return;
        }
        if (seekBar == mWidthBar) {
            // 设置宽度
            mArcOne.setWidth(progress);
            mArcTwo.setWidth(progress);
        } else if (seekBar == mColorBar) {
            // 设置颜色
            mArcOne.setColor(Color.argb(255, 0, 0, progress));
            mArcTwo.setColor(Color.argb(255, 0, progress, 0));
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
