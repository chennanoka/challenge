package com.example.nanchen.bridgitchallenge.card;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.nanchen.bridgitchallenge.MainActivity;
import com.example.nanchen.bridgitchallenge.R;
import com.example.nanchen.bridgitchallenge.util.Util;

/**
 * Created by nanchen on 2017-06-02.
 */

public class ListFilerCard extends LinearLayout {
    public ProgressBar progressBar;
    public LinearLayout containerLinearLayout;
    public ListFilerCard(MainActivity fragmentActivity) {
        super(fragmentActivity);
        this.setPadding(Util.dptopx(5),Util.dptopx(5),Util.dptopx(5),Util.dptopx(5));
        this.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams viewgroupParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, Util.dptopx(60));
        this.setLayoutParams(viewgroupParam);

        containerLinearLayout=new LinearLayout(fragmentActivity);
        containerLinearLayout.setBackgroundResource(R.drawable.layerlist_frame_grayround);
        containerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        containerLinearLayout.setPadding(Util.dptopx(5),Util.dptopx(5),Util.dptopx(5),Util.dptopx(5));

        LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        containerLinearLayout.setLayoutParams(containerParam);



        progressBar=new ProgressBar(fragmentActivity,null,android.R.attr.progressBarStyleLarge);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

        containerLinearLayout.addView(progressBar);
        this.addView(containerLinearLayout);


    }
}
