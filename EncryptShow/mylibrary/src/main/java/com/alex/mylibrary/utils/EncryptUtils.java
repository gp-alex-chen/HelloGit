package com.alex.mylibrary.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-12.
 */
public final class EncryptUtils {
    private EncryptUtils() {
    }


    /**
     * 将字节数组转化为16进制编码字符串
     */
    public static String toHex(byte[] data) {
        String ret = null;
        if (data != null && data.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : data) {
                int v = b & 0x0FF;
                String hex = Integer.toHexString(v);
                if (v > 0x0F) {
                    stringBuilder.append(hex);
                } else {
                    stringBuilder.append('0').append(hex);
                }
            }
            ret = stringBuilder.toString();
        }
        return ret;
    }

    /**
     * 将网址映射为文件名
     */
    public static String MD5(String url) {
        String ret = null;
        if (url != null) {
            try {
                // 创建消息摘要 使用MD5算法
                MessageDigest digest = MessageDigest.getInstance("MD5");
                byte[] data = digest.digest(url.getBytes());
                // 计算 url 对应的 MD5 生成的数据,内部包含了不可显示的字节，需要编码转化为字符串
                // 不要使用 new String(byte[]);  需要转化成0x

                // byte[] 每一个字节转换为 16进制 并且拼接成一个字符串

                ret = toHex(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * ----- DES 加密
      */
    public static byte[] desEncrypt(byte[] data, byte[] keyData) {
        byte[] ret = des(data, keyData, Cipher.ENCRYPT_MODE);
        return ret;
    }

    /**
     * DES 解密
     * @param data
     * @param keyData
     * @return
     */
    public static byte[] desDecrypt(byte[] data, byte[] keyData) {
        byte[] ret = des(data, keyData, Cipher.DECRYPT_MODE);
        return ret;
    }

    private static byte[] des(byte[] data, byte[] keyData, int decryptMode) {
        byte[] ret = null;
        if (data != null && data.length > 0 && keyData != null && keyData.length > 0) {
            try {
                // 3 准备key 对象
                // 3.1 DES 使用 DESKeySpec

                DESKeySpec keySpec = new DESKeySpec(keyData);
                // 3.2 DesKeySpec 需要转换成 Key 对象，才能继续使用

                // 需要使用 SecretKeyFactory() 处理
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

                // 3.3 生成 key 对象
                SecretKey key = keyFactory.generateSecret(keySpec);


                Cipher cipher = Cipher.getInstance("DES");
                // 2. 设置 Cipher 时加密还是解密,同时对于对称加密设置密码

                cipher.init(decryptMode, key);

                // 4. 加密
                // 返回值就是最终的加密结果
                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }





    //-------------------------------------

    /**
     * AES 加密
     * @param data
     * @param keyData
     * @return
     */
    public static byte[] aesEncrypt(byte[] data,byte[] keyData){
        byte[] ret = null;
        if (data != null && data.length>0 && keyData!= null && keyData.length == 16){
            try {
                Cipher cipher = Cipher.getInstance("AES");
                // AES 方式一 ， 单一密码情况
                SecretKeySpec keySpec = new SecretKeySpec(keyData,"AES");
                cipher.init(Cipher.ENCRYPT_MODE,keySpec);
                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }


        return ret;
    }


    /**
     * AES 解密
     * @param data
     * @param keyData
     * @return
     */
    public static byte[] aesDecrypt(byte[] data,byte[] keyData){
        byte[] ret = null;
        if (data != null && data.length>0 && keyData!= null && keyData.length == 16){
            try {
                Cipher cipher = Cipher.getInstance("AES");
                // AES 方式一 ， 单一密码情况
                SecretKeySpec keySpec = new SecretKeySpec(keyData,"AES");
                cipher.init(Cipher.DECRYPT_MODE,keySpec);
                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    // AES 带有加密模式的方法， 形成的加密强度更高，需要一个 lv 参数

    /**
     *
     * @param mode 模式
     * @param data 数据
     * @param keyData key
     * @param ivData lv data
     * @return
     */
    private static byte[] absWithIv(int mode,byte[] data,byte[] keyData,byte[] ivData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] ret = null;
        if (data!=null && data.length>0 && keyData != null && keyData.length == 16 && ivData != null && ivData.length == 16){
            //支持的加密模式
            // AES/CBC/PKCS5Padding
            // AES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            SecretKeySpec keySpec = new SecretKeySpec(keyData,"AES");

            // 准备一个 iv 参数 用于支持 CBC
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivData);

            cipher.init(mode,keySpec,ivParameterSpec);
            ret = cipher.doFinal(data);
        }

        return ret;
    }

    public static byte[] ASEEnCrypt(byte[] data,byte[]keyData,byte[] ivData) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return absWithIv(Cipher.ENCRYPT_MODE,data,keyData,ivData);
    }
    public static byte[] ASEDeCrypt(byte[] data,byte[]keyData,byte[] ivData) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        return absWithIv(Cipher.DECRYPT_MODE,data,keyData,ivData);
    }


    //RSA 密钥生成

    /**
     * 通过指定的密钥长度生成非对称的密钥堆
     * @param keySize 推荐使用 1024  2048 ，不要低于1024
     * @return
     */
    public static KeyPair generateRSAKeyPare(int keySize){
        KeyPair ret = null;
        try {
            // 生成
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

            // 初始化
            generator.initialize(keySize);

            ret = generator.generateKeyPair();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * RES 加密
     * @param data
     * @param key PublicBey privateKey
     * @return
     */
    public static byte[] resEncrypt(byte[] data,Key key){
        byte[] ret = null;
        if (data != null && data.length > 0 && key != null){
            // 创建 Cipher
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE,key);
                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        }
        return ret;

    }


    /**
     * RES 解密
     * @param data
     * @param key privateKey
     * @return
     */
    public static byte[] resDecrypt(byte[] data,Key key){
        byte[] ret = null;
        if (data != null && data.length > 0 && key != null){
            // 创建 Cipher
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE,key);
                ret = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        }
        return ret;

    }
}
