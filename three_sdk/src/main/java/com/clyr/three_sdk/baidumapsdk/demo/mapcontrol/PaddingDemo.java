package com.clyr.three_sdk.baidumapsdk.demo.mapcontrol;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.clyr.three_sdk.R;


/**
 * 设置地图上控件与地图边界的距离
 */
public class PaddingDemo extends AppCompatActivity {

    // MapView 是地图主控件
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private  int mPaddingLeft = 0;
    private  int mPaddingTop = 0;
    private  int mPaddingRight = 0;
    private  int mPaddingBottom = 200;

    private EditText mEditTop;
    private EditText mEditBottom;
    private EditText mEditLeft;
    private EditText mEditRight;

    private TextView mBottomView;
    private TextView mRightView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padding);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        mEditTop = (EditText) findViewById(R.id.paddingTop);
        mEditBottom = (EditText) findViewById(R.id.paddingBottom);
        mEditLeft = (EditText) findViewById(R.id.paddingLeft);
        mEditRight = (EditText) findViewById(R.id.paddingRight);
    }

    /**
     * 设置Padding区域
     */
    public void setPadding(View v) {
        try {
            mPaddingLeft = Integer.parseInt(mEditLeft.getText().toString());
            mPaddingTop = Integer.parseInt(mEditTop.getText().toString());
            mPaddingRight = Integer.parseInt(mEditRight.getText().toString());
            mPaddingBottom = Integer.parseInt(mEditBottom.getText().toString());
            // 设置地图上控件与地图边界的距离，包含比例尺、缩放控件、logo、指南针的位置只有在 OnMapLoadedCallback.onMapLoaded() 之后设置才生效
            mBaiduMap.setViewPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            addViewBottom(mMapView);
            addViewRight(mMapView);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入正确padding值", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载子View
     *
     * @param mapView 传入mapView
     */
    private void addViewBottom(MapView mapView) {
        if (mBottomView != null) mMapView.removeView(mBottomView);
        if (mPaddingBottom == 0) return;

        mBottomView = new TextView(this);
        mBottomView.setText(getText(R.string.instruction));
        mBottomView.setTextSize(15.0f);
        mBottomView.setGravity(Gravity.CENTER);
        mBottomView.setTextColor(Color.BLACK);
        mBottomView.setBackgroundColor(Color.parseColor("#AA00FF00"));

        MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
        builder.width(mapView.getWidth());
        builder.height(mPaddingBottom);
        builder.point(new Point(0, mapView.getHeight()));
        builder.align(MapViewLayoutParams.ALIGN_LEFT, MapViewLayoutParams.ALIGN_BOTTOM);

        mapView.addView(mBottomView, builder.build());
    }

    /**
     * 加载子view
     *
     * @param mapView 传入mapView
     */
    private void addViewRight(MapView mapView) {
        if (mRightView != null) mMapView.removeView(mRightView);
        if (mPaddingRight == 0) return;

        mRightView = new TextView(this);
        mRightView.setBackgroundColor(Color.parseColor("#00FF00"));

        MapViewLayoutParams.Builder builder = new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);
        builder.width(mPaddingRight);
        builder.height(mapView.getHeight());
        builder.point(new Point(mapView.getWidth(), mapView.getHeight()));
        builder.align(MapViewLayoutParams.ALIGN_RIGHT, MapViewLayoutParams.ALIGN_BOTTOM);

        mapView.addView(mRightView, builder.build());
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
