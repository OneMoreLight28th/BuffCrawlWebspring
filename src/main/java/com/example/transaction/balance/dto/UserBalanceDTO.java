package com.example.transaction.balance.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("cs_balance")
public class UserBalanceDTO {
    private Integer userId;
    private BigDecimal userBalance;
    @Version
    private Integer version;
}
