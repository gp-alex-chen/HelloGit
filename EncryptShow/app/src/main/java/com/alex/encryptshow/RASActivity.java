package com.alex.encryptshow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alex.mylibrary.utils.EncryptUtils;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RASActivity extends AppCompatActivity {

    private EditText txtContent;
    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ras);


        txtContent = (EditText) findViewById(R.id.txt_content);
        share = getSharedPreferences("configration", Context.MODE_PRIVATE);



    }

    // 加密数据
    public void btnRASEncrypt(View view) {
        String publicBase64 = share.getString("public", "error");
        String privateBase64 = share.getString("private", "error");
        String content = share.getString("content", "error");
        SharedPreferences.Editor editor = editor = share.edit();
        String data = txtContent.getText().toString();

        if ("error".equals(publicBase64) && "error".equals(privateBase64)){


            KeyPair keyPair = EncryptUtils.generateRSAKeyPare(1024);

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            byte[] publicKeyByte = publicKey.getEncoded();
            byte[] privateKeyByte = privateKey.getEncoded();

            publicBase64 = Base64.encodeToString(publicKeyByte, Base64.NO_WRAP);
            privateBase64 = Base64.encodeToString(privateKeyByte, Base64.NO_WRAP);

            editor.putString("public", publicBase64);
            editor.putString("private", privateBase64);
            editor.apply();
            // 加密
            byte[] encryptData = EncryptUtils.resEncrypt(data.getBytes(), publicKey);
            editor.putString("content", Base64.encodeToString(encryptData,Base64.NO_WRAP));
            editor.apply();
        }



        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicBase64.getBytes(), Base64.NO_WRAP)));
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateBase64.getBytes(), Base64.NO_WRAP)));

            byte[] encryptData = EncryptUtils.resEncrypt(data.getBytes(), publicKey);
            editor.putString("content", Base64.encodeToString(encryptData,Base64.NO_WRAP));

            editor.apply();


        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }


    // 解密数据
    public void btnRASDecrypt(View view) {
        String publicBase64 = share.getString("public", "error");
        String privateBase64 = share.getString("private", "error");
        String content = share.getString("content", "error");


        if ("error".equals(publicBase64)){
        }else{
            Log.d("cc","public"+publicBase64);
            Log.d("cc","private"+privateBase64);
            Log.d("cc","content"+content);

            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                try {
                    PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicBase64.getBytes(), Base64.NO_WRAP)));
                    PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateBase64.getBytes(), Base64.NO_WRAP)));
                    byte[] privateData = EncryptUtils.resDecrypt(Base64.decode(content.getBytes(), Base64.NO_WRAP), privateKey);
                    TextView textView = (TextView) findViewById(R.id.txt_result);
                    textView.setText(new String(privateData,"utf-8"));
                } catch (InvalidKeySpecException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }


}
