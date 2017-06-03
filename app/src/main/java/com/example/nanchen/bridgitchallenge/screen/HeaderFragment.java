package com.example.nanchen.bridgitchallenge.screen;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.nanchen.bridgitchallenge.MainActivity;
import com.example.nanchen.bridgitchallenge.R;
import com.example.nanchen.bridgitchallenge.util.Util;

/**
 * Created by nanchen on 2017-06-02.
 */

public class HeaderFragment extends BaseFragment{

    public RelativeLayout headerRelativeLayout;
    public ImageView menuRelative;
    public MainActivity nn_activity;

    public HeaderFragment(){

    }
    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            nn_activity=(MainActivity)context;
        }
    }
    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        headerRelativeLayout=new RelativeLayout(nn_activity);
        headerRelativeLayout.setBackgroundColor(ContextCompat.getColor(nn_activity, R.color.indigo_500));
        headerRelativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Util.dptopx(50)));


        menuRelative= new ImageView(nn_activity);
        menuRelative.setImageResource(R.mipmap.ic_menu_white_36dp);
        RelativeLayout.LayoutParams menuparam=new RelativeLayout.LayoutParams(Util.dptopx(24),Util.dptopx(24));
        menuparam.addRule(RelativeLayout.CENTER_VERTICAL);
        menuparam.setMargins(Util.dptopx(15),0,0,0);
        menuRelative.setLayoutParams(menuparam);

        menuRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator rot = ObjectAnimator.ofFloat(v, "rotation", 0f, 180f);
                rot.setDuration(500);
                rot.start();
                nn_activity.toggleDrawer();
            }
        });


        ImageView imgview = new ImageView(nn_activity);
        imgview.setImageResource(R.drawable.shape_line_lightgray);
        RelativeLayout.LayoutParams imgviewparam=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Util.dptopx(1.5f));
        imgviewparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        imgview.setLayoutParams(imgviewparam);

        headerRelativeLayout.addView(menuRelative);
        headerRelativeLayout.addView(imgview);

        return headerRelativeLayout;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    public void updateHeaderFragment(int type){
        switch(type) {
            case 0:
                menuRelative.setImageResource(R.mipmap.ic_arrow_back_white_36dp);
                menuRelative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ObjectAnimator rot = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                        rot.setDuration(500);
                        rot.start();
                        if (nn_activity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            nn_activity.getSupportFragmentManager().popBackStack ();
                        }
                    }
                });
                //pull stack back
                break;
            case 1:
                menuRelative.setImageResource(R.mipmap.ic_menu_white_36dp);
                menuRelative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ObjectAnimator rot = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                        rot.setDuration(500);
                        rot.start();
                        nn_activity.toggleDrawer();
                    }
                });break;
        }
    }
}


