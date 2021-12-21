package com.clyr.three_sdk.baidumapsdk.demo.createmap;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.three_sdk.R;


public class MapTypeFragmentDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_type_fragment);

        getSupportFragmentManager()    //
                .beginTransaction()
                .add(R.id.fragment_container,new MapFragment())   // 此处的R.id.fragment_container是要盛放fragment的父容器
                .commit();
    }
}