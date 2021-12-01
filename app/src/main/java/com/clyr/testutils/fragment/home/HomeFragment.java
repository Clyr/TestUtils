package com.clyr.testutils.fragment.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.clyr.base.interfaces.OnItemClickListener;
import com.clyr.testutils.R;
import com.clyr.testutils.activity.DialogActivity;
import com.clyr.testutils.activity.EmptyActivity;
import com.clyr.testutils.activity.GridActivity;
import com.clyr.testutils.activity.IOActivity;
import com.clyr.testutils.activity.LoadActivity;
import com.clyr.testutils.activity.MarqueeActivity;
import com.clyr.testutils.activity.MediaActivity;
import com.clyr.testutils.activity.OkHttpActivity;
import com.clyr.testutils.activity.RadarActivity;
import com.clyr.testutils.activity.RefrashActivity;
import com.clyr.testutils.activity.SystemUtilActivity;
import com.clyr.testutils.activity.TableActivity;
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
            case "AlertDialog":
                return DialogActivity.class;
            case "LoadDialog":
                return LoadActivity.class;
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
            case "雷达图":
                return RadarActivity.class;
            case "滚动新闻":
                return MarqueeActivity.class;
            case "表格":
                return TableActivity.class;
            case "测试调用三方软件":
                startThreeApp();
                return null;
            case "测试获取网络地址":
                getIpAddress();
                return null;
            case "刷新":
                return RefrashActivity.class;
            case "系统工具":
                return SystemUtilActivity.class;
            case "UpDate":
                getUpdate();
                return null;
            case "通知":
                return DialogActivity.class;
            case "Retrofit":
                return DialogActivity.class;
            case "Rxjava":
                return DialogActivity.class;
            case "ShortcutBadger":
                return DialogActivity.class;
            case "VPN监测开关":
                return DialogActivity.class;
            case "推送":
                return DialogActivity.class;
            case "地图":
                return DialogActivity.class;
            case "分享":
                return DialogActivity.class;
            case "图表":
                return DialogActivity.class;
        }

        return EmptyActivity.class;
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