package com.qccr.paycenter.model.dal.so.pay.bocom;

import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/22 11:34 $
 */
public class BocomPayQueryParamSO extends PayQuerySO {

    private String traceNo;

    private String invioceNo;

    private String addData;

    private String  addDataField;

    private String carNo;

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getInvioceNo() {
        return invioceNo;
    }

    public void setInvioceNo(String invioceNo) {
        this.invioceNo = invioceNo;
    }

    public String getAddData() {
        return addData;
    }

    public void setAddData(String addData) {
        this.addData = addData;
    }

    public String getAddDataField() {
        return addDataField;
    }

    public void setAddDataField(String addDataField) {
        this.addDataField = addDataField;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
