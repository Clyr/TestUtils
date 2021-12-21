package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MultiPoint;
import com.baidu.mapapi.map.MultiPointItem;
import com.baidu.mapapi.map.MultiPointOption;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class MultiPointOverlayDemo extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.marker_blue);
    private BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    private Marker mMarker;
    private MultiPoint mMultiPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_point_demo);
        mMapView = findViewById(R.id.bmapView);
        Button addMultiPoint = findViewById(R.id.add_multi_point);
        Button removeMultiPoint = findViewById(R.id.remove_multi_point);
        addMultiPoint.setOnClickListener(this);
        removeMultiPoint.setOnClickListener(this);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMultiPointClickListener(new BaiduMap.OnMultiPointClickListener() {
            @Override
            public boolean onMultiPointClick(MultiPoint multiPoint, MultiPointItem multiPointItem) {
                if (mMarker != null) {
                    mMarker.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(multiPointItem.getPoint());
                markerOptions.icon(bitmap);
                mMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        bitmap.recycle();
        bitmapA.recycle();
        if (null != mMultiPoint) {
            mMultiPoint.remove();
        }
        if (null != mMarker) {
            mMarker.remove();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_multi_point) {
            List<LatLng> locations = getLocations();
            ArrayList<MultiPointItem> multiPointItems = new ArrayList<>();
            for (int i = 0; i < locations.size(); i++) {
                MultiPointItem multiPointItem = new MultiPointItem(locations.get(i));
                multiPointItems.add(multiPointItem);
            }
            MultiPointOption multiPointOption = new MultiPointOption();
            multiPointOption.setMultiPointItems(multiPointItems);
            multiPointOption.setIcon(bitmapA);
            mMultiPoint = (MultiPoint) mBaiduMap.addOverlay(multiPointOption);
        } else if (v.getId() == R.id.remove_multi_point) {
            if (null != mMultiPoint) {
                mMultiPoint.remove();
            }
            if (mMarker != null) {
                mMarker.remove();
            }
        }
    }

    private List<LatLng> getLocations() {
        List<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(R.raw.locations);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array;
        try {
            array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                double lat = object.getDouble("lat");
                double lng = object.getDouble("lng");
                list.add(new LatLng(lat, lng));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}