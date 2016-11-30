package com.example.jessyuan.alldemo.helper;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.jessyuan.alldemo.listeners.ImageLoaderListener;
import com.example.jessyuan.alldemo.model.Folder;
import com.example.jessyuan.alldemo.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by JessYuan on 25/11/2016.
 */

public class ImageLoader {

    String[] columnIndexs = new String[] {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    private Context mContext;
    private ExecutorService mExecutorService;

    public ImageLoader(Context context) {
        mContext = context;
    }

    public void loadDeviceImages(ImageLoaderListener listener) {
        getExecutorService().execute(new ImageLoadRunnable(listener));
    }

    public void abortLoadImages() {
        if (mExecutorService != null) {
            getExecutorService().shutdown();
            mExecutorService = null;
        }
    }

    private ExecutorService getExecutorService() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }

        return mExecutorService;
    }

    private class ImageLoadRunnable implements Runnable {

        private ImageLoaderListener mListener;

        public ImageLoadRunnable(ImageLoaderListener listener) {
            mListener = listener;
        }

        @Override
        public void run() {
            Cursor cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columnIndexs, null, null, MediaStore.Images.Media.DATE_ADDED);

            if (cursor == null) {
                mListener.onFailed(new NullPointerException());
            }

            List<Image> temp = new ArrayList<>(cursor.getCount());
            Map<String, Folder> folderMap = new HashMap<>();

            if (cursor.moveToLast()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(columnIndexs[0]));
                    String name = cursor.getString(cursor.getColumnIndex(columnIndexs[1]));
                    String path = cursor.getString(cursor.getColumnIndex(columnIndexs[2]));
                    String bucket = cursor.getString(cursor.getColumnIndex(columnIndexs[3]));

                    File file = new File(path);
                    if (file.exists()) {
                        Image image = new Image(id, name, path, false);
                        temp.add(image);


                        Folder folder = folderMap.get(bucket);
                        if (folder == null) {
                            folder = new Folder(bucket);
                            folderMap.put(bucket, folder);
                        }

                        folder.getImages().add(image);
                    }
                } while (cursor.moveToPrevious());
            }

            cursor.close();

            List<Folder> folders = new ArrayList<>(folderMap.values());

            mListener.onImageLoaded(temp, folders);
        }
    }
}