package com.clyr.testutils.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clyr.testutils.R;
import com.clyr.utils.GsonUtil;
import com.clyr.utils.MessageEvent;
import com.clyr.utils.MyLog;
import com.clyr.utils.ToastUtils;
import com.clyr.utils.UtilsKit;
import com.clyr.view.loadingdialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by 11635 of clyr on 2021/11/25.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public LinearLayout left_lin;
    public TextView title_center_text;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        UtilsKit.init(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    protected abstract void initView();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        MyLog.d(GsonUtil.toJson(event));
        if (event != null) {
            if (event.Type == MessageEvent.NOLOGIN) {
                ToastUtils.showShort(event.message);
            } else if (event.Type == MessageEvent.CHATRELOAD) {
                ToastUtils.showShort(event.message);
            }
        }
    }

    public void startActivity(Class<?> cla) {
        startActivity(cla, null);
    }

    public void startActivity(Class<?> cla, Intent in) {
        Intent intent;
        if (in == null) {
            intent = new Intent(this, cla);
        } else {
            intent = in;
        }
        startActivity(intent);
    }

    public void initBar() {
        left_lin = findViewById(R.id.left_lin);
        title_center_text = findViewById(R.id.title_center_text);
        left_lin.setOnClickListener(v -> onBackPressed());
        title_center_text.setText(getIntent().getStringExtra(Const.TITLE));
    }

    public LinearLayout getTitleBody() {
        return findViewById(R.id.title_body);
    }

    private void showLoadding() {
        if (this.isFinishing()) {
            return;
        }
        if (dialog == null) {
            dialog = LoadingDialog.showLoading(this);
        } else {
            dialog.show();
        }
    }

    private void hideLoadding() {
        if (this.isFinishing()) {
            return;
        }
        if (dialog != null) {
            dialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (dialog != null) {
            dialog.hide();
        }
    }

}
