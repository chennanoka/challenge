package com.example.nanchen.bridgitchallenge.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.nanchen.bridgitchallenge.object.DrawerOptionsItem;
import com.example.nanchen.bridgitchallenge.util.ThreadCondition;

import java.util.ArrayList;

/**
 * Created by nanchen on 2017-06-02.
 */

public class DrawerOptionsAdapter extends BaseAdapter {


    private LayoutInflater nn_inflater;
    private ArrayList<DrawerOptionsItem> nn_data=new ArrayList<DrawerOptionsItem>();
    private Activity nn_activity;

    public DrawerOptionsAdapter(Activity currentactivity, ArrayList<DrawerOptionsItem> mData) {
        nn_inflater = LayoutInflater.from(currentactivity);
        nn_data =mData;
        nn_activity=currentactivity;
    }

    public void addandupdatelist(ArrayList<DrawerOptionsItem> items){
        nn_data=items;
        ThreadCondition.checkOnMainThread();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nn_data.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ){
            //We must create a View:
            convertView=nn_data.get(position).view;
        }
        return convertView;
    }



}