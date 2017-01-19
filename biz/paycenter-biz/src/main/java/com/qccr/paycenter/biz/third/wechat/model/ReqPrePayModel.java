package com.qccr.paycenter.biz.third.wechat.model;

import java.io.Serializable;

/**
 * 微信预支付对象
 * @author denghuajun
 * date:2015年11月30日 下午6:45:54
 */
public class ReqPrePayModel implements Serializable{
	
	private static final long serialVersionUID = -4709187047999988237L;

	private String  transReqNo; //交易请求流水号
	
	private String 	channel;	//渠道
	
	private String  ThirdPayAccountNo;//第三方支付账号
	
	private String 	payWay;     //支付方式 
	
	private String 	tradeType;
	
	private String userId;     //用户ID

	private Integer  orderAmt;  //订单金额
	
	private String 	appid;

	private Long orderId;	//订单ID
	
	private String orderNo;
 	
	private String  busiFiled01;
 	
	private String remortIP;
	

	private String merchantId;
	private String secretKey ;
	
	private String openId;
	
	private String nonceStr;
	
	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getTransReqNo() {
		return transReqNo;
	}

	public void setTransReqNo(String transReqNo) {
		this.transReqNo = transReqNo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getThirdPayAccountNo() {
		return ThirdPayAccountNo;
	}

	public void setThirdPayAccountNo(String thirdPayAccountNo) {
		ThirdPayAccountNo = thirdPayAccountNo;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(Integer orderAmt) {
		this.orderAmt = orderAmt;
	}



	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBusiFiled01() {
		return busiFiled01;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public void setBusiFiled01(String busiFiled01) {
		this.busiFiled01 = busiFiled01;
	}

	public String getRemortIP() {
		return remortIP;
	}

	public void setRemortIP(String remortIP) {
		this.remortIP = remortIP;
	}
	
 	
	
	
}
