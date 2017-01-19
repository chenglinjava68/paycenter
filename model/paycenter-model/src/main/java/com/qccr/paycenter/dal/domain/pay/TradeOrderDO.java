/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.domain.pay;

import com.qccr.paycenter.dal.domain.BaseDO;

/**
 * 交易订单，主订单
 */
public class TradeOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 支付订单编号
     */
    private String tradeNo;

    /**
     * 外部业务订单编号
     */
    private String businessNo;

    /**
     * 外部业务订单类型
     */
    private String businessType;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 订单总金额
     */
    private Long totalAmount;

    /**
     * 支付订单所属
     */
    private String source;

    /**
     * 支付订单来源
     */
    private String partner;

    /**
     * 哪类支付类型：混合支付(blend_pay)，线下支付(offline_pay)，线上支付(online_pay)
     */
    private String whichPay;

    /**
     * 交易种类：分期支付(instalment)，一次付(once_pay)，多笔付(multiple_pay)
     */
    private String tradeType;

    /**
     * 用户Id
     */
    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo == null ? null : businessNo.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner == null ? null : partner.trim();
    }

    public String getWhichPay() {
        return whichPay;
    }

    public void setWhichPay(String whichPay) {
        this.whichPay = whichPay == null ? null : whichPay.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

}