package com.clyr.test;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.test.bean.NumberDouble;
import com.clyr.test.bean.NumberInt;
import com.clyr.utils.MyLog;
import com.clyr.utils.ToastUtils;
import com.google.gson.Gson;
import com.simple.spiderman.SpiderMan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CheckTestActivity extends AppCompatActivity {
    private final List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_test);
        initView();
    }

    private void initView() {
        initBar();
        findViewById(R.id.int_obj_double).setOnClickListener(v -> initTestData());
        findViewById(R.id.num_to_move).setOnClickListener(v -> numToMove());
        findViewById(R.id.check_final).setOnClickListener(v -> checkFinal());
        findViewById(R.id.check_Assert).setOnClickListener(this::chechAssert);
        findViewById(R.id.duplicate_removal).setOnClickListener(this::duplicateRemoval);
        findViewById(R.id.spider_man).setOnClickListener(this::SpiderMan);
        findViewById(R.id.spider_man_throw).setOnClickListener(this::SpiderManThrow);

        SpiderMan.setTheme(R.style.SpiderManTheme_Custom);


    }

    private void SpiderManThrow(View view) {
        String text = null;
        text.toUpperCase();
    }

    private void SpiderMan(View view) {
        try {
            String text = null;
            text.toUpperCase();
        } catch (Exception e) {
            SpiderMan.show(e);
        }
    }

    private void checkFinal() {
        //final针对的是引用，不能再指向第二个。
        //而实际指向的对象，内部(HashMap)怎么样变化，跟final没关系。
        MyLog.d(new Gson().toJson(mList));
        mList.add("1111111");
        mList.add("2222222");
        mList.add("3333333");
        MyLog.d(new Gson().toJson(mList));
    }

    private void initTestData() {
        NumberDouble numberDouble = new NumberDouble();
        numberDouble.setCount(1.0);
        numberDouble.setName("NumberDouble");

        MyLog.loge(new Gson().toJson(numberDouble));

        String json = new Gson().toJson(numberDouble);
        NumberInt numberInt = new Gson().fromJson(json, NumberInt.class);
        MyLog.loge(new Gson().toJson(numberInt));
        MyLog.loge(numberInt.toString());
    }

    private void numToMove() {
        int temp = 9;
        int temp2 = temp >> 1;
        MyLog.loge(temp2 + "");
        MyLog.loge((temp2 >> 1) + "");
        int temp3 = 0;
        MyLog.loge((temp3 >> 1) + "");
    }

    public void initBar() {
        LinearLayout left_lin = findViewById(R.id.left_lin);
        TextView title_center_text = findViewById(R.id.title_center_text);
        left_lin.setOnClickListener(v -> onBackPressed());
        title_center_text.setText("验证测试");
    }

    private void chechAssert(View v) {
        ToastUtils.showShort(returnAssert("123"));
    }

    private String returnAssert(String msg) {
        String ass = "123";
        assert ass.equals(msg);
        return ass;
    }

    private void duplicateRemoval(View v) {
        //能去重基本的几种类型，不能去除自定义类
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list1.add(i + "");
        }
        List<String> list2 = new ArrayList<>();
        for (int i = 6; i < 15; i++) {
            list2.add(i + "");
        }

        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(list1);
        hashSet.addAll(list2);

        MyLog.d(new Gson().toJson(hashSet));
    }


}