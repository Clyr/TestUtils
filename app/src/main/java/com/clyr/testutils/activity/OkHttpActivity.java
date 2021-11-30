package com.clyr.testutils.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clyr.base.bean.CityPM25Detail;
import com.clyr.base.bean.Pm;
import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.GsonUtil;
import com.clyr.utils.MyLog;
import com.clyr.utils.ToastUtils;
import com.clyr.utils.utilshelper.HttpLog;
import com.clyr.utils.utilshelper.ORService;
import com.lzy.okgo.OkGo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class OkHttpActivity extends BaseActivity {
    private final String mUrl = "https://api.apishop.net/common/air/getCityPM25Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
    }

    @Override
    protected void initView() {
        initBar();
        findViewById(R.id.okhttputils).setOnClickListener(v -> okHttpUtils());
        findViewById(R.id.okgo).setOnClickListener(v -> okGo());
        findViewById(R.id.okhttp3).setOnClickListener(v -> okHttp3());
        findViewById(R.id.retrofit).setOnClickListener(v -> okRetrofit());
    }

    private void okRetrofit() {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .client(HttpLog.getLogClient())//此client是为了打印信息
                .build();
        ORService orService = retrofit.create(ORService.class);

        retrofit2.Call<ResponseBody> calln = orService.post("cuGetDevTreeEx");
        calln.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response != null && response.isSuccessful()) {
                    try {
                        MyLog.d(response.body().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                MyLog.e(t.getMessage());
            }
        });


        Map<String, String> map = getStringMap();
        retrofit2.Call<ResponseBody> callph = orService.post("", map);
        callph.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response != null && response.isSuccessful()) {
                    try {
                        MyLog.d(response.body().toString());
                        extracted(response.body().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                MyLog.e(t.getMessage());
            }
        });

    }

    private void okHttp3() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS).build();
        FormBody.Builder body = new FormBody.Builder();
        //body.add("act", "login").add("email", "jn01").add("pwd", "000000");
        //body.add("act","loadmyinfo").add("loginname","jn01");
        Map<String, String> mapData = getStringMap();
        String murl = "";
        for (Map.Entry<String, String> en : mapData.entrySet()) {
            if (en.getKey() == null || en.getValue() == null) {
                continue;
            }
            body.add(en.getKey(), en.getValue());
            murl += en.getKey() + "=" + en.getValue() + "&";
        }
        Log.d("okhttp3url", murl);
        Request.Builder builder = new Request.Builder().url(mUrl).post(body.build());
        Call call = client.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {//判断超时异常

                }
                if (e instanceof ConnectException) {//判断连接异常，我这里是报Failed to connect to 10.7.5.144

                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().toString();
                extracted(string);
            }
        });
    }

    private void okGo() {
        Map<String, String> mapData = getStringMap();
        OkGo.put(mUrl).params(mapData).execute(new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                extracted(s);
            }
        });
    }

    private void okHttpUtils() {
        Map<String, String> mapData = getStringMap();
        OkHttpUtils.get().url(mUrl).params(mapData).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("response", e.toString());
                Toast.makeText(OkHttpActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("response", response);
                extracted(response);
            }
        });
    }

    private void extracted(String response) {
        CityPM25Detail cityPM25Detail = GsonUtil.fromJson(response, CityPM25Detail.class);
        Pm pm = cityPM25Detail.getResult().getPm();
        String toast = pm.getArea() + " - " + pm.getQuality() + " - " + pm.getPm2_5();
        ToastUtils.showShort(toast);
    }

    @NonNull
    private Map<String, String> getStringMap() {
        //PM2.5 key n2yeWil241d1a7d235c308e8556bccf5ec185de68768979
        //?apiKey=您的apiKey&city=参数1

        Map<String, String> mapData = new HashMap<>();
        mapData.put("apiKey", "n2yeWil241d1a7d235c308e8556bccf5ec185de68768979");
        mapData.put("city", "潍坊");
        return mapData;
    }

    /*
     **打印retrofit信息部分
     */
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
            //打印retrofit日志
            Log.e("RetrofitLog", "retrofitBack = " + message)

    );
}