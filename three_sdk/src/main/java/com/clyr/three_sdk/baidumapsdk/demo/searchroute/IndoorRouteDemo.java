package com.clyr.three_sdk.baidumapsdk.demo.searchroute;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.baidu.mapapi.overlayutil.IndoorRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorPlanNode;
import com.baidu.mapapi.search.route.IndoorRouteLine;
import com.baidu.mapapi.search.route.IndoorRoutePlanOption;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.indoorview.BaseStripAdapter;
import com.clyr.three_sdk.baidumapsdk.demo.indoorview.StripListView;

/**
 * 介绍室内路线规划
 */

public class IndoorRouteDemo extends AppCompatActivity implements OnGetRoutePlanResultListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button mIndoorRoutePlaneBtn;

    private StripListView mStripListView;
    private BaseStripAdapter mFloorListAdapter;
    private MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    private RoutePlanSearch mSearch;
    private IndoorRouteLine mIndoorRouteline;
    private Button mBtnPre = null; // 上一个节点
    private Button mBtnNext = null; // 下一个节点
    private NodeUtils mNodeUtils; // 浏览节点工具类
    private EditText mStartLatitudeET;
    private EditText mStartLongitudeET;
    private EditText mStartfloorET;
    private EditText mEndLatitudeET;
    private EditText mEndLongitudeET;
    private EditText mEndfloorET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainview = inflater.inflate(R.layout.activity_indoorroute, null);
        layout.addView(mainview);

        mStartLatitudeET = (EditText) mainview.findViewById(R.id.start_lat);
        mStartLongitudeET = (EditText)  mainview.findViewById(R.id.start_lon);
        mStartfloorET = (EditText)  mainview.findViewById(R.id.start_floor);
        mEndLatitudeET = (EditText)  mainview.findViewById(R.id.end_lat);
        mEndLongitudeET = (EditText)  mainview.findViewById(R.id.end_lon);
        mEndfloorET =  mainview.findViewById(R.id.end_floor);
        mMapView = (MapView) mainview.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        LatLng centerpos = new LatLng(39.916958, 116.379278); // 西单大悦城
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(centerpos).zoom(19.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        // 设置是否显示室内图, 默认室内图不显示
        mBaiduMap.setIndoorEnable(true);
        // 获取RoutePlan检索实例
        mSearch = RoutePlanSearch.newInstance();
        // 设置路线检索监听者
        mSearch.setOnGetRoutePlanResultListener(this);
        mIndoorRoutePlaneBtn = (Button) mainview.findViewById(R.id.indoorRoutePlane);
        mIndoorRoutePlaneBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startfloor = mStartfloorET.getText().toString().trim();
                String endfloor = mEndfloorET.getText().toString().trim();
                double startLatitude;
                double startLongitude;
                double endLatitude;
                double endLongitude;
                try {
                     startLatitude = Double.valueOf(mStartLatitudeET.getText().toString().trim());
                     startLongitude = Double.valueOf(mStartLongitudeET.getText().toString().trim());
                     endLatitude = Double.valueOf(mEndLatitudeET.getText().toString().trim());
                     endLongitude = Double.valueOf(mEndLongitudeET.getText().toString().trim());
                } catch (NumberFormatException e){
                    e.printStackTrace();
                    Toast.makeText(IndoorRouteDemo.this,"请输入正确经纬度",Toast.LENGTH_LONG).show();
                    return;
                }

                if (startfloor.isEmpty() || endfloor.isEmpty()){
                    Toast.makeText(IndoorRouteDemo.this,"请输楼层信息",Toast.LENGTH_LONG).show();
                    return;
                }

                // 发起室内路线规划检索
                IndoorPlanNode startNode = new IndoorPlanNode(new LatLng(startLatitude,startLongitude), startfloor);
                IndoorPlanNode endNode = new IndoorPlanNode(new LatLng(endLatitude, endLongitude), endfloor);
                IndoorRoutePlanOption indoorRoutePlanOption = new IndoorRoutePlanOption()
                        .from(startNode) // 起点
                        .to(endNode); // 终点
                mSearch.walkingIndoorSearch(indoorRoutePlanOption);
            }
        });

        mStripListView = new StripListView(this);
        layout.addView( mStripListView );
        setContentView(layout);
        mFloorListAdapter = new BaseStripAdapter(IndoorRouteDemo.this);

        mBaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean isIndoorMap, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (!isIndoorMap || mapBaseIndoorMapInfo == null) {
                    mStripListView.setVisibility(View.INVISIBLE);
                    return;
                }
                mFloorListAdapter.setmFloorList( mapBaseIndoorMapInfo.getFloors());
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

        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mNodeUtils = new NodeUtils(this, mBaiduMap);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
        if (indoorRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            IndoorRouteOverlay overlay = new IndoorRouteOverlay(mBaiduMap);
            mIndoorRouteline = indoorRouteResult.getRouteLines().get(0);
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            overlay.setData(indoorRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

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
        mBaiduMap.clear();
        mMapView.onDestroy();
        // 释放检索对象
        mSearch.destroy();
    }

    /**
     * 节点浏览
     *
     * @param view
     */
    public void nodeClick(View view) {
        if (null != mIndoorRouteline) {
            mNodeUtils.browseRoutNode(view,mIndoorRouteline);
        }
    }
}
