/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 轨迹运行demo展示
 */
public class TrackShowDemo extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Polyline mPolyline;
    private Marker mMoveMarker;
    private Handler mHandler;

    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static final int TIME_INTERVAL = 80;
    private static final double DISTANCE = 0.00002;
    private BitmapDescriptor mGreenTexture = BitmapDescriptorFactory.fromAsset("Icon_road_green_arrow.png");
    private BitmapDescriptor mBitmapCar = BitmapDescriptorFactory.fromResource(R.drawable.car);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.onCreate(this, savedInstanceState);
        mBaiduMap = mMapView.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(40.056865, 116.307766));
        builder.zoom(19.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mHandler = new Handler(Looper.getMainLooper());
        drawPolyLine();
        moveLooper();
        mMapView.showZoomControls(false);
    }

    private void drawPolyLine() {
        List<LatLng> polylines = new ArrayList<>();
        for (int index = 0; index < latlngs.length; index++) {
            polylines.add(latlngs[index]);
        }
        polylines.add(latlngs[0]);

        // 绘制纹理PolyLine
        PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(10).customTexture(mGreenTexture)
                .dottedLine(true);
        mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);

        // 添加小车marker
        OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f).icon(mBitmapCar).
                position(polylines.get(0)).rotate((float) getAngle(0));
        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);

        // 添加InfoWindow相关
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.popup);
        button.setText("我是InfoWindow");
        InfoWindow.OnInfoWindowClickListener infoWindowClickListener = new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                Toast.makeText(TrackShowDemo.this, "点击了InfoWindow", Toast.LENGTH_SHORT).show();
            }
        };
        // 添加 Marker 关联的InfoWindow,两者的更新是相互独立的。
        mMoveMarker.showInfoWindow(new InfoWindow(BitmapDescriptorFactory.fromView(button),polylines.get(0), -47,
                infoWindowClickListener));
    }

    /**
     * 根据点获取图标转的角度
     */
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    /**
     * 根据两点算取图标转的角度
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        } else if (slope == 0.0) {
            if (toPoint.longitude > fromPoint.longitude) {
                return -90;
            } else {
                return 90;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {
        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBitmapCar.recycle();
        mGreenTexture.recycle();
        mBaiduMap.clear();
        mMapView.onDestroy();
    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE || slope == 0.0) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * 1 / slope) / Math.sqrt(1 + 1 / (slope * slope)));
    }

    /**
     * 计算y方向每次移动的距离
     */
    private double getYMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE || slope == 0.0) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 循环进行移动逻辑
     */
    public void moveLooper() {
        new Thread() {
            public void run() {
                while (true) {
                    for (int i = 0; i < latlngs.length - 1; i++) {
                        final LatLng startPoint = latlngs[i];
                        final LatLng endPoint = latlngs[i + 1];
                        mMoveMarker.setPosition(startPoint);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // refresh marker's rotate
                                if (mMapView == null) {
                                    return;
                                }
                                mMoveMarker.setRotate((float) getAngle(startPoint, endPoint));
                            }
                        });
                        double slope = getSlope(startPoint, endPoint);
                        // 是不是正向的标示
                        boolean isYReverse = (startPoint.latitude > endPoint.latitude);
                        boolean isXReverse = (startPoint.longitude > endPoint.longitude);
                        double intercept = getInterception(slope, startPoint);
                        double xMoveDistance = isXReverse ? getXMoveDistance(slope) : -1 * getXMoveDistance(slope);
                        double yMoveDistance = isYReverse ? getYMoveDistance(slope) : -1 * getYMoveDistance(slope);

                        for (double j = startPoint.latitude, k = startPoint.longitude;
                             !((j > endPoint.latitude) ^ isYReverse) && !((k > endPoint.longitude) ^ isXReverse); ) {
                            LatLng latLng = null;

                            if (slope == Double.MAX_VALUE) {
                                latLng = new LatLng(j, k);
                                j = j - yMoveDistance;
                            } else if (slope == 0.0) {
                                latLng = new LatLng(j, k - xMoveDistance);
                                k = k - xMoveDistance;
                            } else {
                                latLng = new LatLng(j, (j - intercept) / slope);
                                j = j - yMoveDistance;
                            }

                            final LatLng finalLatLng = latLng;
                            if (finalLatLng.latitude == 0 && finalLatLng.longitude == 0) {
                                continue;
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (mMapView == null) {
                                        return;
                                    }
                                    mMoveMarker.setPosition(finalLatLng);
                                    // 设置 Marker 覆盖物的位置坐标,并同步更新与Marker关联的InfoWindow的位置坐标.
                                    mMoveMarker.setPositionWithInfoWindow(finalLatLng);
                                }
                            });
                            try {
                                Thread.sleep(TIME_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }.start();
    }

    private static final LatLng[] latlngs = new LatLng[]{
            new LatLng(40.055826, 116.307917),
            new LatLng(40.055916, 116.308455),
            new LatLng(40.055967, 116.308549),
            new LatLng(40.056014, 116.308574),
            new LatLng(40.056440, 116.308485),
            new LatLng(40.056816, 116.308352),
            new LatLng(40.057997, 116.307725),
            new LatLng(40.058022, 116.307693),
            new LatLng(40.058029, 116.307590),
            new LatLng(40.057913, 116.307119),
            new LatLng(40.057850, 116.306945),
            new LatLng(40.057756, 116.306915),
            new LatLng(40.057225, 116.307164),
            new LatLng(40.056134, 116.307546),
            new LatLng(40.055879, 116.307636),
            new LatLng(40.055826, 116.307697),
    };

}

