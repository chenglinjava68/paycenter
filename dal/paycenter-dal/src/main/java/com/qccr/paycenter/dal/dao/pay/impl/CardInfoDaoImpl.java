/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.dao.pay.impl;

import com.qccr.paycenter.dal.dao.pay.CardInfoDao;
import com.qccr.paycenter.dal.domain.pay.CardInfoDO;
import com.qccr.paycenter.dal.mapper.pay.CardInfoMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository("cardInfoDao")
public class CardInfoDaoImpl implements CardInfoDao {
    @Resource
    private CardInfoMapper cardInfoMapper;

    /**
     * 删除单条记录
     *
     * @param id
     */
    @Override
    public int delete(Long id) {
        return cardInfoMapper.delete(id);
    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(CardInfoDO record) {
        return cardInfoMapper.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
    @Override
    public List<CardInfoDO> query(CardInfoDO condition) {
        return cardInfoMapper.query(condition);
    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(CardInfoDO record) {
        return cardInfoMapper.update(record);
    }

    /**
     * @param cardNo
     * @return
     */
//    @Override
//    public CardInfoDO findByCardNo(String cardNo) {
//        return cardInfoMapper.findByCardNo(cardNo);
//    }

    /**
     * 标记已经签约
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public int signCard(String cardNo) {
//        return cardInfoMapper.signCard(cardNo);
//    }

    /**
     * 用户ID和支付渠道查卡信息
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public CardInfoDO findByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel) {
        return cardInfoMapper.findByUserIdAndPayChannel(userId, payChannel);
    }

    /**
     * 更新卡信息（签约之前才能更新）
     *
     * @param map
     * @return
     */
    @Override
    public int updateByUserIdAndPayChannel(Map map) {
        return cardInfoMapper.updateByUserIdAndPayChannel(map);
    }

    /**
     * 查已经签约的卡号
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public CardInfoDO findSignedByCardNo(String cardNo) {
//        return cardInfoMapper.findSignedByCardNo(cardNo);
//    }


    /**
     * 查已经签约的卡号
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public CardInfoDO findSignedByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel) {
        return cardInfoMapper.findSignedByUserIdAndPayChannel(userId, payChannel);
    }

    /**
     * 标记已经签约
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public int signCardByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel) {
        return cardInfoMapper.signCardByUserIdAndPayChannel(userId, payChannel);
    }
}