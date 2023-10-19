package com.example.transaction.balance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transaction.balance.dto.UserBalanceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface UserBalanceMapper extends BaseMapper<UserBalanceDTO> {
    @Update("UPDATE cs_balance SET user_balance = #{newBalance}, version = version + 1 WHERE user_id = #{userId} AND version = #{version}")
    int updateBalanceWithOptimisticLock(@Param("userId") Integer userId, @Param("newBalance") BigDecimal newBalance, @Param("version") Integer version);
}