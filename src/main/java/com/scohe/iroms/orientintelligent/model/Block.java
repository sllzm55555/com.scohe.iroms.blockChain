package com.scohe.iroms.orientintelligent.model;

import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import lombok.Data;

/**
 * @author sllzm
 * @date 2020/2/18
 */
@Data
public class Block {

    private BlockHeader blockHeader;

    private BlockBody blockBody;

    private String blockHash;

    public String getBlockHash() {
        return DigestUtil.sha256Hex(blockHeader.toString() + blockBody.toString());
    }
}
