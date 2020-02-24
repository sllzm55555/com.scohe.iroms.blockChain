package com.scohe.iroms.orientintelligent.service;

import com.scohe.iroms.orientintelligent.model.DealInfo;

/**
 * @author sllzm
 * @date 2020/2/24
 */
public interface ICoinService {

    void deal(DealInfo dealInfo, Long allianceNodeId);

}
