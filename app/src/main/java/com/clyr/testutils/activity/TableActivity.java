package com.clyr.testutils.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.clyr.base.bean.DateInfo;
import com.clyr.base.bean.JsonModle;
import com.clyr.base.bean.OrderInfo;
import com.clyr.base.bean.RoomInfo;
import com.clyr.testutils.R;
import com.clyr.testutils.adapter.ScrollablePanelAdapter;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.view.scrollablepanel.ScrollablePanel;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

public class TableActivity extends BaseActivity implements View.OnClickListener {

    ScrollablePanel mScrollablepanel;
    Button mUrl1, mUrl2, mUrl3, mUrl4, mUrl5;
    LinearLayout mLinearLayout;
    private ScrollablePanelAdapter mScrollablePanelAdapter;
    private String mUrl;
    private String url = "http://60.208.80.55:9999/TRAMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
    }

    @Override
    protected void initView() {
        mScrollablepanel = findViewById(R.id.scrollablepanel);
        mUrl1 = findViewById(R.id.url1);
        mUrl2 = findViewById(R.id.url2);
        mUrl3 = findViewById(R.id.url3);
        mUrl4 = findViewById(R.id.url4);
        mUrl5 = findViewById(R.id.url5);
        mLinearLayout = findViewById(R.id.linearLayout);
        mUrl1.setOnClickListener(this);
        mUrl2.setOnClickListener(this);
        mUrl3.setOnClickListener(this);
        mUrl4.setOnClickListener(this);
        mUrl5.setOnClickListener(this);
    }

    private void init() {
        mScrollablePanelAdapter = new ScrollablePanelAdapter();
        mScrollablepanel.setPanelAdapter(mScrollablePanelAdapter);
        mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000586";
//        mUrl = "http://192.168.32.22:8080/TRAMS/mobileinspect!ItemData?itemid=ITEMS0000000708";
        initGetData();
    }

    private void initGetData() {
        OkHttpUtils.post().url(mUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(TableActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                JsonModle jsonModle = new Gson().fromJson(response, JsonModle.class);
                if (jsonModle != null && jsonModle.getDatalist() != null)
                    setData(jsonModle);
            }
        });
    }

    private void setData(JsonModle jsonModle) {
        int size = jsonModle.getDatalist().size();
        List<DateInfo> dateInfoList = new ArrayList<>();//horizontal
        List<RoomInfo> roomInfoList = new ArrayList<>();// vertical
        //设置颜色
        List<List<OrderInfo>> ordersList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomName("" + i);
            roomInfoList.add(roomInfo);
            TreeMap<String, String[]> stringMap = jsonModle.getDatalist().get(i);
            List<OrderInfo> orderInfoList = new ArrayList<>();
            for (Map.Entry<String, String[]> entry : stringMap.entrySet()) {
                if (i <= 0) {
                    /*RoomInfo roomInfo = new RoomInfo();
                    roomInfo.setRoomName(entry.getKey());
                    roomInfoList.add(roomInfo);*/
                    DateInfo dateInfo = new DateInfo();
                    dateInfo.setDate(entry.getKey());
                    dateInfoList.add(dateInfo);
                }
                String[] value = entry.getValue();
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setGuestName(value[0]);
                orderInfo.setStatus(getStatus(value[1]));
                orderInfoList.add(orderInfo);

            }
            ordersList.add(orderInfoList);
        }
        //horizontal
        mScrollablePanelAdapter.setHoriInfoList(dateInfoList);
        // vertical
        mScrollablePanelAdapter.setVertInfoList(roomInfoList);

        mScrollablePanelAdapter.setOrdersList(ordersList);
        mScrollablepanel.notifyDataSetChanged();
        for (int i = 0; i < ordersList.size(); i++) {
            List<OrderInfo> orderInfos = ordersList.get(i);
            for (int j = 0; j < orderInfos.size(); j++) {
                Log.d("数据打印-------------", orderInfos.get(j).getGuestName());
            }
        }
    }

    private OrderInfo.Status getStatus(int i) {
        switch (i % 3) {
            case 0:
                return OrderInfo.Status.ALARM;
            case 1:
                return OrderInfo.Status.NORMAL;
            case 2:
                return OrderInfo.Status.WARN;
            default:
                return OrderInfo.Status.ALARM;

        }

    }

    private OrderInfo.Status getStatus(String i) {
        switch (i) {
            case "0":
                return OrderInfo.Status.ALARM;
            case "1":
                return OrderInfo.Status.NORMAL;
            case "2":
                return OrderInfo.Status.WARN;
            default:
                return OrderInfo.Status.DANGER;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.url1:
                mUrl = url + "/mobileinspect!ItemData?itemid=ITEMS0000000625";
                break;
            case R.id.url2:
                mUrl = url + "/mobileinspect!ItemData?itemid=ITEMS0000000626";
                break;
            case R.id.url3:
                mUrl = url + "/mobileinspect!ItemData?itemid=ITEMS0000000640";
                break;
            case R.id.url4:
                mUrl = url + "/mobileinspect!ItemData?itemid=ITEMS0000000655";
                break;
            case R.id.url5:
                mUrl = url + "/mobileinspect!ItemData?itemid=ITEMS0000000708";
                break;

        }
        initGetData();
    }
}
