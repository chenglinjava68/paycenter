/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.dao.pay.impl;

import com.qccr.paycenter.dal.dao.pay.PayCodeSerialDao;
import com.qccr.paycenter.dal.domain.pay.PayCodeSerialDO;
import com.qccr.paycenter.dal.mapper.pay.PayCodeSerialMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("payCodeSerialDao")
public class PayCodeSerialDaoImpl implements PayCodeSerialDao {
    @Resource
    private PayCodeSerialMapper payCodeSerialMapper;

    /**
     * 删除单条记录
     *
     * @param id
     */
//    @Override
//    public int delete(Long id) {
//        return payCodeSerialMapper.delete(id);
//    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(PayCodeSerialDO record) {
        return payCodeSerialMapper.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
//    @Override
//    public List<PayCodeSerialDO> query(PayCodeSerialDO condition) {
//        return payCodeSerialMapper.query(condition);
//    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(PayCodeSerialDO record) {
        return payCodeSerialMapper.update(record);
    }

    /**
     * 用票据号查询当天的记录
     *
     * @param invioceNo
     * @return
     */
    @Override
    public PayCodeSerialDO findByInvioceNo(String invioceNo) {
        return payCodeSerialMapper.findByInvioceNo(invioceNo);
    }

    /**
     * 更新返回的附加域值
     *
     * @param returnAddData
     * @param invioceNo
     * @return
     */
    @Override
    public int addReturnAddData(String returnAddData, String invioceNo) {
        return payCodeSerialMapper.addReturnAddData(returnAddData, invioceNo);
    }

    /**
     * 获取最新的一条记录
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public PayCodeSerialDO findLatestByCardNo(String cardNo) {
//        return payCodeSerialMapper.findLatestByCardNo(cardNo);
//    }

    /**
     * 获取最新的一条记录
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public PayCodeSerialDO findLatestByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel) {
        return payCodeSerialMapper.findLatestByUserIdAndPayChannel(userId, payChannel);
    }
}