package com.qccr.paycenter.facade.dal.ro.refund;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 退款订单回查
 * @author denghuajun
 * @version 1.1.9
 * @date 2016/1/20
 */
public class RefundOrderRO implements Serializable{

    private static final long serialVersionUID = -1809388945720128619L;
    /**
     * 支付订单号
     */
    private String payNo;

    /**
     * 退款订单编号
     */
    private String refundNo;

    /**
     * 业务系统退单编号
     */
    private String outRefundNo;

    /**
     * 业务系统订单编号
     */
    private String businessNo;

    /**
     * 业务系统订单类型
     */
    private String businessType;

    /**
     * 退款状态,success  ， fail
     */
    private String status;

    private String partner;

    /**
     * 失败提示信息
     */
    private String errorMsg;

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
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

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
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
