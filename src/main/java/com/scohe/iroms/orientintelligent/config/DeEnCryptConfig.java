package com.scohe.iroms.orientintelligent.config;

import com.google.crypto.tink.config.TinkConfig;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;

@Configuration
public class DeEnCryptConfig {

    public void register(){
        try {
            TinkConfig.register();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

}
