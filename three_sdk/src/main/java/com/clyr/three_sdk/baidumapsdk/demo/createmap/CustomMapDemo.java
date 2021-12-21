package com.clyr.three_sdk.baidumapsdk.demo.createmap;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 演示MapView的基本用法
 */
public class CustomMapDemo extends AppCompatActivity {
    // 地图View实例
    private MapView mMapView;

    // 地图View布局
    private FrameLayout mFrameLayout;

    // 个性化地图开关标识
    private static final int OPEN_ID_GRAY = 0;
    private static final int OPEN_ID_WHITE = 1;
    private static final int CLOSE_ID = 2;

    // 用于设置个性化地图的样式文件
    private static final String CUSTOM_FILE_NAME_GRAY = "custom_map_config_CX.sty";
    private static final String CUSTOM_FILE_NAME_WHITE = "custom_map_config_YSYY.sty";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapView = new MapView(this, new BaiduMapOptions());
        initView(this);
        setContentView(mFrameLayout);

        // 构建地图状态
        MapStatus.Builder builder = new MapStatus.Builder();
        // 中心点设置为颐和园
        LatLng center = new LatLng(39.998152, 116.276973);
        // 默认缩放级别14.5级
        float zoom = 14.5f;
        builder.target(center).zoom(zoom);
        // 更新地图
        mMapView.getMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        // 默认开启个性化样式
        String customStyleFilePath = getCustomStyleFilePath(CustomMapDemo.this, CUSTOM_FILE_NAME_GRAY);

        // V6.0.0版本起，强烈建议使用新的个性化地图API。能够实现动态更改样式（同一地图设置不同的样式），适配多地图场景（不同的地图设置不同的样式）
        // 并且路径设置API不再要求在地图创建之前，地图创建完成之后设置即可。在地图释放时，也无需关闭个性化开关了。
        // 老版本个性化样式API功能保持兼容，但是限制较多。不建议使用。
        mMapView.setMapCustomStylePath(customStyleFilePath);
        mMapView.setMapCustomStyleEnable(true);
    }

    // 初始化View
    private void initView(Context context) {
        mFrameLayout = new FrameLayout(this);
        mFrameLayout.addView(mMapView);

        RadioGroup group = new RadioGroup(context);
        group.setBackgroundColor(Color.DKGRAY);
        // 个性化开关水平排列
        group.setOrientation(LinearLayout.HORIZONTAL);
        group.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final RadioButton grayStyleButton = new RadioButton(context);
        grayStyleButton.setText("开启样式1");
        grayStyleButton.setId(OPEN_ID_GRAY);
        grayStyleButton.setTextColor(Color.WHITE);
        group.addView(grayStyleButton, params);

        final RadioButton whiteStyleButton = new RadioButton(context);
        whiteStyleButton.setText("开启样式2");
        whiteStyleButton.setTextColor(Color.WHITE);
        whiteStyleButton.setId(OPEN_ID_WHITE);
        group.addView(whiteStyleButton, params);

        final RadioButton closeStyleButton = new RadioButton(context);
        closeStyleButton.setText("关闭个性化");
        closeStyleButton.setTextColor(Color.WHITE);
        closeStyleButton.setId(CLOSE_ID);
        group.addView(closeStyleButton, params);

        // 默认打开个性化地图样式，开关选择打开
        grayStyleButton.setChecked(true);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == OPEN_ID_GRAY) {
                    // 开启灰色道路个性化样式
                    String customStyleFilePath = getCustomStyleFilePath(CustomMapDemo.this, CUSTOM_FILE_NAME_GRAY);
                    mMapView.setMapCustomStylePath(customStyleFilePath);
                    mMapView.setMapCustomStyleEnable(true);
                } else if (checkedId == OPEN_ID_WHITE) {
                    // 开启白色道路个性化样式
                    String customStyleFilePath = getCustomStyleFilePath(CustomMapDemo.this, CUSTOM_FILE_NAME_WHITE);
                    mMapView.setMapCustomStylePath(customStyleFilePath);
                    mMapView.setMapCustomStyleEnable(true);
                } else if (checkedId == CLOSE_ID){
                    // 关闭个性化样式
                    mMapView.setMapCustomStyleEnable(false);
                } else {
                    Log.e("CustomMapDemo", "Invalid check");
                }
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFrameLayout.addView(group, layoutParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }

    private String getCustomStyleFilePath(Context context, String customStyleFileName) {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String parentPath = null;

        try {
            inputStream = context.getAssets().open("customConfigdir/" + customStyleFileName);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            parentPath = context.getFilesDir().getAbsolutePath();
            File customStyleFile = new File(parentPath + "/" + customStyleFileName);
            if (customStyleFile.exists()) {
                customStyleFile.delete();
            }
            customStyleFile.createNewFile();

            outputStream = new FileOutputStream(customStyleFile);
            outputStream.write(buffer);
        } catch (IOException e) {
            Log.e("CustomMapDemo", "Copy custom style file failed", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("CustomMapDemo", "Close stream failed", e);
                return null;
            }
        }

        return parentPath + "/" + customStyleFileName;
    }

}
