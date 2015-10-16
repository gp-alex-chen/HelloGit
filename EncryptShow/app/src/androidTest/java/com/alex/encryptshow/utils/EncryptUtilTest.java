package com.alex.encryptshow.utils;

import junit.framework.TestCase;

import java.util.Arrays;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-16.
 */
public class EncryptUtilTest extends TestCase {
    public void testToHex(){
        byte[] data = new byte[]{1,2};
        String hex = EncryptUtil.toHex(data);

        // 检查 hex 的值是否是 0102
        assertEquals("0102", hex);

        byte[] d1 = EncryptUtil.fromHex(hex);


        assertTrue(Arrays.equals(data,d1));
    }
}
