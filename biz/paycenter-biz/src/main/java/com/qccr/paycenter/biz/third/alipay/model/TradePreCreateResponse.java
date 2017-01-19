package com.qccr.paycenter.biz.third.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * author: denghuajun
 * date: 2016/5/6 14:03
 * version: v1.0.0
 */
public class TradePreCreateResponse extends AlipayResponse{


    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    @JSONField(name = "qr_code")
    private String qrCode;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
