package com.qccr.paycenter.model.dal.so.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 第三方支付查询SO
 * author: denghuajun
 * date: 2016/4/1 14:00
 * version: v1.0.0
 */
public class PayQuerySO {

    /**
     * 支付渠道，如：微信
     */
    private String channel;

    /**
     * 支付类型，如：微信js
     */
    private String type ;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 支付订单号
     */
    private String payNo;

    /**
     * 第三方支付订单
     */
    private String billNo;

    /**
     * 业务订单号
     */
    private String businessNo;

    /**
     * 查询时间
     */
    private Date queryTime;

    /**
     * 查询类型，如:委托查询-非支付中心订单进行相应查询
     */
    private int  queryType;

    /**
     * 合作标识
     */
    private String partner;

    /**
     * 支付发起时间
     */
    private Date payStartTime;

    /**
     * 用户第三方支付账号
     */
    private String payerAccount;

    private Long amount;

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

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

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

    public Date getQueryTime() {
        if(queryTime != null) {
            return (Date) queryTime.clone();
        }else {
            return null;
        }
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Date getPayStartTime() {
        if(payStartTime != null) {
            return (Date) payStartTime.clone();
        }else {
            return null;
        }
    }

    public void setPayStartTime(Date payStartTime) {
        this.payStartTime = payStartTime;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
