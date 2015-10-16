package com.alex.mylibrary.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-12.
 */
public final class StreamUtil {
    private StreamUtil(){
    }

    public static void close(Object stream){
        if (stream != null) {
            try {
                if (stream instanceof InputStream){
                    ((InputStream)stream).close();
                }else if (stream instanceof OutputStream){
                    ((OutputStream)stream).close();
                }else if (stream instanceof Reader){
                    ((Reader)stream).close();
                }else if (stream instanceof Writer){
                    ((Writer)stream).close();
                }else if (stream instanceof HttpURLConnection){
                    ((HttpURLConnection)stream).disconnect();
                }

            }catch (Exception ex){

            }
        }
    }

    public static byte[] readStream(InputStream inputStream) throws IOException {
        byte[] ret = null;
        if (inputStream != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            buffer = null;
            ret = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        }
        return ret;
    }
}
