package com.clyr.testutils.activity;

import android.os.Bundle;

import com.clyr.test.SqliteActivity;
import com.clyr.test.cache.AcheckActivity;
import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.testutils.base.Const;
import com.clyr.utils.IOSave;
import com.clyr.utils.SystemUtils;
import com.clyr.utils.ToastUtils;

public class IOActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ioactivity);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.acheck).setOnClickListener(v -> startActivity(AcheckActivity.class));
        findViewById(R.id.sputilset).setOnClickListener(v -> IOSave.init().spSaveString(this, Const.TITLE, SystemUtils.getSystemTime() + ""));
        findViewById(R.id.sputilget).setOnClickListener(v -> {
            String string = IOSave.init().spGetString(this, Const.TITLE, "");
            ToastUtils.showShort(string);
        });

        findViewById(R.id.sq_lite).setOnClickListener(v -> startActivity(SqliteActivity.class));

    }
}