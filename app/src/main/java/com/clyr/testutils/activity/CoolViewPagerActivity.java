package com.clyr.testutils.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.clyr.testutils.R;
import com.clyr.testutils.adapter.FragmentAdapter;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.testutils.fragment.coolviewpager.ImageVScrollFragment;
import com.clyr.testutils.fragment.coolviewpager.MyInfoFragment;
import com.clyr.testutils.fragment.coolviewpager.VScrollFragment;

import java.util.ArrayList;
import java.util.List;

public class CoolViewPagerActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_view_pager);
    }

    @Override
    protected void initView() {
        ViewPager view_pager = findViewById(R.id.view_pager);
        List<Fragment> list = new ArrayList<>();
        list.add(new ImageVScrollFragment());
        list.add(new MyInfoFragment());
        list.add(new VScrollFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), list);
        view_pager.setAdapter(fragmentAdapter);
    }


}