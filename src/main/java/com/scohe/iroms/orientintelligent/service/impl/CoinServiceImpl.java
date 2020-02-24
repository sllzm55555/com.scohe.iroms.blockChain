package com.scohe.iroms.orientintelligent.service.impl;

import com.google.common.collect.Ordering;
import com.scohe.iroms.orientintelligent.model.CoinDealEntity;
import com.scohe.iroms.orientintelligent.model.CoinInfo;
import com.scohe.iroms.orientintelligent.model.DealInfo;
import com.scohe.iroms.orientintelligent.model.UserWalletEntity;
import com.scohe.iroms.orientintelligent.repository.ICoinDealRepository;
import com.scohe.iroms.orientintelligent.repository.IUserWalletRepository;
import com.scohe.iroms.orientintelligent.service.ICoinService;
import com.xiaoleilu.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author sllzm
 * @date 2020/2/24
 */
@Service
@Slf4j
public class CoinServiceImpl implements ICoinService {

    private final IUserWalletRepository userWalletDao;
    private final ICoinDealRepository coinDealDao;
    /**
     * 节点新生成区块的奖励数量
     */
    private static final int CREATE_BLOCK_REWARD = 100;

    @Autowired
    public CoinServiceImpl(IUserWalletRepository userWalletRepository, ICoinDealRepository coinDealRepository) {
        userWalletDao = userWalletRepository;
        coinDealDao = coinDealRepository;
    }

    @Override
    public void deal(DealInfo dealInfo, Long allianceNodeId) {

        //校验联盟节点
        if(allianceNodeId <= 0){
            return;
        }
        if(dealInfo == null){
            return;
        }
        //校验交易信息
        if(!checkDealInfo(dealInfo)){
            return;
        }
        processDeal(dealInfo, allianceNodeId);
    }

    private boolean checkDealInfo(DealInfo dealInfo){
        //校验交易双方Id
        if(dealInfo.getFromUserId() <= 0 || dealInfo.getToUserId() <= 0){
            return false;
        }
        //如果生成区块的费用<=0
        if(dealInfo.getBlockCost() <= 0){
            return false;
        }
        //如果交易发起方的币列表为空
        if(dealInfo.getFromCoinInfos().isEmpty()){
            return false;
        }
        //获取交易发起方的余额总数
        double allBalance = getAllBalance(dealInfo.getFromCoinInfos());
        //如果余额小于交易费用+区块创建费用
        if(allBalance < (dealInfo.getDealCost() + dealInfo.getBlockCost())){
            return false;
        }
        return true;
    }

    private double getAllBalance(List<CoinInfo> coinInfos){
        double result = 0d;
        for (CoinInfo coinInfo : coinInfos) {
            result += coinInfo.getBalance();
        }
        return result;
    }

    private void processDeal(DealInfo dealInfo, Long allianceNodeId){

        processAndSaveDeal(dealInfo);

        createBlockToAlliance(allianceNodeId);

    }

    private void processAndSaveDeal(DealInfo dealInfo){

        Long fromUserId = dealInfo.getFromUserId();
        Long toUserId = dealInfo.getToUserId();
        double blockCost = dealInfo.getBlockCost();
        List<CoinInfo> fromCoinInfos = dealInfo.getFromCoinInfos();
        double dealCost = dealInfo.getDealCost();

        //对CoinInfo按余额从小到大排序
        Ordering<CoinInfo> coinInfoOrdering = new Ordering<CoinInfo>() {
            @Override
            public int compare(@Nullable CoinInfo left, @Nullable CoinInfo right) {
                return Double.compare(left.getBalance(), right.getBalance());
            }
        };
        fromCoinInfos = coinInfoOrdering.sortedCopy(fromCoinInfos);

        //查找满足交易的一个或多个address及其balance
        int index = 0, length = fromCoinInfos.size();
        double count = 0;

        for (int i = 0; i < length; i++) {
            CoinInfo coinInfo = fromCoinInfos.get(i);

            count += coinInfo.getBalance();

            if(count <= dealCost){
                //保存该条币信息到交易接收方，同时将交易数据入库
                saveIntoTable(dealInfo.getFromUserId(), dealInfo.getToUserId(),
                        coinInfo.getAddress(),coinInfo.getAddress(),
                        coinInfo.getBalance(),coinInfo.getBalance());
            }

            //如果地址下对应的余额大于交易金额，拆分账户
            if(count > dealCost){
                index = i;

                double giveCoin = dealCost - (count - coinInfo.getBalance());

                //生成一个新币地址
                String newAddress = DigestUtil.sha256Hex(String.format("create new Address at [%s]", System.currentTimeMillis()));

                //交易接收方
                saveIntoTable(fromUserId, toUserId, null, newAddress, 0, giveCoin);

                //交易发起方扣款
                saveIntoTable(fromUserId, fromUserId, coinInfo.getAddress(), coinInfo.getAddress(), coinInfo.getBalance(), coinInfo.getBalance() - giveCoin );
                coinInfo.setBalance(coinInfo.getBalance() - giveCoin);
            }
        }

        //处理打包成区块的费用
        for (int i = index; i < length; i++) {
            CoinInfo coinInfo = fromCoinInfos.get(i);
            toUserId = 888L;

            //钱包余额 < 打包费用
            if(coinInfo.getBalance() <= blockCost){
                saveIntoTable(fromUserId, toUserId, coinInfo.getAddress(), coinInfo.getAddress(), coinInfo.getBalance(), coinInfo.getBalance());

                blockCost -= coinInfo.getBalance();
                if(blockCost == 0.0){
                    return;
                }
            }
            //钱包余额 > 打包费用
            if(coinInfo.getBalance() > blockCost){
                //生成一个新币地址
                String newAddress = DigestUtil.sha256Hex(String.format("create new Address at [%s]", System.currentTimeMillis()));

                //将打包费用转入超级账户
                saveIntoTable(fromUserId, toUserId, null, newAddress, 0, blockCost);

                //交易发起方扣款
                saveIntoTable(fromUserId, fromUserId, coinInfo.getAddress(), coinInfo.getAddress(), coinInfo.getBalance(), coinInfo.getBalance() - blockCost);
                coinInfo.setBalance(coinInfo.getBalance() - blockCost);
                return;
            }
        }
    }

    private void saveIntoTable(Long fromUserId, Long toUserId, String fromAddress, String toAddress, double balanceBefore, double balanceAfter){

        UserWalletEntity userWalletEntity = new UserWalletEntity(null, toUserId, toAddress, balanceAfter, new Date(), new Date());

        userWalletDao.save(userWalletEntity);

        CoinDealEntity coinDealEntity = new CoinDealEntity(null, fromUserId, fromAddress, balanceBefore, toUserId, toAddress, balanceAfter, new Date());

        coinDealDao.save(coinDealEntity);

    }

    private void createBlockToAlliance(Long allianceNodeId){

    }
}
