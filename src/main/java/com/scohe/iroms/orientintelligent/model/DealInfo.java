package com.scohe.iroms.orientintelligent.model;

import lombok.Data;

import java.util.List;

/**
 * @author sllzm
 * @date 2020/2/24
 */
@Data
public class DealInfo {

    /**
     * 交易发起人
     */
    private Long fromUserId;

    /**
     * 交易发起方币信息
     */
    private List<CoinInfo> fromCoinInfos;

    /**
     * 交易接受方
     */
    private Long toUserId;

    /**
     * 交易费用
     */
    private double dealCost;

    /**
     * 打包成区块的费用
     */
    private double blockCost;

}
