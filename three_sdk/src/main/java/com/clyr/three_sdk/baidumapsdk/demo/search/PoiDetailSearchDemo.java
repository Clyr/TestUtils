package com.clyr.three_sdk.baidumapsdk.demo.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.clyr.three_sdk.R;

import java.util.List;



/**
 * 演示poi详情搜索功能
 */
public class PoiDetailSearchDemo extends AppCompatActivity implements OnGetPoiSearchResultListener {

    // 声明Search
    private PoiSearch mPoiSearch = null;
    // 声明地图
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    // 搜索uid输入窗口
    private AutoCompleteTextView mEditUidView = null;
    // 详情检索窗口
    private RelativeLayout mPoiDetailView;
    private TextView mDetailInfo;
    private BitmapDescriptor mBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poidetailsearch);
        // 创建map
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 创建poi检索实例，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 详情检索结果展示控件
        mPoiDetailView = (RelativeLayout) findViewById(R.id.poi_detail);
        mDetailInfo = (TextView) findViewById(R.id.detail_info);

        mEditUidView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        mEditUidView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                showPoiDetailView(false);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() <= 0) {
                    mBaiduMap.clear();
                }
            }
        });
    }

    /**
     * 发起详情检索
     */
    public void searchButtonProcess(View view){
        String uid = mEditUidView.getText().toString().trim();
        // 该方法要在Listener之后执行，否则会在某些场景出现拿不到回调结果的情况
        mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUids(uid));
    }


    @Override
    public void onGetPoiResult(PoiResult result) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    /**
     * poi 详情查询结果回调
     * V5.2.0版本新增接口，用于返回详细检索结果
     *
     * @param poiDetailSearchResult poi详情查询结果
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PoiDetailSearchDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
            if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
                Toast.makeText(PoiDetailSearchDemo.this, "抱歉，检索结果为空", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < poiDetailInfoList.size(); i++) {
                // 获取详情检索结果
                PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(i);
                if (null != poiDetailInfo) {
                    // 展示Detail 相关信息
                    addDetailInfor(poiDetailInfo);
                }
            }
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     *  在view中展示 Detail 相关信息
     *
     * @param poiDetailInfo poi详情信息
     */
    private void addDetailInfor(PoiDetailInfo poiDetailInfo) {
        mBaiduMap.clear();
        OverlayOptions markerOptions = new MarkerOptions().position(poiDetailInfo.getLocation()).icon(mBitmap);
        mBaiduMap.addOverlay(markerOptions);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(poiDetailInfo.getLocation()));

        mDetailInfo.setText("poi名称:" + poiDetailInfo.getName() + "\n");
        mDetailInfo.append("poi地址:" + poiDetailInfo.getAddress() + "\n");
        mDetailInfo.append("省份:" + poiDetailInfo.getCity() + "\n");
        mDetailInfo.append("营业时间:" + poiDetailInfo.getShopHours() + "\n");
        mDetailInfo.append("电话:" + poiDetailInfo.getTelephone() + "\n");

        showPoiDetailView(true);
    }


    /**
     * 是否展示详情 view
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
