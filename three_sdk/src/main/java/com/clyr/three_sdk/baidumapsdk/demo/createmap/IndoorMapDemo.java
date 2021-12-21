package com.clyr.three_sdk.baidumapsdk.demo.createmap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.indoorview.BaseStripAdapter;
import com.clyr.three_sdk.baidumapsdk.demo.indoorview.StripListView;

/**
 * 此demo介绍室内地图展示
 */
public class IndoorMapDemo extends AppCompatActivity {
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    // 楼层条View
    private StripListView mStripListView;
    private BaseStripAdapter mFloorListAdapter;
    private MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout layout = new RelativeLayout(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainview = inflater.inflate(R.layout.activity_indoormapview, null);
        layout.addView(mainview);

        mMapView = (MapView) mainview.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 西单大悦城
        LatLng centerpos = new LatLng(39.916958, 116.379278);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(centerpos).zoom(19.0f);
        // 更新地图
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        // 设置是否显示室内图, 默认室内图不显示
        mBaiduMap.setIndoorEnable(true);
        // 楼层条View
        mStripListView = new StripListView(this);
        // 楼层条数据适配器
        mFloorListAdapter = new BaseStripAdapter(IndoorMapDemo.this);
        layout.addView(mStripListView);
        setContentView(layout);

        // 设置室内图模式监听
        mBaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean isIndoorMap, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (!isIndoorMap || mapBaseIndoorMapInfo == null) {
                    mStripListView.setVisibility(View.INVISIBLE);
                    return;
                }
                // 设置楼层数据
                mFloorListAdapter.setmFloorList(mapBaseIndoorMapInfo.getFloors());
                mStripListView.setVisibility(View.VISIBLE);
                mStripListView.setStripAdapter(mFloorListAdapter);
                mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
            }
        });
        mStripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMapBaseIndoorMapInfo == null) {
                    return;
                }
                String floor = (String) mFloorListAdapter.getItem(position);
                mBaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());
                mFloorListAdapter.setSelectedPostion(position);
                mFloorListAdapter.notifyDataSetInvalidated();
            }
        });
    }

    /**
     * 显示室内图开关
     */
    public void isShowIndoorMap(View view) {
        mBaiduMap.setIndoorEnable(((CheckBox) view).isChecked());
    }

    /**
     * 显示室内图POI开关
     */
    public void isShowIndoorMapPoi(View view) {
        mBaiduMap.showMapIndoorPoi(((CheckBox) view).isChecked());
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
}
