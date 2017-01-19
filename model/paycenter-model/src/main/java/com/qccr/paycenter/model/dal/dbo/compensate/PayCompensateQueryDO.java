package com.qccr.paycenter.model.dal.dbo.compensate;

import com.qccr.paycenter.model.dal.dbo.PagedQueryDO;

import java.util.Date;

/**
 * author: denghuajun
 * date: 2016/3/8 15:29
 * version: v1.0.0
 */
public class PayCompensateQueryDO extends PagedQueryDO{

    private static final long serialVersionUID = 1388456202674307230L;

    /**
     * 0-未退款  1-已经退款
     */
    private int status ;
    /**
     * 三方支付渠道，1-支付宝，6-微信，8-银联
     */
    private int channel;

    private String payChannel;

    /**
     *时间区间，从---开始
     */
    private Date fromTime;
    /**
     * 时间区间，结束
     */
    private Date endTime;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public Date getFromTime() {
        if(fromTime != null) {
            return (Date) fromTime.clone();
        }else {
            return null;
        }
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getEndTime() {
        if(endTime != null) {
            return (Date) endTime.clone();
        }else {
            return null;
        }
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }
}
