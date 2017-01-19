package com.qccr.paycenter.dal.domain.pay;

import com.qccr.paycenter.dal.domain.BaseDO;

import java.util.Date;

public class PayOrderDO extends BaseDO{

	
	private static final long serialVersionUID = 8160377772156887702L;
	
	/**
	 * 主键
	 */
	private Integer id;
	
	/**
	 * 金额
	 */
	private Integer amount;
	
	/**
	 * 支付订单编号
	 */
	private String payNo;
	
	/**
	 * 业务系统，订单编号
	 */
	private String businessNo;
	
	/**
	 * 业务系统，订单类型
	 */
	private String businessType;
	
	/**
	 * 支付流水编号
	 */
	private String paySerialNo;
	
	/**
	 * 通知业务系统地址
	 */
	private String notifyUrl ;
	
	/**
	 * 通知状态
	 */
	private Integer notifyStatus;
	/**
	 * 三方支付单号
	 */
	private String billNo;
		
	/**
	 * 支付订单状态
	 */
	private Integer status;
	//-------------------支付完成后，将三方数据同步到支付订单 ----------------------///
	/**
	 * 支付渠道
	 */
	private String  payChannel;
	/**
	 * 支付类型，APP ，NATIVE，WEB等
	 */
	private String payType;
	/**
	 * 商户号
	 */
	private String mchId;
	/**
	 * 支付账号
	 */
	private String payerAccount;
	/**
	 * 合作方，系统编号
	 */
	private String  partner;

	/**
	 * 超时截止时间
	 */
	private Date outTime;

	/**
	 * 支付请求时间
	 */
	private Date payTime;

	/**
	 * 支付成功时间，以第三方时间为准
	 */
	private Date successTime;

	/**
	 * 保留字段，接收与支付无关的数据
	 * 最好使用json字符串格式转
	 */
	private String retain;

	//-------------------20160715新加字段 ----------------------///
	/**
	 * 支付订单编号，主订单编号
	 */
	private String tradeNo;
	/**
	 * 用户Id
	 */
	private String userId;
	/**
	 * 汇款人
	 */
	private String remitter;
	/**
	 * 短流水号（适配三方银行流水）
	 */
	private String shortPayNo;
	/**
	 * 支付请求的全部参数对象json
	 */
	private String requestParam;
	/**
	 * 支付返回的全部参数对象json
	 */
	private String returnParam;

	private Long receiptAmount;
	/**
	 * 收款方用户ID
	 */
	private Long debitUserId;

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

	public String getPaySerialNo() {
		return paySerialNo;
	}

	public void setPaySerialNo(String paySerialNo) {
		this.paySerialNo = paySerialNo;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Integer getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(Integer notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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

	public Date getSuccessTime() {
		return (Date)successTime.clone();
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public String getRetain() {
		return retain;
	}

	public void setRetain(String retain) {
		this.retain = retain;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
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

	public String getShortPayNo() {
		return shortPayNo;
	}

	public void setShortPayNo(String shortPayNo) {
		this.shortPayNo = shortPayNo;
	}

	public String getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(String requestParam) {
		this.requestParam = requestParam;
	}

	public String getReturnParam() {
		return returnParam;
	}

	public void setReturnParam(String returnParam) {
		this.returnParam = returnParam;
	}

	public Long getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(Long receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Long getDebitUserId() {
		return debitUserId;
	}

	public void setDebitUserId(Long debitUserId) {
		this.debitUserId = debitUserId;
	}
}
