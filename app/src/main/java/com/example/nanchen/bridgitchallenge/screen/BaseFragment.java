package com.example.nanchen.bridgitchallenge.screen;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nanchen.bridgitchallenge.R;

/**
 * Created by nanchen on 2017-06-02.
 */

public class BaseFragment extends Fragment{
    public RelativeLayout loadingLayout;
    private boolean isChildFragment=false;
    public BaseFragmentActivity nn_activity;
    public boolean isChildFragment(){
        return isChildFragment;
    }

    public void setIsChildFragment(boolean isChildFragment){
        this.isChildFragment=isChildFragment;
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);

        if (context instanceof BaseFragmentActivity){
            nn_activity=(BaseFragmentActivity) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nn_activity.changeHeaderElement(this);
        hideSoftKeyboard();
    }

    public void hideSoftKeyboard() {
        if(this != null && nn_activity.getCurrentFocus() != null && nn_activity.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)nn_activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(nn_activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     *  all after onCreateView
     */
    public void showLoading(){
        if(getView()!=null){
            ViewGroup groupView = (ViewGroup) getView();
            if(loadingLayout==null){
                loadingLayout=new RelativeLayout(getActivity());

                LinearLayout wrapper =new LinearLayout(getActivity());
                wrapper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                wrapper.setOrientation(LinearLayout.VERTICAL);
                wrapper.setGravity(Gravity.CENTER);
                wrapper.setBackgroundColor(getResources().getColor(R.color.white_smoke));

                ProgressBar mProgressDialog = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleSmall);
                mProgressDialog.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView textView=new TextView(getActivity());
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText("Loading...");

                wrapper.addView(mProgressDialog);
                wrapper.addView(textView);
                loadingLayout.addView(wrapper);
            }
            groupView.addView(loadingLayout);
        }
    }
    /**
     *  all after onCreateView
     */
    public void dismissLoading(){
        if(getView()!=null){
            ViewGroup groupView = (ViewGroup) getView();
            if(loadingLayout!=null){
                groupView.removeView(loadingLayout);
            }
        }
    }

}
