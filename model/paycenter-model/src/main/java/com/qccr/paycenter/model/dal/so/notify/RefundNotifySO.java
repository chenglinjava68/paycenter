package com.qccr.paycenter.model.dal.so.notify;

import java.util.Date;

public class RefundNotifySO {
	
	private String batchNo;
	
	private String refundNo;
	
	private String outRefundNo;
	
	private String businessNo;
	
	private String refundChannel;
	
	private String businessType;
	
	private String payBillNo;
	
	private String status;
	
	private boolean verify;
	
	private String msg;
	
	private boolean hasReturn;
	
	private String reData;
	
	private String refundBillNo;


	private boolean needNotify;

	/**
	 * 合作方编号
	 */
	private String partner;
	/**
	 * 处理非定向来源退款，指定消息发送至source
	 */
	private String source;

	private Date successTime;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isVerify() {
		return verify;
	}

	public void setVerify(boolean verify) {
		this.verify = verify;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isHasReturn() {
		return hasReturn;
	}

	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}

	public String getReData() {
		return reData;
	}

	public void setReData(String reData) {
		this.reData = reData;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getPayBillNo() {
		return payBillNo;
	}

	public void setPayBillNo(String payBillNo) {
		this.payBillNo = payBillNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getRefundChannel() {
		return refundChannel;
	}

	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}

	public String getRefundBillNo() {
		return refundBillNo;
	}

	public void setRefundBillNo(String refundBillNo) {
		this.refundBillNo = refundBillNo;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
}
