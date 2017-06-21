package com.example.nanchen.bridgitchallenge.presenter;

import android.text.TextUtils;

import com.example.nanchen.bridgitchallenge.App;
import com.example.nanchen.bridgitchallenge.database.ItemDao;
import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.network.NetworkConnection;
import com.example.nanchen.bridgitchallenge.network.URLManager;
import com.example.nanchen.bridgitchallenge.network.WebResponse;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;
import com.example.nanchen.bridgitchallenge.screen.HomeFragment;
import com.example.nanchen.bridgitchallenge.screen.IListLoadListener;
import com.example.nanchen.bridgitchallenge.xmlparser.XmlParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanchen on 2017-06-20.
 */

public class HomePresenter {

    private IHomeView mView;
    public HomePresenter(IHomeView homeView){
        mView=homeView;
    }
    public void retrieveWholeList(int limit){
        App.getInstance().execute(new RetrieveListThread(this,limit));
    }
    public void retrieveExtraList(int limit,int offset){
        App.getInstance().execute(new RetrieveExtraListThread(this,limit,offset));
    }

    protected void onGetRetrieveWholeList(boolean isavailable, ArrayList<ItemListObject> list){
        mView.onGetQueryListResult(isavailable,list,IHomeView.QueryType.FULL);
    }

    protected void onGetRetrieveExtraList(boolean isavailable, ArrayList<ItemListObject> list){
        mView.onGetQueryListResult(isavailable,list,IHomeView.QueryType.Extra);
    }

    class RetrieveListThread implements Runnable {
        private boolean isavailable=false;
        private HomePresenter mPresenter;
        private int mLimit;
        public RetrieveListThread(HomePresenter presenter,int limit) {
            mPresenter=presenter;
            mLimit=limit;
        }

        @Override
        public void run(){
            ArrayList<ItemListObject> list=null;
            try{
                //after retrieve update database
                WebResponse response=new NetworkConnection().sendRequest(NetworkConnection.HTTPVerb.GET, URLManager.getRssUrl(),null,"");
                if(response.code==200&&!TextUtils.isEmpty(response.result)){
                    List<Item> itemList= new XmlParser().parse(response.result);
                    new ItemDao().addAll(itemList);
                    list=new ItemDao().getJoinedList(mLimit,0);
                }
                isavailable=true;
            }catch (Exception e){
                e.printStackTrace();
                isavailable=false;
            }finally{
                if(mPresenter!=null){
                    mPresenter.onGetRetrieveWholeList(isavailable,list);
                }
            }
        }
    }

    // if extends 10
    class RetrieveExtraListThread implements Runnable{
        private boolean isavailable=false;
        private HomePresenter mPresenter;
        private int mLimit;
        private int mOffset;

        public RetrieveExtraListThread(HomePresenter presenter,int limit,int offset) {
            mPresenter=presenter;
            mLimit=limit;
            mOffset=offset;
        }

        @Override
        public void run(){

            ArrayList<ItemListObject> list=null;
            try{
                list = new ItemDao().getJoinedList(mLimit, mOffset);
                isavailable = true;
            }catch (Exception e){
                e.printStackTrace();
                isavailable=false;
            }finally{
                if(mPresenter!=null){
                    mPresenter.onGetRetrieveExtraList(isavailable,list);
                }
            }
        }
    }

}
