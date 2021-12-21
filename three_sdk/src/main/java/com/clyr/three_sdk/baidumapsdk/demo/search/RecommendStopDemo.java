package com.clyr.three_sdk.baidumapsdk.demo.search;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RecommendStopInfo;
import com.baidu.mapapi.search.recommendstop.OnGetRecommendStopResultListener;
import com.baidu.mapapi.search.recommendstop.RecommendStopResult;
import com.baidu.mapapi.search.recommendstop.RecommendStopSearch;
import com.baidu.mapapi.search.recommendstop.RecommendStopSearchOption;
import com.clyr.three_sdk.R;

import java.util.List;


/**
 * 推荐上车点Demo.
 */
public class RecommendStopDemo extends Activity implements BaiduMap.OnMapStatusChangeListener,
        OnGetRecommendStopResultListener {

    // 地图View实例
    private MapView mMapView;

    private BaiduMap mBaiduMap;

    private RecommendStopSearch mRecommendStopSearch;

    private BitmapDescriptor mBitmapDescriptor =
            BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_stop);
        initMap();
        mRecommendStopSearch = RecommendStopSearch.newInstance();
        mRecommendStopSearch.setOnGetRecommendStopResultListener(this);
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
    protected void onDestroy() {
        super.onDestroy();
        mRecommendStopSearch.destroy();
        mMapView.onDestroy();
    }

    private void initMap() {
        mMapView = findViewById(R.id.mapview);
        if (null == mMapView) {
            return;
        }
        mBaiduMap = mMapView.getMap();
        if (null == mBaiduMap) {
            return;
        }

        mBaiduMap.setViewPadding(20, 0, 0, 0);

        // 设置初始中心点为北京
        final LatLng center = new LatLng(39.947689, 116.392196);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(center, 18);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mRecommendStopSearch.requestRecommendStop(new RecommendStopSearchOption().location(center));
            }
        });
    }

    /**
     * 推荐上车点添加到地图上
     * @param stops 推荐上车点列表
     */
    private void addMarkerToMap(List<RecommendStopInfo> stops) {
        if (stops == null || stops.size() == 0) {
            return;
        }
        if (mBaiduMap == null) {
            return;
        }

        mBaiduMap.clear();

        for (RecommendStopInfo stop : stops) {
            if (stop == null || stop.getLocation() == null) {
                continue;
            }
            TextView textView = new TextView(getApplicationContext());
            textView.setText(stop.getName());
            textView.setBackgroundColor(Color.WHITE);
            InfoWindow infoWindow = new InfoWindow(textView,
                    stop.getLocation(), -47);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(mBitmapDescriptor).position(stop.getLocation()).scaleX(0.5f).scaleY(0.5f).infoWindow(infoWindow);
            mBaiduMap.addOverlay(markerOptions);
        }
    }

    @Override
    public void onMapStatusChangeStart(MapStatus status) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus status, int reason) {

    }

    @Override
    public void onMapStatusChange(MapStatus status) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus status) {
        final LatLng center = status.target;
        mRecommendStopSearch.requestRecommendStop(new RecommendStopSearchOption().location(center));

    }

    @Override
    public void onGetRecommendStopResult(RecommendStopResult result) {
        if (result != null) {
            addMarkerToMap(result.getRecommendStopInfoList());
        }
    }
}
