package com.clyr.three_sdk.baidumapsdk.demo.util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.three_sdk.R;


public class UtilsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(UtilsList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(UtilsList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_title_open_baidumap, R.string.demo_desc_open_baidumap, OpenBaiduMap.class),
            new DemoInfo(R.string.demo_title_favorite, R.string.demo_desc_favorite, FavoriteDemo.class),
            new DemoInfo(R.string.demo_title_distance, R.string.demo_desc_distance, DistanceUtilDemo.class),
            new DemoInfo(R.string.demo_title_contains, R.string.demo_desc_contains, SpatialRelationDemo.class),
            new DemoInfo(R.string.demo_title_share,R.string.demo_desc_share, ShareDemo.class),
//            new DemoInfo(R.string.demo_title_custom_map_preview, R.string.demo_desc_custom_map_preview, CustomMapPreview.class),
    };

}
