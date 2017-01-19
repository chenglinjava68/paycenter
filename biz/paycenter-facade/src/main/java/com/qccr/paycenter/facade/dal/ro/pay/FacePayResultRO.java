package com.qccr.paycenter.facade.dal.ro.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 当面付
 * @author pengdc
 *
 */
public class FacePayResultRO implements Serializable {

	private static final long serialVersionUID = 3679589601691071442L;
	/**支付状态*/
	private int status;
	/**错误信息*/
	private String errMsg;
	/**支付编号*/
	private String payNo;
	/**备注*/
	private String remark;

//	支付账号	payAccount	是	String
	private String payAccount;
//	支付时间	payTime	是	Date
	private String payTime;
	/**
	 * 渠道,1支付宝，6微信
	 */
	private Integer channel;
	/**
	 * 订单总金额,支付金额，单位：分
	 */
	private Integer totalAmount;

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
