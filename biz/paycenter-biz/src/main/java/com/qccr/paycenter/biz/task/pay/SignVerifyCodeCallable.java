package com.qccr.paycenter.biz.task.pay;

import com.qccr.paycenter.biz.service.pay.SignService;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignVerifyCodeParamSO;
import com.qccr.paycenter.model.exception.PayCenterException;

import java.util.concurrent.Callable;

/**
 * 用于异步构建三方签约
 */
public class SignVerifyCodeCallable implements Callable<PayResultSO>{
	private SignService signService;

	private SignVerifyCodeParamSO propertiesSo;

	public SignVerifyCodeCallable(SignService signService, SignVerifyCodeParamSO propertiesSo){
		
		this.signService = signService;
		this.propertiesSo = propertiesSo;
		
	}

	@Override
	public PayResultSO call() throws Exception {
		
		return doSignVerifyCode(propertiesSo);
	}
	
	public PayResultSO doSignVerifyCode(SignVerifyCodeParamSO propertiesSo) throws PayCenterException{
		
		return  signService.doSignVerifyCode(propertiesSo);
	}

}
