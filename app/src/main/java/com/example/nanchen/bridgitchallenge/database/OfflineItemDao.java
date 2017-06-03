package com.example.nanchen.bridgitchallenge.database;

import android.text.TextUtils;

import com.example.nanchen.bridgitchallenge.model.OfflineItem;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanchen on 2017-06-02.
 */

public class OfflineItemDao extends BaseDao<OfflineItem> {
    public OfflineItemDao(){
        super();
    }

    public ArrayList<ItemListObject> getList(int limit, int offset) {
        try {
            String query= "SELECT id,title,dateupdated,categorylabel,imgurl,contentstr,savedtime FROM OfflineItem ORDER BY savedtime DESC LIMIT ? OFFSET ?";
            GenericRawResults<ItemListObject> results=dao.queryRaw(query, new RawRowMapper<ItemListObject>() {
                @Override
                public ItemListObject mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    ItemListObject otemobject=new ItemListObject(resultColumns[0],resultColumns[1],new DateTime(Long.valueOf(resultColumns[2])),resultColumns[3],resultColumns[4]==null?null:resultColumns[4],resultColumns[5], TextUtils.isEmpty(resultColumns[6])?null:new DateTime(Long.valueOf(resultColumns[6])));
                    return otemobject;
                }
            },String.valueOf(limit),String.valueOf(offset));
            return new ArrayList<>(results.getResults());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
