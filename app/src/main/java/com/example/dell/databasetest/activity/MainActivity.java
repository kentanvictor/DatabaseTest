package com.example.dell.databasetest.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dell.databasetest.MyDatabaseHelper;
import com.example.dell.databasetest.R;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null ,2);//构建MyDatabaseHelper对象
        //上面对应的是，创建一个名字为"BookStore.db"并且版本号为1的数据库文件
        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        //创建数据库
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("name","The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price",16.96);
                db.insert("Book",null,values);//插入第一条数据
                values.clear();
                //开始组装第二条数据
                values.put("name","The Lost Symbol");
                values.put("author","Dan Brown");
                values.put("pages",510);
                values.put("price",19.95);
                db.insert("Book",null,values);//插入第二条数据
            }
        });
        //添加数据
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",10.99);//只想更新价格这一数据
                db.update("Book",values,"name = ?",new String[] {"The Da Vinci Code"});
                /**
                * 第三个参数对应的是SQL语句的where部分
                * 表示更新所有name等于?的行，而?是一个占位符
                * 可以通过第四个参数提供的一个字符串数组为第三个参数中的每个占位符指定相应的内容
                * */
            }
        });
        //更新数据
        Button deleteButton = (Button) findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book","page > ?",new String[] {"500"});
            }
        });
        //删除数据
    }
}
