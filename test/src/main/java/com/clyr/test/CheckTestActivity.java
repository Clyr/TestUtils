package com.clyr.test;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.clyr.test.bean.NumberDouble;
import com.clyr.test.bean.NumberInt;
import com.clyr.utils.MyLog;
import com.google.gson.Gson;

public class CheckTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_test);
        initView();
    }

    private void initView() {
        findViewById(R.id.int_obj_double).setOnClickListener(v -> initTestData());
        findViewById(R.id.num_to_move).setOnClickListener(v -> numToMove());
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
}