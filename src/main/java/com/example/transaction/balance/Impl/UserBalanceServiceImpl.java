package com.example.transaction.balance.Impl;

import com.example.transaction.balance.dto.UserBalanceDTO;
import com.example.transaction.balance.mapper.UserBalanceMapper;
import com.example.transaction.balance.service.UserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserBalanceServiceImpl implements UserBalanceService {

    private static final BigDecimal MAX_RECHARGE_AMOUNT = new BigDecimal("50000");
    private static final BigDecimal MAX_WITHDRAW_AMOUNT = new BigDecimal("50000");

    @Autowired
    private UserBalanceMapper userBalanceMapper;

    @Override
    public UserBalanceDTO getUserBalanceById(Integer userId) {
        return userBalanceMapper.selectById(userId);
    }

    @Override
    public boolean updateUserBalance(Integer userId, BigDecimal newBalance) {
        UserBalanceDTO userBalance = new UserBalanceDTO();
        userBalance.setUserId(userId);
        userBalance.setUserBalance(newBalance);
        return userBalanceMapper.updateById(userBalance) > 0;
    }

    @Override
    public boolean recharge(Integer userId, BigDecimal amount) {
        if (amount.compareTo(MAX_RECHARGE_AMOUNT) > 0) {
            return false;
        }

        int retryTimes = 3;
        while (retryTimes > 0) {
            UserBalanceDTO userBalance = getUserBalanceById(userId);
            if (userBalance == null) {
                return false;
            }
            BigDecimal newBalance = userBalance.getUserBalance().add(amount);
            int updatedRows = userBalanceMapper.updateBalanceWithOptimisticLock(userId, newBalance, userBalance.getVersion());
            if (updatedRows > 0) {
                return true;
            }
            retryTimes--;
        }
        return false;
    }

    @Override
    public boolean withdraw(Integer userId, BigDecimal amount) {
        if (amount.compareTo(MAX_WITHDRAW_AMOUNT) > 0) {
            return false;
        }

        int retryTimes = 3;
        while (retryTimes > 0) {
            UserBalanceDTO userBalance = getUserBalanceById(userId);
            if (userBalance == null || userBalance.getUserBalance().compareTo(amount) < 0) {
                return false;
            }
            BigDecimal newBalance = userBalance.getUserBalance().subtract(amount);
            int updatedRows = userBalanceMapper.updateBalanceWithOptimisticLock(userId, newBalance, userBalance.getVersion());
            if (updatedRows > 0) {
                return true;
            }
            retryTimes--;
        }
        return false;
    }
}
