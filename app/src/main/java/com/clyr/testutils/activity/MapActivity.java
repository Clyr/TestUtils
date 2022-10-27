package com.clyr.testutils.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.testutils.utils.GeocodingService;
import com.clyr.three_sdk.baidumapsdk.WebViewForBaiduMap;
import com.clyr.three_sdk.baidumapsdk.demo.BMapApiDemoMain;
import com.clyr.utils.GsonUtil;
import com.clyr.utils.MyLog;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.GeocoderService;
import com.huawei.hms.location.GetFromLocationRequest;
import com.huawei.hms.location.LocationAvailability;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_LOCATION_SERVER = 100;
    private static final int PERMISSION_LOCATION_CODE = 1001;

    String[] locationPermission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    private static final String TAG = MapActivity.class.getSimpleName();

    TextView logTextview;

    // 声明fusedLocationProviderClient对象
    //FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void initView() {
        initBar();
        logTextview = findViewById(R.id.log_textview);
        findViewById(R.id.baidumap).setOnClickListener(v -> startActivity(BMapApiDemoMain.class));
        findViewById(R.id.baidumap_webview).setOnClickListener(v -> startActivity(WebViewForBaiduMap.class));
        findViewById(R.id.tianmap).setOnClickListener(this::mapGetInfo);
        findViewById(R.id.huaweiMap).setOnClickListener(this::huaweiMap);
    }

    private void mapGetInfo(View view) {
        if (EasyPermissions.hasPermissions(this, locationPermission)) {
            if (!isLocationProviderEnabled()) {
                openLocationServer();
            } else {
                getLocationInfo();
            }
        } else {
            EasyPermissions.requestPermissions(this, "", PERMISSION_LOCATION_CODE, locationPermission);
        }
    }

    private void getLocationInfo() {
        logTextview.setText("");
        initLocationListener();
        Location lastLocation = getLastLocation();
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();
            MyLog.logeArr("latitude = ", latitude + "", "longitude = ", longitude + "");
            getAddress(latitude, longitude);
            getTianMapGetInfo(latitude, longitude);
        }
    }


    /**
     * 判断是否开启了GPS或网络定位开关
     */
    public boolean isLocationProviderEnabled() {
        boolean result = false;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            return false;
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            result = true;
        }
        return result;
    }

    /**
     * 跳转到设置界面，引导用户开启定位服务
     */
    private void openLocationServer() {

        new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage("此功能需要访问位置权限，请在设置中修改权限，以便正常使用功能")
                .setPositiveButton("去设置", (dialog, which) -> {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(i, REQUEST_CODE_LOCATION_SERVER);
                }).setNegativeButton("取消", null).show();

    }

    private final LocationListener mLocationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged");
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled");

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled");

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, String.format("location: longitude: %f, latitude: %f", location.getLongitude(),
                    location.getLatitude()));
            //更新位置信息
        }
    };

    /**
     * 监听位置变化
     */
    @SuppressLint("MissingPermission")
    private void initLocationListener() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            return;
        }
        try {
            locationManager
                    .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, mLocationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            locationManager
                    .requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, mLocationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(() -> {
            locationManager.removeUpdates(mLocationListener);
        }, 2000);


    }

    private Location getLastLocation() {
        Location location = null;

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            return null;
        }
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {

            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                // Found best last known location: %s", l);
                location = l;
            }
        }
        return location;

    }

    /**
     * 获取地址
     *
     * @param latitude  纬度
     * @param longitude 经度
     */
    @SuppressLint("SetTextI18n")
    public void getAddress(double latitude, double longitude) {
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null) {
            for (Address address : addressList) {
                Log.d(TAG, String.format("address: %s", address.toString()));
                MyLog.logeArr(address.getAdminArea(), address.getLocality());
                logTextview.setText(logTextview.getText() + "\n" + address.getAdminArea() + "," + address.getLocality());
            }
        }
    }

    private void getTianMapGetInfo(double latitude, double longitude) {
        //http://api.tianditu.gov.cn/geocoder
        // ?postStr={'lon':119.107254,'lat':36.710426,'ver':1}&type=geocode&tk=02a91d1c37dcf7209d4398e7b814269d
        Map<String, String> mapData = new HashMap<>();
        Map<String, String> locationMap = new HashMap<>();
        locationMap.put("lon", longitude + "");
        locationMap.put("lat", latitude + "");
        locationMap.put("ver", "1");
        mapData.put("postStr", GsonUtil.toJson(locationMap));
        mapData.put("type", "geocode");
        mapData.put("tk", "02a91d1c37dcf7209d4398e7b814269d");


        OkHttpUtils.get().url("http://api.tianditu.gov.cn/geocoder").params(mapData).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response, int id) {
                MyLog.logeArr(response);
                logTextview.setText(logTextview.getText() + "\n" + response);
            }
        });


        /*POST https://siteapi.cloud.huawei.com/mapApi/v1/siteService/reverseGeocode?key=API KEY   HTTP/1.1
        Content-Type: application/json
        Accept: application/json
        {
            "location": {
            "lng": 10.252502,
                    "lat": 43.8739168
        },
            "language": "en",
                "radius": 10
        }*/

        String KEY = "DAEDAFXqHAvwkCeCwpIteg6298jO1fr6H3dcBTA5onnIc2LHQChT9cliDQuYsy+2WpW5klUv4U8bsTr1fLRbRQLOrjqQtYgdnTr5fA==";


        try {
            GeocodingService.reverseGeocoding(KEY, latitude, longitude);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    private void huaweiMap(View view) {

        /*logTextview.setText("");
        // Android SDK<=28 所需权限动态申请
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "android sdk <= 28 Q");
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings =
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 1);
            }
        } else {
            // Android SDK>28 所需权限动态申请，需添加“android.permission.ACCESS_BACKGROUND_LOCATION”权限
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"};
                ActivityCompat.requestPermissions(this, strings, 2);
            }
        }


        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        LocationRequest mLocationRequest = new LocationRequest();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
// 检查设备定位设置
        settingsClient.checkLocationSettings(locationSettingsRequest)
                // 检查设备定位设置接口调用成功监听
                .addOnSuccessListener(locationSettingsResponse -> {
                    LocationSettingsStates locationSettingsStates =
                            locationSettingsResponse.getLocationSettingsStates();
                    StringBuilder stringBuilder = new StringBuilder();
                    // 定位开关是否打开
                    stringBuilder.append(",\nisLocationUsable=")
                            .append(locationSettingsStates.isLocationUsable());
                    // HMS Core是否可用
                    stringBuilder.append(",\nisHMSLocationUsable=")
                            .append(locationSettingsStates.isHMSLocationUsable());
                    Log.i(TAG, "checkLocationSetting onComplete:" + stringBuilder.toString());

                    //logTextview.setText(stringBuilder.toString());
                })
                // 检查设备定位设置接口失败监听回调
                .addOnFailureListener(e -> Log.i(TAG, "checkLocationSetting onFailure:" + e.getMessage()));


// 设置定位类型
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
// 设置回调次数为1
        mLocationRequest.setNumUpdates(1);

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    // TODO: 处理位置回调结果
                    MyLog.logeArr(locationResult.toString());
                    logTextview.setText(locationResult.toString());
                }
            }
        };


        *//*fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // TODO: 接口调用成功的处理
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // TODO: 接口调用失败的处理
                    }
                });

        // 获取最后的已知位置
        Task<Location> task = fusedLocationProviderClient.getLastLocation()
                // 获取最后的已知位置成功监听回调
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            return;
                        }
                        // TODO：成功时Location对象处理逻辑
                    }
                })
                // 获取最后的已知位置失败监听回调
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // TODO：接口调用失败处理
                    }
                });*//*

         *//*fusedLocationProviderClient.disableBackgroundLocation();
        // 注意：停止位置更新时，mLocationCallback必须与requestLocationUpdates方法中的LocationCallback参数为同一对象。
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                // 停止位置更新成功监听回调
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // TODO: 停止位置更新成功的处理
                    }
                })
                // 停止位置更新失败监听回调
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // TODO：停止位置更新失败的处理
                    }
                });*/


        Locale locale = new Locale("zh", "CN");
        GeocoderService geocoderService =
                LocationServices.getGeocoderService(MapActivity.this, locale);
        // 获取逆地理编码请求
        GetFromLocationRequest getFromLocationRequest = new GetFromLocationRequest(36.694504444808175, 119.12488360351868, 5);
        // 发起逆地理编码
        geocoderService.getFromLocation(getFromLocationRequest)
                .addOnSuccessListener(hwLocation -> {
                    // TODO:接口调用成功的处理
                    MyLog.logeArr("onLocationResult = ", hwLocation.get(0).getCity());
                    logTextview.setText("onLocationResult = " + GsonUtil.toJson(hwLocation));
                })
                .addOnFailureListener(e -> {
                    // TODO:接口调用失败的处理
                    MyLog.logeArr(e.getMessage());
                });

        hwMap();
    }

    private void hwMap() {
        LocationCallback mLocationCallback = null;
        LocationRequest mLocationRequest;
        FusedLocationProviderClient mFusedLocationProviderClient;
// 获取FusedLocationProviderClient实例
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = new LocationRequest();
// 设置位置回调的时间间隔为6000ms，默认是5000ms。
        mLocationRequest.setInterval(6000);
// 设置定位类型，PRIORITY_HIGH_ACCURACY为融合定位模式。
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
// 设置回调次数为100，默认值为Integer.MAX_VALUE；。
// 特殊情况：单次定位场景，只需设置回调次数为1，仅会发起一次定位，定位结束之后，也不需要移除定位请求。
        mLocationRequest.setNumUpdates(1);
// 设置返回坐标的类型COORDINATE_TYPE_WGS84代表返回84坐标，COORDINATE_TYPE_GCJ02代表返回02坐标，默认COORDINATE_TYPE_WGS84
        mLocationRequest.setCoordinateType(LocationRequest.COORDINATE_TYPE_WGS84);
        if (null == mLocationCallback) {
            MyLog.logeArr("11111111111111111111111111");
            // 位置回调监听
            LocationCallback finalMLocationCallback = mLocationCallback;
            mLocationCallback = new LocationCallback() {
                @Override
                public String getUuid() {
                    return super.getUuid();
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    super.onLocationAvailability(locationAvailability);
                    MyLog.logeArr("444444444444444");
                }

                @Override
                public void onLocationResult(LocationResult locationResult) {

                    MyLog.logeArr("222222222222222222222222222222");
                    if (locationResult != null) {
                        List<Location> locations = locationResult.getLocations();
                        // TODO: 回调位置处理
                        MyLog.logeArr("onLocationResult = ", locations.toString());
                        logTextview.setText("onLocationResult = " + locations.toString());

                        String KEY = "DAEDAFXqHAvwkCeCwpIteg6298jO1fr6H3dcBTA5onnIc2LHQChT9cliDQuYsy+2WpW5klUv4U8bsTr1fLRbRQLOrjqQtYgdnTr5fA==";

                        try {
                            GeocodingService.reverseGeocoding(KEY, locations.get(0).getLatitude(), locations.get(0).getLongitude());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        Locale locale = new Locale("zh", "CN");
                        GeocoderService geocoderService =
                                LocationServices.getGeocoderService(MapActivity.this, locale);
                        // 获取逆地理编码请求
                        GetFromLocationRequest getFromLocationRequest = new GetFromLocationRequest(locations.get(0).getLatitude(), locations.get(0).getLongitude(), 5);
                        // 发起逆地理编码
                        geocoderService.getFromLocation(getFromLocationRequest)
                                .addOnSuccessListener(hwLocation -> {
                                    // TODO:接口调用成功的处理
                                    MyLog.logeArr("onLocationResult = ", hwLocation.get(0).getCity());
                                    logTextview.setText("onLocationResult = " + GsonUtil.toJson(hwLocation));

                                })
                                .addOnFailureListener(e -> {
                                    // TODO:接口调用失败的处理
                                    MyLog.logeArr(e.getMessage());
                                });

                        MyLog.logeArr("333333333333333333333333333333333");
                    }

                    mFusedLocationProviderClient.removeLocationUpdates(finalMLocationCallback)
                            // 停止位置更新成功监听回调
                            .addOnSuccessListener(aVoid -> {
                                // TODO: 停止位置更新成功的处理
                            })
                            // 停止位置更新失败监听回调
                            .addOnFailureListener(e -> {
                                // TODO：停止位置更新失败的处理
                            });
                }
            };
        }
        // 发起定位
        mFusedLocationProviderClient
                .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_LOCATION_SERVER == requestCode && resultCode == RESULT_OK) {
            getLocationInfo();
        }
    }

    /**
     * 在权限授予
     *
     * @param requestCode 请求代码
     * @param perms       烫发
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 在权限否认
     *
     * @param requestCode 请求代码
     * @param perms       烫发
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
