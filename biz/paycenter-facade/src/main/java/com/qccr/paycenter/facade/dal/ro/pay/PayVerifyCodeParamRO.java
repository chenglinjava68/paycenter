package com.qccr.paycenter.facade.dal.ro.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 获取支付验证码参数组装类
 * @author lim
 * @version 1.1.9
 * @date 2016/8/17
 */
public class PayVerifyCodeParamRO implements Serializable {

    private static final long serialVersionUID = 3791799492785469427L;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 金额，单位分
     */
    private Integer amount;
    /**
     * 合作方，系统编号
     */
    private String  partner;

    /**
     *支付渠道(1支付宝，2微信，8银联，10浙商，12线下，13交行)
     */
    private int payChannel;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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
