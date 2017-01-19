/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.biz.bo.pay.impl;

import com.qccr.paycenter.biz.bo.pay.VerifyCodeSerialBO;
import com.qccr.paycenter.biz.third.bocom.utils.BocomUtils;
import com.qccr.paycenter.dal.dao.pay.VerifyCodeSerialDao;
import com.qccr.paycenter.dal.domain.pay.VerifyCodeSerialDO;
import com.qccr.paycenter.dal.mapper.pay.VerifyCodeSerialMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Component
public class VerifyCodeSerialBOImpl implements VerifyCodeSerialBO {
    @Resource(name="verifyCodeSerialDao")
    private VerifyCodeSerialDao verifyCodeSerialDao;

    /**
     * 删除单条记录
     *
     * @param id
     */
//    @Override
//    public int delete(Long id) {
//        return verifyCodeSerialDao.delete(id);
//    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(VerifyCodeSerialDO record) {
        if(record != null && record.getCardNo() != null) {
            String cardNo = record.getCardNo();
            cardNo = BocomUtils.makeSecurity(cardNo);
            record.setCardNo(cardNo);
        }
        return verifyCodeSerialDao.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
//    @Override
//    public List<VerifyCodeSerialDO> query(VerifyCodeSerialDO condition) {
//        return verifyCodeSerialDao.query(condition);
//    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(VerifyCodeSerialDO record) {
        if(record != null && record.getCardNo() != null) {
            String cardNo = record.getCardNo();
            cardNo = BocomUtils.makeSecurity(cardNo);
            record.setCardNo(cardNo);
        }
        return verifyCodeSerialDao.update(record);
    }

    /**
     * 用票据号查询当天的记录
     *
     * @param invioceNo
     * @return
     */
    @Override
    public VerifyCodeSerialDO findByInvioceNo(String invioceNo) {
        VerifyCodeSerialDO verifyCodeSerialDO = verifyCodeSerialDao.findByInvioceNo(invioceNo);
        if(verifyCodeSerialDO != null && verifyCodeSerialDO.getCardNo() != null) {
            String cardNo = verifyCodeSerialDO.getCardNo();
            cardNo = BocomUtils.openSecurity(cardNo);
            verifyCodeSerialDO.setCardNo(cardNo);
        }
        return verifyCodeSerialDO;
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
        return verifyCodeSerialDao.addReturnAddData(returnAddData, invioceNo);
    }

    /**
     * 获取最新的一条记录
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public VerifyCodeSerialDO findLatestByCardNo(String cardNo) {
//        return verifyCodeSerialDao.findLatestByCardNo(cardNo);
//    }

    /**
     * 获取最新的一条记录
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public VerifyCodeSerialDO findLatestByUserIdAndPayChannel(String userId, String payChannel) {
        VerifyCodeSerialDO verifyCodeSerialDO = verifyCodeSerialDao.findLatestByUserIdAndPayChannel(userId, payChannel);
        if(verifyCodeSerialDO != null && verifyCodeSerialDO.getCardNo() != null) {
            String cardNo = verifyCodeSerialDO.getCardNo();
            cardNo = BocomUtils.openSecurity(cardNo);
            verifyCodeSerialDO.setCardNo(cardNo);
        }
        return verifyCodeSerialDO;
    }
}