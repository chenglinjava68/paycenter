package com.qccr.paycenter.model.dal.so.refund;

import java.util.Date;

public class RefundResultSO {
	
	
	/**
	 * 退款申请是否成功
	 */
	private boolean success;
	
	/**
	 * 失败时，错误信息
	 */
	private String errmsg;
	
	/**
	 * 退款编号
	 */
	private String outRefundNo;
	
	/**
	 * 业务编号，如：订单编号
	 */
	private String businessNo;
	
	/**
	 * 业务类型，如1-订单业务 2- 其他业务等
	 */
	private String businessType;

	/**
	 * 退款金额
	 */
	private int refundFee;
	
	/**
	 *退款状态，只针对退款同步通知
	 *0-进行中，1-退款完成，2-失败 
	 */
	private int status;

	/**
	 * 支付中心，退款单号
	 */
	private String refundNo;
	
	private String refundBillNo;

	/**
	 * 合作方编号
	 */
	private String partner;

	private Date successTime;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
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

	public int getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(int refundFee) {
		this.refundFee = refundFee;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRefundBillNo() {
		return refundBillNo;
	}

	public void setRefundBillNo(String refundBillNo) {
		this.refundBillNo = refundBillNo;
	}


	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
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
}
