package com.example.dell.databasetest.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null ,5);//构建MyDatabaseHelper对象
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
                db.delete("Book","pages > ?",new String[] {"500"});//第二、第三个参数来指定仅删除那些页数超过500页的书
            }
        });
        //删除数据
        Button queryButton = (Button) findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //查询Book表中所有的数据
                Cursor cursor = db.query("Book",null,null,null,null,null,null);//利用query()方法查询数据
                if(cursor.moveToFirst())
                    /*
                    * 调用moveToFirst()方法将数据的指针移动到第一行
                    * */
                {
                    do {
                        //遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        /*
                        * 通过Cursor的getColumnIndex()方法获取到某一列在表中对应的位置索引
                        * 然后将这个索引传入到相应的取值方法中，就可以得到数据库中读取到的数据了
                        * */
                        Log.d("MainActivity","book name is " + name);
                        Log.d("MainActivity","book author is " + author);
                        Log.d("MainActivity","book pages is " + pages);
                        Log.d("MainActivity","book price is " + price);
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }
        });
        //对数据进行查询工作
    }
}
