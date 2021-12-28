package com.clyr.utils.utilshelper;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by M S I of clyr on 2019/5/20.
 */
public class HttpLog {
    /*
     **打印retrofit信息部分
     */
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
            //打印retrofit日志
            Log.e("RetrofitLog", "retrofitBack = " + message)
    );
    public static OkHttpClient getLogClient(){
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();
    }
}
