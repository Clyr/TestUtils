package com.clyr.testutils.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clyr.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by clyr on 2018/3/13 0013.
 * 其他10个fragment都要继承它
 */

public class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {

    }
    public void showLoadingDialog(String msg) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            //activity.showLoadingDialog(msg);
        }
    }

    public void hideLoadingDialog() {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
           // activity.hideLoadingDialog();
        }
    }
    /** 因为使用了fragment==null 的判断 故不会执行生命周期（无法被动刷新）
     *  使用 在fragment管理界面 使用fragment.reShowExecute()执行刷新
     * **/
    public void reShowExecute(){

    }
}
