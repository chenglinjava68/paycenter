package com.qccr.paycenter.biz.third.wechat.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author: denghuajun
 * date: 2016/4/6 9:58
 * version: v1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class WechatPayQueryResult {

    /**
     * 返回状态码
     */
    @XmlElement(name="return_code")
    private String  returnCode;

    /**
     * 返回信息
     */
    @XmlElement(name="return_msg")
    private String returnMsg;

    /**
     * 应用APPID
     */
    @XmlElement(name="appid")
    private String appId;

    /**
     * 商户号
     */
    @XmlElement(name="mch_id")
    private String  mchId;

    /**
     * 随机字符串
     */
    @XmlElement(name="nonce_str")
    private String  nonceStr;

    /**
     * 签名
     */
    @XmlElement(name="sign")
    private String  sign;

    /**
     * 业务结果,SUCCESS/FAIL
     */
    @XmlElement(name="result_code")
    private String resultCode;

    /**
     * 错误代码
     */
    @XmlElement(name="err_code")
    private String errCode;

    /**
     * 错误代码描述
     */
    @XmlElement(name="err_code_des")
    private String errCodeDes;

    /**
     * 设备号
     */
    @XmlElement(name="device_info")
    private String deviceInfo;

    /**
     * 用户标识
     */
    @XmlElement(name="openid")
    private String openId;

    /**
     * 是否关注公众账号
     */
    @XmlElement(name="is_subscribe")
    private String isSubscribe;

    /**
     * 交易类型
     */
    @XmlElement(name="trade_type")
    private String tradeType;

    /**
     * 交易状态
     */
    @XmlElement(name="trade_state")
    private String tradeState;

    /**
     * 付款银行
     */
    @XmlElement(name="bank_type")
    private String bankType;

    /**
     * 总金额
     */
    @XmlElement(name="total_fee")
    private int totalFee;

    /**
     * 货币种类
     */
    @XmlElement(name="fee_type")
    private String feeType;

    /**
     * 现金支付金额
     */
    @XmlElement(name="cash_fee")
    private int cashFee;

    /**
     *现金支付货币类型
     */
    @XmlElement(name="cash_fee_type")
    private String cashFeeType;

    /**
     * 代金券或立减优惠金额
     */
    @XmlElement(name="coupon_fee")
    private int couponFee;

    /**
     * 代金券或立减优惠使用数量
     */
    @XmlElement(name="coupon_count")
    private int couponCount;

    /**
     * 微信支付订单号---billNo
     */
    @XmlElement(name="transaction_id")
    private String transactionId;

    /**
     *商户订单号，---payNo
     */
    @XmlElement(name="out_trade_no")
    private String outTradeNo;

    /**
     * 附加数据
     */
    @XmlElement(name="attach")
    private String attach;

    /**
     * 支付完成时间，yyyyMMddHHmmss
     */
    @XmlElement(name="time_end")
    private String timeEnd;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int getCashFee() {
        return cashFee;
    }

    public void setCashFee(int cashFee) {
        this.cashFee = cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public int getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(int couponFee) {
        this.couponFee = couponFee;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
