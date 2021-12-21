/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.FileTileProvider;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Tile;
import com.baidu.mapapi.map.TileOverlay;
import com.baidu.mapapi.map.TileOverlayOptions;
import com.baidu.mapapi.map.TileProvider;
import com.baidu.mapapi.map.UrlTileProvider;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.clyr.three_sdk.R;

import java.io.InputStream;
import java.nio.ByteBuffer;




/**
 * TileOverlay 添加自定义瓦片图demo
 */
public class TileOverlayDemo extends AppCompatActivity implements BaiduMap.OnMapLoadedCallback {
    @SuppressWarnings("unused")
    private static final String LTAG = TileOverlayDemo.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mOnline;
    private Button mOffline;
    // 设置瓦片图的在线缓存大小，默认为20 M
    private static final int TILE_TMP = 20 * 1024 * 1024;
    private static final int MAX_LEVEL = 21;
    private static final int MIN_LEVEL = 4;
    private EditText mEditText;
    private CheckBox mHidePoiInfoCB = null;
    private TileProvider mTileProvider;
    private TileOverlay mTileOverlay;
    private Tile mOfflineTile;
    private MapStatusUpdate mMapStatusUpdate;
    private final String onlineUrl = "http://online1.map.bdimg.com/tile/?qt=vtile&x={x}&y={y}&z={z}&styles=pl&scaler"
            + "=1&udt=20190528";
    private boolean mapLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_overlay_demo);
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(16.0f);
        builder.target(new LatLng(39.914935D, 116.403119D));
        mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mOnline = (Button) findViewById(R.id.online);
        mOffline = (Button) findViewById(R.id.offline);
        mEditText = (EditText) findViewById(R.id.online_url);
        mHidePoiInfoCB = (CheckBox) findViewById(R.id.hide_poiinfo);
        // 设置在线方法监听
        mOnline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onlineTile();
            }
        });
        // 设置离线方法监听
        mOffline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                offlineTile();
            }
        });
        mHidePoiInfoCB.setOnCheckedChangeListener(new HidePoiInfoListener());
        mEditText.setText(onlineUrl);
    }

    /**
     * 使用瓦片图的在线方式
     */
    private void onlineTile() {
        if (null == mBaiduMap) {
            return;
        }

        if (mTileOverlay != null) {
            mTileOverlay.removeTileOverlay();
        }
        final String urlString = mEditText.getText().toString();

        /*定义瓦片图的在线Provider，并实现相关接口
          MAX_LEVEL、MIN_LEVEL 表示地图显示瓦片图的最大、最小级别
          urlString 表示在线瓦片图的URL地址*/
        TileProvider tileProvider = new UrlTileProvider() {
            @Override
            public int getMaxDisLevel() {
                return MAX_LEVEL;
            }

            @Override
            public int getMinDisLevel() {
                return MIN_LEVEL;
            }

            @Override
            public String getTileUrl() {
                return urlString;
            }
        };
        TileOverlayOptions options = new TileOverlayOptions().tileProvider(tileProvider);
        if (options == null) {
            return;
        }
        // 构造显示瓦片图范围，当前为世界范围
        LatLng northeast = new LatLng(80, 180);
        LatLng southwest = new LatLng(-80, -180);
        // 通过option指定相关属性，向地图添加在线瓦片图对象
        mTileOverlay = mBaiduMap.addTileLayer(options.setMaxTileTmp(TILE_TMP)
                .setPositionFromBounds(new LatLngBounds.Builder().include(northeast).include(southwest).build()));

        if (mapLoaded) {
             /* 设置底图类型为NONE，即空白地图，
              以避免瓦片图标注与底图标注存在叠加显示，影响暂时效果
              同时提高加载速度*/
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
            mBaiduMap.setMaxAndMinZoomLevel(21.0f, 3.0f);
        }
    }

    /**
     * 瓦片图的离线添加
     */
    private void offlineTile() {

        if (mBaiduMap == null) {
            return;
        }

        if (mTileOverlay != null) {
            mTileOverlay.removeTileOverlay();
        }

         /* 定义瓦片图的离线Provider，并实现相关接口
          MAX_LEVEL、MIN_LEVEL 表示地图显示瓦片图的最大、最小级别
          Tile 对象表示地图每个x、y、z状态下的瓦片对象*/
        mTileProvider = new FileTileProvider() {
            @Override
            public Tile getTile(int x, int y, int z) {
                // 根据地图某一状态下x、y、z加载指定的瓦片图
                String filedir = "LocalTileImage/" + z + "/" + z + "_" + x + "_" + y + ".jpg";
                Bitmap bm = getFromAssets(filedir);
                if (bm == null) {
                    return null;
                }
                // 瓦片图尺寸必须满足256 * 256
                mOfflineTile = new Tile(bm.getWidth(), bm.getHeight(), toRawData(bm));
                bm.recycle();
                return mOfflineTile;
            }

            @Override
            public int getMaxDisLevel() {
                return MAX_LEVEL;
            }

            @Override
            public int getMinDisLevel() {
                return MIN_LEVEL;
            }

        };
        TileOverlayOptions options = new TileOverlayOptions();
        // 构造显示瓦片图范围，当前为世界范围
        LatLng northeast = new LatLng(80, 180);
        LatLng southwest = new LatLng(-80, -180);
        // 设置离线瓦片图属性option
        options.tileProvider(mTileProvider).setPositionFromBounds(new LatLngBounds.Builder().include(northeast)
                .include(southwest).build());
        // 通过option指定相关属性，向地图添加离线瓦片图对象
        mTileOverlay = mBaiduMap.addTileLayer(options);

        if (mapLoaded) {
            setMapStatusLimits();
        }
    }

    private void setMapStatusLimits() {
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(39.94001804746338, 116.41224644234747))
                .include(new LatLng(39.90299859954822, 116.38359947963427));
        mBaiduMap.setMaxAndMinZoomLevel(17.0f, 16.0f);
        mBaiduMap.setMapStatusLimits(builder.build());
    }

    /**
     * 瓦片文件解析为Bitmap
     *
     * @param fileName
     * @return 瓦片文件的Bitmap
     */
    public Bitmap getFromAssets(String fileName) {
        AssetManager assetManager = this.getAssets();
        InputStream inputStream;
        Bitmap bitmap;

        try {
            inputStream = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onMapLoaded() {
        mapLoaded = true;
    }

    private class HidePoiInfoListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mBaiduMap.showMapPoi(false);
            } else {
                mBaiduMap.showMapPoi(true);
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
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }

    /**
     * 解析Bitmap
     *
     * @param bitmap
     * @return
     */
    byte[] toRawData(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight() * 4);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] data = buffer.array();
        buffer.clear();
        return data;
    }
}
