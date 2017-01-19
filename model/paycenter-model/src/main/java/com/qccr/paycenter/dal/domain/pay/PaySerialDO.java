package com.qccr.paycenter.dal.domain.pay;

import com.qccr.paycenter.dal.domain.BaseDO;

import java.util.Date;

public class PaySerialDO extends BaseDO{

	
	private static final long serialVersionUID = -8602108566368399321L;
	
	/**
	 *主键
	 */
	private Integer id;
	/**
	 * 流水号
	 */
	private String serialNo;
	
	/**
	 * 支付订单编号
	 */
	private String payNo;
	
	/**
	 * 支付渠道
	 */
	private String payChannel;
	
	/**
	 * 支付类型
	 */
	private String payType;
	
	/**
	 * 商户号
	 */
	private String mchId;
	
	/**
	 * 三方用户支付账号
	 */
	private String payerAccount;
	
	/**
	 * 支付金额
	 */
	private Integer amount;
	
	/**
	 * 商品名称
	 */
	private String productName;
	
	/**
	 * 主题
	 */
	private String subject;
	
	/**
	 * 支付数据
	 */
	private String payData;
		
	/**
	 * 是否超时
	 */
	private Integer isTimeout;
	
	/**
	 * 支付时间
	 */
	private Date payTime;
	
	/**
	 * 支付超时时间
	 */
	private Date outTime;
	
	/**
	 * 状态
	 */
	private Integer status;
	
	/**
	 * 第三方支付，回调通知地址
	 */
	private String notifyUrl;
	
	/**
	 * 支付返回url
	 */
	private String returnUrl ;
	
	/**
	 * 第三方支付系统编号
	 */
	private String billNo;

	/**
	 * 支付成功时间，以第三方时间为准
	 */
	private Date sucessTime;

	/**
	 * 保留字段，接收与支付无关的数据
	 * 最好使用json字符串格式转
	 */
	private String retain;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
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

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getPayData() {
		return payData;
	}

	public void setPayData(String payData) {
		this.payData = payData;
	}

	public Integer getIsTimeout() {
		return isTimeout;
	}

	public void setIsTimeout(Integer isTimeout) {
		this.isTimeout = isTimeout;
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

	public Date getOutTime() {
		if(outTime != null) {
			return (Date) outTime.clone();
		}else {
			return null;
		}
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getPayerAccount() {
		return payerAccount;
	}

	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}

	public Date getSucessTime() {
		if(sucessTime != null) {
			return (Date) sucessTime.clone();
		}else {
			return null;
		}
	}

	public void setSucessTime(Date sucessTime) {
		this.sucessTime = sucessTime;
	}

	public String getRetain() {
		return retain;
	}

	public void setRetain(String retain) {
		this.retain = retain;
	}
}
