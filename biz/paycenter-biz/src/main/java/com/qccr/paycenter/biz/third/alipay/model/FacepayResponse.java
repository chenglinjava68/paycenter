package com.qccr.paycenter.biz.third.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by user on 2016/11/18.
 */
public class FacepayResponse {
//    trade_no	String	必填	64	支付宝交易号	2013112011001004330000121536
    @JSONField(name = "trade_no")
    private String tradeNo;
//    out_trade_no	String	必填	64	商户订单号	6823789339978248
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
//    buyer_logon_id	String	必填	100	买家支付宝账号	159****5620
    @JSONField(name = "buyer_logon_id")
    private String buyerLogonId;
//    total_amount	Price	必填	11	交易金额	120.88
    @JSONField(name = "total_amount")
    private String totalAmount;
//    receipt_amount	String	必填	11	实收金额
    @JSONField(name = "receipt_amount")
    private String receiptAmount;
//    gmt_payment	Date	必填	32	交易支付时间	2014-11-27 15:45:57
    @JSONField(name = "gmt_payment")
    private String gmtPayment;
//    buyer_user_id	String	必填	28	买家在支付宝的用户id	2088101117955611
    @JSONField(name = "buyer_user_id")
    private String buyerUserId;

    /**
     * 调用查询接口的时候返回的交易状态
     * 交易状态：
     * WAIT_BUYER_PAY（交易创建，等待买家付款）、
     * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
     * TRADE_SUCCESS（交易支付成功）、
     * TRADE_FINISHED（交易结束，不可退款）
     */
    @JSONField(name = "trade_status")
    private String tradeStatus;

    /**
     * 支付成功（10000），支付失败（40004），等待用户付款（10003）和未知异常（20000）。
     */
    @JSONField(name = "code")
    private String code;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "sub_code")
    private String subCode;
    @JSONField(name = "sub_msg")
    private String subMsg;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(String gmtPayment) {
        this.gmtPayment = gmtPayment;
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
