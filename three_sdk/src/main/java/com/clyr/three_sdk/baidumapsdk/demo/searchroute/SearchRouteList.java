package com.clyr.three_sdk.baidumapsdk.demo.searchroute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoInfo;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoListAdapter;

public class SearchRouteList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(SearchRouteList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }


    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(SearchRouteList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_title_driving_route, R.string.demo_desc_driving_route, DrivingRouteSearchDemo.class),
            new DemoInfo(R.string.demo_title_walking_route, R.string.demo_desc_biking_route, WalkingRouteSearchDemo.class),
            new DemoInfo(R.string.demo_title_biking_route, R.string.demo_desc_transit_route, BikingRouteSearchDemo.class),
            new DemoInfo(R.string.demo_title_transit_route, R.string.demo_desc_walking_route, TransitRoutePlanDemo.class),
            new DemoInfo(R.string.demo_title_mass_transit_route, R.string.demo_desc_mass_transit_route, MassTransitRouteDemo.class),
            new DemoInfo(R.string.demo_title_indoorroute, R.string.demo_desc_indoorroute, IndoorRouteDemo.class),
            new DemoInfo(R.string.demo_title_bus, R.string.demo_desc_bus, BusLineSearchDemo.class),
    };
}


