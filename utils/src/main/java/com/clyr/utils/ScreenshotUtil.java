package com.clyr.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author lzy of clyr
 * @date 2022/06/17
 */

public class ScreenshotUtil {
    /**
     * 截取指定activity显示内容
     * 需要读写权限
     *
     * @return
     */
    public static String saveScreenshotFromActivity(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        String file = saveImageToGallery(bitmap, activity);
        //回收资源
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return file;
    }

    /**
     * 截取指定View显示内容
     * 需要读写权限
     *
     * @return
     */
    public static String saveScreenshotFromView(View view, Activity context) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        String file = saveImageToGallery(bitmap, context);
        //回收资源
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return file;
    }

    /**
     * 保存图片至相册
     * 需要读写权限
     *
     * @return
     */

    @Nullable
    private static String saveImageToGallery(Bitmap bmp, Activity context) {
        File appDir = new File(getDCIM());
        MyLog.loge("appDir = " + appDir);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        MyLog.loge(file.getAbsolutePath());
        MyLog.loge(file.getPath());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + getDCIM())));
        return null;
    }

    /**
     * 获取相册路径
     */
    public static String getDCIM() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return "";
        }
        String path = Environment.getExternalStorageDirectory().getPath() + "/dcim/";
        if (new File(path).exists()) {
            return path;
        }
        path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/";
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return "";
            }
        }
        return path;
    }

    /**
     * 将Bitmap以指定格式保存到指定路径
     *
     * @param bitmap
     * @param name
     */
    public static void saveBitmap(Bitmap bitmap, String name, Bitmap.CompressFormat format) {
        // 创建一个位于SD卡上的文件
        File file = new File(Environment.getExternalStorageDirectory(),
                name);
        FileOutputStream out = null;
        try {
            // 打开指定文件输出流
            out = new FileOutputStream(file);
            // 将位图输出到指定文件
            bitmap.compress(format, 100,
                    out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String saveScreenImage(View v) {
        try {

            String filePath = getDCIM() + System.currentTimeMillis() + ".png";

            Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);
            v.draw(canvas);

            try {
                FileOutputStream fos = new FileOutputStream(filePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                ToastUtils.showShort("截图保存成功");
                fos.close();
                return filePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //throw new InvalidParameterException();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void prohibitScreenShot(@NonNull Activity activity) {
        // 禁止截屏
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }
}
