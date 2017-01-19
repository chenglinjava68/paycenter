package com.qccr.paycenter.biz.third.wechat.model;

import java.io.Serializable;

public class RtnCallBackModel implements Serializable {
	private static final long serialVersionUID = -885014706973319190L;
	
	private 	String	returnMsg;//	返回状态码
	private 	String	returnCode;//	返回信息
	private 	String	appid	;//公众账号ID
	private 	String	mchId	;//商户号
	private 	String	nonceStr;//	随机字符串
	private 	String	sign	;//签名
	private 	String	resultCode;//	业务结果
	private 	String	openid	;//用户标识
	private 	String	isSubscribe;//	是否关注公众账号
	private 	String	tradeType	;//交易类型
	private 	String	bankType	;//付款银行
	private 	int		totalFee	;//总金额
	private 	int		cashFee	;//现金支付金额
	private 	String	transactionId	;//微信支付订单号
	private 	String	outTradeNo	;//商户订单号
	private 	String 	timeEnd	;//支付完成时间
	private     String errCode;
	private 	String errCodeDes;
	
	public String getReturnMsg() {
		return returnMsg;
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
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
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
	
	public String getResultCode() {
		return resultCode;
	}
	 
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
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
	public int getCashFee() {
		return cashFee;
	}
	public void setCashFee(int cashFee) {
		this.cashFee = cashFee;
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
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

}
