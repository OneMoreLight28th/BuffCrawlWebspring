package com.example.transaction.balance.service;

import com.example.transaction.balance.dto.UserBalanceDTO;

import java.math.BigDecimal;

public interface UserBalanceService {
    UserBalanceDTO getUserBalanceById(Integer userId);

    boolean updateUserBalance(Integer userId, BigDecimal newBalance);

    boolean recharge(Integer userId, BigDecimal amount);

    boolean withdraw(Integer userId, BigDecimal amount);
}
