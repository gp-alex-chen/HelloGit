package com.alex.encryptshow.utils;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-16.
 *
 * <ol>
 *     <li> Hex 编码</li>
 * </ol>
 *
 *
 */
public final class EncryptUtil {
    private EncryptUtil(){

    }

    /**
     * 一个字节变成两个字符 数据变为原始数据2倍
     * @param data 数据源
     * @return
     */
    public static String toHex(byte[] data){
        String ret = null;
        // TODO 将字节数组转换为字符串
        if (data != null && data.length > 0){
            StringBuilder sb = new StringBuilder();
            for (byte b : data) {

                // 分别获取高四位，低四位的内容
                int h = (b >> 4) & 0x0f;
                int l = b & 0x0f;

                char ch , cl;

                if (h > 9){
                    ch = (char) ('A' + (h - 10));
                }else {
                    ch = (char)('0'+h);
                }

                if (l > 9){
                    cl = (char) ('A' + (l - 10));
                }else {
                    cl = (char)('0'+l);
                }

                sb.append(ch).append(cl);

                }
                ret = sb.toString();
            }

        return ret;
    }


    /**
     *
     * @param str
     * @return
     */
    public static byte[] fromHex(String str){
        byte[] ret = null;
        // TODO 将Hex编码的字符串 还原为原始的字节数组

        if (str != null) {
            int len = str.length();
            // 检查长度是否合法
            if (len > 0 && len % 2 == 0){

                char[] chs = str.toCharArray();
                ret = new byte[len / 2];

                for (int i = 0,j = 0; i < len-1; i += 2, j++){
                    char ch = chs[i];
                    char cl = chs[i + 1];

                    int ih = 0,il = 0,v;
                    if (ch >= 'A'){
                        ih = 10 + (ch - 'A');
                    }else if (ch >= '0'){
                        ih = ch - '0';
                    }


                    if (cl >= 'A'){
                        il = 10 + (cl - 'A');
                    }else if (cl >= '0'){
                        il = cl - '0';
                    }

                    v = ((ih & 0x0f) << 4) | (il & 0x0f);
                    ret[i/2] = (byte)v;
                }
            }


        }
        return ret;
    }


}
