package com.qccr.paycenter.biz.service.auth.imp;

import com.qccr.paycenter.biz.service.auth.filter.SignVerifyCodeAuthFilter;
import com.qccr.paycenter.facade.dal.ro.pay.SignVerifyCodeParamRO;
import org.springframework.stereotype.Component;

/**
 * 签约验证码
 * @author denghuajun
 * date:2015年12月17日 下午3:43:35
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component("signVerifyCodeAuthService")
public class SignVerifyCodeAuthServiceImpl extends AbstractAuthService<SignVerifyCodeParamRO>{

	public SignVerifyCodeAuthServiceImpl(){
		super();
		init();
	}

	@Override
	public void  init(){
		if(inited.compareAndSet(false, true)){
			filterChain.add(new SignVerifyCodeAuthFilter());
		}
	}



	

}
