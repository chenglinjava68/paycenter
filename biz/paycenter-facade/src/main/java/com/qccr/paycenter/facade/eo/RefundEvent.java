package com.qccr.paycenter.facade.eo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 退款推送消息
 * @author denghuajun
 * @date 2015年12月24日 上午11:31:33
 * @version: v1.0.0
 */
public class RefundEvent implements Serializable {

	private static final long serialVersionUID = 8090032648035698399L;

	/**
	 * 退款订单编号
	 */
	private String refundNo;
	
	/**
	 * 业务系统退单编号
	 */
	private String outRefundNo;
	
	/**
	 * 业务系统订单编号
	 */
	private String businessNo;
	
	/**
	 * 业务系统订单类型
	 */
	private String businessType;
	
	/**
	 * 退款状态,success  ， fail
	 */
	private String status;
	
	/**
	 * 失败提示信息
	 */
	private String errorMsg;

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
