package com.qccr.paycenter.model.dal.so.pay;

import java.util.Date;

public class PayParamSO {
		
	/**
	 * 支付编号
	 */
	private String payNo;
	
	/**
	 * 支付流水号
	 */
	private String paySerailNo;


	/**
	 * 金额
	 */
	private Integer amount;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 返回url，主要是web支付时跳转
	 */
	private String returnUrl;
	
	/**
	 * 后台通知
	 */
	private String notifyUrl;
	
	/**
	 * 主题
	 */
	private String subject;
	
	
	/**
	 * 业务
	 */
	private String businessNo;
	
	/**
	 * 业务类型
	 */
	private String  businessType;
	
	/**
	 * 描述
	 */
	private String desc ;
	
	/**
	 *支付渠道
	 */
	private String payChannel ;
	
	/**
	 *支付类型
	 */
	private String payType;
	
	/**
	 * 用户支付时的ip
	 */
	private String ip;
	
	/**
	 * 支付时间
	 */
	private Date payTime;
	
	/**
	 * 商户号
	 */
	private String mchId;
	/**
	 * 用户用于支付的第三方账号，如支付宝账号，银行卡账号等 s
	 */
	private String payerAccount;
	
	/**
	 * 三方回调通知
	 */
	private String localNotifyUrl;

	/**
	 * 合作方编号
	 */
	private String partner;

	private String nonceStr;
	/**
	 * 是否超时
	 */
	private boolean timeOut;
	/**
	 * 超时时间,单位-s
	 */
	private long times;

	/**
	 * 保留字段，接收与支付无关的数据
	 * 最好使用json字符串格式转
	 */
	private String retain;

	/**
	 * ###############################################################################
	 * ###############################20160715lim 后加#################################
	 * ###############################################################################
	 */
	/**
	 * 交易单号（主订单号)
	 */
	private String tradeNo;
	/**
	 * 订单总金额，单位分
	 * 20160715lim 后加，为了支持多笔支付
	 */
	private Integer totalAmount;
	/**
	 * 订单来源
	 */
	private String source;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 用于线下汇款
	 * 汇款人
	 */
	private String remitter;
	/**
	 * 用于线下汇款
	 * 汇款时间，对应pay_order里面的success_time
	 */
	private Date remitTime;
	/**
	 * 用于线下汇款
	 * 汇款流水号，对应pay_order里面的bill_no
	 */
	private String remitSerialNo;

	/**
	 * 哪类支付类型：混合支付(blend_pay)，线下支付(offline_pay)，线上支付(online_pay)
	 */
	private String whichPay;

	/**
	 * 交易种类：分期支付(instalment)，一次付(once_pay)，多笔付(multiple_pay)
	 */
	private String tradeType;

	/**
	 * 卡片有效期
	 * 交通银行支付使用
	 */
	private String expiryDate;
	/**
	 * 6位批次号（对应动态验证码交易）+6位凭证号（对应动态验证码交易）
	 * 交通银行支付使用
	 */
	private String payVerifyCodeInvioceNo;
	/**
	 * 动态验证码
	 * 交通银行支付使用
	 */
	private String dynamicCode;
	/**
	 * 票据号，6位批次号+6位凭证号，每天唯一(不可重复提交，由0~9数字组成)
	 * 交通银行支付使用
	 */
	private String invioceNo;
	/**
	 * 原来获取消费验证码时候的返回的附加域
	 */
	private String payCodeReturnAddData;
	/**
	 * 流水号，短流水，
	 */
	private String traceNo;
	private String addData;	//附加域
	/**
	 * 使用于当面付的条码付，用户的支付宝付款码
	 */
	private String authCode;
	/**
	 * 当面付的授权TOKEN
	 */
	private String appAuthToken;

	/**
	 * 收款方用户ID
	 */
	private String debitUserId;

	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPaySerailNo() {
		return paySerailNo;
	}
	public void setPaySerailNo(String paySerailNo) {
		this.paySerailNo = paySerailNo;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public Date getPayTime() {
		if(payTime != null) {
			return (Date) payTime.clone();
		}else {
			return null;
		}
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getLocalNotifyUrl() {
		return localNotifyUrl;
	}
	public void setLocalNotifyUrl(String localNotifyUrl) {
		this.localNotifyUrl = localNotifyUrl;
	}
	public String getPayerAccount() {
		return payerAccount;
	}
	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public boolean isTimeOut() {
		return timeOut;
	}

	public void setTimeOut(boolean timeOut) {
		this.timeOut = timeOut;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public String getRetain() {
		return retain;
	}

	public void setRetain(String retain) {
		this.retain = retain;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRemitter() {
		return remitter;
	}

	public void setRemitter(String remitter) {
		this.remitter = remitter;
	}

	public Date getRemitTime() {
		return remitTime;
	}

	public void setRemitTime(Date remitTime) {
		this.remitTime = remitTime;
	}

	public String getRemitSerialNo() {
		return remitSerialNo;
	}

	public void setRemitSerialNo(String remitSerialNo) {
		this.remitSerialNo = remitSerialNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getWhichPay() {
		return whichPay;
	}

	public void setWhichPay(String whichPay) {
		this.whichPay = whichPay;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getDynamicCode() {
		return dynamicCode;
	}

	public void setDynamicCode(String dynamicCode) {
		this.dynamicCode = dynamicCode;
	}

	public String getInvioceNo() {
		return invioceNo;
	}

	public void setInvioceNo(String invioceNo) {
		this.invioceNo = invioceNo;
	}

	public String getPayVerifyCodeInvioceNo() {
		return payVerifyCodeInvioceNo;
	}

	public void setPayVerifyCodeInvioceNo(String payVerifyCodeInvioceNo) {
		this.payVerifyCodeInvioceNo = payVerifyCodeInvioceNo;
	}

	public String getPayCodeReturnAddData() {
		return payCodeReturnAddData;
	}

	public void setPayCodeReturnAddData(String payCodeReturnAddData) {
		this.payCodeReturnAddData = payCodeReturnAddData;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getAddData() {
		return addData;
	}

	public void setAddData(String addData) {
		this.addData = addData;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAppAuthToken() {
		return appAuthToken;
	}

	public void setAppAuthToken(String appAuthToken) {
		this.appAuthToken = appAuthToken;
	}

	public String getDebitUserId() {
		return debitUserId;
	}

	public void setDebitUserId(String debitUserId) {
		this.debitUserId = debitUserId;
	}
}
