package com.clyr.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSONObject;
import com.clyr.utils.utilshelper.ACache;
import com.clyr.utils.utilshelper.DBHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by M S I of clyr on 2019/11/14.
 * IO存储
 * 包含 SharedPreferences、Sqlite、ACache
 * 使用前 调用init()
 */
public class IOSave {
    @SuppressLint("StaticFieldLeak")
    private static IOSave ioSave;

    /**
     * 初始化IOSave
     *
     * @return ioSave
     */
    public static IOSave init() {
        if (ioSave == null) {
            synchronized (IOSave.class) {
                if (ioSave == null) {
                    ioSave = new IOSave();
                }
            }
        }
        return ioSave;
    }

    /**
     * SharedPreferences
     */
    private final static String name = "SPConfig";
    private final static int mode = Context.MODE_PRIVATE;

    /**
     * 保存
     *
     * @param context ...
     * @param key key
     * @param value value
     */
    public void spSaveBoolean(Context context, String key, boolean value) {
        if (context == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public void spSaveInt(Context context, String key, int value) {
        if (context == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void spSaveString(Context context, String key, String value) {
        if (context == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 获取
     *
     * @param context ...
     * @param key key
     * @param defValue defValue
     * @return
     */
    public boolean spGetBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getBoolean(key, defValue);
    }

    public int spGetInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getInt(key, defValue);
    }

    public String spGetString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getString(key, defValue);
    }

    /**
     * ACache
     */
    private static ACache mCache;

    public void acSaveString(Context context, String name, String str) {
        if (mCache == null) {
            mCache = ACache.get(context);
        }
        mCache.put(name, str);
    }

    public String acGetString(Context context, String name) {
        if (mCache == null) {
            mCache = ACache.get(context);
        }
        return mCache.getAsString(name);
    }

    public void acClear(Context context, String name) {
        if (mCache == null) {
            mCache = ACache.get(context);
        }
        mCache.remove(name);
    }

    public void acSaveJsonString(Context context, String name, List list) {
        String string = JSONObject.toJSONString(list);
        acSaveString(context, name, string);
    }

    /**
     * Sqlite 数据库
     */
    private static String mSql = "sql_db";
    private static Context mContext = null; //todo 通过Applaction获取context

    /**
     * 创建数据库
     */
    public void createDB() {
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可读的SQLiteDatabase对象
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Toast.makeText(mContext, "创建数据库", Toast.LENGTH_SHORT).show();
    }

    /**
     * 插入数据
     */
    public void insertDB(String dbkey, String dbvalue) {
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //生成ContentValues对象 //key:列名，value:想插入的值
        ContentValues cv = new ContentValues();
        //往ContentValues对象存放数据，键-值对模式
        cv.put("dbkey", dbkey);//建议使用用户名和特殊字符拼接
        cv.put("dbvalue", dbvalue);
        //调用insert方法，将数据插入数据库
        db.insert("date_table", null, cv);
        //关闭数据库
        db.close();
        Toast.makeText(mContext, "插入数据", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查询数据
     */
    public void queryDB(String string) {
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //参数1：表名
        //参数2：要想显示的列
        //参数3：where子句
        //参数4：where子句对应的条件值
        //参数5：分组方式
        //参数6：having条件
        //参数7：排序方式
        Cursor cursor = db.query("date_table", new String[]{"dbkey", "dbvalue"}, "dbkey=?", new String[]{string}, null, null, null);
        while (cursor.moveToNext()) {//设置只有一条
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("dbvalue"));
        }
        //关闭数据库
        db.close();
        Toast.makeText(mContext, "查询数据", Toast.LENGTH_SHORT).show();
    }

    /**
     * 修改数据
     */
    public void updateDB(String dbkey, String dbvalue) {
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbkey, dbvalue);
        //where 子句 "?"是占位符号，对应后面的"1",
        String whereClause = "dbkey=?";
        String[] whereArgs = {String.valueOf(1)};
        //参数1 是要更新的表名
        //参数2 是一个ContentValeus对象
        //参数3 是where子句
        db.update("date_table", cv, whereClause, whereArgs);
        Toast.makeText(mContext, "修改数据", Toast.LENGTH_SHORT).show();
    }

    /**
     * 删除数据
     */
    public void deleteDB() {
        DBHelper dbHelper = new DBHelper(mContext, mSql, null, 2);
        //得到一个可写的数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String whereClauses = "dbkey=?";
        String[] whereArgs = {"1"};
        //调用delete方法，删除数据
        db.delete("date_table", whereClauses, whereArgs);
//            db.execSQL("delete from date_table");//删除表中所有数据
//            db.delete("date_table", "id=?", new String[]{"1"});
        Toast.makeText(mContext, "删除数据", Toast.LENGTH_SHORT).show();
    }

    private String mStu_db;
    Context context;
    //创建数据库的方法
    class CreateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //创建StuDBHelper对象
            mStu_db = "stu_db";
            StuDBHelper dbHelper = new StuDBHelper(context, mStu_db, null, 2);
            //得到一个可读的SQLiteDatabase对象
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Toast.makeText(context,"创建数据库",Toast.LENGTH_SHORT).show();
        }
    }

    //更新数据库的方法
    class UpdateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // 数据库版本的更新,由原来的1变为2
            StuDBHelper dbHelper = new StuDBHelper(context, "stu_db", null, 2);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Toast.makeText(context,"更新数据库",Toast.LENGTH_SHORT).show();
        }
    }

    //插入数据的方法
    class InsertListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            StuDBHelper dbHelper = new StuDBHelper(context, "stu_db", null, 2);
            //得到一个可写的数据库
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            //生成ContentValues对象 //key:列名，value:想插入的值
            ContentValues cv = new ContentValues();
            //往ContentValues对象存放数据，键-值对模式
            cv.put("id", 1);
            cv.put("sname", "xiaoming");
            cv.put("sage", 21);
            cv.put("ssex", "male");
            //调用insert方法，将数据插入数据库
            db.insert("stu_table", null, cv);
            //关闭数据库
            db.close();
            Toast.makeText(context,"插入数据",Toast.LENGTH_SHORT).show();
        }
    }

    //查询数据的方法
    class QueryListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            StuDBHelper dbHelper = new StuDBHelper(context, "stu_db", null, 2);
            //得到一个可写的数据库
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //参数1：表名
            //参数2：要想显示的列
            //参数3：where子句
            //参数4：where子句对应的条件值
            //参数5：分组方式
            //参数6：having条件
            //参数7：排序方式
            Cursor cursor = db.query("stu_table", new String[]{"id", "sname", "sage", "ssex"}, "id=?", new String[]{"1"}, null, null, null);
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("sname"));
                @SuppressLint("Range") String age = cursor.getString(cursor.getColumnIndex("sage"));
                @SuppressLint("Range") String sex = cursor.getString(cursor.getColumnIndex("ssex"));
                System.out.println("query------->" + "姓名：" + name + " " + "年龄：" + age + " " + "性别：" + sex);
            }
            //关闭数据库
            db.close();
            Toast.makeText(context,"查询数据",Toast.LENGTH_SHORT).show();
        }
    }

    //修改数据的方法
    class ModifyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            StuDBHelper dbHelper = new StuDBHelper(context, "stu_db", null, 2);
            //得到一个可写的数据库
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("sage", "23");
            //where 子句 "?"是占位符号，对应后面的"1",
            String whereClause = "id=?";
            String[] whereArgs = {String.valueOf(1)};
            //参数1 是要更新的表名
            //参数2 是一个ContentValeus对象
            //参数3 是where子句
            db.update("stu_table", cv, whereClause, whereArgs);
            Toast.makeText(context,"修改数据",Toast.LENGTH_SHORT).show();
        }
    }

    //删除数据的方法
    class DeleteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            StuDBHelper dbHelper = new StuDBHelper(context, "stu_db", null, 2);
            //得到一个可写的数据库
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String whereClauses = "id=?";
            String[] whereArgs = {"1"};
            //调用delete方法，删除数据
            db.delete("stu_table", whereClauses, whereArgs);
//            db.execSQL("delete from stu_table");//删除表中所有数据
//            db.delete("stu_table", "id=?", new String[]{"1"});
            Toast.makeText(context,"删除数据",Toast.LENGTH_SHORT).show();
        }
    }
    public class StuDBHelper extends SQLiteOpenHelper {

        private static final String TAG = "TestSQLite";
        public static final int VERSION = 2;

        //必须要有构造函数
        public StuDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                           int version) {
            super(context, name, factory, version);
        }

        // 当第一次创建数据库的时候，调用该方法
        public void onCreate(SQLiteDatabase db) {
            String sql = "create table stu_table(id int,sname varchar(20),sage int,ssex varchar(10))";
            //输出创建数据库的日志信息
            Log.i(TAG, "create Database------------->");
            //execSQL函数用于执行SQL语句
            db.execSQL(sql);
        }

        //当更新数据库的时候执行该方法
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //输出更新数据库的日志信息
            Log.i(TAG, "update Database------------->");
        }
    }



    /**
     * 日志保存
     *
     * @param bfile    文件的byte流
     * @param filePath 文件要保存的路径
     * @param fileName 文件保存的名字
     */
    /*try {
        MyLog.saveFile(token.getBytes(),
                Environment.getExternalStorageDirectory().getPath(),
                "hwToken.txt");
    } catch (Exception e) {
        e.printStackTrace();
    }*/
    public static void saveFile(byte[] bfile, String filePath, String fileName) {

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        File file = null;
        try {
//            Environment.getExternalStorageState()
            filePath = filePath == null ? Environment.getExternalStorageDirectory().getPath() : filePath;
            //通过创建对应路径的下是否有相应的文件夹。
            File dir = new File(filePath);
            if (!dir.exists()) {// 判断文件目录是否存在
                //如果文件存在则删除已存在的文件夹。
                dir.mkdirs();
            }

            //如果文件存在则删除文件
            file = new File(filePath, fileName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            //把需要保存的文件保存到SD卡中
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }
    public static class DownloadAPK {
        private static String downloadPath;
        private long mTaskId;
        Context mContext;
        String versionName1;

        public DownloadAPK(Context context) {
            mContext = context;
        }

        // 使用系统下载器下载
        public void downloadAPK(String versionUrl, String versionName) {
            // 创建下载任务
            versionName1 = versionName;
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(versionUrl));
            request.setAllowedOverRoaming(false);// 漫游网络是否可以下载

            // 设置文件类型，可以在下载结束后自动打开该文件
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                    .getFileExtensionFromUrl(versionUrl));
            request.setMimeType(mimeString);

            // 在通知栏中显示，默认就是显示的
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setVisibleInDownloadsUi(true);

            // sdcard的目录下的download文件夹，必须设置
            request.setDestinationInExternalPublicDir("/download/", versionName);
            // request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

            downloadManager = (DownloadManager) mContext
                    .getSystemService(Context.DOWNLOAD_SERVICE);
            // 加入下载队列后会给该任务返回一个long型的id，
            // 通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
            mTaskId = downloadManager.enqueue(request);
            // 注册广播接收者，监听下载状态
            mContext.registerReceiver(receiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }

        // 接下来是广播接收器

        // 广播接受者，接收下载状态
        private BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkDownloadStatus();// 检查下载状态
            }
        };
        private DownloadManager downloadManager;

        // 检查下载状态
        private void checkDownloadStatus() {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mTaskId);// 筛选下载任务，传入任务ID，可变参数
            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                @SuppressLint("Range") int status = c.getInt(c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_PAUSED:
                        Log.d("1111", ">>>下载暂停");
                    case DownloadManager.STATUS_PENDING:
                        Log.d("1111", ">>>下载延迟");
                    case DownloadManager.STATUS_RUNNING:
                        Log.d("1111", ">>>正在下载");

                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        Log.d("1111", ">>>下载完成");
                        // 下载完成安装APK
                        downloadPath = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                                + File.separator + versionName1;
                        installAPK(downloadPath);
                        break;
                    case DownloadManager.STATUS_FAILED:
                        Log.d("1111", ">>>下载失败");
                        break;
                }
            }

        }

        // 下载到本地后执行安装
        protected void installAPK(String apkFilePath) {

            try {
                File apkfile = new File(apkFilePath);
        /*try {
			if (!file.exists())
                return;
			Intent intent = new Intent(Intent.ACTION_VIEW);
//			Uri uri = Uri.parse("file://" + file.toString());
			Uri uri = getUriForFile(mContext,file);
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
			// 在服务中开启activity必须设置flag
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			SDToast.showToast("手机兼容性问题您可以去以下位置安装应用"+file.getAbsolutePath(), Toast.LENGTH_LONG);
		}*/

                if (!apkfile.exists()) {
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    String authority = "com.matrix.myapplication.fileProvider";
                    Uri contentUri = FileProvider.getUriForFile(mContext, authority, new File(apkFilePath));
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(apkFilePath)), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showLong(mContext,"打开APK失败::>_<::，请手动安装,安装包位于下载器中"+apkFilePath);
            }


        }

        public static Uri getUriForFile(Context context, File file) {
//		return FileProvider.getUriForFile(context, GB.getCallBack().getApplicationId(), file);
            return FileProvider.getUriForFile(context, BuildConfig.LIBRARY_PACKAGE_NAME, file);
        }
    }
    public class DownloadObserver extends ContentObserver {
        private Handler mHandler;
        private Context mContext;
        private int progress;
        private DownloadManager mDownloadManager;
        private DownloadManager.Query query;
        private Cursor cursor;
        @SuppressLint("NewApi")
        public DownloadObserver(Handler handler, Context context, long downId) {
            super(handler);
            // TODO Auto-generated constructor stub
            this.mHandler = handler;
            this.mContext = context;
            mDownloadManager =  (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            query = new DownloadManager.Query().setFilterById(downId);
        }
        @SuppressLint({"NewApi", "Range"})
        @Override
        public void onChange(boolean selfChange) {
            // 每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
            super.onChange(selfChange);
            //
            boolean downloading = true;
            while (downloading) {
                cursor  = mDownloadManager.query(query);
                cursor.moveToFirst();
                int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                progress = (int) ((bytes_downloaded * 100) / bytes_total);
                cursor.close();
                mHandler.sendEmptyMessageDelayed(progress, 100);
                if (cursor.getInt(
                        cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;
                }
            }
        }
    }
}
