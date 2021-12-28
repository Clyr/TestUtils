package com.clyr.testutils.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.clyr.base.bean.ListBean;
import com.clyr.base.interfaces.OnItemClickListener;
import com.clyr.testutils.R;
import com.clyr.testutils.adapter.GridViewAdapter;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.view.DialogHelper;
import com.clyr.view.activity.CameraActivity;
import com.clyr.view.activity.SimpleLookActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GridActivity extends BaseActivity implements OnItemClickListener, EasyPermissions.PermissionCallbacks {

    public List<ListBean> mList = new ArrayList<>();
    private GridViewAdapter mAdapter;

    public final int IMAGE_OPEN = 0;//打开图片标记
    public final int VIDEO_OPEN = 1;//打开视频标记
    private static final int PERMISSION_CAMERA_CODE = 1000;
    private static final int PERMISSION_STORAGE_CODE = 1001;

    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] storagePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
    }

    @Override
    protected void initView() {
        initBar();
        GridView mGridview = findViewById(R.id.gridview);
        ListBean listBean = new ListBean();
        listBean.setTag("out");
        listBean.setInfo("");
        mList.add(listBean);
        mGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new GridViewAdapter(this);
        mGridview.setAdapter(mAdapter);

    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case IMAGE_OPEN:
                if (resultCode == RESULT_OK) {
                    ListBean listBean = new ListBean();
                    listBean.setTag("image");
                    listBean.setInfo(getPath(data));
                    mList.add(listBean);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(this, getPath(data), Toast.LENGTH_SHORT).show();
                }
                break;
            case VIDEO_OPEN:
                if (resultCode == 1000) {
                    String mVideoUrl = data.getStringExtra("video");
                    ListBean listBean = new ListBean();
                    listBean.setTag("video");
                    listBean.setInfo(mVideoUrl);
                    mList.add(listBean);
                    mAdapter.notifyDataSetChanged();
                    break;
                }
        }
    }

    public String getPath(Intent data) {
        String[] imgPath = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data.getData(), imgPath, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected void openCamera() {
        File file = new File(this.getExternalCacheDir() + "/person");
        if (!file.exists())
            file.mkdirs();
        String pathName = file.getAbsolutePath() + "/photo.jpg";
        Uri photoUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
            photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(pathName));
        } else {
            photoUri = Uri.fromFile(new File(pathName));
        }

        if (photoUri != null) {
            //使用隐示的Intent，系统会找到与它对应的活动，即调用摄像头，并把它存储
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, 0);
        }
    }

    protected void openAlbum() {
        //Crop.pickImage(PhotoEditActivity.this);
        Intent intentablum = new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
        intentablum.addCategory(Intent.CATEGORY_OPENABLE);
        intentablum.setType("image/jpeg");
        startActivityForResult(intentablum, 0);

    }


    @Override
    public void onClick(int position, Object obj) {

        if (position == -1) {
            DialogHelper.cameraSelectDialog(this, (position1, obj1) -> {
                if (position1 == 0) {
                    if (EasyPermissions.hasPermissions(this, storagePermission)) {
                        openAlbum();
                    } else {
                        EasyPermissions.requestPermissions(this, "", PERMISSION_STORAGE_CODE, storagePermission);
                    }
                    Intent intent2 = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2, IMAGE_OPEN);
                } else {
                    Intent intent = new Intent(GridActivity.this, CameraActivity.class);
                    startActivityForResult(intent, VIDEO_OPEN);

                    /*Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);*/
                }
            });
        } else {
            ListBean listBean = (ListBean) obj;
            Intent intent = new Intent(GridActivity.this, SimpleLookActivity.class);
            intent.putExtra("TAG", listBean.getTag());
            intent.putExtra("INFO", listBean.getInfo());
            startActivity(intent);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (PERMISSION_CAMERA_CODE == requestCode) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog
                        .Builder(this)
                        .setTitle("权限申请")
                        .setRationale("此功能需要相机与存储权限，请在设置中修改权限，以便正常使用功能")
                        .setPositiveButton("去设置")
                        .setNegativeButton("取消")
                        .build()
                        .show();
            }
        }
    }

}