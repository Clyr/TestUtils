package com.clyr.three_sdk.baidumapsdk.demo.search;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.IndoorPoiOverlay;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorInfo;
import com.baidu.mapapi.search.poi.PoiIndoorOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.indoorview.BaseStripAdapter;
import com.clyr.three_sdk.baidumapsdk.demo.indoorview.StripListView;

/**
 * 演示室内poi检索功能
 */
public class IndoorSearchDemo extends AppCompatActivity implements OnGetPoiSearchResultListener,
        BaiduMap.OnBaseIndoorMapListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch = null;
    private Button mSearchBtn;
    private EditText mEditSearchContent;
    private StripListView mStripView;
    private BaseStripAdapter mFloorListAdapter;
    private MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_search);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        // 因为室内图支持最大缩放级别为22.0f，要做特殊处理，所以要先打开室内图开关，然后做地图刷新，以防设置缩放级别为22.0f时不生效
        mBaiduMap.setIndoorEnable(true);

        LatLng centerpos = new LatLng(39.871281,116.385306); // 北京南站
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(centerpos).zoom(19.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        mStripView = new StripListView(this);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.viewStub);
        layout.addView(mStripView);
        mFloorListAdapter = new BaseStripAdapter(this);
        mBaiduMap.setOnBaseIndoorMapListener(this);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        mEditSearchContent = (EditText) findViewById(R.id.content);
        mSearchBtn = (Button) findViewById(R.id.indoorSearch);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapBaseIndoorMapInfo indoorInfo = mBaiduMap.getFocusedBaseIndoorMapInfo();
                if (indoorInfo == null) {
                    Toast.makeText(IndoorSearchDemo.this, "当前无室内图，无法搜索" , Toast.LENGTH_LONG).show();
                    return;
                }
                PoiIndoorOption option = new PoiIndoorOption().poiIndoorBid(indoorInfo.getID())
                        .poiIndoorWd(mEditSearchContent.getText().toString());
                mPoiSearch.searchPoiIndoor( option);
            }
        });

        mStripView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    @Override
    public void onGetPoiResult(PoiResult result) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult result) {

    }

    /**
     * poi 室内检索结果回调
     *
     * @param result poi室内检索结果
     */
    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult result) {
        mBaiduMap.clear();
        if (result == null  || result.error == PoiIndoorResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(IndoorSearchDemo.this, "无结果" , Toast.LENGTH_LONG).show();
            return;
        }
        if (result.error == PoiIndoorResult.ERRORNO.NO_ERROR) {
            IndoorPoiOverlay overlay = new MyIndoorPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    /**
     * 地图进入室内图模式回调函数
     *
     * @param isIndoorMap 是否进入室内图模式
     * @param mapBaseIndoorMapInfo 室内图信息
     */
    @Override
    public void onBaseIndoorMapMode(boolean isIndoorMap, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
        if (isIndoorMap) {
            mStripView.setVisibility(View.VISIBLE);
            if (mapBaseIndoorMapInfo == null) {
                return;
            }
            mFloorListAdapter.setmFloorList(mapBaseIndoorMapInfo.getFloors());
            mStripView.setStripAdapter(mFloorListAdapter);

        } else {
            mStripView.setVisibility(View.GONE);
        }
        mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
    }

    private class MyIndoorPoiOverlay extends IndoorPoiOverlay {

        /**
         * 构造函数
         *
         * @param baiduMap 该 IndoorPoiOverlay 引用的 BaiduMap 对象
         */
        private MyIndoorPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        /**
         * 响应点击室内POI点事件
         * @param i
         *            被点击的poi在
         *            {@link com.baidu.mapapi.search.poi.PoiIndoorResult#getmArrayPoiInfo()} } 中的索引
         * @return
         */
        @Override
        public boolean onPoiClick(int i) {
            PoiIndoorInfo info =  getIndoorPoiResult().getmArrayPoiInfo().get(i);
            Toast.makeText(IndoorSearchDemo.this, info.name + ",在" + info.floor + "层", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
        mMapView.onDestroy();
    }
}
