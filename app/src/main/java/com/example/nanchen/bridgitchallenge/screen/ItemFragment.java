package com.example.nanchen.bridgitchallenge.screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;

/**
 * Created by nanchen on 2017-06-02.
 */

public class ItemFragment extends BaseFragment{
    public ItemListObject itemListObject;
    public WebView webView;
    public ItemFragment(){
        setIsChildFragment(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        webView=new WebView(nn_activity);
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView.loadData(itemListObject.getContentstr(),"text/html", "UTF-8");
        return webView;
    }
}
