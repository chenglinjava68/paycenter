package com.qccr.paycenter.facade.dal.ro.compensate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 支付补偿
 * @author: denghuajun
 * @date: 2016/3/5 15:49
 * @version: v1.1.0
 */
public class PayCompensateParamRO implements Serializable{

    private static final long serialVersionUID = 8358488008972913971L;
    /**
     * 支付中心，补偿订单编号
     */
    private String compensateNo;

    /**
     * 是否全额退款
     */
    private boolean notFull;

    /**
     * 自定义补偿金额
     */
    private String refundAmout;

    /**
     * 发起补偿操作员
     */
    private String operatePerson;

    /**
     * 是否需要消息通知
     */
    private boolean needNotify;

    /**
     * 消息通知定向源
     */
    private String notifySourse;

    public String getCompensateNo() {
        return compensateNo;
    }

    public void setCompensateNo(String compensateNo) {
        this.compensateNo = compensateNo;
    }

    public boolean isNotFull() {
        return notFull;
    }

    public void setNotFull(boolean notFull) {
        this.notFull = notFull;
    }

    public boolean isNeedNotify() {
        return needNotify;
    }

    public String getRefundAmout() {
        return refundAmout;
    }

    public void setRefundAmout(String refundAmout) {
        this.refundAmout = refundAmout;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    public boolean getNeedNotify() {
        return needNotify;
    }

    public void setNeedNotify(boolean needNotify) {
        this.needNotify = needNotify;
    }

    public String getNotifySourse() {
        return notifySourse;
    }

    public void setNotifySourse(String notifySourse) {
        this.notifySourse = notifySourse;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
