package com.qccr.paycenter.biz.bo.pay.impl;

import com.qccr.paycenter.biz.bo.pay.CardInfoBO;
import com.qccr.paycenter.biz.third.bocom.utils.BocomUtils;
import com.qccr.paycenter.dal.dao.pay.CardInfoDao;
import com.qccr.paycenter.dal.domain.pay.CardInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lim on 2016/7/15.
 */
@Component
public class CardInfoBOImpl implements CardInfoBO {
    @Resource(name="cardInfoDao")
    private CardInfoDao cardInfoDao;

    /**
     * 删除单条记录
     *
     * @param id
     */
//    @Override
//    public int delete(Long id) {
//        return cardInfoDao.delete(id);
//    }

    /**
     * 增加单条记录
     *
     * @param record
     */
    @Override
    public int insert(CardInfoDO record) {
        if(record != null && record.getCardNo() != null) {
            String cardNo = record.getCardNo();
            cardNo = BocomUtils.makeSecurity(cardNo);
            record.setCardNo(cardNo);
        }
        return cardInfoDao.insert(record);
    }

    /**
     * 查询记录
     *
     * @param condition
     */
//    @Override
//    public List<CardInfoDO> query(CardInfoDO condition) {
//        return cardInfoDao.query(condition);
//    }

    /**
     * 更新单条记录
     *
     * @param record
     */
    @Override
    public int update(CardInfoDO record) {
        if(record != null && record.getCardNo() != null) {
            String cardNo = record.getCardNo();
            cardNo = BocomUtils.makeSecurity(cardNo);
            record.setCardNo(cardNo);
        }
        return cardInfoDao.update(record);
    }

    /**
     * 查卡号
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public CardInfoDO findByCardNo(String cardNo) {
//        return cardInfoDao.findByCardNo(cardNo);
//    }

    /**
     * 标记已经签约
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public int signCard(String cardNo) {
//        return cardInfoDao.signCard(cardNo);
//    }

    /**
     * 用户ID和支付渠道查卡信息
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public CardInfoDO findByUserIdAndPayChannel(String userId, String payChannel) {
        CardInfoDO cardInfoDO = cardInfoDao.findByUserIdAndPayChannel(userId, payChannel);
        if(cardInfoDO != null && cardInfoDO.getCardNo() != null) {
            String cardNo = cardInfoDO.getCardNo();
            cardNo = BocomUtils.openSecurity(cardNo);
            cardInfoDO.setCardNo(cardNo);
        }
        return cardInfoDO;
    }

    /**
     * 更新卡信息（签约之前才能更新）
     * @param cardNo
     * @param cardName
     * @param expiryDate
     * @return
     */
    @Override
    public int updateByUserIdAndPayChannel(String cardNo, String cardName, String expiryDate, String userId, String payChannel) {
        if(cardNo != null) {
            cardNo = BocomUtils.makeSecurity(cardNo);
        }
        Map map = new HashMap();
        map.put("cardName",cardName);
        map.put("expiryDate",expiryDate);
        map.put("cardNo",cardNo);
        map.put("userId",userId);
        map.put("payChannel",payChannel);
        return cardInfoDao.updateByUserIdAndPayChannel(map);
    }

    /**
     * 查已经签约的卡号
     *
     * @param cardNo
     * @return
     */
//    @Override
//    public CardInfoDO findSignedByCardNo(String cardNo) {
//        return cardInfoDao.findSignedByCardNo(cardNo);
//    }


    /**
     * 查已经签约的卡号
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public CardInfoDO findSignedByUserIdAndPayChannel(String userId, String payChannel) {
        CardInfoDO cardInfoDO = cardInfoDao.findSignedByUserIdAndPayChannel(userId, payChannel);
        if(cardInfoDO != null && cardInfoDO.getCardNo() != null) {
            String cardNo = cardInfoDO.getCardNo();
            cardNo = BocomUtils.openSecurity(cardNo);
            cardInfoDO.setCardNo(cardNo);
        }
        return cardInfoDO;
    }

    /**
     * 标记已经签约
     *
     * @param userId
     * @param payChannel
     * @return
     */
    @Override
    public int signCardByUserIdAndPayChannel(String userId, String payChannel) {
        return cardInfoDao.signCardByUserIdAndPayChannel(userId, payChannel);
    }
}
