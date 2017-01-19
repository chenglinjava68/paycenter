package com.qccr.paycenter.facade.dal.ro.refund;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 退款参数组装对象
 * @author denghuajun
 * @version 1.1.9
 * @date 2016/1/20
 */
public class RefundParamRO implements Serializable{
	
	private static final long serialVersionUID = -8568196647716767936L;
	
	
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
	 * 异步通知地址
	 */
	private String notifyUrl;
	
	/**
	 * 退款说明
	 */
	private String subject;
	
	/**
	 * 退款金额
	 */
	private Integer refundFee;
	
	/**
	 * 支付订单号
	 */
	private String payNo;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 发起退款时间
	 */
	private Date refundDate;
	/**
	 * 合作方编号
	 */
	private String partner;

	/**
	 * 线下汇款的时候需要
	 * 三方支付单号
	 */
	private String billNo;
	/**
	 * 线下汇款的时候需要
	 * 支付时使用的渠道，12-线下汇款
	 */
	private int payChannel;
	/**
	 * 哪类支付类型：混合支付(blend_pay)，线下支付(offline_pay)，线上支付(online_pay)
	 */
	private String whichPay;

	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
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
	public Integer getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOutRefundNo() {
		return outRefundNo;
	}
	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}
	public Date getRefundDate() {
		if(refundDate != null) {
			return (Date) refundDate.clone();
		}else {
			return null;
		}
	}
	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
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

	public String getWhichPay() {
		return whichPay;
	}

	public void setWhichPay(String whichPay) {
		this.whichPay = whichPay;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
