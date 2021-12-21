package com.clyr.three_sdk.baidumapsdk.demo.util;
/**
 * 此工具类用于通过扫描个性化样式编辑器生成的二维码展示个性化编辑器生成的个性化地图效果
 * 便于个性化地图调试
 */

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.baidu.mapapi.SDKInitializer;
import com.clyr.three_sdk.R;
import com.clyr.three_sdk.baidumapsdk.demo.createmap.MapTypeDemo;
import com.google.android.material.snackbar.Snackbar;


public final class CustomMapPreview extends AppCompatActivity
        implements /*QRCodeReaderView.OnQRCodeReadListener,*/ ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 0;

    // 扫描框
    //private QRCodeReaderView mQRCodeReaderView;
    // 整体布局
    private ViewGroup mMainLayout;

    //private CustomMapPreviewPointsView mPointsOverlayView;

    // 扫描结果view
    private TextView mResultTextView;

    private LoadCustomStyleResultReceiver mReceiver;

    // 标识是否第一次成功获取二维码结果，防止多次结果返回，造成频繁请求
    private boolean mIsFirstReaderSuccess = true;

    // 记录加载样式文件失败的错误码，通过比对方式相同的错误多次弹窗
    private int loadCustomStyleErrorCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map_preview);
        mMainLayout = findViewById(R.id.main_layout);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            requestCameraPermission();
        }

        registerLoadCustomStyleResultReceiver();
    }

    private void initQRCodeReaderView() {
        View content = getLayoutInflater().inflate(R.layout.custom_map_preview_content, mMainLayout, true);

       /* mQRCodeReaderView = content.findViewById(R.id.qrdecoderview);
        // 闪光灯勾选框
        CheckBox mFlashlightCheckBox = content.findViewById(R.id.flashlight_checkbox);
        // 扫描范围示意
        mPointsOverlayView = content.findViewById(R.id.points_overlay_view);
        mResultTextView = content.findViewById(R.id.result_text_view);
        // disable logging
        mQRCodeReaderView.setLoggingEnabled(false);

        mQRCodeReaderView.setOnQRCodeReadListener(this);
        // Use this function to enable/disable decoding
        mQRCodeReaderView.setQRDecodingEnabled(true);
        // Use this function to change the autofocus interval (default is 5 secs)
        mQRCodeReaderView.setAutofocusInterval(2000L);
        // Use this function to set back camera preview
        mQRCodeReaderView.setBackCamera();

        mFlashlightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Use this function to enable/disable Torch
                mQRCodeReaderView.setTorchEnabled(isChecked);
            }
        });

        mQRCodeReaderView.startCamera();*/
    }


    @Override
    protected void onResume() {
        super.onResume();

        mIsFirstReaderSuccess = true;

        /*if (null != mQRCodeReaderView) {
            mQRCodeReaderView.startCamera();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*if (null != mQRCodeReaderView) {
            mQRCodeReaderView.stopCamera();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if (null != mQRCodeReaderView) {
            mQRCodeReaderView.stopCamera();
        }*/

        unregisterReceiver(mReceiver);
    }


    public void onQRCodeRead(String text, PointF[] points) {
        // 赋值
        String mCustomStyleFileId = text;
        if (!TextUtils.isEmpty(mCustomStyleFileId) && mIsFirstReaderSuccess) {
            mIsFirstReaderSuccess = false;
            // 下载样式文件
            //CustomMapStyleLoader.getInstance().initCustomStyleFilePath(this.getApplicationContext());
            //CustomMapStyleLoader.getInstance().loadCustomMapStyleFile(mCustomStyleFileId, false);
        }

        mResultTextView.setText(text);
        //mPointsOverlayView.setPoints(points);

    }

    private void registerLoadCustomStyleResultReceiver() {
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        //iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_LOAD_CUSTOM_STYLE_SUCCESS);
        //iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_LOAD_CUSTOM_STYLE_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);

        mReceiver = new LoadCustomStyleResultReceiver();

        registerReceiver(mReceiver, iFilter);
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(mMainLayout, "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(CustomMapPreview.this, new String[] {
                            Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }).show();
        } else {
            Snackbar.make(mMainLayout, "Requesting camera permission.", Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(CustomMapPreview.this, new String[] {
                    Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != CAMERA_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mMainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mMainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private class LoadCustomStyleResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent) {
                return;
            }

            String action = intent.getAction();
            if (null == action) {
                return;
            }

            /*if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_LOAD_CUSTOM_STYLE_SUCCESS)) {
                // 调起MapFragmentDemo，展示个性化地图
                startMapFragmentDemoForPreview();
            } else {
                int errorCode = intent.getIntExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0);
                String errorMessage = intent.getStringExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_MESSAGE);

                if (errorCode != loadCustomStyleErrorCode) {
                    loadCustomStyleErrorCode = errorCode;
                    Snackbar.make(mMainLayout, "Load style error: " + errorCode + "; " + errorMessage, Snackbar.LENGTH_LONG).show();
                }
                mIsFirstReaderSuccess = true;
            }*/
        }
    }

    private void startMapFragmentDemoForPreview() {
        Intent intent = new Intent();
        intent.putExtra("loadCustomStyleFileMode", 1);
        intent.setClass(CustomMapPreview.this, MapTypeDemo.class);
        startActivity(intent);
    }
}
