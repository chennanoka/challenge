package com.example.nanchen.bridgitchallenge.card;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nanchen.bridgitchallenge.util.Util;

/**
 * Created by nanchen on 2017-06-02.
 */
public class DrawerOptionCard extends LinearLayout {

    public ImageView titleimgview;
    public TextView titletextview;
    public DrawerOptionCard(Context context) {
        super(context);
        this.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams relativeparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeparam.setMargins(Util.dptopx(5),Util.dptopx(5),Util.dptopx(5),Util.dptopx(5));
        setLayoutParams(relativeparam);

        titleimgview = new ImageView(context);
        LinearLayout.LayoutParams imgviewparam =new LinearLayout.LayoutParams(Util.dptopx(30), Util.dptopx(30));
        imgviewparam.setMargins(0,Util.dptopx(5),0,0);
        titleimgview.setLayoutParams(imgviewparam);

        titletextview = new TextView(context);
        titletextview.setGravity(Gravity.CENTER_VERTICAL);
        titletextview.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        LinearLayout.LayoutParams textviewparam =new LinearLayout.LayoutParams(Util.dptopx(0),Util.dptopx(40));
        textviewparam.setMargins(Util.dptopx(10),0,0,0);
        textviewparam.weight=1;
        titletextview.setLayoutParams(textviewparam);

        this.addView(titleimgview);
        this.addView(titletextview);
    }
}