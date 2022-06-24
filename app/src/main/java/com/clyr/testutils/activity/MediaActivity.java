package com.clyr.testutils.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.ScreenshotUtil;
import com.clyr.utils.ThreadPoolUtils;
import com.clyr.utils.ToastUtils;
import com.clyr.view.GuaGuaKa;
import com.clyr.view.ProcessImageView;
import com.clyr.view.activity.CameraActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;

public class MediaActivity extends BaseActivity {

    private static final int IMAGE_OPEN = 10;
    private static final int IMAGE_CAMERA = 11;
    private static final int VIDEO_OPEN = 12;
    private static final int VIDEO_CAMERA = 13;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    String[] storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ProcessImageView processImageView = null;
    private Button mButton;
    private final int SUCCESS = 0;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.select_photo).setOnClickListener(v -> {
            if (getPermissions(0)) {
                Intent intent2 = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2, IMAGE_OPEN);
            }

        });
        findViewById(R.id.select_camera).setOnClickListener(v -> {
            if (getPermissions(1)) {
                Intent intent = new Intent(MediaActivity.this, CameraActivity.class);
                startActivityForResult(intent, IMAGE_CAMERA);
            }
        });
        findViewById(R.id.select_video).setOnClickListener(v -> {
            if (getPermissions(0)) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, VIDEO_OPEN);
            }
        });
        findViewById(R.id.select_camera_video).setOnClickListener(v -> {
            if (getPermissions(1)) {
                Intent intent = new Intent(MediaActivity.this, CameraActivity.class);
                startActivityForResult(intent, VIDEO_CAMERA);
            }
        });
        findViewById(R.id.select_mp3).setOnClickListener(v -> {

        });

        GuaGuaKa.setText("星期三");
        initImageView();
        initScreenShot();
    }

    private void initScreenShot() {
        if (getPermissions(0)) {
            RelativeLayout ssRel = findViewById(R.id.screenshot_rel);
            ImageView ssImage = findViewById(R.id.screenshot_image);
            Button ssBtn = findViewById(R.id.screenshot_btn);
            ssBtn.setOnClickListener(v -> {
                String fileRel = ScreenshotUtil.saveScreenshotFromView(ssRel, this);
                Glide.with(this).load(fileRel).into(ssImage);
            });

//            File file = ScreenshotUtil.saveScreenshotFromActivity(this);
            // Glide.with(this).load("/storage/emulated/0/dcim/1655452456986.jpg").into(ssImage);

            //
            ssRel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ssRel.getViewTreeObserver().removeOnGlobalLayoutListener(this);

//                    String saveScreenImage = ScreenshotUtil.saveScreenImage(ssRel);
                    String saveScreenImage = ScreenshotUtil.saveScreenImage(getWindow().getDecorView());
                    Glide.with(MediaActivity.this).load(saveScreenImage).into(ssImage);
                }
            });

        }
    }


    private void initImageView() {


        processImageView = findViewById(R.id.image);
        //模拟图片上传进度
        mButton = findViewById(R.id.buttonimage);
        final Animation animation = AnimationUtils.loadAnimation(MediaActivity.this, R.anim.revolve_refresh);
        mButton.setOnClickListener(v -> mButton.startAnimation(animation));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateImage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        updateImage();
    }

    private void updateImage() {
        mButton.setVisibility(View.GONE);
        processImageView.setImageResource(R.drawable.image);
        progress = 0;
        /*new Thread(() -> {
            while (true) {
                if (progress == 100) {//图片上传完成
                    handler.sendEmptyMessage(SUCCESS);
                    return;
                }
                progress++;
                processImageView.setProgress(progress);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

        ExecutorService es = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());

//        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.DiscardPolicy());
//        scheduledThreadPoolExecutor.schedule()
//        corePoolSize：线程池中的线程数量；
//        maximumPoolSize：线程池中的最大线程数量；
//        keepAliveTime：当线程池线程数量超过corePoolSize时，多余的空闲线程会在多长时间内被销毁；
//        unit：keepAliveTime的时间单位；
//        workQueue：任务队列，被提交但是尚未被执行的任务；
//        threadFactory：线程工厂，用于创建线程，一般情况下使用默认的，即Executors类的静态方法defaultThreadFactory()；handler：拒绝策略。当任务太多来不及处理时，如何拒绝任务。
        Runnable runnable = () -> {
            while (true) {
                if (progress == 100) {//图片上传完成
                    handler.sendEmptyMessage(SUCCESS);
                    return;
                }
                progress++;
                processImageView.setProgress(progress);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        es.execute(runnable);


    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(msg -> {
        if (msg.what == SUCCESS) {
            Toast.makeText(MediaActivity.this, "上传完成", Toast.LENGTH_SHORT).show();
//                    processImageView.setVisibility(View.GONE);
            processImageView.setImageResource(R.drawable.error);
            mButton.setVisibility(View.VISIBLE);
        }
        return false;
    });

    private boolean getPermissions(int tag) {
        if (tag == 0) {
            if (EasyPermissions.hasPermissions(this, storagePermission)) {
                return true;
            } else {
                EasyPermissions.requestPermissions(this, "", 0, storagePermission);
            }
        } else {
            if (EasyPermissions.hasPermissions(this, permissions)) {
                return true;
            } else {
                EasyPermissions.requestPermissions(this, "", 0, permissions);
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}