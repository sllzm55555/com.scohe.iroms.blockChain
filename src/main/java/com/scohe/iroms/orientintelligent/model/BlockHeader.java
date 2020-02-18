package com.scohe.iroms.orientintelligent.model;

import lombok.Data;

import java.util.List;

/**
 * @author sllzm
 * @date 2020/2/18
 */
@Data
public class BlockHeader {

    private int version;
    private String previousBlockHash;
    private String merkleRootHash;
    private String publicKey;
    private String serialNumber;
    private Long timeStamp;
    //32位随机数
    private Long nonce;
    //该区块每条交易的hash集合
    private List<String> hashList;

}
