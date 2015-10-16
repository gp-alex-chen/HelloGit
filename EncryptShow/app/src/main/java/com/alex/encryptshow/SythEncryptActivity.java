package com.alex.encryptshow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class SythEncryptActivity extends AppCompatActivity {

    private EditText txtContent;
    private EditText txtPassword;
    private TextView txtResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syth_encrypt);


        txtContent = (EditText) findViewById(R.id.txt_content);
        txtPassword = (EditText) findViewById(R.id.txt_passwort);
        txtResult = (TextView) findViewById(R.id.txt_result);


    }

    public void btnDesEncrypt(View view){
        // des 加密
        // 1. 所有的加密都用到 cipher
        try {
            // 3 准备key 对象
            // 3.1 DES 使用 DESKeySpec
            String content = txtContent.getText().toString();
            String password = txtPassword.getText().toString();

            byte[] keyData = password.getBytes();
            byte[] contentData = content.getBytes();
            DESKeySpec keySpec = new DESKeySpec(keyData);
            // 3.2 DesKeySpec 需要转换成 Key 对象，才能继续使用

            // 需要使用 SecretKeyFactory() 处理
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

            // 3.3 生成 key 对象
            SecretKey key = keyFactory.generateSecret(keySpec);


            Cipher cipher = Cipher.getInstance("DES");
            // 2. 设置 Cipher 时加密还是解密,同时对于对称加密设置密码

            cipher.init(Cipher.ENCRYPT_MODE,key);

            // 4. 加密
            // 返回值就是最终的加密结果
            byte[] encryptedData = cipher.doFinal(contentData);

            String str = Base64.encodeToString(encryptedData,Base64.NO_WRAP);

            txtResult.setText(str);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }


    /**
     * DES 解密
     * @param view
     */
    public void btnDesDecrypt(View view) {
        String encryptedStr = txtResult.getText().toString();

        if (encryptedStr.length() > 0 ){
            String password = txtPassword.getText().toString();
            // 在加密方法中使用 Base64对加密的内容进行编码，要解密的时候先解码
            byte[] encryptedData = Base64.decode(encryptedStr,Base64.NO_WRAP);

            byte[] keyData = password.getBytes();

            // DES 要求 8个 字节
            if (keyData.length == 8){
                // 2. 创建引擎 Cipher
                try {
                    Cipher cipher = Cipher.getInstance("DES");

                    // 4准备 key 对象
                    DESKeySpec keySpec = new DESKeySpec(keyData);
                    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                    SecretKey key = keyFactory.generateSecret(keySpec);


                    // 3 初始化
                    cipher.init(Cipher.DECRYPT_MODE,key);

                    // Cipher doFinal 将指定的参数进行转换生成结果
                    byte[] data = cipher.doFinal(encryptedData);
                    txtContent.setText(new String(data,"utf-8"));


                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }



        }

    }
}
