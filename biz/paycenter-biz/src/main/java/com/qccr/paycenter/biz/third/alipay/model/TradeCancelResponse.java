package com.qccr.paycenter.biz.third.alipay.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/7/5 12:13 $
 */
public class TradeCancelResponse extends AlipayResponse{

    @JSONField(name="out_trade_no")
    private String outTradeNo;

    @JSONField(name="trade_no")
    private String tradeNo;

    @JSONField(name="retry_flag")
    private String retryFlag;

    @JSONField(name="action")
    private String action;

    @JSONField(name = "code")
    private String code;
    @JSONField(name = "msg")
    private String msg;
    @JSONField(name = "sub_code")
    private String subCode;
    @JSONField(name = "sub_msg")
    private String subMsg;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getRetryFlag() {
        return retryFlag;
    }

    public void setRetryFlag(String retryFlag) {
        this.retryFlag = retryFlag;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getSubCode() {
        return subCode;
    }

    @Override
    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    @Override
    public String getSubMsg() {
        return subMsg;
    }

    @Override
    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }
}
