package com.clyr.testutils.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.testutils.base.Const;
import com.clyr.utils.ToastUtils;
import com.clyr.view.SlideUnlockView;
import com.clyr.view.UnReadView;
import com.clyr.view.captcha.SwipeCaptchaView;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

public class CustomUIActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_uiactivity);
    }

    @Override
    protected void initView() {
        initBar();
        UnReadView unReadView = findViewById(R.id.unreadview);
        EditText editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                unReadView.setText(s.toString());
            }
        });


        SlideUnlockView slideUnlockView = (SlideUnlockView) findViewById(R.id.slideUnlockView);
//    long[] patter = {1000, 1000, 2000, 50};
        //    vibrator.vibrate(patter,0);
        //    vibrator.cancel();
        //    vibrator.vibrate(10);
        //    long[] patter = {1000, 1000, 2000, 50};
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // 设置滑动解锁-解锁的监听
        slideUnlockView.setOnUnLockListener(unLock -> {
            // 如果是true，证明解锁
            if (unLock) {
                // 启动震动器 100ms
                vibrator.vibrate(100);
                // 当解锁的时候，执行逻辑操作，在这里仅仅是将图片进行展示
                // 重置一下滑动解锁的控件
                slideUnlockView.reset();
                // 让滑动解锁控件消失
//                slideUnlockView.setVisibility(View.GONE);
                ToastUtils.showLong("Open");
            }
        });

        findViewById(R.id.captchadialog).setOnClickListener(v -> showCaptchaDialog());

        initMarqueeView();
        findViewById(R.id.alertdialog).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, DialogActivity.class);
            intent.putExtra(Const.TITLE, getResources().getString(R.string.alertdialog));
            startActivity(intent);
        });
        findViewById(R.id.loaddialog).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, LoadActivity.class);
            intent.putExtra(Const.TITLE, getResources().getString(R.string.loaddialog));
            startActivity(intent);
        });
    }

    private void initMarqueeView() {
        String[] res = {
                "静夜思",
                "床前明月光", "疑是地上霜",
                "举头望明月",
                "低头思故乡"
        };
        MarqueeView marqueeView = findViewById(R.id.marqueeView);

        List<String> info = new ArrayList<>();
        info.add("静夜思");
        info.add("床前明月光");
        info.add("疑是地上霜");
        info.add("举头望明月");
        info.add("低头思故乡");
        // marqueeView.startWithList(info);

        // 在代码里设置自己的动画
        marqueeView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);

        // String notice = "心中有阳光，脚底有力量！心中有阳光，脚底有力量！心中有阳光，脚底有力量！";
        // marqueeView.startWithText(notice);

        marqueeView.setOnItemClickListener((position, textView) -> ToastUtils.showShort((String) textView.getText()));
    }

    private void showCaptchaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dlg_activity_main, null);
        final SwipeCaptchaView mSwipeCaptchaView = view.findViewById(R.id.swipeCaptchaView);

        final SeekBar mSeekBar = view.findViewById(R.id.dragBar);
        builder.setView(view);
        final AlertDialog alertDialog = builder.show();
        view.findViewById(R.id.btnChange).setOnClickListener(v -> {
            mSwipeCaptchaView.createCaptcha();
            mSeekBar.setEnabled(true);
            mSeekBar.setProgress(0);
        });
        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                Toast.makeText(CustomUIActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
                alertDialog.dismiss();
            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                Log.d("CustomUIActivity", "matchFailed() called with: swipeCaptchaView = [" + swipeCaptchaView + "]");
                Toast.makeText(CustomUIActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                mSwipeCaptchaView.matchCaptcha();
            }
        });

        //测试从网络加载图片是否ok
        Glide.with(this)
                .asBitmap()
                .load(R.drawable.bg_slide_unblock)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }
}