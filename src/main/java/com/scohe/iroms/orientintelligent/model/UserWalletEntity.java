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
@NoArgsConstructor
@AllArgsConstructor
@Data@Entity
@Table(name = "bc_user_wallet")
public class UserWalletEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 币地址
     */
    private String coinAddress;

    /**
     * 币地址对应的余额
     */
    private double coinBalance;

    /**
     * 交易创建时间
     */
    private Date createTime;

    /**
     * 交易更新时间
     */
    private Date updateTime;



}
