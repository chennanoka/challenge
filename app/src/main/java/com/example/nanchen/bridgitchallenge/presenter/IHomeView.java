package com.example.nanchen.bridgitchallenge.presenter;

import com.example.nanchen.bridgitchallenge.object.ItemListObject;

import java.util.ArrayList;

/**
 * Created by nanchen on 2017-06-20.
 */

public interface IHomeView {
    enum QueryType{
        FULL,
        Extra
    }
    void onGetQueryListResult(boolean isavailable, ArrayList<ItemListObject> list, QueryType type);
}
