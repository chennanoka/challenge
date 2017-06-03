package com.example.nanchen.bridgitchallenge.xmlparser;


import android.util.Xml;

import com.example.nanchen.bridgitchallenge.model.Item;
import com.example.nanchen.bridgitchallenge.util.Util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nanchen on 2017-06-02.
 */

public class XmlParser {
    private static final String ns = null;
    public List<Item> parse(String str) throws XmlPullParserException, IOException {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(str));
            parser.nextTag();
            return readFeed(parser);
    }


    private List<Item> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Item> entries = new ArrayList<Item>();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("entry")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }
    private Item readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String id = null;
        String title = null;
        DateTime dateupdated = null;
        String categorylabel = null;
        String imgurl = null;
        String content = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readStr(parser,"title");
            } else if (name.equals("id")) {
                id = readStr(parser,"id");
            } else if (name.equals("updated")) {

                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                dateupdated = formatter.parseDateTime(readStr(parser,"updated"));
            } else if (name.equals("category")){
                categorylabel=readCategotyLabel(parser);
            } else if (name.equals("content")){
                String str = readStr(parser,"content");
                content=str;
                imgurl=Util.getFirstImgInHtml(str);
            }
            else {
                skip(parser);
            }
        }
        return new Item(id,title, dateupdated, categorylabel,imgurl,content);
    }



    private String readStr(XmlPullParser parser,String key) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, key);
        String str = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, key);
        return str;
    }



    private String readCategotyLabel(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "category");
        String result=parser.getAttributeValue(null,"label");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "category");
        return result;
    }






    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }






    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
