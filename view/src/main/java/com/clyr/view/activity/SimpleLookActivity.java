package com.clyr.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.VideoView;

import com.clyr.utils.BitmapUtils;
import com.clyr.view.R;

public class SimpleLookActivity extends AppCompatActivity {

    private ImageView mImageView;
    private VideoView mVideoView;
    private PopupWindow mPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_look);
        initView();
    }

    private void initView() {
        mImageView = findViewById(R.id.image_view);
        mVideoView = findViewById(R.id.video_view);

        Intent intent = getIntent();
        if (intent != null) {
            if ("image".equals(intent.getStringExtra("TAG"))) {
                mImageView.setVisibility(View.VISIBLE);
                setImageView(intent.getStringExtra("INFO"));
            } else if ("video".equals(intent.getStringExtra("TAG"))) {
                mVideoView.setVisibility(View.VISIBLE);
                setVideoView(intent.getStringExtra("INFO"));
            }
        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }

    }

    private void setVideoView(String info) {
        Uri uri = Uri.parse(info);
        initVideo(uri);
    }

    private void initVideo(final Uri data) {
        if (data == null) {
            return;
        }
        final VideoView video = (VideoView) findViewById(R.id.video);
//        Uri uri = Uri.parse("");
        video.setOnCompletionListener(mp -> video.start());
        //设置视频路径
        video.setVideoURI(data);
        //开始播放视频
        video.start();

    }

    private void setImageView(Object img) {
        BitmapUtils.setBitmap(this, img, mImageView);
        mPop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows2, null);
        mPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPop.setBackgroundDrawable(new BitmapDrawable());
        mPop.setContentView(view);
        ImageView mLl_popup = (ImageView) view.findViewById(R.id.imagepop);
        BitmapUtils.setBitmap(this, img, mLl_popup);
        mPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        mLl_popup.setOnClickListener(v -> mPop.dismiss());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mPop != null) {
                mPop.dismiss();
                mPop = null;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}