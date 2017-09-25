package com.ckt.admin.myapplication.util;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ckt.admin.myapplication.Exif.ExifInterface;

import java.io.File;

/**
 * Created by admin on 2017/9/25.
 */

public class FileSaveServices extends Service {
    private final String TAG = "FileSaveServices";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public void testMethod1() {
        Log.e("FileSaveServices", "liang.chen testMethod1()");
    }

    public class MyBinder extends Binder {

        public FileSaveServices getService() {
            return FileSaveServices.this;
        }
    }

    private MyBinder myBinder = new MyBinder();

    public void saveImageofJpeg(byte[] imgDatas, String title, long data, int width, int height,
                                String format, OnImageSaveListener listener, ContentResolver contentResolver, ExifInterface exifInterface) {
        SaveAsyncTask saveAsyncTask = new SaveAsyncTask(imgDatas, title, data, width, height, format, listener, contentResolver, exifInterface);
        saveAsyncTask.execute();
    }

    public class SaveAsyncTask extends AsyncTask<Void, Void, Uri> {
        private byte[] mImgDatas;
        private String mTitle;
        private long mData;
        private int mWidth;
        private int mHeight;
        private String mFormat;
        private OnImageSaveListener mListener;
        private ContentResolver mContentResolver;
        private ExifInterface mExifInterface;

        public SaveAsyncTask(byte[] imgDatas, String title, long data, int width, int height,
                             String format, OnImageSaveListener listener, ContentResolver contentResolver, ExifInterface exifInterface) {

            this.mImgDatas = imgDatas;
            this.mTitle = title;
            this.mData = data;
            this.mWidth = width;
            this.mHeight = height;
            this.mFormat = format;
            this.mListener = listener;
            this.mContentResolver = contentResolver;
            this.mExifInterface = exifInterface;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "start save jpeg image");
        }

        @Override
        protected Uri doInBackground(Void... voids) {
            Log.d(TAG, "progressing to save jpeg image");
            //mTitle = mTitle + ".jpeg";
            String fileName = Storage.getImageFilePath(mTitle);
            //if (mFormat.contains("jpeg")) {
            Storage.writeJpeg(fileName, mImgDatas, null);
            // }
            //还未将相关信息添加到数据库里
            return null;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
            mListener.onImageSaveFinish();
            Log.d(TAG, "liang.chen save img end");

        }
    }

    public interface OnImageSaveListener {
        public void onImageSaveFinish();
    }

}