/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.clyr.three_sdk.baidumapsdk.demo.search;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.clyr.three_sdk.R;

import java.util.List;



/**
 * 此demo演示了检索行政区域边界数据
 */
public class DistrictSearchDemo extends AppCompatActivity implements OnGetDistricSearchResultListener,
        Button.OnClickListener {

    private DistrictSearch mDistrictSearch;
    private EditText mCity;
    private EditText mDistrict;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mSearchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_search_demo);
        // 初始化检索对象
        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(this);

        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
        mCity = (EditText) findViewById(R.id.city);
        mDistrict = (EditText) findViewById(R.id.district);
        mSearchBtn = (Button) findViewById(R.id.districSearch);
        mSearchBtn.setOnClickListener(this);
    }

    /**
     * 检索回调结果
     * @param districtResult 行政区域信息查询结果
     */
    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        mBaiduMap.clear();
        if (districtResult == null) {
            return;
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<List<LatLng>> polyLines = districtResult.getPolylines();
            if (polyLines == null) {
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {
                OverlayOptions ooPolyline = new PolylineOptions().width(10).points(polyline).dottedLine(true).color(0xAA00FF00);
                mBaiduMap.addOverlay(ooPolyline);
                OverlayOptions ooPolygon = new PolygonOptions().points(polyline).stroke(new Stroke(5, 0xAA00FF88))
                        .fillColor(0xAAFFFF00);
                mBaiduMap.addOverlay(ooPolygon);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()));
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
        // 释放检索对象
        mDistrictSearch.destroy();
        // 清除所有图层
        mBaiduMap.clear();
        // 在activity执行onDestroy时必须调用mMapView. onDestroy ()
        mMapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        String city = mCity.getText().toString().trim();
        String district = mDistrict.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(DistrictSearchDemo.this, "城市名字必填" , Toast.LENGTH_LONG).show();
        } else {
            mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(city).districtName(district));
        }
    }
}
