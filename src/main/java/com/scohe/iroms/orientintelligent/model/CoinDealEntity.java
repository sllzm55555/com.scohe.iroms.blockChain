package com.scohe.iroms.orientintelligent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author sllzm
 * @date 2020/2/24
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bc_coin_deal")
@Data
public class CoinDealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易发起方id
     */
    private Long fromUserId;

    /**
     * 交易发起方地址
     */
    private String fromAddress;

    /**
     * 交易前账户余额
     */
    private double coinBalanceBeforeDeal;

    /**
     * 交易接受方id
     */
    private Long toUserId;

    /**
     * 交易接受方地址
     */
    private String toAddress;

    /**
     * 交易后账户余额
     */
    private double coinBalanceAfterDeal;

    /**
     * 交易创建时间
     */
    private Date createTime;

}
