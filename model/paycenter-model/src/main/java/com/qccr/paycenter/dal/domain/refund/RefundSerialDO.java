package com.qccr.paycenter.dal.domain.refund;

import com.qccr.paycenter.dal.domain.BaseDO;

import java.util.Date;

public class RefundSerialDO extends BaseDO{

	private static final long serialVersionUID = -197595178038398554L;

	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 退款批次号，这个字段不再用了(20160614)
	 */
	private String batchNo;
	/**
	 * 退款流水号
	 */
	private String serialNo;
	/**
	 * 支付平台，退款订单编号
	 */
	private String refundNo;
	/**
	 * 第三方，退款编号
	 */
	private String refundBillNo;
	/**
	 * 退款状态
	 */
	private Integer status;
	/**
	 * 退款时间
	 */
	private Date refundTime;
	/**
	 * 超时时间
	 */
	private Long outTime;
	/**
	 * 是否超时
	 */
	private Integer isTimeout;

	/**
	 * 退款渠道，保持和支付记录一致
	 */
	private String refundChannel;

	/**
	 * 支付方式，和支付订单保持一致
	 */
	private String refundType;

	/**
	 * 退款商户号，和原先收款商户号一致
	 */
	private String mchId;
	/**
	 * 原先付款账号
	 */
	private String refundAccount;

	/**
	 * 退款费用
	 */
	private Integer refundFee;
	/**
	 *  第三方支付，回调通知地址
	 */
	private String notifyUrl;
	/**
	 * 退款处理类型，1后端自动处理，-“无密” 2.手动处理，-打开退款链接，输入密码，“有密”
	 */
	private Integer processType;
	/**
	 * 退款描述
	 */
	private String subject;
	/**
	 * 备注
	 */
	private String remark;

	private Date successTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getRefundBillNo() {
		return refundBillNo;
	}

	public void setRefundBillNo(String refundBillNo) {
		this.refundBillNo = refundBillNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Long getOutTime() {
		return outTime;
	}

	public void setOutTime(Long outTime) {
		this.outTime = outTime;
	}

	public Integer getIsTimeout() {
		return isTimeout;
	}

	public void setIsTimeout(Integer isTimeout) {
		this.isTimeout = isTimeout;
	}

	public String getRefundChannel() {
		return refundChannel;
	}

	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}


	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Integer getProcessType() {
		return processType;
	}

	public void setProcessType(Integer processType) {
		this.processType = processType;
	}

	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
