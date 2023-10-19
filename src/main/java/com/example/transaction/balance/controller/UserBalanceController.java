package com.example.transaction.balance.controller;

import com.example.transaction.balance.dto.UserBalanceDTO;
import com.example.transaction.balance.service.UserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/user/balance")
public class UserBalanceController {
    @Autowired
    private UserBalanceService userBalanceService;

    @GetMapping("/{userId}")
    public UserBalanceDTO getUserBalance(@PathVariable Integer userId) {
        return userBalanceService.getUserBalanceById(userId);
    }

    @PutMapping("/{userId}/{newBalance}")
    public boolean updateUserBalance(@PathVariable Integer userId, @PathVariable BigDecimal newBalance) {
        return userBalanceService.updateUserBalance(userId, newBalance);
    }

    @PostMapping("/recharge/{userId}/{amount}")
    public boolean recharge(@PathVariable Integer userId, @PathVariable BigDecimal amount) {
        return userBalanceService.recharge(userId, amount);
    }

    @PostMapping("/withdraw/{userId}/{amount}")
    public boolean withdraw(@PathVariable Integer userId, @PathVariable BigDecimal amount) {
        return userBalanceService.withdraw(userId, amount);
    }
}