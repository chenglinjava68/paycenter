package com.qccr.paycenter.biz.third.alipay.core;

import com.qccr.paycenter.biz.third.ProcessAdapter;
import com.qccr.paycenter.dal.dao.auth.FacepayAuthDao;
import com.qccr.paycenter.dal.domain.auth.FacepayAuthDO;
import com.qccr.paycenter.facade.constants.PayConstants;
import com.qccr.paycenter.facade.statecode.PayCenterStateCode;
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
import com.qccr.paycenter.model.enums.FacepayAuthEnum;
import com.qccr.paycenter.model.exception.PayCenterException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/18 15:49 $
 */
@Component
public class AliPayProcessAdapter extends ProcessAdapter {

    @Resource
    private AliPayProcess aliPayProcess;
    @Resource
    private FacepayAuthDao facepayAuthDao;

    @Override
    public RefundResultSO refund(RefundParamSO param) throws Exception {
        return aliPayProcess.refund(param);
    }

    @Override
    public PayResultSO pay(PayParamSO param) throws PayCenterException {
        return aliPayProcess.pay(param);
    }

    @Override
    public PayNotifySO syncPay(PayParamSO param) throws PayCenterException {
        if(PayConstants.ALI_PAY_FACEPAY.equals(param.getPayType())) {//获取当面付授权TOKDN
            FacepayAuthDO facepayAuthDO = facepayAuthDao.findNormalBySuperUserId(Long.parseLong(param.getDebitUserId()));
            if(facepayAuthDO != null) {
                param.setAppAuthToken(facepayAuthDO.getAppAuthToken());
            }else {
                FacepayAuthDO tmp = facepayAuthDao.findOneBySuperUserId(Long.parseLong(param.getDebitUserId()));
                if(tmp != null) {
                    if(FacepayAuthEnum.TIMEOUT.getValue() == tmp.getStatus()) {
                        throw new PayCenterException(PayCenterStateCode.TIMEOUT_AUTH_TOKEN_ERROR, "当前用户TOKEN已过期");
                    }else {
                        throw new PayCenterException(PayCenterStateCode.NULL_AUTH_TOKEN_ERROR, "当前用户未授权");
                    }
                }else {
                    throw new PayCenterException(PayCenterStateCode.NULL_AUTH_TOKEN_ERROR, "当前用户未授权");
                }
            }
        }
        return aliPayProcess.syncPay(param);
    }


    @Override
    public PayNotifySO payNotify(HttpServletRequest request, String partner) {
        return aliPayProcess.payNotify(request,partner);
    }

    @Override
    public RefundNotifySO refundNotify(HttpServletRequest request, String partner) {
        return aliPayProcess.refundNotify(request, partner);
    }

    @Override
    public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
        return aliPayProcess.refundCheckBack(refundCheckBackSO);
    }

    @Override
    public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
        return aliPayProcess.queryPayOrder(payQuerySO);
    }

    @Override
    public PayCloseResultSO payClose(PayCloseSO payCloseSO) {
        return aliPayProcess.payClose(payCloseSO);
    }

    @Override
    public String getPayCode() {
        return aliPayProcess.getPayCode();
    }
}
