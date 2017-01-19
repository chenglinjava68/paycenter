/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.domain.pay;

import com.qccr.paycenter.dal.domain.BaseDO;

/**
 * 卡信息
 */
public class CardInfoDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 持卡人姓名
     */
    private String cardName;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 卡片有效期
     */
    private String expiryDate;

    /**
     * ，卡类型(信用卡1还是储蓄卡2,)
     */
    private Integer cardType;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 是否已经签约
     */
    private Integer isSign;
    /**
     * 支付渠道
     */
    private String payChannel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName == null ? null : cardName.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate == null ? null : expiryDate.trim();
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getIsSign() {
        return isSign;
    }

    public void setIsSign(Integer isSign) {
        this.isSign = isSign;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }
}