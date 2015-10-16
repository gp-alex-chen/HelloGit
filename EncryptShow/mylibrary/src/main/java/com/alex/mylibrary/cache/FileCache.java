package com.alex.mylibrary.cache;

import android.content.Context;
import android.os.Environment;

import com.alex.mylibrary.utils.EncryptUtils;
import com.alex.mylibrary.utils.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-12.
 */
public class FileCache {
    private static FileCache ourInstance;

    public static FileCache newInstance(Context context){
        if (context != null){

            if (ourInstance == null) {
                ourInstance = new FileCache(context);
            }


        }else{
            throw new IllegalArgumentException("Context must be set");

        }

        return ourInstance;
    }

    public static FileCache getInstance(){
        if (ourInstance == null){
            throw new IllegalStateException("newInstance invoke");
        }
        return ourInstance;
    }

    private Context context;
    private FileCache(Context context) {
        this.context = context;
    }


    /**
     * 从文件存储加载对应网址的内容
     */
    public byte[] load(String url){
        // 通过网址找文件
        byte[] ret = null;
        if (url != null){
            // 最终的文件缓存目录
            File cacheDir;
            String state = Environment.getExternalStorageState();
            // 获取存储卡的缓存目录
            if (Environment.MEDIA_BAD_REMOVAL.equals(state))
                cacheDir = context.getExternalCacheDir();
            else {
                // 内部缓存
                cacheDir = context.getCacheDir();
            }
            // 映射文件名成
            String fileName = EncryptUtils.MD5(url);
            File targetFile = new File(cacheDir,fileName);
            if (targetFile.exists()){

                FileInputStream fin = null;
                try {
                    fin = new FileInputStream(targetFile);

                    ret = StreamUtil.readStream(fin);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    StreamUtil.close(fin);
                }
            }
        }
        return ret;
    }

    /**
     * 保存对应网址的数据到文件中
     */
    public void save(String url, byte[] data){
        // 通过网址存文件

        if (url != null && data != null){

            // 最终的文件缓存目录
            File cacheDir = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_BAD_REMOVAL.equals(state)){
                // 获取存储卡的缓存目录
                cacheDir = context.getExternalCacheDir();

            }else {
                // 内部缓存
                cacheDir = context.getCacheDir();
            }

            // 映射文件名成
            String fileName = EncryptUtils.MD5(url);
            File targetFile = new File(cacheDir,fileName);
            //IO
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(targetFile);
                fout.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
