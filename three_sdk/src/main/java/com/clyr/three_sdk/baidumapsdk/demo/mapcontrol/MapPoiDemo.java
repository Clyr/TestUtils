package com.clyr.three_sdk.baidumapsdk.demo.mapcontrol;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.clyr.three_sdk.R;


/**
 * 控制地图标注显示
 */

public class MapPoiDemo extends AppCompatActivity {

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private CheckBox mShowPoiCB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappoi);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mShowPoiCB =(CheckBox)findViewById(R.id.poi);
    }

    /**
     * 控制是否显示底图默认标注, 默认显示
     */
    public void showMapPoi(View v){

        if(mShowPoiCB.isChecked()){
            mShowPoiCB.setText("开启底图标注");
        }else{
            mShowPoiCB.setText("关闭底图标注");
        }
        // 控制是否显示底图默认标注, 默认显示 true为显示，false为关闭
        mBaiduMap.showMapPoi(mShowPoiCB.isChecked());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }
}
