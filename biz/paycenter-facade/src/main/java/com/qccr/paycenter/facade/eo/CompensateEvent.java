package com.qccr.paycenter.facade.eo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 补偿消息
 * @author: denghuajun
 * @date: 2016/5/4 15:01
 * @version: v1.0.0
 */
public class CompensateEvent implements Serializable{

    private static final long serialVersionUID = -5632437902554697978L;


    /**
     * 补偿单号,用于向第三方发起退款。
     */
    private String compensateNo;

    /**
     * 批次号，用于支付退款
     */
    private String batchNo;

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
     * 支付订单状态0-待处理，1-处理成功 2-处理失败
     */
    private Integer status;

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
     *  定向源消息，可用于通知消息体统
     */
    private String source;

    /**
     * 三方退款编号
     */
    private String refundBillNo;

    /**
     *退款补偿时间
     */
    private Date refundTime;

    /**
     * 补偿类型，1-重复支付补偿，2-订单关闭补偿
     */
    private Integer compensateType;

    /**
     * 退款成功时间
     */
    private Date successTime;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getRefundBillNo() {
        return refundBillNo;
    }

    public void setRefundBillNo(String refundBillNo) {
        this.refundBillNo = refundBillNo;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getCompensateType() {
        return compensateType;
    }

    public void setCompensateType(Integer compensateType) {
        this.compensateType = compensateType;
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
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
