package com.qccr.paycenter.facade.dal.ro.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付参数组装类
 * @author denghuajun
 * @version 1.1.9
 * @date 2016/1/20
 */
public class PayParamRO implements Serializable{

	private static final long serialVersionUID = 4798521522117407013L;


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
	 *支付渠道(1支付宝，2微信，8银联，10浙商，12线下，13交行)
	 */
	private int payChannel;
	/**
	 *支付类型
	 */
	private String payType;

	/**
	 * 用户用于支付账号，如：支付宝账号，微信open_id
	 */
	private String payerAccount;

	/**
	 * 用户支付时的ip
	 */
	private String ip;
	/**
	 * 合作方编号
	 */
	private String partner;
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
	 * 动态验证码
	 * 交通银行支付使用
	 */
	private String dynamicCode;

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
	public int getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(int payChannel) {
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
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

	public String getDynamicCode() {
		return dynamicCode;
	}

	public void setDynamicCode(String dynamicCode) {
		this.dynamicCode = dynamicCode;
	}
}
