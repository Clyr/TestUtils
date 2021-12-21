package com.clyr.three_sdk.baidumapsdk.demo.search;

import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.weather.LanguageType;
import com.baidu.mapapi.search.weather.OnGetWeatherResultListener;
import com.baidu.mapapi.search.weather.WeatherDataType;
import com.baidu.mapapi.search.weather.WeatherResult;
import com.baidu.mapapi.search.weather.WeatherSearch;
import com.baidu.mapapi.search.weather.WeatherSearchOption;
import com.baidu.mapapi.search.weather.WeatherSearchRealTime;
import com.baidu.mapapi.search.weather.WeatherServerType;
import com.clyr.three_sdk.R;


public class WeatherSearchActivity extends AppCompatActivity {

    private WeatherSearch mWeatherSearch;

    private Button mBtnSearch;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private GeoCoder mGeoCoder;
    private TextView mTxtDistrict;

    private static final String TAG = WeatherSearchActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_search);

        mWeatherSearch = WeatherSearch.newInstance();
        mGeoCoder = GeoCoder.newInstance();
        initMapView();
        initView();
    }

    private void initView(){
        mBtnSearch = findViewById(R.id.btn_search_weather);
        mTxtDistrict = findViewById(R.id.districtID);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String districtId = (String)mTxtDistrict.getText();
                WeatherSearchOption weatherSearchOption = new WeatherSearchOption();
                weatherSearchOption
                        .weatherDataType(WeatherDataType.WEATHER_DATA_TYPE_ALL)
                        .districtID(districtId)
                        .languageType(LanguageType.LanguageTypeChinese)
                        .serverType(WeatherServerType.WEATHER_SERVER_TYPE_DEFAULT);
                mWeatherSearch.setWeatherSearchResultListener(new OnGetWeatherResultListener() {
                    @Override
                    public void onGetWeatherResultListener(final WeatherResult weatherResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                popupWeatherDialog(weatherResult);
                            }
                        });
                    }
                });
                mWeatherSearch.request(weatherSearchOption);
            }
        });
    }
    private void initMapView(){
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        final LatLng center = new LatLng(39.90923, 116.447428);
        final MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(center);
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Point point = mBaiduMap.getProjection().toScreenLocation(center);
                BitmapDescriptor bitmapDescriptor =
                        BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
                MarkerOptions markerOptions =
                        new MarkerOptions().icon(bitmapDescriptor).position(center).fixedScreenPosition(point);
                mBaiduMap.addOverlay(markerOptions);
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {
            }
            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {
            }
            @Override
            public void onMapStatusChange(MapStatus status) {
            }
            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                LatLng center = status.target;
                ReverseGeoCodeOption rgcOption =
                        new ReverseGeoCodeOption().location(center).radius(500);
                mGeoCoder.reverseGeoCode(rgcOption);
            }
        });
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener(){
            /**
             * 地理编码查询结果回调函数
             *
             * @param result 地理编码查询结果
             */
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }
            /**
             * 反地理编码查询结果回调函数
             *
             * @param result 反地理编码查询结果
             */
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                int adCode = result.getAdcode();
                mTxtDistrict.setText(String.valueOf(adCode));
            }
        });
    }
    private void popupWeatherDialog(WeatherResult weatherResult){
        if (null == weatherResult) {
            return;
        }
        WeatherSearchRealTime weatherSearchRealTime = weatherResult.getRealTimeWeather();
        if (null == weatherSearchRealTime) {
            return;
        }
        final AlertDialog.Builder weatherDialog =
                new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.layout_weather, null);
        if (null == view) {
            return;
        }
        TextView txtRelativeHumdidity = view.findViewById(R.id.txtRelativeHumidity);
        String relativeHumidity =
                "相对湿度：" + weatherSearchRealTime.getRelativeHumidity() + "%";
        txtRelativeHumdidity.setText(relativeHumidity);
        TextView txtSensoryTemp = view.findViewById(R.id.txtSensoryTemp);
        String sensoryTemp = "体感温度：" + String.valueOf(weatherSearchRealTime.getSensoryTemp()) + "℃";
        txtSensoryTemp.setText(sensoryTemp);
        TextView txtPhenomenon = view.findViewById(R.id.txtPhenomenon);
        String phenomenon = "天气现象："+weatherSearchRealTime.getPhenomenon();
        txtPhenomenon.setText(phenomenon);
        TextView txtWindDirection = view.findViewById(R.id.txtWindDirection);
        String windDirection = "风向：" + weatherSearchRealTime.getWindDirection();
        txtWindDirection.setText(windDirection);
        TextView txtWindPower = view.findViewById(R.id.txtWindPower);
        String windPower = "风力：" + weatherSearchRealTime.getWindPower();
        txtWindPower.setText(windPower);
        TextView txtTemp = view.findViewById(R.id.txtTemp);
        String temp = "温度：" + weatherSearchRealTime.getTemperature() + "℃";
        txtTemp.setText(temp);
        TextView txtUpdateTime = view.findViewById(R.id.txtUpdateTime);
        String updateTime = "更新时间：" + weatherSearchRealTime.getUpdateTime();
        txtUpdateTime.setText(updateTime);
        weatherDialog.setTitle("实时天气").setView(view).create();
        weatherDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时必须调用mMapView. onResume ()
        if (null != mMapView) {
            mMapView.onResume();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时必须调用mMapView. onPause ()
        if(null != mMapView) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (null != mWeatherSearch) {
            mWeatherSearch.destroy();
        }

        if (null != mGeoCoder) {
            mGeoCoder.destroy();
        }

        if (null != mMapView) {
            mMapView.onDestroy();
        }
    }
}
