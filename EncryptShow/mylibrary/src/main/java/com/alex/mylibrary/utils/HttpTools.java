package com.alex.mylibrary.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-12.
 */
public final class HttpTools {

    private HttpTools(){

    }

    public static byte[] doGet(String url){
        byte[] ret = null;

        if (url != null) {


            HttpURLConnection connection = null;
            InputStream inputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;

            try {
                URL u = new URL(url);
                connection = (HttpURLConnection) u.openConnection();
                connection.connect();
                int code = connection.getResponseCode();
                if (code == 200){
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    inputStream = connection.getInputStream();

                    //支持解压缩
                    String encoding = connection.getContentEncoding();
                    if ("gzip".equals(encoding)){
                        inputStream = new GZIPInputStream(inputStream);
                    }

                    ret = StreamUtil.readStream(inputStream);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return ret;
    }

}
