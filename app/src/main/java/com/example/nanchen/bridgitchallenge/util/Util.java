package com.example.nanchen.bridgitchallenge.util;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nanchen on 2017-06-02.
 */

public class Util {
    private static Context context;
    public Util(Context context){
        this.context=context;
    }

    public static String getFirstImgInHtml(String str){
        Pattern p = Pattern.compile(".*(<img\\s+.*src\\s*=\\s*\"([^\"]+)\".*>).*");
        Matcher m = p.matcher(str);
        if(m.find()) {
            return m.group(2);
        }else{
            return null;
        }
    }

    public static int pxtodp(float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int dptopx(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}
