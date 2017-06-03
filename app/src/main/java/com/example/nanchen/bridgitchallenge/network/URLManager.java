package com.example.nanchen.bridgitchallenge.network;

/**
 * Created by nanchen on 2017-06-02.
 */

public class URLManager {

    public static final String BASE_URL="https://www.reddit.com/";
    public static final String RSS_URL="hot/.rss";



    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getRssUrl() {
        return RSS_URL;
    }
}
