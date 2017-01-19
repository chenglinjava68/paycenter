package com.qccr.paycenter.biz.third.alipay.model;

/**
 * Created by user on 2016/5/27.
 */
public class AlipayResponse {

    protected   String code;
    protected String msg;
    protected String subCode;
    protected String subMsg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }
}
