package com.qccr.paycenter.model.dal.so.pay;

/**
 * 付款结果SO
 * @author denghuajun
 * date:2015年12月2日 下午6:36:46
 */
public class PayResultSO {
		
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

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
}
