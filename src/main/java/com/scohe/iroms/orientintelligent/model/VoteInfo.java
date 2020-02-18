package com.scohe.iroms.orientintelligent.model;

import lombok.Data;

import java.util.List;

/**
 * @author sllzm
 * @date 2020/2/18
 */
@Data
public class VoteInfo {

    //投票状态码
    private int code;
    //待写入区块的内容
    private List<String> list;
    //待写入区块的Merkle树根节点hash值
    private String hash;

}
