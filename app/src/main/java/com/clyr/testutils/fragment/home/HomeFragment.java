package com.clyr.testutils.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.clyr.base.interfaces.OnItemClickListener;
import com.clyr.test.AnimActivity;
import com.clyr.test.CheckTestActivity;
import com.clyr.testutils.App;
import com.clyr.testutils.BuildConfig;
import com.clyr.testutils.R;
import com.clyr.testutils.activity.BottomSmallActivity;
import com.clyr.testutils.activity.ChartActivity;
import com.clyr.testutils.activity.CoolViewPagerActivity;
import com.clyr.testutils.activity.CustomUIActivity;
import com.clyr.testutils.activity.EmptyActivity;
import com.clyr.testutils.activity.FragmentActivity;
import com.clyr.testutils.activity.GridActivity;
import com.clyr.testutils.activity.IOActivity;
import com.clyr.testutils.activity.MainActivity;
import com.clyr.testutils.activity.MapActivity;
import com.clyr.testutils.activity.MediaActivity;
import com.clyr.testutils.activity.NotificationActivity;
import com.clyr.testutils.activity.OkHttpActivity;
import com.clyr.testutils.activity.PushActivity;
import com.clyr.testutils.activity.RxjavaActivity;
import com.clyr.testutils.activity.ShareActivity;
import com.clyr.testutils.activity.SystemUtilActivity;
import com.clyr.testutils.activity.TreeListActivity;
import com.clyr.testutils.activity.WebViewActivity;
import com.clyr.testutils.adapter.HomeFragmentAdapter;
import com.clyr.testutils.base.Const;
import com.clyr.testutils.databinding.FragmentHomeBinding;
import com.clyr.testutils.push.PushUtils;
import com.clyr.utils.MyLog;
import com.clyr.utils.SystemUtils;
import com.clyr.utils.ToastUtils;
import com.clyr.view.RecycleViewDivider;
import com.clyr.view.loadingdialog.LoadingDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.unity3d.player.UnityPlayerActivity;
import com.xuexiang.xupdate.UpdateManager;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemClickListener {

    private FragmentHomeBinding binding;
    private AppBarLayout appBarLayout;
    private final Context mContext = getContext() == null ? App.getContext() : getContext();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {

        });
        initView();
        return root;
    }

    private void initView() {
        LinearLayoutManager layout = new LinearLayoutManager(mContext);
        binding.recyclerview.setLayoutManager(layout);

        List<String> mList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.home_fun_list)));
        if (!BuildConfig.DEBUG) {
            //UnsupportedOperationException
            //????????????String[]?????????List< String >???????????????????????????????????????????????????add???remove????????????
            //????????????????????????????????????ArrayList?????????Arrays??????????????????ArrayList
            mList.remove(0);
        }
        HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(mList, this);
        binding.recyclerview.setAdapter(homeFragmentAdapter);
        binding.recyclerview.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));

        //startActivity(new Intent(getContext(),CustomUIActivity.class));
    }


    /**
     * ????????????????????????????????????
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(int position, Object obj) {
        String title = (String) obj;
        Class<?> aClass = getActivityClass(title);
        if (aClass != null) {
            Intent intent = new Intent();
            intent.setClass(mContext, aClass);
            intent.putExtra(Const.TITLE, title);
            startActivity(intent);
            if ("Activity??????".equals(title)) {
                requireActivity().overridePendingTransition(com.clyr.test.R.anim.fade_in, com.clyr.test.R.anim.fade_out);
            }
        }

    }

    private Class<?> getActivityClass(String str) {
        MyLog.d(str);
        switch (str) {
            case "OkHttp":
                return OkHttpActivity.class;
            case "IO??????":
                return IOActivity.class;
            case "GridView":
                return GridActivity.class;
            case "Media??????":
                return MediaActivity.class;
            case "Tree_list":
                return TreeListActivity.class;
            case "????????????????????????":
                startThreeApp();
                return null;
            case "????????????????????????":
                getIpAddress();
                return null;
            case "????????????":
                return SystemUtilActivity.class;
            case "UpDate":
                getUpdate();
                return null;
            case "??????":
                return NotificationActivity.class;
            case "Rxjava":
                return RxjavaActivity.class;
            case "?????????UI":
                return CustomUIActivity.class;
            case "VPN????????????":
                checkVPN();
                return null;
            case "??????":
                return PushActivity.class;
            case "??????":
                return MapActivity.class;
            case "??????":
                return ShareActivity.class;
            case "??????":
                return ChartActivity.class;
            case "Fragment??????":
                return FragmentActivity.class;
            case "CoolViewPager":
                return CoolViewPagerActivity.class;
            case "????????????":
                return CheckTestActivity.class;
            case "WebView":
                return WebViewActivity.class;
            case "Activity??????":
                return AnimActivity.class;
            case "??????View":
                return BottomSmallActivity.class;
            case "UnityGame":
                return UnityPlayerActivity.class;
            default:
        }

        return EmptyActivity.class;
    }

    private boolean vpn = false;

    private void checkVPN() {
        vpn = !vpn;
        ToastUtils.showShort(vpn ? "????????????VPN" : "????????????VPN");
        if (vpn) {
            if (mContext != null) {
                if (SystemUtils.isVpnUsed()) {
                    ToastUtils.showShort("??????VPN???...\n" + SystemUtils.getIPAddress(mContext));
                } else {
                    ToastUtils.showShort("???????????????VPN\n" + SystemUtils.getIPAddress(mContext));
                }
            }
        }
    }

    private void onClickShortCutsAdd() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = mContext.getSystemService(ShortcutManager.class);

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(mContext, "notification_channel_demo")
                    .setIcon(Icon.createWithResource(mContext, R.drawable.logo))
                    .setShortLabel("????????????")
                    .setLongLabel("??????????????????")
                    .setIntent(intent)
                    .build();
            shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcut));
        }
    }

    private void getUpdate() {

        String app_url = "http://182.38.207.125:49155/imtt.dd.qq.com/16891/apk/912E6414B2549FFC49C8C63887F4C536.apk?mkey=618a0eb9700b56835a0410fc7dcf0043&arrive_key=23364718829&fsname=com.heiguang.hgrcwandroid_2.4.9_60.apk&hsr=4d5s&cip=122.4.199.97&proto=http";
        UpdateEntity updateEntity = new UpdateEntity().setHasUpdate(true)
                .setIsIgnorable(true)
                .setVersionCode(SystemUtils.getVersionCode(mContext))
                .setVersionName(SystemUtils.getAppVersion(mContext))
                .setUpdateContent("Fix bug")
                .setDownloadUrl(app_url).setSize(30 * 1024);
        UpdateManager build = XUpdate.newBuild(mContext)
                .supportBackgroundUpdate(true)
                //.promptThemeColor(mContext.getResources().getColor(R.color.white))
                //.promptTopResId() //????????????
                //.promptButtonTextColor() //????????????
                //.promptWidthRatio((float) 0.5) //?????????????????????????????????
                //.promptHeightRatio()
                //.promptIgnoreDownloadError(true)// ?????????????????????????????????????????????
                .build();
        build.update(updateEntity);
        MyLog.d("XUpdate", build.toString());

    }

    private void getIpAddress() {
        SystemUtils.getNetIp();
    }

    private void startThreeApp() {
        String class_name = mContext.getResources().getString(R.string.class_name);
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(class_name, class_name + ".activity.SplashActivity");
            intent.putExtra(PushUtils.pushTag, PushUtils.pushTag);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showLong("???(?????????)???");
        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LoadingDialog.cancelLoading();
    }


    private void simulateTouchEvent(View view, int x, int y) {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, metaState);
        view.dispatchTouchEvent(motionEvent);
        MotionEvent upEvent = MotionEvent.obtain(downTime + 1000, eventTime + 1000,
                MotionEvent.ACTION_UP, x, y, metaState);
        view.dispatchTouchEvent(upEvent);
    }
}