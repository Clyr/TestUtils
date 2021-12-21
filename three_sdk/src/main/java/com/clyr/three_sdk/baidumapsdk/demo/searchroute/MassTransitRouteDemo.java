/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.clyr.three_sdk.baidumapsdk.demo.searchroute;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.MassTransitRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 此demo用来展示如何进行跨城综合路线搜索并在地图使用RouteOverlay、TransitOverlay绘制
 * 同时展示如何进行节点浏览并弹出泡泡
 */
public class MassTransitRouteDemo extends AppCompatActivity implements BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {

    // 浏览路线节点相关
    private Button mBtnPre = null; // 上一个节点
    private Button mBtnNext = null; // 下一个节点
    private MassTransitRouteLine mMassTransitRouteLine = null;
    private OverlayManager mRouteOverlay = null;
    private boolean mUseDefaultIcon = false; // 切换路线图标

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    private MapView mMapView = null;    // 地图View
    private BaiduMap mBaidumap = null;
    // 搜索相关
    private RoutePlanSearch mSearch = null;   // 搜索模块，也可去掉地图模块独立使用
    private MassTransitRouteResult mMassTransitRouteResult = null;
    private boolean hasShowDialog = false;
    // 换乘路线规划参数
    private MassTransitRoutePlanOption mMassTransitRoutePlanOption = null;
    private NodeUtils mNodeUtils;
    private EditText mEditStartCity;
    private EditText mEditEndCity;
    private AutoCompleteTextView mStrartNodeView;
    private AutoCompleteTextView mEndNodeView;
    private Spinner mTranstypeSpinner;
    private Spinner mIntercitySpinner;
    private Spinner mIncitySpinner;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mass_transit_route);

        mEditStartCity = (EditText) findViewById(R.id.st_city);
        mStrartNodeView = (AutoCompleteTextView) findViewById(R.id.st_node);
        mEditEndCity = (EditText) findViewById(R.id.ed_city);
        mEndNodeView = (AutoCompleteTextView) findViewById(R.id.ed_node);
        mIncitySpinner = (Spinner) findViewById(R.id.tactics_incity_sp);
        mTranstypeSpinner = (Spinner) findViewById(R.id.transtype_intercity_sp);
        mIntercitySpinner = (Spinner) findViewById(R.id.tactics_intercity_sp);

        // 初始化地图
        mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();

        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        // 创建换乘路线规划option
        if (mMassTransitRoutePlanOption == null){
            // 设置策略前创建
            mMassTransitRoutePlanOption = new MassTransitRoutePlanOption();
        }
        // 设置市内公交换乘策略
        setTacticsIncity();
        // 设置跨城交通方式策略
        setTacticsIntercity();
        // 设置跨城交通方式策略
        setTransTypeIntercity();
        mNodeUtils = new NodeUtils(this, mBaidumap);
    }

    /**
     *  设置市内公交换乘策略
     */
    private void  setTacticsIncity(){
        List<String> list = new ArrayList<>();
        list.add("推荐");
        list.add("少换成");
        list.add("少步行");
        list.add("不坐地铁");
        list.add("时间短");
        list.add("地铁优先");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mIncitySpinner.setAdapter(adapter);
        mIncitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 推荐
                        mMassTransitRoutePlanOption.tacticsIncity(MassTransitRoutePlanOption.TacticsIncity.ETRANS_SUGGEST);
                        break;
                    case 1:
                        // 少换成
                        mMassTransitRoutePlanOption.tacticsIncity(MassTransitRoutePlanOption.TacticsIncity.ETRANS_LEAST_TRANSFER);
                        break;
                    case 2:
                        // 少步行
                        mMassTransitRoutePlanOption.tacticsIncity(MassTransitRoutePlanOption.TacticsIncity.ETRANS_LEAST_WALK);
                        break;
                    case 3:
                        // 不坐地铁
                        mMassTransitRoutePlanOption.tacticsIncity(MassTransitRoutePlanOption.TacticsIncity.ETRANS_NO_SUBWAY);
                        break;
                    case 4:
                        // 时间短
                        mMassTransitRoutePlanOption.tacticsIncity(MassTransitRoutePlanOption.TacticsIncity.ETRANS_LEAST_TIME);
                        break;
                    case 5:
                        // 地铁优先
                        mMassTransitRoutePlanOption.tacticsIncity(MassTransitRoutePlanOption.TacticsIncity.ETRANS_SUBWAY_FIRST);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 设置跨城交通方式策略
     */
    private void  setTacticsIntercity(){
        List<String> list = new ArrayList<>();
        list.add("时间短");
        list.add("出发早");
        list.add("价格低");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mIntercitySpinner.setAdapter(adapter);
        mIntercitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 时间短
                        mMassTransitRoutePlanOption.tacticsIntercity(MassTransitRoutePlanOption.TacticsIntercity.ETRANS_LEAST_TIME);
                        break;
                    case 1:
                        // 出发早
                        mMassTransitRoutePlanOption.tacticsIntercity(MassTransitRoutePlanOption.TacticsIntercity.ETRANS_START_EARLY);
                        break;
                    case 2:
                        // 价格低
                        mMassTransitRoutePlanOption.tacticsIntercity(MassTransitRoutePlanOption.TacticsIntercity.ETRANS_LEAST_PRICE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 设置跨城交通方式策略
     */
    private void  setTransTypeIntercity(){
        List<String> list = new ArrayList<>();
        list.add("火车优先");
        list.add("飞机优先");
        list.add("大巴优先");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mTranstypeSpinner.setAdapter(adapter);
        mTranstypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 火车优先
                        mMassTransitRoutePlanOption.transtypeintercity(MassTransitRoutePlanOption.TransTypeIntercity.ETRANS_TRAIN_FIRST);
                        break;
                    case 1:
                        // 飞机优先
                        mMassTransitRoutePlanOption.transtypeintercity(MassTransitRoutePlanOption.TransTypeIntercity.ETRANS_PLANE_FIRST);
                        break;
                    case 2:
                        // 大巴优先
                        mMassTransitRoutePlanOption.transtypeintercity(MassTransitRoutePlanOption.TransTypeIntercity.ETRANS_COACH_FIRST);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 发起路线规划搜索示例
     */
    public void searchButtonProcess(View v) {
        // 重置浏览节点的路线数据
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBaidumap.clear();

        // 设置起终点信息 起点参数
        PlanNode startNode = PlanNode.withCityNameAndPlaceName(mEditStartCity.getText().toString().trim(), mStrartNodeView.getText().toString().trim());
        // 终点参数
        PlanNode endNode = PlanNode.withCityNameAndPlaceName(mEditEndCity.getText().toString().trim(), mEndNodeView.getText().toString().trim());

        // 发起跨城公共路线检索
        mSearch.masstransitSearch(mMassTransitRoutePlanOption.from(startNode).to(endNode));
    }

    /**
     * 节点浏览
     */
    public void nodeClick(View v) {
        if (null != mMassTransitRouteLine && null != mMassTransitRouteResult) {
            mNodeUtils.browseTransitRouteNode(v,mMassTransitRouteLine,mMassTransitRouteResult);
        }
    }

    /**
     * 切换路线图标，刷新地图使其生效
     * 注意： 起终点图标使用中心对齐.
     */
    public void changeRouteIcon(View v) {
        if (mRouteOverlay == null) {
            return;
        }
        if (mUseDefaultIcon) {
            ((Button) v).setText("自定义起终点图标");
            Toast.makeText(this, "将使用系统起终点图标", Toast.LENGTH_SHORT).show();
        } else {
            ((Button) v).setText("系统起终点图标");
            Toast.makeText(this, "将使用自定义起终点图标", Toast.LENGTH_SHORT).show();
        }
        mUseDefaultIcon = !mUseDefaultIcon;
        mRouteOverlay.removeFromMap();
        mRouteOverlay.addToMap();
    }


    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {

    }

    /**
     * 跨城公共交通路线结果回调
     *
     * @param result 跨城公交线路规划结果
     */
    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
        if (result != null && result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点模糊，获取建议列表
            Toast.makeText(MassTransitRouteDemo.this, "起终点或途经点地址有岐义，通过result.getSuggestAddrInfo()接口获取建议查询信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(MassTransitRouteDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mMassTransitRouteResult = result;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);

            if (!hasShowDialog) {
                // 列表选择
                SelectRouteDialog selectRouteDialog = new SelectRouteDialog(MassTransitRouteDemo.this,
                        result.getRouteLines(), RouteLineAdapter.Type.MASS_TRANSIT_ROUTE);
                mMassTransitRouteResult = result;
                selectRouteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        hasShowDialog = false;
                    }
                });
                selectRouteDialog.setOnItemInDlgClickLinster(new SelectRouteDialog.OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        MyMassTransitRouteOverlay overlay = new MyMassTransitRouteOverlay(mBaidumap);
                        mBaidumap.setOnMarkerClickListener(overlay);
                        mRouteOverlay = overlay;
                        mMassTransitRouteLine = mMassTransitRouteResult.getRouteLines().get(position);
                        overlay.setData(mMassTransitRouteResult.getRouteLines().get(position));
                        // 获取选择的路线
                        MassTransitRouteLine line = mMassTransitRouteResult.getRouteLines().get(position);
                        overlay.setData(line);
                        if (mMassTransitRouteResult.getOrigin().getCityId() == mMassTransitRouteResult.getDestination().getCityId()) {
                            // 同城
                            overlay.setSameCity(true);
                        } else {
                            // 跨城
                            overlay.setSameCity(false);
                        }
                        mBaidumap.clear();
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }

                });

                // 防止多次进入退出，Activity已经释放，但是Dialog仍然弹出，导致的异常释放崩溃
                if (!isFinishing()) {
                    selectRouteDialog.show();
                    hasShowDialog = true;
                }
            }
        }
    }


    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {

    }

    private class MyMassTransitRouteOverlay extends MassTransitRouteOverlay {
        private MyMassTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (mUseDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (mUseDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public void onMapPoiClick(MapPoi poi) {

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
        // 释放检索对象
        if (mSearch != null) {
            mSearch.destroy();
        }
        mBaidumap.clear();
        mMapView.onDestroy();
    }
}
