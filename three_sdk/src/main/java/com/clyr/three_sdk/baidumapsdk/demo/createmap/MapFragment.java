package com.clyr.three_sdk.baidumapsdk.demo.createmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;


public class MapFragment extends Fragment {

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_map, container, false);

        initView(inflate);

        // 构建地图状态
        MapStatus.Builder builder = new MapStatus.Builder();
        // 默认 天安门
        LatLng center = new LatLng(39.915071, 116.403907);
        // 默认 11级
        float zoom = 11.0f;

        builder.target(center).zoom(zoom);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(builder.build());

        // 设置地图状态
        mBaiduMap.setMapStatus(mapStatusUpdate);

        return inflate;
    }

    private void initView(View inflate) {
        mMapView = inflate.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 设置底图显示模式
        RadioGroup mRadioGroup = inflate.findViewById(R.id.RadioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.normal) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                } else if (checkedId == R.id.statellite) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                } else if (checkedId == R.id.none) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
                }
            }
        });

        // 清除地图缓存数据，支持清除普通地图和卫星图缓存，再次进入地图页面生效。
        inflate.findViewById(R.id.cache_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaiduMap == null) {
                    return;
                }
                int mapType = mBaiduMap.getMapType();
                if (mapType == BaiduMap.MAP_TYPE_NORMAL) {
                    // // 清除地图缓存数据
                    mBaiduMap.cleanCache(BaiduMap.MAP_TYPE_NORMAL);
                } else if (mapType == BaiduMap.MAP_TYPE_SATELLITE) {
                    // 清除地图缓存数据
                    mBaiduMap.cleanCache(BaiduMap.MAP_TYPE_SATELLITE);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
