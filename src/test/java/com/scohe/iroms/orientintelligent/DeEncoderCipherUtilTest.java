package com.scohe.iroms.orientintelligent;

import com.scohe.iroms.orientintelligent.utils.DeEncoderCipherUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeEncoderCipherUtilTest {

    private String cipherTextGlobal;

    @Test
    public void testEncrypt(){
        String originalContent = null;
        String key = null;
        Assert.assertNull(DeEncoderCipherUtil.encrypt(originalContent, key));

        originalContent = "123";
        key = null;
        Assert.assertNull(DeEncoderCipherUtil.encrypt(originalContent, key));

        originalContent = "123";
        key = "12345123123123123";
        cipherTextGlobal = DeEncoderCipherUtil.encrypt(originalContent, key);
        Assert.assertEquals(cipherTextGlobal, "x3AHZOIdUZw=");

    }

    @Test(dependsOnMethods = "testEncrypt")
    public void testDecrypt(){

        String key = "12345123123123123";
        Assert.assertEquals(DeEncoderCipherUtil.decrypt(cipherTextGlobal, key), "123");

    }



}
