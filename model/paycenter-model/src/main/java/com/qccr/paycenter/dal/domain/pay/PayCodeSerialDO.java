/**
 * qccr.com Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.qccr.paycenter.dal.domain.pay;

import com.qccr.paycenter.dal.domain.BaseDO;

import java.util.Date;

public class PayCodeSerialDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 请求的附加域
     */
    private String requestAddData;

    /**
     * 返回的附加域，00+12位手续费+3位错误代码+手机号后四位+2位验证代码序号
     */
    private String returnAddData;

    /**
     * 流水号,商户自定义6位流水号(可重复提交)
     */
    private String traceNo;

    /**
     * 票据号，6位批次号+6位凭证号每天唯一(不可重复提交，由0~9数字组成)
     */
    private String invioceNo;

    /**
     * 用户Id
     */
    private String userId;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getRequestAddData() {
        return requestAddData;
    }

    public void setRequestAddData(String requestAddData) {
        this.requestAddData = requestAddData == null ? null : requestAddData.trim();
    }

    public String getReturnAddData() {
        return returnAddData;
    }

    public void setReturnAddData(String returnAddData) {
        this.returnAddData = returnAddData == null ? null : returnAddData.trim();
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo == null ? null : traceNo.trim();
    }

    public String getInvioceNo() {
        return invioceNo;
    }

    public void setInvioceNo(String invioceNo) {
        this.invioceNo = invioceNo == null ? null : invioceNo.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }
}