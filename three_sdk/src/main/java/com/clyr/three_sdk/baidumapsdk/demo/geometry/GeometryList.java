package com.clyr.three_sdk.baidumapsdk.demo.geometry;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoInfo;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoListAdapter;

public class GeometryList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(GeometryList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(GeometryList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_title_marker, R.string.demo_desc_marker, MarkerDemo.class),
            new DemoInfo(R.string.demo_title_marker_animation, R.string.demo_desc_marker_animation, MarkerAnimationDemo.class),
            new DemoInfo(R.string.demo_title_cluster, R.string.demo_desc_cluster, MarkerClusterDemo.class),
            new DemoInfo(R.string.demo_title_polyline, R.string.demo_desc_polyline, PolylineDemo.class),
            new DemoInfo(R.string.demo_title_polygon, R.string.demo_desc_polygon, PolygonDemo.class),
            new DemoInfo(R.string.demo_title_circle, R.string.demo_desc_circle, CircleDemo.class),
            new DemoInfo(R.string.demo_title_arc, R.string.demo_desc_arc, ArcDemo.class),
            new DemoInfo(R.string.demo_title_groundoverlay, R.string.demo_desc_groundoverlay, GroundOverlayDemo.class),
            new DemoInfo(R.string.demo_title_tile, R.string.demo_desc_tile, TileOverlayDemo.class),
            new DemoInfo(R.string.demo_title_heat, R.string.demo_desc_heat, HeatMapDemo.class),
            new DemoInfo(R.string.demo_title_opengl, R.string.demo_desc_opengl, OpenglDemo.class),
            new DemoInfo(R.string.demo_title_track_show, R.string.demo_desc_track_show, TrackShowDemo.class),
            new DemoInfo(R.string.demo_title_Prism_show, R.string.demo_desc_Prism_show, Building3DPrismDemo.class),
            new DemoInfo(R.string.demo_title_multi_point_show, R.string.demo_desc_multi_point_show, MultiPointOverlayDemo.class),
            new DemoInfo(R.string.demo_title_track_overlay, R.string.demo_desc_track_overlay, TrackOverlayDemo.class),
            new DemoInfo(R.string.demo_title_3d_modle_show, R.string.demo_desc_3d_modle_show, BM3DModelDemo.class)
    };
}

