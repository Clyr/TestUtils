package com.clyr.testutils.fragment.home;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.clyr.base.interfaces.OnItemClickListener;
import com.clyr.test.CheckTestActivity;
import com.clyr.testutils.R;
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
import com.clyr.testutils.adapter.HomeFragmentAdapter;
import com.clyr.testutils.base.Const;
import com.clyr.testutils.databinding.FragmentHomeBinding;
import com.clyr.utils.MyLog;
import com.clyr.utils.SystemUtils;
import com.clyr.utils.ToastUtils;
import com.clyr.view.RecycleViewDivider;
import com.clyr.view.loadingdialog.LoadingDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.xuexiang.xupdate.UpdateManager;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;

import java.util.Arrays;
import java.util.Collections;

public class HomeFragment extends Fragment implements OnItemClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private AppBarLayout appBarLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {

        });
        initView();
        return root;
    }

    private void initView() {
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        binding.recyclerview.setLayoutManager(layout);

        String[] stringArray = getResources().getStringArray(R.array.home_fun_list);
        HomeFragmentAdapter homeFragmentAdapter = new HomeFragmentAdapter(Arrays.asList(stringArray), this);
        binding.recyclerview.setAdapter(homeFragmentAdapter);
        binding.recyclerview.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL));
    }


    /**
     * 根据百分比改变颜色透明度
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
            intent.setClass(getContext(), aClass);
            intent.putExtra(Const.TITLE, title);
            startActivity(intent);
        }

    }

    private Class<?> getActivityClass(String str) {
        MyLog.d(str);
        switch (str) {
            case "OkHttp":
                return OkHttpActivity.class;
            case "IO存储":
                return IOActivity.class;
            case "GridView":
                return GridActivity.class;
            case "Media相关":
                return MediaActivity.class;
            case "Tree_list":
                return TreeListActivity.class;
            case "测试调用三方软件":
                startThreeApp();
                return null;
            case "测试获取网络地址":
                getIpAddress();
                return null;
            case "系统工具":
                return SystemUtilActivity.class;
            case "UpDate":
                getUpdate();
                return null;
            case "通知":
                return NotificationActivity.class;
            case "Rxjava":
                return RxjavaActivity.class;
            case "自定义UI":
                return CustomUIActivity.class;
            case "VPN监测开关":
                checkVPN();
                return null;
            case "推送":
                return PushActivity.class;
            case "地图":
                return MapActivity.class;
            case "分享":
                return ShareActivity.class;
            case "图表":
                return ChartActivity.class;
            case "Fragment框架":
                return FragmentActivity.class;
            case "CoolViewPager":
                return CoolViewPagerActivity.class;
            case "验证测试":
                return CheckTestActivity.class;
        }

        return EmptyActivity.class;
    }

    private boolean vpn = false;

    private void checkVPN() {
        vpn = !vpn;
        ToastUtils.showShort(vpn ? "正在监测VPN" : "关闭监测VPN");
        if (vpn)
            if (SystemUtils.isVpnUsed()) {
                ToastUtils.showShort("使用VPN中...\n" + SystemUtils.getIPAddress(getContext()));
            } else {
                ToastUtils.showShort("未使用使用VPN\n" + SystemUtils.getIPAddress(getContext()));
            }
    }

    private void onClickshortcutsAdd() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getContext().getSystemService(ShortcutManager.class);

            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(getContext(), "noti_channel_demo")
                    .setIcon(Icon.createWithResource(getContext(), R.drawable.logo))
                    .setShortLabel("通知渠道")
                    .setLongLabel("通知渠道演示")
                    .setIntent(intent)
                    .build();
            shortcutManager.addDynamicShortcuts(Collections.singletonList(shortcut));
        }
    }

    private void getUpdate() {

        String appurl = "http://182.38.207.125:49155/imtt.dd.qq.com/16891/apk/912E6414B2549FFC49C8C63887F4C536.apk?mkey=618a0eb9700b56835a0410fc7dcf0043&arrive_key=23364718829&fsname=com.heiguang.hgrcwandroid_2.4.9_60.apk&hsr=4d5s&cip=122.4.199.97&proto=http";
        UpdateEntity updateEntity = new UpdateEntity().setHasUpdate(true)
                .setIsIgnorable(true)
                .setVersionCode(SystemUtils.getVersionCode(getContext()))
                .setVersionName(SystemUtils.getAppVersion(getContext()))
                .setUpdateContent("Fix bug")
                .setDownloadUrl(appurl).setSize(30 * 1024);
        UpdateManager build = XUpdate.newBuild(getContext())
                .supportBackgroundUpdate(true)
                //.promptThemeColor(getContext().getResources().getColor(R.color.zerofiveczeroab))
                //.promptTopResId() //顶部图片
                //.promptButtonTextColor() //按钮颜色
                //.promptWidthRatio((float) 0.5) //提示器宽度占屏幕的比例
                //.promptHeightRatio()
                //.promptIgnoreDownloadError(true)// 忽略下载异常，不关闭更新提示窗
                .build();
        build.update(updateEntity);
        MyLog.d("XUpdate", build.toString());

    }

    private void getIpAddress() {
        SystemUtils.getNetIp();
    }

    private void startThreeApp() {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.heiguang.hgrcwandroid", "com.heiguang.hgrcwandroid.activity.SplashActivity");
            intent.putExtra("PushUtils", "PushUtils");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showLong("╮(╯▽╰)╭");
        }



        /*try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.matrix.tramsh5", "io.dcloud.PandoraEntry");
            intent.putExtra("test", "测试");
            startActivity(intent);
            LoadingDialog.showLoading(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showLong("╮(╯▽╰)╭");
        }*/
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
}