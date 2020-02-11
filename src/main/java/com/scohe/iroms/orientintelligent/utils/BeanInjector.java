package com.scohe.iroms.orientintelligent.utils;

import com.scohe.iroms.orientintelligent.model.BlockChain;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class BeanInjector {

    private BlockChain blockChain;
    private String nodeId;

    @Qualifier("nodeId")
    @Bean
    private String nodeId() {
        synchronized (this) {
            if (StringUtils.isEmpty(nodeId)) {
                nodeId = UUID.randomUUID().toString().replace("-", "");
            }
        }
        return nodeId;
    }

    @Bean
    @Qualifier("blockChain")
    private BlockChain blockChain() {
        synchronized (this) {
            if (blockChain == null) {
                blockChain = new BlockChain();
                blockChain.newSeedBlock();
            }
        }
        return blockChain;
    }

}
