package com.example.nanchen.bridgitchallenge.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.types.DateType;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

/**
 * Created by nanchen on 2017-06-02.
 */
@DatabaseTable
public class OfflineItem extends Item{
    @DatabaseField
    DateTime savedtime;

    public OfflineItem(){
    }
    public OfflineItem(String id, String title, DateTime dateupdated, String categorylabel, String imgurl, String contentstr,DateTime savedtime) {
        this.id=id;
        this.title = title;
        this.dateupdated = dateupdated;
        this.categorylabel = categorylabel;
        this.imgurl = imgurl;
        this.contentstr=contentstr;
        this.savedtime=savedtime;
    }
}
