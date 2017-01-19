package com.qccr.paycenter.facade.dal.ro.compensate;

import com.qccr.paycenter.facade.dal.ro.PagedQueryRO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 支付补偿查询RO
 * @author: denghuajun
 * @date: 2016/3/8 15:01
 * @version: v1.1.0
 */
public class PayCompensateQueryRO extends PagedQueryRO{

    private static final long serialVersionUID = -160440527384362L;
    /**
     * 0-未退款  1-已经退款
     */
    private int status ;
    /**
     * 三方支付渠道，1-支付宝，6-微信，8-银联
     */
    private int channel;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
