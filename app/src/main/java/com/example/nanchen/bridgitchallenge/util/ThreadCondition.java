package com.example.nanchen.bridgitchallenge.util;

import android.os.Looper;

/**
 * Created by nanchen on 2017-06-02.
 */

public class ThreadCondition {
    public static void checkOnMainThread(){
            if(Thread.currentThread()!= Looper.getMainLooper().getThread()){
                throw new IllegalStateException("Other threads try to update UI");
            }
    }
}
