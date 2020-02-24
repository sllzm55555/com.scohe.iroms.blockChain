package com.scohe.iroms.orientintelligent.controller;

import com.scohe.iroms.orientintelligent.model.CoinInfo;
import com.scohe.iroms.orientintelligent.model.DealInfo;
import com.scohe.iroms.orientintelligent.service.ICoinService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sllzm
 * @date 2020/2/24
 */
@RestController
@Api(tags = "测试交易")
public class TestController {


    private final ICoinService coinService;

    @Autowired
    public TestController(ICoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("test")
    public void test(){
        DealInfo dealInfo = new DealInfo();
        dealInfo.setFromUserId(1L);
        dealInfo.setToUserId(2L);

        dealInfo.setBlockCost(0.5);
        dealInfo.setDealCost(3.5);

        List<CoinInfo> coinInfos = new ArrayList<>();
        coinInfos.add(new CoinInfo("aaa", 1));
        coinInfos.add(new CoinInfo("bbb", 2));
        coinInfos.add(new CoinInfo("ccc", 3));

        dealInfo.setFromCoinInfos(coinInfos);

        coinService.deal(dealInfo, 1L);
    }

}
