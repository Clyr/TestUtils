package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineDottedLineType;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 演示在地图上绘制线
 */

public class PolylineDemo extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    // 多颜色渐变折线，点击时消失
    private Polyline mGradientPolyline;
    // 大地曲线
    private Polyline mGeoPolyline;
    // 普通折线，点击时改变宽度
    private Polyline mPolyline;
    // 多颜色折线，点击时消失
    private Polyline mColorfulPolyline;
    // 纹理折线，点击时获取折线上点数及width
    private Polyline mTexturePolyline;

    private BitmapDescriptor mRedTexture = BitmapDescriptorFactory.fromAsset("Icon_road_red_arrow.png");
    private BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("Icon_road_blue_arrow.png");
    private BitmapDescriptor mGreenTexture = BitmapDescriptorFactory.fromAsset("Icon_road_green_arrow.png");

    private CheckBox mDottedLineCB;
    private CheckBox mClickPolyline;
    private SeekBar mWidthBar;
    private SeekBar mCorlorBar;
    private int mWidth = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polyline);

        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        mDottedLineCB = (CheckBox) findViewById(R.id.dottedline);
        mClickPolyline = (CheckBox) findViewById(R.id.clickline);
        mWidthBar = (SeekBar) findViewById(R.id.width_bar);
        mCorlorBar = (SeekBar) findViewById(R.id.color_bar);
        mCorlorBar = (SeekBar) findViewById(R.id.color_bar);

        mWidthBar.setOnSeekBarChangeListener(this);
        mCorlorBar.setOnSeekBarChangeListener(this);
        mDottedLineCB.setOnCheckedChangeListener(new DottedLineListener());

        // 界面加载时添加绘制图层
        initPolyline();

        // 点击polyline的事件响应
        mBaiduMap.setOnPolylineClickListener(new BaiduMap.OnPolylineClickListener() {
            @Override
            public boolean onPolylineClick(Polyline polyline) {
                if (polyline == mPolyline) {
                    polyline.setDottedLine(true);
                } else if (polyline == mColorfulPolyline) {
                    polyline.remove();
                } else if (polyline == mTexturePolyline) {
                    Toast.makeText(getApplicationContext(), "点数：" + polyline.getPoints().size() + ",width:"
                                    + polyline.getWidth(), Toast.LENGTH_SHORT).show();
                } else if (polyline == mGradientPolyline){
                    polyline.remove();
                }
                return false;
            }
        });
    }

    /**
     * 清除所有图层
     */
    public void clearOverlay(View view) {
        // 清除所有图层
        mBaiduMap.clear();
    }

    /**
     * 重置 Polyline
     */
    public void resetOverlay(View view) {
        // remove 可以移除某一个overlay
        mGeoPolyline.remove();
        mPolyline.remove();
        mColorfulPolyline.remove();
        mGradientPolyline.remove();
        mTexturePolyline.remove();
        // 还原SeekBar
        mWidthBar.setProgress(18);
        mCorlorBar.setProgress(255);

        mDottedLineCB.setChecked(false);
        mClickPolyline.setChecked(true);
        // 添加绘制元素
        initPolyline();
    }

    /**
     * 设置Polyline是否可点击
     */
    public void setPolylineClick(View view) {
        if (mPolyline == null || mColorfulPolyline == null || mTexturePolyline == null
            || mGradientPolyline == null || mGeoPolyline == null) {
            return;
        }
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()){
            mPolyline.setClickable(true);
            mColorfulPolyline.setClickable(true);
            mTexturePolyline.setClickable(true);
            mGeoPolyline.setClickable(true);
            mGradientPolyline.setClickable(true);
        } else {
            mPolyline.setClickable(false);
            mColorfulPolyline.setClickable(false);
            mTexturePolyline.setClickable(false);
            mGeoPolyline.setClickable(false);
            mGradientPolyline.setClickable(false);
        }
    }

    private void initPolyline() {
        // 添加大地曲线绘制
        LatLng latLngAAAA = new LatLng(36.53, -121.47);
        LatLng latLngBBBB = new LatLng(22.33, 114);
        List<LatLng> pointss = new ArrayList<LatLng>();
        pointss.add(latLngAAAA);
        pointss.add(latLngBBBB);
        // 覆盖物参数配置
        OverlayOptions ooGeoPolyline = new PolylineOptions()
                .isGeodesic(true)
                .width(mWidth)
                // 折线经度跨180需增加此字段
                .lineDirectionCross180(PolylineOptions.LineDirectionCross180.FROM_WEST_TO_EAST)
                .points(pointss);// 折线坐标点列表 数目[2,10000]，且不能包含 null
        // 添加覆盖物
        mGeoPolyline = (Polyline) mBaiduMap.addOverlay(ooGeoPolyline);

        // 添加多颜色分段的渐变色折线绘制
        LatLng latLngUU = new LatLng(40.065, 116.444);
        LatLng latLngVV = new LatLng(40.025, 116.494);
        LatLng latLngWW = new LatLng(40.055, 116.534);
        LatLng latLngXX = new LatLng(40.005, 116.594);
        LatLng latLngYY = new LatLng(40.065, 116.644);
        List<LatLng> pointsGradientList = new ArrayList<LatLng>();
        pointsGradientList.add(latLngUU);
        pointsGradientList.add(latLngVV);
        pointsGradientList.add(latLngWW);
        pointsGradientList.add(latLngXX);
        pointsGradientList.add(latLngYY);
        // 折线每个点的颜色值
        List<Integer> colorValue = new ArrayList<Integer>();
        colorValue.add(0xAAFF0000);
        colorValue.add(0xAA00FF00);
        colorValue.add(0xAA0000FF);
        colorValue.add(0xAA00FF00);
        colorValue.add(0xAAFF0000);
        // 覆盖物参数配置
        OverlayOptions ooPolylineG = new PolylineOptions()
                .width(mWidth)// 设置折线线宽， 默认为 5， 单位：像素
                .isGradient(true) // 渐变色折线
                .points(pointsGradientList)// 设置折线坐标点列表
                .colorsValues(colorValue);// 设置折线每个点的颜色值，每一个点带一个颜色值，绘制时按照索引依次取值
        // 添加覆盖物
        mGradientPolyline = (Polyline) mBaiduMap.addOverlay(ooPolylineG);

        // 添加普通折线绘制
        LatLng latLngA = new LatLng(39.97923, 116.357428);
        LatLng latLngB = new LatLng(39.94923, 116.397428);
        LatLng latLngC = new LatLng(39.97923, 116.437428);
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(latLngA);
        points.add(latLngB);
        points.add(latLngC);
        // 覆盖物参数配置
        OverlayOptions ooPolyline = new PolylineOptions()
                .width(mWidth)// 设置折线线宽， 默认为 5， 单位：像素
                .color(Color.argb(255, 255, 0, 0))//  折线颜色。注意颜色值得格式为：0xAARRGGBB，透明度值在前
                .points(points);// 折线坐标点列表 数目[2,10000]，且不能包含 null
        // 添加覆盖物
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);

        // 添加多颜色分段的折线绘制
        LatLng latLngAA = new LatLng(39.965, 116.444);
        LatLng latLngBB = new LatLng(39.925, 116.494);
        LatLng latLngCC = new LatLng(39.955, 116.534);
        LatLng latLngDD = new LatLng(39.905, 116.594);
        LatLng latLngEE = new LatLng(39.965, 116.644);
        List<LatLng> pointsList = new ArrayList<LatLng>();
        pointsList.add(latLngAA);
        pointsList.add(latLngBB);
        pointsList.add(latLngCC);
        pointsList.add(latLngDD);
        pointsList.add(latLngEE);
        // 覆盖物参数配置
        OverlayOptions ooPolylineA = new PolylineOptions()
                .width(mWidth)// 设置折线线宽， 默认为 5， 单位：像素
                .points(pointsList)// 设置折线坐标点列表
                .colorsValues(colorValue);// 设置折线每个点的颜色值，每一个点带一个颜色值，绘制时按照索引依次取值
        // 添加覆盖物
        mColorfulPolyline = (Polyline) mBaiduMap.addOverlay(ooPolylineA);

        // 添加多纹理分段的折线绘制
        LatLng latLngAAA = new LatLng(39.865, 116.444);
        LatLng latLngBBB = new LatLng(39.825, 116.494);
        LatLng latLngCCC = new LatLng(39.855, 116.534);
        LatLng latLngDDD = new LatLng(39.805, 116.594);
        List<LatLng> pointsListA = new ArrayList<LatLng>();
        pointsListA.add(latLngAAA);
        pointsListA.add(latLngBBB);
        pointsListA.add(latLngCCC);
        pointsListA.add(latLngDDD);
        // 折线多纹理分段绘制的纹理队列
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(mRedTexture);
        textureList.add(mBlueTexture);
        textureList.add(mGreenTexture);
        // 折线每个点的纹理索引
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(0);
        textureIndexs.add(1);
        textureIndexs.add(2);
        // 覆盖物参数配置
        OverlayOptions ooPolylineAA = new PolylineOptions().width(mWidth)
                .points(pointsListA)
                .dottedLine(true) // 设置折线是否虚线
                .customTextureList(textureList)// 设置折线多纹理分段绘制的纹理队列
                .textureIndex(textureIndexs);// 设置折线每个点的纹理索引
         // 添加覆盖物
        mTexturePolyline = (Polyline) mBaiduMap.addOverlay(ooPolylineAA);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mPolyline == null || mColorfulPolyline == null || mTexturePolyline == null
            || mGradientPolyline == null || mGeoPolyline == null) {
            return;
        }
        if (seekBar == mWidthBar) {
            mWidth = progress;
            // 设置 Polyline 宽度
            mColorfulPolyline.setWidth(mWidth);
            mPolyline.setWidth(mWidth);
            mTexturePolyline.setWidth(mWidth);
            mGeoPolyline.setWidth(mWidth);
            mGradientPolyline.setWidth(mWidth);
        } else if (seekBar == mCorlorBar) {
            // 设置 Polyline 颜色
            mPolyline.setColor(Color.argb(255, progress, 0, 0));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private class DottedLineListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mPolyline == null || mColorfulPolyline == null || mTexturePolyline == null
                || mGradientPolyline == null || mGeoPolyline == null) {
                return;
            }
            //  是否虚线绘制
            if (isChecked) {
                mPolyline.setDottedLine(true);
                // 设置Polyline的虚线类型
                mPolyline.setDottedLineType(PolylineDottedLineType.DOTTED_LINE_CIRCLE);
                mColorfulPolyline.setDottedLine(true);
                mTexturePolyline.setDottedLine(true);
                mGeoPolyline.setDottedLine(true);
                mGradientPolyline.setDottedLine(true);
            } else {
                mPolyline.setDottedLine(false);
                mColorfulPolyline.setDottedLine(false);
                mTexturePolyline.setDottedLine(false);
                mGeoPolyline.setDottedLine(false);
                mGradientPolyline.setDottedLine(false);
            }
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
        if (mRedTexture != null) {
            mRedTexture.recycle();
        }
        if (mBlueTexture != null) {
            mBlueTexture.recycle();
        }
        if (mGreenTexture != null) {
            mGreenTexture.recycle();
        }
        // 清除所有图层
        mBaiduMap.clear();
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }
}
