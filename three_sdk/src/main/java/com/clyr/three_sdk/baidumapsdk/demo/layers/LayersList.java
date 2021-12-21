package com.clyr.three_sdk.baidumapsdk.demo.layers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoInfo;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoListAdapter;

public class LayersList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(LayersList.this,DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_title_layertraffic, R.string.demo_desc_layertraffic, LayerTrafficAndHeatMapDemo.class),
            new DemoInfo(R.string.demo_title_layerbuilding, R.string.demo_desc_layerbuilding, LayerBuildingDemo.class),
            new DemoInfo(R.string.demo_title_locationtype, R.string.demo_desc_locationtype, LocationTypeDemo.class),
            new DemoInfo(R.string.demo_title_locationcustom, R.string.demo_desc_locationcustom, LocationCustomDemo.class)
    };
}