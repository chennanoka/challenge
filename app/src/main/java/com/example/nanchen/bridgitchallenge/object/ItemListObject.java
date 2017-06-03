package com.example.nanchen.bridgitchallenge.object;

import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.model.OfflineItem;
import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by nanchen on 2017-06-03.
 */

public class ItemListObject {
    private String id;
    private String title;
    private DateTime dateupdated;
    private String categorylabel;
    private String imgurl;
    private String contentstr;
    private  DateTime savedtime;


    protected boolean isfiller;

    public ItemListObject(){

    }

    public ItemListObject(String id, String title, DateTime dateupdated, String categorylabel, String imgurl, String contentstr, DateTime savedtime) {
        this.id=id;
        this.title = title;
        this.dateupdated = dateupdated;
        this.categorylabel = categorylabel;
        this.imgurl = imgurl;
        this.contentstr=contentstr;
        this.savedtime=savedtime;
    }

    public static ItemListObject getFiler(){
        ItemListObject item=new ItemListObject();
        item.isfiller=true;
        return item;
    }

    public boolean isfiller(){
        return isfiller;
    }

    public Item getItem(){
        return new Item(id,title,dateupdated,categorylabel,imgurl,contentstr);
    }

    public OfflineItem getOfflineItem(){
        DateTime dateTime= savedtime;
        if(dateTime==null){
            dateTime= DateTime.now();
        }
        return new OfflineItem(id,title,dateupdated,categorylabel,imgurl,contentstr,dateTime);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public DateTime getDateupdated() {
        return dateupdated;
    }

    public String getCategorylabel() {
        return categorylabel;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getContentstr() {
        return contentstr;
    }

    public DateTime getSavedtime() {
        return savedtime;
    }
}
