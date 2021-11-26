package com.clyr.utils.utilshelper;

import android.util.Log;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by M S I of clyr on 2019/5/20.
 */
public class ORHelper {
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

    public static <T> T getService(String url,Class<T> cla){
        return retrofit(url).build().create(cla);
    }
    /*
     **打印retrofit信息部分
     */
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
            //打印retrofit日志
            Log.e("RetrofitLog", "retrofitBack = " + message)
    );

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
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit;
    }
}
