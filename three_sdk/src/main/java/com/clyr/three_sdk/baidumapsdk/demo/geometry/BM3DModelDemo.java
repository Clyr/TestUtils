package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BM3DModel;
import com.baidu.mapapi.map.BM3DModelOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



public class BM3DModelDemo extends AppCompatActivity implements View.OnClickListener {

    private final static String tag = BM3DModelDemo.class.getSimpleName();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BM3DModel mBM3DModel;
    private String parentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3d_model_demo);
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        Button add3DModelBtn = findViewById(R.id.add_3d_model);
        Button remove3DModelBtn = findViewById(R.id.remove_3d_model);
        add3DModelBtn.setOnClickListener(this);
        remove3DModelBtn.setOnClickListener(this);

        parentPath = getFilesDir().getAbsolutePath();
        copyFilesAssets(this,"model3D",parentPath + "/model3D/");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_3d_model) {
            remove();
            BM3DModelOptions bm3DModelOptions = new BM3DModelOptions();
            bm3DModelOptions.setModelPath(parentPath + "/model3D");
            bm3DModelOptions.setModelName("among_us");
            bm3DModelOptions.setScale(50.0f);
            bm3DModelOptions.setPosition(new LatLng(39.915119,116.403963));
            mBM3DModel = (BM3DModel) mBaiduMap.addOverlay(bm3DModelOptions);
        } else if (v.getId() == R.id.remove_3d_model) {
            remove();
        }
    }

    private void remove() {
        if (null != mBM3DModel) {
            mBM3DModel.remove();
        }
    }

    /**
     *  从assets目录中复制整个文件夹内容
     */
    public void copyFilesAssets(Context context,String oldPath,String newPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            for (int i = 0; i < fileNames.length; i++) {
                String fileNameStr = fileNames[i];
                Log.e(tag, "copyFilesFassets: " + fileNameStr);
            }
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesAssets(context,oldPath + File.separator + fileName,newPath+File.separator+fileName);
                }
            } else { // 如果是文件
                is = context.getAssets().open(oldPath);
                fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            Log.e(tag, "Copy custom style file failed", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.e(tag, "Close stream failed", e);
            }
        }
    }
}