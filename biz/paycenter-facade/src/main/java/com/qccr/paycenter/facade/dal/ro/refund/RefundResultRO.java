package com.qccr.paycenter.facade.dal.ro.refund;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 受理退款返回结果对象
 * @author denghuajun
 * @version 1.1.9
 * @date 2016/1/20
 */
public class RefundResultRO implements Serializable{
	
	private static final long serialVersionUID = 6091006665249410907L;		
	/**
	 * 退款申请是否成功
	 */
	private boolean success;
	
	/**
	 * 失败时，错误信息
	 */
	private String errmsg;

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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
