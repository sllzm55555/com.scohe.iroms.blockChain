package com.scohe.iroms.orientintelligent.utils;

import com.google.common.base.Strings;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DeEncoderCipherUtil {

    private static final String CIPHER_MODE = "DES";
    public static String DEFAULT_DES_KEY = "DEFAULT_DES_KEY";

    public static String encrypt(String originalContent, String key){

        if(Strings.isNullOrEmpty(originalContent) || Strings.isNullOrEmpty(key)){
            return null;
        }

        try {
            byte[] byteContent = encrypt(originalContent.getBytes(), key.getBytes());

            return new BASE64Encoder().encode(byteContent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String decrypt(String cipherText, String key){

        if(Strings.isNullOrEmpty(cipherText) || Strings.isNullOrEmpty(key)){
            return null;
        }

        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] cipherTextByte = decoder.decodeBuffer(cipherText);
            byte[] contentBytes = decrypt(cipherTextByte, key.getBytes());

            return new String(contentBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private static byte[] encrypt(byte[] originalContent, byte[] key) throws Exception {

        //生成可信任的随机数源
        SecureRandom secureRandom = new SecureRandom();

        //根据密钥数据创建DESKeySpec对象
        DESKeySpec desKeySpec = new DESKeySpec(key);

        //创建密钥工厂，将DESKeySpec对象转换为SecretKey对象来保存对称密钥
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(CIPHER_MODE);
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

        //Cipher对象实际完成加密操作，指定其支持指定的加密和解密算法
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);

        //用密钥初始化Cipher对象，ENCRYPT_MODE表示加密模式
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, secureRandom);

        //返回密文
        return cipher.doFinal(originalContent);

    }

    private static byte[] decrypt(byte[] cipherTextByte, byte[] key) throws Exception {

        //生成可信任的随机数源
        SecureRandom secureRandom = new SecureRandom();

        //根据密钥数据创建DESKeySpec对象
        DESKeySpec desKeySpec = new DESKeySpec(key);

        //创建密钥工厂，将DESKeySpec对象转换为SecretKey对象来保存对称密钥
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(CIPHER_MODE);
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);

        //Cipher对象实际完成加密操作，指定其支持指定的加密和解密算法
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);

        //用密钥初始化Cipher对象，ENCRYPT_MODE表示加密模式
        cipher.init(Cipher.DECRYPT_MODE, secretKey, secureRandom);

        //返回密文
        return cipher.doFinal(cipherTextByte);

    }

}
