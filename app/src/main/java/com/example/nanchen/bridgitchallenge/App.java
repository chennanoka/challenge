package com.example.nanchen.bridgitchallenge;

import android.app.Application;

import com.example.nanchen.bridgitchallenge.database.DatabaseHelper;
import com.example.nanchen.bridgitchallenge.model.OfflineItem;
import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.util.ConfigurationManager;
import com.example.nanchen.bridgitchallenge.util.Util;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    public static BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(ConfigurationManager.QUEUE_SIZE);
    public static RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
    public static ExecutorService executorService =  new ThreadPoolExecutor(ConfigurationManager.INITIAL_POOL_SIZE, ConfigurationManager.MAXIMUM_POOL_SIZE,
            ConfigurationManager.THREAD_ALIVE_TIME, TimeUnit.SECONDS, blockingQueue, rejectedExecutionHandler);

    public void execute(Runnable runnable){
        executorService.execute(runnable);
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
