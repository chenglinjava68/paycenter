package com.qccr.paycenter.biz.third.union.core;

import com.qccr.paycenter.biz.third.ProcessAdapter;
import com.qccr.paycenter.model.dal.so.notify.PayNotifySO;
import com.qccr.paycenter.model.dal.so.notify.RefundNotifySO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayCloseSO;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayQueryResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayQuerySO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.refund.RefundParamSO;
import com.qccr.paycenter.model.dal.so.refund.RefundResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackResultSO;
import com.qccr.paycenter.model.dal.so.refund.checkback.RefundCheckBackSO;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/18 16:23 $
 */
@Component
public class UnionProcessAdapter extends ProcessAdapter {

    @Resource
    private UnionPayProcess unionPayProcess;

    @Override
    public RefundResultSO refund(RefundParamSO param) throws Exception {
        return unionPayProcess.refund(param);
    }

    @Override
    public PayResultSO pay(PayParamSO param) throws PayCenterException {
        return unionPayProcess.pay(param);
    }


    @Override
    public PayNotifySO payNotify(HttpServletRequest request, String partner) {
        return unionPayProcess.payNotify(request,partner);
    }

    @Override
    public RefundNotifySO refundNotify(HttpServletRequest request, String partner) {
        return unionPayProcess.refundNotify(request, partner);
    }

    @Override
    public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
        return unionPayProcess.refundCheckBack(refundCheckBackSO);
    }

    @Override
    public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
        return unionPayProcess.queryPayOrder(payQuerySO);
    }

    @Override
    public PayCloseResultSO payClose(PayCloseSO payCloseSO) {
        return unionPayProcess.payClose(payCloseSO);
    }

    @Override
    public String getPayCode() {
        return unionPayProcess.getPayCode();
    }
}
