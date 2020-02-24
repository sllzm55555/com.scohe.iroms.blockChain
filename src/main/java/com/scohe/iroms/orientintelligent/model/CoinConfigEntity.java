package com.scohe.iroms.orientintelligent.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author sllzm
 * @date 2020/2/24
 */
@Entity
@Table(name = "bc_coin_config")
@Data
public class CoinConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易类型
     */
    private String bizType;

    /**
     * 币的总量
     */
    private int totalCoin;

    /**
     * 预留币的数量
     */
    private int reservedCoin;

    /**
     * 每笔交易最小金额
     */
    private double minCoinPerDeal;

    /**
     * 创建新区块的奖励币
     */
    private double createBlockRewardCoin;

    /**
     * 配置信息创建时间
     */
    private Date createTime;

    /**
     * 配置信息更新时间
     */
    private Date updateTime;

}
