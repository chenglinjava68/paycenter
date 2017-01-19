package com.qccr.paycenter.biz.third;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.qccr.paycenter.dal.domain.pay.FacePayCancelResultSO;
import com.qccr.paycenter.dal.domain.pay.FacePayQueryResultSO;
import com.qccr.paycenter.model.dal.so.auth.AuthResultSO;
import com.qccr.paycenter.model.dal.so.notify.AuthNotifySO;
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

/**
 * @author dhj
 * @since $Revision:1.0.0, $Date: 2016/8/19 9:38 $
 */
public abstract class AbstractProcess implements PayProcess{

    @Override
    public RefundResultSO refund(RefundParamSO param) throws Exception {
        return null;
    }

    @Override
    public PayResultSO pay(PayParamSO param) throws PayCenterException {
        return null;
    }

    @Override
    public PayResultSO webPay(PayParamSO param) throws Exception {
        return null;
    }

    @Override
    public PayResultSO appPay(PayParamSO param) throws Exception {
        return null;
    }

    @Override
    public PayNotifySO payNotify(HttpServletRequest request, String partner) {
        return null;
    }

    @Override
    public RefundNotifySO refundNotify(HttpServletRequest request, String partner) {
        return null;
    }

    @Override
    public RefundCheckBackResultSO refundCheckBack(RefundCheckBackSO refundCheckBackSO) {
        return null;
    }

    @Override
    public PayQueryResultSO queryPayOrder(PayQuerySO payQuerySO) {
        return null;
    }

	/**
	 * 查询支付订单
	 */
    public FacePayQueryResultSO queryFacePayPayOrder(String payNo,String appAuthToken){
		return null;
	}

    @Override
    public PayCloseResultSO payClose(PayCloseSO payCloseSO) {
        return null;
    }

    @Override
    public String getPayCode() {
        return null;
    }

    @Override
    public SignVerifyCodeResultSO getSignVerifyCode(SignVerifyCodeParamSO param) {
        return null;
    }

    @Override
    public PayResultSO sign(SignParamSO param) {
        return null;
    }

    @Override
    public PayVerifyCodeResultSO getPayVerifyCode(PayVerifyCodeParamSO param) {
        return null;
    }

    @Override
    public PayNotifySO payNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
        return null;
    }

    @Override
    public RefundNotifySO refundNotify(String payChannel, String payType, HttpServletRequest request, String partnre) {
        return null;
    }

    /**
     * 当面付授权回调通知
     * @param authChannel
     * @param authType
     * @param request
     * @return
     */
    @Override
    public AuthNotifySO authNotify(String authChannel, String authType, HttpServletRequest request) throws UnsupportedEncodingException {
        return null;
    }


    /**
     * 生成授权回调地址
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public String createAuthUrl(String userId) throws Exception {
        return null;
    }

    /**
     * 查询某个应用授权AppAuthToken的授权信息
     *
     * @param appAuthToken
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public AuthResultSO openAuthTokenAppQuery(String appAuthToken) throws UnsupportedEncodingException {
        return null;
    }
    @Override
    public  FacePayCancelResultSO cancelFacePayTrade(String payNo,String appAuthToken){
    	return null;
    }

    @Override
    public PayNotifySO syncPay(PayParamSO param) throws PayCenterException {
        return null;
    }
}
