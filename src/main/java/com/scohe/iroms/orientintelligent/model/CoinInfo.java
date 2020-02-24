package com.scohe.iroms.orientintelligent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sllzm
 * @date 2020/2/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinInfo {

    /**
     * 币地址
     */
    private String address;

    /**
     * 地址余额
     */
    private double balance;

}
