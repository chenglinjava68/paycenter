package com.qccr.paycenter.biz.third.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * author: denghuajun
 * date: 2016/5/6 14:59
 * version: v1.0.0
 */
public class TradeRefundCreateResponse extends AlipayResponse{


    @JSONField(name = "sign")
    private String sign;

    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    @JSONField(name = "trade_no")
    private String tradeNo;

    @JSONField(name = "buyer_logon_id")
    private String buyerLogonId;

    @JSONField(name = "fund_change")
    private String fundChange;

    @JSONField(name = "refund_fee")
    private Double refundFee;

    @JSONField(name = "send_back_fee")
    private Double sendBackFee;

    @JSONField(name = "trade_no")
    private Date gmtRefundPay;

    @JSONField(name = "buyer_user_id")
    private String buyerUserId;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getFundChange() {
        return fundChange;
    }

    public void setFundChange(String fundChange) {
        this.fundChange = fundChange;
    }

    public Double getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Double refundFee) {
        this.refundFee = refundFee;
    }

    public Double getSendBackFee() {
        return sendBackFee;
    }

    public void setSendBackFee(Double sendBackFee) {
        this.sendBackFee = sendBackFee;
    }

    public Date getGmtRefundPay() {
        return gmtRefundPay;
    }

    public void setGmtRefundPay(Date gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
