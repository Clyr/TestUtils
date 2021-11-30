package com.clyr.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.clyr.view.R;

import java.io.File;


public class CameraActivity extends Activity {

    JCameraView jCameraView;
    public final String TAG = CameraActivity.class.getName();
    String mVideoUrl= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
        initCamera();
    }

    private void initCamera() {
        //1.1.1
        jCameraView = (JCameraView) findViewById(R.id.jcameraview);

        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);
        jCameraView.setTip("长按摄像");//设置提示
        //设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        //JCameraView监听
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
//                Toast.makeText(CameraActivity.this,"没有获取到拍照权限",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
//                Toast.makeText(CameraActivity.this,"没有获取到录像权限",Toast.LENGTH_SHORT).show();

            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                mVideoUrl = url;
                //右键设置有问题所以
                setResultUrl(url);

            }
            //@Override
            //public void quit() {
            //    (1.1.9+后用左边按钮的点击事件替换)
            //}
        });
        //左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
        //右边按钮点击事件
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                if("".equals(mVideoUrl)){
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("video",mVideoUrl);
                setResult(1000,intent);
                finish();
            }
        });

    }

    private void setResultUrl(final String url) {
        if("".equals(url)){
            Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("video",url);
        setResult(1000,intent);
        finish();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }

    }

}
