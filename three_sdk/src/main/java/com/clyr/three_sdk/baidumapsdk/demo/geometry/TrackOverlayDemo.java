package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.track.TraceAnimationListener;
import com.baidu.mapapi.map.track.TraceOptions;
import com.baidu.mapapi.map.track.TraceOverlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;



/**
 * 动态轨迹示例
 */
public class TrackOverlayDemo extends AppCompatActivity implements View.OnClickListener, TraceAnimationListener {

    private MapView mMapView;
    private BaiduMap mBaiDuMap;
    private TraceOverlay mTraceOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_overlay_demo);
        mMapView = findViewById(R.id.bmapView);
        mBaiDuMap = mMapView.getMap();
        Button  addTrace = findViewById(R.id.add_trace);
        Button addTrace2 = findViewById(R.id.add_trace_overlay);
        Button traceClean = findViewById(R.id.trace_clean);
        addTrace.setOnClickListener(this);
        addTrace2.setOnClickListener(this);
        traceClean.setOnClickListener(this);

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
        removeTrace();
        mMapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_trace) {
            if (null != mTraceOverlay) {
                mTraceOverlay.clear(); // 清除轨迹数据，但不会移除轨迹覆盖物
            }
            TraceOptions traceOptions = initTraceOptions();
            // 添加轨迹动画
            mTraceOverlay = mBaiDuMap.addTraceOverlay(traceOptions, this);
            upDataMapStatus();
        } else if (v.getId() == R.id.add_trace_overlay) {
            if (null != mTraceOverlay) {
                mTraceOverlay.clear(); // 清除轨迹数据，但不会移除轨迹覆盖物
            }
            TraceOptions traceOptions = initTraceOptions();
            traceOptions.setTrackMove(true);
            mTraceOverlay = mBaiDuMap.addTraceOverlay(traceOptions, this);
            upDataMapStatus();
        } else if (v.getId() == R.id.trace_clean) {
            removeTrace();
        }
    }

    /**
     * 配置轨迹参数
     */
    private TraceOptions initTraceOptions() {
        TraceOptions traceOptions = new TraceOptions();
        traceOptions.animationTime(5000);
        traceOptions.animate(true);
        traceOptions.animationType(TraceOptions.TraceAnimateType.TraceOverlayAnimationEasingCurveLinear);
        traceOptions.color(0xAAFF0000);
        traceOptions.width(10);
        traceOptions.points(getTraceLocation());
        return traceOptions;
    }

    /**
     * 移除图层
     */
    private void removeTrace() {
        if (null != mTraceOverlay) {
            mTraceOverlay.clear(); // 清除轨迹数据，但不会移除轨迹覆盖物
            mTraceOverlay.remove(); // 移除轨迹覆盖物
        }
    }

    /**
     * 更新地图范围
     */
    private void upDataMapStatus() {
        if (null == mTraceOverlay || null == mBaiDuMap) {
            return;
        }

        LatLngBounds latLngBounds = mTraceOverlay.getLatLngBounds();
        mBaiDuMap.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(latLngBounds));
    }

    /**
     * 获取轨迹点
     *
     * @return 经纬度list
     */
    private List<LatLng> getTraceLocation() {
        ArrayList<LatLng> latLngList = new ArrayList<>();
        for (int i = 0; i < latlngs.length; i++) {
            latLngList.add(latlngs[i]);
        }

        return latLngList;
    }

    /**
     * 模拟轨迹点
     */
    private static final LatLng[] latlngs = new LatLng[]{

            new LatLng(40.055826, 116.307917), new LatLng(40.055916, 116.308455), new LatLng(40.055967, 116.308549),
            new LatLng(40.056014, 116.308574), new LatLng(40.056440, 116.308485), new LatLng(40.056816, 116.308352),
            new LatLng(40.057997, 116.307725), new LatLng(40.058022, 116.307693), new LatLng(40.058029, 116.307590),
            new LatLng(40.057913, 116.307119), new LatLng(40.057850, 116.306945), new LatLng(40.057756, 116.306915),
            new LatLng(40.057225, 116.307164), new LatLng(40.056134, 116.307546), new LatLng(40.055879, 116.307636),
            new LatLng(40.055826, 116.307697),};

    /**
     * @param percent 轨迹动画更新进度 0～100
     */
    @Override
    public void onTraceAnimationUpdate(int percent) {
        Log.e("TAG==", "onTraceAnimationUpdate: " + percent);
    }

    /**
     *
     * @param position 轨迹动画更新的当前位置点
     */
    @Override
    public void onTraceUpdatePosition(LatLng position) {
        Log.e("TAG==", "onTraceUpdatePosition: " + position.toString());

    }

    /**
     * 轨迹动画完成
     */
    @Override
    public void onTraceAnimationFinish() {
        Log.e("TAG==", "onTraceAnimationFinish: ");

    }
}