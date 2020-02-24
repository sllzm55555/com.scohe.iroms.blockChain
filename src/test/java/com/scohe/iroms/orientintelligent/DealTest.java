package com.scohe.iroms.orientintelligent;

import com.scohe.iroms.orientintelligent.model.CoinDealEntity;
import com.scohe.iroms.orientintelligent.model.CoinInfo;
import com.scohe.iroms.orientintelligent.model.DealInfo;
import com.scohe.iroms.orientintelligent.model.UserWalletEntity;
import com.scohe.iroms.orientintelligent.repository.ICoinDealRepository;
import com.scohe.iroms.orientintelligent.repository.IUserWalletRepository;
import com.scohe.iroms.orientintelligent.service.ICoinService;
import com.scohe.iroms.orientintelligent.service.impl.CoinServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.event.TransactionalEventListener;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sllzm
 * @date 2020/2/24
 */
public class DealTest {

    @Mock
    private IUserWalletRepository userWalletRepository;
    @Mock
    private ICoinDealRepository coinDealRepository;
    @InjectMocks
    private CoinServiceImpl coinService;

    @BeforeTest
    public void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeal(){

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
