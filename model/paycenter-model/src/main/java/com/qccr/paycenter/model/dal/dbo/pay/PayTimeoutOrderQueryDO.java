package com.qccr.paycenter.model.dal.dbo.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应超时订单，以及它的超时时间。
 * author: denghuajun
 * date: 2016/4/12 9:49
 * version: v1.0.0
 */
public class PayTimeoutOrderQueryDO implements Serializable{

    private String  payNo;

    private Date outTime;

    private String payChannel;

    private String payType;

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public Date getOutTime() {
        if(outTime != null) {
            return (Date) outTime.clone();
        }else {
            return null;
        }
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
