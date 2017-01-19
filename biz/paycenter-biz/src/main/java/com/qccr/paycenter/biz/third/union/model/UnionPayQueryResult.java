package com.qccr.paycenter.biz.third.union.model;

import java.util.Date;

/**
 * author: denghuajun
 * date: 2016/4/6 15:49
 * version: v1.0.0
 */
public class UnionPayQueryResult {

    /**
     * 证书id
     */
    private String certId;

    /**
     * 签名
     */
    private String signature;

    /**
     *编码方式
     */
    private String encoding;

    /**
     *签名方法
     */
    private String signMethod;

    /**
     *交易类型
     */
    private String txnType;

    /**
     * 交易子类
     */
    private String txnSubType;

    /**
     * 接入类型
     */
    private String accessType;

    /**
     * 商户代码
     */
    private String merId;

    /**
     * 订单发送时间
     */
    private Date txnTime;

    /**
     * 商户订单号
     */
    private String orderId;

    /**
     * 请求方保留域请求方保留域
     */
    private String reqReserved;

    /**
     * 保留域
     */
    private String reserved;

    /**
     * 交易查询流水号
     */
    private String queryId;

    /**
     * 系统跟踪号
     */
    private String traceNo;

    /**
     * 交易传输时间
     */
    private String traceTime;

    /**
     * 清算日期,资金类交易统一返回
     */
    private String settleDate;

    /**
     * 清算币种
     */
    private String settleCurrencyCode;

    /**
     * 清算金额
     */
    private Integer  settleAmt;

    /**
     * 交易金额
     */
    private Integer txnAmt;

    /**
     * 原交易应答码
     */
    private String origRespCode;

    /**
     * 原交易应答信息
     */
    private String origRespMsg;

    /**
     * 应答码
     */
    private String respCode;

    /**
     * 应答信息
     */
    private String respMsg;

    /**
     * 账号
     */
    private String accNo;

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public Date getTxnTime() {
        if(txnTime != null) {
            return (Date) txnTime.clone();
        }else {
            return null;
        }
    }

    public void setTxnTime(Date txnTime) {
        this.txnTime = txnTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getSettleCurrencyCode() {
        return settleCurrencyCode;
    }

    public void setSettleCurrencyCode(String settleCurrencyCode) {
        this.settleCurrencyCode = settleCurrencyCode;
    }

    public Integer getSettleAmt() {
        return settleAmt;
    }

    public void setSettleAmt(Integer settleAmt) {
        this.settleAmt = settleAmt;
    }

    public Integer getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(Integer txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getOrigRespCode() {
        return origRespCode;
    }

    public void setOrigRespCode(String origRespCode) {
        this.origRespCode = origRespCode;
    }

    public String getOrigRespMsg() {
        return origRespMsg;
    }

    public void setOrigRespMsg(String origRespMsg) {
        this.origRespMsg = origRespMsg;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }
}
