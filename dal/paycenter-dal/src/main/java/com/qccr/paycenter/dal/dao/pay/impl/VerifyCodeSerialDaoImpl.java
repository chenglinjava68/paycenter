/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.dao.pay.impl;

import com.qccr.paycenter.dal.dao.pay.VerifyCodeSerialDao;
import com.qccr.paycenter.dal.domain.pay.VerifyCodeSerialDO;
import com.qccr.paycenter.dal.mapper.pay.VerifyCodeSerialMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
@Repository("verifyCodeSerialDao")
public class VerifyCodeSerialDaoImpl implements VerifyCodeSerialDao {
    @Resource
    private VerifyCodeSerialMapper verifyCodeSerialMapper;

    /**
     * 删除单条记录
     *
     * @param id
     */
//    @Override
//    public int delete(Long id) {
//        return verifyCodeSerialMapper.delete(id);
//    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(VerifyCodeSerialDO record) {
        return verifyCodeSerialMapper.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
//    @Override
//    public List<VerifyCodeSerialDO> query(VerifyCodeSerialDO condition) {
//        return verifyCodeSerialMapper.query(condition);
//    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(VerifyCodeSerialDO record) {
        return verifyCodeSerialMapper.update(record);
    }

    /**
     * 用票据号查询当天的记录
     *
     * @param invioceNo
     * @return
     */
    @Override
    public VerifyCodeSerialDO findByInvioceNo(String invioceNo) {
        return verifyCodeSerialMapper.findByInvioceNo(invioceNo);
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
        return verifyCodeSerialMapper.addReturnAddData(returnAddData, invioceNo);
    }

    /**
     * 获取最新的一条记录
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public VerifyCodeSerialDO findLatestByCardNo(String cardNo) {
//        return verifyCodeSerialMapper.findLatestByCardNo(cardNo);
//    }

    /**
     * 获取最新的一条记录
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public VerifyCodeSerialDO findLatestByUserIdAndPayChannel(@Param(value = "userId") String userId, @Param(value = "payChannel") String payChannel) {
        return verifyCodeSerialMapper.findLatestByUserIdAndPayChannel(userId, payChannel);
    }
}