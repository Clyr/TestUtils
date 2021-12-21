package com.clyr.three_sdk.baidumapsdk.demo.search;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.clyr.three_sdk.R;

import java.util.List;
import java.util.regex.Pattern;



/**
 * 介绍逆地理编码的使用（用坐标检索地址），及周边poi的结果
 */
public class ReverseGeoCodeDemo extends AppCompatActivity implements OnGetGeoCoderResultListener, AdapterView.OnItemClickListener {

    // 搜索模块，也可去掉地图模块独立使用
    private GeoCoder mSearch = null;
    private BaiduMap mBaiduMap = null;
    private MapView mMapView = null;

    // 检索分页
    private int mLoadIndex = 0;
    private ListView mPoiList;
    private RelativeLayout mPoiInfo;
    private EditText mEditLatitude;
    private EditText mEditLongitude;
    private CheckBox mNewVersionCB;
    private EditText mEditRadius;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse_geocoder);

        // 初始化ui
        mEditLatitude = (EditText) findViewById(R.id.lat);
        mEditLongitude = (EditText) findViewById(R.id.lon);
        mPoiInfo = (RelativeLayout) findViewById(R.id.poi_info);
        mNewVersionCB = (CheckBox) findViewById(R.id.newVersion);
        mEditRadius = (EditText) findViewById(R.id.ex_radius);
        mPoiList = (ListView) findViewById(R.id.poi_list);
        mPoiList.setOnItemClickListener(this);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // 隐藏poiInfor 控件
                showNearbyPoiView(false);
            }

            @Override
            public void onMapPoiClick(MapPoi poi) {

            }
        });

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    /**
     * 发起搜索
     */
    public void searchButtonProcess(View v) {
        if (!isFloat(mEditLatitude.getText().toString()) ||
                !isFloat(mEditLongitude.getText().toString())) {
            Toast.makeText(ReverseGeoCodeDemo.this, "请输入正确的数据", Toast.LENGTH_SHORT).show();
            return;
        }
        LatLng ptCenter = new LatLng((Float.valueOf(mEditLatitude.getText().toString())), (Float.valueOf(mEditLongitude.getText().toString())));
        int radius = Integer.parseInt(mEditRadius.getText().toString().trim());
        int version = 0;
        // 反Geo搜索
        if (mNewVersionCB.isChecked()) {
            version = 1;
        }

        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption()
                .location(ptCenter) // 设置反地理编码位置坐标
                .newVersion(version) // 设置是否返回新数据 默认值0不返回，1返回
                .radius(radius) //  POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .pageNum(mLoadIndex);
        // 发起反地理编码请求，该方法必须在监听之后执行，否则会在某些场景出现拿不到回调结果的情况
        mSearch.reverseGeoCode(reverseGeoCodeOption);
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    /**
     * 逆地理编码查询回调结果
     *
     * @param result 逆地理编码查询结果
     */
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(ReverseGeoCodeDemo.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        mBaiduMap.clear();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        // 添加poi
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation()).icon(bitmapDescriptor));
        // 更新地图中心点
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));

        // 获取周边poi结果
        List<PoiInfo> poiList = result.getPoiList();
        if (null != poiList && poiList.size() > 0) {
            PoiListAdapter poiListAdapter = new PoiListAdapter(this, poiList);
            mPoiList.setAdapter(poiListAdapter);
            showNearbyPoiView(true);
        } else {
            Toast.makeText(ReverseGeoCodeDemo.this, "周边没有poi", Toast.LENGTH_LONG).show();
            showNearbyPoiView(false);
        }

        Toast.makeText(ReverseGeoCodeDemo.this, result.getAddress() + " adcode: " + result.getAdcode(), Toast.LENGTH_LONG).show();
        bitmapDescriptor.recycle();
    }

    /**
     * 下一页
     */
    public void goToNextPage(View view) {
        mLoadIndex++;
        searchButtonProcess(null);
    }

    /**
     * 展示poi信息 view
     */
    private void showNearbyPoiView(boolean whetherShow) {
        if (whetherShow) {
            mPoiInfo.setVisibility(View.VISIBLE);
        } else {
            mPoiInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        showNearbyPoiView(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放检索对象
        mSearch.destroy();
        // 清空地图所有的覆盖物
        mBaiduMap.clear();
        // 释放地图
        mMapView.onDestroy();
    }

    public static boolean isFloat(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }
}
