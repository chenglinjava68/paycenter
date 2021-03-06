package com.qccr.paycenter.model.dal.so.pay;

import java.util.Date;

/**
 * Created by denghuajun on 2016/1/20.
 */
public class PayOrderSO {


    /**
     * 金额
     */
    private Integer amount;

    /**
     * 支付订单编号
     */
    private String payNo;

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
     * 支付订单状态
     */
    private Integer  status;
    //-------------------支付完成后，将三方数据同步到支付订单 ----------------------///
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
     * 支付账号
     */
    private String payerAccount;
    /**
     * 合作方，系统编号
     */
    private String  partner;

    /**
     * 支付请求时间
     */
    private Date payTime;
    /**
     * 汇款人
     */
    private String remitter;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getRemitter() {
        return remitter;
    }

    public void setRemitter(String remitter) {
        this.remitter = remitter;
    }
}
