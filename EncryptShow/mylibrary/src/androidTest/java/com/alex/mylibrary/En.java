package com.alex.mylibrary;

import com.alex.mylibrary.utils.EncryptUtils;

import junit.framework.TestCase;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by
 * Author: alex
 * Email:
 * Date: 15-10-16.
 */
public class En extends TestCase {
    public void testFeneratorRSAKeyPair(){
        KeyPair pair = EncryptUtils.generateRSAKeyPare(1024);
        PublicKey aPublic = pair.getPublic();

        PrivateKey aPrivate = pair.getPrivate();

        String str = "Hello World";
        byte[] enceyptedData = EncryptUtils.resEncrypt(str.getBytes(), aPrivate);

        byte[] data = EncryptUtils.resDecrypt(enceyptedData, aPublic);

        String s1 = new String(data);

        assertEquals(str,s1);
    }

    


}
