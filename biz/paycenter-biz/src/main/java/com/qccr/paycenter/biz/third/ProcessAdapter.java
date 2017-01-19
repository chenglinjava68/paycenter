package com.qccr.paycenter.biz.third;

import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeResultSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.exception.PayCenterException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/18 15:48 $
 */
public abstract class ProcessAdapter {

    public RefundResultSO refund(RefundParamSO param) throws Exception {
        return null;
    }

    public PayResultSO pay(PayParamSO param) throws PayCenterException {
        return null;
    }

    public PayNotifySO payNotify(HttpServletRequest request, String partner) {
        return null;
    }

    public RefundNotifySO refundNotify(HttpServletRequest request, String partner) {
        return null;
    }

    public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
        return null;
    }

    public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
        return null;
    }

    public PayCloseResultSO payClose(PayCloseSO payCloseSO) {
        return null;
    }

    public String getPayCode() {
        return null;
    }

    public SignVerifyCodeResultSO getSignVerifyCode(SignVerifyCodeParamSO param) {
        return null;
    }

    public PayVerifyCodeResultSO getPayVerifyCode(PayVerifyCodeParamSO param) {
        return null;
    }

    /**
     * 签约---交通银行使用
     *
     * @param param
     * @return
     */
    public PayResultSO sign(SignParamSO param) {
        return null;
    }

    public PayNotifySO syncPay(PayParamSO param) throws PayCenterException {
        return null;
    }
}
