package com.clyr.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clyr.utils.IOSave;


public class SqliteActivity extends Activity {
    //声明各个按钮
    private Button createBtn;
    private Button insertBtn;
    private Button updateBtn;
    private Button queryBtn;
    private Button deleteBtn;
    private Button ModifyBtn;
    private TextView mTextView;
    private String mStu_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        //调用creatView方法
        creatView();
        //setListener方法
        setListener();
    }

    //通过findViewById获得Button对象的方法
    private void creatView() {
        mTextView = (TextView) findViewById(R.id.text);
        createBtn = (Button) findViewById(R.id.createDatabase);
        updateBtn = (Button) findViewById(R.id.updateDatabase);
        insertBtn = (Button) findViewById(R.id.insert);
        ModifyBtn = (Button) findViewById(R.id.update);
        queryBtn = (Button) findViewById(R.id.query);
        deleteBtn = (Button) findViewById(R.id.delete);
    }

    //为按钮注册监听的方法
    private void setListener() {
        createBtn.setOnClickListener(new CreateListener());
        updateBtn.setOnClickListener(new UpdateListener());
        insertBtn.setOnClickListener(new InsertListener());
        ModifyBtn.setOnClickListener(new ModifyListener());
        queryBtn.setOnClickListener(new QueryListener());
        deleteBtn.setOnClickListener(new DeleteListener());
    }

    //创建数据库的方法
    class CreateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //创建StuDBHelper对象
            mStu_db = "stu_db";
            IOSave.StuDBHelper dbHelper = new IOSave.StuDBHelper(SqliteActivity.this, mStu_db, null, 2);
            //得到一个可读的SQLiteDatabase对象
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Toast.makeText(SqliteActivity.this,"创建数据库",Toast.LENGTH_SHORT).show();
        }
    }

    //更新数据库的方法
    class UpdateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // 数据库版本的更新,由原来的1变为2
            IOSave.StuDBHelper dbHelper = new IOSave.StuDBHelper(SqliteActivity.this, "stu_db", null, 2);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Toast.makeText(SqliteActivity.this,"更新数据库",Toast.LENGTH_SHORT).show();
        }
    }

    //插入数据的方法
    class InsertListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            IOSave.StuDBHelper dbHelper = new IOSave.StuDBHelper(SqliteActivity.this, "stu_db", null, 2);
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
            Toast.makeText(SqliteActivity.this,"插入数据",Toast.LENGTH_SHORT).show();
        }
    }

    //查询数据的方法
    @SuppressLint("Range")
    class QueryListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            IOSave.StuDBHelper dbHelper = new IOSave.StuDBHelper(SqliteActivity.this, "stu_db", null, 2);
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
                String name = cursor.getString(cursor.getColumnIndex("sname"));
                String age = cursor.getString(cursor.getColumnIndex("sage"));
                String sex = cursor.getString(cursor.getColumnIndex("ssex"));
                System.out.println("query------->" + "姓名：" + name + " " + "年龄：" + age + " " + "性别：" + sex);
            }
            //关闭数据库
            db.close();
            Toast.makeText(SqliteActivity.this,"查询数据",Toast.LENGTH_SHORT).show();
        }
    }

    //修改数据的方法
    class ModifyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            IOSave.StuDBHelper dbHelper = new IOSave.StuDBHelper(SqliteActivity.this, "stu_db", null, 2);
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
            Toast.makeText(SqliteActivity.this,"修改数据",Toast.LENGTH_SHORT).show();
        }
    }

    //删除数据的方法
    class DeleteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            IOSave.StuDBHelper dbHelper = new IOSave.StuDBHelper(SqliteActivity.this, "stu_db", null, 2);
            //得到一个可写的数据库
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String whereClauses = "id=?";
            String[] whereArgs = {"1"};
            //调用delete方法，删除数据
            db.delete("stu_table", whereClauses, whereArgs);
//            db.execSQL("delete from stu_table");//删除表中所有数据
//            db.delete("stu_table", "id=?", new String[]{"1"});
            Toast.makeText(SqliteActivity.this,"删除数据",Toast.LENGTH_SHORT).show();
        }
    }
}
