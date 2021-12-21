package com.clyr.three_sdk.baidumapsdk.demo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.poi.BaiduMapPoiSearch;
import com.baidu.mapapi.utils.poi.PoiParaOption;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption.EBusStrategyType;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 此Demo用来说明调起百度地图功能
 */
public class OpenBaiduMap extends Activity {

    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;
    private RadioGroup.OnCheckedChangeListener mRadioButtonListener;
    private CoordType mCoordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_baidumap);
        TextView openInfoTextView = (TextView) findViewById(R.id.open_Info);
        openInfoTextView.setTextColor(Color.RED);
        openInfoTextView.setText("当手机没有安装百度地图客户端或版本过低时，默认调起百度地图webApp\n" +
                "支持国测局坐标和百度坐标两种坐标调起，您可以通过SDKInitializer.setCoordType()方法来设置您使用的坐标类型");
        ListView mListView = (ListView) findViewById(R.id.listView_openBaiduMap);
        mListView.setAdapter(new OpenBaiduMapListAdapter(getData()));
        mCoordType = SDKInitializer.getCoordType();//获取全局设置的坐标类型
        SDKInitializer.setCoordType(CoordType.BD09LL);
        RadioGroup group = (RadioGroup) findViewById(R.id.RadioGroup);
        mRadioButtonListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bd) {
                    SDKInitializer.setCoordType(CoordType.BD09LL);//设置坐标为百度坐标
                }
                if (checkedId == R.id.gc) {
                    SDKInitializer.setCoordType(CoordType.GCJ02);//设置坐标类型为国测局坐标
                }
            }
        };
        group.setOnCheckedChangeListener(mRadioButtonListener);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        startNavi();
                        break;

                    case 1:
                        startWebNavi();
                        break;

                    case 2:
                        startPoiNearbySearch();
                        break;

                    case 3:
                        startPoiDetails();
                        break;

                    case 4:
                        startRoutePlanWalking();
                        break;

                    case 5:
                        startRoutePlanDriving();
                        break;

                    case 6:
                        startRoutePlanTransit();
                        break;
                    case 7:
                        startWalkingNavi();
                        break;

                    case 8:
                        startBikingNavi();
                        break;
                    case 9:
                        startPoiPanoShow();
                        break;
                    case 10:
                        startWalkingNaviAR();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("启动百度地图导航(Native)");
        data.add("启动百度地图导航(Web)");
        data.add("启动百度地图Poi周边检索");
        data.add("启动百度地图Poi详情页面");
        data.add("启动百度地图步行路线规划");
        data.add("启动百度地图驾车路线规划");
        data.add("启动百度地图公交路线规划");
        data.add("启动百度地图步行导航");
        data.add("启动百度地图骑行导航");
        data.add("启动百度地图全景");
        data.add("启动百度地图步行AR导航");
        return data;
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");

        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图导航(Web)
     */
    public void startWebNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2);

        BaiduMapNavigation.openWebBaiduMapNavi(para, this);
    }

    /**
     * 启动百度地图步行导航(Native)
     */
    public void startWalkingNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");

        try {
            BaiduMapNavigation.openBaiduMapWalkNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图步行AR导航(Native)
     */
    public void startWalkingNaviAR() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");
        try {
            BaiduMapNavigation.openBaiduMapWalkNaviAR(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图骑行导航(Native)
     */
    public void startBikingNavi() {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(mLat2, mLon2);

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName("天安门").endName("百度大厦");

        try {
            BaiduMapNavigation.openBaiduMapBikeNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图Poi周边检索
     */
    public void startPoiNearbySearch() {
        LatLng ptCenter = new LatLng(mLat1, mLon1); // 天安门
        PoiParaOption poiParaOption = new PoiParaOption()
                .key("西单")
                .center(ptCenter)
                .radius(2000);

        try {
            BaiduMapPoiSearch.openBaiduMapPoiNearbySearch(poiParaOption, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图Poi详情页面
     */
    public void startPoiDetails() {
        PoiParaOption poiParaOption = new PoiParaOption().uid("65e1ee886c885190f60e77ff"); // 天安门

        try {
            BaiduMapPoiSearch.openBaiduMapPoiDetialsPage(poiParaOption, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图POI全景页面
     */
    public void startPoiPanoShow() {
        try {
            BaiduMapPoiSearch.openBaiduMapPanoShow("65e1ee886c885190f60e77ff", this); // 天安门
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图步行路线规划
     */
    public void startRoutePlanWalking() {
        LatLng ptStart = new LatLng(34.264642646862, 108.95108518068);

        // 构建 route搜索参数
        RouteParaOption routeParaOption = new RouteParaOption()
                .startPoint(ptStart)
                .endName("大雁塔")
                .cityName("西安");
        try {
            BaiduMapRoutePlan.openBaiduMapWalkingRoute(routeParaOption, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图驾车路线规划
     */
    public void startRoutePlanDriving() {
        LatLng ptStart = new LatLng(34.264642646862, 108.95108518068);

        // 构建 route搜索参数
        RouteParaOption routeParaOption = new RouteParaOption()
                .startPoint(ptStart)
                .endName("大雁塔")
                .cityName("西安");

        try {
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(routeParaOption, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }
    }

    /**
     * 启动百度地图公交路线规划
     */
    public void startRoutePlanTransit() {
        LatLng ptStart = new LatLng(mLat1, mLon1);

        // 构建 route搜索参数
        RouteParaOption routeParaOption = new RouteParaOption()
                .startName("天安门")
                .endPoint(ptStart)
                .busStrategyType(EBusStrategyType.bus_recommend_way);

        try {
            BaiduMapRoutePlan.openBaiduMapTransitRoute(routeParaOption, this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaiduMapNavigation.finish(this);
        BaiduMapRoutePlan.finish(this);
        BaiduMapPoiSearch.finish(this);
        // 设置为之前的坐标类型，保证此activity不对其他有影响。
        SDKInitializer.setCoordType(mCoordType);
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(OpenBaiduMap.this);
            }
        });

        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    private class OpenBaiduMapListAdapter extends BaseAdapter {
        List<String> list;

        private OpenBaiduMapListAdapter(List<String> list) {
            super();
            this.list = list;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            if (null == convertView){
                convertView = View.inflate(OpenBaiduMap.this,
                        R.layout.open_map_info_item, null);
            }

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            desc.setVisibility(View.INVISIBLE);
            title.setText(list.get(index));

            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int index) {
            return list.get(index);
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }
}
