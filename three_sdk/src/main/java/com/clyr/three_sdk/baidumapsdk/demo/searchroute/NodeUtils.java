package com.clyr.three_sdk.baidumapsdk.demo.searchroute;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.IndoorRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.clyr.three_sdk.R;


public class NodeUtils {

    private Context mContext;
    private BaiduMap mBaiduMap;
    private int nodeIndex = -1; // 节点索引,供浏览节点时使用
    private LatLng mNodeLocation = null;
    private String nodeTitle = null;
    private Object step = null;

    public NodeUtils(Context context, BaiduMap baiduMap) {
        this.mContext = context;
        this.mBaiduMap = baiduMap;
    }

    /**
     * 节点浏览
     */
    public void browseRoutNode(View view, RouteLine route) {
        // 非跨城综合交通
        if (route == null || route.getAllStep() == null) {
            return;
        }
        if (nodeIndex == -1 && view.getId() == R.id.pre) {
            return;
        }
        // 设置节点索引
        if (view.getId() == R.id.next) {
            if (nodeIndex < route.getAllStep().size() - 1) {
                nodeIndex++;
            } else {
                return;
            }
        } else if (view.getId() == R.id.pre) {
            if (nodeIndex > 0) {
                nodeIndex--;
            } else {
                return;
            }
        }
        // 获取节结果信息
        step = route.getAllStep().get(nodeIndex);
        if (step instanceof DrivingRouteLine.DrivingStep) {
            mNodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance().getLocation();
            nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
        } else if (step instanceof WalkingRouteLine.WalkingStep) {
            mNodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance().getLocation();
            nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
        } else if (step instanceof TransitRouteLine.TransitStep) {
            mNodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance().getLocation();
            nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
        } else if (step instanceof BikingRouteLine.BikingStep) {
            mNodeLocation = ((BikingRouteLine.BikingStep) step).getEntrance().getLocation();
            nodeTitle = ((BikingRouteLine.BikingStep) step).getInstructions();
        } else if (step instanceof IndoorRouteLine.IndoorRouteStep) {
            mNodeLocation = ((IndoorRouteLine.IndoorRouteStep) step).getEntrace().getLocation();
            nodeTitle = ((IndoorRouteLine.IndoorRouteStep) step).getInstructions();
        }

        if (mNodeLocation == null || nodeTitle == null) {
            return;
        }
        addInfoWindow();
    }

    /**
     * 跨城公交 node 处理方法
     */
    public void browseTransitRouteNode(View view, MassTransitRouteLine massroute, MassTransitRouteResult nowResultmass) {
        // 跨城综合交通  综合跨城公交的结果判断方式不一样
        if (massroute == null || massroute.getNewSteps() == null) {
            return;
        }
        if (nodeIndex == -1 && view.getId() == R.id.pre) {
            return;
        }
        boolean isSamecity = nowResultmass.getOrigin().getCityId() == nowResultmass.getDestination().getCityId();
        int size = 0;
        if (isSamecity) {
            size = massroute.getNewSteps().size();
        } else {
            for (int i = 0; i < massroute.getNewSteps().size(); i++) {
                size += massroute.getNewSteps().get(i).size();
            }
        }

        // 设置节点索引
        if (view.getId() == R.id.next) {
            if (nodeIndex < size - 1) {
                nodeIndex++;
            } else {
                return;
            }
        } else if (view.getId() == R.id.pre) {
            if (nodeIndex > 0) {
                nodeIndex--;
            } else {
                return;
            }
        }
        if (isSamecity) {
            // 同城
            step = massroute.getNewSteps().get(nodeIndex).get(0);
        } else {
            // 跨城
            int num = 0;
            for (int j = 0; j < massroute.getNewSteps().size(); j++) {
                num += massroute.getNewSteps().get(j).size();
                if (nodeIndex - num < 0) {
                    int k = massroute.getNewSteps().get(j).size() + nodeIndex - num;
                    step = massroute.getNewSteps().get(j).get(k);
                    break;
                }
            }
        }

        mNodeLocation = ((MassTransitRouteLine.TransitStep) step).getStartLocation();
        nodeTitle = ((MassTransitRouteLine.TransitStep) step).getInstructions();
        if (mNodeLocation == null || nodeTitle == null) {
            return;
        }
        addInfoWindow();
    }

    /**
     * 浏览公交线路节点
     */
    public void browseBusRouteNode (View view , BusLineResult busLineResult) {
        if (nodeIndex < -1 || busLineResult == null
                || nodeIndex >= busLineResult.getStations().size()) {
            return;
        }
        // 上一个节点
        if (view.getId() == R.id.pre && nodeIndex > 0) {
            // 索引减
            nodeIndex--;
        }
        // 下一个节点
        if (view.getId() == R.id.next && nodeIndex < (busLineResult.getStations().size() - 1)) {
            // 索引加
            nodeIndex++;
        }
        if (nodeIndex >= 0) {
            // 移动到指定索引的坐标
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(busLineResult.getStations().get(nodeIndex).getLocation()));
            mNodeLocation = busLineResult.getStations().get(nodeIndex).getLocation();
            nodeTitle = busLineResult.getStations().get(nodeIndex).getTitle();
            if (mNodeLocation == null || nodeTitle == null) {
                return;
            }
            addInfoWindow();
        }
    }

    public void addInfoWindow() {
        // 移动节点至中心
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mNodeLocation));
        // show popup
        TextView popupText = new TextView(mContext);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaiduMap.showInfoWindow(new InfoWindow(popupText, mNodeLocation, 0));
    }

}
