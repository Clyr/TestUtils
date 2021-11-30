package com.clyr.testutils.fragment.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clyr.base.interfaces.OnItemClickListener;
import com.clyr.testutils.R;
import com.clyr.testutils.activity.DialogActivity;
import com.clyr.testutils.activity.EmptyActivity;
import com.clyr.testutils.activity.GridActivity;
import com.clyr.testutils.activity.IOActivity;
import com.clyr.testutils.activity.LoadActivity;
import com.clyr.testutils.activity.OkHttpActivity;
import com.clyr.testutils.adapter.HomeFragmentAdapter;
import com.clyr.testutils.base.Const;
import com.clyr.testutils.databinding.FragmentHomeBinding;
import com.clyr.utils.MyLog;
import com.clyr.utils.ToastUtils;
import com.clyr.utils.UIUtils;
import com.clyr.utils.UtilsKit;
import com.clyr.view.DialogHelper;
import com.clyr.view.RecycleViewDivider;
import com.google.android.material.appbar.AppBarLayout;

import java.lang.reflect.Array;
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
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(title)) {
            intent.setClass(getContext(), getActivityClass(title));
            intent.putExtra(Const.TITLE, title);
        }
        startActivity(intent);
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
                return DialogActivity.class;
            case "Sqlite":
                return DialogActivity.class;
            case "Tree_list":
                return DialogActivity.class;
            case "雷达图":
                return DialogActivity.class;
            case "滚动新闻":
                return DialogActivity.class;
            case "表格":
                return DialogActivity.class;
            case "测试调用三方软件":
                return DialogActivity.class;
            case "测试获取网络地址":
                return DialogActivity.class;
            case "刷新":
                return DialogActivity.class;
            case "系统工具":
                return DialogActivity.class;
            case "UpDate":
                return DialogActivity.class;
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
        }

        return EmptyActivity.class;
    }
}