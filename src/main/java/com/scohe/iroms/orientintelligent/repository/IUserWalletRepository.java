package com.scohe.iroms.orientintelligent.repository;

import com.scohe.iroms.orientintelligent.model.CoinConfigEntity;
import com.scohe.iroms.orientintelligent.model.UserWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sllzm
 * @date 2020/2/24
 */
@Repository
public interface IUserWalletRepository extends JpaRepository<UserWalletEntity, Long> {

    UserWalletEntity findByCoinAddress(String coinAddress);

}
