package com.qccr.paycenter.facade.dal.ro.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 获取签约验证码参数组装对象
 * @author lim
 * @version 1.1.9
 * @date 2016/8/9
 */
public class SignVerifyCodeParamRO implements Serializable {

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
     *支付渠道(1支付宝，2微信，8银联，10浙商，12线下，13交行)
     */
    private int payChannel;

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

    public int getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
