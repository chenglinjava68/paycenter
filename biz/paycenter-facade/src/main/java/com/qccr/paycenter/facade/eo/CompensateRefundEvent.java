package com.qccr.paycenter.facade.eo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 支付补偿消息
 * @author: denghuajun
 * @date: 2016/3/7 10:52
 * @version: v1.0.0
 */
public class CompensateRefundEvent implements Serializable {

    private static final long serialVersionUID = -2898615423447647365L;

    /**
     * 支付补偿业务编号
     */
    private String compensateNo;

    /**
     * 对应业务系统订单编号
     */
    private String businessNo;

    /**
     * 对应业务系统订单类型
     */
    private String businessType;

    /**
     * 退款状态,success  ， fail
     */
    private String status;

    /**
     * 失败提示信息
     */
    private String errorMsg;

    public String getCompensateNo() {
        return compensateNo;
    }

    public void setCompensateNo(String compensateNo) {
        this.compensateNo = compensateNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
