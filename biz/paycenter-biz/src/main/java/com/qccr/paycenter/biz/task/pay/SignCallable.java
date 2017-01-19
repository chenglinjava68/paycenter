package com.qccr.paycenter.biz.task.pay;

import com.qccr.paycenter.biz.service.pay.SignService;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.dal.so.pay.SignParamSO;
import com.qccr.paycenter.model.exception.PayCenterException;

import java.util.concurrent.Callable;

/**
 * 用于异步构建三方支付
 * @author denghuajun
 * date:2015年12月11日 下午3:45:32
 */
public class SignCallable implements Callable<PayResultSO>{
	private SignService signService;

	private SignParamSO propertiesSo;

	public SignCallable(SignService signService, SignParamSO propertiesSo){
		
		this.signService = signService;
		this.propertiesSo = propertiesSo;
		
	}

	@Override
	public PayResultSO call() throws Exception {
		return doSign(propertiesSo);
	}
	
	public PayResultSO doSign(SignParamSO propertiesSo) throws PayCenterException{
		
		return  signService.doSign(propertiesSo);
	}

}
