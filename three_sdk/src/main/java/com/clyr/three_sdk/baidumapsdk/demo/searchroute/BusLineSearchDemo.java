package com.clyr.three_sdk.baidumapsdk.demo.searchroute;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 此demo用来展示如何进行公交线路详情检索，并使用RouteOverlay在地图上绘制 同时展示如何浏览路线节点并弹出泡泡
 */
public class BusLineSearchDemo extends AppCompatActivity implements OnGetPoiSearchResultListener,
        OnGetBusLineSearchResultListener, BaiduMap.OnMapClickListener {
    private Button mPreviousBtn = null; // 上一个节点
    private Button mNextBtn = null; // 下一个节点
    private BusLineResult mBusLineResult = null; // 保存驾车/步行路线数据的变量，供浏览节点时使用
    private List<String> mBusLineIDList = null;
    private int mBusLineIndex = 0;
    // 搜索相关
    private PoiSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private BusLineSearch mBusLineSearch = null;

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private BusLineOverlay mBusLineOverlay; // 公交路线绘制对象
    private NodeUtils mNodeUtils;
    private EditText mEditCity;
    private EditText mEditSearchKey;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busline);

        CharSequence titleLable = "公交线路查询功能";
        setTitle(titleLable);

        mPreviousBtn = (Button) findViewById(R.id.pre);
        mNextBtn = (Button) findViewById(R.id.next);
        mPreviousBtn.setVisibility(View.INVISIBLE);
        mNextBtn.setVisibility(View.INVISIBLE);
        mEditCity = (EditText) findViewById(R.id.city);
        mEditSearchKey = (EditText) findViewById(R.id.searchkey);

        //获取地图控件
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapClickListener(this);

        mSearch = PoiSearch.newInstance();
        mSearch.setOnGetPoiSearchResultListener(this);
        mBusLineSearch = BusLineSearch.newInstance();
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);

        mBusLineIDList = new ArrayList<String>();
        mBusLineOverlay = new BusLineOverlay(mBaiduMap);
        mBaiduMap.setOnMarkerClickListener(mBusLineOverlay);
        mNodeUtils = new NodeUtils(this, mBaiduMap);
    }

    /**
     * 发起检索
     */
    public void searchButtonProcess(View v) {
        mBusLineIDList.clear();
        mBusLineIndex = 0;
        mPreviousBtn.setVisibility(View.INVISIBLE);
        mNextBtn.setVisibility(View.INVISIBLE);
        // 发起poi检索，从得到所有poi中找到公交线路类型的poi，再使用该poi的uid进行公交详情搜索
        mSearch.searchInCity((new PoiCitySearchOption())
                .city(mEditCity.getText().toString()) // 检索城市
                .keyword(mEditSearchKey.getText().toString())); //  搜索关键字
    }

    public void searchNextBusline(View v) {
        if (mBusLineIndex >= mBusLineIDList.size()) {
            mBusLineIndex = 0;
        }
        if (mBusLineIndex >= 0 && mBusLineIndex < mBusLineIDList.size()) {
            mBusLineSearch.searchBusLine((new BusLineSearchOption()
                    .city(mEditCity.getText().toString()) // 设置查询城市
                    .uid(mBusLineIDList.get(mBusLineIndex))));// 设置公交路线uid
            mBusLineIndex++;
        }
    }

    /**
     * 节点浏览
     */
    public void nodeClick(View v) {
        if ( null != mBusLineResult ){
            mNodeUtils.browseBusRouteNode(v,mBusLineResult);
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
        mBusLineIDList.clear();
        // 释放检索对象
        mSearch.destroy();
        mBusLineSearch.destroy();
        // 清除所有图层
        mBaiduMap.clear();
        // 在activity执行onDestroy时必须调用mMapView. onDestroy ()
        mMapView.onDestroy();
    }

    /**
     * 公交信息查询结果回调函数
     *
     * @param result  公交信息查询结果
     *
     */
    @Override
    public void onGetBusLineResult(BusLineResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BusLineSearchDemo.this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        mBaiduMap.clear();
        mBusLineResult = result;
        mBusLineOverlay.removeFromMap();
        mBusLineOverlay.setData(result);
        mBusLineOverlay.addToMap();
        mBusLineOverlay.zoomToSpan();
        mPreviousBtn.setVisibility(View.VISIBLE);
        mNextBtn.setVisibility(View.VISIBLE);
        Toast.makeText(BusLineSearchDemo.this, result.getBusLineName(), Toast.LENGTH_SHORT).show();
    }

    /**
     * poi 查询结果回调
     *
     * @param result poi查询结果
     */
    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BusLineSearchDemo.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        // 遍历所有poi，找到类型为公交线路的poi
        mBusLineIDList.clear();
        for (PoiInfo poi : result.getAllPoi()) {
            mBusLineIDList.add(poi.uid);
        }
        searchNextBusline(null);
        mBusLineResult = null;
    }


    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult result) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onMapClick(LatLng point) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public void onMapPoiClick(MapPoi poi) {

    }
}
