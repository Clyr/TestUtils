package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleDottedStrokeType;
import com.baidu.mapapi.map.CircleHoleOptions;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.HoleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;


/**
 * 在地图上绘制圆
 */

public class CircleDemo extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    // UI相关
    private SeekBar mWidthBar;
    private SeekBar mColorBar;
    private SeekBar mFillAlphaBar;
    private SeekBar mRadiusBar;
    private CheckBox mStrokeDotted;
    // 绘制圆
    private Circle mCircle;
    private Circle mCircleTwo;

    private int mStrokeWidth = 10;
    private int mStrokeColor = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 初始化UI
        mWidthBar = (SeekBar) findViewById(R.id.width_bar);
        mColorBar = (SeekBar) findViewById(R.id.color_bar);
        mFillAlphaBar = (SeekBar) findViewById(R.id.fillalpha_bar);
        mRadiusBar = (SeekBar) findViewById(R.id.radius_bar);
        mStrokeDotted = (CheckBox) findViewById(R.id.dotted_stroke);
        mWidthBar.setOnSeekBarChangeListener(this);
        mColorBar.setOnSeekBarChangeListener(this);
        mFillAlphaBar.setOnSeekBarChangeListener(this);
        mRadiusBar.setOnSeekBarChangeListener(this);
        // 添加圆
        initCircle();
    }

    public void initCircle() {
        // 添加圆
        LatLng llCircleA = new LatLng(39.90923, 116.447428);
        OverlayOptions ooCircleA = new CircleOptions()
                .fillColor(Color.argb(100, 0, 0, 255)) // 设置圆填充颜色
                .center(llCircleA) // 设置圆心坐标
                .stroke(new Stroke(mStrokeWidth, Color.argb(255, 0, mStrokeColor, 0))) // 设置圆边框信息
                .radius(1400); //  圆半径，单位：米
        mCircle = (Circle) mBaiduMap.addOverlay(ooCircleA);

        LatLng llCircleB = new LatLng(39.97923, 116.357428);
        OverlayOptions ooCircleB = new CircleOptions()
                .fillColor(Color.argb(100, 255, 0, 0))// 设置圆填充颜色
                .center(llCircleB)// 设置圆心坐标
                .stroke(new Stroke(mStrokeWidth, Color.argb(255, 0, 0, mStrokeColor)))// 设置圆边框信息
                .radius(2800); //  圆半径，单位：米
        mCircleTwo = (Circle) mBaiduMap.addOverlay(ooCircleB);
    }


    /**
     * 清除所有图层
     *
     * @param view
     */
    public void clearOverlay(View view) {
        // 清除所有图层
        mBaiduMap.clear();
    }

    /**
     * 重置 Circle
     *
     * @param view
     */
    public void resetOverlay(View view) {
        // remove 可以清除某一个overlay
        mCircle.remove();
        mCircleTwo.remove();
        // 还原SeekBar
        mFillAlphaBar.setProgress(100);
        mColorBar.setProgress(180);
        mWidthBar.setProgress(10);
        mRadiusBar.setProgress(1400);

        mStrokeDotted.setChecked(false);
        // 添加 Circle
        initCircle();
    }

    /**
     * 是否绘制虚线边框
     */
    public void isDottedStroke(View view) {
        if (mCircle == null ||  mCircleTwo == null){
            return;
        }
        CheckBox isDotted = (CheckBox) view;
        if (isDotted.isChecked()) {
            // 设置是否绘制虚线圆边框
            mCircle.setDottedStroke(true);
            mCircleTwo.setDottedStroke(true);
            // 设置Circle的虚线Stroke类型
            mCircle.setDottedStrokeType(CircleDottedStrokeType.DOTTED_LINE_CIRCLE);
            mCircleTwo.setDottedStrokeType(CircleDottedStrokeType.DOTTED_LINE_SQUARE);
        } else {
            mCircle.setDottedStroke(false);
            mCircleTwo.setDottedStroke(false);
        }
    }

    /**
     * 添加镂空
     */
    public void addHole(View view){
        if (mCircle == null ||  mCircleTwo == null){
            return;
        }
        LatLng llCircleA = new LatLng(39.90923, 116.447428);
        HoleOptions holeOptionsA = new CircleHoleOptions().center(llCircleA).radius(1000);
        // 设置Circle的镂空形状选项
        mCircle.setHoleOption(holeOptionsA);

        LatLng llCircleB = new LatLng(39.97923, 116.357428);
        HoleOptions holeOptionsB = new CircleHoleOptions().center(llCircleB).radius(1800);
        mCircleTwo.setHoleOption(holeOptionsB);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mCircle == null || mCircleTwo == null) {
            return;
        }

        // 设置边框宽度
        if (seekBar == mWidthBar) {
            mStrokeWidth = progress;
            Stroke stroke = new Stroke(mStrokeWidth, Color.argb(255, 0, mStrokeColor, 0));
            mCircle.setStroke(stroke);
            Stroke strokeTwo = new Stroke(mStrokeWidth, Color.argb(255, 0, 0, mStrokeColor));
            mCircleTwo.setStroke(strokeTwo);
        } else if (seekBar == mColorBar) {
            mStrokeColor = progress;
            // 设置边框颜色
            Stroke stroke = new Stroke(mStrokeWidth, Color.argb(255, 0, mStrokeColor, 0));
            mCircle.setStroke(stroke);
            Stroke stroketTwo = new Stroke(mStrokeWidth, Color.argb(255, 0, 0, mStrokeColor));
            mCircleTwo.setStroke(stroketTwo);
        } else if (seekBar == mFillAlphaBar) {
            // 设置填充颜色
            mCircle.setFillColor(Color.argb(progress, 0, 0, 255));
            mCircleTwo.setFillColor(Color.argb(progress, 255, 0, 0));
        } else if (seekBar == mRadiusBar) {
            // 设置半径
            mCircle.setRadius(progress);
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
        mMapView.getMap().clear();
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }
}
