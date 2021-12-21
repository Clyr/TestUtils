package com.clyr.three_sdk.baidumapsdk.demo.mapcontrol;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapView;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



public class ViewSettingDemo extends AppCompatActivity implements BaiduMap.OnMapLoadedCallback {

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Spinner mSpinner;
    private EditText mEditPointx;
    private EditText mEditPointy;
    private EditText mZoomPointx;
    private EditText mZoomPointy;
    private Button mScaleControlBtn;
    private Button mZoomControlBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsetting);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mEditPointx = (EditText) findViewById(R.id.pointx);
        mEditPointy = (EditText) findViewById(R.id.pointy);
        mZoomPointx = (EditText) findViewById(R.id.zoompointx);
        mZoomPointy = (EditText) findViewById(R.id.zoompointy);
        mScaleControlBtn = (Button) findViewById(R.id.scalecontrol_btn);
        mZoomControlBtn = (Button) findViewById(R.id.zoomcontrol_btn);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        list.add("中上");
        list.add("中下");
        list.add("左上");
        list.add("左下");
        list.add("右上");
        list.add("右下");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_vict, list);
        adapter.setDropDownViewResource(R.layout.spinner_item_vict);
        mSpinner.setAdapter(adapter);
        initViewListener();
    }

    /**
     * 初始化控件监听
     */
    public void initViewListener() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        mMapView.setLogoPosition(LogoPosition.logoPostionCenterTop);
                        break;
                    case 1:
                        mMapView.setLogoPosition(LogoPosition.logoPostionCenterBottom);
                        break;
                    case 2:
                        mMapView.setLogoPosition(LogoPosition.logoPostionleftTop);
                        break;
                    case 3:
                        mMapView.setLogoPosition(LogoPosition.logoPostionleftBottom);
                        break;
                    case 4:
                        mMapView.setLogoPosition(LogoPosition.logoPostionRightTop);
                        break;
                    case 5:
                        mMapView.setLogoPosition(LogoPosition.logoPostionRightBottom);
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
     * 地图加载完成回调函数
     */
    @Override
    public void onMapLoaded() {
        mScaleControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pointx = mEditPointx.getText().toString().trim();
                String pointy = mEditPointy.getText().toString().trim();
                if (pointx.isEmpty() || pointy.isEmpty())return;
                int scalePointx = Integer.parseInt(pointx);
                int scalePointy = Integer.parseInt(pointy);
                if (scalePointx < mMapView.getWidth() && scalePointy < mMapView.getHeight()){
                    // 设置比例尺控件的位置,必须在地图加载完成之后
                    mMapView.setScaleControlPosition(new Point(scalePointx, scalePointy));
                }else {
                    Toast.makeText(ViewSettingDemo.this, "请输入正确的值", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mZoomControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pointxStr = mZoomPointx.getText().toString().trim();
                String pointyStr = mZoomPointy.getText().toString().trim();
                if (pointxStr.isEmpty() || pointyStr.isEmpty())return;
                int zoomPointx = Integer.parseInt(pointxStr);
                int zoomPointy = Integer.parseInt(pointyStr);
                if (zoomPointx < mMapView.getWidth() && zoomPointy < mMapView.getHeight()) {
                    //设置缩放控件的位置，必须在地图加载完成之后
                    mMapView.setZoomControlsPosition(new Point(zoomPointx, zoomPointy));
                }else {
                    Toast.makeText(ViewSettingDemo.this, "请输入正确的值", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    /**
     * 设置是否显示比例尺
     */
    public void showScaleControl(View v) {
        boolean checked = ((CheckBox) v).isChecked();
        mMapView.showScaleControl(checked);
    }

    /**
     * 设置是否显示缩放控件
     */
    public void showZoomControl(View v) {
        boolean checked = ((CheckBox) v).isChecked();
        mMapView.showZoomControls(checked);
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
