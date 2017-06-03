package com.example.nanchen.bridgitchallenge;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nanchen.bridgitchallenge.adapter.DrawerOptionsAdapter;
import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.object.DrawerOptionsItem;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;
import com.example.nanchen.bridgitchallenge.screen.BaseFragment;
import com.example.nanchen.bridgitchallenge.screen.BaseFragmentActivity;
import com.example.nanchen.bridgitchallenge.screen.HeaderFragment;
import com.example.nanchen.bridgitchallenge.screen.HomeFragment;
import com.example.nanchen.bridgitchallenge.screen.ItemFragment;
import com.example.nanchen.bridgitchallenge.screen.OfflineListFragment;

import java.util.ArrayList;

//signle activity mutiple fragments
public class MainActivity extends BaseFragmentActivity {


    public DrawerLayout drawerlayout;
    public ListView drawermenulistview;
    public BaseFragment showingFragment;
    public HeaderFragment headerFragment;
    public RelativeLayout drawerRelativeLayout;
    public ArrayList<DrawerOptionsItem> leftdraweritemslist;
    public DrawerOptionsAdapter slideradapter;


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof HeaderFragment){
            headerFragment=(HeaderFragment)fragment;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerlayout= (DrawerLayout)findViewById(R.id.leftdrawer_layout);
//
        TextView namtetext = (TextView)findViewById(R.id.nametext_leftslide);
        ImageView titleimg = (ImageView)findViewById(R.id.titleimg_leftslide);

        namtetext.setGravity(Gravity.CENTER_VERTICAL);
        namtetext.setText("Version:1.0");
        titleimg.setImageResource(R.mipmap.ic_launcher);


        Drawable opportunityicon = ContextCompat.getDrawable(this, R.mipmap.ic_home_gray_36dp);
        Drawable historyicon = ContextCompat.getDrawable(this, R.mipmap.ic_history_white_36dp);


        drawermenulistview=(ListView)findViewById(R.id.leftdrawer_list);
        drawerRelativeLayout = (RelativeLayout)findViewById(R.id.drawer_frame);


        leftdraweritemslist= new ArrayList<DrawerOptionsItem>();
        leftdraweritemslist.add(new DrawerOptionsItem(this,"Home", opportunityicon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(showingFragment instanceof HomeFragment)){
                    for(DrawerOptionsItem item :leftdraweritemslist) {
                        if(item.title.equals("Home")) {
                            item.view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white_smoke));
                        }
                        else{
                            item.view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                        }
                    }
                    slideradapter.addandupdatelist(leftdraweritemslist);
                    showHomeFragment();
                    MainActivity.this.toggleDrawer();
                }
            }
        }));
        leftdraweritemslist.add(new DrawerOptionsItem(this,"History", historyicon,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(showingFragment instanceof OfflineListFragment)){
                    for(DrawerOptionsItem item :leftdraweritemslist) {
                        if(item.title.equals("History")) {
                            item.view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white_smoke));
                        }
                        else{
                            item.view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                        }
                    }
                    slideradapter.addandupdatelist(leftdraweritemslist);
                    showOfflineListFragment();
                    MainActivity.this.toggleDrawer();
                }
            }
        }));

        slideradapter=new DrawerOptionsAdapter(this,leftdraweritemslist);
        drawermenulistview.setAdapter(slideradapter);


        //set slidebar size
        int drawerwidth = (getResources().getDisplayMetrics().widthPixels)*2/3;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) drawerRelativeLayout.getLayoutParams();
        params.width = drawerwidth;
        drawerRelativeLayout.setLayoutParams(params);


        //initial content frame
        showHomeFragment();

    }

    public void showHomeFragment(){
        if (!isFinishing()) {
            HomeFragment homeFragment =new HomeFragment();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.home_content_framelayout,homeFragment).commit();
            showingFragment=homeFragment;
        }

    }

    public void showOfflineListFragment(){
        if (!isFinishing()) {
            OfflineListFragment offlineItemListFragment =new OfflineListFragment();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.home_content_framelayout, offlineItemListFragment).commit();
            showingFragment= offlineItemListFragment;
        }
    }

    public void showItemFragment(ItemListObject item){
        if (!isFinishing()) {
            ItemFragment itemFragment =new ItemFragment();
            itemFragment.itemListObject=item;
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.addToBackStack (null);
            ft.replace(R.id.home_content_framelayout, itemFragment);
            ft.commitAllowingStateLoss ();
            showingFragment= itemFragment;
        }
    }

    // extends or listener functions
    @Override
    public void changeHeaderElement(BaseFragment fragment){
        if(headerFragment!=null&&fragment!=headerFragment&&fragment.isChildFragment()){
            ((HeaderFragment)headerFragment).updateHeaderFragment(0);
        }else if(headerFragment!=null&&fragment!=headerFragment&&!fragment.isChildFragment()){
            ((HeaderFragment)headerFragment).updateHeaderFragment(1);
        }
    }
    public void toggleDrawer(){
        if(!drawerlayout.isDrawerOpen(drawerRelativeLayout)) {
            drawerlayout.openDrawer(Gravity.LEFT);
        }else{
            drawerlayout.closeDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (showingFragment.isChildFragment()&&getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack ();
        }
    }
}

