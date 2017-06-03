package com.example.nanchen.bridgitchallenge.screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.example.nanchen.bridgitchallenge.util.ConfigurationManager;

/**
 * Created by nanchen on 2017-06-02.
 */

public class BaseFragmentActivity extends FragmentActivity{


    public static BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(ConfigurationManager.QUEUE_SIZE);
    public static RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
    public static ExecutorService executorService =  new ThreadPoolExecutor(ConfigurationManager.INITIAL_POOL_SIZE, ConfigurationManager.MAXIMUM_POOL_SIZE,
            ConfigurationManager.THREAD_ALIVE_TIME, TimeUnit.SECONDS, blockingQueue, rejectedExecutionHandler);

    public void execute(Runnable runnable){
        executorService.execute(runnable);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //refresh the showing element of headfragment
    public void changeHeaderElement(BaseFragment fragment){
    }


}
