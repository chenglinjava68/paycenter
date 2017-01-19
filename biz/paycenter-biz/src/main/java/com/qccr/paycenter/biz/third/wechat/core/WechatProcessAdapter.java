package com.qccr.paycenter.biz.third.wechat.core;

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
 * @since $Revision:1.0.0, $Date: 2016/8/18 16:24 $
 */
@Component
public class WechatProcessAdapter extends ProcessAdapter {

    @Resource
    private WechatPayProcess wechatPayProcess;

    @Override
    public RefundResultSO refund(RefundParamSO param) throws Exception {

        return wechatPayProcess.refund(param);
    }

    @Override
    public PayResultSO pay(PayParamSO param) throws PayCenterException {
        return wechatPayProcess.pay(param);
    }


    @Override
    public PayNotifySO payNotify(HttpServletRequest request, String partner) {
        return wechatPayProcess.payNotify(request,partner);
    }

    @Override
    public RefundNotifySO refundNotify(HttpServletRequest request, String partner) {
        return wechatPayProcess.refundNotify(request, partner);
    }

    @Override
    public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
        return wechatPayProcess.refundCheckBack(refundCheckBackSO);
    }

    @Override
    public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
        return wechatPayProcess.queryPayOrder(payQuerySO);
    }

    @Override
    public PayCloseResultSO payClose(PayCloseSO payCloseSO) {
        return wechatPayProcess.payClose(payCloseSO);
    }

    @Override
    public String getPayCode() {
        return wechatPayProcess.getPayCode();
    }
}
