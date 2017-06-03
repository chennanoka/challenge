package com.example.nanchen.bridgitchallenge.network;

/**
 * Created by nanchen on 2017-06-02.
 */

public class WebResponse {
    public String result;
    public int code;
    public WebResponse(int code,String result){
        this.code=code;
        this.result=result;
    }
}