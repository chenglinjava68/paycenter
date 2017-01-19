/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.dao.pay;

import com.qccr.paycenter.dal.domain.pay.CardInfoDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CardInfoDao {
    /**
     *
     * 删除单条记录
     */
    int delete(Long id);

    /**
     *
     * 增加单条记录
     */
    int insert(CardInfoDO record);

    /**
     *
     * 查询记录
     */
    List<CardInfoDO> query(CardInfoDO condition);

    /**
     *
     * 更新单条记录
     */
    int update(CardInfoDO record);

    /**
     * 查卡号
     * @param cardNo
     * @return
     */
//    CardInfoDO findByCardNo(String cardNo);

    /**
     * 标记已经签约
     * @param cardNo
     * @return
     */
//    int signCard(String cardNo);

    /**
     * 用户ID和支付渠道查卡信息
     * @return
     */
    CardInfoDO findByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel);

    /**
     * 更新卡信息（签约之前才能更新）
     * @param map
     * @return
     */
    int updateByUserIdAndPayChannel(Map map);

    /**
     * 查已经签约的卡号
     * @param cardNo
     * @return
     */
//    CardInfoDO findSignedByCardNo(String cardNo);

    /**
     * 查已经签约的卡号
     * @param userId
     * @param payChannel
     * @return
     */
    CardInfoDO findSignedByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel);
    /**
     * 标记已经签约
     * @param userId
     * @param payChannel
     * @return
     */
    int signCardByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel);

}