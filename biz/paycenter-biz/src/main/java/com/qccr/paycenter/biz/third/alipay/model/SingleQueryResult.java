package com.qccr.paycenter.biz.third.alipay.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author: denghuajun
 * date: 2016/4/1 17:23
 * version: v1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "alipay")
@SuppressWarnings("restriction")
public class SingleQueryResult {

    @XmlElement(name="is_success")
    private String  isSuccess;

    @XmlElement(name="error")
    private String error;

    @XmlElement(name="response")
    private Response response;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "response")
    public static class Response {

        @XmlElement(name="trade")
        private Trade trade;

        public Trade getTrade() {
            return trade;
        }

        public void setTrade(Trade trade) {
            this.trade = trade;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "trade")
    public static class Trade {

        @XmlElement(name="additional_trade_status")
        private String  additionalTradeStatus;

        @XmlElement(name="body")
        private String body;

        @XmlElement(name="buyer_email")
        private String buyerEmail;

        @XmlElement(name="buyer_id")
        private String buyerId;

        @XmlElement(name="discount")
        private String discount;

        @XmlElement(name="flag_trade_locked")
        private String flagTradeLocked;

        @XmlElement(name="gmt_payment")
        private String gmtPayment;

        @XmlElement(name="gmt_refund")
        private String gmtRefund;

        @XmlElement(name="out_trade_no")
        private String outTradeNo;

        @XmlElement(name="price")
        private Double price;

        @XmlElement(name="refund_fee")
        private Double refundFee;

        @XmlElement(name="refund_status")
        private String refundStatus;

        @XmlElement(name="seller_id")
        private String sellerId;

        @XmlElement(name="total_fee")
        private Double totalFee;

        @XmlElement(name="trade_no")
        private String tradeNo;

        @XmlElement(name="trade_status")
        private String tradeStatus;

        public String getAdditionalTradeStatus() {
            return additionalTradeStatus;
        }

        public void setAdditionalTradeStatus(String additionalTradeStatus) {
            this.additionalTradeStatus = additionalTradeStatus;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getBuyerEmail() {
            return buyerEmail;
        }

        public void setBuyerEmail(String buyerEmail) {
            this.buyerEmail = buyerEmail;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getFlagTradeLocked() {
            return flagTradeLocked;
        }

        public void setFlagTradeLocked(String flagTradeLocked) {
            this.flagTradeLocked = flagTradeLocked;
        }

        public String getGmtPayment() {
            return gmtPayment;
        }

        public void setGmtPayment(String gmtPayment) {
            this.gmtPayment = gmtPayment;
        }

        public String getGmtRefund() {
            return gmtRefund;
        }

        public void setGmtRefund(String gmtRefund) {
            this.gmtRefund = gmtRefund;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getRefundFee() {
            return refundFee;
        }

        public void setRefundFee(Double refundFee) {
            this.refundFee = refundFee;
        }

        public String getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(String refundStatus) {
            this.refundStatus = refundStatus;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public Double getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(Double totalFee) {
            this.totalFee = totalFee;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(String tradeStatus) {
            this.tradeStatus = tradeStatus;
        }
    }


}



