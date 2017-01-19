package com.qccr.paycenter.biz.task.pay;

import java.util.concurrent.Callable;
import com.qccr.paycenter.biz.service.pay.PayService;
import com.qccr.paycenter.model.dal.so.pay.PayParamSO;
import com.qccr.paycenter.model.dal.so.pay.PayResultSO;
import com.qccr.paycenter.model.exception.PayCenterException;
/**
 * 用于异步构建三方支付
 * @author denghuajun
 * date:2015年12月11日 下午3:45:32
 */
public class PayCallable implements Callable<PayResultSO>{
	
	
	private PayService payService;
	
	private PayParamSO propertiesSo;
	
	public PayCallable(PayService payService,PayParamSO propertiesSo){
		
		this.payService = payService;
		this.propertiesSo = propertiesSo;
		
	}

	@Override
	public PayResultSO call() throws Exception {
		
		return doPay(propertiesSo);
	}
	
	public PayResultSO doPay(PayParamSO propertiesSo) throws PayCenterException{
		
		return  payService.doPay(propertiesSo);
	}

}
