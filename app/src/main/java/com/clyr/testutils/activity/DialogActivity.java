package com.clyr.testutils.activity;


import android.os.Bundle;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.testutils.base.Const;
import com.clyr.view.DialogHelper;

public class DialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.button1).setOnClickListener(v -> DialogHelper.alertDialogDiy(this));
        findViewById(R.id.button2).setOnClickListener(v -> DialogHelper.textDialog(this));
    }
}