package com.qccr.paycenter.model.dal.so.refund;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 退款请求参数
 * @author denghuajun
 * date:2015年12月2日 下午6:38:45
 */
public class RefundParamSO {

	/**
	 * 支付订单编号
	 */
	private String payNo;

	/**
	 * 退款单号
	 */
	private String refundNo ;
	/**
	 * 退款流水号
	 */
	private String serialNo;

	/**
	 * 退款商户号，
	 */
	private String mchId;
	/**
	 * 三方渠道支付账号
	 */
	private String payerAccount;

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
	 * 三方支付单号
	 */
	private String billNo;

	/**
	 * 总金额
	 */
	private Integer totalFee;

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
	 * payChannel为1-支付宝，默认都为1.
	 * payChannel为6-微信支付， APPPAY, NATIVE  JS
	 */
	private String payType;

	/**
	 * 支付时使用的渠道，1-支付宝，6-微信,必填参数
	 */
	private String payChannel ;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 退款时间
	 */
	private Date refundDate;

	private String localNotifyUrl;
	/**
	 * 合作方编号
	 */
	private String partner;
	/**
	 * 哪类支付类型：混合支付(blend_pay)，线下支付(offline_pay)，线上支付(online_pay)
	 */
	private String whichPay;

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
	public Integer getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
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

	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getLocalNotifyUrl() {
		return localNotifyUrl;
	}
	public void setLocalNotifyUrl(String localNotifyUrl) {
		this.localNotifyUrl = localNotifyUrl;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
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
