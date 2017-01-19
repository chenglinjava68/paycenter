package com.qccr.paycenter.model.dal.so.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * author: denghuajun
 * date: 2016/4/13 16:01
 * version: v1.0.0
 */
public class PayCloseResultSO {

    /**
     * 支付渠道，如：微信
     */
    private String channel;

    /**
     * 支付类型，如：微信js
     */
    private String type ;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 支付订单号
     */
    private String payNo;

    /**
     * 第三方支付订单
     */
    private String billNo;

    /**
     * 业务订单号
     */
    private String businessNo;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 合作标识
     */
    private String partner;

    private String errMsg;

    private Integer status;

    private Boolean success;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
