/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.biz.bo.pay;

import com.qccr.paycenter.dal.domain.pay.CardInfoDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardInfoBO {

    /**
     *
     * 删除单条记录
     */
//    int delete(Long id);

    /**
     *
     * 增加单条记录
     */
    int insert(CardInfoDO record);

    /**
     *
     * 查询记录
     */
//    List<CardInfoDO> query(CardInfoDO condition);

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
     * @param cardNo
     * @param cardName
     * @param expiryDate
     * @return
     */
    int updateByUserIdAndPayChannel(String cardNo, String cardName, String expiryDate, String userId, String payChannel);

    /**
     * 查已经签约的卡号
     * @param cardNo
     * @return
     */
//    CardInfoDO findSignedByCardNo(String cardNo);

    /**
     * 查已经签约的卡号
     *
     * @param userId
     * @param payChannel
     * @return
     */
    CardInfoDO findSignedByUserIdAndPayChannel(String userId, String payChannel);

    /**
     * 标记已经签约
     *
     * @param userId
     * @param payChannel
     * @return
     */
    int signCardByUserIdAndPayChannel(String userId, String payChannel);
}