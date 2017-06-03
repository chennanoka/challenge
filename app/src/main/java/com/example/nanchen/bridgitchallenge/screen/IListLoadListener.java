package com.example.nanchen.bridgitchallenge.screen;

import java.util.ArrayList;

/**
 * Created by nanchen on 2017-06-03.
 */

public interface IListLoadListener<T>{
    void onGetQueryListResult(boolean isavailable, ArrayList<T> list,int type);
}
