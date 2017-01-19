package com.qccr.paycenter.model.dal.so.notify;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class PayNotifySO {
	
	private  String payNo;
	
	private String billNo;
	
	private String serialNo;
	
	private String mchId;
	
	private String businessNo;
	
	private String businessType;
	
	private String  payerAccount;
	
	private Date finish;
	
	private String amount;
	
	private String subject;
	
	private String payChannel;
	
	private String payType;
	
	private String status;
	
	private boolean verify;
	
	private String msg;
	
	private boolean hasReturn;
	
	private String reData;

	private boolean  needNotify;

	/**
	 * 合作方编号
	 */
	private String partner;

	/**
	 * 支付完成时间
	 */
	private Date successTime;

	private String receiptAmount;

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getFinish() {
		if(finish != null) {
			return (Date) finish.clone();
		}else {
			return null;
		}
	}

	public void setFinish(Date finish) {
		this.finish = finish;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public boolean isHasReturn() {
		return hasReturn;
	}

	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}

	public String getPayerAccount() {
		return payerAccount;
	}

	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	public String getReData() {
		return reData;
	}

	public void setReData(String reData) {
		this.reData = reData;
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

	public boolean isNeedNotify() {
		return needNotify;
	}

	public void setNeedNotify(boolean needNotify) {
		this.needNotify = needNotify;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public Date getSuccessTime() {
		if(successTime != null) {
			return (Date) successTime.clone();
		}else {
			return null;
		}
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public String getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(String receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
