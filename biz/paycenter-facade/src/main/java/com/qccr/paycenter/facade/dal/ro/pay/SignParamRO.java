package com.qccr.paycenter.facade.dal.ro.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 签约参数组装对象
 * @author lim
 * @version 1.1.9
 * @date 2016/8/9
 */
public class SignParamRO implements Serializable {

    private static final long serialVersionUID = 3791799492785469427L;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 合作方，系统编号
     */
    private String  partner;

    /**
     *支付渠道
     */
    private int payChannel;
    /**
     * 动态验证码
     */
    private String verifyCode;


    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
