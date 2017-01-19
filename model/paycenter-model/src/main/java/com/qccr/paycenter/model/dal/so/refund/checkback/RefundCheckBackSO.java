package com.qccr.paycenter.model.dal.so.refund.checkback;

import java.util.Date;

import com.qccr.paycenter.model.enums.RefundTypeEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *退款回查
 * author: denghuajun
 * date: 2016/3/16 21:06
 * version: v1.0.0
 */
public class RefundCheckBackSO {

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
    /**
     * 商户号
     */
    private String mchId;

    /**
     * 原退款时间
     */
    private Date refundTime;

    /**
     * 正常退款和补偿退款 使用在定时作业退款与补偿的时候
     */
    private RefundTypeEnum refundType;

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

    public Date getRefundTime() {
        if(refundTime != null) {
            return (Date) refundTime.clone();
        }else {
            return null;
        }
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public RefundTypeEnum getRefundType() {
        return refundType;
    }

    public void setRefundType(RefundTypeEnum refundType) {
        this.refundType = refundType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
