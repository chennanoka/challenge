package com.example.nanchen.bridgitchallenge.itemfactory;

import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.model.OfflineItem;
import com.example.nanchen.bridgitchallenge.object.ItemListObject;

import org.joda.time.DateTime;

/**
 * Created by nanchen on 2017-06-19.
 */

public class ItemFactory {
    public static Item listObjectToItem(ItemListObject itmelistobj){
        return new Item(itmelistobj.getId(),itmelistobj.getTitle(),itmelistobj.getDateupdated(),itmelistobj.getCategorylabel(),itmelistobj.getImgurl(),itmelistobj.getContentstr());
    }

    public static OfflineItem listObjectToOfflineItem(ItemListObject itmelistobj,DateTime savedtime){
        return new OfflineItem(itmelistobj.getId(),itmelistobj.getTitle(),itmelistobj.getDateupdated(),itmelistobj.getCategorylabel(),itmelistobj.getImgurl(),itmelistobj.getContentstr(),savedtime);
    }
}
