package com.alex.encryptshow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.alex.encryptshow.utils.EncryptUtil;
import com.alex.mylibrary.utils.StreamUtil;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // base64 编码

        String str = "一个测试数据";

        // 将字节数组参数转换为 Base64 字符串， 第二个参数时不进行换行处理
        // 参数2 采用什么内容， 解码时也要设置相同的内容
        String encode = Base64.encodeToString(str.getBytes(),Base64.NO_WRAP);
        Log.d("Base64","编码结果"+ encode);

        // Base64 解码
        byte[] buf = Base64.decode(encode, Base64.NO_WRAP);
        String ns = new String(buf);
        Log.d("Base64","解码结果"+ns);


        // Base64 编码字节数组

        byte[] data = new byte[]{0,0,0};
        encode = Base64.encodeToString(data,Base64.NO_WRAP);

        // 解码 还原字节数组
        byte[] di = Base64.decode(encode, Base64.NO_WRAP);

        byte[] d2 = new byte[]{1,2};
        String hex = EncryptUtil.toHex(d2);
        Log.d("hex", "d2" + hex);


        // 开启网络 检查 URLEncoding 编码的设置
        Thread thread = new Thread(this);

        thread.start();

        FileInputStream fin = null;
        try {
            fin = new FileInputStream("ahc");
            DataInputStream din = new DataInputStream(fin);
            din.readInt();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void run() {

        try {

            String word = URLEncoder.encode("变形金刚","utf-8");

            // URLEncoding 的解码
            URLDecoder.decode(word,"utf-8");

            URL url = new URL("http://news.baidu.com/ns?word=变形金刚");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int code = conn.getResponseCode();

            Log.d("URLEncoding","code"+code);
            if (code == 200){
                // TODO 进行内容获取
                byte[] data = StreamUtil.readStream(conn.getInputStream());
                String s = new String(data, "utf-8");
                Log.d("Encoding","str="+s);
            }
            conn.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
