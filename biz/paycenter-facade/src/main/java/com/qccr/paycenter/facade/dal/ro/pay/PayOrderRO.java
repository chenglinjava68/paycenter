package com.qccr.paycenter.facade.dal.ro.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付订单回查
 * @author denghuajun
 * @version 1.1.9
 * @date 2016/1/20
 */
public class PayOrderRO implements Serializable{

    private static final long serialVersionUID = 5732039131614127690L;


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
    private String  status;
    //-------------------支付完成后，将三方数据同步到支付订单 ----------------------///
    /**
     * 支付渠道(1支付宝，2微信，8银联，10浙商，12线下，13交行)
     */
    private Integer  payChannel;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
