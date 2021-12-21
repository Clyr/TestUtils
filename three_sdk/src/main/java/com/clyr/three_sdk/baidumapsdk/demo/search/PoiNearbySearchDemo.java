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
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.clyr.three_sdk.R;

import java.util.List;




/**
 * 介绍周边检索和子节点展示
 */
public class PoiNearbySearchDemo extends AppCompatActivity implements OnGetPoiSearchResultListener,
        OnGetSuggestionResultListener, AdapterView.OnItemClickListener, PoiListAdapter.OnGetChildrenLocationListener {

    // 声明 PoiSearch
    private PoiSearch mPoiSearch = null;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    // 检索分页
    private int mLoadIndex = 0;
    // 初始化输入框
    private EditText mEditLatitude;
    private EditText mEditLongitude;
    private EditText mEditRadius;
    private AutoCompleteTextView mKeyWordsView;
    private RelativeLayout mPoiDetailView;
    private ListView mPoiList;
    private List<PoiInfo> mAllPoi;
    private CheckBox mLimitCB;
    private CheckBox mScopeCB;

    private BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poinearbysearch);

        // 创建map
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 创建poi检索实例，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化ui
        mEditLatitude = (EditText) findViewById(R.id.edit_latitude);
        mEditLongitude = (EditText) findViewById(R.id.edit_longitude);
        mEditRadius = (EditText) findViewById(R.id.edit_radius);
        mKeyWordsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        mPoiDetailView = (RelativeLayout) findViewById(R.id.poi_detail);
        mLimitCB = (CheckBox) findViewById(R.id.limit);
        mScopeCB = (CheckBox) findViewById(R.id.scope);
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
     * 响应周边搜索按钮点击事件
     *
     * @param v    检索Button
     */
    public void searchNearbyProcess(View v) {
        //  按搜索按钮时隐藏软件盘，为了在结果回调时计算 PoiDetailView 控件的高度，把地图中poi展示到合理范围内
        KeybordUtil.closeKeybord(this);
        // 获取检索参数
        String strLatitude = mEditLatitude.getText().toString();
        String strLongitude = mEditLongitude.getText().toString();
        String strRadius = mEditRadius.getText().toString();
        String keyWorlds = mKeyWordsView.getText().toString();

        if (strLatitude.isEmpty() || strLongitude.isEmpty()) {
            Toast.makeText(PoiNearbySearchDemo.this, "检索经纬度是必填参数", Toast.LENGTH_LONG).show();
            return;
        }

        if (strRadius.isEmpty()) {
            Toast.makeText(PoiNearbySearchDemo.this, "检索半径是必填参数", Toast.LENGTH_LONG).show();
            return;
        }

        if (keyWorlds.isEmpty()) {
            Toast.makeText(PoiNearbySearchDemo.this, "检索关键字是必填参数", Toast.LENGTH_LONG).show();
            return;
        }

        // 是否严格限定召回结果在设置检索半径范围内,(默认值为false)设置为true时会影响返回结果中total准确性及每页召回poi数量
        boolean limit = false;
        if (mLimitCB.isChecked()) {
            limit = true;
        }

        // 检索结果详细程度：取值为1 或空，则返回基本信息；取值为2，返回检索POI详细信息
        int scope = 1;
        if (mScopeCB.isChecked()) {
            scope = 2;
        }

        LatLng latLng;
        int radius;
        try {
            double latitude = Double.parseDouble(strLatitude);
            double longitude = Double.parseDouble(strLongitude);
            radius = Integer.parseInt(strRadius);
            latLng = new LatLng(latitude, longitude);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确值", Toast.LENGTH_SHORT).show();
            return;
        }

        // 配置请求参数
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption()
                .keyword(mKeyWordsView.getText().toString()) // 检索关键字
                .location(latLng) // 经纬度
                .radius(radius) // 检索半径 单位： m
                .pageNum(mLoadIndex) // 分页编号
                .radiusLimit(limit)
                .scope(scope);
        // 发起检索
        mPoiSearch.searchNearby(nearbySearchOption);
    }

    /**
     * 检索下一页
     *
     * @param v
     */
    public void goToNextPage(View v) {
        mLoadIndex++;
        searchNearbyProcess(null);
    }

    /**
     * 获取周边poi检索结果
     *
     * @param result poi查询结果
     */
    @Override
    public void onGetPoiResult(final PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(PoiNearbySearchDemo.this, "未找到结果", Toast.LENGTH_LONG).show();
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

            double latitude = Double.parseDouble(mEditLatitude.getText().toString());
            double longitude = Double.parseDouble(mEditLongitude.getText().toString());
            int radius = Integer.parseInt(mEditRadius.getText().toString());
            showNearbyArea(new LatLng(latitude, longitude), radius);

            mAllPoi = result.getAllPoi();
            PoiListAdapter poiListAdapter = new PoiListAdapter(this, mAllPoi);
            poiListAdapter.setOnGetChildrenLocationListener(this);
            mPoiList.setAdapter(poiListAdapter);
            showPoiDetailView(true);

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
            Toast.makeText(PoiNearbySearchDemo.this, strInfo, Toast.LENGTH_LONG).show();
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
     * @param childrenLocation  子节点经纬度
     */
    @Override
    public void getChildrenLocation(LatLng childrenLocation) {
        addPoiLoction(childrenLocation);
    }

    private  class MyPoiOverlay extends PoiOverlay {
        MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            Toast.makeText(PoiNearbySearchDemo.this,poi.address,Toast.LENGTH_LONG).show();
            return true;
        }
    }

    /**
     * 对周边检索的范围进行绘制
     *
     * @param center    周边检索中心点坐标
     * @param radius    周边检索半径，单位米
     */
    public void showNearbyArea(LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
        mBaiduMap.addOverlay(ooMarker);

        OverlayOptions ooCircle = new CircleOptions().fillColor( 0xCCCCCC00 )
                .center(center)
                .stroke(new Stroke(5, 0xFFFF00FF ))
                .radius(radius);

        mBaiduMap.addOverlay(ooCircle);
        centerBitmap.recycle();
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
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPoiDetailView(false);
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 回收bitmap资源
        mBitmap.recycle();
        // 释放检索对象
        mPoiSearch.destroy();
        // 清空地图所有的覆盖物
        mBaiduMap.clear();
        // 释放地图
        mMapView.onDestroy();
    }
}
