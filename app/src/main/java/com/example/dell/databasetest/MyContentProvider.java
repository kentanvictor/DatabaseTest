package com.example.dell.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final int BOOK_DIR = 0;//访问Book表中所有数据
    public static final int BOOK_ITEM = 1;//访问Book表中单例数据
    public static final int CATEGORY_DIR = 2;//访问Category表中所有数据
    public static final int CATEGORY_ITEM = 3;//访问Category表中单例数据
    //定义4个常量
    public static final String AUTHORITY = "com.example.dell.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_ITEM);
    }//在静态代码块中进行初始化的操作
    //将期望匹配的几种URI格式匹配进去

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        switch (uriMatcher.match(uri))
        {
            case BOOK_DIR:
                deletedRows = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Book","id = ?",new String[] {bookId});
                break;
            case CATEGORY_DIR:
                deletedRows = db.delete("Category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deletedRows = db.delete("Category","id = ?",new String[] {categoryId});
                break;
            default:
                break;
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri))
        {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.dell.databasetest.privider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.dell.databasetest.privider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.dell.databasetest.privider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.dell.databasetest.privider.category";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    /**
     * 注意：insert()方法要求返回一个能够表示这条新增数据的URI
     * 所以我们还需要调用URI.parse()方法来将一个内容URI解析为URI对象
     * 当然
     * 这个内容URI是以新增数据的id结尾的
    * */
    {
        //添加数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();//获取SQLiteDatabase的实例
        Uri uriReturn = null;
        switch (uriMatcher.match(uri))//利用URI参数来判断用户想要往哪一张表里面添加数据
        {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book",null,values);//然后再调用SQLiteDatabase的insert()方法进行添加数据
                uriReturn = Uri.parse("content://"+AUTHORITY+"/Book/"+newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert("Category",null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/Category/"+newCategoryId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new MyDatabaseHelper(getContext(),"BookStore.db",null,2);//创建了一个MyDatabaseHelper的实例
        return true;//返回TRUE表示内容提供器初始化成功
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //查询数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();//获取SQliteDatabase的实例
        Cursor cursor = null;
        switch (uriMatcher.match(uri))//根据传入的URI来进行判断用户想要访问哪一张表
        {
            case BOOK_DIR:
                cursor = db.query("Book",projection,selection,selectionArgs,null,
                        null,null,sortOrder);//然后调用SQLiteDatabase的query()方法进行查询
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                /**
                 * 注意：这里调用了URI对象的getPathSegments()方法
                 * 它会将内容URI权限之后的部分以“/”符号进行分割
                 * 把分割之后的结果放入到一个字符串列表中，那个列表的第0个位置存放的就是路径
                 * 第1个位置的就是id了
                * */
                cursor = db.query("Book",projection,"id = ?",new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                /**
                 * 得到id之后，再通过selection和selectionArgs参数进行约束
                * */
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category",projection,"id = ?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;//然后返回cursor对象就好了
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        //更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();//获取SQliteDatabase的实例
        int updatedRows = 0;//要求返回的是一个int类型的值
        switch (uriMatcher.match(uri))//根据传入的URI来进行判断用户想要访问哪一张表
        {
            case BOOK_DIR:
                updatedRows = db.update("Book",values,selection,selectionArgs);//再调用SQLiteDatabase的update方法
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = db.update("Book",values,"id = ?",new String[] {bookId});
                break;
            case CATEGORY_DIR:
                updatedRows = db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                 String categoryId = uri.getPathSegments().get(1);
                updatedRows = db.update("Category",values,"id = ?",new String[] {categoryId});
                break;
            default:
                break;
        }
        return updatedRows;
    }
}
