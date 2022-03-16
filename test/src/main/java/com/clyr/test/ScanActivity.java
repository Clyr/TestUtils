package com.clyr.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.clyr.test.view.MyViewPager;
import com.clyr.test.view.ScanPhotoAdapter;
import com.clyr.utils.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heiguang on 2018/4/14.
 */

public class ScanActivity extends Activity {
    private static final String IMAGES = "images";
    private static final String INDEX = "index";

    public static void show(Context context, List<String> images, int index) {
        Intent intent = new Intent(context, ScanActivity.class);
        intent.putExtra(IMAGES, GsonUtil.toJson(images));
        intent.putExtra(INDEX, index);
        context.startActivity(intent);
    }

    List<String> images;
    int index;

    TextView titleTv;
    MyViewPager photoVp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        hideBottomUIMenu();

        images = GsonUtil.fromJson(getIntent().getStringExtra(IMAGES), new TypeToken<ArrayList<String>>() {
        }.getType());
        index = getIntent().getIntExtra(INDEX, 0);

        initViews();

        addListener();
    }

    protected void initViews() {
        titleTv = (TextView) findViewById(R.id.tv_title);
        photoVp = (MyViewPager) findViewById(R.id.vp_photo);

        titleTv.setText((index + 1) + "/" + images.size());

        photoVp.setAdapter(new ScanPhotoAdapter(ScanActivity.this, images, new ScanPhotoAdapter.OnTapListener() {
            @Override
            public void onTap() {
                finish();
                overridePendingTransition(com.clyr.test.R.anim.fade_in, com.clyr.test.R.anim.fade_out);
            }
        }));

        photoVp.setCurrentItem(index);
    }

    protected void addListener() {
        photoVp.addOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 隐藏虚拟按键，并且设置成全屏
     */
    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            position = position % images.size();
            if (position < 0) {
                position = position + images.size();
            }
            titleTv.setText((position + 1) + "/" + images.size());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(com.clyr.test.R.anim.fade_in, com.clyr.test.R.anim.fade_out);
    }
}
