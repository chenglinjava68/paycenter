package com.qccr.paycenter.biz.task.pay;

import com.qccr.paycenter.biz.service.pay.PayService;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.PayVerifyCodeParamSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.exception.PayCenterException;

import java.util.concurrent.Callable;

/**
 * 用于异步构建三方签约
 */
public class PayVerifyCodeCallable implements Callable<PayResultSO>{


	private PayService payService;

	private PayVerifyCodeParamSO propertiesSo;

	public PayVerifyCodeCallable(PayService payService, PayVerifyCodeParamSO propertiesSo){
		
		this.payService = payService;
		this.propertiesSo = propertiesSo;
		
	}

	@Override
	public PayResultSO call() throws Exception {
		
		return doPayVerifyCode(propertiesSo);
	}
	
	public PayResultSO doPayVerifyCode(PayVerifyCodeParamSO propertiesSo) throws PayCenterException{
		
		return  payService.doPayVerifyCode(propertiesSo);
	}

}
