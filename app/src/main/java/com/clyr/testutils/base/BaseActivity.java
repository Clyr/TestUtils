package com.clyr.testutils.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clyr.utils.EventBusMsg;
import com.clyr.utils.GsonUtil;
import com.clyr.utils.MessageEvent;
import com.clyr.utils.MyLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by 11635 of clyr on 2021/11/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
        MyLog.d(BaseActivity.class.getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected abstract void initView();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        MyLog.d(GsonUtil.toJson(event));
        if (event != null) {
            if (event.Type == MessageEvent.NOLOGIN) {

            } else if (event.Type == MessageEvent.CHATRELOAD) {

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
}
