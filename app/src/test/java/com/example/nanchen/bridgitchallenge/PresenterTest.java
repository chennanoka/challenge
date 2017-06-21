package com.example.nanchen.bridgitchallenge;

import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;
import com.example.nanchen.bridgitchallenge.presenter.HomePresenter;
import com.example.nanchen.bridgitchallenge.presenter.IHomeView;
import com.example.nanchen.bridgitchallenge.xmlparser.XmlParser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by nanchen on 2017-06-02.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PresenterTest {
    final Object syncObject = new Object();
    private ArrayList<ItemListObject> mList;
  @Test
    public void testPresenter() throws Exception {
      HomePresenter presenter=new HomePresenter(new IHomeView() {
          @Override
          public void onGetQueryListResult(boolean isavailable, final ArrayList<ItemListObject> list, QueryType type) {
            if(isavailable){
                mList=list;

                synchronized (syncObject){
                    syncObject.notify();
                }
            }
          }
      });
      presenter.retrieveWholeList(10);
      synchronized (syncObject){
          syncObject.wait();
      }
      Assert.assertNotNull(mList);
    }
}
