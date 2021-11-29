package com.clyr.testutils.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.clyr.base.bean.CityPM25Detail;
import com.clyr.base.bean.Pm;
import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.GsonUtil;
import com.clyr.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class OkHttpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.okhttputils).setOnClickListener(v -> okHttpUtils());
    }

    private void okHttpUtils() {
        //PM2.5 key n2yeWil241d1a7d235c308e8556bccf5ec185de68768979
        //?apiKey=您的apiKey&city=参数1
        String url = "https://api.apishop.net/common/air/getCityPM25Detail";
        Map<String, String> mapData = new HashMap<>();
        mapData.put("apiKey", "n2yeWil241d1a7d235c308e8556bccf5ec185de68768979");
        mapData.put("city", "潍坊");
        OkHttpUtils.get().url(url).params(mapData).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("response", e.toString());
                Toast.makeText(OkHttpActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("response", response);
                CityPM25Detail cityPM25Detail = GsonUtil.fromJson(response, CityPM25Detail.class);
                Pm pm = cityPM25Detail.getResult().getPm();
                String toast = pm.getArea() + " - " + pm.getQuality() + " - " + pm.getPm2_5();
                ToastUtils.showShort(toast);
            }
        });
    }
}