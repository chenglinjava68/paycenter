package com.qccr.paycenter.dal.domain.refund;

import com.qccr.paycenter.dal.domain.BaseDO;

import java.util.Date;

public class RefundOrderDO extends BaseDO{
	
	private static final long serialVersionUID = -5262689535402938357L;
	
	/**
	 * 主键
	 */
	private Integer id;
	
	/**
	 * 退款订单编号
	 */
	private String refundNo;
	
	/**
	 * 退款批次号，这个字段不再用了(20160614)
	 */
	private String batchNo;
	
	/**
	 * 退款流水号
	 */
	private String refundSerialNo;
	
	/**
	 * 支付订单编号
	 */
	private String payNo;
	/**
	 * 第三方支付订单号
	 */
	private String payBillNo;
	/**
	 * 三方退款单号
	 */
	private String refundBillNo;
	
	/**
	 * 外部退款编号
	 */
	private String outRefundNo;
	
	/**
	 * 业务系统订单编号
	 */
	private String businessNo;
	
	/**
	 * 业务类型
	 */
	private String businessType;	
	/**
	 * 退款商户号，和原先收款商户号一致
	 */
	private String mchId;
	/**
	 * 原先付款账号
	 */
	private String refundAccount;
	
	private String refundChannel;
	
	private String refundType;
	
	/**
	 * 通知业务系统地址
	 */
	private String notifyUrl;
	
	/**
	 * 通知状态
	 */
	private String notifyStatus;
	
	private int totalFee;
	/**
	 * 退款金额
	 */
	private Integer refundFee;
	
	/**
	 * 备注
	 */
	private String remark ;
	/**
	 * 退款描述
	 */
	private String subject;	
	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 合作方编号
	 */
	private String partner;

	private Date refundTime;

	private Date successTime;
	/**
	 * 主交易号
	 */
	private String tradeNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getRefundSerialNo() {
		return refundSerialNo;
	}

	public void setRefundSerialNo(String refundSerialNo) {
		this.refundSerialNo = refundSerialNo;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	

	public String getPayBillNo() {
		return payBillNo;
	}

	public void setPayBillNo(String payBillNo) {
		this.payBillNo = payBillNo;
	}

	public String getRefundBillNo() {
		return refundBillNo;
	}

	public void setRefundBillNo(String refundBillNo) {
		this.refundBillNo = refundBillNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	public String getRefundChannel() {
		return refundChannel;
	}

	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public Date getRefundTime() {
		if(refundTime != null) {
			return (Date) refundTime.clone();
		}else {
			return null;
		}
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
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

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
}
