package com.qccr.paycenter.biz.service.pay;

import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;

import java.util.concurrent.ExecutionException;

/**
 * Created by user on 2016/8/19.
 */
public interface SignService {

    /**
     * 交通银行获取签约验证码
     * @param signVerifyCodeParamSO
     * @return
     */
    PayResultSO signVerifyCode(SignVerifyCodeParamSO signVerifyCodeParamSO) throws Exception;

    /**
     * 交通银行第一次支付前的签约
     * @param signParamSO
     * @return
     */
    PayResultSO sign(SignParamSO signParamSO) throws ExecutionException, InterruptedException;

    PayResultSO doSignVerifyCode(SignVerifyCodeParamSO propertiesSo);
    PayResultSO doSign(SignParamSO propertiesSo);
    /**
     * 是否签约
     * @param userId
     * @param payChannel
     * @return
     */
    PayResultSO isSigned(String userId,  String payChannel);
}
