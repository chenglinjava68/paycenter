package com.qccr.paycenter.facade.eo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 支付推送消息
 * @author denghuajun
 * @date 2015年12月24日 上午11:31:58
 * @version: v1.0.0
 */
public class PayEvent implements Serializable {
	private static final long serialVersionUID = 947067363359122034L;

	/**
	 * 支付订单编号
	 */
	private String payNo;
	
	/**
	 * 业务系统订单编号
	 */
	private String businessNo;
	
	/**
	 * 业务系统订单类型
	 */
	private String businessType;
	
	/**
	 * 三方支付单号
	 */
	private String billNo;

	/**
	 * 支付账号
	 */
	private String payerAccount;
	
	/**
	 * 第三方支付渠道
	 */
	private int payChannel;
	
	/**
	 * 第三方支付类型
	 */
	private String payType;
	
	/**
	 * 支付状态,success  ， fail
	 */
	private String status;
	/**
	 * 实收金额
	 */
	private Long receiptAmount;
	/**
	 * 订单金额
	 */
	private Long totalAmount;

	/**
	 * 失败提示信息
	 */
	private String errorMsg;

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

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public String getPayerAccount() {
		return payerAccount;
	}

	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}

	public Long getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(Long receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
