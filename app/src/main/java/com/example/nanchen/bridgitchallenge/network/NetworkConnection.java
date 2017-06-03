package com.example.nanchen.bridgitchallenge.network;

import android.net.Uri;
import android.text.TextUtils;

import com.example.nanchen.bridgitchallenge.util.ConfigurationManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by nanchen on 2017-06-02.
 */

public class NetworkConnection {
    public enum HTTPVerb{
        GET,
        POST,
        PUT,
        DELETE
    }

    private boolean mCertified=false;


    public NetworkConnection(){
        this(false);
    }
    public NetworkConnection(boolean certified){
        mCertified=certified;
        if(certified){
            //TODO add HTTPS
        }
    }

    public WebResponse sendRequest(HTTPVerb verb, String detailURL, HashMap<String,String> parameters, String jsonstr) throws Exception{
        InputStream stream=null;
        OutputStream os=null;
        HttpURLConnection connection = null;
        String totalURL=URLManager.getBaseUrl()+detailURL;
        StringBuilder sb = new StringBuilder();
        try {

            String param = "";
            if(parameters!=null) {
                for (HashMap.Entry<String, String> pair : parameters.entrySet()) {
                    if (param.equals("")) {
                        param += "?" + Uri.encode(pair.getKey()) + "=" + Uri.encode(pair.getValue());
                    } else {
                        param += "&" + Uri.encode(pair.getKey()) + "=" + Uri.encode(pair.getValue());
                    }
                }
            }

            URL url = new URL(totalURL+param);
            if(!mCertified) {
                connection = (HttpURLConnection) url.openConnection();
            }
            //TODO add HTTPS

            connection.setReadTimeout(ConfigurationManager.NETWORK_READ_TIMEOUT);
            connection.setConnectTimeout(ConfigurationManager.NETWORK_CONNECTION_TIMEOUT);
            connection.setRequestMethod(verb.name());
            connection.setRequestProperty("Content-Type","application/json");
            connection.setInstanceFollowRedirects(false);
            connection.connect();

            if(!TextUtils.isEmpty(jsonstr)) {
                byte[] outputInBytes = jsonstr.getBytes("UTF-8");
                os = connection.getOutputStream();
                os.write(outputInBytes);
                os.close();
            }

            int responseCode = connection.getResponseCode();

            switch (responseCode) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    break;
                default:
                    throw new Exception("Network Error:"+responseCode);
            }
            return new WebResponse(responseCode,sb.toString());
        } catch(Exception e){
            throw e;
        }
        finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
            if(os!=null){
                os.close();
            }
        }
    }
}
