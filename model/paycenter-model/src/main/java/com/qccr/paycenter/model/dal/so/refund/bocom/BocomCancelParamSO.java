package com.qccr.paycenter.model.dal.so.refund.bocom;

import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/19 10:02 $
 */
public class BocomCancelParamSO extends RefundParamSO {

    private String authNo;

    private String invioceNo;

    private String cardNo;

    /**
     * 流水号，短流水，
     */
    private String traceNo;

    private String addData;

    private String expiryDate;

    public String getAuthNo() {
        return authNo;
    }

    public void setAuthNo(String authNo) {
        this.authNo = authNo;
    }

    public String getInvioceNo() {
        return invioceNo;
    }

    public void setInvioceNo(String invioceNo) {
        this.invioceNo = invioceNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getAddData() {
        return addData;
    }

    public void setAddData(String addData) {
        this.addData = addData;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
