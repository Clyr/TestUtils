package com.clyr.three_sdk.baidumapsdk.demo.mapcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoInfo;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoListAdapter;

public class  MapControlList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(MapControlList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(MapControlList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
             new DemoInfo(R.string.demo_title_gesture, R.string.demo_desc_gesture, UISettingDemo.class),
             new DemoInfo(R.string.demo_title_logosetting, R.string.demo_desc_logosetting, ViewSettingDemo.class),
             new DemoInfo(R.string.demo_title_mapstatus, R.string.demo_desc_mapstatus, MapStatusDemo.class),
             new DemoInfo(R.string.demo_title_mapclick, R.string.demo_desc_mapclick,MapClickDemo.class),
             new DemoInfo(R.string.demo_title_padding, R.string.demo_desc_padding, PaddingDemo.class),
             new DemoInfo( R.string.demo_title_mapbounds, R.string.demo_desc_mapbounds, MapBoundsDemo.class),
             new DemoInfo(R.string.demo_title_limitbounds, R.string.demo_desc_limitbounds,MapLimitBoundsDemo.class),
             new DemoInfo(R.string.demo_title_mappoi, R.string.demo_desc_mappoi, MapPoiDemo.class),
             new DemoInfo(R.string.demo_title_snapshot, R.string.demo_desc_snapshot, SnapShotDemo.class)
    };
}


