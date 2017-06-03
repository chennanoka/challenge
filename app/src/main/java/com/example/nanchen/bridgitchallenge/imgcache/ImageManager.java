package com.example.nanchen.bridgitchallenge.imgcache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nanchen on 2017-06-02.
 */

public class ImageManager {
    private static Context mContext;
    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(5);
    private LruCache<String,Bitmap> mImageCache = new LruCache<>(1024 * 1024 * 4);
    private static ImageManager mInstance;
    private Handler mHandler = new Handler();
    private static ImageManager getInstance(){
        if(mInstance==null){
            mInstance=new ImageManager();
        }
        return mInstance;
    }
    public static ImageManager getInstance(Context context){
        mContext=context;
        return getInstance();
    }
    public ImgRequest load(String url,int placeholderId,int errorholderid){
        return new ImgRequest(url,placeholderId,errorholderid);
    }
    public class ImgRequest implements Runnable{
        String mUrl;
        int mHolderResId;
        int mErrorResId;
        ImageView mImageView;
        public ImgRequest(String url,int placeholderId,int errorholderid){
            mUrl=url;
            mHolderResId=placeholderId;
            mErrorResId=errorholderid;
        }
        public void toImg(ImageView imgView){
            mImageView=imgView;
            //set placeholder when loading
            mImageView.setImageResource(mHolderResId);
            //try to find img in memory
            Bitmap cacheBitmap =mImageCache.get(mUrl);
            if(cacheBitmap!=null){
                mImageView.setImageBitmap(cacheBitmap);
                Log.d(this.getClass().getName(),"Get from memoryCache:"+mUrl);
                return;
            }



            //try to get img from file
            Bitmap diskBitmap = getBitMapFromFile();
            if (diskBitmap != null) {
                mImageView.setImageBitmap(diskBitmap);
                mImageCache.put(mUrl, diskBitmap);
                Log.d(this.getClass().getName(),"Get from fileCache:"+mUrl);
                return;
            }
            mExecutorService.submit(this);
        }
        @Override
        public void run() {
            try {
                URL loadUrl = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) loadUrl.openConnection();

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(2000);

                if (conn.getResponseCode() == 200||conn.getResponseCode() == 201) {
                    InputStream is = conn.getInputStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mImageView.setImageBitmap(bm);
                        }
                    });
                    mImageCache.put(mUrl,bm);

                    byte[]bytesEncoded = Base64.encode(mUrl.getBytes(),Base64.URL_SAFE);
                    String filename=new String(bytesEncoded);
                    File file = new File(getCacheDir(),filename);
                    FileOutputStream os = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();
                    Log.d(this.getClass().getName(),"Get from remoteServer:"+mUrl);
                } else {
                    throw new Exception("Error Return Code:"+conn.getResponseCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError();
            }
        }

        private void showError() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageResource(mErrorResId);
                }
            });
        }

        private Bitmap getBitMapFromFile(){
            byte[]bytesEncoded = Base64.encode(mUrl.getBytes(),Base64.URL_SAFE);
            String filename=new String(bytesEncoded);
            File file = new File(getCacheDir(),filename);
            if(file.exists()&&file.length()>0){
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }else{
                return null;
            }
        }
        private File getCacheDir() {
            File file;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //can release space from system clean cache
                file = mContext.getExternalCacheDir();
            } else {
                file = mContext.getCacheDir();
            }
            return file;
        }

    }
}
