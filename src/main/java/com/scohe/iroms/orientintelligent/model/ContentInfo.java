package com.scohe.iroms.orientintelligent.model;

import lombok.Data;

/**
 * @author sllzm
 * @date 2020/2/18
 */
@Data
public class ContentInfo {

    private String jsonContent;
    private Long timestamp;
    private String publicKey;
    private String sign;
    private String hash;

}
