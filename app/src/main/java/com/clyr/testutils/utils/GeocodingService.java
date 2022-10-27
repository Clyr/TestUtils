package com.clyr.testutils.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 地理编码服务
 * Created by lzy of clyr on 2022/10/27.
 * 华为逆地理编码
 * @author lzy of clyr
 * @date 2022/10/27
 */
public class GeocodingService {
    public static final String ROOT_URL = "https://siteapi.cloud.huawei.com/mapApi/v1/siteService/reverseGeocode";

    public static final String connection = "?key=";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void reverseGeocoding(String apiKey, double lat, double lng) throws UnsupportedEncodingException {
        JSONObject json = new JSONObject();

        JSONObject location = new JSONObject();
        try {
            location.put("lng", lng);
            location.put("lat", lat);
            json.put("location", location);
            json.put("radius", 10);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        RequestBody body = RequestBody.create(JSON, String.valueOf(json));

        OkHttpClient client = new OkHttpClient();
        Request request =
                new Request.Builder().url(ROOT_URL + connection + URLEncoder.encode(apiKey, "UTF-8"))
                        .post(body)
                        .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HWonFailure", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("HWonResponse", response.body().string());
            }
        });
    }
}
