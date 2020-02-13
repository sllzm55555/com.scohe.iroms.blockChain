package com.scohe.iroms.orientintelligent.utils;

import com.google.common.base.Strings;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.asymmetric.KeyType;
import com.xiaoleilu.hutool.crypto.asymmetric.RSA;
import com.xiaoleilu.hutool.crypto.symmetric.DES;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;

public class DeEncoderHutoolUtil {

    private static RSA rsa = new RSA();
    private static PublicKey publicKey = rsa.getPublicKey();
    private static PrivateKey privateKey = rsa.getPrivateKey();

    public static String rsaEncrypt(String originalContent){

        if(Strings.isNullOrEmpty(originalContent)){
            return null;
        }
        return rsa.encryptStr(originalContent, KeyType.PublicKey);

    }

    public static String rsaDecrypt(String cipherText){

        if(Strings.isNullOrEmpty(cipherText)){
            return null;
        }
        return rsa.encryptStr(cipherText, KeyType.PrivateKey);

    }

    public static String desEncrypt(String originalContent, String key){

        if(Strings.isNullOrEmpty(originalContent) || Strings.isNullOrEmpty(key)){
            return null;
        }

        //随机生成Key
        byte[] keyByte = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();

        DES des = SecureUtil.des(key.getBytes());

        return des.encryptHex(originalContent);

    }

    public static String desDecrypt(String cipherText, String key){

        if(Strings.isNullOrEmpty(cipherText) || Strings.isNullOrEmpty(key)){
            return null;
        }

        DES des = SecureUtil.des(key.getBytes());

        return des.decryptStr(cipherText);

    }


}
