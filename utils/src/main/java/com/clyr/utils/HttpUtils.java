package com.clyr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.clyr.utils.utilshelper.ORHelper;
import com.clyr.utils.utilshelper.ORService;
import com.google.gson.GsonBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by M S I of clyr on 2019/11/15.
 * okhttp 封装工具 OkHttpUtils Retrofit
 */
public class HttpUtils {
    /**
     * OkHttpUtils start
     */
    public static GetBuilder get(String url, Map<String, String> map) {
        sprintLog(url, map);
        return OkHttpUtils.get().url(url).params(map);
    }

    public static PostFormBuilder post(String url, Map<String, String> map) {
        sprintLog(url, map);
        return OkHttpUtils.post().url(url).params(map);
    }


    public static void getFile(String url, String path, String name) {
        if (path == null) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        if (name == null) {
            name = String.valueOf(System.currentTimeMillis());//没有格式
        }
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(path, name) {

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(File response, int id) {

            }
        });
    }

    public static void getBitmapNet(String url, final ImageView imageView, final Context context) {
        OkHttpUtils.get().url(url).build().execute(new BitmapCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                ToastUtils.showShort(context, "图片加载失败");
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                imageView.setImageBitmap(response);
            }
        });

    }

    public static void setBackgroundNet(String url, final View view, final Context context) {
        OkHttpUtils.get().url(url).build().execute(new BitmapCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                ToastUtils.showShort(context, "图片加载失败");
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                view.setBackground(BitmapUtils.bitmapToDrawable(response));
            }
        });
    }

    /**
     * 打印网络请求Log
     *
     * @param url
     * @param params
     */
    public static void sprintLog(String url, Map<String, String> params) {
        if (url != null && params != null) {
            url += "?";
            for (Map.Entry<String, String> en : params.entrySet()) {
                url += en.getKey() + "=" + en.getValue() + "&";
            }
            url = url.substring(0, url.length() - 1);
            Log.d("OkHttpUtils", url);
        }
    }

    /**
     * https访问设置
     * 1.可以访问所有 https
     * 2.请求时间延长为 10s
     *
     * @return
     */
    public static OkHttpUtils setSSLParams() {

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("OkHttpUtils"))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        return OkHttpUtils.initClient(okHttpClient);
    }
    /** OkHttpUtils end */


    /**
     * Retrofit start
     */
    public static Call<ResponseBody> getCall(@NotNull String mUrl, String url) {
        ORService orService = getOrService(mUrl);
        return orService.get(url);
    }

    public static Call<ResponseBody> getCall(@NotNull String mUrl, String url, @NotNull Map<String, String> map) {
        ORService orService = getOrService(mUrl);
        return orService.get(url, map);
    }

    public static Call<ResponseBody> postCall(@NotNull String mUrl, String url) {
        ORService orService = getOrService(mUrl);
        return orService.post(url);
    }

    public static Call<ResponseBody> postCall(@NotNull String mUrl, String url, @NotNull Map<String, String> map) {
        ORService orService = getOrService(mUrl);
        return orService.post(url, map);
    }

    private static ORService getOrService(@NotNull String mUrl) {
        //打印retrofit信息部分 //打印retrofit日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Log.e("RetrofitLog", "retrofitBack = " + message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .client(getLogClient())//此client是为了打印信息
                .build();
        return retrofit.create(ORService.class);
    }

    //TODO 仅作为例子
    public static Call<ResponseBody> intactCall(@NotNull String mUrl, String url) {
        //打印retrofit信息部分 //打印retrofit日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Log.e("RetrofitLog", "retrofitBack = " + message)
        );
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .client(getLogClient())//此client是为了打印信息
                .build();
        ORService orService = retrofit.create(ORService.class);
        return orService.get(url);
    }

    private static String BASE_URl = "";

    public static Retrofit.Builder retrofit(String url) {
        OkHttpClient client = getClient();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client);

    }

    public static OkHttpClient getClient() {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static <T> T getService(String url, Class<T> cla) {
        return retrofit(url).build().create(cla);
    }


    public <T> T getHttpApi(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .client(ORHelper.getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(service);
    }

    public static Retrofit getHttpApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .client(ORHelper.getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit;
    }


    /**
     * 打印retrofit信息部分
     */
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
            //打印retrofit日志
            Log.e("RetrofitLog", "retrofitBack = " + message)
    );

    public static OkHttpClient getLogClient() {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();
        return client;
    }


    /**
     * Retrofit end
     */

    // Demo
    public void retrofit() {
        String mUrl = "";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .client(getLogClient())//此client是为了打印信息
                .build();
        ORService orService = retrofit.create(ORService.class);
        Call<ResponseBody> calln = orService.post("cuGetDevTreeEx");
        calln.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null && response.isSuccessful()) {
                    try {
                        MyLog.d(response.body().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(t.getMessage());
            }
        });
    }
}
