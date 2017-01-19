package com.qccr.paycenter.facade.dal.ro.pay;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 支付返回对象
 * @author denghuajun
 * @version 1.1.9
 * @date 2016/1/20
 */
public class PayResultRO implements Serializable{

	private static final long serialVersionUID = -5904992822381200826L;

	/**
	 * 返回支付封装后的字符串
	 */
	private String data;
	
	/**
	 * 数据封装是否成功
	 */
	private boolean success;	
	/**
	 * 错误描述
	 */
	private String msg;
	
	/**
	 * 支付订单编号
	 */
	private String payNo;
	
	/**
	 * 备注
	 */
	private String remark;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
