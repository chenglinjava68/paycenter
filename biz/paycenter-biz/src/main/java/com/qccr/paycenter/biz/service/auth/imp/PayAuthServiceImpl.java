package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.filter.PayChannelAuthFilter;
import com.qccr.paycenter.biz.service.auth.filter.PayParamAuthFilter;
import com.qccr.paycenter.facade.dal.ro.pay.PayParamRO;
import org.springframework.stereotype.Component;

/**
 * 支付验证服务
 * @author denghuajun
 * date:2015年12月17日 下午3:43:35
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component("payAuthService")
public class PayAuthServiceImpl extends AbstractAuthService<PayParamRO>{

	public PayAuthServiceImpl (){
		super();
		init();
	}

	@Override
	public void  init(){
		if(inited.compareAndSet(false, true)){
			filterChain.add(new PayChannelAuthFilter());
			filterChain.add(new PayParamAuthFilter());
		}
	}



	

}
