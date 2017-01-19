package com.qccr.paycenter.dal.domain.pay;

public class FacePayCancelResultSO {
	
	/**状态*/
	private int status;
	/**错误信息*/
	private String errMsg;
	/**支付编号*/
	private String payNo;
	/**备注*/
	private String remark;
	
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

}
