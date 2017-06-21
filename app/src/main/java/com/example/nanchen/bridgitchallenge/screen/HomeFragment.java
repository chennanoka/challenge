package com.example.nanchen.bridgitchallenge.screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.nanchen.bridgitchallenge.MainActivity;
import com.example.nanchen.bridgitchallenge.adapter.HomeRecycleAdapter;
import com.example.nanchen.bridgitchallenge.database.ItemDao;
import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.network.NetworkConnection;
import com.example.nanchen.bridgitchallenge.network.URLManager;
import com.example.nanchen.bridgitchallenge.network.WebResponse;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;
import com.example.nanchen.bridgitchallenge.presenter.HomePresenter;
import com.example.nanchen.bridgitchallenge.presenter.IHomeView;
import com.example.nanchen.bridgitchallenge.xmlparser.XmlParser;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nanchen on 2017-06-02.
 * top over refresh all
 * bottom over load more
 * click star toggle save current to offline table
 * click item jump to detail
 */

public class HomeFragment extends BaseFragment implements IHomeView{
    MainActivity nn_activity;
    RelativeLayout rootview;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycleview;
    HomeRecycleAdapter adapter;
    LinearLayoutManager mLayoutManager;

    private HomePresenter presenter;

    public static final int LIMIT=10;
    public static int offset;
    private int preLast;
    public volatile boolean flag_loading=false;

    // set the value to indicate the last retrieve the whole list from web, to prevent repeat action
    public static DateTime previousRefresh;

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
        presenter=new HomePresenter(this);
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
                    presenter.retrieveWholeList(LIMIT);
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
                                presenter.retrieveExtraList(LIMIT,offset);
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
        offset=0;
        flag_loading=true;
        if(previousRefresh!=null&&TimeUnit.MILLISECONDS.toSeconds(DateTime.now().getMillis()-previousRefresh.getMillis())<60) {
            presenter.retrieveExtraList(LIMIT,offset);
        }else{
            showLoading();
            presenter.retrieveWholeList(LIMIT);
        }
    }

    //refresh all from database
    // if is extra retrieve locally, if full retreive frm remote and save to local
    @Override
    public void onGetQueryListResult(final boolean isavailable,  final ArrayList list,IHomeView.QueryType type) {
        if(type == QueryType.FULL) {
            nn_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HomeFragment.this.dismissLoading();
                    swipeRefreshLayout.setRefreshing(false);
                    if (isavailable) {
                        if (list != null) {
                            adapter.clearList();
                            adapter.addandupdatelist(list);
                        }
                    }
                    previousRefresh=DateTime.now();
                }
            });
        }else if(type == QueryType.Extra){
            nn_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isavailable) {
                        if (list != null) {
                            adapter.deleteRefreshRow();
                            adapter.addandupdatelist(list);
                        }
                    }
                }});
        }

        // not matter available or not set the flag to allow next event
        flag_loading=false;

    }
}
