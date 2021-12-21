package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



/**
 * 热力图功能demo 使用用户自定义热力图数据
 */
public class HeatMapDemo extends AppCompatActivity {
    @SuppressWarnings("unused")
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private HeatMap mHeatmap;
    private Button mAddHeatMapBtn;
    private Button mRemoveBtn;
    private boolean isDestroy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);
        mMapView = (MapView) findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(5));
        mAddHeatMapBtn = (Button) findViewById(R.id.add);
        mRemoveBtn = (Button) findViewById(R.id.remove);
        mAddHeatMapBtn.setEnabled(false);
        mRemoveBtn.setEnabled(false);
        mAddHeatMapBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                addHeatMap();
            }
        });
        mRemoveBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mHeatmap.removeHeatMap();
                mAddHeatMapBtn.setEnabled(true);
                mRemoveBtn.setEnabled(false);
            }
        });
        addHeatMap();
    }

    private void addHeatMap() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!isDestroy) {
                    mBaiduMap.addHeatMap(mHeatmap);
                }
                mAddHeatMapBtn.setEnabled(false);
                mRemoveBtn.setEnabled(true);
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<LatLng> data = getLocations();
                mHeatmap = new HeatMap.Builder().data(data).build();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMap.clear();
        isDestroy = true;
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
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
