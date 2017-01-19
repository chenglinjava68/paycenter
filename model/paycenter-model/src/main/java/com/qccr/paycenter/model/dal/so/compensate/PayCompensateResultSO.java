package com.qccr.paycenter.model.dal.so.compensate;

/**
 * author: denghuajun
 * date: 2016/3/7 11:32
 * version: v1.0.0
 */
public class PayCompensateResultSO {

    /**
     * 支付中心，退款单号
     */
    private String compensateNo;
    /**
     * 处理是否成功
     */
    private boolean  success;

    /**
     * 业务编号，如：订单编号
     */
    private String businessNo;

    /**
     * 业务类型，如1-订单业务 2- 其他业务等
     */
    private String businessType;

    /**
     * 退款金额
     */
    private int refundFee;

    /**
     *退款状态，只针对退款同步通知
     *0-进行中，1-退款完成，2-失败
     */
    private int status;

    /**
     *三方退款编号
     */
    private String refundBillNo;
    /**
     * 错误信息
     */
    private String errMsg;

    public String getCompensateNo() {
        return compensateNo;
    }

    public void setCompensateNo(String compensateNo) {
        this.compensateNo = compensateNo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public int getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(int refundFee) {
        this.refundFee = refundFee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRefundBillNo() {
        return refundBillNo;
    }

    public void setRefundBillNo(String refundBillNo) {
        this.refundBillNo = refundBillNo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
