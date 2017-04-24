package com.dgkj.fxmall.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

/** 创建数据表（利用数据库框架）
 * Created by Android004 on 2016/8/24.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    //单例模式获取数据库对象
    private static DBHelper dbHelper;
    public static DBHelper getInstance(Context context){
        if(dbHelper == null){
            synchronized (DBHelper.class){//同步锁创建新对象
                if(dbHelper == null){
                    dbHelper = new DBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    private DBHelper(Context context) {//私有化构造方法
        super(context, "msg.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        //通过表格工具类创建数据表
        // TableUtils.createTable(connectionSource, ChatMsgBean.class);

        Log.i("CHW","创建数据表完成");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        //当更新数据表时，先清除旧数据
        // TableUtils.clearTable(connectionSource,ChatMsgBean.class);
        onCreate(sqLiteDatabase,connectionSource);

    }
}
