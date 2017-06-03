package com.example.nanchen.bridgitchallenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nanchen.bridgitchallenge.MainActivity;
import com.example.nanchen.bridgitchallenge.R;
import com.example.nanchen.bridgitchallenge.card.ListFilerCard;
import com.example.nanchen.bridgitchallenge.database.OfflineItemDao;
import com.example.nanchen.bridgitchallenge.imgcache.ImageManager;
import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.model.OfflineItem;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;
import com.example.nanchen.bridgitchallenge.screen.BaseFragment;
import com.example.nanchen.bridgitchallenge.util.ThreadCondition;

import java.util.ArrayList;

/**
 * Created by nanchen on 2017-06-02.
 */
public class HomeRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ItemListObject> itemObjectList = new ArrayList<ItemListObject>();
    //refresh indicate row
    public ItemListObject fillerobj;
    public MainActivity mActivity;
    public BaseFragment mFragment;
    public boolean[] isMarkedArray;

    // public MyViewControl control;
    public HomeRecycleAdapter(MainActivity activity, BaseFragment fragment) {
        mActivity = activity;
        mFragment = fragment;
        fillerobj = ItemListObject.getFiler();
        // control=new MyViewControl();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, dateupdated, categorylevel;
        public ImageView thumbnail;
        public ImageView star;
        public RelativeLayout root;

        public MyViewHolder(final View view) {
            super(view);
            root=(RelativeLayout)view.findViewById(R.id.listitem_item_root);
            title = (TextView) view.findViewById(R.id.listitem_item_title_textview);
            thumbnail=(ImageView) view.findViewById(R.id.listitem_item_thumbnail_imageview);
            star=(ImageView)view.findViewById(R.id.listitem_item_star_imageview);
            dateupdated=(TextView)view.findViewById(R.id.listitem_item_dateupdated_textview);
            categorylevel=(TextView)view.findViewById(R.id.listitem_item_categorylevel_textview);

            //click cell go to detail page
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.showItemFragment(itemObjectList.get(MyViewHolder.this.getAdapterPosition()));
                }
            });
            //click star save current item
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OfflineItemDao dao = new OfflineItemDao();
                    //if stared save else delete
                    if(!isMarkedArray[MyViewHolder.this.getAdapterPosition()]) {
                        dao.add(itemObjectList.get(MyViewHolder.this.getAdapterPosition()).getOfflineItem());
                        isMarkedArray[MyViewHolder.this.getAdapterPosition()]=true;
                        star.setImageResource(R.mipmap.ic_star_black_36dp);
                    }else{
                        dao.delete(itemObjectList.get(MyViewHolder.this.getAdapterPosition()).getOfflineItem());
                        isMarkedArray[MyViewHolder.this.getAdapterPosition()]=false;
                        star.setImageResource(R.mipmap.ic_star_border_black_36dp);
                    }
                }
            });

        }
    }

    public class MyViewHolderFiller extends RecyclerView.ViewHolder {
        public MyViewHolderFiller(View view) {
            super(view);
        }

    }


    @Override
    public int getItemViewType(int position) {
        ItemListObject opp = itemObjectList.get(position);
        if (opp.isfiller()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_item, parent, false));
        } else {
            return new MyViewHolderFiller(new ListFilerCard(mActivity));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemListObject opp = itemObjectList.get(position);
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewholder = ((MyViewHolder) holder);
            viewholder.title.setText(opp.getTitle());
            if(!TextUtils.isEmpty(opp.getImgurl())) {
                ImageManager.getInstance(mActivity).load(opp.getImgurl(),R.mipmap.ic_launcher,R.mipmap.ic_error_black_24dp).toImg(viewholder.thumbnail);
            }else{
                viewholder.thumbnail.setImageResource(R.mipmap.ic_error_black_24dp);
            }
            viewholder.categorylevel.setText(opp.getCategorylabel());
            viewholder.dateupdated.setText(opp.getDateupdated().toString("MM/dd/yyyy HH:mm:ss"));

            //if clicked star set resource,refresh whenever reuse
            if (isMarkedArray[viewholder.getAdapterPosition()]) {
                viewholder.star.setImageResource(R.mipmap.ic_star_black_36dp);
            } else {
                viewholder.star.setImageResource(R.mipmap.ic_star_border_black_36dp);
            }
        }

    }

    @Override
    public int getItemCount() {
        return itemObjectList.size();
    }

    //when scroll down
    public void addandupdatelist(ArrayList<ItemListObject> positionObjectArrayList) {
        ThreadCondition.checkOnMainThread();
        itemObjectList.addAll(positionObjectArrayList); 
        isMarkedArray = new boolean[itemObjectList.size()];
        //pre-set star
        for(int i=0;i<itemObjectList.size();i++){
            if(itemObjectList.get(i).getSavedtime()!=null){
                isMarkedArray[i]=true;
            }
        }
        notifyDataSetChanged();
    }

    public void addRefreshRow() {
        ThreadCondition.checkOnMainThread();
        itemObjectList.add(fillerobj);
        isMarkedArray = new boolean[itemObjectList.size()];
        notifyDataSetChanged();
    }

    public void deleteRefreshRow() {
        ThreadCondition.checkOnMainThread();
        itemObjectList.remove(fillerobj);
        isMarkedArray = new boolean[itemObjectList.size()];
        notifyDataSetChanged();
    }

    public void clearList() {
        ThreadCondition.checkOnMainThread();
        itemObjectList.clear();
        isMarkedArray = new boolean[itemObjectList.size()];
        notifyDataSetChanged();
    }

}
