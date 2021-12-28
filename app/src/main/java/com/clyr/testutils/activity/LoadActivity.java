package com.clyr.testutils.activity;

import android.os.Bundle;
import android.os.Handler;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.view.loadingdialog.LoadingDialog;

public class LoadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.btn_load1).setOnClickListener(v -> {
            LoadingDialog.showLoading(this);
            handler.postDelayed(runnable, 2000);
        });
        findViewById(R.id.btn_load2).setOnClickListener(v -> {
            LoadingDialog.showLoading(this, R.style.ProgressDialogTransparent);
            handler.postDelayed(runnable, 2000);
        });
        findViewById(R.id.btn_load3).setOnClickListener(v -> {
            LoadingDialog.showLoading(this, R.style.ProgressDialogTransparent, "BallSpinFadeLoaderIndicator");
            handler.postDelayed(runnable, 2000);
        });
    }

    Handler handler = new Handler();
    Runnable runnable = LoadingDialog::cancelLoading;
}