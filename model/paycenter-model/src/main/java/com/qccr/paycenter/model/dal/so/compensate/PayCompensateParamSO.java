package com.qccr.paycenter.model.dal.so.compensate;

import java.util.Date;

/**
 * 支付补偿SO
 * author: denghuajun
 * date: 2016/3/5 17:52
 * version: v1.1.0
 */
public class PayCompensateParamSO {
    /**
     * 补偿单号,用于向第三方发起退款。
     */
    private String compensateNo;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 支付订单编号
     */
    private String payNo;

    private String batchNo;

    /**
     * 业务系统，订单编号
     */
    private String businessNo;

    /**
     * 业务系统，订单类型
     */
    private String businessType;

    /**
     * 三方支付单号
     */
    private String billNo;

    /**
     * 支付渠道
     */
    private String  payChannel;

    /**
     * 支付类型，APP ，NATIVE，WEB等
     */
    private String payType;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 退款说明
     */
    private String subject;

    /**
     * 支付账号
     */
    private String payerAccount;

    /**
     * 合作方，系统编号
     */
    private String  partner;

    /**
     *  定向源消息，可用于通知消息体统，反馈给用户，曾加用户友好度
     */
    private String source;

    /**
     * 三方退款编号
     */
    private String refundbillNo;

    /**
     *退款补偿时间
     */
    private Date refundTime;

    /**
     * 三方回调地址
     */
    private String localNotifyUrl;

    /**
     * 发起补偿操作员
     */
    private String operatePerson;

    /**
     * 是否需要消息通知
     */
    private boolean needNotify;

    /**
     * 消息通知定向源
     */
    private String notifySourse;

    public String getCompensateNo() {
        return compensateNo;
    }

    public void setCompensateNo(String compensateNo) {
        this.compensateNo = compensateNo;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRefundbillNo() {
        return refundbillNo;
    }

    public void setRefundbillNo(String refundbillNo) {
        this.refundbillNo = refundbillNo;
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

    public String getLocalNotifyUrl() {
        return localNotifyUrl;
    }

    public void setLocalNotifyUrl(String localNotifyUrl) {
        this.localNotifyUrl = localNotifyUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    public boolean isNeedNotify() {
        return needNotify;
    }

    public void setNeedNotify(boolean needNotify) {
        this.needNotify = needNotify;
    }

    public String getNotifySourse() {
        return notifySourse;
    }

    public void setNotifySourse(String notifySourse) {
        this.notifySourse = notifySourse;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
