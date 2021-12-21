package com.clyr.three_sdk.baidumapsdk.demo.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoInfo;
import com.clyr.three_sdk.baidumapsdk.demo.util.DemoListAdapter;

public class SearchList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menulist);
        ListView demoList = (ListView) findViewById(R.id.mapList);
        // 添加ListItem，设置事件响应
        demoList.setAdapter(new DemoListAdapter(SearchList.this, DEMOS));
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(SearchList.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_title_poicitysearch, R.string.demo_desc_poicitysearch, PoiCitySearchDemo.class),
            new DemoInfo(R.string.demo_title_poinearbysearch, R.string.demo_desc_poinearbysearch, PoiNearbySearchDemo.class),
            new DemoInfo(R.string.demo_title_poiboundsearch, R.string.demo_desc_poiboundsearch, PoiBoundSearchDemo.class),
            new DemoInfo(R.string.demo_title_poisugsearch, R.string.demo_desc_poisugsearch, PoiSugSearchDemo.class),
            new DemoInfo(R.string.demo_title_poidetailsearch, R.string.demo_desc_poidetailsearch, PoiDetailSearchDemo.class),
            new DemoInfo(R.string.demo_title_districsearch, R.string.demo_desc_districsearch, DistrictSearchDemo.class),
            new DemoInfo(R.string.demo_title_geocode, R.string.demo_desc_geocode, GeoCoderDemo.class),
            new DemoInfo(R.string.demo_title_regeocode, R.string.demo_desc_regeocode, ReverseGeoCodeDemo.class),
            new DemoInfo(R.string.demo_title_recommendstop, R.string.demo_desc_recommendstop,
                    RecommendStopDemo.class),
            new DemoInfo(R.string.demo_title_indoorsearch, R.string.demo_desc_indoorsearch, IndoorSearchDemo.class),
            new DemoInfo(R.string.demo_title_weather, R.string.demo_desc_weather,
                    WeatherSearchActivity.class)
    };
}

