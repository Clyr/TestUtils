package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Prism;
import com.baidu.mapapi.map.PrismOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.building.BuildingResult;
import com.baidu.mapapi.search.building.BuildingSearch;
import com.baidu.mapapi.search.building.BuildingSearchOption;
import com.baidu.mapapi.search.building.OnGetBuildingSearchResultListener;
import com.baidu.mapapi.search.core.BuildingInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.clyr.three_sdk.R;

import java.util.List;



public class Building3DPrismDemo extends AppCompatActivity implements
        OnGetBuildingSearchResultListener,
        View.OnClickListener, OnGetDistricSearchResultListener {

    private BuildingSearch mBuildingSearch;
    private DistrictSearch mDistrictSearch;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mDistrictPrismButton;
    private Button mBuildingPrismButton;
    private Button mCleanPrismButton;
    private Prism mBuildingPrism;
    private Prism mCustomPrism;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_3d_overlay_demo);
        initView();
    }

    private void initView() {
        mMapView = findViewById(R.id.bmapView);
        mDistrictPrismButton = findViewById(R.id.district_prism);
        mBuildingPrismButton = findViewById(R.id.building_prism);
        mCleanPrismButton = findViewById(R.id.clean_prism);
        mDistrictPrismButton.setOnClickListener(this);
        mBuildingPrismButton.setOnClickListener(this);
        mCleanPrismButton.setOnClickListener(this);
        mBaiduMap = mMapView.getMap();
        mDistrictSearch = mDistrictSearch.newInstance();
        mBuildingSearch = BuildingSearch.newInstance();
        mBuildingSearch.setOnGetBuildingSearchResultListener(this);
    }

    private void searchBuilding() {
        BuildingSearchOption buildingSearchOption = new BuildingSearchOption();
        buildingSearchOption.setLatLng(new LatLng(23.02738, 113.748139));
        mBuildingSearch.requestBuilding(buildingSearchOption);
    }

    @Override
    public void onGetBuildingResult(BuildingResult result) {
        if (null == result || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        List<BuildingInfo> buildingList = result.getBuildingList();
        for (int i = 0; i < buildingList.size(); i++) {
            BuildingInfo buildingInfo = buildingList.get(i);
            // 创建3D棱柱覆盖物选类配置参数
            PrismOptions prismOptions = new PrismOptions();
            prismOptions.setBuildingInfo(buildingInfo);
            prismOptions.setSideFaceColor(0xAAFF0000);
            prismOptions.setTopFaceColor(0xAA00FF00);
            // 添加3D棱柱
            mBuildingPrism = (Prism) mBaiduMap.addOverlay(prismOptions);
        }
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(23.02738, 113.748139));
        builder.zoom(20);
        builder.overlook(-30.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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
        mMapView.onDestroy();
        if (null != mBuildingSearch) {
            mBuildingSearch.destroy();
        }
        if (null != mDistrictSearch) {
            mDistrictSearch.destroy();
        }
    }

    private void searchDistrict() {
        mDistrictSearch.setOnDistrictSearchListener(this);
        mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName("北京市").districtName("海淀区"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.district_prism) {
            searchDistrict();
        } else if (v.getId() == R.id.building_prism) {
            searchBuilding();
        } else if (v.getId() == R.id.clean_prism) {
            clean3DPrim();
        }
    }

    private void clean3DPrim() {
        if (null != mBuildingPrism) {
            // 清除建筑物3D棱柱
            mBuildingPrism.remove();
        }
        if (null != mCustomPrism) {
            // 清除自定义3D棱柱
            mCustomPrism.remove();
        }
    }

    @Override
    public void onGetDistrictResult(DistrictResult result) {
        if (null != result && result.error == SearchResult.ERRORNO.NO_ERROR) {

            List<List<LatLng>> polyLines = result.getPolylines();
            if (polyLines == null) {
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {
                PrismOptions prismOptions = new PrismOptions();
                prismOptions.setHeight(200);
                prismOptions.setPoints(polyline);
                prismOptions.setSideFaceColor(0xAAFF0000);
                prismOptions.setTopFaceColor(0xAA00FF00);
                prismOptions.customSideImage(BitmapDescriptorFactory.fromResource(R.drawable.wenli));
                // 添加自定3D棱柱
                mCustomPrism = (Prism) mBaiduMap.addOverlay(prismOptions);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()));
            MapStatus.Builder builder1 = new MapStatus.Builder();
            builder1.overlook(-30.0f);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
        }
    }
}