package com.clyr.three_sdk.baidumapsdk.demo.search;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.clyr.three_sdk.R;

import java.util.List;



/**
 * 演示城市检索poi以及父子节点的展示
 */
public class PoiCitySearchDemo extends AppCompatActivity implements OnGetPoiSearchResultListener,
        OnGetSuggestionResultListener, AdapterView.OnItemClickListener, PoiListAdapter.OnGetChildrenLocationListener {

    private PoiSearch mPoiSearch = null;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    // 搜索关键字输入窗口
    private EditText mEditCity = null;
    private AutoCompleteTextView mKeyWordsView = null;
    private RelativeLayout mPoiDetailView;
    private ListView mPoiList;
    private CheckBox mLimitCB;
    private CheckBox mScopeCB;
    // 分页
    private int mLoadIndex = 0;
    private List<PoiInfo> mAllPoi;
    private BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poicitysearch);
        // 创建map
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 创建poi检索实例，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mEditCity = (EditText) findViewById(R.id.city);
        mKeyWordsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        mLimitCB = (CheckBox) findViewById(R.id.limit);
        mScopeCB = (CheckBox) findViewById(R.id.scope);

        // 展示父子节点控件
        mPoiDetailView = (RelativeLayout) findViewById(R.id.poi_detail);
        mPoiList = (ListView) findViewById(R.id.poi_list);
        mPoiList.setOnItemClickListener(this);
        // 地图点击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                showPoiDetailView(false);
            }

            @Override
            public void onMapPoiClick(MapPoi poi) {

            }
        });
    }

    /**
     * 响应城市内搜索按钮点击事件
     *
     * @param v 检索Button
     */
    public void searchButtonProcess(View v) {
        //  按搜索按钮时隐藏软件盘，为了在结果回调时计算 PoiDetailView 控件的高度，把地图中poi展示到合理范围内
        KeybordUtil.closeKeybord(this);
        // 获取检索城市
        String cityStr = mEditCity.getText().toString();
        // 获取检索关键字
        String keyWordStr = mKeyWordsView.getText().toString();

        // 区域数据召回限制为true时，仅召回city对应区域内数据
        boolean limit = false;
        if (mLimitCB.isChecked()) {
            limit = true;
        }

        // 检索结果详细程度：取值为1 或空，则返回基本信息；取值为2，返回检索POI详细信息
        int scope = 1;
        if (mScopeCB.isChecked()) {
            scope = 2;
        }

        // 发起请求
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(cityStr)
                .keyword(keyWordStr)
                .pageNum(mLoadIndex) // 分页编号
                .cityLimit(limit)
                .scope(scope));
    }

    /**
     * 下一页
     */
    public void goToNextPage(View v) {
        mLoadIndex++;
        searchButtonProcess(null);
    }

    /**
     * 获取城市poi检索结果
     *
     * @param result poi查询结果
     */
    @Override
    public void onGetPoiResult(final PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            mLoadIndex = 0;
            mBaiduMap.clear();
            showPoiDetailView(false);
            Toast.makeText(PoiCitySearchDemo.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            showPoiDetailView(true);
            mBaiduMap.clear();

            // 监听 View 绘制完成后获取view的高度
            mPoiDetailView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int padding = 50;
                    // 添加poi
                    PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result);
                    overlay.addToMap();
                    // 获取 view 的高度
                    int PaddingBootom = mPoiDetailView.getMeasuredHeight();
                    // 设置显示在规定宽高中的地图地理范围
                    overlay.zoomToSpanPaddingBounds(padding,padding,padding,PaddingBootom);
                    // 加载完后需要移除View的监听，否则会被多次触发
                    mPoiDetailView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

            // 获取poi结果
            mAllPoi = result.getAllPoi();
            PoiListAdapter poiListAdapter = new PoiListAdapter(this, mAllPoi);
            poiListAdapter.setOnGetChildrenLocationListener(this);
            // 把poi结果添加到适配器
            mPoiList.setAdapter(poiListAdapter);

            return;
        }

        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";

            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(PoiCitySearchDemo.this, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult result) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }

    /**
     * poilist 点击处理
     *
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PoiInfo poiInfo = mAllPoi.get(position);
        if (poiInfo.getLocation() == null) {
            return;
        }

        addPoiLoction(poiInfo.getLocation());
    }

    /**
     * 点击子节点list 获取经纬添加poi更新地图
     *
     * @param childrenLocation 子节点经纬度
     */
    @Override
    public void getChildrenLocation(LatLng childrenLocation) {

        addPoiLoction(childrenLocation);
    }


    /**
     * 更新到子节点的位置
     *
     * @param latLng 子节点经纬度
     */
    private void addPoiLoction(LatLng latLng) {
        mBaiduMap.clear();
        showPoiDetailView(false);
        OverlayOptions markerOptions = new MarkerOptions().position(latLng).icon(mBitmap);
        mBaiduMap.addOverlay(markerOptions);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        builder.zoom(18.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    protected class MyPoiOverlay extends PoiOverlay {
        MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            Toast.makeText(PoiCitySearchDemo.this,poi.address,Toast.LENGTH_LONG).show();
            return true;
        }
    }


    /**
     * 是否展示详情 view
     *
     */
    private void showPoiDetailView(boolean whetherShow) {
        if (whetherShow) {
            mPoiDetailView.setVisibility(View.VISIBLE);
        } else {
            mPoiDetailView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 隐藏控件
        showPoiDetailView(false);
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放bitmap
        mBitmap.recycle();
        // 释放检索对象
        mPoiSearch.destroy();
        // 清空地图所有的覆盖物
        mBaiduMap.clear();
        // 释放地图组件
        mMapView.onDestroy();
    }
}
