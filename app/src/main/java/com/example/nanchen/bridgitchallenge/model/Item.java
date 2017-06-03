package com.example.nanchen.bridgitchallenge.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

/**
 * Created by nanchen on 2017-06-02.
 */
@DatabaseTable
public class Item {
    @DatabaseField(id =true)
    protected String id;
    @DatabaseField
    protected String title;
    @DatabaseField
    protected DateTime dateupdated;
    @DatabaseField
    protected String categorylabel;
    @DatabaseField
    protected String imgurl;
    @DatabaseField
    protected String contentstr;


    public Item(){

    }


    public Item(String id, String title, DateTime dateupdated, String categorylabel, String imgurl, String contentstr) {
        this.id=id;
        this.title = title;
        this.dateupdated = dateupdated;
        this.categorylabel = categorylabel;
        this.imgurl = imgurl;
        this.contentstr=contentstr;
    }

    public OfflineItem getOfflineItem(){
        return new OfflineItem(id,title,dateupdated,categorylabel,imgurl,contentstr,DateTime.now());
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

    public String getId(){
        return id;
    }

    public String getContentstr() {
        return contentstr;
    }
}