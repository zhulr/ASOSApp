package com.asosapp.phone.activity;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.asosapp.phone.R;
import com.asosapp.phone.utils.DatabaseHelper;

/**
 * Created by ASOS_zhulr on 2016/1/4.
 */
public class SQLiteActivity extends Activity {
    /** Called when the activity is first created. */
    private Button createDatabaseButton = null;
    private Button updateDatabaseButton = null;
    private Button insertButton = null;
    private Button updateButton = null;
    private Button selectButton = null;
    private Button deleteButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datatest);
        // 根据控件id获得相应的控件对象
        createDatabaseButton = (Button) findViewById(R.id.createDatabase);
        updateDatabaseButton = (Button) findViewById(R.id.updateDatabase);
        insertButton = (Button) findViewById(R.id.insert);
        updateButton = (Button) findViewById(R.id.update);
        selectButton = (Button) findViewById(R.id.select);
        deleteButton = (Button) findViewById(R.id.delete);
        // 为按钮设置监听器
        createDatabaseButton
                .setOnClickListener(new CreateDatabaseOnClickListener());
        updateDatabaseButton
                .setOnClickListener(new UpdateDatabaseOnClickListener());
        insertButton.setOnClickListener(new InsertOnClickListener());
        updateButton.setOnClickListener(new UpdateOnClickListener());
        selectButton.setOnClickListener(new SelectOnClickListener());
        deleteButton.setOnClickListener(new DeleteOnClickListener());
    }

    // createDatabaseButton点击事件监听器
    class CreateDatabaseOnClickListener implements OnClickListener {
        public void onClick(View v) {
            // 创建了一个DatabaseHelper对象，只执行这句话是不会创建或打开连接的
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,
                    "test_yangyz_db");
            // 只有调用了DatabaseHelper的getWritableDatabase()方法或者getReadableDatabase()方法之后，才会创建或打开一个连接
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        }
    }

    // updateDatabaseButton点击事件监听器
    class UpdateDatabaseOnClickListener implements OnClickListener {
        public void onClick(View v) {
            // TODO Auto-generated method stub
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,
                    "test_yangyz_db", 2);
            // 得到一个只读的SQLiteDatabase对象
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        }

    }

    // insertButton点击事件监听器
    class InsertOnClickListener implements OnClickListener {
        public void onClick(View v) {
            // 创建ContentValues对象
            ContentValues values = new ContentValues();
            // 向该对象中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
            values.put("id", 1);
            values.put("name", "yangyz");
            // 创建DatabaseHelper对象
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,
                    "test_yangyz_db", 2);
            // 得到一个可写的SQLiteDatabase对象
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            // 调用insert方法，就可以将数据插入到数据库当中
            // 第一个参数:表名称
            // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
            // 第三个参数：ContentValues对象
            sqliteDatabase.insert("user", null, values);
        }
    }

    // updateButton点击事件监听器
    class UpdateOnClickListener implements OnClickListener {
        public void onClick(View v) {
            // 创建一个DatabaseHelper对象
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,
                    "test_yangyz_db", 2);
            // 得到一个可写的SQLiteDatabase对象
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            // 创建一个ContentValues对象
            ContentValues values = new ContentValues();
            values.put("name", "zhangsan");
            // 调用update方法
            // 第一个参数String：表名
            // 第二个参数ContentValues：ContentValues对象
            // 第三个参数String：where字句，相当于sql语句where后面的语句，？号是占位符
            // 第四个参数String[]：占位符的值
            sqliteDatabase.update("user", values, "id=?", new String[] { "1" });
            System.out.println("-----------update------------");
        }
    }

    // selectButton点击事件监听器
    class SelectOnClickListener implements OnClickListener {
        public void onClick(View v) {
            String id = null;
            String name = null;
            //创建DatabaseHelper对象
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,
                    "test_yangyz_db", 2);
            // 得到一个只读的SQLiteDatabase对象
            SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
            // 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象
            // 第一个参数String：表名
            // 第二个参数String[]:要查询的列名
            // 第三个参数String：查询条件
            // 第四个参数String[]：查询条件的参数
            // 第五个参数String:对查询的结果进行分组
            // 第六个参数String：对分组的结果进行限制
            // 第七个参数String：对查询的结果进行排序
            Cursor cursor = sqliteDatabase.query("user", new String[] { "id",
                    "name" }, "id=?", new String[] { "1" }, null, null, null);
            // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
            while (cursor.moveToNext()) {
                id = cursor.getString(cursor.getColumnIndex("id"));
                name = cursor.getString(cursor.getColumnIndex("name"));
            }
            System.out.println("-------------select------------");
            System.out.println("id: "+id);
            System.out.println("name: "+name);
        }
    }

    // deleteButton点击事件监听器
    class DeleteOnClickListener implements OnClickListener {
        public void onClick(View v) {
            //创建DatabaseHelper对象
            DatabaseHelper dbHelper = new DatabaseHelper(SQLiteActivity.this,"test_yangyz_db",2);
            //获得可写的SQLiteDatabase对象
            SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
            //调用SQLiteDatabase对象的delete方法进行删除操作
            //第一个参数String：表名
            //第二个参数String：条件语句
            //第三个参数String[]：条件值
            sqliteDatabase.delete("user", "id=?", new String[]{"1"});
            System.out.println("----------delete----------");
        }
    }
}
