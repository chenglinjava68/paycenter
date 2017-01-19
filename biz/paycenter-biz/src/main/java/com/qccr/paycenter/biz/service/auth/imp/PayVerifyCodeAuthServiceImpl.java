package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.filter.PayVerifyCodeAuthFilter;
import com.qccr.paycenter.facade.dal.ro.pay.PayVerifyCodeParamRO;
import org.springframework.stereotype.Component;

/**
 * 支付验证码
 * @author denghuajun
 * date:2015年12月17日 下午3:43:35
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component("payVerifyCodeAuthService")
public class PayVerifyCodeAuthServiceImpl extends AbstractAuthService<PayVerifyCodeParamRO>{

	public PayVerifyCodeAuthServiceImpl(){
		super();
		init();
	}

	@Override
	public void  init(){
		if(inited.compareAndSet(false, true)){
			filterChain.add(new PayVerifyCodeAuthFilter());
		}
	}



	

}
