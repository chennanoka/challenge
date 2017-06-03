package com.example.nanchen.bridgitchallenge.screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.nanchen.bridgitchallenge.MainActivity;
import com.example.nanchen.bridgitchallenge.adapter.HomeRecycleAdapter;
import com.example.nanchen.bridgitchallenge.database.OfflineItemDao;
import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.model.OfflineItem;
import com.example.nanchen.bridgitchallenge.network.NetworkConnection;
import com.example.nanchen.bridgitchallenge.network.URLManager;
import com.example.nanchen.bridgitchallenge.network.WebResponse;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;
import com.example.nanchen.bridgitchallenge.xmlparser.XmlParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanchen on 2017-06-02.
 */

public class OfflineListFragment extends BaseFragment implements IListLoadListener<ItemListObject>{
    MainActivity nn_activity;
    RelativeLayout rootview;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycleview;
    HomeRecycleAdapter adapter;
    LinearLayoutManager mLayoutManager;

    public static final int LIMIT=10;
    public static int offset;
    private int preLast;
    public volatile boolean flag_loading=false;

    @Override
    public void onAttach (Context context)
    {
        super.onAttach (context);

        if (context instanceof MainActivity){
            nn_activity=(MainActivity)context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offset=0;
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=new RelativeLayout(nn_activity);
        rootview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        swipeRefreshLayout=new SwipeRefreshLayout(nn_activity);
        swipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!flag_loading){
                    flag_loading=true;
                    offset=0;
                    nn_activity.execute(new RetrieveListThread(OfflineListFragment.this));
                }
            }
        });

        //build list
        recycleview =new RecyclerView(nn_activity);
        RelativeLayout.LayoutParams listviewparam=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        recycleview.setLayoutParams(listviewparam);

        adapter=new HomeRecycleAdapter(nn_activity,this);
        recycleview.setAdapter(adapter);

        mLayoutManager = new LinearLayoutManager(nn_activity);
        recycleview.setLayoutManager(mLayoutManager);

        recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                final int lastItem = firstVisibleItem + visibleItemCount;
                //if at top & scroll
                if (dy>0&&lastItem>0&&lastItem == totalItemCount && totalItemCount % 10 == 0) {
                    if (!flag_loading&&preLast != lastItem) {
                        flag_loading = true;
                        preLast = lastItem;
                        offset += 10;
                        adapter.addRefreshRow();
                        nn_activity.execute(new RetrieveExtraListThread(OfflineListFragment.this));
                    }
                }

            }
        });
        swipeRefreshLayout.addView(recycleview);
        rootview.addView(swipeRefreshLayout);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLoading();
        nn_activity.execute(new RetrieveListThread(OfflineListFragment.this));
    }

    //refresh all from database
    @Override
    public void onGetQueryListResult(boolean isavailable, ArrayList list, int type) {
        if(type==0) {
            if (isavailable) {
                final ArrayList<ItemListObject> templist = list;
                nn_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (templist != null) {
                            adapter.clearList();
                            adapter.addandupdatelist(templist);
                            flag_loading=false;
                        }
                    }
                });
            }
        }else if(type==1){
            if (isavailable) {
                final ArrayList<ItemListObject> templist = list;
                nn_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (templist != null) {
                            adapter.deleteRefreshRow();
                            adapter.addandupdatelist(templist);
                            flag_loading=false;
                        }
                    }
                });
            }
        }
    }

    // if extends 10
    class RetrieveExtraListThread implements Runnable{
        private boolean isavailable=false;
        private IListLoadListener nn_listener;

        public RetrieveExtraListThread(IListLoadListener listener) {
            nn_listener=listener;
        }

        @Override
        public void run(){

            ArrayList<ItemListObject> list=null;
            try{
                Thread.sleep(1000);
                list = new OfflineItemDao().getList(LIMIT, offset);
                isavailable=true;
            }catch (Exception e){
                e.printStackTrace();
                isavailable=false;
                flag_loading=false;
            }finally{
                if(nn_listener!=null){
                    nn_listener.onGetQueryListResult(isavailable,list,1);
                }
            }
        }


    }

    class RetrieveListThread implements Runnable {
        private boolean isavailable=false;
        private IListLoadListener nn_listener;

        public RetrieveListThread(IListLoadListener listener) {
            nn_listener=listener;
        }

        @Override
        public void run(){
            ArrayList<ItemListObject> list=null;
            try{
                Thread.sleep(1000);
                //after retrieve update database
                list=new OfflineItemDao().getList(LIMIT,offset);
                offset=0;
                isavailable=true;
            }catch (Exception e){
                e.printStackTrace();
                isavailable=false;
                flag_loading=false;
            }finally{
                nn_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OfflineListFragment.this.dismissLoading();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                if(nn_listener!=null){
                    nn_listener.onGetQueryListResult(isavailable,list,0);
                }
            }
        }
    }

}