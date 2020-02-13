package com.scohe.iroms.orientintelligent;

import com.scohe.iroms.orientintelligent.utils.DeEncoderHutoolUtil;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeEncoderHutoolUtilTest {

    private String cipherTextGlobal;
    private String key;

    @Test
    public void testEncrypt(){
        String originalContent = null;
        key = null;
        Assert.assertNull(DeEncoderHutoolUtil.desEncrypt(originalContent, key));

        originalContent = "123";
        key = null;
        Assert.assertNull(DeEncoderHutoolUtil.desEncrypt(originalContent, key));

        key = new String(SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded());
        cipherTextGlobal = DeEncoderHutoolUtil.desEncrypt(originalContent, key);
        Assert.assertNotNull(cipherTextGlobal);

    }

    @Test(dependsOnMethods = "testEncrypt")
    public void testDecrypt(){

        Assert.assertEquals(DeEncoderHutoolUtil.desDecrypt(cipherTextGlobal, key), "123");

    }



}
