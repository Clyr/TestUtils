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
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
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
 * 介绍区域检索及poi父子节点的效果展示
 */
public class PoiBoundSearchDemo extends AppCompatActivity implements OnGetPoiSearchResultListener,
        OnGetSuggestionResultListener, AdapterView.OnItemClickListener, PoiListAdapter.OnGetChildrenLocationListener {

    // 声明 PoiSearch
    private PoiSearch mPoiSearch = null;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    // 检索分页
    private int loadIndex = 0;

    // 初始化输入框
    private EditText mEditSouthLatitude;
    private EditText mEditSouthLongitude;
    private EditText mEditNorthLatitude;
    private EditText mEditNorthLongitude;
    private AutoCompleteTextView mKeyWordsView;
    private CheckBox mScopeCB;

    // 声明poi展示list
    private RelativeLayout mPoiDetailView;
    private ListView mPoiList;

    // poi结果list
    private List<PoiInfo> mAllPoi;
    private BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poiboundsearch);
        // 创建map
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 创建poi检索实例，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化ui
        mEditSouthLatitude = (EditText) findViewById(R.id.southLat);
        mEditSouthLongitude = (EditText) findViewById(R.id.southLng);
        mEditNorthLatitude = (EditText) findViewById(R.id.northLat);
        mEditNorthLongitude = (EditText) findViewById(R.id.northLng);
        mKeyWordsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        mScopeCB = (CheckBox) findViewById(R.id.scope);
        mPoiDetailView = (RelativeLayout) findViewById(R.id.poi_detail);
        mPoiList = (ListView) findViewById(R.id.poi_list);
        mPoiList.setOnItemClickListener(this);
        //地图点击事件
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
     * 响应区域搜索按钮点击事件
     *
     * @param v 检索Button
     */
    public void searchBoundProcess(View v) {
        //  按搜索按钮时隐藏软件盘，为了在结果回调时计算 PoiDetailView 控件的高度，把地图中poi展示到合理范围内
        KeybordUtil.closeKeybord(this);
        // 获取输入的坐标点
        String southlat = mEditSouthLatitude.getText().toString();
        String southLnt = mEditSouthLongitude.getText().toString();
        String northLat = mEditNorthLatitude.getText().toString();
        String northLng = mEditNorthLongitude.getText().toString();

        if (southlat.isEmpty() || southLnt.isEmpty() || northLat.isEmpty() || northLng.isEmpty()) {
            Toast.makeText(PoiBoundSearchDemo.this, "检索经纬度是必填参数", Toast.LENGTH_LONG).show();
            return;
        }
        // 检索结果详细程度：取值为1 或空，则返回基本信息；取值为2，返回检索POI详细信息
        int scope = 1;
        if (mScopeCB.isChecked()) {
            scope = 2;
        }

        LatLng southwest;
        LatLng northeast;
        try {
            double southwestLatitude = Double.parseDouble(southlat);
            double southwestLongitude = Double.parseDouble(southLnt);
            double northeastLatitude = Double.parseDouble(northLat);
            double northeastLongitude = Double.parseDouble(northLng);
            // 西南角经纬度
            southwest = new LatLng(southwestLatitude, southwestLongitude);
            // 东北角经纬度
            northeast = new LatLng(northeastLatitude, northeastLongitude);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确值", Toast.LENGTH_SHORT).show();
            return;
        }
        // 获取地理范围
        LatLngBounds searchBound = new LatLngBounds.Builder().include(southwest).include(northeast).build();

        // 发起检索
        mPoiSearch.searchInBound(new PoiBoundSearchOption()
                .bound(searchBound) // 检索范围
                .keyword(mKeyWordsView.getText().toString()) // 检索关键字
                .scope(scope) // 设置检索结果详情
                .pageNum(loadIndex)); // 分页
    }

    /**
     * 检索下一页
     */
    public void goToNextPage(View v) {
        loadIndex++;
        searchBoundProcess(null);
    }

    /**
     * 获取区域poi检索结果
     *
     * @param result poi查询结果
     */
    @Override
    public void onGetPoiResult(final PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(PoiBoundSearchDemo.this, "未找到结果", Toast.LENGTH_LONG).show();
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

            String northLat = mEditNorthLatitude.getText().toString();
            String northLng = mEditNorthLongitude.getText().toString();
            String southlat = mEditSouthLatitude.getText().toString();
            String southLnt = mEditSouthLongitude.getText().toString();
            // 西南角经纬度
            LatLng northeast = new LatLng(Double.parseDouble(northLat), Double.parseDouble(northLng));
            // 东北角经纬度
            LatLng southwest = new LatLng(Double.parseDouble(southlat), Double.parseDouble(southLnt));
            // 获取地图范围
            LatLngBounds latLngBounds = new LatLngBounds.Builder().include(southwest).include(northeast).build();
            showBound(latLngBounds);

            // 获取poi检索结果
            mAllPoi = result.getAllPoi();
            PoiListAdapter poiListAdapter = new PoiListAdapter(this, mAllPoi);
            poiListAdapter.setOnGetChildrenLocationListener(this);
            // poi检索结果设置到适配器
            mPoiList.setAdapter(poiListAdapter);
            showPoiDetailView(true);
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

    private class MyPoiOverlay extends PoiOverlay {
        MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            Toast.makeText(PoiBoundSearchDemo.this,poi.address,Toast.LENGTH_LONG).show();
            return true;
        }
    }

    /**
     * 对区域检索的范围进行绘制
     *
     * @param bounds 区域检索指定区域
     */
    public void showBound(LatLngBounds bounds) {
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromResource(R.drawable.ground_overlay);

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdGround)
                .transparency(0.8f) // 设置 ground 覆盖物透明度 范围:[0.0f , 1.0f]
                .zIndex(1);

        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(mapStatusUpdate);
        bdGround.recycle();
    }

    /**
     * 更新到子节点的位置
     *
     * @param latLng 子节点经纬度
     */
    private void addPoiLoction(LatLng latLng){
        mBaiduMap.clear();
        showPoiDetailView(false);
        OverlayOptions markerOptions = new MarkerOptions().position(latLng).icon(mBitmap);
        mBaiduMap.addOverlay(markerOptions);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        builder.zoom(18);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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
        // 在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
        showPoiDetailView(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBitmap.recycle();
        // 释放检索对象
        mPoiSearch.destroy();
        // 清除所有图层
        mBaiduMap.clear();
        // 在activity执行onDestroy时必须调用mMapView. onDestroy ()
        mMapView.onDestroy();
    }
}
