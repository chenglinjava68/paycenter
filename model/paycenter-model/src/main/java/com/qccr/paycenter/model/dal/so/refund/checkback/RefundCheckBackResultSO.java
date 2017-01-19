package com.qccr.paycenter.model.dal.so.refund.checkback;

import com.qccr.paycenter.model.enums.RefundOrderEnum;

import java.util.Date;

/**
 * 退款回查结果
 * author: denghuajun
 * date: 2016/3/17 15:10
 * version: v1.0.0
 */
public class RefundCheckBackResultSO {

    /**
     * 支付订单号
     */
    private String payNo;
    /**
     * 三方支付编号
     */
    private String billNo;
    /**
     * 退款编号
     */
    private String refundNo;
    /**
     * 三方退款编号
     */
    private String refundBillNo;
    /**
     * 原支付退款渠道，
     */
    private String channel;
    /**
     * 原支付方式，
     */
    private String type;
    /**
     * 合作方标识
     */
    private String partner;

    private Date successTime ;

    private RefundOrderEnum refundOrderEnum;

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getRefundBillNo() {
        return refundBillNo;
    }

    public void setRefundBillNo(String refundBillNo) {
        this.refundBillNo = refundBillNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public RefundOrderEnum getRefundOrderEnum() {
        return refundOrderEnum;
    }

    public void setRefundOrderEnum(RefundOrderEnum refundOrderEnum) {
        this.refundOrderEnum = refundOrderEnum;
    }

    public Date getSuccessTime() {
        if(successTime != null) {
            return (Date) successTime.clone();
        }else {
            return null;
        }
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }
}
