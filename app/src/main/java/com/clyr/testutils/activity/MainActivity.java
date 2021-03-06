package com.clyr.testutils.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.testutils.databinding.ActivityMainBinding;
import com.clyr.utils.MessageEvent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaeger.library.StatusBarUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.clyr.testutils.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void initView() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        //StatusBarUtil.setLightMode(MainActivity.this);
        StatusBarUtil.setDarkMode(MainActivity.this);
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //?????????????????????
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            String shortcuts = SystemUtilActivity.shortcuts;
            if (!TextUtils.isEmpty(intent.getStringExtra(shortcuts)) && shortcuts.equals(intent.getStringExtra(shortcuts))) {
                startActivity(SystemUtilActivity.class);
                intent.putExtra(shortcuts, "");
            }
        }
    }
}