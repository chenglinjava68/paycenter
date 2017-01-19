package com.qccr.paycenter.biz.third.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * author: denghuajun
 * date: 2016/5/10 14:45
 * version: v1.0.0
 */
public class TradeRefundQueryResponse extends AlipayResponse{

    private String sign;

    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    @JSONField(name = "trade_no")
    private String tradeNo;

    @JSONField(name = "refund_amount")
    private String refundAmount;

    @JSONField(name = "total_amount")
    private String totalAmount;

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

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
