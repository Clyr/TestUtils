package com.clyr.three_sdk.baidumapsdk.demo.layers;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapLayer;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;


/**
 * 展示定位图层自定义样式
 */
public class LocationCustomDemo extends AppCompatActivity {

    private LocationClient mLocClient;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    // 是否首次定位
    boolean isFirstLoc = true;
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    private Marker mMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationcustom);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        final MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup);
        RadioGroup.OnCheckedChangeListener radioButtonListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.defaulticon) {
                    // 传入null则，恢复默认图标
                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, null));
                }
                if (checkedId == R.id.customicon) {
                    int accuracyCircleFillColor = 0xAAFFFF88;
                    int accuracyCircleStrokeColor = 0xAA00FF00;
                    // 修改为自定义图层
                    BitmapDescriptor currentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(mCurrentMode, true, currentMarker,
                            accuracyCircleFillColor, accuracyCircleStrokeColor));
                    currentMarker.recycle();
                }
            }
        };
        group.setOnCheckedChangeListener(radioButtonListener);
        // 定位初始化
        initLocation();
        // 地图定位图标点击事件监听
        mBaiduMap.setOnMyLocationClickListener(new BaiduMap.OnMyLocationClickListener() {
            @Override
            public boolean onMyLocationClick() {
                Toast.makeText(LocationCustomDemo.this,"点击定位图标", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(LocationCustomDemo.this,"点击Marker图标", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    /**
     * 定位初始化
     */
    public void initLocation(){
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        MyLocationListenner myListener = new MyLocationListenner();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        // 打开gps
        option.setOpenGps(true);
        // 设置坐标类型
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 添加marker
     *
     * @param latLng 经纬度
     */
    public void addMarker(LatLng latLng){
        if (latLng.latitude == 0.0 || latLng.longitude == 0.0){
            return;
        }
        MarkerOptions markerOptionsA = new MarkerOptions().position(latLng).yOffset(30).icon(bitmapA).draggable(true);
        mMarker = (Marker) mBaiduMap.addOverlay(markerOptionsA);
    }

    /**
     * 切换指定图层的顺序
     */
    public void switchLayerOrder(View view){
        if (mBaiduMap == null){
            return;
        }
        mBaiduMap.switchLayerOrder(MapLayer.MAP_LAYER_LOCATION, MapLayer.MAP_LAYER_OVERLAY);
    }

    /**
     * 关闭定位图层点击事件
     */
    public void setLayerClickable(View view){
        if (mBaiduMap == null){
            return;
        }
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()){
            // 设置指定的图层是否可以点击
            mBaiduMap.setLayerClickable(MapLayer.MAP_LAYER_LOCATION,false );
        } else {
            mBaiduMap.setLayerClickable(MapLayer.MAP_LAYER_LOCATION,true );
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // MapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())// 设置定位数据的精度信息，单位：米
                    .direction(location.getDirection()) // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            // 设置定位数据, 只有先允许定位图层后设置数据才会生效
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLng).zoom(20.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                addMarker(latLng);
            }
            if (mMarker != null){
                mMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
            }
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
        bitmapA.recycle();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }
}
