package com.qccr.paycenter.facade.dal.ro.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 当面付支付参数
 * Created by user on 2016/11/18.
 */
public class FacepayParamRO implements Serializable {
    private static final long serialVersionUID = -1888911144569624695L;
    /**
     * 收款方用户ID,超人平台上面的商户的userId
     */
    private String userId;

    /**
     * 支付条形码,用户支付宝付款码信息
     */
    private String authCode;
    /**
     * 订单号,业务单号
     */
    private String businessNo;
    /**
     * 订单总金额,支付金额，单位：分
     */
    private Integer totalAmount;
    /**
     * 订单主题,订单标题
     */
    private String subject;
    /**
     * 商户门店编号
     */
    private String storeId;
    /**
     * 是否超时,true超时
     */
    private Boolean outTime;
    /**
     * 超时时间,	out_time为true必传，交易超时时间，单位：秒
     */
    private Integer times;

    /**
     *  订单描述
     */
    private String desc;
    /**
     * 渠道,1支付宝，6微信
     */
    private Integer channel;
    /**
     * 支付方式,alipay_facepay
     */
    private String payType;
    /**
     * 业务类型
     */
    private String  businessType;

    /**
     * 用户支付时的ip
     */
    private String ip;
    /**
     * 合作方编号
     */
    private String partner;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Boolean getOutTime() {
        return outTime;
    }

    public void setOutTime(Boolean outTime) {
        this.outTime = outTime;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
