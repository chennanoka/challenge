package com.example.nanchen.bridgitchallenge.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by nanchen on 2017-06-02.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Context mContext;
    private ArrayList<Class<?>> mTables;
    // the DAO object we use to access the NewsItem table
    //private Dao<NewsItem, Integer> simpleDao = null;

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "dbtest.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    public static DatabaseHelper instance;


    //public static HashMap<Object,Dao<?, Integer>> hashmap;
    /**
     *
     * Returns the Database Access Object (DAO) for our NewsItem class. It will create it or just give the cached
     * value.
     */

    public DatabaseHelper(Context context,ArrayList<Class<?>> tables) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mTables=tables;
        mContext = context;
    }

//    public  <D extends Dao<T, ?>, T> D getDao(T inputClass) throws SQLException {
//        Dao<T, Integer> simpleDao=null;
//        if (!hashmap.containsKey(inputClass)) {
//            simpleDao = (Dao<T, Integer>)getDao(inputClass.getClass());
//            hashmap.put(inputClass,simpleDao);
//        }else{
//            simpleDao=(Dao<T, Integer>)hashmap.get(inputClass);
//        }
//        return (D)simpleDao;
//    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            //create all classes
            for(Class<?> obj:mTables) {
                TableUtils.createTable(connectionSource, obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            for(Class<?> obj:mTables) {
                TableUtils.dropTable(connectionSource, obj,true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        super.close();
//        instance = null;
        mContext = null;
    }

}
