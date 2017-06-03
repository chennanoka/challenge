package com.example.nanchen.bridgitchallenge;

import android.app.Application;

import com.example.nanchen.bridgitchallenge.database.DatabaseHelper;
import com.example.nanchen.bridgitchallenge.model.OfflineItem;
import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.util.Util;

import java.util.ArrayList;

/**
 * Created by nanchen on 2017-06-02.
 */

public class App extends Application {
    private static App singleton;
    private static Util util;
    private static DatabaseHelper databaseHelper;

    public static App getInstance(){
        return singleton;
    }
    public static DatabaseHelper getDatabaseHelper(){
        return databaseHelper;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        ArrayList<Class<?>> arrayList=new ArrayList<>();
        arrayList.add(OfflineItem.class);
        arrayList.add(Item.class);
        databaseHelper=new DatabaseHelper(this,arrayList);
        util=new Util(this.getApplicationContext());
    }
}
