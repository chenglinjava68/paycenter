package com.qccr.paycenter.model.dal.so.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Created by limin on 2016/8/9.
 */
public class SignParamSO implements Serializable {

    private static final long serialVersionUID = 3791799492785469427L;
    /**
     * 持卡人姓名
     */
    private String cardName;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 卡片有效期
     */
    private String expiryDate;

    /**
     * ，卡类型(信用卡1还是储蓄卡2,)
     */
    private Integer cardType;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 合作方，系统编号
     */
    private String  partner;
    /**
     * 流水号，短流水，相对于pay_order表的short_pay_no
     */
    private String traceNo;
    /**
     * 动态验证码
     */
    private String verifyCode;
    /**
     * 票据号，6位批次号+6位凭证号
     */
    private String invioceNo;

    /**
     *支付渠道
     */
    private String payChannel;

    /**
     * 票据号，6位批次号+6位凭证号
     * 原来获取验证码时候的票据号
     */
    private String verifyCodeInvioceNo;

    /**
     * 原来获取验证码时候的返回的附加域
     */
    private String verifyCodeReturnAddData;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getInvioceNo() {
        return invioceNo;
    }

    public void setInvioceNo(String invioceNo) {
        this.invioceNo = invioceNo;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getVerifyCodeInvioceNo() {
        return verifyCodeInvioceNo;
    }

    public void setVerifyCodeInvioceNo(String verifyCodeInvioceNo) {
        this.verifyCodeInvioceNo = verifyCodeInvioceNo;
    }

    public String getVerifyCodeReturnAddData() {
        return verifyCodeReturnAddData;
    }

    public void setVerifyCodeReturnAddData(String verifyCodeReturnAddData) {
        this.verifyCodeReturnAddData = verifyCodeReturnAddData;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
