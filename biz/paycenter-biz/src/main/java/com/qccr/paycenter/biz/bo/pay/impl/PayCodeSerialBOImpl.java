package com.qccr.paycenter.biz.bo.pay.impl;

import com.qccr.paycenter.biz.bo.pay.PayCodeSerialBO;
import com.qccr.paycenter.biz.third.bocom.utils.BocomUtils;
import com.qccr.paycenter.dal.dao.pay.PayCodeSerialDao;
import com.qccr.paycenter.dal.domain.pay.PayCodeSerialDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by user on 2016/8/17.
 */

@Component
public class PayCodeSerialBOImpl implements PayCodeSerialBO {
    @Resource(name="payCodeSerialDao")
    private PayCodeSerialDao payCodeSerialDao;

    /**
     * 删除单条记录
     *
     * @param id
     */
//    @Override
//    public int delete(Long id) {
//        return payCodeSerialDao.delete(id);
//    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(PayCodeSerialDO record) {
        if(record != null && record.getCardNo() != null) {
            String cardNo = record.getCardNo();
            cardNo = BocomUtils.makeSecurity(cardNo);
            record.setCardNo(cardNo);
        }
        return payCodeSerialDao.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
//    @Override
//    public List<PayCodeSerialDO> query(PayCodeSerialDO condition) {
//        return payCodeSerialDao.query(condition);
//    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(PayCodeSerialDO record) {
        if(record != null && record.getCardNo() != null) {
            String cardNo = record.getCardNo();
            cardNo = BocomUtils.makeSecurity(cardNo);
            record.setCardNo(cardNo);
        }
        return payCodeSerialDao.update(record);
    }

    /**
     * 用票据号查询当天的记录
     *
     * @param invioceNo
     * @return
     */
    @Override
    public PayCodeSerialDO findByInvioceNo(String invioceNo) {
        PayCodeSerialDO payCodeSerialDO = payCodeSerialDao.findByInvioceNo(invioceNo);
        if(payCodeSerialDO != null && payCodeSerialDO.getCardNo() != null) {
            String cardNo = payCodeSerialDO.getCardNo();
            cardNo = BocomUtils.openSecurity(cardNo);
            payCodeSerialDO.setCardNo(cardNo);
        }
        return payCodeSerialDO;
    }

    /**
     * 更新返回的附加域值
     *
     * @param returnAddData
     * @param invioceNo
     * @return
     */
    @Override
    public int addReturnAddData(@Param(value = "returnAddData") String returnAddData, @Param(value = "invioceNo") String invioceNo) {
        return payCodeSerialDao.addReturnAddData(returnAddData, invioceNo);
    }

    /**
     * 获取最新的一条记录
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public PayCodeSerialDO findLatestByCardNo(String cardNo) {
//        return payCodeSerialDao.findLatestByCardNo(cardNo);
//    }

    /**
     * 获取最新的一条记录
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public PayCodeSerialDO findLatestByUserIdAndPayChannel(String userId, String payChannel) {
        PayCodeSerialDO payCodeSerialDO = payCodeSerialDao.findLatestByUserIdAndPayChannel(userId, payChannel);
        if(payCodeSerialDO != null && payCodeSerialDO.getCardNo() != null) {
            String cardNo = payCodeSerialDO.getCardNo();
            cardNo = BocomUtils.openSecurity(cardNo);
            payCodeSerialDO.setCardNo(cardNo);
        }
        return payCodeSerialDO;
    }
}
