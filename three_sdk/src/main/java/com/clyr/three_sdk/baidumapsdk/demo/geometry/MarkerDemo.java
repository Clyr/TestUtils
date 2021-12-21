package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import java.util.ArrayList;



/**
 * 介绍在地图上绘制Marker,添加动画，响应事件
 */

public class MarkerDemo extends AppCompatActivity {

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private InfoWindow mInfoWindow;
    private SeekBar mAlphaSeekBar = null;
    private CheckBox mAnimationBox = null;
    private CheckBox mClickMarker = null;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    private BitmapDescriptor bitmapB = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
    private BitmapDescriptor bitmapC = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
    private BitmapDescriptor bitmapD = BitmapDescriptorFactory.fromResource(R.drawable.icon_markd);
    private BitmapDescriptor bitmapE = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        mAlphaSeekBar = (SeekBar) findViewById(R.id.alphaBar);
        mAlphaSeekBar.setOnSeekBarChangeListener(new SeekBarListener());
        mAnimationBox = (CheckBox) findViewById(R.id.animation);
        mClickMarker = (CheckBox) findViewById(R.id.click_marker);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(13.0f);
        mBaiduMap.setMapStatus(mapStatusUpdate);

        initMarker();
        initListener();
    }

    public void initMarker() {
        // add marker overlay
        LatLng latLngA = new LatLng(39.963175, 116.400244);
        LatLng latLngB = new LatLng(39.942821, 116.369199);
        LatLng latLngC = new LatLng(39.939723, 116.425541);
        LatLng latLngD = new LatLng(39.906965, 116.401394);

        MarkerOptions markerOptionsA = new MarkerOptions()
                .position(latLngA)
                .icon(bitmapA)// 设置 Marker 覆盖物的图标
                .zIndex(9)// 设置 marker 覆盖物的 zIndex
                .clickable(mClickMarker.isChecked()) // 设置Marker是否可点击
                .draggable(true); // 设置 marker 是否允许拖拽，默认不可拖拽
        if (mAnimationBox.isChecked()) {
            // 掉下动画
            markerOptionsA.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerA = (Marker) (mBaiduMap.addOverlay(markerOptionsA));

        MarkerOptions markerOptionsB = new MarkerOptions().position(latLngB).icon(bitmapB).zIndex(5)
                .clickable(mClickMarker.isChecked());
        if (mAnimationBox.isChecked()) {
            // 掉下动画
            markerOptionsB.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerB = (Marker) (mBaiduMap.addOverlay(markerOptionsB));

        MarkerOptions markerOptionsC = new MarkerOptions()
                .position(latLngC)// 经纬度
                .icon(bitmapC) // 设置 Marker 覆盖物的图标
                .perspective(false) // 设置是否开启 marker 覆盖物近大远小效果，默认开启
                .anchor(0.5f, 0.5f) // 设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
                .rotate(30) // 设置 marker 覆盖物旋转角度，逆时针
                .clickable(mClickMarker.isChecked()) // 设置Marker是否可点击
                .zIndex(7); // 设置 marker 覆盖物的 zIndex
        if (mAnimationBox.isChecked()) {
            // 生长动画
            markerOptionsC.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        mMarkerC = (Marker) (mBaiduMap.addOverlay(markerOptionsC));

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bitmapA);
        giflist.add(bitmapB);
        giflist.add(bitmapC);
        MarkerOptions markerOptionsD = new MarkerOptions()
                .position(latLngD)
                .icons(giflist)// 设置 Marker 覆盖物的图标，相同图案的 icon 的 marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
                .zIndex(0)
                .clickable(mClickMarker.isChecked()) // 设置Marker是否可点击
                .period(10);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快 默认为20，最小为1
        if (mAnimationBox.isChecked()) {
            // 生长动画
            markerOptionsD.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        mMarkerD = (Marker) (mBaiduMap.addOverlay(markerOptionsD));
    }

    public void initListener(){
        // 设置Marker 点击事件监听
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA || marker == mMarkerD) {
                    button.setText("更改位置");
                    button.setTextColor(Color.BLACK);
                    button.setWidth(300);
                    // InfoWindow点击事件监听接口
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                            LatLng latLng = marker.getPosition();
                            LatLng latLngNew = new LatLng(latLng.latitude + 0.005, latLng.longitude + 0.005);
                            marker.setPosition(latLngNew);
                            // 隐藏地图上的所有InfoWindow
                            mBaiduMap.hideInfoWindow();
                        }
                    };
                    LatLng latLng = marker.getPosition();
                    // 创建InfoWindow
                    mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), latLng, -47, listener);
                    // 显示 InfoWindow, 该接口会先隐藏其他已添加的InfoWindow, 再添加新的InfoWindow
                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerB) {
                    button.setText("更改图标");
                    button.setTextColor(Color.BLACK);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.setIcon(bitmapE);
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng latLng = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, latLng, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                } else if (marker == mMarkerC) {
                    button.setText("删除");
                    button.setTextColor(Color.BLACK);
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            marker.remove();
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    LatLng latLng = marker.getPosition();
                    mInfoWindow = new InfoWindow(button, latLng, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });

        // 设置 Marker 拖拽事件监听者
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {

            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(MarkerDemo.this, "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                        + marker.getPosition().longitude, Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {

            }
        });
    }
    /**
     * 清除所有Overlay
     */
    public void clearOverlay(View view) {
        mBaiduMap.clear();
        mMarkerA = null;
        mMarkerB = null;
        mMarkerC = null;
        mMarkerD = null;
    }

    /**
     * 重新添加Overlay
     */
    public void resetOverlay(View view) {
        clearOverlay(null);
        initMarker();
    }

    /**
     * 设置marker 是否可点击
     */
    public void setMarkerClick(View view) {
        if (mMarkerA == null || mMarkerB == null || mMarkerC == null || mMarkerD == null) {
            return;
        }
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()){
            mMarkerA.setClickable(true);
            mMarkerB.setClickable(true);
            mMarkerC.setClickable(true);
            mMarkerD.setClickable(true);
        }else {
            mMarkerA.setClickable(false);
            mMarkerB.setClickable(false);
            mMarkerC.setClickable(false);
            mMarkerD.setClickable(false);
        }
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            float alpha = ((float) seekBar.getProgress()) / 10;
            if (mMarkerA != null) {
                // 设置 Marker 图标的透明度
                mMarkerA.setAlpha(alpha);
            }
            if (mMarkerB != null) {
                mMarkerB.setAlpha(alpha);
            }
            if (mMarkerC != null) {
                mMarkerC.setAlpha(alpha);
            }
            if (mMarkerD != null) {
                mMarkerD.setAlpha(alpha);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // MapView的生命周期与Activity同步，当activity恢复时必须调用MapView.onResume()
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 回收bitmap资源，防止内存泄露
        bitmapA.recycle();
        bitmapB.recycle();
        bitmapC.recycle();
        bitmapD.recycle();
        bitmapE.recycle();
        // 清除所有图层
        mBaiduMap.clear();
        // MapView的生命周期与Activity同步，当activity销毁时必须调用MapView.destroy()
        mMapView.onDestroy();
    }
}
