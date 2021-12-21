package com.clyr.three_sdk.baidumapsdk.demo.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.favorite.FavoriteManager;
import com.baidu.mapapi.favorite.FavoritePoiInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示如何使用本地点收藏功能
 */
public class FavoriteDemo extends AppCompatActivity implements OnMapLongClickListener, OnMarkerClickListener,
        OnMapClickListener {

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    // 界面控件相关
    private EditText mEditLocation;
    private EditText mEditName;
    private View mPopView;
    private View mModify;
    private EditText mEidtfyName;
    // 保存点中的点id
    private String currentID;
    // 现实marker的图标
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    private List<Marker> markers = new ArrayList<Marker>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLongClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
        mBaiduMap.setOnMapClickListener(this);
        // 初始化收藏夹
        FavoriteManager.getInstance().init();
        // 初始化UI
        initUI();
    }

    public void initUI() {
        mEditLocation = (EditText) findViewById(R.id.pt);
        mEditName = (EditText) findViewById(R.id.name);
        LayoutInflater mInflater = getLayoutInflater();
        mPopView = (View) mInflater.inflate(R.layout.activity_favorite_infowindow, null, false);
    }

    /**
     * 添加收藏点
     */
    public void saveClick(View v) {
        if (mEditName.getText().toString() == null || mEditName.getText().toString().equals("")) {
            Toast.makeText(FavoriteDemo.this, "名称必填", Toast.LENGTH_LONG).show();
            return;
        }
        if (mEditLocation.getText().toString() == null || mEditLocation.getText().toString().equals("")) {
            Toast.makeText(FavoriteDemo.this, "坐标点必填", Toast.LENGTH_LONG).show();
            return;
        }

        FavoritePoiInfo info = new FavoritePoiInfo();
        info.poiName(mEditName.getText().toString());

        LatLng latLng;
        try {
            String strLatLng = mEditLocation.getText().toString();
            String lat = strLatLng.substring(0, strLatLng.indexOf(","));
            String lng = strLatLng.substring(strLatLng.indexOf(",") + 1);
            latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            info.pt(latLng);
            if (FavoriteManager.getInstance().add(info) == 1) {
                Toast.makeText(FavoriteDemo.this, "添加成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(FavoriteDemo.this, "添加失败", Toast.LENGTH_LONG).show();
                return;
            }

        } catch (Exception e) {
            Toast.makeText(FavoriteDemo.this, "坐标解析错误", Toast.LENGTH_LONG).show();
            return;
        }

        // 在地图上更新当前最新添加的点
        mBaiduMap.clear();
        List<FavoritePoiInfo> list = FavoriteManager.getInstance().getAllFavPois();
        if(null == list || list.size() == 0){
            return;
        }
        MarkerOptions option = new MarkerOptions().icon(bitmapA).position(list.get(0).getPt());
        Bundle bundle = new Bundle();
        bundle.putString("id", list.get(0).getID());
        option.extraInfo(bundle);
        Marker currentMarker = (Marker) mBaiduMap.addOverlay(option);
        markers.add(currentMarker);
    }

    /**
     * 修改收藏点
     */
    public void modifyClick(View v) {
        mBaiduMap.hideInfoWindow();
        // 弹框修改
        LayoutInflater mInflater = getLayoutInflater();
        mModify = (LinearLayout) mInflater.inflate(R.layout.activity_favorite_alert, null);
        mEidtfyName = (EditText) mModify.findViewById(R.id.modifyedittext);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(mModify);

        // 避免点未收藏时，点击修改框，造成空指针异常
        if (null == currentID) {
            Toast.makeText(FavoriteDemo.this, "该点未收藏，无法修改", Toast.LENGTH_LONG).show();
            return;
        }

        if (null == FavoriteManager.getInstance().getFavPoi(currentID)) {
            Toast.makeText(FavoriteDemo.this, "获取Poi失败", Toast.LENGTH_LONG).show();
            return;
        }

        String oldName = FavoriteManager.getInstance().getFavPoi(currentID).getPoiName();
        mEidtfyName.setText(oldName);
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = mEidtfyName.getText().toString();
                if (newName != null && !newName.equals("")) {
                    // modify
                    FavoritePoiInfo info = FavoriteManager.getInstance().getFavPoi(currentID);
                    info.poiName(newName);
                    if (FavoriteManager.getInstance().updateFavPoi(currentID, info)) {
                        Toast.makeText(FavoriteDemo.this, "修改成功", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FavoriteDemo.this, "名称不能为空，修改失败", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }


        });

        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 删除一个指定点
     */
    public void deleteOneClick(View v) {
        // 避免点未收藏时，点击删除选项框，造成空指针异常
        if (null == currentID) {
            Toast.makeText(FavoriteDemo.this, "该点未收藏，无法进行删除操作", Toast.LENGTH_LONG).show();
            return;
        }

        if (FavoriteManager.getInstance().deleteFavPoi(currentID)) {
            Toast.makeText(FavoriteDemo.this, "删除点成功", Toast.LENGTH_LONG).show();
            if (markers != null) {
                for (int i = 0; i < markers.size(); i++) {
                    String id = markers.get(i).getExtraInfo().getString("id");
                    if (id != null && id.equals(currentID)) {
                        markers.get(i).remove();
                        markers.remove(i);
                        mBaiduMap.hideInfoWindow();
                        break;
                    }
                }
            }
        } else {
            Toast.makeText(FavoriteDemo.this, "删除点失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取全部收藏点
     */
    public void getAllClick(View v) {
        mBaiduMap.clear();
        List<FavoritePoiInfo> list = FavoriteManager.getInstance().getAllFavPois();
        if (list == null || list.size() == 0) {
            Toast.makeText(FavoriteDemo.this, "没有收藏点", Toast.LENGTH_LONG).show();
            return;
        }
        // 绘制在地图
        markers.clear();
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions option = new MarkerOptions().icon(bitmapA).position(list.get(i).getPt());
            Bundle b = new Bundle();
            b.putString("id", list.get(i).getID());
            option.extraInfo(b);
            markers.add((Marker) mBaiduMap.addOverlay(option));
        }
    }

    /**
     * 删除全部点
     */
    public void deleteAllClick(View v) {
        if (FavoriteManager.getInstance().clearAllFavPois()) {
            Toast.makeText(FavoriteDemo.this, "全部删除成功", Toast.LENGTH_LONG).show();
            mBaiduMap.clear();
            mBaiduMap.hideInfoWindow();
        } else {
            Toast.makeText(FavoriteDemo.this, "全部删除失败", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 释放收藏夹功能资源
        FavoriteManager.getInstance().destroy();
        bitmapA.recycle();
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        mBaiduMap = null;
        super.onDestroy();
    }


    @Override
    public void onMapLongClick(LatLng point) {
        mEditLocation.setText(String.valueOf(point.latitude) + "," + String.valueOf(point.longitude));
        MarkerOptions ooA = new MarkerOptions().position(point).icon(bitmapA);
        mBaiduMap.clear();
        mBaiduMap.addOverlay(ooA);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mBaiduMap.hideInfoWindow();
        if (marker == null) {
            return false;
        }

        InfoWindow mInfoWindow = new InfoWindow(mPopView, marker.getPosition(), -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker.getPosition());
        mBaiduMap.setMapStatus(update);

        if (null == marker.getExtraInfo()) {
            return false;
        }

        currentID = marker.getExtraInfo().getString("id");
        return true;
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public void onMapPoiClick(MapPoi poi) {

    }
}
